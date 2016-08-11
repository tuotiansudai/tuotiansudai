<#import "macro/global.ftl" as global>
<@global.main pageCss="${(css.main)!'main.css'}" pageJavascript="${(js.main)!'main.js'}">
<div class="article-content fl answer-container">
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
            </dl>
        </div>
    </div>

    <div class="to-answer-box">
        <div class="m-title">我来回答</div>
        <form name="formAnswer" class="formAnswer">
            <dl class="form-answer-in">
                <dd>
                    <textarea rows="4" class="text-area" placeholder="请 登陆 或 注册 后再进行回答"></textarea>
                    <i class="error text-area-error fa fa-times-circle">您的回答过于简短</i>
                </dd>
                <dd>
                    <input type="text" placeholder="请输入验证码" class="captcha" name="captcha">
                    <img src="/captcha" alt="">
                    <button type="button" class="btn fr" disabled >提交答案</button>
                    <i class="error">验证码不正确</i>
                </dd>
            </dl>
        </form>
    </div>

    <#if bestAnswer??>
        <div class="borderBox clearfix margin-top-10">
            <div class="answers-box ">
                <dl class="answers-list">
                    <dd>${bestAnswer.answer}</dd>
                    <dd class="date-time-answer"><span>${bestAnswer.mobile}</span>
                        <span class="datetime">${bestAnswer.createdTime?string("yyyy-MM-dd HH:mm")}</span>
                        <span class="agree-ok ${bestAnswer.favored?string("active", "")} fr">${bestAnswer.favorite}</span>
                    </dd>
                </dl>
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