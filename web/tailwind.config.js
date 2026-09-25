/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,ts,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        sans: [
          '"PingFang SC"',
          '"Hiragino Sans GB"',
          '"Microsoft YaHei"',
          'system-ui',
          '-apple-system',
          'sans-serif',
        ],
      },
      colors: {
        brand: {
          50: '#eef4ff',
          100: '#d9e6ff',
          200: '#bcd4ff',
          300: '#8eb8ff',
          400: '#558fff',
          500: '#2f6bff',
          600: '#1f4fe6',
          700: '#1b3fbc',
          800: '#1b3698',
          900: '#1b3075',
        },
      },
    },
  },
  // Tailwind v3 与 Element Plus 共存：Element Plus 组件样式优先级更高
  corePlugins: {
    preflight: false,
  },
  plugins: [],
}
