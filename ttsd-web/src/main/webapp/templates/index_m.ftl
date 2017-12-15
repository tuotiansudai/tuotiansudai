<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.index}" pageJavascript="${m_js.index}" activeNav="首页" activeLeftNav="" title="拓天速贷-互联网金融信息服务平台" keywords="拓天速贷,互联网金融平台,P2P理财,拓天借贷,网络理财" description="拓天速贷是基于互联网的金融信息服务平台,由拓天伟业(北京)资产管理有限公司旗下的拓天伟业(北京)金融信息服务有限公司运营.">

<div class="home-page-container" id="homePageContainer">
    <div id="bannerBox" class="banner-box-inner">
        <ul class="banner-img-list">
            <#list bannerList as banner>
            <li style="opacity: 0;" class="">
                <a href="${banner.url}" target="_blank" <#if banner.url == 'http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1'>rel="nofollow"</#if>>
                    <img src="${commonStaticServer}${banner.webImageUrl}" data-app-img="${commonStaticServer}${banner.appImageUrl}" alt="${banner.title}">
                </a>

            </li>
        </#list>

        </ul>
    </div>

    <div class="gift-box">
        <div class="gift-img"></div>
        <div class="gift-to-detail">
            <span>快来邀好友领取投资大礼包</span>
            <a href="#" class="btn-look">查看详情</a>
        </div>
    </div>

    <#--拓天体验金项目-->
    <div class="main-column-title">
       <span>新手专享</span>
    </div>

    <div class="target-category-box newer-experience" data-url="/loan/1">
        <b class="newer-title"><span>${experienceLoan.name} </span><i class="icon-sign">体验金投资</i></b>
            <ul class="loan-info clearfix">
                <li>
                    <span class="percent-number"><i>${experienceLoan.baseRate}</i>%</span>
                    <em class="note">预期年化收益</em>
                </li>
                <li><em class="duration-day">${experienceLoan.duration}</em> 天 <em class="note">项目期限</em></li>
                <li><a href="/loan/1" class="btn-invest btn-normal">立即投资</a></li>
            </ul>
    </div>

    <#--优选债权-->
    <div class="main-column-title">
        <span>优选债权</span>
        <a href="/m/loan-list"  class="hot-more">更多</a>
    </div>

    <#list normalLoans as loan>
        <div class="target-category-box" data-url="/loan/${loan.id?c}">
            <b class="newer-title">${loan.name}</b>
            <ul class="loan-info clearfix">
                <li>
                    <span class="percent-number">
                    <#if loan.extraRate != 0>
                        <i>${loan.baseRate + loan.activityRate}</i>% ~ <i>${loan.baseRate + loan.activityRate + loan.extraRate * 100}</i>%
                    <#else>
                        <i><@percentInteger>${loan.baseRate}</@percentInteger></i>%
                        <#if loan.activityRate != 0>+<i>${loan.activityRate!}</i>%
                        </#if>
                    </#if>
                    </span>
                    <em class="note">预期年化收益</em>
                </li>
                <li>最长<em class="duration-day">${loan.duration}</em> 天 <em class="note">项目期限</em></li>
                <li>
                    <#if loan.status== 'RAISING'>
                        <a href="javascript:void(0)" class="btn-invest btn-normal">立即投资</a>

                    <#elseif loan.status == 'PREHEAT'>
                        <a href="javascript:void(0)" class="btn-invest btn-normal preheat-btn">
                            <#if loan.preheatSeconds lte 1800>

                                <span class="preheat" data-time="${loan.preheatSeconds?string.computer}">
                                        <i class="minute_show"></i>分
                                        <i class="second_show"></i>秒后开标
                                    </span>
                            <#else>
                            ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                            </#if>
                        </a>
                    <#else>
                        <i class="loan-status icon-sellout"></i>
                    </#if>
                </li>
            </ul>
            <div class="table-row progress-column">
                <div class="progress-bar">
                    <div class="process-percent">
                        <div class="percent" style="width:${loan.progress}%">
                        </div>
                    </div>
                </div>
                <span class="p-title">剩余金额：<i>${loan.availableInvestAmount}元</i></span>
            </div>
        </div>
    </#list>

    <div class="main-column-title">
        <span>转让项目</span>
        <a href="/m/transfer-list" class="hot-more">更多</a>
    </div>

    <#list transferApplications as loan>
        <div class="target-category-box sold-out-box" data-url="/m/transfer/${loan.transferApplicationId}">
            <b class="newer-title">${loan.name}</b>
            <ul class="loan-info  clearfix">
                <li>
                    <span class="percent-number">
                        <i><@percentInteger>${loan.baseRate}</@percentInteger><@percentFraction>${loan.baseRate}</@percentFraction></i>%
                    </span>
                    <em class="note">预期年化收益</em>
                </li>
                <li>剩余天数<em class="duration-day">${loan.leftDays}</em> 天 <em class="note">项目期限</em></li>
                <li>
                    <#if loan.transferStatus=='TRANSFERRING'>
                        <a href="/transfer/${loan.transferApplicationId}" class="btn-invest btn-normal">立即购买</a>
                    <#else>
                        <i class="loan-status icon-sellout"></i>
                    </#if>
                </li>
            </ul>
            <div class="transfer-price">转让价格：<@percentInteger>${loan.transferAmount}</@percentInteger>
                <@percentFraction>${loan.transferAmount}</@percentFraction>元/<@percentInteger>${loan.investAmount}</@percentInteger>
                <@percentFraction>${loan.investAmount}</@percentFraction>元(原)
            </div>
        </div>
    </#list>

    <div class="company-info">
        客服电话：400-169-1188（服务时间：9:00-20:00）<br/>
        拓天伟业（北京）金融信息服务有限公司    版权所有
    </div>
</div>

</@global.main>