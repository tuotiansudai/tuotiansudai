<!DOCTYPE html>
<html lang="en">
<#import "macro/global.ftl" as global>
<@global.head title="个人资料" pageCss="${css.global}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="mainFrame PersonalInfo">
    <aside class="menuBox fl">
        <ul class="menu-list">
            <li><a href="javascript:" class="active">账户总览</a></li>
            <li><a href="javascript:">投资记录</a></li>
            <li><a href="javascript:">债权转让</a></li>
            <li><a href="javascript:">资金管理</a></li>
            <li><a href="javascript:">个人资产</a></li>
            <li><a href="javascript:" >自动投标</a></li>
            <li><a href="javascript:">积分红包</a></li>
            <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="contentContainer fr autoHeight">
        <h4 class="columnTitle"><em class="tc">个人资料</em></h4>
        <div class="pad-m">
            <ul class="InfoList" id="personInfoBox">
                <li><span class="infoTitle"> 用户名</span>
                    <em class="info">${loginName}</em>

                </li>
                <li><span class="infoTitle"> 姓名</span>
                    <em class="info">${userName}</em>
                    <span class="BindingSet">
                       <i class="fa fa-check-circle ok"></i> 已认证
                    </span>
                </li>
                <li><span class="infoTitle"> 身份认证</span>
                    <em class="info">${identityNumber}</em>
                    <span class="BindingSet">
                       <i class="fa fa-check-circle ok"></i> 已认证
                    </span>
                </li>
                <li><span class="infoTitle"> 手机</span>
                    <em class="info">${mobile}</em>
                    <span class="BindingSet">
                       <i class="fa fa-check-circle ok"></i> 已绑定
                    </span>
                </li>
                <li><span class="infoTitle"> 邮箱</span>
                    <em class="info">绑定邮箱后，您可及时了解交易情况及拓天速贷的最新动态</em>
                    <span class="BindingSet">
                       <i class="fa fa-times-circle no"></i> 未绑定 <a class="setlink setEmail" href="javascript:void(0);">绑定</a>
                    </span>
                </li>
                <li><span class="infoTitle"> 绑定银行卡</span>
                    <#if bankCard??>
                    <em class="info">${bankCard}</em>
                    <span class="BindingSet">
                        <i class="fa fa-check-circle ok"></i> 已绑定
                    </span>
                    <#else>
                    <em class="info">绑定银行卡后，您可以进行快捷支付和提现操作</em>
                    <span class="BindingSet">
                        <i class="fa fa-times-circle no"></i> 未绑定 <a class="setlink setBankCard" href="${requestContext.getContextPath()}/bind-card">绑定</a>
                    </span>
                    </#if>
                </li>
                <li><span class="infoTitle"> 密码</span>
                    <em class="info">********</em>
                    <span class="BindingSet">
                       <i class="fa fa-check-circle ok"></i> 已设置 <a class="setlink setPass" href="javascript:void(0);">修改</a>
                    </span>
                </li>
            </ul>
         </div>
    </div>
</div>

<div id="changePassDOM" class="pad-m popLayer" style="display: none;">
    <form name="changePasswordForm" action="${requestContext.getContextPath()}/personal-info/change-password" method="post">
        <dl>
            <dt class="requireOpt">请输入原密码 </dt>
            <dd><input type="password" id="originalPassword" name="originalPassword" class="input-control"  placeholder="请输入密码" > </dd>
        </dl>
        <dl>
            <dt class="requireOpt">请输入新密码 </dt>
            <dd><input type="password" id="newPassword" name="newPassword" class="input-control"  placeholder="6位至20位数字与字母组合" > </dd>
        </dl>
        <dl>
            <dt class="requireOpt">请确认新密码 </dt>
            <dd><input type="password" id="newPasswordConfirm" name="newPasswordConfirm" class="input-control"  placeholder="6位至20位数字与字母组合" > </dd>
        </dl>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-normal" id="btnChangePass">确认修改</button>
    </form>
</div>

<div id="changeEmailDOM" class="pad-m popLayer" style="display: none;">
    <form name="changeEmailForm">
        <dl>
            <dt class="requireOpt">请输入邮箱 </dt>
            <dd><input type="email" name="email" class="input-control"  placeholder="请输入邮箱"> </dd>
        </dl>
            <button type="submit" class="btn btn-normal" id="btnChangeEmail">绑定</button>
    </form>
</div>

<div id="CESuccess" class="pad-m popLayer" style="display: none;">
    验证邮箱已发送到 <span class="msgTip"> 825809454@qq.com</span> 请进入邮箱进行验证完成
</div>
<div id="CEFailed" class="pad-m popLayer" style="display: none;">
    错误
</div>

<#include "footer.ftl">
<@global.javascript pageJavascript="${js.personal_info}">
</@global.javascript>

</body>
</html>