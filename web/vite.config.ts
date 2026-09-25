import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// 开发时代理到后端，VITE_API_TARGET = http://<host>:<port><contextPath>
//   （默认 http://localhost:8080/bookStore，即 IDEA Tomcat 的 context）：
//   - /api/**       走 SPA 专用的 REST 接口
//   - /kaptcha.jpg  验证码图片（web.xml 中的 KaptchaServlet）
// 后端 contextPath 也可能为空，此时 target 直接写 http://localhost:8080
// 生产构建产物输出到 src/main/webapp/app/，由 Tomcat 直接托管于 <context>/app/
//
// contextPath 的拼接方式：target 只写 host:port（target 自带的 path 会被
// http-proxy 在一次请求里重复拼接），改用 configure/proxyReq 钩子在
// 发出请求前把 contextPath 前缀补到 path 上。contextPath 为空时无需处理。
const backendUrl = new URL(process.env.VITE_API_TARGET || 'http://localhost:8080/bookStore')
const backendTarget = `${backendUrl.protocol}//${backendUrl.host}`
const backendBase = backendUrl.pathname.replace(/\/+$/, '') // 如 /bookStore，可能为 ''

function makeProxy() {
  return {
    target: backendTarget,
    changeOrigin: true,
    // http-proxy 的 Server.on 重载签名复杂，这里直接 any 避免类型打扰
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    configure: (proxy: any) => {
      /** 补 contextPath 前缀；保留 query/fragment。 */
      if (backendBase) {
        proxy.on('proxyReq', (proxyReq: any) => {
          const raw: string = proxyReq.path || '/'
          const qIdx = raw.indexOf('?')
          const hashIdx = raw.indexOf('#')
          const cut = [qIdx, hashIdx].filter((i) => i >= 0).sort((a, b) => a - b)[0]
          const path = cut === undefined ? raw : raw.slice(0, cut)
          const suffix = cut === undefined ? '' : raw.slice(cut)
          proxyReq.path = backendBase + path + suffix
        })
      }

      /**
       * 后端把 JSESSIONID 的 `Path` 设成 Tomcat contextPath（如 /bookStore）。
       * 在 vite 开发代理下,cookie 落到 :5173 域,而 :5173 上不存在 /bookStore/... 路径,
       * 浏览器认为 Path 不匹配,后续 /api/** 就不会携带 JSESSIONID —— 图像可以显示
       * 但登录校验失败。故归一化开发代理下的 cookie Path 为 `/`。
       * 生产由 Tomcat 直接托管 webapp/app/，cookie Path 与 context 一致，不受影响。
       */
      proxy.on('proxyRes', (proxyRes: any) => {
        const raw = proxyRes.headers['set-cookie']
        if (!raw) return
        const cookies: string[] = Array.isArray(raw) ? raw : [raw]
        proxyRes.headers['set-cookie'] = cookies.map((c: string) => c.replace(/Path=[^;,]*/gi, 'Path=/'))
      })
    },
  }
}

export default defineConfig({
  plugins: [vue()],
  // SPA 入口在 <context>/app/ 下，资源用相对路径解析，任意 context 都正确
  base: './',
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/api': makeProxy(),
      '/kaptcha.jpg': makeProxy(),
    },
  },
  build: {
    // 产物输出到 webapp/app/，Tomcat 直接托管于 <context>/app/。
    // 用绝对路径 + emptyOutDir 确保每次全量重写，不残留旧 hash 的 chunk
    outDir: fileURLToPath(new URL('../src/main/webapp/app', import.meta.url)),
    emptyOutDir: true,
    rollupOptions: {
      output: {
        // 库单独打块，利于缓存
        manualChunks: {
          vendor: ['vue', 'vue-router', 'pinia', 'axios', 'dayjs', 'element-plus'],
        },
      },
    },
  },
})
