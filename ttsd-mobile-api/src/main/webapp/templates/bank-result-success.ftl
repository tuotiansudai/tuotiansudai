<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${bankCallbackType.getTitle()}</title>
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="yes" name="apple-touch-fullscreen">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta charset="UTF-8" name="viewport" content="width=device-width,initial-scale=1,target-density dpi=high-dpi,user-scalable=no">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>

<div class="bank-result-content">
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-success"></div>
            <p class="desc">
                <#if isInProgress>
                业务正在处理中，请稍后查询。
                <#else>
                ${bankCallbackType.getTitle()}
                </#if>
            </p>
        </div>
    </div>
    <div class="btn-container">
        <a href="${bankCallbackType.getMobileLink()}/success" class="btn-confirm">确定</a>
    </div>

    <div class="contact"><p>客服电话：400-169-1188（服务时间：9:00-20:00）</p></div>
</div>
</body>
</html>