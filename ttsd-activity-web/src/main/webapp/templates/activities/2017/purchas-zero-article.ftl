<#import "../../macro/global.ftl" as global>

<@global.main pageCss="${css.purchas_zero_2017}" pageJavascript="${js.purchas_zero_article_2017}" activeNav="" activeLeftNav="" title="好货0元购_活动中心_拓天速贷" keywords="拓天速贷,0元购,商品奖励,专享项目" description="拓天速贷好货0元购活动,活动期间用户点选心仪好货,通过活动页面'立即投资'按钮进入投资页面,投资带有'0元购'标签的专享项目达到该商品对应投资额,即可0元白拿商品.">

<div id="purchas_zero_detail" class="shopping-zero-frame">
    <i class="date" id="dateTime" data-starttime="${activityStartTime!}" data-endtime="${activityEndTime!}"></i>
   <div class="product-intro-wrap clearfix page-width page680">
            <div class="preview-wrap" id="previewImg">
            </div>
       <div class="itemInfo-wrap">
           <h2 class="item-name" id="itemName"></h2>
           <p class="info-price">
               市场价：<del id="marketPrice"></del> <strong id="nowPrice"></strong> 元
           </p>
           <ul>
               <li>存入期限： <span id="termDay"></span> </li>
               <li>存入本金：<span id="principal"></span></li>
               <li class="media-320">收益说明：<span id="explain"></span></li>
           </ul>

           <#assign versions = ['4.4.1', '4.4.2', '4.3.5', '4.3.4', '4.3.3', '4.4', '4.5']>
           <#--未登录-->
           <@global.isAnonymous>
               <a href="javascript:void(0)" class="invest-btn" id="unLogin">立即白拿</a>
           </@global.isAnonymous>
           <#--已登录-->
           <@global.isNotAnonymous>
           <#if exists>
               <#if !isAppSource>
                   <a href="/activity/zero-shopping/activity-loan-detail?zeroShoppingPrize=${prizeName}" class="invest-btn" id="toInvest">立即白拿</a>
               <#elseif versions?seq_contains(appVersion)?string('true','false') == 'true'>
                   <a href="javascript:void(0)" class="invest-btn" id="versionUpdate">立即白拿</a>
               <#else>
                   <a href="app/tuotian/loan-detail/${loanId}?zeroShoppingPrize=${prizeName}" class="invest-btn" id="toInvest">立即白拿</a>
               </#if>
           <#else>
           <#--标的不存在 弹框-->
               <a href="javascript:void(0)" class="invest-btn" id="loanNoExist">立即白拿</a>
           </#if>
           </@global.isNotAnonymous>
       </div>
   </div>
    <div class="shopping-process page-width">
        <h2></h2>
    </div>
    <div class="product-detail page-width wap-width-detail">
        <h2 class="detail-product">商品介绍</h2>
        <div class="product-imgs" id="productImages">
        </div>
    </div>
    <div id="soldTipDOM" class="sold-tip" style="display: none">
        <div class="close-btn to-close"></div>
        <div class="icon-sold"></div>
        <p>太火爆了！该标的已售罄，<br/>
            新标的正在努力筹备中，请稍候重试...</p>
        <div class="known-btn to-close">我知道啦</div>
    </div>
    <div id="versionUpdateDOM" class="sold-tip" style="display: none">
        <div class="close-btn to-close"></div>
        <#--<div class="icon-sold"></div>-->
        <p>该活动需更新版本后参加！</p>
        <div class="known-btn to-close">我知道啦</div>
    </div>
    <#include "../../module/login-tip.ftl" />
</div>
</@global.main>