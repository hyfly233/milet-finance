import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import mitt from "mitt";
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


const app = createApp(App);

app
    .use(router)
    .use(store)
    .use(ElementPlus)
    .mount('#app');

app.config.globalProperties.$bus = mitt();