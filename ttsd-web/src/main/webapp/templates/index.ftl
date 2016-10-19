<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.home_page_v2}" pageJavascript="${js.index}" activeNav="首页" activeLeftNav="" title="拓天速贷-互联网金融信息服务平台" keywords="拓天速贷,互联网金融平台,P2P理财,拓天借贷,网络理财" description="拓天速贷是基于互联网的金融信息服务平台,由拓天伟业(北京)资产管理有限公司旗下的拓天伟业(北京)金融信息服务有限公司运营.">

<div class="home-page-container" id="homePageContainer">
    <div class="banner-box" >
        <div id="bannerBox" class="banner-box-inner">
            <ul class="banner-img-list">
                <#list bannerList as banner>
                    <li>
                        <a href="${banner.url}" data-name="${banner.url}"
                           onclick="cnzzPush.trackClick('首页','Banner模块','${banner.name!}')" target="_blank"
                           <#if banner.url == 'http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1'>rel="nofollow"</#if>>
                            <img src="http://ci1.tuotiansudai.com:6002//${banner.webImageUrl}" data-app-img="${staticServer}${banner.appImageUrl}" alt="${banner.title}" >
                        </a>
                    </li>
                </#list>
            </ul>
        </div>
        <div class="page-width">
            <@global.isAnonymous>
                <div class="register-ad-box fr tc">
                    <p class="num-text clearfix">
                        <span class="percent">46</span>
                        <span class="num-unit">倍</span>
                    </p>
                    <b class="h-title">活期存款收益</b>

                    <p class="welcome-text"></p>
                    <a class="btn-normal" href="/register/user" onclick="cnzzPush.trackClick('21首页','Banner模块','免费注册')">免费注册 </a>
                    <i class="clearfix tr">已有账户？<a href="/login"
                                                   onclick="cnzzPush.trackClick('22首页','Banner模块','立即登录')"> 立即登录</a></i>
                </div>
            </@global.isAnonymous>
        </div>
    </div>
</div>

</@global.main>