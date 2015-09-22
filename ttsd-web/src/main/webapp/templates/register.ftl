<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="注册" pageCss="${css.register}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="register">
    <ul>
        <li class="register-step-one-title register-arrow active">1 注册</li>
        <li class="register-step-two-title register-arrow">2 实名验证</li>
        <li class="register-step-three-title register-arrow">3 充值投资</li>
    </ul>
    <div class="register-step-one register-step active">
        <ol>
            <form id="register-user-form">
                <li>
                    <input type="text" name="loginName" placeholder="请输入用户名" maxlength="25" class="login-name"/>
                </li>
                <li>
                    <input type="text" name="mobile" placeholder="请输入手机号" maxlength="11" class="mobile"/>
                </li>
                <li>
                    <input type="text" name="captcha" placeholder="请输入验证码" class="captcha" maxlength="6"/>
                    <button class="fetch-captcha grey"  disabled="disabled">获取验证码</button>
                </li>
                <li>
                    <input type="password" name="password" placeholder="请输入密码" maxlength="20" class="password"/>
                </li>
                <li>
                    <input type="text" name="referrer" placeholder="请输入推荐人（选填）" maxlength="25" class="referrer"/>
                </li>
                <li>
                    <input style="" name='agreement' type="checkbox" class='agreement-check' checked="checked"/>
                    <span class="agreement-title">
                        同意拓天速贷
                        <a href="javascript:;" class="show-agreement">《服务协议》</a>
                    </span>
                </li>
                <input type="submit" class="register-user" value="下一步"/>
            </form>
        </ol>
    </div>
    <div class="register-step-two register-step">
        <ol>
            <form id="register-account-form">
                <li>
                    <input type="text" name="userName" placeholder="请输入您的姓名" class="user-name"/>
                </li>
                <li>
                    <input type="text" name="identityNumber" placeholder="请输入您的身份证" class="identity-number"/>
                </li>
                <input type="hidden" name="loginName" class="login-name"/>
                <input type="hidden" name="mobile" class="mobile"/>
                <input type="submit" class="register-account" value="下一步"/>
            </form>
        </ol>
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
<div class="verification-code"></div>
<div class="verification-code-main">
    <span>手机验证<i class="close">X</i></span>
    <p>
        <input type="text" class="verification-code-text" maxlength="5" placeholder="请输入图形验证码"/>
        <img src="/register/image-captcha" alt="" class="verification-code-img"/>
    </p>
    <b>图形验证码不正确</b>
    <button class="complete grey" disabled="disabled">确定</button>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.register}">
</@global.javascript>
</body>
</html>