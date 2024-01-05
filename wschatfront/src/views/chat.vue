<template>
  <div class="mess">
    <!--菜单-->
    <div class="left-menu">
      <el-tabs v-model="tabName" tab-position="left" type="border-card" @tab-click="handleClick" style="height: 100%;">
        <el-tab-pane name="chat">
          <span slot="label">
             <svg class="icon" aria-hidden="true" style="font-size: 20px">
                <use xlink:href="#icon-liaoyiliaoxuanzhong"></use>
             </svg>
          </span>
          <div style="text-align: center;width: 100%"><span>聊天列表</span></div>
          <!--添加一个搜索框,可以通过会话名称进行过滤-->
          <div style="margin-top: 10px;margin-left: 10px;margin-right: 10px">
            <el-input placeholder="搜索" v-model="messageSearchText" clearable></el-input>
          </div>
          <div class="chat-list" style="height: 560px;scrollbar-width: none;
  -ms-overflow-style: none;
  overflow-x: hidden;
  overflow-y: auto;">
            <el-card v-for="(item, index) in messageList" @click.native="select(item,'聊天列表')" :key="item.id"
                     shadow="hover" style="margin-top: 10px;" :class="{ selected: selectedItem === item }">
              <div class="user_list_item">

                <el-avatar shape="square" :size="35" v-if="item.avatar !== '' & item.avatar !== undefined"
                           :src="item.avatar"></el-avatar>
                <el-avatar shape="square" :size="35" v-else :src="require('@/assets/image/avatar.jpg')"
                ></el-avatar>

                <div style="margin-left: 15px;">
                  <p style="font-size: 12px;margin-top: 0px;margin-bottom: 0px;">{{ item.name }}<span
                      v-if="item.type==2" style="color: #8a8282">[群聊]</span></p>
                  <!--                  <p style="font-size: 10px;margin: 0px;margin-top: 4px;color:#a19f9f;" class="text-overflow">-->
                  <!--                    {{ item.lastMess }}-->
                  <!--                  </p>-->
                  <!--聊天记录如果有，显示最后一条-->
                  <!--另外，如果最后一条消息是图片，显示[图片]，如果是文字，显示文字-->
                  <p v-if="allChatRecords[item.acceptId+'type-'+item.type] && allChatRecords[item.acceptId+'type-'+item.type][allChatRecords[item.acceptId+'type-'+item.type].length-1].contentType == 'image'  "
                     style="font-size: 10px;margin: 0px;margin-top: 4px;color:#a19f9f;" class="text-overflow">
                    [图片]
                  </p>
                  <p v-if="allChatRecords[item.acceptId+'type-'+item.type] && allChatRecords[item.acceptId+'type-'+item.type][allChatRecords[item.acceptId+'type-'+item.type].length-1].contentType !== 'image' "
                     style="font-size: 10px;margin: 0px;margin-top: 4px;color:#a19f9f;" class="text-overflow">
                    {{
                      allChatRecords[item.acceptId + 'type-' + item.type][allChatRecords[item.acceptId + 'type-' + item.type].length - 1].content
                    }}
                  </p>
                </div>
                <div style="position: sticky;left: 200px">
                  <p style="font-size: 10px;margin-top: 0px;margin-bottom: 0px;color:#a19f9f;">{{ item.lastTime }}</p>
                </div>
              </div>

            </el-card>
          </div>
        </el-tab-pane>
        <el-tab-pane name="friend">
          <span slot="label">
            <svg class="icon" aria-hidden="true" style="font-size: 20px">
                <use xlink:href="#icon-tongxunlu"></use>
             </svg>
          </span>
          <div style="text-align: center;width: 100%"><span>好友列表</span></div>
          <!--添加一个搜索框-->
          <div style="margin-top: 10px;margin-left: 10px;margin-right: 10px">
            <el-input placeholder="搜索" v-model="friendSearchText" clearable></el-input>
          </div>
          <div class="chat-list" style="height: 560px;scrollbar-width: none;
  -ms-overflow-style: none;
  overflow-x: hidden;
  overflow-y: auto;">
            <el-card v-for="(item, index) in friendList" @click.native="select(item,'好友列表')" :key="item.id"
                     shadow="hover" style="margin-top: 10px;" :class="{ selected: selectedItem === item }">
              <div class="user_list_item">
                <el-avatar shape="square" :size="35" v-if="item.avatar !== '' & item.avatar !== undefined"
                           :src="item.avatar" style="vertical-align:middle;"></el-avatar>
                <el-avatar shape="square" :size="35" v-else style="vertical-align:middle;"
                           :src="require('@/assets/image/avatar.jpg')"
                ></el-avatar>

                <div style="margin-left: 15px;">
                  <p style="font-size: 12px;color: black">{{ item.nickname }}</p>
                </div>
                <div style="position: sticky;left: 200px">
                  <el-button size="mini" @click="unfollowUser(item.id)" style="background-color: rgb(121, 180, 235)">
                    互相⇋关注
                  </el-button>
                </div>
              </div>

            </el-card>
          </div>
        </el-tab-pane>
        <el-tab-pane name="group">
          <span slot="label">
            <svg class="icon" aria-hidden="true" style="font-size: 20px">
              <use xlink:href="#icon-tongshiqunzu"></use>
            </svg>
          </span>
          <div style="text-align: center;width: 100%"><span>群组列表</span></div>
          <!--添加一个搜索框-->
          <div style="margin-top: 10px;margin-left: 10px;margin-right: 10px">
            <el-input placeholder="搜索" v-model="groupSearchText" clearable></el-input>
          </div>
          <div class="chat-list" style="height: 560px;scrollbar-width: none;
  -ms-overflow-style: none;
  overflow-x: hidden;
  overflow-y: auto;">
            <el-card v-for="(item, index) in groupList" @click.native="select(item,'群组列表')" :key="item.id"
                     shadow="hover" style="margin-top: 10px;" :class="{ selected: selectedItem === item }">
              <div class="user_list_item">
                <el-avatar shape="square" :size="35" v-if="item.avatar !== '' & item.avatar !== undefined"
                           :src="item.avatar" style="vertical-align:middle;"></el-avatar>
                <el-avatar shape="square" :size="35" v-else style="vertical-align:middle;"
                           :src="require('@/assets/image/avatar.jpg')"
                ></el-avatar>
                <div style="margin-left: 15px;">
                  <p style="font-size: 12px;color: black">{{ item.name }}<span
                      style="color: #8a8282">({{ item.memberCount }})</span></p>
                </div>
                <div style="position: sticky;left: 200px">
                  <el-button size="mini" @click="exitGroup(item.id)">退出群聊</el-button>
                </div>
              </div>

            </el-card>
          </div>
        </el-tab-pane>
        <el-tab-pane style="text-align: center" name="watch">
          <span slot="label">
            <svg class="icon" aria-hidden="true" style="font-size: 20px">
              <use xlink:href="#icon-yiguanzhu"></use>
            </svg>
          </span>
          <div style="text-align: center;width: 100%"><span>关注列表</span></div>
          <!--添加一个搜索框-->
          <div style="margin-top: 10px;margin-left: 10px;margin-right: 10px">
            <el-input placeholder="搜索" v-model="watchSearchText" clearable></el-input>
          </div>
          <div class="chat-list" style="height: 560px;scrollbar-width: none;
  -ms-overflow-style: none;
  overflow-x: hidden;
  overflow-y: auto;">
            <el-card v-for="(item, index) in watchList" @click.native="select(item,'关注列表')" :key="item.id"
                     shadow="hover" style="margin-top: 10px;" :class="{ selected: selectedItem === item }">
              <div class="user_list_item">
                <el-avatar shape="square" :size="35" v-if="item.avatar !== '' & item.avatar !== undefined"
                           :src="item.avatar" style="vertical-align:middle;"></el-avatar>
                <el-avatar shape="square" :size="35" v-else style="vertical-align:middle;"
                           :src="require('@/assets/image/avatar.jpg')"
                ></el-avatar>

                <div style="margin-left: 15px;">
                  <p style="font-size: 12px;color: black">{{ item.nickname }}</p>
                </div>
                <div style="position: sticky;left: 200px">
                  <el-button size="mini" @click="unfollowUser(item.id)" style="background-color: rgb(121, 180, 235)">
                    已关注
                  </el-button>
                </div>
              </div>

            </el-card>
          </div>
        </el-tab-pane>
        <el-tab-pane style="text-align: center" name="fans">
          <span slot="label">
            <svg class="icon" aria-hidden="true" style="font-size: 20px">
              <use xlink:href="#icon-ziyuan"></use>
            </svg>
          </span>
          <div style="text-align: center;width: 100%"><span>粉丝列表</span></div>
          <!--添加一个搜索框-->
          <div style="margin-top: 10px;margin-left: 10px;margin-right: 10px">
            <el-input placeholder="搜索" v-model="fansSearchText" clearable></el-input>
          </div>
          <div class="chat-list" style="height: 560px;scrollbar-width: none;
  -ms-overflow-style: none;
  overflow-x: hidden;
  overflow-y: auto;">
            <el-card v-for="(item, index) in fansList" @click.native="select(item,'粉丝列表')" :key="item.id"
                     shadow="hover" style="margin-top: 10px;" :class="{ selected: selectedItem === item }">
              <div class="user_list_item">
                <el-avatar shape="square" :size="35" v-if="item.avatar !== '' & item.avatar !== undefined"
                           :src="item.avatar" style="vertical-align:middle;"></el-avatar>
                <el-avatar shape="square" :size="35" v-else style="vertical-align:middle;"
                           :src="require('@/assets/image/avatar.jpg')"
                ></el-avatar>

                <div style="margin-left: 15px;">
                  <p style="font-size: 12px;color: black">{{ item.nickname }}</p>
                </div>
                <div style="position: sticky;left: 200px">
                  <el-button size="mini" @click="followUser(item.id)" style="background-color: rgb(121, 180, 235)">
                    回关
                  </el-button>
                </div>
              </div>

            </el-card>
          </div>
        </el-tab-pane>
        <el-tab-pane style="text-align: center" name="add">
          <span slot="label">
            <svg class="icon" aria-hidden="true" style="font-size: 20px">
              <use xlink:href="#icon-tianjiahaoyou"></use>
            </svg>
          </span>
          <div style="text-align: center;width: 100%"><span>添加好友</span></div>
          <div style="text-align: left;color: #8a8282;font-size: 14px;margin-top: 20px">可能感兴趣</div>
          <!--推荐用户-->
          <div class="chat-list" style="height: 560px;scrollbar-width: none;
  -ms-overflow-style: none;
  overflow-x: hidden;
  overflow-y: auto;">
            <el-card v-for="(item, index) in RecommendUserList" @click.native="select(item,'添加好友')" :key="item.id"
                     shadow="hover" style="margin-top: 10px;" :class="{ selected: selectedItem === item }">
              <div class="user_list_item">
                <el-avatar shape="square" :size="35" v-if="item.avatar !== '' & item.avatar !== undefined"
                           :src="item.avatar" style="vertical-align:middle;"></el-avatar>
                <el-avatar shape="square" :size="35" v-else style="vertical-align:middle;"
                           :src="require('@/assets/image/avatar.jpg')"
                ></el-avatar>

                <div style="margin-left: 15px;">
                  <p style="font-size: 12px;color: #ff6200">{{ item.nickname }}</p>
                </div>
                <div style="position: sticky;left: 200px">
                  <el-button size="mini" @click="followUser(item.id)">关注</el-button>
                </div>
              </div>

            </el-card>
            <el-card v-for="(item, index) in RecommendGroupList" @click.native="select(item,'添加好友')" :key="item.id"
                     shadow="hover" style="margin-top: 10px;" :class="{ selected: selectedItem === item }">
              <div class="user_list_item">
                <el-avatar shape="square" :size="35" v-if="item.avatar !== '' & item.avatar !== undefined"
                           :src="item.avatar" style="vertical-align:middle;"></el-avatar>
                <el-avatar shape="square" :size="35" v-else style="vertical-align:middle;"
                           :src="require('@/assets/image/avatar.jpg')"
                ></el-avatar>
                <div style="margin-left: 15px;">
                  <p style="font-size: 12px;color: black">{{ item.name }}<span
                      style="color: #8a8282">({{ item.memberCount }})</span></p>
                </div>
                <div style="position: sticky;left: 200px">
                  <el-button size="mini" @click="applyGroup(item.owner.id)">申请</el-button>
                </div>
              </div>

            </el-card>
          </div>


        </el-tab-pane>
        <el-tab-pane style="text-align: center" name="notify">
          <span slot="label">
            <svg class="icon" aria-hidden="true" style="font-size: 20px">
              <use xlink:href="#icon-tongzhi"></use>
            </svg>
          </span>
          <div style="text-align: center;width: 100%"><span>系统通知</span></div>
          <!--添加一个搜索框-->
          <div style="margin-top: 10px;margin-left: 10px;margin-right: 10px">
            <el-input placeholder="搜索" v-model="SysSearchText" clearable></el-input>
          </div>
        </el-tab-pane>

      </el-tabs>
    </div>

    <!--聊天框-->
    <div v-if="acceptUser.name !== ''" class="mess_dialog">
      <!--对话框头部-->
      <div class="dlog_header">
        <el-avatar shape="square" :size="35" v-if="acceptUser.avatar !== '' & acceptUser.avatar !== undefined"
                   :src="acceptUser.avatar" style="margin: 0;margin-left: 20px;margin-right: 10px;"></el-avatar>
        <el-avatar shape="square" :size="35" v-else :src="require('@/assets/image/avatar.jpg')"
                   style="margin: 0;margin-left: 20px;margin-right: 10px;"></el-avatar>
        <span>{{ acceptUser.name }}</span>
      </div>
      <!--对话框内容-->
      <div id="content_overflow" class="dlog_content">
        <div v-for="(item, index) in chatRecordsList" :key="item.id" class="dlog_content_item"
             style="margin-left: 5px;">
          <!--其他用户的消息展示[单聊]-->
          <div v-if="item.sendId !== userInfo.id & acceptUser.type !== 2" class="content_other">
            <div>
              <el-avatar shape="square" :size="35" v-if="acceptUser.avatar !== '' & acceptUser.avatar !== undefined"
                         :src="acceptUser.avatar" style="margin-top: 5px;"></el-avatar>
              <el-avatar shape="square" :size="35" v-else :src="require('@/assets/image/avatar.jpg')"
                         style="margin-top: 5px;"></el-avatar>
            </div>
            <div class="mess_other">
              <div>
                <span style="font-size: 8px;color: red" v-if="item.sendId==1">{{
                    formatDate(item.sendTime)
                  }}   {{ item.sendNickname }}</span>
                <span style="font-size: 8px;" v-else>{{ formatDate(item.sendTime) }}   {{ item.sendNickname }}</span>
              </div>
              <div v-if="item.contentType === 'image'" class="img_left">
                <el-image :src="item.content" :preview-src-list="imgNowList" class="images"></el-image>
              </div>
              <div v-else class="content_other_bgd" @contextmenu.prevent="show_rightMenu_mess_content($event, item)" v-html="getContent(item.content)">
<!--                <span class="mess_content_msg" v-if="item.sendId==1" style="font-weight: 500" v-html="item.content"></span>-->
<!--                <span class="mess_content_msg" v-else v-html="item.content"></span>-->
              </div>
            </div>
          </div>
          <!--其他用户的消息展示[群聊]-->
          <div v-else-if="item.sendId !== userInfo.id & acceptUser.type !== 1" class="content_other">
            <div>
              <el-avatar shape="square" :size="35" v-if="item.sendAvatar !== '' & item.sendAvatar !== undefined"
                         :src="item.sendAvatar" style="margin-top: 5px;"></el-avatar>
              <el-avatar shape="square" :size="35" v-else :src="require('@/assets/image/avatar.jpg')"
                         style="margin-top: 5px;"></el-avatar>
            </div>
            <div class="mess_other">
              <div>
                <span style="font-size: 8px;color: red" v-if="item.sendId==1">{{
                    formatDate(item.sendTime)
                  }}   {{ item.sendNickname }}</span>
                <span style="font-size: 8px;" v-else>{{ formatDate(item.sendTime) }}   {{ item.sendNickname }}</span>
              </div>
              <div v-if="item.contentType === 'image'" class="img_left">
                <el-image :src="item.content" :preview-src-list="imgNowList" class="images"></el-image>
              </div>
              <div v-else class="content_other_bgd" @contextmenu.prevent="show_rightMenu_mess_content($event, item)" v-html="getContent(item.content)">
<!--                <span class="mess_content_msg" v-if="item.sendId==1" style="font-weight: 500" v-html="item.content"></span>-->
<!--                <span class="mess_content_msg" v-else v-html="item.content"></span>-->
              </div>
            </div>
          </div>
          <!--本用户的消息展示-->
          <div v-else-if="item.sendId === userInfo.id " class="content_me">
            <div class="mess_me">
              <div>
                <span style="font-size: 8px;color: red" v-if="item.sendId==1">{{
                    formatDate(item.sendTime)
                  }}   {{ item.sendNickname }}</span>
                <span style="font-size: 8px;" v-else>{{ formatDate(item.sendTime) }}   {{ item.sendNickname }}</span>
              </div>
              <div v-if="item.contentType === 'image'" class="img">
                <el-image :src="item.content" :preview-src-list="imgNowList" class="images"></el-image>
              </div>
              <div v-else class="content_me_bgd" @contextmenu.prevent="show_rightMenu_mess_content($event, item)" v-html="getContent(item.content)">
<!--                <span class="mess_content_msg" v-if="item.sendId==1" style="font-weight: 500" v-html="item.content"></span>-->
<!--                <span class="mess_content_msg" v-else v-html="item.content"></span>-->
              </div>
            </div>
            <div>
              <el-avatar shape="square" :size="35" :src="userInfo.avatar" style="margin-top: 5px;"></el-avatar>
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
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-guilian"></use>
                </svg>
              </div>
              <el-dropdown-menu slot="dropdown">
                <div class="face_list">
                  <div v-for="(item, index) in emojiList" v-bind:key="item.id" class="face-item"
                       @click="emojiDown(index)">
                    {{ item }}
                  </div>
                </div>
              </el-dropdown-menu>
            </el-dropdown>
            <div class="footer_content_menu_img">
              <label for="fileInput">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-tupian"></use>
                </svg>
              </label>
              <input v-show="false" type="file" accept="image/png, image/jpeg" ref="chartImage" id="fileInput"
                     @change="showUpload">
            </div>
          </div>
          <div class="inputDeep">
            <el-input style="border: 0px" type="textarea" :rows="2" v-model="mess"
                      placeholder="按Ctrl + Enter 发送"
                      @keydown.enter.native="keyDown"></el-input>
            <button @click="Wssendmess('message', '')"
                    style="float:right;margin-right:20px;border: none; background-color: transparent; color: rgb(155,158,164);cursor: pointer;font-size: 14px">
              发送
            </button>
          </div>

        </div>
      </div>

    </div>

    <!--添加好友框-->
    <div v-else-if="myaction == 'add'" class="mess_dialog">
      <!--搜索框-->
      <div class="dlog_header">
        <el-input placeholder="搜索" v-model="friendSearchText" clearable></el-input>
        <el-button type="primary" @click="">搜索</el-button>
      </div>
      <!--搜索结果-->
      <div style="text-align: left;color: #8a8282;font-size: 14px;margin-top: 20px">搜索结果：</div>
      <!--居中展示-->


    </div>
    <!--其他显示-->
    <div v-else class="mess_dialog_false">
      <span>欢迎来到tobeyou聊天室，开始你的探索吧！</span>
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
    <!--右键菜单-聊天内容复制-->
    <div class="rightMenu_mess_content" v-if="isShow_rightMenu_mess_content"
         :style="{'left': rightMenu_mess_content_left + 'px', 'top': rightMenu_mess_content_top + 'px'}">
      <el-card class="rightMenu_mess_content_card">
        <div class="rightMenu_mess_content_card_item">
          <el-button @click="onCopy">复制</el-button>
        </div>
        <div class="rightMenu_mess_content_card_item">
          <el-button @click="noused">撤回</el-button>
        </div>
      </el-card>
    </div>

  </div>
</template>
<script>
import {emojiList} from "@/assets/emoji/emoji.json";
import base from '@/api/base'
import moment from "moment/moment";
import userApi from '@/api/user';
import groupApi from "@/api/group";
import MessageApi from "@/api/message";
import cookie from "js-cookie";
import ossApi from "@/api/oss";
import aiApi from "@/api/ai";
import {zanghua} from "@/assets/zh.json";
import mdKatex from '@traptitech/markdown-it-katex'
import MarkdownIt from 'markdown-it'
import "highlight.js/styles/github-dark.min.css";
import ClipboardJS from 'clipboard';
import hljs from "highlight.js";

export default {
  name: 'wsChat',
  data() {
    return {
      aiChatMess: [],//ai聊天信息
      friendSearchText: '',//好友搜索框
      groupSearchText: '',//群组搜索框
      SysSearchText: '',//系统通知搜索框
      watchSearchText: '',//关注搜索框
      fansSearchText: '',//粉丝搜索框
      messageSearchText: '',//会话搜索框
      watchList: [],//关注列表
      watchListCopy: [],//关注列表备份
      fansList: [],//粉丝列表
      fansListCopy: [],//粉丝列表备份
      sensitiveWords: zanghua.split(','),//敏感词列表
      allChatRecords: {},//所有聊天记录
      groupMembers: [],//接收人id列表
      messageList: [],//会话列表
      messageListCopy: [],//会话列表备份
      friendList: [],//好友列表
      friendListCopy: [],//好友列表备份
      groupList: [],//群组列表
      groupListCopy: [],//群组列表备份
      chatRecordsList: [],//消息列表
      myaction: '',
      tabName: 'chat',//tab选中名称
      selectedItem: null,//选中
      RecommendUserList: [],//推荐用户列表
      RecommendGroupList: [],//推荐群组列表
      isShowDialog: false, // 发送图片预览dialog
      imageUrl: '', // 预览图
      files: {}, // 发送的文件
      sendImgLoad: false,// 发送图片按钮加载
      mess: '',//消息内容
      emojiList: emojiList.split(','), // 表情包列表
      imgNowList: [
        // 'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
        // 'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
      ],//图片预览列表
      userInfo: {},//用户信息
      //接收者对象
      acceptUser: {
        name: '', // 对话的用户名或群名
        userId: '', // 对话用户的id
        type: '', // 对话用户类型(个人、群)
        avatar: '', // 对话用户头像
      },
      isShow_rightMenu_mess_content: false,//右键菜单-聊天内容复制
      rightMenu_mess_content_top: 0,
      rightMenu_mess_content_left: 0,
    }
  },
  // 初始化
  created() {
    this.showInfo();
    //获取会话列表
    this.getMessages();
    //获取好友列表
    this.getFriends();
    //获取群组列表
    this.getGroups();
    //获取该用户的所有聊天记录
    this.getAllChatRecords();
    // this.getuserList()
    this.addCloseRightMenu()

  },
  watch: {
    //可以监听messageSearchText的变化，当messageSearchText变化时，进行过滤
    messageSearchText: function (newVal, oldVal) {
      this.messageList = this.messageList.filter(item => item.name.indexOf(newVal) !== -1);
      if (newVal === '') {
        this.messageList = this.messageListCopy;
      }
    },
    friendSearchText: function (newVal, oldVal) {
      this.friendList = this.friendList.filter(item => item.nickname.indexOf(newVal) !== -1);
      if (newVal === '') {
        this.friendList = this.friendListCopy;
      }
    },
    groupSearchText: function (newVal, oldVal) {
      this.groupList = this.groupList.filter(item => item.name.indexOf(newVal) !== -1);
      if (newVal === '') {
        this.groupList = this.groupListCopy;
      }
    },
    watchSearchText: function (newVal, oldVal) {
      this.watchList = this.watchList.filter(item => item.nickname.indexOf(newVal) !== -1);
      if (newVal === '') {
        this.watchList = this.watchListCopy;
      }
    },
    fansSearchText: function (newVal, oldVal) {
      this.fansList = this.fansList.filter(item => item.nickname.indexOf(newVal) !== -1);
      if (newVal === '') {
        this.fansList = this.fansListCopy;
      }
    },
    // 监听当前消息列表，更新时，保持滚动条位于底部
    chatRecordsList: function scrollToBottom() {
      if(this.chatRecordsList){
        //先清空
        this.aiChatMess = []
        this.chatRecordsList.forEach((item) => {
          if(item.sendId === "2"){
            this.aiChatMess.push({
              "role": "assistant",
              "content": item.content
            })
          }else{
            this.aiChatMess.push({
              "role": "user",
              "content": item.content
            })
          }

        })
        // //先查看aiChatMess最后一条是不是自己发的，如果是，直接清空，如果不是，就添加一条
        // if(this.aiChatMess.length > 0){
        //   if(this.aiChatMess[this.aiChatMess.length - 1].role === "user"){
        //     this.aiChatMess = []
        //   }
        // }
      }
      console.log(this.aiChatMess)
      this.$nextTick(() => {
        var message = document.getElementById('content_overflow')
        // 滚动滑钮到滚动条顶部的距离=滚动条的高度
        message.scrollTop = message.scrollHeight
      })
    }
  },
  mounted() {
    if (this.ws) {
      this.ws.addEventListener('open', this.handleWsOpen.bind(this), false)
      this.ws.addEventListener('close', this.handleWsClose.bind(this), false)
      this.ws.addEventListener('error', this.handleWsError.bind(this), false)
      this.ws.addEventListener('message', this.handleWsMessage.bind(this), false)
    }
  },
  methods: {
    getContent(content){
      //如果有代码块
      if(this.hasCodeBlock(content)){
        return this.getMdiText(content)
      }else{
        return content
      }
    },
    hasCodeBlock(text) {
      const regex = /```[\s\S]*?```/g;
      return regex.test(text);
    },
    highlightBlock(str, lang) {
      const codeIndex1 = parseInt(Date.now() + "") + Math.floor(Math.random() * 10000000);
      const codeIndex2 = parseInt(Date.now() + "") + Math.floor(Math.random() * 10000000);

      const clipboard = new ClipboardJS(`#copy-${codeIndex2}`);
      // 复制成功失败的提示
      clipboard.on("success", (e) => {
        this.$message.success("复制成功");
      });
      clipboard.on("error", (e) => {
        this.$message.error("复制失败");
      });

      return `<pre class="pre-code-box"><div class="pre-code-header" style="background-color: rgb(75, 85, 105);
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  color: rgb(255,255,255);
  display: flex;
  font-size: 12px;
  justify-content: space-between;
  line-height: 20px;
  padding: 5px 10px;
  user-select: none;"><span class="code-block-header__lang">${lang}</span><span id="copy-${codeIndex2}" class="code-block-header__copy" data-clipboard-action="copy" data-clipboard-target="#copy${codeIndex1}">复制代码</span></div><div class="pre-code" style="margin: 0;
  padding: 0;
  overflow: auto;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-all;
  word-wrap: break-word;
  position: relative;"><code id="copy${codeIndex1}" class="hljs code-block-body ${lang}">${str}</code></div></pre>`
    },
    getMdiText(value) {
      var _this = this
      const mdi = new MarkdownIt({
        linkify: true,
        highlight(code, language) {
          const validLang = !!(language && hljs.getLanguage(language))
          if (validLang) {
            const lang = language ?? ''
            return _this.highlightBlock(hljs.highlight(code, { language: lang, ignoreIllegals: true })
                .value, lang)
          }
          return _this.highlightBlock(hljs.highlightAuto(code).value, '')
        }
      })
      mdi.use(mdKatex, {blockClass: 'katexmath-block rounded-md p-[10px]', errorColor: ' #cc0000'})
      return mdi.render(value)
    },
    applyGroup(userId) {
      //提示
      this.$confirm('确定申请加入该群聊吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        //创建系统消息
        let obj = JSON.stringify({
          sendId: this.userInfo.id,
          type: 1,
          acceptId: userId,
          sendAvatar: this.userInfo.avatar,
          sendNickname: this.userInfo.nickname,
          contentType: 'system',
          content: '申请加入群聊',
          sendTime: moment().format('YYYY-MM-DD HH:mm:ss')
        })
        this.ws.send(obj);
        this.$message({
          type: 'success',
          message: '申请成功'
        });
        //新增系统会话记录
        //todo

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消申请'
        });
      });
    },
    //退出群聊
    exitGroup(id) {
      //提示
      this.$confirm('确定要退出该群聊吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userApi.quitGroup(id).then((res) => {
          if (res.data.code === 20000) {
            this.$message.success('退出成功');
            //刷新群组列表
            this.getGroups();
          } else {
            this.$message.error(res.data.message);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        });
      });
    },
    //关注用户
    followUser(id) {
      //提示
      this.$confirm('是否关注该用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userApi.followUser(id).then((res) => {
          if (res.data.code === 20000) {
            this.$message.success('关注成功');
            //刷新推荐列表
            this.getRecommend();
          } else {
            this.$message.error(res.data.message);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消关注'
        });
      });

    },
    //取关用户
    unfollowUser(id) {
      //提示
      this.$confirm('确定要取关该用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userApi.unFollow(id).then((res) => {
          if (res.data.code === 20000) {
            this.$message.success('取关成功');
            //刷新好友列表
            this.getFriends();
          } else {
            this.$message.error(res.data.message);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消取关'
        });
      });

    },
    filterSensitiveWords(text) {
      for (var i = 0; i < this.sensitiveWords.length; i++) {
        //构造正则表达式，g表示全局匹配，i表示不区分大小写
        //要模糊匹配的敏感词
        var regExp = new RegExp(this.sensitiveWords[i], 'gi');
        //text有多少个敏感词就替换多少个*
        text = text.replace(regExp, '***');
      }
      return text;
    },
    getAllChatRecords() {
      MessageApi.getAllChatRecords().then((res) => {
        this.allChatRecords = res.data.data.map.allRecords;
        //遍历所有聊天记录，将脏话置换为*
        //获取脏话列表
        for (let key in this.allChatRecords) {
          this.allChatRecords[key].forEach((item) => {
            if (item.contentType === 'message') {
              item.content = this.filterSensitiveWords(item.content);
            }
          })
        }
      })
    },
    formatDate(date) {
      const createTime = new Date(date);
      const year = createTime.getFullYear();
      const month = ("0" + (createTime.getMonth() + 1)).slice(-2);
      const day = ("0" + createTime.getDate()).slice(-2);
      const hour = ("0" + createTime.getHours()).slice(-2);
      const minute = ("0" + createTime.getMinutes()).slice(-2);
      const second = ("0" + createTime.getSeconds()).slice(-2);
      const formattedDateTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
      return formattedDateTime;
    },
    showInfo() {
      const userStr = cookie.get("ws_userInfo");
      //把字符串转换成json对象
      if (userStr) {
        this.userInfo = JSON.parse(userStr);
      }
      //获取websocket
      this.getwebsocket();
      // console.log(this.userInfo);
    },
    getChatRecords() {
      // console.log(this.acceptUser)
      MessageApi.getChatRecords(this.acceptUser.userId, this.acceptUser.type).then((res) => {
        if (res.data.data.chatRecord) {
          this.chatRecordsList = res.data.data.chatRecord;
        } else {
          this.chatRecordsList = []
        }
        // console.log(res);
      })
    },
    getMessages() {
      MessageApi.getMessageList().then((res) => {
        this.messageList = res.data.data.messageList;
        this.messageListCopy = res.data.data.messageList;
        // console.log(res);
      })
    },
    getGroups() {
      groupApi.getGroupList().then((res) => {
        this.groupList = res.data.data.groupList;
        this.groupListCopy = res.data.data.groupList;
        // console.log(res);this.
      })
    },
    getWatch() {
      userApi.getFollowList().then((res) => {
        this.watchList = res.data.data.list;
        this.watchListCopy = res.data.data.list;
        // console.log(res);
      })
    },
    getFans() {
      userApi.getFansList().then((res) => {
        this.fansList = res.data.data.list;
        this.fansListCopy = res.data.data.list;
        // console.log(res);
      })
    },
    getFriends() {
      userApi.getFriendList().then((res) => {
        this.friendList = res.data.data.list;
        this.friendListCopy = res.data.data.list;
        // console.log(res);
      })
    },
    getGroupMembers() {
      groupApi.getGroupMemberList(this.acceptUser.userId).then((res) => {
        this.groupMembers = res.data.data.groupMember;
        // console.log(res);
      })
    },
    handleClick(tab, event) {
      // console.log(tab, event);
      if (tab.name == 'add') {
        if (this.myaction != "add") {
          //将acceptUser置空
          this.acceptUser = {
            name: '', // 对话的用户名或群名
            userId: '', // 对话用户的id
            type: '', // 对话用户类型(个人、群)
            avatar: '', // 对话用户头像
          }
        }
        // 触发‘添加好友’事件
        this.addFriend();
        //查询推荐列表
        this.getRecommend();
      } else if (tab.name == 'chat') {
        if (this.myaction != "chat") {
          //将acceptUser置空
          this.acceptUser = {
            name: '', // 对话的用户名或群名
            userId: '', // 对话用户的id
            type: '', // 对话用户类型(个人、群)
            avatar: '', // 对话用户头像
          }
        }
        this.toChat();
      } else if (tab.name == 'friend') {
        if (this.myaction != "friend") {
          //将acceptUser置空
          this.acceptUser = {
            name: '', // 对话的用户名或群名
            userId: '', // 对话用户的id
            type: '', // 对话用户类型(个人、群)
            avatar: '', // 对话用户头像
          }
        }
        this.toFriends();
      } else if (tab.name == 'group') {
        if (this.myaction != "group") {
          //将acceptUser置空
          this.acceptUser = {
            name: '', // 对话的用户名或群名
            userId: '', // 对话用户的id
            type: '', // 对话用户类型(个人、群)
            avatar: '', // 对话用户头像
          }
        }
        this.toGroups();
      } else if (tab.name == 'watch') {
        if (this.myaction != "watch") {
          //将acceptUser置空
          this.acceptUser = {
            name: '', // 对话的用户名或群名
            userId: '', // 对话用户的id
            type: '', // 对话用户类型(个人、群)
            avatar: '', // 对话用户头像
          }
        }
        this.toWatch();
      } else if (tab.name == 'fans') {
        if (this.myaction != "fans") {
          //将acceptUser置空
          this.acceptUser = {
            name: '', // 对话的用户名或群名
            userId: '', // 对话用户的id
            type: '', // 对话用户类型(个人、群)
            avatar: '', // 对话用户头像
          }
        }
        this.toFans();
      }
    },
    toFans() {
      this.myaction = 'fans';
      //如果粉丝列表为空，进行初始化
      if (this.fansList.length === 0) {
        this.getFans();
      }
    },
    toWatch() {
      this.myaction = 'watch';
      //如果群组列表为空，进行初始化
      if (this.watchList.length === 0) {
        this.getWatch();
      }
    },
    toGroups() {
      this.myaction = 'group';
      //如果群组列表为空，进行初始化
      if (this.groupList.length === 0) {
        this.getGroups();
      }
    },
    toFriends() {
      this.myaction = 'friend';
      //如果好友列表为空，进行初始化
      if (this.friendList.length === 0) {
        this.getFriends();
      }
    },
    toChat() {
      this.myaction = 'tochat';
    },
    addFriend() {
      this.myaction = 'add';
    },
    select(item, type) {
      // console.log(item)
      if (type == '聊天列表') {
        // alert('聊天列表')
        this.acceptUser = {
          name: item.name, // 对话的用户名或群名
          userId: item.acceptId, // 对话用户的id
          type: item.type, // 对话用户类型(个人、群)
          avatar: item.avatar, // 对话用户头像
        }
        // 获取聊天记录
        // this.getChatRecords()
        this.chatRecordsList = this.allChatRecords[item.acceptId + "type-" + item.type];
        //将对应的会话聊天记录中的图片url放入imgNowList
        this.chatRecordsList.forEach((item) => {
          if (item.contentType === 'image') {
            this.imgNowList.push(item.content)
          }
        })
        //如果是群聊，还需要获取群里的人员列表
        if (item.type == 2) {
          this.getGroupMembers();
        }
      } else if (type == '好友列表') {
        // alert('好友列表')
        this.acceptUser = {
          name: item.nickname, // 对话的用户名或群名
          userId: item.id, // 对话用户的id
          type: 1, // 对话用户类型(个人、群)
          avatar: item.avatar, // 对话用户头像
        }
        // 获取聊天记录
        // this.getChatRecords()
        // console.log(item);
        this.chatRecordsList = this.allChatRecords[item.id + "type-" + 1];
        // console.log(item.id);
        // console.log(this.allChatRecords);
      } else if (type == '群组列表') {
        // alert('群组列表')
        this.acceptUser = {
          name: item.name, // 对话的用户名或群名
          userId: item.id, // 对话用户的id
          type: 2, // 对话用户类型(个人、群)
          avatar: item.avatar, // 对话用户头像
        }
        // console.log(item);

        // 获取聊天记录
        // this.getChatRecords()
        this.chatRecordsList = this.allChatRecords[item.id + "type-" + 2];
        this.getGroupMembers();
      } else if (type == '添加好友') {
        // alert('添加好友')

      }
      this.selectedItem = item;
    },

    handleWsOpen() {
      console.log('WebSocket2已经打开!')
      var user = this.userInfo;
      setInterval(function (ws, id) {
        if (ws.readyState === 1) {
          var obj = JSON.stringify({
            sendId: id,
            acceptId: id,
            acceptMember: '',
            sendAvatar: user.avatar,
            sendNickname: user.nickname,
            contentType: 'ping',
            type: 1,
            sendTime: moment().format('YYYY-MM-DD HH:mm:ss')
          })
          ws.send(obj)
          console.log('WebSocket2发送心跳!')
        }
      }, 5000, this.ws, this.userInfo.id)
    },
    handleWsClose(e) {
      console.log('WebSocket2关闭!')
      // console.log(e)
    },
    handleWsError(e) {
      this.$message.error('意外错误，weksocket关闭，请刷新网页重试！')

      // console.log(e)
    },
    handleWsMessage(e) {
      var obj = JSON.parse(e.data)

      if (obj.contentType === 'ping') {
        console.log('WebSocket2收到心跳!')
      } else {
        console.log('WebSocket2收到消息!')
      }
      // console.log(e.data)
      // 获取内容

      /** 接收类型-聊天内容 */
      if (obj.contentType === 'message' || obj.contentType === 'image') {
        //现在能收到消息，说明在线，需要实时将最后一条消息更新到会话列表
        // this.updateLastMess(obj);

        if (obj.acceptId !== this.userInfo.id) {
          // console.log(obj.accept_id)
          // 强制刷新
          this.$forceUpdate()
          // 判断是否和该对象的聊天内容为空，如果是需要初始化一下
          if (!this.allChatRecords[obj.acceptId + "type-" + obj.type]) {
            this.allChatRecords[obj.acceptId + "type-" + obj.type] = [];
            this.allChatRecords[obj.acceptId + "type-" + obj.type].push(obj);
            if (this.acceptUser.userId === obj.acceptId && this.acceptUser.type === obj.type) {
              this.chatRecordsList = this.allChatRecords[obj.acceptId + "type-" + obj.type]
            }
          } else {
            this.allChatRecords[obj.acceptId + "type-" + obj.type].push(obj)
          }
        } else {
          // 强制刷新
          this.$forceUpdate()
          // 判断是否和该对象的聊天内容为空，如果是需要初始化一下
          if (!this.allChatRecords[obj.sendId + "type-" + obj.type]) {
            this.allChatRecords[obj.sendId + "type-" + obj.type] = [];
            this.allChatRecords[obj.sendId + "type-" + obj.type].push(obj);
            if (this.acceptUser.userId === obj.sendId && this.acceptUser.type === obj.type) {
              this.chatRecordsList = this.allChatRecords[obj.sendId + "type-" + obj.type]
            }
          } else {
            this.allChatRecords[obj.sendId + "type-" + obj.type].push(obj)
          }
        }
      } else if (obj.contentType === 'system') {
        /** 接收类型-好友申请 */
        // let content = {
        //   id: obj.send_id,
        //   name: obj.send_name,
        //   avatar_url: obj.send_avatar
        // }
        // 强制刷新
        // this.pendingList.push(content)
        this.$notify({
          title: '提示',
          message: '用户:"' + obj.sendNickname + '"--申请加入群聊!',
          type: 'success'
        })
      }
    },
    //更新会话列表的最后一条消息
    updateLastMess(obj) {
      this.messageList.forEach((item) => {
        if (item.acceptId === obj.acceptId && item.type === obj.type) {
          if (obj.contentType === 'image') {
            item.lastMess = '[图片]';
          } else {
            item.lastMess = obj.content;
          }
          item.lastTime = moment().format('MM-DD');
          //更新redis
          MessageApi.saveMessageToRedis(item).then((res) => {
            // console.log(res);
          })
        }
      })
    },
    // 初始化websocket
    getwebsocket() {
      // console.log(this.userInfo);
      if ('WebSocket' in window) {
        const token = cookie.get('ws_token')
        this.ws = new WebSocket(base.ip, [token]);//用于创建 WebSocket 对象。WebSocketTest对应的是java类的注解值
        // this.ws = new WebSocket(base.ip);
      } else {
        alert('当前浏览器 Not support websocket')
      }
    },
    /**
     * 添加关闭右键菜单的监听器
     */
    addCloseRightMenu() {
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
    // 功能还未启用提示
    noused() {
      this.$message.warning('撤回功能正在开发中!')
    },
    // 复制功能
    onCopy() {
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
// 聊天图片发送
    WssendImg() {
      this.sendImgLoad = true
      let formData = new FormData()
      formData.append('file', this.files)
      //将图片上传到oss
      ossApi.uploadFileToOss(formData).then((res) => {
        this.sendImgLoad = false
        if (res.data.code === 20000) {
          this.isShowDialog = false
          this.Wssendmess('image', res.data.data.url)
        } else {
          this.$message.error(res.data.message)
        }
      }).catch((err) => {
        this.sendImgLoad = false
        this.$message.error("图片上传失败")
      })
    },
    /**
     * enter发送消息，ctrl+enter换行
     * @param e
     * @returns {boolean}
     */
    keyDown(e) {
      if (e.ctrlKey && e.keyCode === 13) { // 用户点击了ctrl+enter触发
        this.mess += '\n'
      } else { // 用户点击了enter触发
        this.Wssendmess('message', '')
        e.preventDefault() // 阻止浏览器默认换行操作
        return false
      }
    },
    // 关闭图片的dialog事件
    diaClose() {
      this.isShowDialog = false// 关闭窗口
    },
    // 时间戳转换
    chatTime(timestamp) {
      let date = new Date(timestamp * 1000)
      let Y = date.getFullYear() + '-'
      let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
      let D = date.getDate() + ' '
      let h = date.getHours() + ':'
      let m = date.getMinutes() + ':'
      let s = date.getSeconds()
      return Y + M + D + h + m + s
    },
    // 右键菜单-聊天内容-事件
    show_rightMenu_mess_content(e, item) {
      this.isShow_rightMenu_mess_content = true
      // 定位
      this.rightMenu_mess_content_top = e.pageY
      this.rightMenu_mess_content_left = e.pageX
      // 获取当前选中内容
      this.rightMenu_mess_content_item = item
    },
    // 表情包点击事件
    emojiDown(index) {
      this.mess = this.mess + this.emojiList[index]
    },
    // 选中图片后初步守卫
    showUpload() {
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
    imgSaveToUrl(file) {
      // 获取上传图片的本地URL，用于上传前的本地预览，转换后的地址为 blob:http://xxx/7bf54338-74bb-47b9-9a7f-7a7093c716b5
      // console.log(file);
      this.imageUrl = URL.createObjectURL(file)
      console.log('图片预览地址：', this.imageUrl)
    },
    // 聊天发送按钮点击事件
    async Wssendmess(contentType, content) {
      let message = this.mess;
      if (contentType === 'image') {
        message = content
        console.log("发送的图片消息：" + message);
      }
      if (message !== '') {
        // 发送消息格式
        //遍历this.groupMembers获取ids
        let ids = []
        if (this.acceptUser.type === 2) {
          this.groupMembers.forEach((item) => {
            ids.push(item.id)
          })
        }



        let obj = JSON.stringify({
          sendId: this.userInfo.id,
          type: this.acceptUser.type,
          acceptId: this.acceptUser.userId,
          acceptMember: ids,
          sendAvatar: this.userInfo.avatar,
          sendNickname: this.userInfo.nickname,
          contentType: contentType,
          content: message,
          sendTime: moment().format('YYYY-MM-DD HH:mm:ss')
        })

        //发送消息
        console.log("发送消息")
        this.ws.send(obj)

        this.aiChatMess.push({
          "role":"user",
          "content":message
        })
        //如果对方是AI助理(ai助理的id为2)
        if (this.acceptUser.userId === "2" && this.acceptUser.type === 1) {
          //如果超过15条，删除前面的
          if(this.aiChatMess.length > 15){
            this.aiChatMess.splice(0, this.aiChatMess.length - 15)
          }
          // var toAiObj = {
          //   "userId": this.userInfo.id,
          //   "messageDTOList": this.aiChatMess
          // }
          var toAiObj = {
            "model": "gpt-3.5-turbo",
            "messageDTOList": this.aiChatMess
          }
          aiApi.aiChat(toAiObj).then((res) => {
            // console.log(res);
            let aiMess = res.data.choices[0].message.content;
            //发送消息
            let obj = JSON.stringify({
              sendId: "2",
              type: 1,
              acceptId: this.userInfo.id,
              acceptMember: ids,
              sendAvatar: this.acceptUser.avatar,
              sendNickname: this.acceptUser.name,
              contentType: "message",
              // content: res.data.result,
              content: aiMess,
              sendTime: moment().format('YYYY-MM-DD HH:mm:ss')
            })
            this.ws.send(obj)
          })
        }




        //判断会话列表有没有该用户的会话
        let isHave = false;
        this.messageList.forEach((item) => {
          if (item.acceptId === this.acceptUser.userId) {
            isHave = true;
          }
        })
        let lastMess = this.mess;
        if (contentType === 'image') {
          lastMess = '[图片]'
        }
        //如果没有，就添加一条
        if (!isHave) {
          if (this.acceptUser.type == 1) {
            let obj = {
              userId: this.userInfo.id,
              acceptId: this.acceptUser.userId,
              groupUserIds: ids,
              avatar: this.acceptUser.avatar,
              name: this.acceptUser.name,
              type: this.acceptUser.type,
              lastMess: lastMess,
              lastTime: moment().format('MM-DD'),
            }
            this.messageList.push(obj)
            //保存到redis
            MessageApi.saveMessageToRedis(obj).then((res) => {
              // console.log(res);
            })
            // 对方的会话列表也要添加一条
            let obj2 = {
              userId: this.acceptUser.userId,
              acceptId: this.userInfo.id,
              avatar: this.userInfo.avatar,
              name: this.userInfo.nickname,
              type: this.acceptUser.type,
              lastMess: lastMess,
              lastTime: moment().format('MM-DD'),
            }
            MessageApi.saveMessageToRedis(obj2).then((res) => {
              // console.log(res);
            })
          } else {
            let obj = {
              userId: this.userInfo.id,
              acceptId: this.acceptUser.userId,
              groupUserIds: ids,
              avatar: this.acceptUser.avatar,
              name: this.acceptUser.name,
              type: this.acceptUser.type,
              lastMess: lastMess,
              lastTime: moment().format('MM-DD'),
            }
            this.messageList.push(obj)
            //保存到redis
            MessageApi.saveMessageToRedis(obj).then((res) => {
              // console.log(res);
            })
          }
        }
        // 发送完消息，清空输入框
        this.mess = ''
      }
    },
    //发送系统消息
    async sendSysMess(contentType, content) {

    },
    getRecommend() {
      console.log("获取推荐用户列表")
      userApi.getRecommendUserList().then((res) => {
        this.RecommendUserList = res.data.data.list;
      })
      console.log("获取推荐群聊列表")
      groupApi.getRecommendGroupList().then((res) => {
        this.RecommendGroupList = res.data.data.list;
      })
    }
  }

}
</script>
<style scoped>
.pre-code-box{
  /*border-radius: 4px;*/
  /*border: 1px solid #eaecef;*/
  /*background-color: #f6f8fa;*/
  /*margin: 0;*/
  /*padding: 0;*/
  /*overflow: auto;*/
  /*font-size: 14px;*/
  /*line-height: 1.5;*/
  /*word-break: break-all;*/
  /*word-wrap: break-word;*/
  /*position: relative;*/
}

.pre-code-box .pre-code-header{
  background-color: #f6f8fa;
  border-bottom: 1px solid #eaecef;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  color: #6a737d;
  display: flex;
  font-size: 12px;
  justify-content: space-between;
  line-height: 20px;
  padding: 5px 10px;
  user-select: none;
}

.code-block-header__lang{
  color: #6a737d;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
  font-size: 12px;
  line-height: 20px;
}

.code-block-header__copy{
  color: #6a737d;
  cursor: pointer;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
  font-size: 12px;
  line-height: 20px;
}

.pre-code{
  margin: 0;
  padding: 0;
  overflow: auto;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-all;
  word-wrap: break-word;
  position: relative;
}

.code-block-body{
  margin: 0;
  padding: 16px;
  background-color: #f6f8fa;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  color: #24292e;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
  font-size: 14px;
  line-height: 1.5;
  overflow: auto;
  word-break: normal;
  word-wrap: normal;
}

.code-block-body .hljs-comment,
.code-block-body .hljs-quote{
  color: #6a737d;
}
.selected {
  background-color: rgb(209, 222, 240);
}

.text-overflow {
  font-size: 10px;
  margin: 0;
  margin-top: 4px;
  color: #a19f9f;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px; /* 或者根据实际情况设置合适的宽度 */
}


::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}

/* 如果你的 el-input type 设置成textarea ，就要用这个了 */
.inputDeep >>> .el-textarea__inner {
  border: 0;
  resize: none; /* 这个是去掉 textarea 下面拉伸的那个标志，如下图 */
}

.user_list_item {
  display: flex;
  align-items: center;
}

.mess {
  margin: 0 auto;
  border-radius: 5px;
  width: 1200px;
  height: 620px;
  border: 1px #8a8282;
  box-shadow: 0 0 10px #9b9393;
  background-color: white;
  display: flex;
}

.mess_dialog_false {
  width: 100%;
  height: 100%;
  text-align: center;
  line-height: 600px;
  background: #fbfcfc;
  font-size: 24px;
}

.mess_dialog {
  width: 100%;
  height: 100%;

}

.dlog_footer {
  width: 100%;
}

.left-menu {
  width: 500px;
  height: 100%;
  border-right: 1px solid #cecdcd;
  /*background: #d3d3d3;*/
}

.chat-content {
  width: 100%;
  padding: 20px;
}

.chat-content .word {
  display: flex;
  margin-bottom: 20px;
}

.chat-content .word img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.chat-content .word .info {
  margin-left: 10px;
}

.chat-content .word .info .time {
  font-size: 12px;
  color: rgba(51, 51, 51, 0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
  margin-top: -5px;
}

.chat-content .word .info .info-content {
  padding: 10px;
  font-size: 14px;
  background: #00fdaf;
  position: relative;
  margin-top: 8px;
}

.chat-content .word .info .info-content::before {
  position: absolute;
  left: -8px;
  top: 8px;
  content: '';
  border-right: 10px solid #00fdaf;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}

.chat-content .word-my {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.chat-content .word-my img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.chat-content .word-my .info {
  width: 90%;
  margin-left: 10px;
  text-align: right;
}

.chat-content .word-my .info .time {
  font-size: 12px;
  color: rgba(51, 51, 51, 0.8);
  margin: 0;
  height: 20px;
  line-height: 20px;
  margin-top: -5px;
  margin-right: 10px;
}

.chat-content .word-my .info .info-content {
  max-width: 70%;
  padding: 10px;
  font-size: 14px;
  float: right;
  margin-right: 10px;
  position: relative;
  margin-top: 8px;
  background: #A3C3F6;
  text-align: left;
}

.chat-content .word-my .info .info-content::after {
  position: absolute;
  right: -8px;
  top: 8px;
  content: '';
  border-left: 10px solid #A3C3F6;
  border-top: 8px solid transparent;
  border-bottom: 8px solid transparent;
}

.el-card /deep/ .el-card__body {
  padding: 10px 20px;
}

.user_addfrid:hover .icon-friend-add {
  color: #3d9aff;
}

.user_addfrid:active .icon-friend-add {
  color: #0b7bf5;
}


.mess_icon_chart:hover .icon-chart {
  color: #3d9aff;
}

.mess_icon_chart:active .icon-chart {
  color: #0b7bf5;
}


.mess_icon_figures:hover .icon-figures {
  color: #3d9aff;
}

.mess_icon_figures:active .icon-figures {
  color: #0b7bf5;
}

.mess_icon_circle:hover .icon-circle {
  color: #3d9aff;
}

.mess_icon_circle:active .icon-circle {
  color: #0b7bf5;
}

.dlog_header {
  width: 100%;
  height: 60px;
  border-bottom: 1px solid #cecdcd;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.dlog_content {
  width: 100%;
  height: 400px;
  border-bottom: 1px solid #cecdcd;
  overflow-y: auto;
  overflow-x: hidden;
}

.rightMenu_mess_content_card /deep/ .el-card__body {
  padding: 0;
}

.rightMenu_mess_content_card /deep/ .el-button {
  border: 0;
  padding: 12px 30px;
}

.footer_content {
  margin: 10px;
}

.footer_content_menu {
  display: flex;
  height: 30px;
  width: 100%;
}

.footer_content_menu_face {
  width: 30px;
  height: 30px;
  text-align: center;
  font-size: 20px;
}

.footer_content_menu_face:hover .icon-face {
  color: #01a3fc;
}

.footer_content_menu_img {
  width: 30px;
  height: 30px;
  text-align: center;
  font-size: 20px;
  margin-left: 5px;
}

.footer_content_menu_img:hover .icon-img {
  color: #01a3fc;
}

.icon-img {
  color: #0a0a0a;
  font-size: 25px;
}

.face_list {
  width: 300px;
  height: 200px;
  display: flex;
  flex-wrap: wrap;
  overflow-y: auto;
  overflow-x: hidden;
}

.face-item {
  width: 30px;
  height: 30px;
  border: solid 1px #e0e0e0;
  margin: 0 -1px -1px 0; /*解决相邻border看起来很粗问题*/
  text-align: center;
  font-size: 20px;
}

.face-item:hover {
  background: #eeeeee;
}

.user_head {
  height: 60px;
  width: 100%;
  border-bottom: 1px solid #cecdcd;
}


.cardisfriend_user p {
  margin: 0px;
  padding: 5px 0px;
}


.content_other {
  width: 70%;
  display: flex;
  justify-content: flex-start;
  margin: 11px 18px;
}

.mess_other {
  text-align: left;
  margin-left: 10px;
}

.content_me {
  width: 70%;
  display: flex;
  justify-content: flex-end;
  float: right;
  margin: 11px 18px;
}

.mess_me {
  text-align: right;
  margin-right: 10px;
}

.mess_content_msg {
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
  top: 10px;
  left: -8px;
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
  /*height: auto;*/
  line-height: 30px;
  background: #f2f6fc;
  z-index: 0;
  text-align: left;
}

.content_me_bgd::after {
  border-style: solid;
  border-width: 0 0 11px 11px;
  border-color: transparent transparent transparent #f6f8fa;
  content: "";
  position: absolute;
  top: 10px;
  right: -8px;
  margin-top: -10px;
  display: block;
  width: 0px;
  height: 0px;
  z-index: -1;
}

.el-form-item /deep/ .el-form-item__error {
  margin-left: 500px;
}

/*上传图片预览的dialog*/
.img_left {
  text-align: left;
}

.img {
  text-align: right;
}

.images {
  height: 150px;
  fit: contain;
}

.dialogUpload /deep/ .el-dialog {
  width: 400px;
  text-align: center;
}

.dialogUpload /deep/ .el-dialog__body {
  padding: 10px;
}

.icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

/*右键菜单css样式*/
.rightMenu_mess_content {
  position: fixed;
}

.rightMenu_mess_content_card /deep/ .el-card__body {
  padding: 0;
}

.rightMenu_mess_content_card /deep/ .el-button {
  border: 0;
  padding: 12px 30px;
}

.rightMenu_mess_content_card_item {
  border-bottom: solid 1px #eeeded;
}

</style>
