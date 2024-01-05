<template>
  <div class="page">
    <div class="begintitle">
      <h1 v-show="!list.length" @click="send">TEST</h1>
    </div>

    <div id="myList">
      <div
          v-show="item.text"
          :class="item.type === 0 ? 'problemList' : 'answerList'"
          v-for="item in list"
      >
        <img class="listImg" :src="item.avatar" alt=""/>
        <div v-html="item.text" class="listText"></div>
      </div>

      <div v-show="loading" class="answerList">
        <img class="listImg" src="/logo.jpg" alt=""/>
        <img class="addin" src="/loading.gif" alt=""/>
      </div>
    </div>

    <div v-show="!list.length" class="exhibition">
      <div class="witem">
        <svg
            class="w-6 h-6 m-auto"
            fill="none"
            height="1em"
            stroke="currentColor"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="1.5"
            viewBox="0 0 24 24"
            width="1em"
            xmlns="http://www.w3.org/2000/svg"
        >
          <circle cx="12" cy="12" r="5"></circle>
          <line x1="12" x2="12" y1="1" y2="3"></line>
          <line x1="12" x2="12" y1="21" y2="23"></line>
          <line x1="4.22" x2="5.64" y1="4.22" y2="5.64"></line>
          <line x1="18.36" x2="19.78" y1="18.36" y2="19.78"></line>
          <line x1="1" x2="3" y1="12" y2="12"></line>
          <line x1="21" x2="23" y1="12" y2="12"></line>
          <line x1="4.22" x2="5.64" y1="19.78" y2="18.36"></line>
          <line x1="18.36" x2="19.78" y1="5.64" y2="4.22"></line>
        </svg>
        <h3 class="title">实例</h3>
        <p>"用简单的术语解释量子计算"</p>
        <p>"10岁的生日有什么创意吗？"</p>
        <p>"如何在Javascript中提出HTTP请求？"</p>
      </div>

      <div class="witem">
        <svg
            aria-hidden="true"
            class="w-6 h-6 m-auto"
            fill="none"
            stroke="currentColor"
            stroke-width="1.5"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
        >
          <path
              d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z"
              stroke-linecap="round"
              stroke-linejoin="round"
          ></path>
        </svg>
        <h3 class="title">功能</h3>
        <p>还记得用户在对话中早些时候说的话</p>
        <p>允许用户提供后续更正</p>
        <p>接受过拒绝不当请求的培训</p>
      </div>

      <div class="witem">
        <svg
            class="w-6 h-6 m-auto"
            fill="none"
            height="1em"
            stroke="currentColor"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="1.5"
            viewBox="0 0 24 24"
            width="1em"
            xmlns="http://www.w3.org/2000/svg"
        >
          <path
              d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"
          ></path>
          <line x1="12" x2="12" y1="9" y2="13"></line>
          <line x1="12" x2="12.01" y1="17" y2="17"></line>
        </svg>
        <h3 class="title">限制</h3>
        <p>偶尔可能会生成错误的信息</p>
        <p>偶尔可能会产生有害的指令或有偏见的内容</p>
        <p>对2021年后的世界和事件的了解有限</p>
      </div>
    </div>
    <div class="steppingstone"></div>

    <div class="inputbox">
      <el-input
          @keydown.enter.native="send"
          v-bind:readonly="loading"
          @keypress="handleEnter"
          tabindex="0"
          data-id="root"
          rows="1"
          v-model="question"
          type="text"
          id="message"
          placeholder="输入你的指令"
      />

      <div class="btn-send" id="submit-btn" @click="send">
        <div class="send-view" style="display: flex">
          <svg
              stroke="currentColor"
              fill="none"
              stroke-width="2"
              viewBox="0 0 24 24"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="h-4 w-4 mr-1"
              height="1.5em"
              width="1.5em"
              xmlns="http://www.w3.org/2000/svg"
          >
            <line x1="22" y1="2" x2="11" y2="13"></line>
            <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
          </svg>
        </div>
        <div class="send-loading" style="display: none">
          <div></div>
          <div></div>
          <div></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import mdKatex from '@traptitech/markdown-it-katex'
import MarkdownIt from 'markdown-it'
import "highlight.js/styles/github-dark.min.css";
import ClipboardJS from 'clipboard';
import hljs from "highlight.js";
import aiApi from "@/api/ai";

export default {
  data() {
    return {
      htmlStr: '',
      list: [],
      question: '',
      loading: false,
      isMobile: false,
      aiChatMess: [],
    }
  },
  mounted() {
    this.getMdiText("```java\n" +
        "@Component\n" +
        "public class BaseWoVarGetter extends AbstractVarGetter {\n" +
        "    @Resource\n" +
        "    protected BaseWoLineinfoService woLineinfoService;\n" +
        "    @Resource\n" +
        "    protected BaseWoBillService woBillService;\n" +
        "\n" +
        "    @Override\n" +
        "    protected void initFunctions() {\n" +
        "        this.addFunction(WoFlowVar.WO_LINEINFO, WoLineinfo.class,\n" +
        "                         bill -> this.woLineinfoService.getByBillId(bill.getBillId(), GlobalConstant.YESNO_N));\n" +
        "        this.addFunction(WoFlowVar.WO_BILL, WoBill.class, bill -> this.woBillService.getById(bill.getBillId()));\n" +
        "    }\n" +
        "\n" +
        "    @Override\n" +
        "    public String getBusinessCode() {\n" +
        "        return BaseWoConstants.BUSINESS_CODE;\n" +
        "    }\n" +
        "}\n" +
        "```") //htmlStr就是已经包含html样式的文本
  },
  methods: {
    setScreen() {
      setTimeout(() => {
        window.scrollTo(0, document.body.scrollHeight);
      }, 0);
    },
    handleEnter(e) {
      if (e.key === "Enter" && !this.isMobile && !e.shiftKey) {
        send();
      }
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
            return _this.highlightBlock(hljs.highlight(code, {language: lang, ignoreIllegals: true})
                .value, lang)
          }
          return _this.highlightBlock(hljs.highlightAuto(code).value, '')
        }
      })
      mdi.use(mdKatex, {blockClass: 'katexmath-block rounded-md p-[10px]', errorColor: ' #cc0000'})
      return mdi.render(value)
    },
    async send() {
      //判断是否回复
      if (this.loading) return;

      const message = this.question;
      if (message == "") {
        this.$message.error("问题不能为空！");
        this.loading = false;
        return;
      }
      this.list.push({
        text: message,
        avatar: "/avatar.jpeg",
      });//显示用户聊天记录

      //定位页面位置
      this.setScreen();
      this.question = "";
      this.loading = true;

      this.list.push({
        text: "",
        avatar: "/logo.jpg",
      });//显示机器人聊天记录
      this.aiChatMess.push({
        "role": "user",
        "content": message
      })
      const toAiObj = {
        "model": "gpt-3.5-turbo",
        "messageDTOList": this.aiChatMess
      }
      // aiApi.aiChat(toAiObj).then((res) => {
      //   let aiMess = res.data.choices[0].message.content;
      //   this.aiChatMess.push({
      //     "role":"assistant",
      //     "content":aiMess
      //   })
      //   this.list[this.list.length - 1].text = this.getMdiText(aiMess);
      //   this.loading = false;
      //   this.setScreen();
      // })

      //流式请求
      const res = await fetch('http://localhost:8080/ai/chat/gpt/stream', {
        method: 'POST',
        dataType: "text/event-stream",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(toAiObj)
      })
      const reader = res.body.getReader()
      let decoder = new TextDecoder();
      let result = '';
      let flag = true;
      while (flag) {
        const {done, value} = await reader.read();
        if (done) {
          console.log("Stream ended");
          flag = false;
          break;
        }
        this.list[this.list.length - 1].text += decoder.decode(value);
      }
      this.list[this.list.length - 1].text = this.getMdiText(this.list[this.list.length - 1].text);
      this.aiChatMess.push({
        "role":"assistant",
        "content":this.list[this.list.length - 1].text
      })
      this.loading = false;
      this.setScreen();
    }
  }

}
</script>

<style scoped>
.pre-code-box {
  border-radius: 4px;
  border: 1px solid #eaecef;
  background-color: #f6f8fa;
  margin: 0;
  padding: 0;
  overflow: auto;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-all;
  word-wrap: break-word;
  position: relative;
}

.pre-code-header {
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

.code-block-header__lang {
  color: #6a737d;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
  font-size: 12px;
  line-height: 20px;
}

.code-block-header__copy {
  color: #6a737d;
  cursor: pointer;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
  font-size: 12px;
  line-height: 20px;
}

.pre-code {
  margin: 0;
  padding: 0;
  overflow: auto;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-all;
  word-wrap: break-word;
  position: relative;
}

.code-block-body {
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
.code-block-body .hljs-quote {
  color: #6a737d;
}

.page {
  position: relative;
  height: 100vh;
}

.defbut {
  position: fixed;
  right: 2px;
  bottom: 152px;
}

.btn-send {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 32px;
  border-radius: 6px;
  color: rgba(0, 0, 0, 0.6);
  background: rgba(0, 0, 0, 0.1);
}

.btn-send:hover {
  cursor: pointer;
  opacity: 0.85;
}

.text {
  position: absolute;
  top: 50px;
  border: 1px solid #e5e5e5;
  height: 60px;
  padding: 10px;
  width: 90%;
}

#myList {
  max-width: 1000px;
  margin: 0 auto;
  overflow-x: hidden;
  overflow-y: auto;
}

.problemList {
  display: flex;
  padding: 0px 200px;
}

.answerList {
  position: relative;
  padding: 20px 18px;
  font-size: 15px;
  display: flex;
  overflow-x: auto;
  white-space: pre-wrap;
  border-top: 1px solid #e5e5e5;
  border-bottom: 1px solid #e5e5e5;
}

.listImg {
  margin-top: 5px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.listText {
  margin-left: 20px;
  padding-top: 10px;
  width: 100%;
  white-space: pre-wrap;
}

.inputbox {
  position: fixed;
  bottom: 30px;
  left: 0;
  right: 0;
  margin: auto;
  width: 90%;
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  padding-right: 12px;
  background: #fff;
  border-radius: 8px;
}

.inputbox input {
  flex-grow: 1;
  height: 44px;
  max-height: 100px;
  border: 0;
  outline: none;
  padding: 12px 15px;
  background: transparent;
  font-size: 16px;
  width: 100%;
  font-weight: bold;
  color: rgba(0, 0, 0, 0.7);
}

.inputbox button {
  margin-left: 15px;
  width: 56px;
  height: 82%;
  border-radius: 6px;
  border: 0;
  background: silver;
  color: #333;
  font-size: 14px;
  outline: none;
}

.inputbox button:hover {
  cursor: pointer;
  opacity: 0.8;
}

.addin {
  margin: 10px 20px;
  width: 30px;
  height: 30px;
}

.steppingstone {
  width: 100%;
  height: 160px;
}

.begintitle {
  width: 100%;
  /* padding: 50px 50px 30px 50px; */
}

.begintitle h1 {
  padding: 50px;
  font-size: 28px;
  font-weight: bold;
  text-align: center;
}

.exhibition {
  width: 80%;
  margin: auto;
  display: flex;
  justify-content: space-around;
}

.witem p {
  margin: auto;
  padding: 10px;
  margin-top: 15px;
  font-size: 16px;
  border-radius: 5px;
  text-align: center;
}

.witem h3 {
  padding: 15px;
  font-size: 20px;
  color: #606266;
  text-align: center;
}

textarea {
  border: none;
  resize: none;
  cursor: pointer;
  outline: none;
  overflow-y: hidden;
}

@media screen and (max-width: 600px) {
  .text {
    position: absolute;
    top: 30px;
    border: 1px solid #e5e5e5;
    height: 45px;
    padding: 10px;
    width: 80%;
  }
}
</style>

