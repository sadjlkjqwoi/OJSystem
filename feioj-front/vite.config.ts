const { defineConfig } = require('@vue/cli-service')
export default defineConfig({
    server: {
      proxy: {
        '^/api/': {
          target: 'http://locahost:8081',
          changeOrigin: true
        },
      }
    },
  }
) 