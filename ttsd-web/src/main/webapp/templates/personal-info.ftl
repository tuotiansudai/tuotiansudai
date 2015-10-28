<!DOCTYPE html>
<html lang="en">
<#import "macro/global.ftl" as global>
<#--<@global.head title="个人资料" pageCss="${css.global}">-->
<#--</@global.head>-->
<head lang="en">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<title>个人资料</title>
<link rel="stylesheet" type="text/css" href="${requestContext.getContextPath()}/style/global.css">

</head>
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
                    <em class="info">dongshao</em>

                </li>
                <li><span class="infoTitle"> 姓名</span>
                    <em class="info">皮**</em>
                    <span class="BindingSet">
                       <i class="fa fa-check-circle ok"></i> 已认证
                    </span>
                </li>
                <li><span class="infoTitle"> 身份认证</span>
                    <em class="info">412 *** ******** 0111</em>
                    <span class="BindingSet">
                       <i class="fa fa-check-circle ok"></i> 已认证
                    </span>
                </li>
                <li><span class="infoTitle"> 手机</span>
                    <em class="info">186****4768</em>
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
                    <em class="info">12333*****333333</em>
                    <span class="BindingSet">
                       <i class="fa fa-times-circle no"></i> 未绑定 <a class="setlink setBankCard" href="/bind-card">绑定</a>
                    </span>
                </li>
                <li><span class="infoTitle"> 密码</span>
                    <em class="info">********</em>
                    <span class="BindingSet">
                       <i class="fa fa-times-circle no"></i> 已设置 <a class="setlink setPass" href="javascript:void(0);">修改</a>
                    </span>
                </li>
            </ul>
         </div>
    </div>


</div>

<div id="changePassDOM" class="pad-m popLayer" style="display: none;">

    <form name="changePassForm">
        <dl>
            <dt class="requireOpt">请输入原密码：</dt>
            <dd><input type="text" id="oldPassword" name="oldPassword" class="input-control"  placeholder="请输入密码"> </dd>
        </dl>
        <dl>
            <dt class="requireOpt">请输入新密码：</dt>
            <dd><input type="text" id="newPassword" name="newPassword" class="input-control"  placeholder="6位至16位数字与字母组合"> </dd>
        </dl>
        <dl>
            <dt class="requireOpt">请确认新密码：</dt>
            <dd><input type="text" id="newPassword2" name="newPassword2" class="input-control"  placeholder="6位至16位数字与字母组合"> </dd>
        </dl>

            <button type="submit" class="btn btn-normal" id="btnChangePass">确认修改</button>

    </form>
</div>

<div id="changeEmailDOM" class="pad-m popLayer" style="display: none;">
    <form name="changeEmailForm">
        <dl>
            <dt class="requireOpt">请输入邮箱：</dt>
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
<#--<@global.javascript pageJavascript="${js.personalInfo}">-->
<#--</@global.javascript>-->
<script src="${requestContext.getContextPath()}/js/dest/${js.config}"></script>
<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js"
defer
async="true"
data-main="${requestContext.getContextPath()}/js/personal_info.js"></script>
</body>
</html>