<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>正在处理中...</title>
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>

<div class="my-account-content personal-profile">
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-success"></div>
            <p class="desc">银行卡绑定成功</p>
        </div>
   
    </div>
    <div class="btn-container">
        <a href="/" class="btn-confirm">确定</a>
    </div>
<#--实名认证成功后下一步 去绑卡-->
    <form action="${requestContext.getContextPath()}/bank-card/bind/source/M" method="post" style="display: inline-block;float:right">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <span class="binding-set">
                        <i class="fa fa-times-circle no"></i>未绑定<input type="submit" class="setlink setBankCard" value="绑定" style="border: none;color: #ffac2a;cursor: pointer;font-size: 13px"/>
                    </span>
        <div class="btn-container">
            <input type="submit" class="btn-confirm btn-next"  value="下一步"/>
        </div>
    </form>

    <div class="contact"><p>客服电话：400-169-1188（服务时间：9:00-20:00）</p></div>
</div>


</body>
</html>