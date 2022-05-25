import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import mitt from "mitt";
import store from './store'
import elementUI from 'element-ui'
import moment from 'moment'
import 'element-ui/lib/theme-chalk/index.css'


const app = createApp(App);

app
    .use(router)
    .use(store)
    .use(elementUI)
    .use(moment)
    .mount('#app');

app.config.globalProperties.$bus = mitt();
app.config.globalProperties.$moment = moment;