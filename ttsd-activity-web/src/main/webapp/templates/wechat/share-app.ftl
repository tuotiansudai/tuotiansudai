<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.share_app}" pageJavascript="${js.app_shareSuc}"  title="新手福利_拓天新手投资_拓天速贷">

<#--<#include "../pageLayout/header.ftl" />-->
<#--<div class="share-app-container clearfix" id="shareAppContainer">-->
<#--<div class="share-container" >-->
<#--<div id="register_btn">注册</div>-->
<#--<div class="share-item">-->
<#--<#if isOldUser?? && isOldUser>-->
<#--<!-- 老用户信息 start &ndash;&gt;-->
<#--<div class="item-intro share-old"></div>-->
<#--<!-- 老用户信息  end &ndash;&gt;-->
<#--<#else>-->
<#--<div class="item-tel">-->
<#--<span>${referrerInfo!}</span>-->
<#--</div>-->
<#--<div class="item-intro">-->
<#--送你<span>6888元</span>体验金+<span>668元</span>投资红包-->
<#--</div>-->
<#--</#if>-->


<#--<div class="item-form">-->
<#--<div class="item-int tc">-->
<#--<span class="gift-icon"></span>-->
<#--</div>-->
<#--<div class="item-int tc">-->
<#--<a href="/app/download" class="btn item-submit" >礼包到手下载APP赚钱</a>-->
<#--</div>-->
<#--<div class="item-int pad-m-tb">-->
<#--<p class="tc">好友<span>${referrerInfo!}</span>邀请你来拓天速贷投资</p>-->
<#--<p class="tc">新手活动收益高，奖不停，拿红包到手软！</p>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->
<#--<div class="newbie-step-one page-width">-->
<#--<div class="image-title"></div>-->
<#--<dl class="new-user-list clearfix">-->
<#--<dt class="clearfix tc">新手体验项目</dt>-->
<#--<dd><i>13</i>% <br/><em>约定年化利率</em></dd>-->
<#--<dd><i>3</i>天 <br/><em>项目期限</em></dd>-->
<#--</dl>-->

<#--</div>-->

<#--<div class="newbie-step-two">-->
<#--<div class="image-decoration-bg"></div>-->
<#--<div class="image-decoration-bg-app-l"></div>-->
<#--<div class="image-decoration-bg-app-r"></div>-->
<#--<div class="image-title"></div>-->
<#--<div class="image-red-envelope"></div>-->
<#--</div>-->

<#--<div class="newbie-step-three page-width">-->
<#--<div class="image-title"></div>-->
<#--<div class="subtitle tc">-->
<#--<div class="subtitle-container">-->
<#--<i>新人独享11%高息新手标，30天灵活期限，满足您对资金流动性的需求</i>-->
<#--</div>-->
<#--</div>-->

<#--<dl class="new-user-list clearfix">-->
<#--<dt class="clearfix tc">新手专享标</dt>-->
<#--<dd><i>11</i>% <br/><em>约定年化利率</em></dd>-->
<#--<dd><i>30</i>天 <br/><em>项目期限</em></dd>-->
<#--</dl>-->
<#--</div>-->

<#--<div class="newbie-step-four">-->
<#--<div class="image-decoration-bg"></div>-->
<#--<div class="image-decoration-bg-app-l"></div>-->
<#--<div class="image-decoration-bg-app-r"></div>-->
<#--<div class="image-title"></div>-->
<#--<div class="subtitle tc">-->
<#--<div class="subtitle-container">-->
<#--<i>注册后15天内完成首次投资，可获得3%加息券</i>-->
<#--</div>-->
<#--</div>-->

<#--<div class="image-coupon"></div>-->

<#--<div class="image-steps tc">-->
<#--<span class="step-one"></span>-->
<#--<span class="step-two"></span>-->
<#--<span class="step-three"></span>-->

<#--</div>-->
<#--</div>-->
<#--<#include '../module/register-reason.ftl' />-->

<#--<div class="newbie-step-six">-->
<#--<div class="newbie-step-six-box">-->
<#--<p>温馨提示</p>-->
<#--<p>1. 平台新注册用户可使用6888元体验金投资新手体验项目，投资周期为3天，到期可获得收益，该笔收益可在 "我的账户"-->
<#--中查看，投资累计满1000元即可提现（投资债权转让项目除外）；</p>-->
<#--<p>2. 30天 "新手专享" 债权每次限投50-10000元，每人仅限投1次；</p>-->
<#--<p>3. 用户所获红包及加息券可在 "我的账户-我的宝藏" 查看；</p>-->
<#--<p>4. 每笔投资仅限使用一张优惠券，用户可在投资时优先选择收益最高的优惠券使用，并在 "优惠券" 一栏中进行勾选；</p>-->
<#--<p>5. 使用红包金额将于所投债权放款后返至您的账户；</p>-->
<#--<p>6. 使用加息券所得收益，将体现在该笔投资项目收益中，用户可在 "我的账户" 中查询；</p>-->
<#--<p>7. 每个身份证仅限参加一次，刷奖、冒用他人身份证、银行卡者一经核实，取消活动资格，所得奖励不予承兑；</p>-->
<#--<p>8. 活动遵循拓天速贷法律声明，最终解释权归拓天速贷平台所有。</p>-->
<#--</div>-->

<#--</div>-->
<#--</div>-->

<div class="landing-page-container" id="landingTop">
    <div id="register_btn">注册</div>
    <div class="success-con">
        <#if isOldUser?? && isOldUser>
            <!-- 老用户信息 start -->
            <div class="share-old"></div>
            <!-- 老用户信息  end -->
        <#else>
            <div class="share-refer-info"><span class="refer-name">${referrerInfo!}</span><br/>送你<strong>6888元</strong>体验金+<strong>1000元</strong>投资红包
            </div>
            <div class="gift-icon"></div>
        </#if>
    <div class="textCenter">
    <a href="/app/download" class="btn-app" >礼包到手下载APP赚钱</a>
    </div>
    <div class="invite-friend-invest">
    <p class="textCenter">好友<span>${referrerInfo!}</span>邀请你来拓天速贷投资</p>
    <p class="textCenter">新手活动收益高，奖不停，拿红包到手软！</p>
    </div>
    </div>
    <div class="landing-main">
        <div class="star-content">
            <div class="newbie-register-wrap">
                <h2 class="newbie-register-title"></h2>

                <div class="desc-container">
                    <h2 class="title">新手体验项目</h2>
                    <div class="line"></div>
                    <div class="transverse-line left-line"></div>
                    <div class="transverse-line right-line"></div>
                    <div class="top"></div>
                    <div class="content clearfix">
                        <dl class="fl left-desc">
                            <dt>13<span>%</span></dt>
                            <dd>约定年化利率</dd>
                        </dl>

                        <dl class="fr right-desc">
                            <dt>3<span>天</span></dt>
                            <dd>项目期限</dd>
                        </dl>

                    </div>
                    <div class="bot"></div>

                </div>


                <div class="to-experience textCenter">
                    <a class="gold-btn coupon-btn" href="javascript:;">领取6888元体验金</a>
                </div>
            </div>
            <div class="wap-con-swiper">
                <div class="swiper-container" id="fuliList">
                    <div class="swiper-wrapper">
                        <div class="swiper-slide item1">
                            <@global.isAnonymous>
                                <a class="get-btn coupon-btn">领取6888元体验金</a>
                            </@global.isAnonymous>
                            <@global.isNotAnonymous>
                                <a class="get-btn" href="/loan-list">立即投资</a>
                            </@global.isNotAnonymous>
                        </div>
                        <div class="swiper-slide item2">
                            <@global.isAnonymous>
                                <a class="get-btn coupon-btn">领取6888元体验金</a>
                            </@global.isAnonymous>
                            <@global.isNotAnonymous>
                                <a class="get-btn" href="/loan-list">立即投资</a>
                            </@global.isNotAnonymous>
                        </div>
                        <div class="swiper-slide item3">
                            <@global.isAnonymous>
                                <a class="get-btn coupon-btn">领取6888元体验金</a>
                            </@global.isAnonymous>
                            <@global.isNotAnonymous>
                                <a class="get-btn" href="/loan-list">立即投资</a>
                            </@global.isNotAnonymous>
                        </div>
                    </div>
                    <div class="controlsBtn prevBtn"></div>
                    <div class="controlsBtn nextBtn"></div>
                </div>
            </div>
            <div class="red-ware-wrap">
                <h2 class="red-ware-title"></h2>
                <div class="red-ware">
                    <div class="red-ware-img">
                        <@global.isAnonymous>
                            <a class="red-ware-btn coupon-btn" href="javascript:;">立即领取</a>
                        </@global.isAnonymous>
                        <@global.isNotAnonymous>
                            <a class="red-ware-btn" href="/loan-list">立即投资</a>
                        </@global.isNotAnonymous>
                    </div>
                </div>
            </div>
            <div class="newbie-increase-wrap">
                <h2 class="increase-title"></h2>
                <div class="desc-container">
                    <h2 class="title">新手私享加息</h2>
                    <div class="line"></div>
                    <div class="transverse-line left-line"></div>
                    <div class="transverse-line right-line"></div>
                    <div class="top"></div>
                    <div class="content clearfix">
                        <dl class="fl left-desc">
                            <dt>9<span>%</span><em class="plus">+</em><span class="newbie-dt">2<i>%<i></i></span></dt>
                            <dd>约定年化利率</dd>
                        </dl>

                        <dl class="fr right-desc ">
                            <dt class="newbie">最长90天</dt>
                            <dd>项目期限</dd>
                        </dl>

                    </div>
                    <div class="bot"></div>

                </div>
                <div class="to-loan textCenter">
                    <a class="gold-btn" href="/loan-list">马上投资</a>
                </div>
                <div class="loan-wrap">
                    <div class="desc-container">
                        <div class="top"></div>
                        <div class="content">
                            <div class="top-content clearfix border-section">
                                <div class="first">
                                    <p class="top-p">10<span>%</span></p>
                                    <p>约定年化利率</p>
                                </div>
                                <div class="split"></div>
                                <div class="second">
                                    <p class="top-p">360<span>天</span></p>
                                    <p>项目期限</p>
                                </div>
                                <div class="split"></div>
                                <div class="third">
                                    <p><a href="/loan-list" class="btn-red">马上投资</a></p>
                                    <p class="bank"><span class="checkbox-red"></span><span>银行资金存管</span></p>
                                </div>
                            </div>
                            <div class="bot-content clearfix">
                                <div class="left-bot border-section fl">
                                    <div class="loan-wrap">
                                        <h2 class="loan-title">房产抵押借款</h2>
                                        <div class="loan-con">
                                            <div class="loan-info"><span class="big-num">10</span><span class="percent">%</span>360<span
                                                    class="day">天</span></div>
                                            <p class="rate">约定年化利率</p>
                                            <p class="other"><span class="icon1"></span><span>银行存管</span> <span
                                                    class="icon2"></span><span>按天计息 即投即生息</span></p>
                                            <p class="loan-link"><a href="/loan-list" class="btn-red">马上投资</a></p>
                                        </div>
                                    </div>
                                </div>
                                <div class="right-bot border-section fr count-form">
                                    <form id="countForm" action="" class="clearfix">
                                        <ul>
                                            <li><label>投资金额：</label>
                                                <div class="border-section"><input type="text" name="money"
                                                                                   id="moneyNum"><span>元</span></div>
                                            </li>
                                            <li><label>项目期限：</label>
                                                <div class="border-section"><input type="text" name="day"
                                                                                   id="dayNum"><span>天</span></div>
                                            </li>
                                            <li class="rate-li"><label>约定年化利率：</label>
                                                <div class="border-section"><input type="text" name="rate" id="rateNum"><span>%</span>
                                                </div>
                                            </li>
                                            <li class="error-li"><label></label>
                                                <div class="error-box"></div>
                                            </li>

                                        </ul>
                                        <p class="compute-link"><input type="submit" class="btn-red" value="计算收益"/></p>
                                        <p class="profit">本息合计<strong id="resultNum">0</strong>元</p>
                                        <p class="small-tip">计算结果仅供参考，以实际收益为准</p>
                                    </form>


                                </div>
                            </div>

                        </div>
                        <div class="bot"></div>

                    </div>
                </div>
            </div>
            <div class="six-advance-wrap clearfix">
                <h2 class="title"></h2>
                <div class="advance-con">
                    <dl>
                        <dt class="icon-advance1"></dt>
                        <dd><p class="profile">银行资金存管</p>
                            <p class="font">与平台自有资金物理隔离</p></dd>
                    </dl>
                    <dl>
                        <dt class="icon-advance2"></dt>
                        <dd><p class="profile">约定年化利率8%-10%</p>
                            <p class="font">房/车抵押债权安全系数高</p></dd>
                    </dl>
                    <dl>
                        <dt class="icon-advance3"></dt>
                        <dd><p class="profile">六重风控，22道手续</p>
                            <p class="font">历史全额兑付，0逾期0坏账</p></dd>
                    </dl>
                    <dl>
                        <dt class="icon-advance4"></dt>
                        <dd><p class="profile">获批ICP经营许可证</p>
                            <p class="font">稳健运营，合规披露</p></dd>
                    </dl>
                    <dl>
                        <dt class="icon-advance5"></dt>
                        <dd><p class="profile">四大保障，12项措施</p>
                            <p class="font">资金、个人信息均安全</p></dd>
                    </dl>
                    <dl>
                        <dt class="icon-advance6"></dt>
                        <dd><p class="profile">携手CFCA权威认证</p>
                            <p class="font">投资合同受法律保护</p></dd>
                    </dl>
                </div>
                <div class="gift-link textCenter">
                    <a class="gold-btn coupon-btn" href="javascript:;">注册领取新手大礼包</a>
                </div>

            </div>
            <div class="good-project-wap">
                <h2 class="title"></h2>
                <ul class="project-list">
                    <li>
                        <div class="pro-item item1"><a class="btn-red" href="/loan-list">马上投资</a></div>
                    </li>
                    <li>
                        <div class="pro-item item2"><a class="btn-red" href="/loan-list">马上投资</a></div>
                    </li>
                    <li>
                        <div class="pro-item item3"><a class="btn-red" href="/loan-list">马上投资</a></div>
                    </li>
                    <li>
                        <div class="pro-item item4"><a class="btn-red" href="/loan-list">马上投资</a></div>
                    </li>
                </ul>
            </div>
            <div class="kindly-tips-wrap">
                <div class="title">温馨提示</span>
                </div>
                <div class="kindly-tips-con">
                    <p>1.活动时间：2018年X月X日起，本活动仅针对活动开始后注册的新用户；</p>
                    <p>2.平台新注册用户可使用6888元体验金投资新手体验项目，投资周期为3天，到期可获得收益，该笔收益可在 "我的账户" 中查看，投资累计满1000元即可提现（投资债权转让项目除外）；</p>
                    <p>3.2%新手私享加息项目每个注册用户限投一次，每次限投50元-10万元，新手私享项目不参与阶梯加息，且不与平台其他优惠活动同享；</p>
                    <p>4.1000元新手红包有效期为7天，为方便您分散投资，将以红包组的形式发放到用户账户，您可在pc端 "我的账户-我的宝藏" 或APP端“我的”中查看；</p>
                    <p>5.用户每笔投资仅限使用一张优惠券，用户可在投资时优先选择收益最高的优惠券使用，并在 "优惠券" 一栏中进行勾选，使用红包金额将于所投债权放款后返至您的账户；</p>
                    <p>6.投资时产生的提现费用及手续费由用户自理；</p>
                    <p>7.每个身份证仅限参加一次，刷奖、冒用他人身份证、银行卡者一经核实，取消活动资格，所得奖励不予承兑；</p>
                    <p>8.活动遵循拓天速贷法律声明，最终解释权归拓天速贷平台所有。</p>
                </div>
            </div>

        </div>

    </div>

</div>
</@global.main>