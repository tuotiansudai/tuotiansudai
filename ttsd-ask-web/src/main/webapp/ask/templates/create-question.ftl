<#import "macro/global.ftl" as global>
<@global.main pageCss="${(css.main)!'main.css'}" pageJavascript="${(js.main)!'main.js'}">
<div class="article-content fl" id="createQuestion">
    <div class="ask-question-box clearfix">
        <div class="m-title">我要提问</div>
        <div class="ask-question-con">
            <form name="askQuestion" class="form-question">
                <dl class="ask-question-list">
                    <dt>一句话描述您的问题</dt>
                    <dd class="clearfix">
                        <input type="text" name="question" placeholder="请 登录 或 注册 后再进行提问" class="ask-con">
                        <span class="error fl">sds</span> <span class="words-tip fr">0/30</span>
                    </dd>
                    <dt>问题补充（选填）</dt>
                    <dd class="clearfix">
                        <textarea name="addition" class="addition" placeholder="请注意不要随意透漏您的个人信息"></textarea>
                        <span class="words-tip fr">0/10000</span>
                    </dd>
                    <dt>选择问题分类 <i class="fr">最多选择三个分类</i></dt>
                    <dd class="tag-list">
                        <span class="tag checked">证券</span>
                        <#list tags as tag>
                            <span class="tag">${tag.description}</span>
                        </#list>
                    </dd>
                    <dd class="dd-captcha">
                        <input type="text" placeholder="请输入验证码" class="captcha input-short" name="captcha">
                        <img src="${askServer}/captcha" alt="">
                        <span class="error ">sds</span>
                    </dd>
                    <dd class="tc ask-button">
                        <button class="btn formSubmit">提问</button>
                    </dd>
                </dl>
            </form>
        </div>
    </div>
</div>

</@global.main>