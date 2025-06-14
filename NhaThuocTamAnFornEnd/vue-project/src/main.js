// import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// Thêm bootstrap vào vuejs
import 'bootstrap/dist/css/bootstrap.min.css'
import * as bootstrap from 'bootstrap/dist/js/bootstrap.bundle.min.js'

// Đây là phần cần thêm (chính là lời giải)
window.bootstrap = bootstrap

const app = createApp(App)

app.use(router)

app.mount('#app')
