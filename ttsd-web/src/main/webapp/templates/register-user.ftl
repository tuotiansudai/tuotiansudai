<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<#import "macro/global.ftl" as global>
<@global.head title="注册" pageCss="${css.register}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="register-container page-width">
    <ul class="step-tab">
        <li class="first on"><s></s>1 注册<g></g></li>
        <li><s></s>2 实名验证<g></g></li>
        <li class="last"><s></s>3 充值投资<g></g></li>
    </ul>
    <div class="clear-blank"></div>
    <div class="register-step-one register-box">
        <ul class="reg-list tl">
            <form class="register-user-form" action="/register/user" method="post">
                <li>
                    <input type="text" class="login-name input-control" class="" name="loginName" placeholder="请输入用户名" maxlength="25" value="${(originalFormData.loginName)!}" />
                </li>
                <li>
                    <input type="text" name="mobile" class="mobile" placeholder="请输入手机号" maxlength="11" value="${(originalFormData.mobile)!}" />
                </li>
                <li class="captcha-tag">
                    <input type="text" name="captcha" class="captcha" placeholder="请输入验证码"  maxlength="6" value="${(originalFormData.captcha)!}"/>
                    <button class="fetch-captcha grey" disabled="disabled">获取验证码</button>
                </li>
                <li>
                    <input type="password" name="password" placeholder="请输入密码" maxlength="20" class="password" value="${(originalFormData.password)!}"/>
                </li>
                <li>
                    <input type="text" name="referrer" placeholder="请输入推荐人（选填）" maxlength="25" class="referrer" value="${(originalFormData.referrer)!}"/>
                </li>
                <li>
                    <input type="checkbox" name='agreement' class='agreement-check' checked="checked" />
                    <span class="agreement-title">
                        同意拓天速贷
                        <a href="javascript:;" class="show-agreement">《服务协议》</a>
                    </span>
                </li>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <#if success?? && success == false>
                    <div class="register-error">注册失败，请检查您提交的信息是否正确！</div>
                </#if>

                <input type="submit" class="register-user" value="下一步"/>
            </form>
        </ul>
    </div>
</div>
<div class="agreement-dialog"></div>
<div class="agreement">
    <h3>拓天速贷服务协议</h3>

    <p>央广网北京6月11日消息（记者刘乐）中共中央总书记、国家主席习近平11日在人民大会堂会见了由主席昂山素季率领的缅甸全国民主联盟代表团。</p>

    <p>习近平指出，中缅是亲密友好的邻居。建交65年来，中缅传统友谊历经风雨从未改变，各领域务实合作成果丰富，成为休戚与共的利益共同体和命运共同体。这是两国历代领导人和两国人民共同努力的结果，要倍加珍惜。</p>

    <p>
        习近平强调，中方始终从战略高度和长远角度看待中缅关系，支持缅甸维护主权独立和领土完整，尊重缅甸自主选择发展道路，支持缅甸民族和解进程，坚定不移推进中缅传统友好和务实合作。希望并且相信，缅方在中缅关系问题上的立场也将是一贯的，无论国内形势如何变化，都将积极致力于推动中缅友好关系发展。</p>

    <p>央广网北京6月11日消息（记者刘乐）中共中央总书记、国家主席习近平11日在人民大会堂会见了由主席昂山素季率领的缅甸全国民主联盟代表团。</p>

    <p>习近平指出，中缅是亲密友好的邻居。建交65年来，中缅传统友谊历经风雨从未改变，各领域务实合作成果丰富，成为休戚与共的利益共同体和命运共同体。这是两国历代领导人和两国人民共同努力的结果，要倍加珍惜。</p>

    <p>
        习近平强调，中方始终从战略高度和长远角度看待中缅关系，支持缅甸维护主权独立和领土完整，尊重缅甸自主选择发展道路，支持缅甸民族和解进程，坚定不移推进中缅传统友好和务实合作。希望并且相信，缅方在中缅关系问题上的立场也将是一贯的，无论国内形势如何变化，都将积极致力于推动中缅友好关系发展。</p>

    <p>央广网北京6月11日消息（记者刘乐）中共中央总书记、国家主席习近平11日在人民大会堂会见了由主席昂山素季率领的缅甸全国民主联盟代表团。</p>

    <p>习近平指出，中缅是亲密友好的邻居。建交65年来，中缅传统友谊历经风雨从未改变，各领域务实合作成果丰富，成为休戚与共的利益共同体和命运共同体。这是两国历代领导人和两国人民共同努力的结果，要倍加珍惜。</p>

    <p>
        习近平强调，中方始终从战略高度和长远角度看待中缅关系，支持缅甸维护主权独立和领土完整，尊重缅甸自主选择发展道路，支持缅甸民族和解进程，坚定不移推进中缅传统友好和务实合作。希望并且相信，缅方在中缅关系问题上的立场也将是一贯的，无论国内形势如何变化，都将积极致力于推动中缅友好关系发展。</p>

    <p>央广网北京6月11日消息（记者刘乐）中共中央总书记、国家主席习近平11日在人民大会堂会见了由主席昂山素季率领的缅甸全国民主联盟代表团。</p>

    <p>习近平指出，中缅是亲密友好的邻居。建交65年来，中缅传统友谊历经风雨从未改变，各领域务实合作成果丰富，成为休戚与共的利益共同体和命运共同体。这是两国历代领导人和两国人民共同努力的结果，要倍加珍惜。</p>

    <p>
        习近平强调，中方始终从战略高度和长远角度看待中缅关系，支持缅甸维护主权独立和领土完整，尊重缅甸自主选择发展道路，支持缅甸民族和解进程，坚定不移推进中缅传统友好和务实合作。希望并且相信，缅方在中缅关系问题上的立场也将是一贯的，无论国内形势如何变化，都将积极致力于推动中缅友好关系发展。</p>

    <p>央广网北京6月11日消息（记者刘乐）中共中央总书记、国家主席习近平11日在人民大会堂会见了由主席昂山素季率领的缅甸全国民主联盟代表团。</p>

    <p>习近平指出，中缅是亲密友好的邻居。建交65年来，中缅传统友谊历经风雨从未改变，各领域务实合作成果丰富，成为休戚与共的利益共同体和命运共同体。这是两国历代领导人和两国人民共同努力的结果，要倍加珍惜。</p>

    <p>
        习近平强调，中方始终从战略高度和长远角度看待中缅关系，支持缅甸维护主权独立和领土完整，尊重缅甸自主选择发展道路，支持缅甸民族和解进程，坚定不移推进中缅传统友好和务实合作。希望并且相信，缅方在中缅关系问题上的立场也将是一贯的，无论国内形势如何变化，都将积极致力于推动中缅友好关系发展。</p>

    <p>央广网北京6月11日消息（记者刘乐）中共中央总书记、国家主席习近平11日在人民大会堂会见了由主席昂山素季率领的缅甸全国民主联盟代表团。</p>

    <p>习近平指出，中缅是亲密友好的邻居。建交65年来，中缅传统友谊历经风雨从未改变，各领域务实合作成果丰富，成为休戚与共的利益共同体和命运共同体。这是两国历代领导人和两国人民共同努力的结果，要倍加珍惜。</p>

    <p>
        习近平强调，中方始终从战略高度和长远角度看待中缅关系，支持缅甸维护主权独立和领土完整，尊重缅甸自主选择发展道路，支持缅甸民族和解进程，坚定不移推进中缅传统友好和务实合作。希望并且相信，缅方在中缅关系问题上的立场也将是一贯的，无论国内形势如何变化，都将积极致力于推动中缅友好关系发展。</p>

    <p>央广网北京6月11日消息（记者刘乐）中共中央总书记、国家主席习近平11日在人民大会堂会见了由主席昂山素季率领的缅甸全国民主联盟代表团。</p>

    <p>习近平指出，中缅是亲密友好的邻居。建交65年来，中缅传统友谊历经风雨从未改变，各领域务实合作成果丰富，成为休戚与共的利益共同体和命运共同体。这是两国历代领导人和两国人民共同努力的结果，要倍加珍惜。</p>

    <p>
        习近平强调，中方始终从战略高度和长远角度看待中缅关系，支持缅甸维护主权独立和领土完整，尊重缅甸自主选择发展道路，支持缅甸民族和解进程，坚定不移推进中缅传统友好和务实合作。希望并且相信，缅方在中缅关系问题上的立场也将是一贯的，无论国内形势如何变化，都将积极致力于推动中缅友好关系发展。</p>

    <p>央广网北京6月11日消息（记者刘乐）中共中央总书记、国家主席习近平11日在人民大会堂会见了由主席昂山素季率领的缅甸全国民主联盟代表团。</p>

    <p>习近平指出，中缅是亲密友好的邻居。建交65年来，中缅传统友谊历经风雨从未改变，各领域务实合作成果丰富，成为休戚与共的利益共同体和命运共同体。这是两国历代领导人和两国人民共同努力的结果，要倍加珍惜。</p>

    <p>
        习近平强调，中方始终从战略高度和长远角度看待中缅关系，支持缅甸维护主权独立和领土完整，尊重缅甸自主选择发展道路，支持缅甸民族和解进程，坚定不移推进中缅传统友好和务实合作。希望并且相信，缅方在中缅关系问题上的立场也将是一贯的，无论国内形势如何变化，都将积极致力于推动中缅友好关系发展。</p>
</div>

<div class="image-captcha-dialog pad-m">
    <form class="image-captcha-form" action="/register/user/send-register-captcha" method="post">
            <img src="/register/user/image-captcha" alt="" class="image-captcha"/>
            <input type="text" class="image-captcha-text" name="imageCaptcha" maxlength="5" placeholder="请输入图形验证码"/>
<div class="clear-blank-m"></div>
        <div class="tc">
            <input type="submit" class="image-captcha-confirm btn-normal" value="确定"/>
        </div>

    </form>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.register_user}">
</@global.javascript>
</body>
</html>