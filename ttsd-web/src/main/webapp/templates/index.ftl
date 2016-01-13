<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.full_screen}" pageJavascript="${js.index}" activeNav="首页" activeLeftNav="">
<div class="home-page-container">
    <div class="banner-box">
        <div class="banner-img-list">
            <a href="http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1" target="_blank">
                <img src="${staticServer}/images/ttimg/ttimg-home-list.jpg" alt="拓天上市" class="pc-img">
                <img src="${staticServer}/images/ttimg/ph-a04.jpg" alt="招募代理" class="iphone-img">
            </a>
            <a href="/activity/ranking" target="_blank">
                <img src="${staticServer}/images/sign/actor/ranking/qph.jpg" alt="抢排行，送大礼" class="pc-img">
                <img src="${staticServer}/images/ttimg/ph-a01.jpg" alt="抢排行，送大礼" class="iphone-img">

            </a>
            <a href="/activity/grand" target="_blank">
                <img src="${staticServer}/images/sign/actor/grand/ba-grand.jpg" alt="累计收益兑大奖" class="pc-img">
                <img src="${staticServer}/images/ttimg/ph-a02.jpg" alt="累计收益兑大奖" class="iphone-img">
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
        </ul>

        <div class="page-width">
            <@global.isAnonymous>
                <div class="register-ad-box fr tc">
                    <p class="num-text clearfix">
                        <span class="percent">46</span>
                        <span class="num-unit">倍</span>
                    </p>
                    <b class="h-title">活期存款收益</b>
                    <p class="welcome-text">
                        <img src="${staticServer}/images/sign/welcome-text.png" width="100%" alt="welcome">
                    </p>
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
                    <span class="icon-hover">
                        <img class="icon-off" src="${staticServer}/images/icons/icon-off-1.png" alt="超高收益 最低门槛">
                        <img class="icon-on" src="${staticServer}/images/icons/icon-on-1.png" alt="超高收益 最低门槛">
                    </span>
                    <span class="clearfix">
                         <b class="clearfix">超高收益 最低门槛</b>
                        最高46倍活期存款收益，最低投资门槛50元
                    </span>
                </a>
            </dd>
            <dd>
                <a href="/about/assurance?aid=2" target="_blank">
                    <span class="icon-hover">
                        <img class="icon-off" src="${staticServer}/images/icons/icon-off-2.png" alt="三方托管 放心理财">
                        <img class="icon-on" src="${staticServer}/images/icons/icon-on-2.png" alt="三方托管 放心理财">
                    </span>
                    <span class="clearfix">
                        <b class="clearfix">三方托管 放心理财</b>
                        第三方资金托管，第三方支付
                    </span>
                </a>
            </dd>
            <dd>
                <a href="/about/assurance?aid=3" target="_blank">
                    <span class="icon-hover">
                        <img class="icon-off" src="${staticServer}/images/icons/icon-off-3.png" alt="实力雄厚 安全保障">
                        <img class="icon-on" src="${staticServer}/images/icons/icon-on-3.png" alt="实力雄厚 安全保障">
                    </span>
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
                                    <img class="icon-off" src="${staticServer}/images/icons/${productType.name()?lower_case}-icon.png" alt="${productType.getName()} ${description}">
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
                    <img src="${staticServer}/images/banner/video-images.png" alt="拓天伟业挂牌视频">
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
                                        <span class="point fr">${loan.progress}%</span>
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

        <#--<div class="page-width clearfix">-->
            <#--<h3 class="label-title media-hide">-->
                <#--<span class="report-icon">-->
                    <#--运营报告-->
                <#--</span>-->
            <#--</h3>-->
            <#--<div class="clear-blank invest-total">-->
                <#--<span class="fl">累计投资人数:-->
                    <#--<strong data-count="${userCount}" id="userCount">${userCount}</strong>人-->
                <#--</span>-->
            <#--</div>-->
        <#--</div>-->

        <div class="page-width clearfix media-coverage-box margin-top25 media-hide">
            <h3 class="label-title">
                <span class="media-coverage-icon"></span>
                <a href="/about/media" class="hot-more">更多>></a>
            </h3>

            <div class="box-radius clearfix">
                <ul class="media-list fl">
                    <li><i>●</i><a href="http://house.qq.com/a/20151221/035341.htm" target="_blank">拓天速贷官网全新升级 迈入2.0智能金融信息服务时代</a> <time>2015-12-21</time></li>
                    <li><i>●</i><a href="http://toutiao.com/news/6233268186905575938/" target="_blank">拓天速贷以卓越风控打造高效资产平台</a> <time>2015-12-28</time></li>
                    <li><i>●</i><a href="http://money.cri.cn/303/2015/12/16/124s17791.htm" target="_blank">拓天速贷走进石家庄 双十二财富送到家</a> <time>2015-12-16</time></li>
                    <li><i>●</i><a href="http://money.china.com/fin/lc/201509/29/3549074.html" target="_blank">拓天速贷：高效资产平台的缔造者</a> <time>2015-09-29</time></li>
                    <li><i>●</i><a href="http://news.dichan.sina.com.cn/2015/10/28/1131448.html" target="_blank">拓天速贷推出累积收益?兑大奖活动</a> <time>2015-10-28</time></li>
                </ul>
                <ul class="media-logo-list fr">
                    <li><a href="http://house.qq.com/a/20151221/035341.htm" target="_blank" class="qq"></a> </li>
                    <li><a href="http://yueyu.cntv.cn/2015/10/28/ARTI1446021624023304.shtml" target="_blank" class="yangshi"></a> </li>
                    <li><a href="http://msn.ynet.com/3.1/1512/16/10626316.html" target="_blank" class="msn"></a> </li>
                    <li><a href="http://news.dichan.sina.com.cn/2015/10/28/1131448.html" target="_blank" class="sina"></a> </li>
                    <li><a href="http://news.ifeng.com/a/20151028/46027672_0.shtml" target="_blank" class="ifeng"></a> </li>
                    <li><a href="http://help.3g.163.com/0406/15/0930/16/B4P96I6K0406002E.html" target="_blank" class="wangyi"></a> </li>
                    <li><a href="http://money.china.com/fin/lc/201509/29/3549074.html" target="_blank" class="china"></a> </li>
                    <li><a href="http://toutiao.com/news/6233268186905575938/" target="_blank" class="toutiao"></a> </li>
                  </ul>
            </div>
        </div>

        <div class="page-width clearfix partner-box margin-top25 margin-bottom25 media-hide">
            <h3 class="label-title">
                <span class="partner-icon"></span>
            </h3>

            <div class="box-radius clearfix">
                <ul>
                    <li><a href="http://www.king-capital.com/" target="_blank"> <img src="${staticServer}/images/sign/partner/jingdu.png"></a></li>
                    <li><a href="http://www.umpay.com/umpay_cms/" target="_blank"><img src="${staticServer}/images/sign/partner/liandongyoushi.png"></a> </li>
                </ul>
             </div>
            </div>
    </div>


</div>

</@global.main>