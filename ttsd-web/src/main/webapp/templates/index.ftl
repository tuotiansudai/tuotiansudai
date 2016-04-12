<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.full_screen}" pageJavascript="${js.index}" activeNav="首页" activeLeftNav="" title="拓天速贷-互联网金融信息服务平台" keywords="拓天速贷,互联网金融平台,P2P理财,拓天借贷,网络理财" description="拓天速贷是基于互联网的金融信息服务平台,由拓天伟业(北京)资产管理有限公司旗下的拓天伟业(北京)金融信息服务有限公司运营.">
<div class="home-page-container">
    <div class="banner-box">
        <div class="banner-img-list">
            <a href="/activity/rank-list" target="_blank">
                <img src="${staticServer}/images/sign/actor/ranklist/rank-list.jpg" alt="霸道总裁第二期即将到来，送钱！送车！还送啥？" class="pc-img">
                <img src="${staticServer}/images/app-banner/app-banner-top.jpg" alt="霸道总裁第二期即将到来，送钱！送车！还送啥？" class="iphone-img">
            </a>
            <a href="/activity/birth-month" target="_blank">
                <img src="${staticServer}/images/sign/actor/birth/birth-month.jpg" alt="生日月投资收益翻倍" class="pc-img">
                <img src="${staticServer}/images/sign/actor/birth/birth-month-phone.jpg" alt="生日月投资收益翻倍" class="iphone-img">
            </a>
            <a href="/activity/red-envelope" target="_blank">
                <img src="${staticServer}/images/sign/actor/redbag/red-bag-pc.png" alt="注册就送现金红包" class="pc-img">
                <img src="${staticServer}/images/sign/actor/redbag/red-bag-phone.png" alt="注册就送现金红包" class="iphone-img">
            </a>
            <a href="http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1" target="_blank">
                <img src="${staticServer}/images/ttimg/ttimg-home-list.jpg" alt="拓天上市" class="pc-img">
                <img src="${staticServer}/images/ttimg/ph-a04.jpg" alt="拓天上市" class="iphone-img">
            </a>
            <a href="/activity/recruit" target="_blank">
                <img src="${staticServer}/images/ttimg/ttimg-home03.png" alt="招募代理" class="pc-img">
                <img src="${staticServer}/images/ttimg/ph-a03.jpg" alt="招募代理" class="iphone-img">
            </a>
        </div>
        <ul class="scroll-num">
            <li class="selected"></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
        </ul>

        <div class="page-width">
            <@global.isAnonymous>
                <div class="register-ad-box fr tc">
                    <p class="num-text clearfix">
                        <span class="percent">46</span>
                        <span class="num-unit">倍</span>
                    </p>
                    <b class="h-title">活期存款收益</b>
                    <p class="welcome-text"></p>
                    <a class="btn-normal" href="/register/user">免费注册 </a>
                    <i class="clearfix tr">已有账户？<a href="/login"> 立即登录</a></i>
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
                        <li>
                            <a href="/announce/${announce.id?string.computer}">
                                <span class="text-title">${announce.title}</span>
                                <span class="text-date">${announce.createdTime?string("yyyy-MM-dd")}</span>
                            </a>
                        </li>
                    </#list>
                </ul>
                <a href="/about/notice" class="text-href">查看更多</a>
            </div>
        </div>
    </div>
    <div class="main-advantage page-width">
        <dl>
            <dd>
                <a href="/about/assurance?aid=1" target="_blank">
                    <div class="icon-hover img-icon-off-1"></div>
                    <span class="clearfix">
                         <b class="clearfix">超高收益 最低门槛</b>
                        最高46倍活期存款收益，最低投资门槛50元
                    </span>
                </a>
            </dd>
            <dd>
                <a href="/about/assurance?aid=2" target="_blank">
                    <div class="icon-hover img-icon-off-2"></div>
                    <span class="clearfix">
                        <b class="clearfix">三方托管 放心理财</b>
                        第三方资金托管，第三方支付
                    </span>
                </a>
            </dd>
            <dd>
                <a href="/about/assurance?aid=3" target="_blank">
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
        <div class="page-width clearfix media-hide">
            <h3 class="label-title">
                <span class="product-icon"></span>
                <span class="notice-icon"></span>
            </h3>
            <div class="product-list">
                <ul>
                    <#list productTypes as productType>
                        <#if productType.name() = 'SYL'>
                            <#assign description = '快速高效'>
                            <#assign description_detail = '快速投资，高效理财'>
                        <#elseif productType.name() = 'WYX'>
                            <#assign description = '稳健灵活'>
                            <#assign description_detail = '稳健收益，灵活便捷'>
                        <#elseif productType.name() = 'JYF'>
                            <#assign description = '财富法宝'>
                            <#assign description_detail = '财富积累，普惠法宝'>
                        </#if>
                        <li class="${productType.name()?lower_case}-text">
                            <a href="/loan-list?productType=${productType.name()}">
                                <div class="icon-proimg">
                                    <div class="icon-off img-${productType.name()?lower_case}-icon"></div>
                                </div>
                                <div class="product-intro">
                                    <span class="text-intro">${description_detail}</span>
                                </div>
                                <div class="product-month">
                                    <p class="income-num-text">
                                        <span class="income-num">${productType.getPeriods()}</span>
                                        <span class="income-month">个月</span>
                                    </p>
                                    <p class="income-text-intro">
                                        项目期限
                                    </p>
                                </div>
                                <div class="product-income">
                                    <p class="income-num-text">
                                        <span class="income-num">${productType.getRate()?string('0')}</span>
                                        <span>%</span>
                                        <span class="income-month">起</span>
                                    </p>
                                    <p class="income-text-intro">
                                        年化收益
                                    </p>
                                </div>
                            </a>
                        </li>
                    </#list>
                </ul>
            </div>
            <div class="company-up">
                <a href="http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1" target="_blank">
                    <div class="img-video"></div>
                </a>
            </div>
        </div>
        <div class="page-width clearfix">
            <h3 class="label-title media-hide">
                <span class="hot-product"></span>
                <a href="/loan-list" class="hot-more">更多>></a>
            </h3>
            <div class="product-box-list fl">
                <div class="product-box-inner">
                    <#list loans as loan>
                        <div class="product-box tc product-type">
                            <i class="img-${loan.productType?lower_case}"></i>
                            <div class="pad-m" title="${loan.name}" data-url="/loan/${loan.id?string.computer}">
                                <h2 class="pr-title">${loan.name}</h2>
                                <div class="pr-square tc">
                                    <div class="pr-square-in">
                                        <em><b><@percentInteger>${loan.baseRate}</@percentInteger></b><@percentFraction>${loan.baseRate}</@percentFraction>
                                            <#if (loan.activityRate > 0) >+<@percentInteger>${loan.activityRate}</@percentInteger>
                                                <@percentFraction>${loan.activityRate}</@percentFraction></#if>%</em>
                                        <i>年化收益</i>
                                    </div>
                                </div>
                                <dl class="pr-info">
                                    <dd class="dl-month"><i>${loan.periods}</i>${loan.isPeriodMonthUnit?string("个月", "天")} <span>项目期限</span></dd>
                                    <dd class="dl-amount"><i><@amount>${loan.amount}</@amount>元</i><span>项目金额</span></dd>
                                </dl>
                                <div class="project-schedule clear-blank clearfix">
                                    <div class="p-title">
                                        <span class="fl">项目进度</span>
                                        <span class="point fr">${loan.progress?string("0.00")}%</span>
                                    </div>
                                    <div class="process-percent">
                                        <div class="percent" style="width:${loan.progress}%"></div>
                                    </div>
                                </div>
                            </div>
                            <#if loan.status=="RAISING">
                                <a href="/loan/${loan.id?string.computer}" class="btn-normal">立即投资</a>
                            <#elseif loan.status=="PREHEAT">
                                <a href="/loan/${loan.id?string.computer}" class="btn-normal wait-invest">预热中</a>
                            <#else>
                                <button type="button" disabled class="btn-normal">已售罄</button>
                            </#if>
                        </div>
                    </#list>
                </div>
            </div>
        </div>

        <div class="page-width clearfix media-coverage-box margin-top25 media-hide">
            <h3 class="label-title">
                <span class="media-coverage-icon"></span>
                <a href="/about/media" class="hot-more">更多>></a>
            </h3>

            <div class="box-radius clearfix">
                <ul class="media-list fl">
                    <li><i>●</i><a href="http://fj.qq.com/a/20160314/060811.htm" target="_blank">拓天速贷：财富盛宴大平台 感恩豪礼滚滚来</a>
                        <time>2016-03-14</time>
                    </li>
                    <li><i>●</i><a href="http://help.3g.163.com/0414/16/0201/14/BEOCI8PP041403U2.html" target="_blank">拓天速贷：新年贺岁嗨翻天，全民领取888元现金红包</a>
                        <time>2016-02-01</time>
                    </li>
                    <li><i>●</i><a href="http://money.china.com/fin/lc/201601/20/2443757.html?qq-pf-to=pcqq.c2c" target="_blank">拓天速贷：宝马名花有主，猴年豪礼来袭</a>
                        <time>2016-01-20</time>
                    </li>
                    <li><i>●</i><a href="http://toutiao.com/news/6233268186905575938/" target="_blank">拓天速贷以卓越风控打造高效资产平台</a>
                        <time>2015-12-28</time>
                    </li>
                    <li><i>●</i><a href="http://house.qq.com/a/20151221/035341.htm" target="_blank">拓天速贷官网全新升级 迈入2.0智能金融信息服务时代</a>
                        <time>2015-12-21</time>
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
                    <li><a href="http://www.king-capital.com/" target="_blank"> <i class="img-jingdu"></i></a></li>
                    <li><a href="http://www.umpay.com/umpay_cms/" target="_blank"><i class="img-ump"></i></a></li>
                </ul>
            </div>
        </div>
    </div>

    <#include "coupon-alert.ftl" />
</div>

    <#include "red-envelope-float.ftl" />
</@global.main>