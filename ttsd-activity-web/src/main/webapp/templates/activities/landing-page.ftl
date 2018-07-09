<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.landing_page_new_2018}" pageJavascript="${js.landing_page_new_2018}" activeNav="" activeLeftNav="" title="拓天速贷_新手注册送1000元投资红包_拓天速贷官网" keywords="拓天速贷,新手注册，1000元投资红包,6888元体验金,3%加息劵" description="拓天速贷新手注册送1000元投资红包,新人独享11%高息新手标,首次投资可获得3%加息券,新用户注册送6888元体验金,为广大投资用户提供多元化的投资选择和优质的综合投资服务.">
<div class="landing-page-container" id="landingTop">
    <@global.isAnonymous>
    <div class="banner" id="bannerBox">
        <div class="banner-img">
            <div class="my-page-width banner-con">

                    <div class="login-wrap" id="registerContainer">

                    <form action="/register/user" method="post" id="registerUserForm" autocomplete="off">
                        <h2 class="title"><span class="icon-title"></span><span>出借资金全程由富滇银行存管</span></h2>
                        <div class="login-con">
                            <div class="refer-person-info">您的好友<span class="refer-name"></span>邀请您领取投资大礼包</div>
                            <ul>
                                <li><input id="mobile" class="width100 height100 borderRadius mobileIcon" placeholder="输入手机号码"
                                           name="mobile"
                                           maxlength="11" type="text" autocomplete="off" validate></li>
                                <li><input class="width100 height100 borderRadius passwordIcon" placeholder="密码" maxlength="20"
                                           type="password" name="password" id="passwordInput" autocomplete="off" validate></li>
                                <li class="clearfix">
                                    <input class="borderRadius height100 captchaIcon" id="input_img_captcha" placeholder="图形验证码"
                                           name="appCaptcha"
                                           type="text" maxlength="5" validate>
                                    <em class="image-captcha"><img class="borderRadius height100" src="" alt=""> </em>
                                    <span class="img-change height100 nextImg wapStyleNone">换一张</span><br/>
                                </li>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <li class="captcha-li"><input id="smsCaptcha" class="height100 borderRadius smsIcon"
                                                              placeholder="手机验证码"
                                                              name="captcha" type="text" maxlength="6" validate>
                                    <input id="getCaptchaBtn" type="button" class="get-captcha borderRadius height100"
                                           value="获取验证码" /><br/>
                                    <span class="voice-captcha" id="voice_captcha" style="display: none">如收不到短信，可使用 <a
                                            href="javascript:;" id="voice_btn">语音验证</a> </span>
                                </li>
                                <li class="recommend" id="recommendLabel" style="display: none"><span
                                        class="icon-arrow-bottom"></span><span>请输入推荐人（此项选填）</span>
                                </li>
                                <li class="recommend" id="recommendLabelExist" style="display: none"><span>推荐人：<em
                                        id="referMobile"></em></span>
                                </li>
                                <li class="recomender-iphone"><input class="borderRadius width100 height100" type="text"
                                                                     name="referrer"
                                                                     maxlength="11" placeholder="推荐人手机号" validate></li>
                                <li class="agreement-li">
                                    <input type="checkbox" name="agreement" id="agreementInput" class="agreement-check"
                                    >
                                    <div id="agreementLable">
                                        <span class="icon-yesOrNo-checked checked"></span>
                                        <label for="agreementInput" class="check-label"><span>同意拓天速贷</span></label>
                                    </div>
                                    <a href="javascript:void(0);"
                                       class="show-agreement">《服务协议》</a></li>
                                <li class="submit-li"><input class="register-sub width100 height100" type="submit"
                                                             value="注册领取6888元体验金" disabled/></li>
                            </ul>
                        </div>
                    </form>
                </div>

            </div>
            <div class="invest-tip">市场有风险，投资需谨慎！</div>
        </div>
    </div>
    </@global.isAnonymous>
    <@global.isNotAnonymous>
        <div class="banner bannerNotLogin" id="bannerBoxLogin">
            <div class="banner-img banner-img-no-login" style="position: relative">
                <div class="invest-tip">市场有风险，投资需谨慎！</div>
                <div class="my-page-width banner-con">

                </div>
            </div>
        </div>
    </@global.isNotAnonymous>
    <div class="banner bannerNotLogin" id="bannerBoxApp" style="display: none">
        <div class="banner-img">
            <div class="my-page-width banner-con">

            </div>
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
                    <@global.isAnonymous>
                        <a class="gold-btn coupon-btn" href="javascript:;">领取6888元体验金</a>
                    </@global.isAnonymous>
                    <@global.isNotAnonymous>
                        <a class="gold-btn" href="/loan-list">马上投资</a>
                    </@global.isNotAnonymous>
                </div>
            </div>
            <div class="wap-con-swiper">
                    <div class="swiper-container" id="fuliList">
                        <div class="swiper-wrapper">
                            <div class="swiper-slide item1 coupon-btn">
                               <@global.isAnonymous>
                                 <a class="get-btn">领取6888元体验金</a>
                                </@global.isAnonymous>
                                <@global.isNotAnonymous>
                                    <a class="get-btn" href="/loan-list">马上投资</a>
                                </@global.isNotAnonymous>
                            </div>
                            <div class="swiper-slide item2 coupon-btn">
                                <@global.isAnonymous>
                                    <a class="get-btn">领取6888元体验金</a>
                                </@global.isAnonymous>
                                <@global.isNotAnonymous>
                                    <a class="get-btn" href="/loan-list">马上投资</a>
                                </@global.isNotAnonymous>
                            </div>
                            <div class="swiper-slide item3 coupon-btn">
                                <@global.isAnonymous>
                                    <a class="get-btn">领取6888元体验金</a>
                                </@global.isAnonymous>
                                <@global.isNotAnonymous>
                                    <a class="get-btn" href="/loan-list">马上投资</a>
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
                            <a class="red-ware-btn" href="/loan-list">马上投资</a>
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
                            <dt>8<span>%</span><em class="plus">+</em><span class="newbie-dt">2<i>%<i></i></span></dt>
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
                                    <div class="swiper-container" id="loanBoxList">
                                        <div class="swiper-wrapper">
                                            <div class="swiper-slide loan-wrap">
                                                <h2 class="loan-title">房产抵押借款</h2>
                                                <div class="loan-con">
                                                    <div class="loan-info"><span class="big-num">10</span><span class="percent">%</span>360<span
                                                            class="day">天</span></div>
                                                    <p class="rate">约定年化利率</p>
                                                    <p class="other"><span class="icon1"></span><span>银行资金存管</span> <span
                                                            class="icon2"></span><span>按天计息 即投即生息</span></p>
                                                    <p class="loan-link"><a href="/loan-list" class="btn-red">马上投资</a></p>
                                                </div>
                                            </div>
                                            <div class="swiper-slide loan-wrap">
                                                <h2 class="loan-title">车辆抵押借款</h2>
                                                <div class="loan-con">
                                                    <div class="loan-info"><span class="big-num">9</span><span class="percent">%</span>180<span
                                                            class="day">天</span></div>
                                                    <p class="rate">约定年化利率</p>
                                                    <p class="other"><span class="icon1"></span><span>银行资金存管</span> <span
                                                            class="icon2"></span><span>按天计息 即投即生息</span></p>
                                                    <p class="loan-link"><a href="/loan-list" class="btn-red">马上投资</a></p>
                                                </div>
                                            </div>
                                            <div class="swiper-slide loan-wrap">
                                                <h2 class="loan-title">车辆抵押借款</h2>
                                                <div class="loan-con">
                                                    <div class="loan-info"><span class="big-num">8</span><span class="percent">%</span>90<span
                                                            class="day">天</span></div>
                                                    <p class="rate">约定年化利率</p>
                                                    <p class="other"><span class="icon1"></span><span>银行资金存管</span> <span
                                                            class="icon2"></span><span>按天计息 即投即生息</span></p>
                                                    <p class="loan-link"><a href="/loan-list" class="btn-red">马上投资</a></p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="swiper-pagination"></div>
                                    </div>
                                </div>
                                <div class="right-bot border-section fr count-form">
                                    <form id="countForm" action="" class="clearfix">
                                        <ul>
                                            <li><label>投资金额：</label>
                                                <div class="border-section"><input type="text" name="money" id="moneyNum"><span>元</span></div>
                                            </li>
                                            <li><label>项目期限：</label>
                                                <div class="border-section"><input type="text" name="day" id="dayNum"><span>天</span></div>
                                            </li>
                                            <li class="rate-li"><label>约定年化利率：</label>
                                                <div class="border-section"><input type="text" name="rate" id="rateNum"><span>%</span></div>
                                            </li>
                                            <li class="error-li"><label></label>
                                               <div class="error-box"></div>
                                            </li>

                                        </ul>
                                        <p class="compute-link"><input type="submit" class="btn-red" value="计算收益" /></p>
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
                <div class="advance-con clearfix">
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
                        <dt class="icon-advance6"></dt>
                        <dd><p class="profile">获批ICP经营许可证</p>
                            <p class="font">稳健运营，合规披露</p></dd>
                    </dl>
                    <dl>
                        <dt class="icon-advance4"></dt>
                        <dd><p class="profile">四大保障，12项措施</p>
                            <p class="font">资金、个人信息均安全</p></dd>

                    </dl>
                    <dl>
                        <dt class="icon-advance5"></dt>
                        <dd><p class="profile">携手CFCA权威认证</p>
                            <p class="font">投资合同受法律保护</p></dd>
                    </dl>
                </div>
                <div class="gift-link textCenter">
                <@global.isAnonymous>
                    <a class="gold-btn coupon-btn" href="javascript:;">注册领取新手大礼包</a>
                </@global.isAnonymous>
                <@global.isNotAnonymous>
                    <a class="gold-btn" href="/loan-list">马上投资</a>
                </@global.isNotAnonymous>
                </div>

            </div>
            <div class="good-project-wap">
                <h2 class="title"></h2>
                    <ul class="project-list">
                        <li><div class="pro-item item1"><a class="btn-red" href="/loan-list">马上投资</a></div></li>
                        <li><div class="pro-item item2"><a class="btn-red" href="/loan-list">马上投资</a></div></li>
                        <li><div class="pro-item item3"><a class="btn-red" href="/loan-list">马上投资</a></div></li>
                        <li><div class="pro-item item4"><a class="btn-red" href="/loan-list">马上投资</a></div></li>
                    </ul>
            </div>
            <div class="link-bottom textCenter">
                <@global.isAnonymous>
                    <a class="gold-btn coupon-btn-bottom" href="javascript:;">注册领取新手大礼包</a>
                </@global.isAnonymous>
                <@global.isNotAnonymous>
                    <a class="gold-btn" href="/loan-list">马上投资</a>
                </@global.isNotAnonymous>
            </div>
            <div class="kindly-tips-wrap">
                <div class="title"><span class="tip-line left-line"></span><span class="squre"></span><span class="squre"></span><span class="squre"></span>温馨提示<span class="squre right-first"></span><span class="squre"></span><span class="squre"></span><span class="tip-line right-line"></span>
                </div>
                <div class="kindly-tips-con">
                    <p>1.活动时间：2018年6月1日起，本活动仅针对活动开始后注册的新用户；</p>
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
    <div class="app-container-landing clearfix" >
        <div class="logo"></div>
        <div class="app-detail">
            拓天速贷<br/>
            <em>互联网金融信息服务平台<em>
        </div>
        <div class="open-app">下载APP</div>
        <div class="close-app"></div>
    </div>
</div>



    <#include '../module/register-agreement.ftl' />
</@global.main>