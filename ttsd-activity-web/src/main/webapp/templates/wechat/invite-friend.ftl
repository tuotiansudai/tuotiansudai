<#--<#import "wechat-global.ftl" as global>-->
<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'midsummer_wap' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>


<@global.main pageCss="${css.midsummer_wap}" pageJavascript="${js.midsummer_wap}"  title="助力好友抢红包" >

<div class="invite-header" id="inviteHeader">
<#--如果是自己打开页面-->
    <span class="invite-text">
            <i>185****4386</i>
        恭喜您获得了<b>50</b>元红包
        </span>
<#--分享此页给好友打开-->
<#--<span class="invite-text">-->
<#--<i>您得好友185****4386</i>-->
<#--获得了<b>50</b>元红包-->
<#--</span>-->
</div>

<div class="invite-friend-container" id="inviteBoxFriend">

<input type="hidden" value="14" class="invite-number">
    <div class="temperature-box">
        <div class="temp-out-square">
            <div class="temp-inner-square">
                <span class="temp-number">60</span>
                <sub>C</sub>
            </div>
        </div>


        <div class="temp-progress-box">
               <div class="temp-progress"></div>
                <div class="temp-scale-line"></div>
        </div>
    </div>

    <div class="invest-detail-box">
        <p>红包当前起投金额：<i class="from-invest"></i>元</p>
    <p>点击按钮邀请好友助力升温，好友注册并累计投资200元即可降低红包起投金额，最低降至50元起投！</p>

        <table class="table-invest">
            <tr>
                <th width="25%">邀请人数</th>
                <th width="15%">0</th>
                <th width="15%">1</th>
                <th width="15%">2</th>
                <th width="15%">3</th>
                <th width="15%">4</th>
            </tr>
            <tr>
                <td>起投金额</td>
                <td>20000</td>
                <td>10000</td>
                <td>5000</td>
                <td>2000</td>
                <td>50</td>
            </tr>
        </table>
    </div>

    <div class="button-layer-bottom" id="buttonLayer">

    <#--如果是自己打开页面-->
        <a href="#" class="btn-normal btn-invite fl">邀请微信好友</a>
        <a href="#" class="btn-normal btn-share fr">分享至朋友圈</a>

    <#--分享此页给好友打开-->
        <#--<a href="#" class="btn-normal btn-help">帮助TA</a>-->
    </div>

</div>
</@global.main>