<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="充值" pageCss="${css.global}">
</@global.head>
<#--<head lang="en">-->
    <#--<meta charset="UTF-8">-->
    <#--<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>-->
    <#--<meta name="_csrf" content="${_csrf.token}"/>-->
    <#--<meta name="_csrf_header" content="${_csrf.headerName}"/>-->
    <#--<title></title>-->
    <#--<link rel="stylesheet" type="text/css" href="${requestContext.getContextPath()}/style/global.css">-->

<#--</head>-->
<body>
<#include "header.ftl" />
<div class="content">
    <aside class="menuBox fl">
        <ul class="menu-list">
            <li><a href="javascript:">账户总览</a></li>
            <li><a href="javascript:">投资记录</a></li>
            <li><a href="javascript:">债权转让</a></li>
            <li><a href="javascript:">资金管理</a></li>
            <li><a href="javascript:">个人资产</a></li>
            <li><a href="javascript:" class="actived">自动投标</a></li>
            <li><a href="javascript:">积分红包</a></li>
            <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="recharge-container fr autoHeight">
        <h4><em class="tc">自动投标</em></h4>

        <div class="recharge-content pad-s">
            <div class="borderBox">
                <ul class="planSet">
                    <li><b>投资金额：</b><i>100</i> --- <i>1000000</i> 元</li>
                    <li><b>保留金额：</b><i>0.00 </i>元</li>
                    <li><b>项目期限：</b><span>1月期、2月期、3月期、4月期、5月期、6月期、7月期、8月期、9月期、10月期、11月期、12月期</span></li>
                </ul>
            </div>


            <div class="btnBox tc">
                <button type="button" class="btn btn-normal">修改设置</button>
                <p class="pad-m text-notice"><i class="icon-notice"></i>自动投标将在次日零点开启</p>
            </div>
        </div>

    </div>
</div>


<#include "footer.ftl">
<@global.javascript pageJavascript="${js.recharge}">
</@global.javascript>
<#--<script src="${requestContext.getContextPath()}/js/dest/${js.config}"></script>-->
<#--<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js"-->
        <#--defer-->
        <#--async="true"-->
        <#--data-main="${requestContext.getContextPath()}/js/recharge.js"></script>-->

</body>
</html>