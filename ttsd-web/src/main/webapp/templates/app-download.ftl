<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>下载拓天速贷</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <link type="text/css" rel="stylesheet" href="${css.app_download}">

</head>
<body>
<div class="wechat-android " id="wechatAndroid" style="display: none">
</div>

<div class="normal-frame-out" id="normalFrame" style="display: none">
    <div class="column-box download-header clearfix">
        <span class="app-logo"></span>
        <a href="javascript:void(0)" class="btn-download" id="btnDownload">立即下载</a>
    </div>

    <div class="column-box bond-box clearfix">
        <img class="img-column">
        <div class="top-decorate"></div>
        <div class="column-text">
            <b>债权上线·早知道</b>
            <p>最新热门标的实时提醒<br/>
                信息快人一步，收益抢先到手</p>
            <i class="icon-clock">开标提醒</i>
        </div>
        <div class="clearfix"></div>
    </div>

    <div class="column-box fund-box">
        <img class="img-column">
        <div class="column-text">
            <b>资金流转·不站岗</b>
            <p>精心打磨回款日历<br/>
                每一笔投资尽在掌握，收益数据更直观</p>
            <i class="icon-calendar">回款日历</i>
        </div>
    </div>

    <div class="column-box hand-invest">
        <img class="img-column">
        <div class="column-text">
            <b>随时随地·掌上投</b>
            <p>碎片时间做投资<br/>
                随时随地能玩赚，闲钱不停滞</p>
            <i class="icon-air">便捷投资</i>
        </div>
    </div>

    <div class="column-box safety-account-box clearfix">
        <img class="img-column">
        <div class="column-text">
            <b>多重保障·更安全</b>
            <p>数据采用256位加密技术<br/>
                多重密码保护，快捷支付也安全</p>
            <i class="icon-lock">账户安全</i>
            <i class="icon-account">数据与信息账户</i>
        </div>
    </div>

    <div class="column-box point-box clearfix">
        <img class="img-column">
        <div class="column-text">
            <b>一键签到·攒积分</b>
            <p>不再错过每次签到<br/>
                每日积分翻倍涨，海量商品随心兑</p>
            <i class="icon-point">积分商城</i>
            <i class="icon-hand">一键签到</i>
        </div>
    </div>
</div>
<script>
    window.commonStaticServer='${commonStaticServer}';
</script>

<#if (js.jquerydll)??>
<script src="${js.jquerydll}" type="text/javascript" ></script>
</#if>
<#if (js.globalFun_page)??>
<script src="${js.globalFun_page!}" type="text/javascript" ></script>
</#if>
<#if (js.app_download)??>
<script src="${js.app_download}" type="text/javascript"></script>
</#if>

</body>
</html>
