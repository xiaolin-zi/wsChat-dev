<template>
  <div class="layout">
    <el-container>
      <el-header>
        <!--导航栏和个人头像-->
        <div class="header">
          <div class="header__left">
            <h1 class="header__title">WSChat</h1>
            <el-menu
                class="header__menu"
                mode="horizontal"
                :default-active="this.$route.path"
                router
            >
              <el-menu-item index="/index">
                <i class="el-icon-s-home"></i>
                <span>首页</span>
              </el-menu-item>
              <el-menu-item index="/chat">
                <i class="el-icon-chat-dot-round"></i>
                <span>聊天</span>
              </el-menu-item>
            </el-menu>
          </div>
          <div class="header__right">
            <el-dropdown trigger="click">
              <span class="el-dropdown-link">
                <el-avatar
                    shape="square"
                    :src="userInfo.avatar"
                    alt="avatar"
                >
                </el-avatar>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="toUserInfo()">
                  <i class="el-icon-user-solid"></i>
                  <span>个人资料</span>
                </el-dropdown-item>


                <el-dropdown-item @click.native="logout">
                  <i class="el-icon-switch-button"></i>
                  <span>退出登录</span>
                </el-dropdown-item>

              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
      <!-- 公共底引入 -->
      <el-footer>

      </el-footer>
    </el-container>
  </div>
</template>


<script>
import cookie from "js-cookie";

export default {
  name: 'index',

  data() {
    return {
      userInfo: {},

    }
  },
  created() {
    this.showInfo();
  },
  methods: {
    logout() {
      //提示是否退出
      this.$confirm("确定要退出登录吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
            //清空cookie
            cookie.set("ws_token", "");
            cookie.set("ws_userInfo", "");
            //跳转到登录页
            window.location.href = "/";
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消退出",
        });
      });
    },
    toUserInfo() {
      this.$message({
        type: "success",
        message: "个人资料功能开发中！",
      });
    },

    showInfo() {
      const userStr = cookie.get("ws_userInfo");
      //把字符串转换成json对象
      if (userStr) {
        this.userInfo = JSON.parse(userStr);
      }
      // console.log(this.userInfo);
    },

  }
}
</script>

<style>
.layout {
  height: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header__left {
  display: flex;
  align-items: center;
}

.header__title {
  margin: 0;
  font-size: 24px;
  color: #fff;
}

.header__menu {
  margin-left: 20px;
}

.header__avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.header__right {
  display: flex;
  align-items: center;
}


</style>

