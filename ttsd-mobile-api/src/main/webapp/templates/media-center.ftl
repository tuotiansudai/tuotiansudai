<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.media_center}" pageJavascript="${js.media_center}"  title="媒体中心" >
<div class="media-container">
    <div class="media-center">媒体中心</div>
    <div class="banner-box">
        <div class="media-img-list">
            <a href="#" target="_blank">
                <img src="${staticServer}/images/cust_withdrawals.png" alt="" class="iphone-img">
            </a>
            <a href="#"  target="_blank">
                <img src="${staticServer}/images/mer_recharge_person.png" alt="霸道总裁第二期即将到来，送钱！送车！还送啥？" class="iphone-img">
            </a>
        </div>
        <ul class="scroll-num">
            <li class="selected"></li>
            <li></li>
        </ul>
    </div>

    <dl class="media-type-list">
        <dd >最新</dd>
        <dd>行业资讯</dd>
        <dd>平台动态</dd>
        <dd>活动中心</dd>
    </dl>
    <div class="media-article-list">
        <div class="media-top"><span class="media-title">拓天速贷第二期全国排行活动正式启动</span> <span class="media-date">2016-05-21</span> </div>
        <div class="media-article-img"><img src="${staticServer}/images/ptp_mer_bind_agreement.png" alt="拓天速贷第二期全国排行活动正式启动" ></div>
        <div class="media-bottom">
            <span class="media-read">阅读:800</span>
            <span class="media-praise">点赞:106</span>
        </div>
    </div>

</div>
</@global.main>