<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.personal_info}" activeNav="我的账户" activeLeftNav="个人资料" title="个人资料">
<div class="content-container auto-height personal-info">
    <h4 class="column-title"><em class="tc">个人资料</em></h4>

     <ul class="info-list" id="personInfoBox">
                <li><span class="info-title"> 用户名</span>
                    <em class="info">${loginName}</em>

                </li>
                <li><span class="info-title"> 姓名</span>
                    <em class="info">${userName}</em>
                    <span class="binding-set">
                       <i class="fa fa-check-circle ok"></i> 已认证
                    </span>
                </li>
                <li><span class="info-title"> 身份认证</span>
                    <em class="info">${identityNumber?replace("^\\d{3}(\\d{3}).*$","***$1************","r")}</em>
                    <span class="binding-set">
                       <i class="fa fa-check-circle ok"></i> 已认证
                    </span>
                </li>
                <li><span class="info-title"> 手机</span>
                    <em class="info">${mobile?replace("^(\\d{3}).*(\\d{4})$","$1****$2","r")}</em>
                    <span class="binding-set">
                       <i class="fa fa-check-circle ok"></i> 已绑定
                    </span>
                </li>
                <li><span class="info-title"> 邮箱</span>
                    <#if email?? && email != "">
                        <em class="info">${email}</em>
                    <span class="binding-set">
                        <i class="fa fa-check-circle ok"></i> 已绑定 <a class="setlink setEmail" href="javascript:">修改</a>
                    </span>
                    <#else>
                        <em class="info">绑定邮箱后，您可及时了解交易情况及拓天速贷的最新动态</em>
                    <span class="binding-set">
                       <i class="fa fa-times-circle no"></i> 未绑定 <a class="setlink setEmail" href="javascript:">绑定</a>
                    </span>
                    </#if>


                </li>
                <li><span class="info-title"> 绑定银行卡</span>
                    <#if bankCard??>
                        <em class="info">${bankCard?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</em>
                    <span class="binding-set">
                        <i class="fa fa-check-circle ok"></i> 已绑定 <a class="setlink setBankCard" href="${requestContext.getContextPath()}/bind-card">修改</a>
                    </span>
                    <#else>
                        <em class="info">绑定银行卡后，您可以进行快捷支付和提现操作</em>
                    <span class="binding-set">
                        <i class="fa fa-times-circle no"></i> 未绑定 <a class="setlink setBankCard" href="${requestContext.getContextPath()}/bind-card">绑定</a>
                    </span>
                    </#if>
                </li>
                <li><span class="info-title"> 登录密码</span>
                    <em class="info">********</em>
                    <span class="binding-set">
                       <i class="fa fa-check-circle ok"></i> 已设置 <a class="setlink setPass" href="javascript:void(0);">修改</a>
                    </span>
                </li>
                <li><span class="info-title"> 支付密码</span>
                    <em class="info">********</em>
                    <span class="binding-set">
                       <i class="fa fa-check-circle ok"></i> 已设置 <a class="setlink setUmpayPass" href="javascript:void(0);">重置</a>
                    </span>
                </li>
                <li><span class="info-title"> 免密投资</span>

                    <#if noPasswordInvest>
                        <em class="info">您已开启免密投资，投资理财快人一步</em>
                        <span class="binding-set">
                            <i class="fa fa-check-circle ok"></i> 已开启  <a class="setlink setTurnOffNoPasswordInvest" href="javascript:void(0);">关闭</a>
                        </span>

                    <#elseif autoInvest>
                        <em class="info">您已授权自动投标，可直接开启免密投资，及时选择心仪标的，理财快人一步</em>
                        <span class="binding-set">
                            <i class="fa fa-times-circle no"></i> 未开启  <a class="setlink setNoPasswordInvest" data-url="/no-password-invest/enabled" href="javascript:void(0);">开启</a>
                        </span>
                    <#else >
                        <em class="info">开启免密投资后，您可及时选择心仪标的，理财快人一步</em>
                        <span class="binding-set">
                            <i class="fa fa-times-circle no"></i> 未开启  <a class="setlink setTurnOnNoPasswordInvest" href="javascript:void(0);">开启</a>
                        </span>
                    </#if>

                </li>
            </ul>

</div>

<div id="resetUmpayPassDOM" class="pad-m popLayer" style="display: none;">
    <form name="resetUmpayPasswordForm" action="${requestContext.getContextPath()}/personal-info/reset-umpay-password" method="post">
        <dl class="identityCodeTitle" align="center">
            通过身份证号重置支付密码
        </dl>
        <dl>
            <dt class="requireOpt">请输入您的身份证号</dt>
            <dd><input type="text" id="identityNumber" name="identityNumber" class="input-control"></dd>
        </dl>
        <dl class="identityCodeError" >
            <dt>您输入的身份证号与当前账号不符，请重新输入。</dt>
        </dl>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-normal">确认重置</button>
    </form>
</div>

<div id="successUmpayPass" class="pad-m popLayer" style="display: none;">
    <dl>
        <dt>您的支付密码已被重置，请注意查收相关短信，查看新密码！</dt>
    </dl>
    <button type="button" class="btn btn-normal" id="readUmpayPass">我已查看</button>
</div>

<div id="changePassDOM" class="pad-m popLayer" style="display: none;">
    <form name="changePasswordForm" action="${requestContext.getContextPath()}/personal-info/change-password" method="post">
        <dl>
            <dt class="requireOpt">请输入原密码</dt>
            <dd><input type="password" id="originalPassword" name="originalPassword" class="input-control" placeholder="请输入密码"></dd>
        </dl>
        <dl>
            <dt class="requireOpt">请输入新密码</dt>
            <dd><input type="password" id="newPassword" name="newPassword" class="input-control" placeholder="6位至20位，不能全为数字"></dd>
        </dl>
        <dl>
            <dt class="requireOpt">请确认新密码</dt>
            <dd><input type="password" id="newPasswordConfirm" name="newPasswordConfirm" class="input-control" placeholder="6位至20位，不能全为数字"></dd>
        </dl>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-normal">确认修改</button>
    </form>
</div>

<div id="changeEmailDOM" class="pad-m popLayer" style="display: none;">
    <form name="changeEmailForm" action="${requestContext.getContextPath()}/bind-email" method="post">
        <dl>
            <dt class="requireOpt">请输入邮箱</dt>
            <dd><input type="email" name="email" class="input-control" placeholder="请输入邮箱"></dd>
        </dl>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-normal">绑定</button>
    </form>
</div>

<div id="change-email-success" class="pad-m popLayer" style="display: none;">
    验证邮件已发送至您的邮箱，请登录邮箱完成验证。
</div>

<div id="turnOnNoPasswordInvestDOM" class="pad-m popLayer" style="display: none;">
    <form name="turnOnNoPasswordInvestForm" action="${requestContext.getContextPath()}/no-password-invest" method="post" target="_blank">
        <div  class="tc text-m">推荐您开通免密投资功能，简化投资过程，理财快人一步。</div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="noPasswordInvest" value="true"/>
        <div class="tc person-info-btn" style="margin-top:50px;">
            <button class="btn  btn-cancel btn-close btn-close-turn-on" type="button" >取消</button>
            <button class="btn btn-success btn-turn-on" type="submit" >去联动优势授权</button>
        </div>
    </form>
</div>
<div id="turnOffNoPasswordInvestDOM" class="pad-m popLayer" style="display: none; ">
    <form id="imageCaptchaForm" name="imageCaptchaForm" action="${requestContext.getContextPath()}/no-password-invest/send-no-password-invest-captcha" method="post">
        <dl>
            <dt >推荐您开通免密投资功能，简化投资过程，理财快人一步，确认关闭吗？</dt>
            <dd>
                <span >图形验证码：</span>
                <input type="text" class="input-control image-captcha-text"  name="imageCaptcha" maxlength="5" placeholder="请输入图形验证码"/>
                <img src="/no-password-invest/image-captcha" alt="" class="image-captcha"/>
                <input type="hidden" name="mobile" value="${mobile}"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </dd>
        </dl>
    </form>
    <form id="turnOffNoPasswordInvestForm" name="turnOffNoPasswordInvestForm" action="${requestContext.getContextPath()}/no-password-invest/disabled" method="post">
        <dl>
            <dd class="code-number code-number-hidden" >验证码发送到${mobile?replace("^(\\d{3}).*(\\d{4})$","$1****$2","r")}</dd>
            <dd>
                <span >短信验证码：</span>
                <input type="captcha" name="captcha" class="input-control captcha" placeholder="请输入短信验证码" maxlength="6">
                <input type="hidden" name="mobile" value="${mobile}"/>
                <button type="button" class="btn btn-normal get-captcha" disabled="disabled" data-url= "/no-password-invest/send-no-password-invest-captcha">获取验证码</button>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </dd>
        </dl>
        <div class="error-content" style="visibility: visible; height:30px;text-align:left"></div>
        <div class="tc person-info-btn">
            <button class="btn btn-success btn-cancel" >取消</button>
            <button class="btn btn-close" type="submit" >我要关闭</button>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
<div id="noPasswordInvestDOM" class="pad-m" style="display: none;">
    <p>请在新打开的联动优势页面充值完成后选择：</p>
    <p><a href="/personal-info" class="btn-success" data-category="确认成功" data-label="noPasswordInvest">继续</a>(授权后视情况可能会有一秒或更长的延迟)</p>
    <span>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-20:00）</span>
</div>



</@global.main>


