<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.full_screen}" pageJavascript="${js.index}" activeNav="首页" activeLeftNav="" title="拓天速贷-互联网金融信息服务平台" keywords="拓天速贷,互联网金融平台,P2P理财,拓天借贷,网络理财" description="拓天速贷是基于互联网的金融信息服务平台,由拓天伟业(北京)资产管理有限公司旗下的拓天伟业(北京)金融信息服务有限公司运营.">
<div class="home-page-container">
    <div class="banner-box" id="bannerBox">
        <div class="banner-img-list bd">
            <ul>
                <#list bannerList as banner>
                    <li>
                        <a href="${banner.url}" data-name="${banner.url}" onclick="cnzzPush.trackClick('首页','Banner模块','${banner.name!}')" target="_blank"
                           <#if banner.url == 'http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1'>rel="nofollow"</#if>>
                            <img src="${banner.webImageUrl}" alt="${banner.title}" class="pc-img">
                        </a>
                        <a href="${banner.url}" data-name="${banner.url}" onclick="cnzzPush.trackClick('首页','Banner模块','${banner.name!}')" target="_self"
                           <#if banner.url == 'http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1'>rel="nofollow"</#if>>
                            <img src="${banner.appImageUrl}" alt="${banner.title}" class="iphone-img">
                        </a>

                    </li>
                </#list>

            </ul>
        </div>
        <div class="hd">
            <ul class="scroll-num">
                <#list bannerList as banner>
                    <li <#if banner_index  == 0>class="on"</#if>>
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
                    <i class="clearfix tr">已有账户？<a href="/login" onclick="cnzzPush.trackClick('22首页','Banner模块','立即登录')"> 立即登录</a></i>
                </div>
            </@global.isAnonymous>
        </div>
    </div>
    <div class="notice-list media-hide">
        <div class="notice-container">
            <h3>最新公告</h3>

            <div class="notice-text scroll-top">
                <ul>
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
                                <span class="text-title">${announce.title}</span>
                                <span class="text-date">${announce.createdTime?string("yyyy-MM-dd")}</span>
                            </a>
                        </li>
                    </#list>
                </ul>
                <a href="/about/notice" onclick="cnzzPush.trackClick('71首页','公告模块','更多')" class="text-href">查看更多</a>
            </div>
        </div>
    </div>

    <div class="main-advantage page-width">
        <dl>
            <dd>
                <a href="/about/assurance?aid=1" onclick="cnzzPush.trackClick('28首页','安全保障模块','1')" target="_blank">
                    <div class="icon-hover img-icon-off-1"></div>
                    <span class="clearfix">
                         <b class="clearfix">稳健收益 较低门槛</b>
                        最高46倍活期存款收益，最低投资门槛50元
                    </span>
                </a>
            </dd>
            <dd>
                <a href="/about/assurance?aid=2" onclick="cnzzPush.trackClick('29首页','安全保障模块','2')" target="_blank">
                    <div class="icon-hover img-icon-off-2"></div>
                    <span class="clearfix">
                        <b class="clearfix">三方托管 放心投资</b>
                        第三方资金托管，第三方支付
                    </span>
                </a>
            </dd>
            <dd>
                <a href="/about/assurance?aid=3" onclick="cnzzPush.trackClick('30首页','安全保障模块','3')" target="_blank">
                    <div class="icon-hover img-icon-off-3"></div>
                    <span class="clearfix">
                        <b class="clearfix">实力雄厚 安全保障</b>
                        上市企业投资，资金数据均安全
                    </span>
                </a>
            </dd>
        </dl>
    </div>

    <div class="home-content" id="productFrame">
        <div class="page-width clearfix new-model">
            <div class="new-user-free" data-url="/loan/1">
                <div class="left-intro">
                </div>
                <div class="center-detail">
                    <div class="loan-top">
                        <span class="l-title fl">新手体验项目<i class="new-tip">仅限使用体验金投资</i></span>
                    </div>
                    <div class="loan-info-dl">
                        <dl>
                            <dt>预期年化收益</dt>
                            <dd><em class="active">${experienceLoanDto.baseRate}</em>
                                <i>%</i>
                            </dd>
                        </dl>
                        <dl>
                            <dt>项目期限</dt>
                            <dd><em>${experienceLoanDto.duration}</em>天</dd>
                        </dl>
                    </div>
                </div>
                <div class="right-operat">
                    <div class="p-title">
                        <span class="fl">项目进度</span>
                        <span class="point fr">${experienceLoanDto.progress} %</span>
                    </div>
                    <div class="process-percent">
                        <div class="percent" style="width:${experienceLoanDto.progress}%">
                        </div>
                    </div>
                    <div class="rest-amount">
                        <span>可投额度：<i>${experienceLoanDto.investAmount}</i>元(体验金)</span>
                        <a href="/loan/1" class="btn-invest btn-normal">马上投资</a>
                    </div>
                </div>
                <div class="mask-model">
                    <div class="model-project-precess">
                        <div class="right-operat">
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">${experienceLoanDto.progress} %</span>
                            </div>
                            <div class="process-percent">
                                <div class="percent" style="width:${experienceLoanDto.progress}%">
                                </div>
                            </div>
                            <div class="rest-amount">
                                <span>可投额度：<i>${experienceLoanDto.investAmount}</i>元(体验金)</span>
                                <a href="/loan/1" class="btn-invest btn-normal">马上投资</a>
                            </div>
                        </div>
                    </div>
                    <div class="mask-btn"></div>
                </div>
                <div class="mask-bg"></div>
            </div>
        </div>
    <#--优选债权-->
        <#list loans as loan>
            <#if loan.activityType == "NEWBIE">
                <div class="page-width clearfix media-hide">
                    <h3 class="label-title">
                        <span class="product-icon"></span>
                    </h3>

                    <div class="product-list">
                        <ul class="loan-btn">
                            <li data-url="/loan/${loan.id?c}" class="clearfix">
                                <span class="new-user"></span>

                                <div class="loan-info-frame fl">
                                    <div class="loan-top">
                                        <span class="l-title fl">${loan.name}</span>
                                    </div>
                                    <div class="loan-info-dl">
                                        <dl>
                                            <dt>预期年化收益</dt>
                                            <dd>
                                                <em class="active"><@percentInteger>${loan.baseRate+loan.activityRate}</@percentInteger></em>
                                                <i><@percentFraction>${loan.baseRate+loan.activityRate}</@percentFraction>
                                                    <#if (loan.newbieInterestCouponRate > 0) >
                                                        +<@percentInteger>${loan.newbieInterestCouponRate}</@percentInteger>
                                                        <@percentFraction>${loan.newbieInterestCouponRate}</@percentFraction>
                                                    </#if>%+3%
                                                </i>
                                                <span>新手加息券</span>
                                            </dd>
                                        </dl>
                                        <dl>
                                            <dt>项目期限</dt>
                                            <dd><em>${loan.duration}</em>天

                                            </dd>
                                        </dl>
                                    </div>
                                </div>

                                <#if loan.status== 'RAISING'>
                                    <div class="loan-process project-schedule now-active">
                                        <div class="p-title">
                                            <span class="fl">项目进度</span>
                                            <span class="point fr">${loan.progress?string("0.00")} %</span>
                                        </div>
                                        <div class="process-percent">
                                            <div class="percent" style="width:${loan.progress}%">
                                            </div>
                                        </div>
                                        <div class="rest-amount">
                                            <span>可投额度：<i>${loan.availableInvestAmount} 元</i></span>
                                            <i class="btn-invest btn-normal">马上投资</i>
                                        </div>
                                    </div>
                                </#if>

                                <#if loan.status== 'PREHEAT'>
                                    <div class="loan-process project-schedule now-active">
                                        <div class="time-item preheat"
                                             data-time="${loan.preheatSeconds?string.computer}">
                                            <#if loan.preheatSeconds lte 1800>
                                                <i class="time-clock"></i><strong
                                                    class="minute_show">00</strong><em>:</em><strong
                                                    class="second_show">00</strong>以后可投资
                                            <#else>
                                            ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                                            </#if>
                                        </div>
                                        <div class="rest-amount wait-invest will">
                                            <i class="btn-wait-invest btn-normal">预热中</i>
                                        </div>

                                        <div class="pro">
                                            <div class="p-title">
                                                <span class="fl">项目进度</span>
                                                <span class="point fr">${loan.progress?string("0.00")} %</span>
                                            </div>
                                            <div class="process-percent">
                                                <div class="percent" style="width:${loan.progress}%"></div>
                                            </div>
                                            <div class="rest-amount">
                                                <span>可投额度：<i>${loan.availableInvestAmount}</i>元</span>
                                                <i class="btn-invest btn-normal">马上投资</i>
                                            </div>
                                        </div>
                                    </div>
                                </#if>

                                <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>
                                    <div class="loan-process project-schedule now-active">
                                        <div class="p-title">
                                            <span class="fl">项目进度</span>
                                            <span class="point fr">${loan.progress?string("0.00")} %</span>
                                        </div>
                                        <div class="process-percent">
                                            <div class="percent" style="width:${loan.progress}%"></div>
                                        </div>
                                        <div class="rest-amount finish-invest">
                                            <span class="give-progress">还款进度：${loan.completedPeriods}/${loan.periods}
                                                期</span>
                                            <button class="btn-normal" disabled="">已售罄</button>
                                        </div>
                                    </div>
                                </#if>
                            </li>
                        </ul>
                    </div>
                    <div class="company-up">
                        <a href="/activity/landing-page" target="_blank"></a>
                    </div>
                </div>
            </#if>
        </#list>
        <div class="page-width clearfix margin-top25">
            <h3 class="label-title media-hide">
                <span class="hot-product"></span>
                <a href="/loan-list" onclick="cnzzPush.trackClick('35首页','热门产品模块','更多')" class="hot-more">更多>></a>
            </h3>

            <div class="loan-list-index fl">

                <div class="web-book-box" data-url="/loan/1">
                    <span class="book-info">预约后当有相应项目我们会及时通知您。</span>
                    <a class="btn-invest btn-normal <@global.isAnonymous>not-anonymous</@global.isAnonymous> <@global.role hasRole="'INVESTOR'">is-user</@global.role>">
                        我要预约
                    </a>
                </div>

                <ul class="loan-box-inner loan-btn">
                    <#list loans as loan>
                        <#if loan.activityType != "NEWBIE">
                            <li data-url="/loan/${(loan.id?string.computer)!}" class="clearfix">
                                <div class="loan-info-frame fl">
                                    <div class="loan-top">
                                        <span class="l-title fl">${loan.name}</span>

                                    </div>
                                    <div class="loan-info-dl">
                                        <dl>
                                            <dt>预期年化收益</dt>
                                            <dd>
                                                <em class="active"><@percentInteger>${loan.baseRate}</@percentInteger>
                                                    <@percentFraction>${loan.baseRate}</@percentFraction>
                                                    <#if (loan.extraRate > 0)>
                                                        ~ <@percentInteger>${loan.baseRate + (loan.extraRate * 100)}</@percentInteger><@percentFraction>${loan.extraRate * 100}</@percentFraction>
                                                    </#if>
                                                    <#if (loan.activityRate > 0) >
                                                        +<@percentInteger>${loan.activityRate}</@percentInteger>
                                                        <@percentFraction>${loan.activityRate}</@percentFraction>
                                                    </#if>
                                                </em>
                                                <i>%
                                                </i>
                                                <#if loan.extraSource?? && loan.extraSource == "MOBILE" >
                                                   <i class="fa fa-mobile"></i>
                                                </#if>
                                            </dd>
                                        </dl>

                                        <dl>
                                            <dt>项目期限</dt>
                                            <dd><em>${loan.duration}</em>天

                                            </dd>
                                        </dl>
                                        <dl>
                                            <dt>项目总额</dt>
                                            <dd><em><@amount>${loan.amount}</@amount></em>元</dd>
                                        </dl>
                                    </div>
                                </div>

                                <div class="loan-process project-schedule now-active">
                                    <#if loan.status== 'PREHEAT'>
                                        <div class="time-item preheat"
                                             data-time="${loan.preheatSeconds?string.computer}">
                                            <#if loan.preheatSeconds lte 1800>
                                                <i class="time-clock"></i><strong
                                                    class="minute_show">00</strong><em>:</em><strong
                                                    class="second_show">00</strong>以后可投资
                                            <#else>
                                            ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                                            </#if>
                                        </div>
                                        <div class="rest-amount wait-invest will">
                                            <i class="btn-wait-invest btn-normal">预热中</i>
                                        </div>

                                        <div class="pro">
                                            <div class="p-title">
                                                <span class="fl">项目进度</span>
                                                <span class="point fr">${loan.progress?string("0.00")} %</span>
                                            </div>
                                            <div class="process-percent">
                                                <div class="percent" style="width:${loan.progress}%"></div>
                                            </div>
                                            <div class="rest-amount">
                                                <span>可投额度：<i>${loan.availableInvestAmount}</i>元</span>
                                                <i class="btn-invest btn-normal">马上投资</i>
                                            </div>
                                        </div>
                                    </#if>
                                    <#if loan.status== 'RAISING'>
                                        <div class="p-title">
                                            <span class="fl">项目进度</span>
                                            <span class="point fr">${loan.progress?string("0.00")} %</span>
                                        </div>
                                        <div class="process-percent">
                                            <div class="percent" style="width:${loan.progress}%"></div>
                                        </div>
                                        <div class="rest-amount">
                                            <span>可投额度：<i>${loan.availableInvestAmount}</i>元</span>
                                            <i class="btn-invest btn-normal">马上投资</i>
                                        </div>
                                    </#if>
                                    <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>
                                        <div class="p-title">
                                            <span class="fl">项目进度</span>
                                            <span class="point fr">${loan.progress?string("0.00")} %</span>
                                        </div>
                                        <div class="process-percent">
                                            <div class="percent" style="width:${loan.progress}%"></div>
                                        </div>
                                        <div class="rest-amount finish-invest">
                                            <span class="give-progress">还款进度：${loan.completedPeriods}/${loan.periods}
                                                期</span>
                                            <button class="btn-normal" disabled>已售罄</button>
                                        </div>
                                    </#if>
                                </div>
                            </li>
                        </#if>
                    </#list>
                </ul>
            </div>
            <div class="product-box-list fl">
                <div class="product-box-inner">
                    <div class="free-guide">

                    </div>
                    <div class="product-box tc product-type new-free-pro">
                        <i class="new-free"></i>

                        <div class="pad-m" title="BLQ001" data-url="/loan/1">
                            <h2 class="pr-title">新手体验项目</h2>

                            <div class="pr-square tc">
                                <div class="pr-square-in">
                                    <i>预期年化收益</i>
                                    <em><b>${experienceLoanDto.baseRate}</b>
                                        %</em>
                                </div>
                            </div>
                            <dl class="pr-info">
                                <dd class="dl-month"><i>${experienceLoanDto.duration}</i>天 <span>项目期限</span></dd>
                                <dd class="dl-amount"><i class="new-user-coupon">仅限使用体验金投资</i></dd>
                            </dl>
                            <div class="project-schedule clear-blank clearfix">
                                <div class="pro">
                                    <div class="p-title">
                                        <span class="fl">项目进度</span>
                                        <span class="point fr">${experienceLoanDto.progress} %</span>
                                    </div>
                                    <div class="process-percent">
                                        <div class="percent" style="width:${experienceLoanDto.progress}%"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <a href="/loan/1" class="btn-normal">立即投资</a>

                        <div class="guide-img">
                            <span class="guide-btn"></span>
                        </div>
                    </div>

                    <div class="product-box tc product-type book-text-tip" >
                        <div class="pad-m" title="BLQ001" data-url="/loan/1">
                            <i>预约后当有相应项目我们会及时通知您。</i>
                        </div>
                        <a href="/loan/1"
                           class="btn-normal <@global.isAnonymous>not-anonymous</@global.isAnonymous> <@global.role hasRole="'USER','INVESTOR'">is-user</@global.role>">我要预约</a>
                    </div>

                    <#list loans as loan>
                        <#if loan.activityType == "NEWBIE">
                            <div class="product-box tc product-type">
                                <#if loan.productType??>
                                    <i class="new-user"></i>
                                </#if>
                                <div class="pad-m" title="BLQ001" data-url="/loan/${(loan.id?string.computer)!}">
                                    <h2 class="pr-title">${loan.name}</h2>

                                    <div class="pr-square tc">
                                        <div class="pr-square-in">
                                            <i>预期年化收益</i>
                                            <em><b><@percentInteger>${loan.baseRate+loan.activityRate}</@percentInteger></b>
                                                <@percentFraction>${loan.baseRate}</@percentFraction>
                                                <#if (loan.newbieInterestCouponRate > 0) >
                                                    +<@percentInteger>${loan.newbieInterestCouponRate}</@percentInteger>
                                                    <@percentFraction>${loan.newbieInterestCouponRate}</@percentFraction>
                                                </#if>%</em>
                                        </div>
                                    </div>
                                    <dl class="pr-info">
                                        <dd class="dl-month"><i>${loan.duration}</i>天 <span>项目期限</span></dd>
                                        <dd class="dl-amount"><i class="new-user-coupon">新手加息券</i></dd>
                                    </dl>
                                    <div class="project-schedule clear-blank clearfix">
                                        <#if loan.status== 'PREHEAT'>
                                            <div class="pro">
                                                <div class="p-title">
                                                    <span class="fl">项目进度</span>
                                                    <span class="point fr">${loan.progress?string("0.00")} %</span>
                                                </div>
                                                <div class="process-percent">
                                                    <div class="percent" style="width:${loan.progress}%"></div>
                                                </div>
                                            </div>
                                        </#if>
                                        <#if loan.status== 'RAISING'>
                                            <div class="p-title">
                                                <span class="fl">项目进度</span>
                                                <span class="point fr">${loan.progress?string("0.00")} %</span>
                                            </div>
                                            <div class="process-percent">
                                                <div class="percent" style="width:${loan.progress}%"></div>
                                            </div>
                                        </#if>
                                        <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>
                                            <div class="p-title">
                                                <span class="fl">项目进度</span>
                                                <span class="point fr">${loan.progress?string("0.00")} %</span>
                                            </div>
                                            <div class="process-percent">
                                                <div class="percent" style="width:${loan.progress}%"></div>
                                            </div>
                                        </#if>
                                    </div>

                                </div>
                                <#if loan.status== 'PREHEAT'>
                                    <a href="/loan/${(loan.id?string.computer)!}" class="btn-normal">预热中</a>
                                </#if>
                                <#if loan.status== 'RAISING'>
                                    <a href="/loan/${(loan.id?string.computer)!}" class="btn-normal">立即投资</a>
                                </#if>
                                <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>
                                    <button type="button" disabled="" class="btn-normal">已售罄</button>
                                </#if>
                            </div>
                        </#if>
                        <#if loan.activityType != "NEWBIE">
                            <div class="product-box tc product-type">
                                <#if loan.productType??>
                                    <i class="${loan.productType.name()?lower_case}"></i>
                                </#if>
                                <div class="pad-m" title="BLQ001" data-url="/loan/${(loan.id?string.computer)!}">
                                    <h2 class="pr-title">${loan.name}</h2>

                                    <div class="pr-square tc">
                                        <div class="pr-square-in">
                                            <i>预期年化收益</i>
                                            <em><b><@percentInteger>${loan.baseRate}</@percentInteger></b>
                                                <@percentFraction>${loan.baseRate}</@percentFraction>
                                                <#if (loan.extraRate > 0)>
                                                    ~ <@percentInteger>${loan.baseRate + (loan.extraRate * 100)}</@percentInteger><@percentFraction>${loan.extraRate * 100}</@percentFraction>
                                                </#if>%</em>
                                        </div>
                                    </div>
                                    <dl class="pr-info">
                                        <dd class="dl-month"><i>${loan.duration}</i>天 <span>项目期限</span></dd>
                                        <dd class="dl-amount"><i><@amount>${loan.amount}</@amount>元</i><span>项目总额</span>
                                        </dd>
                                    </dl>
                                    <div class="project-schedule clear-blank clearfix">
                                        <#if loan.status== 'PREHEAT'>
                                            <div class="pro">
                                                <div class="p-title">
                                                    <span class="fl">项目进度</span>
                                                    <span class="point fr">${loan.progress?string("0.00")} %</span>
                                                </div>
                                                <div class="process-percent">
                                                    <div class="percent" style="width:${loan.progress}%"></div>
                                                </div>
                                            </div>
                                        </#if>
                                        <#if loan.status== 'RAISING'>
                                            <div class="p-title">
                                                <span class="fl">项目进度</span>
                                                <span class="point fr">${loan.progress?string("0.00")} %</span>
                                            </div>
                                            <div class="process-percent">
                                                <div class="percent" style="width:${loan.progress}%"></div>
                                            </div>
                                        </#if>
                                        <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>
                                            <div class="p-title">
                                                <span class="fl">项目进度</span>
                                                <span class="point fr">${loan.progress?string("0.00")} %</span>
                                            </div>
                                            <div class="process-percent">
                                                <div class="percent" style="width:${loan.progress}%"></div>
                                            </div>
                                        </#if>
                                    </div>

                                </div>
                                <#if loan.status== 'PREHEAT'>
                                    <a href="/loan/${(loan.id?string.computer)!}" class="btn-normal">预热中</a>
                                </#if>
                                <#if loan.status== 'RAISING'>
                                    <a href="/loan/${(loan.id?string.computer)!}" class="btn-normal">立即投资</a>
                                </#if>
                                <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>
                                    <button type="button" disabled="" class="btn-normal">已售罄</button>
                                </#if>
                            </div>
                        </#if>
                    </#list>
                <#--转让项目-->

                    <div class="project-transfer-mobile">
                        <#list transferApplications as item>
                        <div class="product-box-tran" data-url="/transfer/${item.transferApplicationId}">
                            <div class="loan-top clearfix">
                                <span class="l-title fl">${item.name}</span>
                            </div>
                            <div class="loan-info-dl clearfix">
                                <dl>
                                    <dt>年化收益</dt>
                                    <dd><em><@percentInteger>${item.baseRate}</@percentInteger>
                                        <@percentFraction>${item.baseRate}</@percentFraction></em>
                                        <i>%</i></dd>
                                </dl>
                                <dl>
                                    <dt class="tc">剩余期数</dt>
                                    <dd class="tc"><em>${item.leftPeriod}</em></dd>
                                </dl>
                                <dl>
                                    <dd>
                                        <#if item.transferStatus=='TRANSFERRING'>
                                        <button class="btn-invest btn-normal">马上投资</button>
                                        <#else>
                                            <button class="btn-invest btn-normal" disabled>已转让</button>
                                        </#if>
                                    </dd>
                                </dl>
                            </div>
                            <div class="transer-bottom clearfix">
                                转让价格：<em><@percentInteger>${item.transferAmount}</@percentInteger></em>/ <em><@percentInteger>${item.investAmount}</@percentInteger></em>元
                            </div>
                        </div>
                        </#list>
                    </div>
                </div>
            </div>
        </div>

    <#--转让项目-->
        <div class="page-width project-transfer-frame margin-top25 media-hide">
            <h3 class="label-title">
                <span class="project-transfer-icon"></span>
                <a href="/transfer-list" onclick="cnzzPush.trackClick('47首页','转让项目模块','更多')" class="hot-more">更多>></a>
            </h3>
            <div class="loan-list-index">
                <ul class="loan-box-inner loan-btn">
                    <#list transferApplications as item>
                    <li data-url="/transfer/${item.transferApplicationId}" class="clearfix">
                        <div class="loan-info-frame fl">
                            <div class="loan-top">
                                <span class="l-title fl">${item.name}</span>
                            </div>
                            <div class="loan-info-dl">
                                <dl class="transfer-one">
                                    <dt>转让价格</dt>
                                    <dd>
                                        <em><@percentInteger>${item.transferAmount}</@percentInteger></em>
                                        <i><@percentFraction>${item.transferAmount}</@percentFraction></i> 元
                                    </dd>
                                </dl>

                                <dl class="transfer-one">
                                    <dt>待收本金</dt>
                                    <dd>
                                        <em><@percentInteger>${item.investAmount}</@percentInteger></em>
                                        <i><@percentFraction>${item.investAmount}</@percentFraction></i>
                                        元</dd>
                                </dl>
                                <dl class="transfer-two">
                                    <dt>年化收益</dt>
                                    <dd>
                                        <em><@percentInteger>${item.baseRate}</@percentInteger>
                                        <@percentFraction>${item.baseRate}</@percentFraction></em>
                                        <i>%</i>
                                    </dd>
                                </dl>
                                <dl class="transfer-two">
                                    <dt>剩余期数</dt>
                                    <dd><em>${item.leftPeriod}</em></dd>
                                </dl>
                            </div>
                        </div>

                        <div class="loan-process ">
                            <span class="deadline"> 截止时间：${item.deadLine?string("yyyy-MM-dd")}</span>
                            <div class="rest-amount">
                                <#if item.transferStatus=='TRANSFERRING'>
                                    <button class="btn-invest btn-normal">马上投资</button>
                                <#else>
                                    <button class="btn-invest btn-normal" disabled>已转让</button>
                                </#if>
                            </div>
                        </div>
                    </li>
                    </#list>
                </ul>
            </div>
        </div>

        <div class="page-width clearfix media-coverage-box margin-top25 media-hide">
            <h3 class="label-title">
                <span class="media-coverage-icon"></span>
                <a href="/about/media" onclick="cnzzPush.trackClick('39首页','媒体报道模块','更多')" class="hot-more">更多>></a>
            </h3>

            <div class="box-radius clearfix">
                <ul class="media-list fl">
                    <li><i>●</i><a rel="nofollow" href="http://mt.sohu.com/20160331/n443005759.shtml?qq-pf-to=pcqq.c2c"
                                   onclick="cnzzPush.trackClick('73首页','媒体报道模块','霸道总裁')" target="_blank">拓天速贷第二期全国排行活动正式启动</a>
                        <time>2016-03-31</time>
                    </li>
                    <li><i>●</i><a rel="nofollow" href="http://fj.qq.com/a/20160314/060811.htm"
                                   onclick="cnzzPush.trackClick('40首页','媒体报道模块','财富盛宴大平台')" target="_blank">拓天速贷：财富盛宴大平台
                        感恩豪礼滚滚来</a>
                        <time>2016-03-14</time>
                    </li>
                    <li><i>●</i><a rel="nofollow" href="http://help.3g.163.com/0414/16/0201/14/BEOCI8PP041403U2.html"
                                   onclick="cnzzPush.trackClick('41首页','媒体报道模块','现金红包')" target="_blank">拓天速贷：新年贺岁嗨翻天，全民领取888元现金红包</a>
                        <time>2016-02-01</time>
                    </li>
                    <li><i>●</i><a rel="nofollow"
                                   href="http://money.china.com/fin/lc/201601/20/2443757.html?qq-pf-to=pcqq.c2c"
                                   onclick="cnzzPush.trackClick('42首页','媒体报道模块','宝马名花有主')" target="_blank">拓天速贷：宝马名花有主，猴年豪礼来袭</a>
                        <time>2016-01-20</time>
                    </li>
                    <li><i>●</i><a rel="nofollow" href="http://toutiao.com/news/6233268186905575938/"
                                   onclick="cnzzPush.trackClick('43首页','媒体报道模块','高效资产平台')" target="_blank">拓天速贷以卓越风控打造高效资产平台</a>
                        <time>2015-12-28</time>
                    </li>
                </ul>
                <div class="media-logo-list fr"></div>
            </div>
        </div>

        <div class="page-width clearfix partner-box margin-top25 margin-bottom25 media-hide">
            <h3 class="label-title">
                <span class="partner-icon"></span>
            </h3>

            <div class="box-radius clearfix friend-links">
                <ul>
                    <li><a rel="nofollow" href="http://www.king-capital.com/"
                           onclick="cnzzPush.trackClick('45首页','合作伙伴模块','京都律师所')" target="_blank"> <i
                            class="img-jingdu"></i></a></li>
                    <li><a rel="nofollow" href="http://www.umpay.com/"
                           onclick="cnzzPush.trackClick('46首页','合作伙伴模块','联动优势')" target="_blank"><i class="img-ump"></i></a>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="book-invest-box" style="display: none">
        <form name="bookInvest" class="book-invest-form">
            <div class="clearfix book-table-column">
                <div class="fl">选择项目</div>
                <div class="fr">
                    <table class="book-invest-table">
                        <tr>
                            <th></th>
                            <th>预约项目</th>
                            <th>预期年化收益</th>
                        </tr>
                        <tr>
                            <td class="tc">
                            <span class="init-radio-style">
                                <input type="radio" name="productType" id="po1" value="_90" class="radio-class">
                            </span>
                            </td>
                            <td class="product-type"><label for="po1">90天项目</label>
                            </td>
                            <td><label for="po1">11%</label></td>
                        </tr>
                        <tr>
                            <td>
                            <span class="init-radio-style">
                                <input type="radio" name="productType" id="po2" value="_180" class="radio-class">
                            </span>
                            </td>
                            <td class="product-type"><label for="po2">180天项目</label></td>
                            <td><label for="po2">12%</label></td>
                        </tr>
                        <tr>
                            <td>
                            <span class="init-radio-style">
                                <input type="radio" name="productType" id="po3" value="_360" class="radio-class">
                            </span>
                            </td>
                            <td class="product-type"><label for="po3">360天项目</label></td>
                            <td><label for="po3">13%</label></td>
                        </tr>
                    </table>
                </div>
            </div>
            <dl class="book-dl clearfix">
                <dt>预计投资金额</dt>
                <dd>
                <span>
                    <input type="text" class="form-text autoNumeric" name="bookingAmount" data-l-zero="deny"
                           data-v-min="0.00" data-v-max="99999999999.99" placeholder="0.00" class="autoNumeric"> 元
                </span>
                </dd>
            </dl>
            <div class="tc margin-top25">
                <button type="submit" class="btn btn-normal">确认预约</button>
            </div>
            <dl class="book-dl book-bottom-notice clearfix">
                <dt>预约说明</dt>
                <dd>1、每次预约仅能预约一个项目；<br/>
                    2、预约设置完成时开始进入预约队列；<br/>
                    3、预约后，当有相应标的时，我们会根据队列依次通知进行投资；<br/>
                    4、可同时添加多笔预约，每笔预约排名互相独立，互不影响；<br/>
                    5、每笔预约在通知投资后失效。<br/>

                </dd>
            </dl>
        </form>
    </div>
    <#include "coupon-alert.ftl" />
</div>

    <#include "red-envelope-float.ftl" />
</@global.main>