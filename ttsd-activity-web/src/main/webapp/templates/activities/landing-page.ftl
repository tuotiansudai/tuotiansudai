<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.landing_page}" pageJavascript="${js.landing_page}" activeNav="" activeLeftNav="" title="拓天速贷_新手注册送1000元投资红包_拓天速贷官网" keywords="拓天速贷,新手注册，1000元投资红包,6888元体验金,3%加息劵" description="拓天速贷新手注册送1000元投资红包,新人独享11%高息新手标,首次投资可获得3%加息券,新用户注册送6888元体验金,为广大投资用户提供多元化的投资选择和优质的综合投资服务.">

<div class="landingContainerBox">
    <div class="landing-container">

        <div class="landing-top-banner">
        </div>

        <div class="newbie-step-one page-width">
            <div class="image-title"></div>
            <dl class="new-user-list clearfix">
                <dt class="clearfix tc">新手体验项目</dt>
                <dd><i>13</i>% <br/><em>预期年化收益</em></dd>
                <dd><i>3</i>天 <br/><em>项目期限</em></dd>
            </dl>

            <@global.isAnonymous>
                <div class="tc"><a href="/register/user" class="btn-normal" id="btn-experience">领取6888元体验金</a></div>
            </@global.isAnonymous>
        </div>

        <div class="newbie-step-two">
            <div class="image-decoration-bg"></div>
            <div class="image-decoration-bg-app-l"></div>
            <div class="image-decoration-bg-app-r"></div>
            <div class="image-title"></div>
            <div class="image-red-envelope"></div>

            <div class="tc"><a href="/loan-list?productType=JYF" class="btn-normal" id="btn-red-envelope">立即领取</a></div>

        </div>

        <div class="newbie-step-three page-width">
            <div class="image-title"></div>
            <div class="subtitle tc">
                <div class="subtitle-container">
                    <i>新人独享11%高息新手标，30天灵活期限，满足您对资金流动性的需求</i>
                </div>
            </div>

            <dl class="new-user-list clearfix">
                <dt class="clearfix tc">新手专享标</dt>
                <dd><i>11</i>% <br/><em>预期年化收益</em></dd>
                <dd><i>30</i>天 <br/><em>项目期限</em></dd>
            </dl>

            <div class="tc"><a href="/loan-list?productType=JYF" class="btn-normal">立即投资</a></div>

        </div>

        <div class="newbie-step-four">
            <div class="image-decoration-bg"></div>
            <div class="image-decoration-bg-app-l"></div>
            <div class="image-decoration-bg-app-r"></div>
            <div class="image-title"></div>
            <div class="subtitle tc">
                <div class="subtitle-container">
                    <i>注册后15天内完成首次投资，可获得3%加息券</i>
                </div>
            </div>

            <div class="image-coupon"></div>

            <div class="image-steps tc">
                
            </div>
            <div class="tc"><a href="#" class="btn-normal" id="btn-get-coupon">立即注册领取</a></div>

        </div>
        <div class="newbie-step-five tc">
            <dl class="newbie-step-five-dl clearfix">
                <dt class="clearfix tc">拓天速贷为您的资金安全保驾护航</dt>
                <dd>
                    <span class="newbie-step5 icon-one"></span>
                <br>
                <i>CFCA权威认证</i>
                    <br/>
                    <p>携手中国金融认证中心<br>投资合同受法律保护</p></dd>
                <dd>
                <span class="newbie-step5 icon-two"></span>
                <br><i>风控严谨</i>
                    <br/>
                    <p>六重风控，22道手续<br>历史全额兑付，0逾期0坏账</p></dd>
                <dd>
                <span class="newbie-step5 icon-three"></span>
                <br><i>稳健安全</i>
                    <br/>
                    <p>预期年化收益8%～11%<br>房/车抵押债权安全系数高</p></dd>
            </dl>

        </div>

        <div class="newbie-step-six">
            <div class="newbie-step-six-box">
                <p>温馨提示</p>
                <p>1. 平台新注册用户可使用6888元体验金投资新手体验项目，投资周期为3天，到期可获得收益，该笔收益可在 "我的账户"
                    中查看，投资累计满1000元即可提现（投资债权转让项目除外）；</p>
                <p>2. 30天 "新手专享" 债权每次限投50-10000元，每人仅限投1次；</p>
                <p>3. 用户所获红包及加息券可在 "我的账户-我的宝藏" 查看；</p>
                <p>4. 每笔投资仅限使用一张优惠券，用户可在投资时优先选择收益最高的优惠券使用，并在 "优惠券" 一栏中进行勾选；</p>
                <p>5. 使用红包金额将于所投债权放款后返至您的账户；</p>
                <p>6. 使用加息券所得收益，将体现在该笔投资项目收益中，用户可在 "我的账户" 中查询；</p>
                <p>7. 每个身份证仅限参加一次，刷奖、冒用他人身份证、银行卡者一经核实，取消活动资格，所得奖励不予承兑；</p>
                <p>8. 活动遵循拓天速贷法律声明，最终解释权归拓天速贷平台所有。</p>
            </div>

        </div>

        <#if !isAppSource>
            <div class="newbie-step-register">
                <div class="register-box">
                    <div class="refer-person-info">您的好友<span class="refer-name"></span>邀请您领取投资大礼包</div>
                    <form class="register-user-form" id="registerUserForm" action="/register/user" method="post" autocomplete="off"
                          novalidate="novalidate">
                        <ul class="reg-list tl register-ul register-icon-list">
                            <li class="reg-row-container">
                            <#--手机号:-->
                                <i class="icon-mobile reg-icon"></i>
                                <input validate type="text" id="mobile" name="mobile" class="mobile input-width"
                                       placeholder="手机号"
                                       maxlength="11" value="">
                            </li>
                        
                            <li class="reg-row-container">
                            <#--密码:-->
                                <i class="icon-password reg-icon"></i>
                                <input validate type="password" id="password" name="password" placeholder="密码" maxlength="20"
                                       class="password input-width" value="">
                            </li>

                            <li class="code reg-row-container">
                            <#--验证码:-->
                                <i class="icon-img-captcha reg-icon"></i>
                                
                                <em class="image-captcha">
                                    <img src="" alt=""/>
                                </em>
                                <span class="img-change">换一张</span>
                                <input validate type="text" id="appCaptcha" name="appCaptcha" placeholder="验证码" maxlength="5"
                                       class="appCaptcha fl" value="">
                            </li>
                        
                            <li class="reg-row-container voice-row clearfix">
                            <#--手机验证码:-->
                                <i class="icon-captcha reg-icon"></i>
                                <span class="captcha-tag" id="pcCaptcha">
                                <button type="button" class="fetch-captcha btn" disabled="disabled" id="fetchCaptcha">获取验证码</button>
                                <input validate type="text" class="captcha" autocomplete="off" name="captcha" id="captcha"
                                       autocorrect="off" autocapitalize="off" placeholder="手机验证码" maxlength="6">
                           <span class="voice-captcha" id="voice_captcha" style="display: none">如收不到短信，可使用 <a href="javascript:;" id="voice_btn">语音验证</a> </span>

                            </span>
                            </li>
                    
                            <li class="agree-register-protocol">
                                <input type="checkbox" name="agreement" id="agreementInput" class="agreement-check"
                                       checked>
                                <label for="agreementInput" class="check-label">同意拓天速贷<a href="javascript:void(0);"
                                                                                         class="show-agreement">《服务协议》</a></label>
                            </li>
                            <li class="tc">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="referrer" value="">
                                <#if success?? && success == false>
                                    <div class="error">注册失败，请检查您提交的信息是否正确！</div>
                                </#if>
                                <input type="submit" class="register-user" value="立即注册" disabled>
                            </li>
                            <@global.isAnonymous>
                                <li class="tc mobile-agreement">
                                    <label>点击立即注册即同意拓天速贷<a href="/register/user" class="show-agreement">《服务协议》</a></label>
                                </li>
                            </@global.isAnonymous>
                        </ul>
                    </form>

                </div>
            </div>
        </#if>
    </div>
</div>

<div class="image-captcha-dialog" style="display: none;">
    <form class="image-captcha-form" id="imageCaptchaForm" >
        <div class="image-captcha-inner">
        <img src="" alt="" class="image-captcha"/>
        <input type="text" class="image-captcha-text" name="imageCaptcha" maxlength="5" placeholder="请输入图形验证码"/>
        <input type="hidden" name="mobile">

            <div class="tc">
                <div class="error-box tl"></div>
                <input type="submit" class="image-captcha-confirm btn-normal" value="确定"/>
            </div>
        </div>
    </form>
</div>
    <#include '../module/register-agreement.ftl' />
</@global.main>