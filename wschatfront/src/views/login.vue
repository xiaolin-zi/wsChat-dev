<template>
  <div class="login">
    <div class="login__container">
      <h1 class="login__title">WSChat</h1>
      <form class="login__form" @submit.prevent="login">
        <input
          type="text"
          class="login__input"
          placeholder="Account"
          v-model="loginInfo.account"
        />
        <input
          type="password"
          class="login__input"
          placeholder="Password"
          v-model="loginInfo.password"
        />
        <button type="submit" class="login__button">Login</button>
        <!--注册-->
        <router-link to="/register" >
          去注册
        </router-link>
      </form>
    </div>
  </div>
</template>
<script>
import loginApi from "@/api/login";
import cookie from "js-cookie";
export default {
  name: "toLogin",
  data() {
    return {
      loginInfo:{
        account: "",
        password: "",
      }

    };
  },
  methods: {
    //登录
    login() {
      console.log(this.loginInfo);
      loginApi.submitLogin(this.loginInfo).then((res) => {
        console.log(res);
        if(res.data.success){
          this.$message({
            type: "success",
            message: "登录成功！",
          });
          //第二步获取token字符串，放到cookie
          //名称、值、作用范围
          cookie.set("ws_token", res.data.data.token);
          //第四步调用接口根据token获取用户信息，首页展示
          loginApi.getLoginUserInfo().then((res) => {
            this.loginInfo = res.data.data.userInfo;
            this.loginInfo = JSON.stringify(this.loginInfo);
            //获取返回的信息，放到cookie里面
            cookie.set("ws_userInfo", this.loginInfo);
            setTimeout(() => {
              //跳转页面
              window.location.href = "/index";
            }, 500);
          });
        }else {
          this.$message({
            type: "error",
            message: "账号或密码错误！",
          });
        }
      });
    },
  },
};
</script>
<style>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login__container {
  width: 400px;
  padding: 20px;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.login__title {
  margin: 0;
  margin-bottom: 20px;
  text-align: center;
  font-size: 30px;
  color: #333;
}

.login__form {
  display: flex;
  flex-direction: column;
}

.login__input {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.login__button {
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #333;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
}

.login__button:hover {
  background-color: #555;
}

.login__button:active {
  background-color: #222;
}

.login__button:focus {
  outline: none;
}

.login__button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.login__button:disabled:hover {
  background-color: #ccc;
}

.login__button:disabled:active {
  background-color: #ccc;
}

.login__button:disabled:focus {
  background-color: #ccc;
}








</style>
