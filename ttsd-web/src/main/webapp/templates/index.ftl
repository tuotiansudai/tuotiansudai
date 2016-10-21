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

     <div class="notice-container bg-screen" >
            <div class="page-width clearfix">
            <h3>最新公告</h3>
            <div class="notice-text scroll-top">
                <ul id="noticeList">
                    <#list announces as announce>
                        <#if announce.id == 187>
                            <#assign category = "70首页">
                            <#assign label = "计息调整">
                        <#elseif announce.id == 188>
                            <#assign category = "69首页">
                            <#assign label = "排行榜">
                        <#else>
                            <#assign category = "71首页">
                            <#assign label = "更多">
                        </#if>
                        <li>
                            <a href="/announce/${announce.id?string.computer}"
                               onclick="cnzzPush.trackClick('${category}','公告模块','${label}')">
                                <span class="text-title fl">${announce.title}</span>
                                <span class="text-date fr">${announce.createdTime?string("yyyy-MM-dd")}</span>
                            </a>
                        </li>
                    </#list>
                </ul>
                <a href="/about/notice" onclick="cnzzPush.trackClick('71首页','公告模块','更多')" class="text-href">更多></a>
            </div>
        </div>
        </div>
    <div class="main-advantage clearfix bg-screen">
        <div class="page-width">
            <dl>
                <dd class="guide">
                    <a href="/about/guide" onclick="cnzzPush.trackClick('28首页','安全保障模块','1')" target="_blank">
                         <b class="clearfix">稳健收益 较低门槛</b>
                        <span>最高46倍活期存款收益，<br>最低投资门槛50元</span>
                    </a>
                </dd>
                <dd class="risk">
                    <a href="/about/risk-flow" onclick="cnzzPush.trackClick('29首页','安全保障模块','2')" target="_blank">
                        <b class="clearfix">六重风控 审核严谨</b>
                       <span>22道审核手续，<br>项目安全透明无死角</span>
                    </a>
                </dd>
                <dd class="assurance">
                    <a href="/about/assurance" onclick="cnzzPush.trackClick('30首页','安全保障模块','3')" target="_blank">
                        <b class="clearfix">实力雄厚 安全保障</b>
                        <span>上市企业投资，<br>资金数据均安全</span>
                    </a>
                </dd>
            </dl>
        </div>
    </div>
    <div class="page-width">
        <div class="newer-exclusive-box clearfix">
            <div class="main-column-title">
                <i class="icon-title"></i>新手专享
            </div>

            <div class="newer-experience clearfix">
                <i class="tag-icon"></i>
                <div class="con-inner">
                    <b class="newer-title">新手体验标 <span>仅限使用体验金投资</span></b>
                    <ul class="loan-info clearfix">
                        <li><span class="percent-number"> <i>${experienceLoanDto.baseRate}</i>%</span>预期年化收益</li>
                        <li><em>${experienceLoanDto.duration}</em>天<br>项目期限</li>
                    </ul>
                    <a href="/loan/1" class="btn-invest btn-normal">马上投资</a>
                </div>

            </div>
            <#list loans as loan>
                <#if loan.activityType == "NEWBIE">
                    <div class="newer-experience clearfix hack-newbie" data-url="/loan/${loan.id?c}">
                        <i class="tag-icon"></i>
                        <div class="con-inner" >
                            <b class="newer-title">${loan.name}</b>
                            <ul class="loan-info clearfix">
                                <li><span class="percent-number" >
                                    <i><@percentInteger>${loan.baseRate+loan.activityRate}</@percentInteger></i>
                                        <@percentFraction>${loan.baseRate+loan.activityRate}</@percentFraction>
                                    <#if (loan.newbieInterestCouponRate > 0) >
                                    +
                                    <i><@percentInteger>${loan.newbieInterestCouponRate}</@percentInteger></i>
                                   <@percentFraction>${loan.newbieInterestCouponRate}</@percentFraction>
                                    </#if>
                                        %<s class="sign-plus">+</s><i>3</i>%</span>预期年化收益</li>
                                <li><em class="duration-day">${loan.duration}</em>天<br>项目期限</li>
                            </ul>
                    <#if loan.status== 'RAISING'>
                        <a href="javascript:void(0)" class="btn-invest btn-normal">马上投资</a>
                    </#if>
                     <#if loan.status== 'PREHEAT'>
                      <#--预热中-->
                        <a href="javascript:void(0)" class="btn-invest btn-normal">
                         <#if loan.preheatSeconds lte 1800>
                             <i class="time-clock"></i><strong
                                 class="minute_show">00</strong><em>:</em><strong
                                 class="second_show">00</strong>放标
                         <#else>
                         ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                         </#if>
                        </a>
                     </#if>
                            <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>
                                <button class="btn-normal" disabled="">已售罄</button>
                            </#if>
                        </div>
                    </div>
                </#if>
            </#list>
            <img src="${staticServer}/images/homepage/hot-bag-a.png" alt="注册送588元红包" class="fr">
        </div>

        <#--优选债权-->
        <div class="main-column-title">
            <i class="icon-title"></i>优选债权
            <a href="/loan-list" onclick="cnzzPush.trackClick('35首页','热门产品模块','更多')" class="hot-more">更多>></a>
        </div>
        <div class="book-invest-box clearfix bg-screen">
            <span class="book-info">预约后当有相应项目我们会及时通知您。</span>
            <a class="btn-invest fr btn-normal <@global.isAnonymous>not-anonymous</@global.isAnonymous> <@global.role hasRole="'INVESTOR'">is-user</@global.role>">
                我要预约
            </a>
        </div>

        <div class="target-category-box clearfix bg-screen">
            <div class="target-title">
                <span>项目名称</span>
                <span>预期年化收益</span>
                <span>项目期限</span>
                <span>项目进度</span>
            </div>
            <div class="target-column-con">
                <span>车辆抵押债权</span>
                <span>11%＋1%</span>
                <span>90天</span>
                <span></span>
            </div>
        </div>

    </div>

</div>

</@global.main>