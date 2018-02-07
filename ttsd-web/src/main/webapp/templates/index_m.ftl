<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.index}" pageJavascript="${m_js.index}" activeNav="首页" activeLeftNav="" title="拓天速贷-互联网金融信息服务平台" keywords="拓天速贷,互联网金融平台,P2P理财,拓天借贷,网络理财" description="拓天速贷是基于互联网的金融信息服务平台,由拓天伟业(北京)资产管理有限公司旗下的拓天伟业(北京)金融信息服务有限公司运营.">

<div class="home-page-container" id="homePageContainer">

    <div id="bannerBox" class="banner-box-inner">
        <div class="app-container clearfix">
            <div class="logo"></div>
            <div class="app-detail">
                打开拓天速贷APP<br/>
                超低门槛  稳健收益
            </div>
            <div class="open-app">打开APP</div>
            <div class="close-app"></div>
        </div>
        <ul class="banner-img-list">
            <#list bannerList as banner>
            <li style="opacity: 0;" class="">
                <a href="${banner.url}?source=app" target="_blank" <#if banner.url == 'http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1'>rel="nofollow"</#if>>
                    <img src="${commonStaticServer}${banner.webImageUrl}" data-app-img="${commonStaticServer}${banner.appImageUrl}" alt="${banner.title}">
                </a>

            </li>
        </#list>

        </ul>
    </div>

    <div class="gift-box">
        <dl>
            <dt><a class="operational-data" href="/m/about/operational"></a></dt>
            <dd>运营数据</dd>
        </dl>
        <dl>
            <dt><a class="inviting-friend" href="javascript:;"></a></dt>
            <dd>邀请好友</dd>
        </dl>
        <dl>
            <dt><a class="assurance" href="/m/about/assurance"></a></dt>
            <dd>安全保障</dd>
        </dl>
    </div>

    <#--拓天体验金项目-->
    <div class="main-column-title" data-url="/m/loan-list">
       <span>新手专享</span>
        <a href="/m/loan-list"  class="hot-more">更多</a>
    </div>

    <div class="target-category-box newer-experience" data-url="/m/loan/1">
        <b class="newer-title"><span class="exper-title">${experienceLoan.name} </span><i class="icon-sign exper">体验金投资</i></b>
            <ul class="loan-info clearfix">
                <li>
                    <span class="percent-number"><i>${experienceLoan.baseRate}</i>%</span>
                    <em class="note">预期年化收益</em>
                </li>
                <li><em class="duration-day">${experienceLoan.duration}</em> 天 <em class="note">项目期限</em></li>
                <li><a class="btn-invest btn-normal goToExDetail" data-url="/m/loan/1">立即投资</a></li>
            </ul>
    </div>
    <#--新手专享-->
    <#if newbieLoan??>
    <div class="target-category-box you" data-url="/m/loan/${newbieLoan.id?c}">
        <b class="newer-title"> <span class="exper-title">${newbieLoan.name} </span><i class="icon-sign">新手专享</i></b>
        <ul class="loan-info clearfix">
            <li>
                    <span class="percent-number">
                        <#if newbieLoan.extraRate != 0>
                            <i>${newbieLoan.baseRate + newbieLoan.activityRate}</i>% ~ <i>${newbieLoan.baseRate + newbieLoan.activityRate + newbieLoan.extraRate * 100}</i>%
                        <#else>
                            <i><@percentInteger>${newbieLoan.baseRate + newbieLoan.activityRate}</@percentInteger></i>%
                        </#if>
                    </span>
                <em class="note">预期年化收益</em>
            </li>
            <li>最长<em class="duration-day">${newbieLoan.duration}</em> 天 <em class="note">项目期限</em></li>
            <li>
                <#if newbieLoan.status== 'RAISING'>
                    <a href="javascript:void(0)" class="btn-invest btn-normal goToDetail">立即投资</a>
                <#elseif newbieLoan.status == 'PREHEAT'>
                    <a href="javascript:void(0)" class="btn-invest btn-normal preheat-status preheat-btn" style="opacity: 0.6">预热中</a>
                </#if>
            </li>
        </ul>
        <#if newbieLoan.status != 'PREHEAT'>
            <div class="table-row progress-column">
                <div class="progress-bar">
                    <div class="process-percent ">
                        <div class="percent" style="width:${newbieLoan.progress}%">
                        </div>
                    </div>
                </div>
                <span class="p-title">剩余金额：<i><em class="money"><@amount>${newbieLoan.availableInvestAmountCent?c}</@amount></em>元</i></span>
            </div>
        <#else>
            <div class="table-row progress-column">
                <div class="progress-bar">
                    <#if newbieLoan.preheatSeconds lte 1800>
                        <span class="preheat" data-time="${newbieLoan.preheatSeconds?string.computer}">
                <i class="minute_show"></i>分
                <i class="second_show"></i>秒后开标
                </span>
                    <#else>
                        <span style="color: #FF473C"> ${(newbieLoan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}开标</span>
                    </#if>
                </div>
                <span class="p-title">项目总额：<i><em class="money"><@amount>${newbieLoan.availableInvestAmountCent?c}</@amount></em>元</i></span>
            </div>
        </#if>
    </div>
    </#if>
    <#--新手专享 end-->
    <#--优选债权-->
    <div class="main-column-title" data-url="/m/loan-list">
        <span>优选债权</span>
        <a href="/m/loan-list"  class="hot-more">更多</a>
    </div>
<div>
    <#list normalLoans as loan>
        <div class="target-category-box you" data-url="/m/loan/${loan.id?c}">
            <b class="newer-title">${loan.name}</b>
            <ul class="loan-info clearfix">
                <li>
                    <span class="percent-number <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>colorChange</#if>">
                    <i>
                    <#if loan.extraRate != 0>
                        ${loan.baseRate + loan.activityRate}~${loan.baseRate + loan.activityRate + loan.extraRate * 100}
                    <#else>
                        <@percentInteger>${loan.baseRate + loan.activityRate}</@percentInteger>
                    </#if>

                    </i><em>%</em>

                    </span>
                    <em class="note">预期年化收益</em>
                </li>

                <li>最长<em class="duration-day">${loan.duration}</em> 天 <em class="note">项目期限</em></li>
                <li>
                    <#if loan.status== 'RAISING'>
                        <a href="javascript:void(0)" data-url="/m/loan/${loan.id?c}" class="btn-invest btn-normal goToDetail">立即投资</a>
                    <#elseif loan.status == 'PREHEAT'>
                        <a href="javascript:void(0)" data-url="/m/loan/${loan.id?c}" class="btn-invest btn-normal preheat-status preheat-btn" style="opacity: 0.6">预热中</a>

                    <#else>
                        <i class="loan-status icon-sellout"></i>
                    </#if>
                </li>
            </ul>
        <#if loan.status!= 'PREHEAT'>
            <div class="table-row progress-column">
                <div class="progress-bar">
                    <div class="process-percent ">
                        <div class="percent <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>colorChange</#if>" style="width:${loan.progress}%">
                        </div>
                    </div>
                </div>
                <span class="p-title">剩余金额：<i><em class="money"><@amount>${loan.availableInvestAmountCent?c}</@amount></em>元</i></span>
            </div>
        <#else>
            <div class="table-row progress-column">
                <div class="progress-bar">


            <#if loan.preheatSeconds lte 1800>
                <span class="preheat" data-time="${loan.preheatSeconds?string.computer}">
                <i class="minute_show"></i>分
                <i class="second_show"></i>秒后开标
                </span>
                <#else>
                       <span style="color: #FF473C"> ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}开标</span>

            </#if>
                </div>
                <span class="p-title">项目总额：<i><em class="money"><@amount>${loan.availableInvestAmountCent?c}</@amount></em>元</i></span>
            </div>
        </#if>
        </div>
    </#list>
</div>
    <div class="main-column-title" data-url="/m/transfer-list">
        <span>转让项目</span>
        <a href="/m/transfer-list" class="hot-more">更多</a>
    </div>
<div>
    <#list transferApplications as loan>
        <div class="target-category-box trans" data-url="/m/transfer/${loan.transferApplicationId}">
            <b class="newer-title">${loan.name}</b>
            <ul class="loan-info  clearfix">
                <li>
                    <span class="percent-number <#if (loan.transferStatus == "SUCCESS")>colorChange</#if>">
                        <i><@percentInteger>${loan.baseRate}</@percentInteger><@percentFraction>${loan.baseRate}</@percentFraction></i>%
                    </span>
                    <em class="note">预期年化收益</em>
                </li>
                <li><em class="duration-day">${loan.leftDays}</em> 天 <em class="note">剩余天数</em></li>
                <li>
                    <#if loan.transferStatus=='TRANSFERRING'>
                        <a data-url="/transfer/${loan.transferApplicationId}" class="btn-invest btn-normal goToTranDetail">立即购买</a>
                    <#else>
                        <i class="loan-status icon-transferred"></i>
                    </#if>
                </li>
            </ul>
            <div class="transfer-price">转让价格：<em class="money"><@percentInteger>${loan.transferAmount}</@percentInteger>
                <@percentFraction>${loan.transferAmount}</@percentFraction></em>元/<em class="money"><@percentInteger>${loan.investAmount}</@percentInteger>
                <@percentFraction>${loan.investAmount}</@percentFraction></em>元(原)
            </div>
        </div>
    </#list>
</div>
    <div class="company-info">
        客服电话：400-169-1188（服务时间：9:00-20:00）<br/>
        拓天伟业（北京）金融信息服务有限公司    版权所有
    </div>
</div>

</@global.main>