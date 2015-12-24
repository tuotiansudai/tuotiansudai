<#import "../macro/global.ftl" as global>
	<@global.main pageCss="${css.company_activity}" pageJavascript="${js.about_us}" activeNav="" activeLeftNav="" title="抢排行">
	<div class="activity-ranking-container" id="WhetherApp">
        <div class="actor-img actor-ranking-img"></div>
        <div class="res-no-app">
            <div class="main-width responsive-width">
                <img src="${staticServer}/images/sign/actor/ranking/c-01.png" class="responsive-width">
            </div>
            <div class="button tc click-article">
                <a href="/about/notice" target="_blank"> <img
                        src="${staticServer}/images/sign/actor/ranking/btn-c-01.png" class="responsive-width"></a>
            </div>
            <div class="main-width responsive-width">
                <div class="tr">
                    <img src="${staticServer}/images/sign/actor/ranking/btn-c-02.png" class="title-img">
                </div>
                <img src="${staticServer}/images/sign/actor/ranking/c-02.png" class="responsive-width">
                <img src="${staticServer}/images/sign/actor/ranking/c-03.png" class="responsive-width">
            </div>
            <div class="activity-date tc">
                <img src="${staticServer}/images/sign/actor/ranking/c-04.png" class="responsive-width">
            </div>
        </div>
        <div class="res-comtainer">
            <h3 class="responsive-title">投资抢大奖</h3>
            <div class="responsive-picture">
                <img src="${staticServer}/images/sign/actor/ranking/activity-img1.png">
            </div>
            <h3 class="responsive-title">推荐争红包</h3>
            <div class="responsive-picture">
                <img src="${staticServer}/images/sign/actor/ranking/get-bag.png">
            </div>
            <div class="link-btn">
                <h5>活动时间：2015年10月19日-2016年1月16日</h5>
                <a href="/about/notice?source=app">查看榜单</a>
            </div>

        </div>
</@global.main>
