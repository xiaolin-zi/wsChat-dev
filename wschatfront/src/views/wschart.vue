<template>
  <!--对话总体-->
  <div class="mess">
    <!--菜单-->
    <div class="mess_user_btn">
      <div class="mess_icon_logo">
        <i class="iconfont icon-chart-logo"></i>
      </div>
      <div class="user_addfrid" @click="relational()">
        <el-badge :is-dot="this.pendingList.length > 0 ? true : false" class="icon-friend-add-badge">
          <i class="iconfont icon-friend-add"></i>
        </el-badge>
      </div>
      <div class="mess_icon_chart" @click="noused()">
        <i class="iconfont icon-chart"></i>
      </div>
      <div class="mess_icon_figures" @click="noused()">
        <i class="iconfont icon-figures"></i>
      </div>
      <div class="mess_icon_circle" @click="noused()">
        <i class="iconfont icon-circle"></i>
      </div>
      <!--用户本人-->
      <div class="user_icon">
        <el-avatar :size="35" :src="userAvatar"></el-avatar>
      </div>
    </div>
    <!--用户列表-->
    <div class="mess_user_list">
      <!--用户本人-->
      <div class="user_head">
        <div class="user_head_title">
          <span>Chats</span>
        </div>
      </div>
      <!--其他用户或群-->
      <div class="user_list">
        <div v-for="(item, index) in userList" :key="index" @click="showmessdlog(item)" class="user_list_item">
          <el-badge :hidden="messIsReadList[item.id] !== undefined ? (messIsReadList[item.id].length > 0 ? false : true) : true" :value="messIsReadList[item.id] !== undefined ? messIsReadList[item.id].length : 0" :max="99" class="user_list_badge">
          </el-badge>
          <el-avatar :size="40" v-if="item.avatar_url !== '' & item.avatar_url !== null" :src="oss_url + item.avatar_url" style="margin: 5px;margin-left: 20px;"></el-avatar>
          <el-avatar :size="40" v-else :src="require('@/assets/image/avatar.jpg')" style="margin: 5px;margin-left: 20px;"></el-avatar>
          <div style="margin-left: 15px;">
            <p style="font-size: 14px;margin-top: 0px;margin-bottom: 0px;">{{item.name}}</p>
            <p v-if="messList[item.id] !== undefined ? messList[item.id][messList[item.id].length-1].type !== null & messList[item.id][messList[item.id].length-1].content_type !=='image' : false" style="font-size: 10px;margin: 0px;margin-top: 4px;color:#a19f9f;">{{mess_length_deal(messList[item.id][messList[item.id].length-1].send_name, messList[item.id][messList[item.id].length-1].content)}}</p>
            <p v-else-if="messList[item.id] !== undefined ? messList[item.id][messList[item.id].length-1].type !== null & messList[item.id][messList[item.id].length-1].content_type ==='image' : false" style="font-size: 10px;margin: 0px;margin-top: 4px;color:#a19f9f;">[{{messList[item.id][messList[item.id].length-1].send_name}}][图片]</p>
            <p v-else style="font-size: 10px;margin: 0px;margin-top: 4px;color:#a19f9f;">暂无消息</p>
          </div>
          <div style="position:absolute;right: 15px;top: 3px;color:#a19f9f;">
            <p v-show="messList[item.id] !== undefined ? messList[item.id][messList[item.id].length-1].type !== 'null' : false" style="font-size: 10px;">{{messList[item.id] !== undefined ? initSendDate(messList[item.id][messList[item.id].length-1].send_date) : ''}}</p>
          </div>
        </div>
      </div>
    </div>
    <!--有对话时，对话框-->
    <div v-if="acceptUser.name !== ''" class="mess_dialog">
      <!--对话框头部-->
      <div class="dlog_header">
        <el-avatar :size="35" v-if="acceptUser.avatar_url !== '' & acceptUser.avatar_url !== undefined" :src="oss_url + acceptUser.avatar_url" style="margin: 0;margin-left: 20px;margin-right: 10px;"></el-avatar>
        <el-avatar :size="35" v-else :src="require('@/assets/image/avatar.jpg')" style="margin: 0;margin-left: 20px;margin-right: 10px;"></el-avatar>
        <span>{{acceptUser.name}}</span>
      </div>
      <!--对话框内容-->
      <div id="content_overflow" class="dlog_content">
        <div v-for="(item, index) in messnowList" :key="index" class="dlog_content_item" style="margin-left: 5px;">
          <!--其他用户的消息展示[单聊]-->
          <div v-if="item.send_id !== userId & item.type !== 'null' & acceptUser.type !== 2" class="content_other">
            <div>
              <el-avatar :size="35" v-if="acceptUser.avatar_url !== '' & acceptUser.avatar_url !== undefined" :src="oss_url + acceptUser.avatar_url" style="margin-top: 5px;"></el-avatar>
              <el-avatar :size="35" v-else :src="require('@/assets/image/avatar.jpg')" style="margin-top: 5px;"></el-avatar>
            </div>
            <div class="mess_other">
              <div>
                <span style="font-size: 8px;">{{acceptUser.name}}   {{item.send_date}}</span>
              </div>
              <div v-if="item.content_type === 'image'" class="img_left">
                <el-image :src="imageurlDeal(item.content)" :preview-src-list="imgNowList" class="images"></el-image>
              </div>
              <div v-else class="content_other_bgd" @contextmenu.prevent="show_rightMenu_mess_content($event, item)">
                <span class="mess_content_msg">{{item.content}}</span>
              </div>
            </div>
          </div>
          <!--其他用户的消息展示[群聊]-->
          <div v-else-if="item.send_id !== userId & item.type !== 'null' & acceptUser.type !== 1" class="content_other">
            <div>
              <el-avatar :size="35" v-if="acceptUser.userCard[item.send_id].avatar_url !== '' & acceptUser.userCard[item.send_id].avatar_url !== undefined" :src="oss_url + acceptUser.userCard[item.send_id].avatar_url" style="margin-top: 5px;"></el-avatar>
              <el-avatar :size="35" v-else :src="require('@/assets/image/avatar.jpg')" style="margin-top: 5px;"></el-avatar>
            </div>
            <div class="mess_other">
              <div>
                <span style="font-size: 8px;">{{acceptUser.userCard[item.send_id].name}}   {{item.send_date}}</span>
              </div>
              <div v-if="item.content_type === 'image'" class="img_left">
                <el-image :src="imageurlDeal(item.content)" :preview-src-list="imgNowList" class="images"></el-image>
              </div>
              <div v-else class="content_other_bgd"  @contextmenu.prevent="show_rightMenu_mess_content($event, item)">
                <span class="mess_content_msg">{{item.content}}</span>
              </div>
            </div>
          </div>
          <!--本用户的消息展示-->
          <div v-else-if="item.send_id === userId & item.type !== 'null'" class="content_me">
            <div class="mess_me">
              <div>
                <span style="font-size: 8px;">{{item.send_date}}   {{item.send_name}}</span>
              </div>
              <div v-if="item.content_type === 'image'" class="img">
                <el-image :src="imageurlDeal(item.content)" :preview-src-list="imgNowList" class="images"></el-image>
              </div>
              <div v-else class="content_me_bgd"  @contextmenu.prevent="show_rightMenu_mess_content($event, item)">
                <span class="mess_content_msg">{{item.content}}</span>
              </div>
            </div>
            <div>
              <el-avatar :size="35" :src="userAvatar" style="margin-top: 5px;"></el-avatar>
            </div>
          </div>
        </div>
      </div>
      <!--对话框底部-->
      <div class="dlog_footer">
        <div class="footer_content">
          <!--聊天功能按钮-->
          <div class="footer_content_menu">
            <!--表情包发送功能-->
            <el-dropdown placement="top-start">
              <div class="footer_content_menu_face">

                <i class="iconfont #icon-guilian"></i>
              </div>
              <el-dropdown-menu slot="dropdown">
                <div class="face_list">
                  <div v-for="(item, index) in emojiList" v-bind:key="index" class="face-item" @click="emojiDown(index)">
                    {{item}}
                  </div>
                </div>
              </el-dropdown-menu>
            </el-dropdown>
            <div class="footer_content_menu_img">
              <label for="fileInput">
                <i class="iconfont icon-img" aria-hidden="true"></i>
              </label>
              <input v-show="false" type="file" accept="image/png, image/jpeg" ref="chartImage" id="fileInput" @change="showUpload">
            </div>
          </div>
          <el-input type="textarea" :rows="4" v-model="mess" maxlength="500" show-word-limit @keydown.enter.native="keyDown"></el-input>
          <el-button type="primary" @click="Wssendmess('text', '')" style="float: right;margin-top: 5px;">发送</el-button>
        </div>
      </div>
    </div>
    <!--加好友时，对话框-->
    <div v-else-if="showrelational" class="mess_dialog_relational">
      <!--对话框头部-->
      <div class="dlog_header">
        <span style="margin-left: 25px;">添加好友</span>
      </div>
      <!--对话框内容-->
      <div class="dlog_content_relational">
        <div class="frid_search">
          <el-form :model="searchform" :rules="searchrules" ref="searchref" @submit.native.prevent>
            <el-form-item prop="search_id">
              <el-input v-model="searchform.search_id" type="text" placeholder="请输入好友/群账号" style="width: 300px;" @keyup.enter.native='searchid'></el-input>
              <el-button type="primary" @click="searchid">搜索</el-button>
            </el-form-item>
          </el-form>
          <!--被搜索对象信息-->
          <div v-show="serarchuser[0].id !== ''" class="cardisfriend">
            <el-card>
              <div style="display: flex;">
                <!--头像-->
                <div class="cardisfriend_avatar">
                  <el-avatar v-if="serarchuser[0].avatar_url !== '' & serarchuser[0].avatar_url !== null" :size="60" :src="baseoss + serarchuser[0].avatar_url"></el-avatar>
                  <el-avatar v-else :size="60" :src="require('@/assets/image/avatar.jpg')"></el-avatar>
                </div>
                <!--用户名及账号-->
                <div class="cardisfriend_user">
                  <p>{{serarchuser[0].name}}</p>
                  <p style="font-size: 15px;color: #8a8282;">{{ serarchuser[0].id }}</p>
                </div>
                <!--是否好友状态-->
                <div class="cardisfriend_btn">
                  <p v-if="serarchuser[0].status === 'true'" style="color: #8a8282;margin:0px;font-size: 13px;">已成为好友</p>
                  <el-button v-else-if="serarchuser[0].status === 'pending_id2'" type="text" class="button" @click="agreefriend(serarchuser[0].id)">同意为好友</el-button>
                  <p v-else-if="serarchuser[0].status === 'pending_id1'" style="color: #8a8282;margin:0px;font-size: 13px;">已发送申请</p>
                  <el-button v-else type="text" class="button" @click="addfriend">添加为好友</el-button>
                </div>
              </div>
            </el-card>
          </div>
          <!--待处理好友申请-->
          <p v-show="pendingList.length !== 0">待处理好友申请</p>
          <div v-show="pendingList.length !== 0" class="cardpending">
            <el-card v-for="(item, index) in pendingList"
                     :key="index"
                     class="box-card">
              <div style="display: flex;">
                <!--头像-->
                <div class="cardisfriend_avatar">
                  <el-avatar v-if="item.avatar_url !== '' & item.avatar_url !== null" :size="60" :src="baseoss + item.avatar_url"></el-avatar>
                  <el-avatar v-else :size="60" :src="require('@/assets/image/avatar.jpg')"></el-avatar>
                </div>
                <!--用户名及账号-->
                <div class="cardisfriend_user">
                  <p>{{item.name}}</p>
                  <p style="font-size: 15px;color: #8a8282;">{{ item.id }}</p>
                </div>
                <!--是否同意-->
                <div class="cardisfriend_btn">
                  <el-button type="text" class="button" @click="agreefriend(item.id)">同意</el-button>
                  <el-button type="text" class="button" style="color: #8a8282;" @click="unagreefriend(item.id)">拒绝</el-button>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </div>
    <!--无对话时，对话框-->
    <div v-else class="mess_dialog_false">
      <span>Welcome to the chat room</span>
    </div>
    <!--右键菜单-聊天内容复制-->
    <div class="rightMenu_mess_content" v-if="isShow_rightMenu_mess_content" :style="{'left': rightMenu_mess_content_left + 'px', 'top': rightMenu_mess_content_top + 'px'}">
      <el-card class="rightMenu_mess_content_card">
        <div class="rightMenu_mess_content_card_item">
          <el-button @click="onCopy">复制</el-button>
        </div>
        <div class="rightMenu_mess_content_card_item">
          <el-button @click="noused">更多</el-button>
        </div>
      </el-card>
    </div>
    <!--发送图片dialog-->
    <el-dialog :visible.sync="isShowDialog"
               class="dialogUpload"
               @closed="diaClose">
      <el-image :src="imageUrl" class="images"></el-image>
      <div class="dialog_btn">
        <el-button @click="diaClose">取消</el-button>
        <el-button type="primary" @click="WssendImg" :loading="sendImgLoad">发送</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import userAvatar from '@/assets/image/avatar.jpg'
import moment from 'moment'
// import base from '../../api/base'
// import cookie from '../../utils/cookie'
const {emojiList} = require('@/assets/emoji/emoji.json')

export default {
  name: 'wsChart',
  data () {
    return {
      mess: '', // 输入的信息
      userAvatar: userAvatar, // 用户头像
      avatarurl: '', // 头像原路径(不含域名)
      userName: '用户名', // 用户名
      userId: '', // 用户id
      // oss_url: base.domain, // 获取bucket
      acceptUser: {
        name: '', // 对话的用户名或群名
        id: '', // 对话用户的id
        type: '', // 对话用户类型(个人、群)
        avatar_url: '', // 对话用户头像
        userlist: [], // 接受群内用户集id
        userCard: {} // 群内用户详细信息
      },
      userList: [], // 对话用户列表
      messList: [], // 聊天内容列表
      messIsReadList: [], // 聊天内容未读内容列表
      messnowList: [], // 当前对话用户的-聊天内容列表
      imgNowList: [], // 当前图片预览集合
      ws: '', // websocket连接
      showrelational: false, // 用户关系处理
      searchform: {
        search_id: '' // 搜索的id
      },
      searchrules: {
        search_id: [
          {required: true, message: '账号不能为空', trigger: 'blur'}
        ]
      }, // 搜索规则
      serarchuser: [{
        id: '',
        name: '',
        avatar_url: '',
        status: ''
      }], // 被搜索对象的信息集合
      // baseoss: base.domain, // oss域名
      pendingList: [], // 待处理好友申请集合
      heartCheck: { timeout: 60000, // 60秒
        timeoutObj: null,
        serverTimeoutObj: null,
        reset: function () {
          clearTimeout(this.timeoutObj)
          clearTimeout(this.serverTimeoutObj)
          this.start()
        },
        start: function () {
          let self = this
          this.timeoutObj = setTimeout(function () {
            this.ws.send('ping')
            self.serverTimeoutObj = setTimeout(
                function () {
                  this.ws.close()
                }, self.timeout)
          }, this.timeout)
        }
      },
      emojiList: emojiList.split(','), // 表情包列表
      isShow_rightMenu_mess_content: false, // 右键菜单
      rightMenu_mess_content_item: '',
      rightMenu_mess_content_top: 0,
      rightMenu_mess_content_left: 0,
      isShowDialog: false, // 发送图片预览dialog
      imageUrl: '', // 预览图
      files: {}, // 发送的文件
      sendImgLoad: false // 发送图片按钮加载
    }
  },
  // 初始化
  created () {
    this.getwebsocket()
    this.getuserList()
    this.getuserData()
    this.addCloseRightMenu()
  },
  mounted () {
    // this.ws.addEventListener('open', this.handleWsOpen.bind(this), false)
    // this.ws.addEventListener('close', this.handleWsClose.bind(this), false)
    // this.ws.addEventListener('error', this.handleWsError.bind(this), false)
    // this.ws.addEventListener('message', this.handleWsMessage.bind(this), false)
  },
  watch: {
    // 监听当前消息列表，更新时，保持滚动条位于底部
    messnowList: function scrollToBottom () {
      this.$nextTick(() => {
        var message = document.getElementById('content_overflow')
        // 滚动滑钮到滚动条顶部的距离=滚动条的高度
        message.scrollTop = message.scrollHeight
      })
    }
  },
  methods: {
    /**
     * 添加关闭右键菜单的监听器
     */
    addCloseRightMenu () {
      // 添加监听器，用于其他位置点击时，关闭打开的右键菜单,并清空获取的key
      document.addEventListener('click', () => {
        this.isShow_rightMenu_mess_content = false
        this.rightMenu_mess_content_item = ''
      })
      // 添加监听器，用于点击菜单内按钮时，关闭打开的右键菜单,并清空获取的key
      document.addEventListener('mousedown', (e) => {
        const {button} = e
        if (button === 2) {
          this.isShow_rightMenu_mess_content = false
          this.rightMenu_mess_content_item = ''
        }
      })
    },
    /**
     * enter发送消息，ctrl+enter换行
     * @param e
     * @returns {boolean}
     */
    keyDown (e) {
      if (e.ctrlKey && e.keyCode === 13) { // 用户点击了ctrl+enter触发
        this.mess += '\n'
      } else { // 用户点击了enter触发
        this.Wssendmess('text', '')
        e.preventDefault() // 阻止浏览器默认换行操作
        return false
      }
    },
    // 聊天发送按钮点击事件
    async Wssendmess (contenttype, content) {
      // 判断是否全是空格
      var message = this.mess.trim()
      if (contenttype === 'image') {
        message = content
      }
      if (message !== '') {
        // 发送消息格式
        // var obj = JSON.stringify({
        //   send_id: this.userId,
        //   accept_id: this.acceptUser.id,
        //   accept_group: this.acceptUser.userlist,
        //   send_name: this.userName,
        //   content: message,
        //   send_date: moment().format('YYYY-MM-DD HH:mm:ss'),
        //   mess_id: 'GP' + this.userId + Date.now(),
        //   type: this.acceptUser.type,
        //   send_type: 'message',
        //   content_type: contenttype
        // })
        // 保存消息到数据库
        // this.$api.addMessContent(JSON.parse(obj))
        //     .then((res) => {
        //       if (res.data.code === 200) {
        //         // 保存成功再发送消息
        //         this.ws.send(obj)
        //       }
        //     })
        //     .catch((err) => {
        //       console.log(err)
        //     })
        // 发送完消息，重新输入框
        this.mess = ''
      }
    },
    // 聊天图片发送
    WssendImg () {
      this.sendImgLoad = true
      // this.$api.upload(this.files, 'chart')
      //     .then(res => {
      //       this.sendImgLoad = false
      //       if (res.data.code === 200) {
      //         this.isShowDialog = false
      //         this.Wssendmess('image', res.data.data)
      //       } else {
      //         this.$message.error(res.data.data)
      //       }
      //     })
      //     .catch(err => {
      //       this.sendImgLoad = false
      //       this.$message.error(err)
      //     })
    },
    // 添加好友按钮点击事件
    addfriend () {
      // 发送消息格this.serarchuser[0].id式
      var obj = JSON.stringify({
        send_id: this.userId,
        accept_id: this.serarchuser[0].id,
        accept_group: '',
        send_name: this.userName,
        send_avatar: this.avatarurl,
        send_date: moment().format('YYYY-MM-DD HH:mm:ss'),
        type: 1,
        send_type: 'addfriend'
      })
      console.log(obj)
      // this.$api.addfriend(this.userId, this.serarchuser[0].id)
      //     .then(res => {
      //       if (res.data.code === 200) {
      //         this.ws.send(obj)
      //         this.$notify({
      //           title: '提示',
      //           message: '已发送好友请求!',
      //           type: 'success'
      //         })
      //         // 初始化搜索信息
      //         this.searchform.search_id = ''
      //         this.serarchuser = [{
      //           id: '',
      //           name: '',
      //           avatar_url: '',
      //           status: ''
      //         }]
      //       } else if (res.data.code === 201) {
      //         this.$message.error('您或对方已发起好友申请,无需再次发起')
      //       }
      //     })
      //     .catch(err => {
      //       console.log(err)
      //     })
    },
    // 同意好友申请
    agreefriend (id) {
      console.log(id)
      // this.$api.agreefriend(id)
      //     .then(res => {
      //       if (res.data.code === 200) {
      //         this.$notify({
      //           title: '提示',
      //           message: '已同意账号为"' + id + '"的好友请求!',
      //           type: 'success'
      //         })
      //         // 发送通知给对方
      //         this.agreefrid_send(id)
      //       } else {
      //         this.$notify({
      //           title: '提示',
      //           message: '该好友请求已失效!',
      //           type: 'error'
      //         })
      //       }
      //       // 初始化数据
      //       this.searchform.search_id = ''
      //       this.serarchuser = [{
      //         id: '',
      //         name: '',
      //         avatar_url: '',
      //         status: ''
      //       }]
      //       this.pendingList = []
      //       this.getuserData()
      //       this.getuserList()
      //     })
      //     .catch(err => {
      //       this.$message.error('意外错误！')
      //       console.log(err)
      //     })
    },
    // 发送同意好友申请后的ws
    agreefrid_send (id) {
      // 发送消息格this.serarchuser[0].id式
      var obj = JSON.stringify({
        send_id: this.userId,
        accept_id: id,
        accept_group: '',
        send_date: moment().format('YYYY-MM-DD HH:mm:ss'),
        type: 1,
        send_type: 'addfriend_agree'
      })
      this.ws.send(obj)
    },
    // 拒绝好友申请
    unagreefriend (id) {
      console.log(id)
      // this.$api.unagreefriend(id)
      //     .then(res => {
      //       if (res.data.code === 200) {
      //         this.$notify({
      //           title: '提示',
      //           message: '已拒绝账号为"' + id + '"的好友请求!',
      //           type: 'success'
      //         })
      //         // 发送通知给对方
      //         this.unagreefrid_send(id)
      //       } else {
      //         this.$notify({
      //           title: '提示',
      //           message: '该好友请求已失效!',
      //           type: 'error'
      //         })
      //       }
      //       // 初始化数据
      //       this.pendingList = []
      //       this.getuserData()
      //       this.getuserList()
      //     })
      //     .catch(err => {
      //       this.$message.error('意外错误！')
      //       console.log(err)
      //     })
    },
    // 发送拒绝好友申请后的ws
    unagreefrid_send (id) {
      // 发送消息格this.serarchuser[0].id式
      var obj = JSON.stringify({
        send_id: this.userId,
        accept_id: id,
        accept_group: '',
        send_date: moment().format('YYYY-MM-DD HH:mm:ss'),
        type: 1,
        send_type: 'addfriend_unagree'
      })
      this.ws.send(obj)
    },
    handleWsOpen () {
      console.log('WebSocket2已经打开!')
      setInterval(function (ws, id) {
        if (ws.readyState === 1) {
          var obj = JSON.stringify({
            send_id: id,
            accept_id: id,
            accept_group: '',
            send_date: moment().format('YYYY-MM-DD HH:mm:ss'),
            type: 1,
            send_type: 'ping'
          })
          ws.send(obj)
        }
      }, 55000, this.ws, this.userId)
    },
    handleWsClose (e) {
      console.log('WebSocket2关闭!')
      console.log(e)
    },
    handleWsError (e) {
      this.$message.error('意外错误，weksocket关闭，请刷新网页重试！')
      console.log('WebSocket2发生错误!')
      console.log(e)
    },
    handleWsMessage (e) {
      /* console.log('WebSocket2收到消息!')
      console.log(e.data) */
      // 获取内容
      var obj = JSON.parse(e.data)
      /** 接收类型-聊天内容 */
      if (obj.send_type === 'message') {
        if (obj.accept_id !== this.userId) {
          // 强制刷新
          this.$forceUpdate()
          // 判断是否和该对象的聊天内容为空，如果是需要初始化一下
          if (this.messList[obj.accept_id] === undefined) {
            this.messList[obj.accept_id] = []
            this.messList[obj.accept_id].push(obj)
            if (this.acceptUser.id === obj.accept_id) {
              this.messnowList = this.messList[obj.accept_id]
            }
          } else {
            this.messList[obj.accept_id].push(obj)
          }
        } else {
          // 强制刷新
          this.$forceUpdate()
          // 判断是否和该对象的聊天内容为空，如果是需要初始化一下
          if (this.messList[obj.send_id] === undefined) {
            this.messList[obj.send_id] = []
            this.messList[obj.send_id].push(obj)
            if (this.acceptUser.id === obj.send_id) {
              this.messnowList = this.messList[obj.send_id]
            }
          } else {
            this.messList[obj.send_id].push(obj)
          }
        }
        this.getuserList()
      } else if (obj.send_type === 'addfriend') {
        /** 接收类型-好友申请 */
        let content = {
          id: obj.send_id,
          name: obj.send_name,
          avatar_url: obj.send_avatar
        }
        // 强制刷新
        this.pendingList.push(content)
        this.$notify({
          title: '提示',
          message: '账号:"' + obj.send_id + '"--向您发起好友请求!',
          type: 'success'
        })
      } else if (obj.send_type === 'addfriend_agree') {
        /** 接收类型-好友申请通过 */
        this.pendingList = []
        // 初始化数据
        this.getuserData()
        this.getuserList()
        this.$notify({
          title: '提示',
          message: '账号:"' + obj.send_id + '"--已同意好友请求!',
          type: 'success'
        })
      } else if (obj.send_type === 'addfriend_unagree') {
        /** 接收类型-好友申请未通过 */
        this.pendingList = []
        // 初始化数据
        this.getuserData()
        this.getuserList()
        this.$notify({
          title: '提示',
          message: '账号:"' + obj.send_id + '"--已拒绝好友请求!',
          type: 'error'
        })
      }
    },
    // 初始化websocket
    // getwebsocket () {
    //   this.ws = new WebSocket(base.ip)
    // },
    // 获取当前用户信息
    getuserData () {
      // let id = cookie.getid()
      // 获取当前用户待处理的好友申请
      // this.$api.getpendingList(id)
      //     .then(res => {
      //       if (res.data.code === 200) {
      //         this.pendingList = res.data.data
      //       }
      //     })
      //     .catch(err => {
      //       console.log(err)
      //     })

      // this.userId = id
      // 默认情况下，axios将JavaScript对象序列化为JSON。要以application/x-www-form-urlencoded格式发送数据，可以使用下面qs库对数据进行编码
      // this.$api.userinformation(id)
      //     .then((response) => { // 请求成功处理
      //       // 赋值用户名
      //       this.userName = response.data.data[0].name
      //       this.userId = response.data.data[0].id
      //       if (response.data.data[0].avatar_url !== '' && response.data.data[0].avatar_url !== null) {
      //         this.avatarurl = response.data.data[0].avatar_url
      //         this.userAvatar = base.domain + this.avatarurl
      //       }
      //     })
      //     .catch((error) => { // 请求失败处理
      //       console.log(error)
      //     })
    },
    // 获取对话列表
    getuserList () {
      // 获取用户列表
      // this.$api.getMessUserList(this.userId)
      //   .then((res) => {
      //     if (res.data.data.length !== 0 && res.data.data !== false) {
      //       this.userList = res.data.data
      //     }
      //   })
      // 获取全部聊天内容
      // this.$api.getmessall()
      //   .then(res => {
      //     if (res.data.code === 200) {
      //       let list = res.data.data
      //       let messlist = {}
      //       let id = this.userId
      //       // 单聊录入数组
      //       list[0].forEach(function (item, index) {
      //         if (item.send_id === id) {
      //           if (messlist[item.accept_id] === undefined) {
      //             messlist[item.accept_id] = []
      //           }
      //           messlist[item.accept_id].push(item)
      //         } else {
      //           if (messlist[item.send_id] === undefined) {
      //             messlist[item.send_id] = []
      //           }
      //           messlist[item.send_id].push(item)
      //         }
      //       })
      // 群聊录入数组
      //     list[1].forEach(function (item) {
      //       if (messlist[item.accept_id] === undefined) {
      //         messlist[item.accept_id] = []
      //       }
      //       messlist[item.accept_id].push(item)
      //     })
      //     this.messList = messlist
      //     // 筛选未读信息
      //     this.isRead()
      //     // 如果打开了聊天框，进行已读处理
      //     if (this.acceptUser.id !== '') {
      //       this.messnowList = this.messList[this.acceptUser.id]
      //
      //       // 初始化当前图片预览集合
      //       this.imgNowList = []
      //       this.messnowList.forEach((item) => {
      //         if (item.content_type === 'image') {
      //           this.imgNowList.push(this.imageurlDeal(item.content))
      //         }
      //       })
      //
      //       // 已读更新信息
      //       this.isRead_true(this.acceptUser.id)
      //     }
      //   }
      // })
    },
    // 展示对话框
    async showmessdlog (item) {
      // 获取聊天对象/群的信息
      this.acceptUser.name = item.name
      this.acceptUser.id = item.id
      this.acceptUser.type = item.type
      this.acceptUser.avatar_url = item.avatar_url !== '' & item.avatar_url !== null ? item.avatar_url : ''
      // 判断是单聊（1）还是群聊（2）
      if (this.acceptUser.type === 2) {
        // 获取群聊成员id
        await this.getGroupUserId(this.acceptUser.id)
      } else {
        this.acceptUser.userlist = []
        this.acceptUser.userCard = {}
        this.messnowList = this.messList[this.acceptUser.id]

        // 初始化当前图片预览集合-单聊
        this.imgNowList = []
        this.messnowList.forEach((item) => {
          if (item.content_type === 'image') {
            this.imgNowList.push(this.imageurlDeal(item.content))
          }
        })
      }

      // 已读更新信息
      this.isRead_true(item.id)
    },
    // 获取群成员id
    async getGroupUserId (groupid) {
      await this.$api.getGroupUserList(groupid)
          .then((res) => {
            if (res.data.data.length !== 0 && res.data.data !== false) {
              var data = res.data.data
              this.acceptUser.userlist = []
              this.acceptUser.userCard = {}
              data.forEach((item) => {
                this.acceptUser.userlist.push(item.id)
                this.acceptUser.userCard[item.id] = item
              })

              // 等群用户信息获取完了，再初始化显示对话数据，防止来不及获取滚动条高度
              this.messnowList = this.messList[this.acceptUser.id]

              // 初始化当前图片预览集合-群聊
              this.imgNowList = []
              this.messnowList.forEach((item) => {
                if (item.content_type === 'image') {
                  this.imgNowList.push(this.imageurlDeal(item.content))
                }
              })
            }
          })
    },
    // 初始化显示的发送时间
    initSendDate (date) {
      // 比较
      let nowday = moment(Date.now()).format('YYYY-MM-DD')
      let nowyear = moment(Date.now()).format('YYYY')
      let dateday = date.substring(0, 10)// 传参年月日
      let dateyear = date.substring(0, 4)// 传参年
      // 返回结果
      let year = date.substring(0, 10)
      let day = date.substring(5, 10)// 非今天，的【月-日】
      let time = date.substring(16, 11)// 是今天的，【时-分】
      // 判断是否不是今天
      if (nowyear > dateyear) {
        return year
      } else if (nowday > dateday) {
        return day
      } else {
        return time
      }
    },
    // 点击处理好友关系
    relational () {
      this.showrelational = true
      this.acceptUser.name = ''
      this.messnowList = []
    },
    // 点击搜索账号
    searchid () {
      this.$refs.searchref.validate(async (valid) => {
        // 判断是否通过rules初步验证
        if (valid) {
          // let myid = cookie.getid()
          // if (this.searchform.search_id === myid) {
          //   this.$message.error('请不要输入自己的账号！')
          //   return
          // }
          // await this.$api.isfriend(myid, this.searchform.search_id)
          //     .then(res => {
          //       if (res.data.code === 200 | res.data.code === 201 | res.data.code === 202 | res.data.code === 203) {
          //         this.serarchuser.shift()
          //         this.serarchuser.push(res.data.data)
          //       } else if (res.data.code === 404) {
          //         this.$message.error('该用户或群不存在')
          //       } else {
          //         this.$message.error('意外错误,请稍后重试')
          //       }
          //     })
          //     .catch(err => {
          //       console.log(err)
          //     })
        }
      })
    },
    // 用户栏，最新聊天内容长度处理
    mess_length_deal (name, mess) {
      let dealmess = '[' + name + ']' + mess
      if (dealmess.length > 15) {
        dealmess = dealmess.substring(0, 14) + '...'
      }
      return dealmess
    },
    // 功能还未启用提示
    noused () {
      this.$message.warning('该功能还未启用，请选择其他功能!')
    },
    // 表情包点击事件
    emojiDown (index) {
      this.mess = this.mess + this.emojiList[index]
    },
    // 右键菜单-聊天内容-事件
    show_rightMenu_mess_content (e, item) {
      this.isShow_rightMenu_mess_content = true
      // 定位
      this.rightMenu_mess_content_top = e.pageY
      this.rightMenu_mess_content_left = e.pageX
      // 获取当前选中内容
      this.rightMenu_mess_content_item = item
    },
    // 复制功能
    onCopy () {
      let msg = this.rightMenu_mess_content_item.content
      // 复制到粘贴板
      navigator.clipboard.writeText(msg)
          .then(() => {
            this.$message.success('复制成功')
          })
          .catch(err => {
            this.$message.error('复制失败，原因：' + err)
          })
    },
    // 选中图片后初步守卫
    showUpload () {
      let file = this.$refs.chartImage.files[0]
      // 文件格式大小判断处理
      const isJPG = file.type === 'image/jpeg' | file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传图片只能是 JPG/PNG 格式!')
        return
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
        return
      }

      // 格式无误后，预览文件处理
      this.imgSaveToUrl(file)
      this.isShowDialog = true
      // 复刻文件信息
      this.files = file
    },
    // 获取选中图片的预览路径，并赋值给本地img路径，在前端展示
    imgSaveToUrl (file) {
      // 获取上传图片的本地URL，用于上传前的本地预览，转换后的地址为 blob:http://xxx/7bf54338-74bb-47b9-9a7f-7a7093c716b5
      this.imageUrl = URL.createObjectURL(file)
      console.log('图片预览地址：', this.imageUrl)
    },
    // 关闭卖出的dialog事件
    diaClose () {
      this.isShowDialog = false// 关闭窗口
    },
    // 返回oss图片地址
    imageurlDeal (url) {
      return url
      // return base.domain + url
    },
    // 筛选未读信息
    isRead () {
      let userList = this.userList
      let messList = this.messList
      let messIsReadList = []
      userList.forEach((item) => {
        // 初始化
        messIsReadList[item.id] = []
        if (messList[item.id] === undefined) {
          console.log('该用户没有聊天记录')
        } else {
          // 保存未读数据
          messList[item.id].forEach((item2) => {
            if (item2.status !== undefined) {
              if (item2.status === 0) {
                messIsReadList[item.id].push(item2.isread_id)
              }
            }
          })
        }
      })
      this.messIsReadList = messIsReadList
    },
    // 根据聊天对象已读信息处理
    isRead_true (id) {
      let readList = this.messIsReadList[id]
      console.log('已读信息：', readList)
      // this.$api.updateMessRead(readList)
      //     .then((res) => {
      //       if (res.data.code === 200) {
      //         this.getuserData()
      //         this.getuserList()
      //       }
      //     })
      //     .catch((err) => {
      //       console.log(err)
      //     })
    }
  }
}
</script>

<style scoped>
.el-card /deep/ .el-card__body {
  padding: 10px 20px;
}
/*阿里矢量库图标样式*/
.icon-chart-logo{
  font-size: 30px;
  color: #3d9aff;
}
.icon-chart{
  line-height: 50px;
  font-size: 20px;
}
.icon-friend-add{
  line-height: 50px;
  font-size: 20px;
}
.icon-friend-add-badge /deep/ .el-badge__content.is-fixed{
  top: 8px;
}
.icon-figures{
  line-height: 50px;
  font-size: 20px;
}
.icon-circle{
  line-height: 50px;
  font-size: 20px;
}
/*阿里图标div*/


.mess_icon_logo{
  position: absolute;
  top: 40px;
  left: 50%;
  transform: translate(-50%, -50%);
}
.user_icon{
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translate(-50%, -50%);
}
.user_addfrid{
  position: absolute;
  top: 100px;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50px;
  height: 50px;
  text-align: center;
  border-radius: 10px;
}
.user_addfrid:hover{
  background: #ecebeb;
}
.user_addfrid:active{
  background: #dedede;
}
.user_addfrid:hover .icon-friend-add{
  color: #3d9aff;
}
.user_addfrid:active .icon-friend-add{
  color: #0b7bf5;
}
.mess_icon_chart{
  position: absolute;
  top: 150px;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50px;
  height: 50px;
  text-align: center;
  border-radius: 10px;
}
.mess_icon_chart:hover{
  background: #ecebeb;
}
.mess_icon_chart:active{
  background: #dedede;
}
.mess_icon_chart:hover .icon-chart{
  color: #3d9aff;
}
.mess_icon_chart:active .icon-chart{
  color: #0b7bf5;
}
.mess_icon_figures{
  position: absolute;
  top: 200px;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50px;
  height: 50px;
  text-align: center;
  border-radius: 10px;
}
.mess_icon_figures:hover{
  background: #ecebeb;
}
.mess_icon_figures:active{
  background: #dedede;
}
.mess_icon_figures:hover .icon-figures{
  color: #3d9aff;
}
.mess_icon_figures:active .icon-figures{
  color: #0b7bf5;
}
.mess_icon_circle{
  position: absolute;
  top: 250px;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50px;
  height: 50px;
  text-align: center;
  border-radius: 10px;
}
.mess_icon_circle:hover{
  background: #ecebeb;
}
.mess_icon_circle:active{
  background: #dedede;
}
.mess_icon_circle:hover .icon-circle{
  color: #3d9aff;
}
.mess_icon_circle:active .icon-circle{
  color: #0b7bf5;
}
/*正式内容*/
.mess{
  border-radius: 5px;
  background-clip: padding-box;
  margin:20px auto;
  width: 1500px;
  height: 755px;
  border: 1px #8a8282;
  box-shadow: 0 0 10px #9b9393;
  background-color: white;
  display: flex;
}
.mess_user_btn{
  width: 80px;
  height: 100%;
  border-right: 1px solid #cecdcd;
  position: relative;
  background: #d3d3d3;
}
.mess_user_list{
  width: 400px;
  height: 100%;
  border-right: 1px solid #cecdcd;
  background: #fbfcfc;
}
.mess_dialog{
  width: 100%;
  height: 100%;
  background: #fbfcfc;
}
.mess_dialog_relational{
  width: 100%;
  height: 100%;
  background: #fbfcfc;
}
.mess_dialog_false{
  width: 100%;
  height: 100%;
  text-align: center;
  line-height: 600px;
  background: #fbfcfc;
  font-size: 24px;
}
.dlog_header{
  width: 100%;
  height: 60px;
  border-bottom: 1px solid #cecdcd;
  display: flex;
  align-items: center;
  font-size: 14px;
}
.dlog_content{
  width: 100%;
  height: 500px;
  border-bottom: 1px solid #cecdcd;
  overflow-y: auto;
  overflow-x: hidden;
}
.dlog_content_relational{
  width: 100%;
}
.dlog_footer{
  width: 100%;
}
/*右键菜单css样式*/
.rightMenu_mess_content{
  position: fixed;
}
.rightMenu_mess_content_card /deep/ .el-card__body{
  padding: 0;
}
.rightMenu_mess_content_card /deep/ .el-button{
  border: 0;
  padding: 12px 30px;
}
.rightMenu_mess_content_card_item{
  border-bottom: solid 1px #eeeded;
}
.footer_content{
  margin: 10px;
}
.footer_content_menu{
  display: flex;
  height: 30px;
  width: 100%;
}
.footer_content_menu_face{
  width: 30px;
  height: 30px;
  text-align: center;
  font-size: 20px;
}
.footer_content_menu_face:hover .icon-face{
  color: #01a3fc;
}
.footer_content_menu_img{
  width: 30px;
  height: 30px;
  text-align: center;
  font-size: 20px;
  margin-left: 5px;
}
.footer_content_menu_img:hover .icon-img{
  color: #01a3fc;
}
.icon-face{
  color: #0a0a0a;
  font-size: 25px;
}
.icon-img{
  color: #0a0a0a;
  font-size: 25px;
}
.face_list{
  width: 300px;
  height: 200px;
  display: flex;
  flex-wrap: wrap;
  overflow-y: auto;
  overflow-x: hidden;
}
.face-item{
  width: 30px;
  height: 30px;
  border: solid 1px #e0e0e0;
  margin: 0 -1px -1px 0;/*解决相邻border看起来很粗问题*/
  text-align: center;
  font-size: 20px;
}
.face-item:hover{
  background: #eeeeee;
}
.user_head{
  height: 60px;
  width: 100%;
  border-bottom: 1px solid #cecdcd;
}
.user_head_title{
  padding: 20px 20px;
}
.cardisfriend{
  margin: 0 auto;
  width: 380px;
}
.cardpending{
  margin: 0 auto;
  width: 380px;
  height: 300px;
  overflow-y: auto;
}
.cardisfriend_avatar{
  margin:auto 0;
}
.cardisfriend_user{
  width: 200px;
  text-align: left;
  padding-left: 10px;
}
.cardisfriend_user p{
  margin: 0px;
  padding: 5px 0px;
}
.cardisfriend_btn{
  display: flex;
  align-items: center;
}
.el-button--text{
  padding: 20px 0px;
}
.user_list{
  height: 694px;
  overflow-y: auto;
}
.user_list_item{
  height: 60px;
  border-bottom: 1px solid #cecdcd;
  display: flex;
  align-items: center;
  position: relative;
}
.user_list_item:hover{
  background: #f5f5f6;
}
.user_list_item:active{
  background: #e0dfdf;
}
.content_other{
  width: 70%;
  display: flex;
  justify-content: flex-start;
  margin: 11px 18px;
}
.mess_other{
  text-align: left;
  margin-left: 10px;
}
.content_me{
  width: 70%;
  display: flex;
  justify-content: flex-end;
  float: right;
  margin: 11px 18px;
}
.mess_me{
  text-align: right;
  margin-right: 10px;
}
.frid_search{
  text-align: center;
  margin-top: 15px;
}
.mess_content_msg{
  font-size: 16px;
  font-weight: 300;
  margin: 2px;
}
/*其他用户的气泡*/
.content_other_bgd {
  border-radius: 6px;
  position: relative;
  display: inline-block;
  padding: 0px 6px;
  width: auto;
  height: auto;
  line-height: 34px;
  background: #e3e1e1;
  z-index: 0;
}
/*气泡前的小三角指向*/
.content_other_bgd::before {
  border-style: solid;
  border-width: 0 11px 11px 0;
  border-color: transparent #e3e1e1 transparent transparent;
  content: "";
  position: absolute;
  top: 10px; left: -8px;
  margin-top: -9px;
  display: block;
  width: 0px;
  height: 0px;
  z-index: 0;
}
/*我方的气泡*/
.content_me_bgd {
  border-radius: 6px;
  position: relative;
  display: inline-block;
  padding: 0px 6px;
  width: auto;
  height: auto;
  line-height: 34px;
  background: #95ec69;
  z-index: 0;
  text-align: left;
}
.content_me_bgd::after {
  border-style: solid;
  border-width: 0 0 11px 11px;
  border-color: transparent transparent transparent #95ec69;
  content: "";
  position: absolute;
  top: 10px; right: -8px;
  margin-top: -10px;
  display: block;
  width: 0px;
  height: 0px;
  z-index: -1;
}
.el-form-item /deep/ .el-form-item__error{
  margin-left: 500px;
}
/*上传图片预览的dialog*/
.img_left{
  text-align: left;
}
.img{
  text-align: right;
}
.images{
  height: 150px;
  fit:contain;
}
.dialogUpload /deep/ .el-dialog{
  width: 400px;
  text-align: center;
}
.dialogUpload /deep/ .el-dialog__body{
  padding: 10px;
}
.dialog_btn{
  margin-top: 5px;
  text-align: right;
}
/*消息未读红标*/
.user_list_badge{
  position: absolute;
  left: 43px;
  bottom: 36px;
}
.user_list_badge__content{
  font-size: 5px;
}
</style>
