# 网上书城 · 现代前端重构（SPA）

在保留原有 JSP 后端页面的基础上，新增了一套前后端分离的 Vue 3 单页应用：

- **前端**：`web/` —— Vue 3 + Vite + TypeScript + Tailwind CSS + Pinia + Vue Router + Element Plus + axios
- **后端**：新增 `com.wrf.web.api` 包提供 REST JSON 接口（`/api/**`），旧 JSP 页面原样保留
- **部署**：构建产物输出到 `src/main/webapp/app/`，由 Tomcat 直接托管于 `<context>/app/`

## 页面清单

| 路由（相对 `/app`） | 说明 |
| --- | --- |
| `/books` | 书城首页：书籍列表、价格区间筛选、分页、加入购物车 |
| `/login`、`/register` | 登录 / 注册（图形验证码，逻辑与旧版一致） |
| `/cart` | 购物车：改数量、删除、清空、去结算 |
| `/orders`、`/orders/:orderId` | 我的订单 / 订单详情 / 确认收货 |
| `/admin/books` | 后台书籍管理：分页、新增、编辑、删除（仅 admin） |
| `/admin/orders` | 后台订单管理：发货、订单明细（仅 admin） |

## 开发

前置：Node ≥ 18（本机 `~/.nvm/versions/node/v24.15.0`）、Java 11、Maven、MySQL（库名 `book`，
见 `src/main/resources/hikari.properties`）。

```bash
# 1. 启动后端（与原项目相同：IDEA 中 Tomcat 运行，context 任意，默认 8080）
cd web
npm install        # 首次
npm run dev        # http://localhost:5173（/api 与 /kaptcha.jpg 代理到 :8080）
```

开发模式下前后端跨端口，`vite.config.ts` 的 proxy + 后端 `ApiCORSFilter`（回显 Origin、
`/api/*` 生效）共同保证 session Cookie 正常工作。

## 构建与部署

```bash
cd web && npm run build   # 产物 → src/main/webapp/app/
```

然后把 WAR 部署到 Tomcat（或继续用 IDEA 的 war exploded artifact）。访问入口：

- `http://localhost:8080/<context>/app/` —— 新版 SPA
- `http://localhost:8080/<context>/`     —— 旧版 JSP 首页（页头有"新版界面"链接）

部署要点（已处理，仅备查）：

- `src/main/java/com/wrf/web/api/SpaController.java`：`/app/**` 回退 forward 到 `app/index.html`，
  保证前端 history 路由直接访问/刷新不 404
- `WEB-INF/web.xml`：追加了 `*.html *.js *.css *.png *.jpg *.gif *.woff* *.svg *.ico *.webp *.ttf`
  的 DefaultServlet 映射，SPA 静态资源不进 DispatcherServlet
- `ApiCORSFilter` 只在 `Origin` 命中 `/api/*` 时回显，旧页面不受影响
- 构建的 `base: './'`：index.html 位于 `<context>/app/` 下，资源相对解析，任意 context path 均可

## REST 接口（`/api/**`，JSON）

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录 `{username,password,code}` → `{token,user}` |
| POST | `/api/auth/register` | 注册 `+email`，成功后自动登录 |
| GET | `/api/auth/me` | 当前会话用户（401 未登录） |
| POST | `/api/auth/logout` | 登出（session 销毁） |
| GET | `/api/books?min=&max=&page=&pageSize=` | 书籍分页（按价格筛选） |
| GET | `/api/books/{id}` | 书籍详情 |
| GET | `/api/cart` | 购物车 |
| POST | `/api/cart/items` | 加购 `{id}` |
| PUT | `/api/cart/items/{id}` | 改数量 `{count}` |
| DELETE | `/api/cart/items/{id}`、`/api/cart` | 删除条目 / 清空 |
| POST | `/api/cart/checkout` | 结算下单 → `{orderId}` |
| GET | `/api/orders/my`、`/api/orders/my/{orderId}/items` | 我的订单 / 明细 |
| POST | `/api/orders/my/{orderId}/receive` | 确认收货 |
| GET | `/api/orders/all`、`/api/orders/{orderId}/items` | 管理端订单列表 / 明细（admin） |
| POST | `/api/orders/{orderId}/send` | 发货（admin） |
| GET/POST/PUT/DELETE | `/api/admin/books[...]` | 管理端书籍 CRUD（admin） |

认证方式：**session 登录**（与旧版 JSP 共享同一 JSESSIONID）。登录接口返回的 `token`
仅用于前端"本会话已登录"的记忆与刷新恢复（`/api/auth/me` 才是权威）。购物车仍挂在
session 上，旧版 JSP 与新前端加购的商品互相可见（同一会话）。

统一错误格式：`{ "code": "10002", "message": "用户名或密码错误" }`，
HTTP 状态 400/401/403/404/500 对应 `ApiCodes`。

## 已知取舍 / 后续可改进

- `AuthSupport` 的 token 表在进程内存中，多实例部署需换 Redis
- 管理端权限沿用旧规则：`username == "admin"`
- 未做表单密码加密（沿用旧库明文），建议后续加 BCrypt
- Element Plus 整包引入，vendor chunk 约 365KB gzip；可改按需引入进一步减小
