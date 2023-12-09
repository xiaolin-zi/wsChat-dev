import axios from 'axios'
import cookie from "js-cookie";
import router from "@/router";

// 创建axios实例
const service = axios.create({
    baseURL: 'http://localhost:8080', // api的base_url
    timeout: 30000 // 请求超时时间
})


service.interceptors.request.use(
    config => {
        //debugger
        if (cookie.get('ws_token')) {
            //第三步把获取的cookie值放到header
            config.headers['token'] = cookie.get('ws_token');
        }
        return config
    },
    err => {
        return Promise.reject(err);
    }
)

// http response 拦截器
service.interceptors.response.use(
    response => {
        //debugger
        if (response.data.code == 10000) {
            console.log("response.data.resultCode是10000")
            // 返回 错误代码-1 清除ticket信息并跳转到登录页面
            //debugger
            // window.location.href = "/login"
            router.replace({
                path: 'login',
                query: {redirect: router.currentRoute.fullPath}
                //携带路由从什么地方失效的
            }).then(r => {})
            return
        } else {
            return response
        }
    },
    error => {
        return Promise.reject(error.response) // 返回接口返回的错误信息
    });
export default service
