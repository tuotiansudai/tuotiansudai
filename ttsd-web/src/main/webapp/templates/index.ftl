<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.home_page_v2}" pageJavascript="${js.index}" activeNav="首页" activeLeftNav="" title="拓天速贷-互联网金融信息服务平台" keywords="拓天速贷,互联网金融平台,P2P理财,拓天借贷,网络理财" description="拓天速贷是基于互联网的金融信息服务平台,由拓天伟业(北京)资产管理有限公司旗下的拓天伟业(北京)金融信息服务有限公司运营.">

<div class="home-page-container" id="homePageContainer">
    <div class="banner-box">
        <div id="bannerBox" class="banner-box-inner">
            <ul class="banner-img-list">
                <#list bannerList as banner>
                    <li>
                        <a href="${banner.url}" onclick="cnzzPush.trackClick('首页','Banner模块','${banner.name!}')" target="_blank"
                           <#if banner.url == 'http://www.iqiyi.com/w_19rt7ygfmh.html#vfrm=8-8-0-1'>rel="nofollow"</#if>>
                            <img src="${staticServer}${banner.webImageUrl}" data-app-img="${staticServer}${banner.appImageUrl}" alt="${banner.title}">
                        </a>
                    </li>
                </#list>
            </ul>
        </div>
        <div class="page-width">
            <@global.isAnonymous>
                <div class="register-ad-box fr tc">
                    <b class="h-title">预期年化收益率</b>
                    <p class="num-text clearfix">
                        <span class="percent">10%~13%</span>
                    </p>

                    <p class="welcome-text">welcome</p>
                    <a class="btn-normal" href="/register/user" onclick="cnzzPush.trackClick('21首页','Banner模块','免费注册')">免费注册 </a>
                    <i class="clearfix tr">已有账户？<a href="/login"
                                                   onclick="cnzzPush.trackClick('22首页','Banner模块','立即登录')"> 立即登录</a></i>
                </div>
            </@global.isAnonymous>
        </div>
    </div>

    <div class="notice-container bg-screen">
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
                        <li class="clearfix">
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
                        <span>预期年化收益10%~13%，<br>投资门槛50元起投</span>
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
                        <b class="clearfix">四大保障 投资无忧</b>
                        <span>12道保障措施并举，<br>资金、个人信息均安全</span>
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

            <div class="newer-experience clearfix" data-url="/loan/1">
                <i class="tag-icon"></i>
                <div class="con-inner">
                    <b class="newer-title">新手体验标 <span>仅限使用体验金投资</span></b>
                    <ul class="loan-info clearfix">
                        <li><span class="percent-number"> <i>${experienceLoanDto.baseRate}</i>%</span>预期年化收益</li>
                        <li><em class="duration-day">${experienceLoanDto.duration}</em>天<br>项目期限</li>
                    </ul>
                    <a href="/loan/1" class="btn-invest btn-normal">立即购买</a>
                </div>

            </div>
            <#list newbieLoans as loan>
                <div class="newer-experience clearfix hack-newbie" data-url="/loan/${loan.id?c}">
                    <i class="tag-icon"></i>
                    <div class="con-inner">
                        <b class="newer-title">${loan.name}</b>
                        <ul class="loan-info clearfix">
                            <li><span class="percent-number">
                                    <i><@percentInteger>${loan.baseRate+loan.activityRate}</@percentInteger></i>
                                <@percentFraction>${loan.baseRate+loan.activityRate}</@percentFraction>%
                                <#if (loan.newbieInterestCouponRate > 0)>
                                    <s class="sign-plus">+</s>
                                    <i><@percentInteger>${loan.newbieInterestCouponRate}</@percentInteger></i>
                                    <@percentFraction>${loan.newbieInterestCouponRate}</@percentFraction>%
                                </#if></span>预期年化收益
                            </li>
                            <li><em class="duration-day">${loan.duration}</em>天<br>项目期限</li>
                        </ul>
                        <#if loan.status== 'RAISING'>
                        <#--筹款-->
                            <a href="javascript:void(0)" class="btn-invest btn-normal">立即购买</a>
                        </#if>
                        <#if loan.status== 'PREHEAT'>
                        <#--预热中-->
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
                        </#if>
                        <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loan.status)>
                        <#--已售罄-->
                            <button class="btn-normal" disabled="">已售罄</button>
                        </#if>
                    </div>
                </div>
            </#list>
            <a href="/activity/landing-page" target="_blank"><img src="${staticServer}/images/homepage/hot-bag-b.png"
                                                                  alt="注册送668元红包" class="fr"></a>
        </div>

    <#--优选债权-->
        <div class="main-column-title">
            <i class="icon-title"></i>优选债权
            <a href="/loan-list" onclick="cnzzPush.trackClick('35首页','热门产品模块','更多')" class="hot-more">更多></a>
        </div>

        <div class="normal-loan">
            <#include "component/loan-title.ftl">
            <#list normalLoans as loan>
            <#include "component/loan-row.ftl">
        </#list>
        </div>


        <#if enterpriseLoans??>
            <div class="main-column-title">
                <i class="icon-title"></i>税易经营性借款
                <a href="/loan-list" onclick="cnzzPush.trackClick('35首页','热门产品模块','更多')" class="hot-more">更多></a>
            </div>

            <#include "component/loan-title.ftl">
            <#list enterpriseLoans as loan>
                <#include "component/loan-row.ftl">
            </#list>
        </#if>

        <div class="main-column-title">
            <i class="icon-title"></i>转让项目
            <a href="/transfer-list" onclick="cnzzPush.trackClick('47首页','转让项目模块','更多')" class="hot-more">更多></a>
        </div>

        <div class="target-category-box clearfix bg-screen">
            <div class="transfer-title">
                <span>项目名称</span>
                <span>预期年化收益</span>
                <span>转让价格</span>
                <span>项目本金</span>
                <span>剩余天数</span>
                <span></span>
            </div>
        </div>

        <#list transferApplications as loan>
            <#include "component/transfer-row.ftl">
        </#list>

        <div class="media-coverage-box">
            <h3 class="label-title">
                媒体报道
                <a href="/about/media" onclick="cnzzPush.trackClick('39首页','媒体报道模块','更多')" class="hot-more">更多></a>
            </h3>
            <ul class="media-list">
                <li><i>●</i><a rel="nofollow" href="http://www.greatchinese.com.cn/news/hyxw/20160907/15431.html"
                               onclick="cnzzPush.trackClick('41首页','媒体报道模块','唯一运营商')" target="_blank">拓天伟业成建设银行“税易-助保贷”唯一运营商</a>
                    <time>2016-09-07</time>
                </li>
                <li><i>●</i><a rel="nofollow" href="http://economy.gmw.cn/2016-03/31/content_19527114.htm"
                               onclick="cnzzPush.trackClick('73首页','媒体报道模块','霸道总裁')"
                               target="_blank">拓天速贷第二期全国排行活动正式启动</a>
                    <time>2016-03-31</time>
                </li>
                <li><i>●</i><a rel="nofollow" href="http://fj.qq.com/a/20160314/060811.htm"
                               onclick="cnzzPush.trackClick('40首页','媒体报道模块','财富盛宴大平台')" target="_blank">拓天速贷：财富盛宴大平台
                    感恩豪礼滚滚来</a>
                    <time>2016-03-14</time>
                </li>
                <li><i>●</i><a rel="nofollow"
                               href="http://money.china.com/fin/lc/201601/20/2443757.html?qq-pf-to=pcqq.c2c"
                               onclick="cnzzPush.trackClick('42首页','媒体报道模块','宝马名花有主')" target="_blank">拓天速贷：宝马名花有主，猴年豪礼来袭</a>
                    <time>2016-01-20</time>
                </li>
                <li><i>●</i><a rel="nofollow" href="http://w.huanqiu.com/r/MV8wXzgyNzQ4NDZfMTM5NF8xNDUxMzAzNzYy"
                               onclick="cnzzPush.trackClick('43首页','媒体报道模块','高效资产平台')"
                               target="_blank">拓天速贷以卓越风控打造高效资产平台</a>
                    <time>2015-12-28</time>
                </li>
            </ul>

        </div>

        <div class="partner-box clearfix">
            <h3 class="label-title">
                合作伙伴
            </h3>
            <ul class="partner-list">
                <li><a rel="nofollow" class="logo-capital" href="http://www.umpay.com/"
                       onclick="cnzzPush.trackClick('46首页','合作伙伴模块','联动优势')"  target="_blank">
                    <img src="${staticServer}/images/homepage/logo-lian.png" alt="安心签">
                </a>
                </li>

                <li><a rel="nofollow" class="logo-umpay" href="http://www.king-capital.com/"
                       onclick="cnzzPush.trackClick('45首页','合作伙伴模块','京都律师所')" target="_blank">
                    <img src="${staticServer}/images/homepage/logo-jing.png" alt="安心签">
                </a>
                </li>

                <li><a rel="nofollow" class="logo-umpay" href="https://www.anxinsign.com/"
                       onclick="cnzzPush.trackClick('47首页','合作伙伴模块','安心签')" target="_blank">
                    <img src="${staticServer}/images/homepage/logo-cfca.png" alt="安心签">
                </a>
                </li>
            </ul>
        </div>

    </div>
    <div class="book-invest-frame" style="display: none">
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
</div>


    <#include "component/coupon-alert.ftl" />
    <#include "component/login-tip.ftl" />
    <#include "component/red-envelope-float.ftl" />
    <#include "component/site-map.ftl" />
</@global.main>