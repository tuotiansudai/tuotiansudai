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
                    <em class="info">${identityNumber?replace("^(\\d{6}).*(\\d{3}(\\d|x))$","$1****$2","r")}</em>
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
                        <em class="info">${bankCard?replace("^(\\d{6}).*(\\d{4})$","$1****$2","r")}</em>
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
                <li><span class="info-title"> 密码</span>
                    <em class="info">********</em>
                    <span class="binding-set">
                       <i class="fa fa-check-circle ok"></i> 已设置 <a class="setlink setPass" href="javascript:void(0);">修改</a>
                    </span>
                </li>
            </ul>

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

</@global.main>


