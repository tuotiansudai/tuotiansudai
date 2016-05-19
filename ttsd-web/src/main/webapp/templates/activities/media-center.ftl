<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.media_center}" pageJavascript="${js.media_center}" activeNav="媒体中心" activeLeftNav="媒体中心" title="媒体中心" >

<div class="media-container">
    <div class="media-center">媒体中心</div>
    <div class="banner-box">
        <div class="media-img-list">
            <a href="#" target="_blank">
                <img src="${staticServer}/images/app-banner/app-banner-landingpage.png" alt="" class="iphone-img">
            </a>
            <a href="#"  target="_blank">
                <img src="${staticServer}/images/app-banner/app-banner-top.jpg" alt="霸道总裁第二期即将到来，送钱！送车！还送啥？" class="iphone-img">
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
        <div><span class="media-title">拓天速贷第二期全国排行活动正式启动</span> <span class="media-date">2016-05-21</span> </div>
        <div class="media-article-img"><img src="${staticServer}/images/app-banner/app-banner-ipo.jpg" alt="拓天速贷第二期全国排行活动正式启动" ></div>
        <div>
            <span class="media-read">阅读:800</span>
            <span class="media-praise">点赞:106</span>
        </div>
    </div>



</div>
</@global.main>