import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import mitt from "mitt";
import store from './store'
import moment from 'moment'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


const app = createApp(App);

app
    .use(router)
    .use(store)
    .use(ElementPlus)
    .use(moment)
    .mount('#app');

app.config.globalProperties.$bus = mitt();
app.config.globalProperties.$moment = moment;