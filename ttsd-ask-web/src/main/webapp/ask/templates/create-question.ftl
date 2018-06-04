<#import "macro/global.ftl" as global>

<@global.main pageCss="${(css.mainSite)!}" pageJavascript="${(js.mainSite)!}" title="拓天问答_投资问答_拓天速贷" keywords="投资问答,网贷问答,投资知识,金融问答" description="拓天速贷投资问答系统,为您解答金融行业最新最快的投资知识,让您放心投资、安全投资,拓天速贷为投资人答疑解惑.">
<div class="article-content fl" id="createQuestion">
    <div class="ask-question-box clearfix">
        <div class="m-title">我要提问</div>
        <div class="ask-question-con">
            <form id="questionForm" name="questionForm" class="form-question" method="POST">
                <dl class="ask-question-list">
                    <dt>一句话描述您的问题</dt>
                    <dd class="clearfix">
                    <@global.isAnonymous>
                        <span class="isAnonymous ask-con">请 <a href="${webServer}/login">登录</a> 或 <a href="${webServer}/register/user">注册</a> 后再进行提问</span>
                    </@global.isAnonymous>
                    <@global.isNotAnonymous>
                        <input type="text" name="question" placeholder="请描述您的问题" class="ask-con question">
                    </@global.isNotAnonymous>
                        
                        <span class="error fl" style="display: none">请描述您的问题</span>
                        <span class="words-tip fr"><em>0</em>/100</span>

                    </dd>
                    <dt>问题补充（选填）</dt>
                    <dd class="clearfix">
                        <@global.isAnonymous>
                            <span class="isAnonymous addition">请注意不要随意透漏您的个人信息</span>
                        </@global.isAnonymous>
                        <@global.isNotAnonymous>
                            <textarea name="addition" class="addition" placeholder="请注意不要随意透漏您的个人信息"></textarea>
                        </@global.isNotAnonymous>
                        <span class="error fl" style="display: none"></span>
                        <span class="words-tip fr"><em>0</em>/2000</span>
                    </dd>
                    <dt>选择问题分类 <i class="fr">最多选择三个分类</i></dt>
                    <dd class="tag-list">
                        <@global.isAnonymous>
                            <#list tags as tag>
                                <span class="isAnonymous tag"> ${tag.description}</span>
                            </#list>
                        </@global.isAnonymous>

                        <@global.isNotAnonymous>
                            <#list tags as tag>
                                <input type="checkbox" class="tag hide" name="tags" value="${tag.name()}" id="${tag.name()}">
                                <label for='${tag.name()}' class="tag">${tag.description}</label>
                            </#list>
                        </@global.isNotAnonymous>
                        <div class="error tag-category clearfix" style="display: none">请选择分类</div>
                    </dd>
                    <dd class="dd-captcha">
                        <@global.isAnonymous>
                            <span class="isAnonymous captcha">请输入验证码</span>
                        </@global.isAnonymous>
                        <@global.isNotAnonymous>
                            <input type="text" placeholder="请输入验证码" class="captcha input-short" name="captcha" maxlength="5">
                        </@global.isNotAnonymous>
                        <img src="${global.applicationContext}/captcha" class="captchaImg" alt="" id="imageCaptcha">
                        <span class="error " style="display: none">请输入验证码</span>
                    </dd>
                    <dd class="tc ask-button">
                        <@global.isAnonymous>
                            <span class="btn">提问</span>
                        </@global.isAnonymous>

                        <@global.isNotAnonymous>
                            <button class="btn formSubmit" name="btnQuestion" disabled type="button">提问</button>
                        </@global.isNotAnonymous>

                    </dd>
                </dl>
            </form>
        </div>
    </div>
</div>

</@global.main>