<template>
  <div class="register">
    <div class="register-form">
      <h3>注册</h3>
      <form>
        <el-form :model="registerInfo" ref="registerInfo" label-width="80px">
          <el-form-item label="账号" prop="account">
            <el-input v-model="registerInfo.account" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="registerInfo.password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="registerInfo.nickname" placeholder="请输入昵称"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="registerInfo.email" placeholder="请输入邮箱"></el-input>
          </el-form-item>
          <el-form-item label="验证码" prop="code">
            <el-input v-model="registerInfo.code" placeholder="请输入验证码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getCode">获取验证码</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="register">注册</el-button>
          </el-form-item>
        </el-form>
      </form>
    </div>
  </div>
</template>
<script>
import loginApi from "@/api/login";
export default {
  name: "toRegister",
  data() {
    return {
      registerInfo: {
        account: "",
        password: "",
        nickname: "",
        email: "",
        code: "",
      }
    };
  },
  methods: {
    //注册
    register() {
      console.log(this.registerInfo);
      loginApi.submitRegister(this.registerInfo).then((res) => {
        console.log(res);
        if(res.data.success){
          this.$message({
            type: "success",
            message: "注册成功！",
          });
          this.$router.push("/login");
        }else{
          this.$message({
            type: "error",
            message: res.data.message,
          });
        }
      });
    },
    //获取验证码
    getCode() {
      loginApi.sendCode(this.registerInfo.email).then((res) => {
        console.log(res);
        if(res.data.success){
          this.$message({
            type: "success",
            message: "验证码发送成功！",
          });
        }else{
          this.$message({
            type: "error",
            message: res.data.message,
          });
        }
      })
    },
  },
};
</script>
<style scoped>
.register {
  width: 100%;
  height: 100vh;
  background-color: #f5f5f5;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-form {
  width: 400px;
  height: 500px;
  background-color: #fff;
  border-radius: 10px;
  padding: 20px;
}

.register-form h3 {
  text-align: center;
  margin-bottom: 20px;
}

.register-form form {
  width: 100%;
  height: 100%;
}

.register-form form .el-form-item {
  margin-bottom: 20px;
}

.register-form form .el-form-item .el-input {
  width: 100%;
}

.register-form form .el-form-item .el-button {
  width: 100%;
}

.register-form form .el-form-item .el-button button {
  width: 100%;
}

.register-form form .el-form-item .el-button button:hover {
  background-color: #333;
}

.register-form form .el-form-item .el-button button:focus {
  background-color: #333;
}

.register-form form .el-form-item .el-button button:active {
  background-color: #333;
}

.register-form form .el-form-item .el-button button:visited {
  background-color: #333;
}

.register-form form .el-form-item .el-button button:link {
  background-color: #333;
}


</style>
