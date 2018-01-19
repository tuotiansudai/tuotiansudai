<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'bonus_rule' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.bonus_rule}" pageJavascript="${js.bonus_rule}" title="推荐送奖金">

<div class="my-account-content bonus-rule recommend-rule" id="recommendRule">
    <div class="box-column">
        <h2>
            邀请好友投资，拿<em class="key">1%奖励</em>
        </h2>
        <div class="box-in">
            <span class="man-col">
                <i class="man img-friend"></i>
                <i>好友<br/>
购买50000元360天产品</i>
            </span>
            <span class="arrow-col">
                奖励1%
                <i class="icon-arrow"></i>
            </span>
            <span class="man-col">
                  <i class="man img-you"></i>
                <i>您 <br/>
                获得<em class="key">493.15元</em>奖励
                  </i>
            </span>
        </div>
    </div>

    <div class="recommend-note">
        1、好友所投项目放款后，奖励即发放至您的账户；<br/>
        2、推荐奖励=<em class="key"> 好友投资金额×1%×<i class="period">项目期限</i></em>；
    </div>

    <div class="learn-more">
        <a href="#"> 了解更多规则></a>
    </div>
    <div class="rule-award-how">
        <i class="icon-kaola">

        </i>
        <b>如何获得奖励？</b>

        1、分享链接发送给好友或发朋友圈； <br/>
        2、好友通过此链接注册并投资；<br/>
        3、好友投资项目放款后，奖金即发放至您的账户；<br/>
    </div>

    <div class="share-box">
        <h2>立即分享拿奖励</h2>

        <div class="share-box-inner">
            <div class="share-list">
            <span class="share-face">
            <i></i>
当面扫
        </span>

                <span class="share-wechat">
            <i></i>
微信好友
        </span>

                <span class="share-quan">
            <i></i>
微信朋友圈
        </span>

       <span class="share-qq">
            <i></i>
QQ好友
        </span>

            </div>
        </div>

    </div>

    <div class="layer-share" style="display: none">
        <div class="img-pointer"></div>
    </div>

    <div class="wechat-android " id="wechatAndroid" style="display: none">
    </div>
</div>
</@global.main>
