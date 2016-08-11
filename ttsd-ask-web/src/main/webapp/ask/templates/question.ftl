<#import "macro/global.ftl" as global>
<@global.main pageCss="${(css.main)!'main.css'}" pageJavascript="${(js.main)!'main.js'}">
<div class="article-content fl answer-container" id="questionDetailTag">
    <div class="borderBox clearfix">
        <div class="answers-box">
            <dl class="answers-list">
                <dt>${question.question}</dt>
                <dd><span>${question.mobile}</span>
                    <span>回答：${question.answers}</span>
                    <span class="datetime">${question.createdTime?string("yyyy-MM-dd HH:mm")}</span> <br/>

                </dd>
                <dd class="tag-answer">
                    <#list question.tags as tag>
                        <span class="tag">${tag.description}</span>
                    </#list>
                <dd>${question.addition!}</dd>
                <dd class="fr clearfix answer-button"><button type="button" class="btn">我来回答</button> </dd>
            </dl>
        </div>
    </div>

    <div class="to-answer-box">
        <div class="m-title">我来回答</div>
        <form name="formAnswer" class="formAnswer">
            <input type="hidden" name="questionId" value="${question.id?string.computer}">
            <dl class="form-answer-in">
                <dd>
                    <@global.isAnonymous>
                        <span class="isAnonymous text-area">请 <a href="${webServer}/login">登录</a> 或 <a href="${webServer}/register/user">注册</a> 后再进行回答</span>
                    </@global.isAnonymous>

                    <@global.isNotAnonymous>
                        <textarea rows="4" class="text-area" placeholder="请回答" name="answer"></textarea>
                        <i class="error text-area-error fa fa-times-circle" style="display: none">您的回答过于简短</i>
                    </@global.isNotAnonymous>


                </dd>
                <dd>
                    <@global.isAnonymous>
                        <span class="isAnonymous captcha">请输入验证码</span>
                        <img src="/captcha" alt="">
                        <span class="is-nologn-btn btn fr">提交答案</span>
                    </@global.isAnonymous>

                    <@global.isNotAnonymous>
                        <input type="text" placeholder="请输入验证码" class="captcha" name="captcha">
                        <img src="/captcha" alt="">
                        <button type="button" class="btn fr" disabled >提交答案</button>
                        <i class="error" style="display: none">验证码不正确</i>
                    </@global.isNotAnonymous>



                </dd>
            </dl>
        </form>
    </div>

    <#if bestAnswer??>
        <div class="borderBox clearfix best-answer">
            <div class="answers-box ">
                <dl class="answers-list">
                    <dd>${bestAnswer.answer}</dd>
                    <dd class="date-time-answer"><span>${bestAnswer.mobile}</span>
                        <span class="datetime">${bestAnswer.createdTime?string("yyyy-MM-dd HH:mm")}</span>
                        <span class="agree-ok ${bestAnswer.favored?string("active", "")} fr">${bestAnswer.favorite}</span>
                    </dd>
                </dl>
                <div class="best-answer-sign"></div>
            </div>
        </div>
    </#if>

<#--ad-->
    <div class="ad-answer"><img src="${staticServer}/images/sign/ad-answer.jpg"></div>

    <div class="borderBox clearfix margin-top-10">
        <div class="answers-box ">
            <div class="other-title">共${question.answers}个回答</div>
            <#list answers as answer>
                <#if bestAnswer?? && answer.id != bestAnswer.id>
                    <dl class="answers-list">
                        <dd>${answer.answer}</dd>
                        <dd class="date-time-answer"><span>${answer.mobile}</span>
                            <span class="datetime">${answer.createdTime?string("yyyy-MM-dd HH:mm")}</span>
                            <span class="agree-ok ${answer.favored?string("active", "")} fr">${answer.favorite}</span>
                            <span class="btn fr">采纳此条信息</span>
                        </dd>
                    </dl>
                </#if>
            </#list>
        </div>
    </div>

</div>
</@global.main>