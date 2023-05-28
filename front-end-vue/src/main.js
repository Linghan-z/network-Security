import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import router from './router'
import store from "./store"
import './assets/global.css'
import request from "@/utils/request";
import axios from 'axios';

Vue.config.productionTip = false

Vue.use(ElementUI,{size: "mini"});

Vue.prototype.request=request
// Vue.prototype.axios=request

axios.interceptors.response.use(
    response => {
        // 处理响应
        return response;
    },
    error => {
        console.log(error); // 自定义处理错误的方式
        return Promise.reject(error); // 防止错误信息弹出
    }
);

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
