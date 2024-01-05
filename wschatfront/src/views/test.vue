<template>
  <div v-html="htmlStr"></div>
</template>
<script>
import mdKatex from '@traptitech/markdown-it-katex'
import MarkdownIt from 'markdown-it'
import "highlight.js/styles/github-dark.min.css";
import ClipboardJS from 'clipboard';
import hljs from "highlight.js";

export default {
  data() {
    return {
      htmlStr: ''
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

      return `<pre class="pre-code-box"><div class="pre-code-header"><span class="code-block-header__lang">${lang}</span><span id="copy-${codeIndex2}" class="code-block-header__copy" data-clipboard-action="copy" data-clipboard-target="#copy${codeIndex1}">复制代码</span></div><div class="pre-code"><code id="copy${codeIndex1}" class="hljs code-block-body ${lang}">${str}</code></div></pre>`
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
      this.htmlStr = mdi.render(value)
    }
  }
}
</script>

<style>
.pre-code-box{
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

.pre-code-header{
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





</style>

