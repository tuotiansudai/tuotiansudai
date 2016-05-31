<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="loan-detail-content" data-loan-status="${loan.loanStatus}" data-loan-progress="${loan.progress?string.computer}" data-loan-countdown="${loan.preheatSeconds?string.computer}"
     data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>">
    <div class="borderBox clearfix no-border">
        <div class="loan-model bg-w borderBox">
            <div class="news-share fl">
                <h2 class="title hd">${loan.name}</h2>
                <div class="chart-box">
                    <div class="box" title="已投${loan.progress?string("0.00")}%">
                        <div class="bg"></div>
                        <div class="rount"></div>
                        <div class="bg2"></div>
                        <div class="rount2" style="display: none;"></div>
                        <div class="pr-square-in">
                            <em>
                                <b><@percentInteger>${loan.basicRate}</@percentInteger><@percentFraction>${loan.basicRate}</@percentFraction></b>
                                <#if loan.activityRate!=0>+<@percentInteger>${loan.activityRate}</@percentInteger><@percentFraction>${loan.activityRate}</@percentFraction></#if>%
                            </em>
                            <i>预期年化收益</i>
                        </div>
                    </div>
                </div>
                <div class="chart-info">
                    项目金额：<@amount>${loan.loanAmount?string.computer}</@amount> 元<br/>
                    代理人：${loan.agentLoginName}<br/>
                    借款人：${loan.loanerLoginName}<br/>
                    项目期限：${loan.duration}天<br/>
                    募集期限：${loan.raisingPeriod}天<br/>
                    还款方式：${loan.type.getName()}<br/>
                    投资要求：${loan.minInvestAmount} 元起投，投资金额为 ${loan.investIncreasingAmount} 元的整数倍<br/>
                    <#if loan.productType != 'EXPERIENCE'>
                        <a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">借款协议样本</a>
                    </#if>
                </div>
                <#if loan.activityType == 'NEWBIE'>
                    <#if loan.newbieInterestCouponRate gt 0>
                        <div class="product-type-text" data-loan-product-type="${loan.productType!}">新手加息券+${loan.newbieInterestCouponRate}%</div>
                    </#if>
                <#else>
                    <#if loan.productType??>
                        <div class="product-type-text" data-loan-product-type="${loan.productType}">${loan.productType.getName()}</div>
                    </#if>
                </#if>
            </div>
            <div class="account-info fl">
                <h5 class="l-title">拓天速贷提醒您：投资非存款，投资需谨慎！</h5>
                <#if ["PREHEAT", "RAISING"]?seq_contains(loan.loanStatus)>
                    <form action="/invest" method="post" id="investForm">
                        <dl class="account-list">
                            <dd>
                                <span class="fl">可投金额：</span>
                                <em class="fr">
                                    <i class="amountNeedRaised-i" data-amount-need-raised="${loan.amountNeedRaised?string.computer}">${(loan.amountNeedRaised / 100)?string("0.00")}</i> 元
                                </em>
                            </dd>
                            <dd><span class="fl">账户余额：</span><em class="fr account-amount" data-user-balance="${loan.userBalance?string.computer}">${(loan.userBalance / 100)?string("0.00")} 元</em></dd>
                            <dd><span class="fl">每人限投：</span><em class="fr">${loan.maxInvestAmount} 元</em></dd>
                            <dd class="invest-amount tl" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span class="fl">投资金额：</span>
                                <input type="text" name="amount" data-l-zero="deny" data-v-min="0.00" data-min-invest-amount="${loan.minInvestAmount}" placeholder="0.00" value="${investAmount!loan.maxAvailableInvestAmount}"
                                       data-no-password-remind="${loan.hasRemindInvestNoPassword?c}"
                                       data-no-password-invest="${loan.investNoPassword?c}"
                                       data-auto-invest-on="${loan.autoInvest?c}"
                                       class="text-input-amount fr position-width"/>
                                <#if errorMessage?has_content>
                                    <span class="errorTip hide"><i class="fa fa-times-circle"></i>${errorMessage!}</span>
                                </#if>
                                <#if errorType?has_content>
                                    <input type="hidden" class="errorType hide" value="${errorType!}"/>
                                </#if>
                            </dd>

                            <dd class="experience-ticket clearfix" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span class="fl">优惠券：</span>
                                <div class="fr experience-ticket-box">
                                    <em class="experience-ticket-input <#if !coupons?has_content>disabled</#if>" id="use-experience-ticket">
                                        <span>
                                            <#if coupons?has_content>
                                                <#if maxBenefitUserCoupon??>
                                                    <#switch maxBenefitUserCoupon.couponType>
                                                        <#case "INTEREST_COUPON">
                                                            +${maxBenefitUserCoupon.rate * 100}%${maxBenefitUserCoupon.name}
                                                            <#break>
                                                        <#case "BIRTHDAY_COUPON">
                                                        ${maxBenefitUserCoupon.name}
                                                            <#break>
                                                        <#default>
                                                        ${maxBenefitUserCoupon.name}${(maxBenefitUserCoupon.amount / 100)?string("0.00")}元
                                                    </#switch>
                                                <#else>
                                                    请选择优惠券
                                                </#if>
                                            <#else>
                                                当前无可用优惠券
                                            </#if>
                                    </span>
                                    <i class="fa fa-sort-down fr"></i>
                                    <i class="fa fa-sort-up hide fr"></i>
                                </em>
                                <#if coupons?has_content>
                                    <ul class="ticket-list hide">
                                        <#list coupons as coupon>
                                            <#if !coupon.shared>
                                                <li data-coupon-id="${coupon.couponId?string.computer}"
                                                    data-user-coupon-id="${coupon.id?string.computer}"
                                                    data-coupon-type="${coupon.couponType}"
                                                    data-product-type-usable="${coupon.productTypeList?seq_contains(loan.productType)?string('true', 'false')}"
                                                    data-coupon-end-time="${coupon.endTime?string("yyyy-MM-dd")}T${coupon.endTime?string("HH:mm:ss")}"
                                                    <#if coupon.investLowerLimit!=0>class="lower-upper-limit"</#if>>
                                                    <input type="radio"
                                                           id="${coupon.id?string.computer}"
                                                           name="userCouponIds"
                                                           value="${coupon.id?string.computer}"
                                                           class="input-use-ticket"
                                                           <#if maxBenefitUserCoupon?? && maxBenefitUserCoupon.id == coupon.id>
                                                           checked
                                                           </#if>
                                                    />
                                                    <label>
                                                        <span class="sign">${coupon.couponType.getAbbr()}</span>
                                                        <span class="ticket-info">
                                                            <i class="ticket-title">
                                                                <#switch coupon.couponType>
                                                                    <#case "INTEREST_COUPON">
                                                                        +${coupon.rate * 100}%${coupon.name}
                                                                        <#break>
                                                                    <#case "BIRTHDAY_COUPON">
                                                                    ${coupon.name}
                                                                        <#break>
                                                                    <#default>
                                                                    ${coupon.name}${(coupon.amount / 100)?string("0.00")}元
                                                                </#switch>
                                                            </i>
                                                            <#if !(coupon.productTypeList?seq_contains(loan.productType))>
                                                                <br/>
                                                                <i class="ticket-term" title="[适用于<#list coupon.productTypeList as productType>${productType.getName()}<#if productType_has_next> 、</#if></#list>可用]">[适用于<#list coupon.productTypeList as productType>${productType.getName()}<#if productType_has_next> 、</#if></#list>可用]</i>
                                                            <#else>
                                                                <br/>
                                                                <#if coupon.investLowerLimit!=0>
                                                                    <i class="ticket-term lower-limit" data-invest-lower-limit="${coupon.investLowerLimit?string.computer}">[投资满${(coupon.investLowerLimit / 100)?string("0.00")}元可用]</i>
                                                                </#if>
                                                                <#if coupon.investLowerLimit==0>
                                                                    <i class="ticket-term"><#if coupon.couponType=='BIRTHDAY_COUPON'>[首月享${1 + coupon.birthdayBenefit}倍收益]<#else>[投资即可使用]</#if>
                                                                    </i>
                                                                </#if>
                                                            </#if>
                                                            </span>
                                                        </label>
                                                    </li>
                                                </#if>
                                            </#list>
                                        </ul>
                                        <#list coupons as coupon>
                                            <#if (coupon.shared && coupon.investLowerLimit==0 && coupon.productTypeList?seq_contains(loan.productType))>
                                                <input type="hidden" id="${coupon.id?string.computer}" name="userCouponIds" value="${coupon.id?string.computer}" data-coupon-id="${coupon.couponId?string.computer}" />
                                                <p class="red-tiptext clearfix">
                                                    <i class="icon-redbag"></i>
                                                    <span>${coupon.couponType.getName()}${(coupon.amount / 100)?string("0.00")}元（投资即可返现）</span>
                                                </p>
                                            </#if>
                                        </#list>
                                    </#if>
                                </div>
                            </dd>

                            <dd class="expected-interest-dd tc" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span>预计总收益：</span>
                                <span class="principal-income">0.00</span>
                                <span class="experience-income"></span>
                                元
                            </dd>

                            <dd class="time-item" <#if loan.loanStatus == "RAISING">style="display: none"</#if>>
                                <#if loan.preheatSeconds lte 1800>
                                    <i class="time-clock"></i><strong id="minute_show">00</strong><em>:</em><strong id="second_show">00</strong>以后可投资
                                <#else>
                                    ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                                </#if>
                            </dd>

                            <dd>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string.computer}"/>
                                <button id="investSubmit" class="btn-pay btn-normal" type="button" <#if loan.loanStatus == "PREHEAT">disabled="disabled"</#if>>
                                    <#if loan.loanStatus == "PREHEAT">预热中</#if>
                                    <#if loan.loanStatus == "RAISING">马上投资</#if>
                                </button>
                            </dd>
                            <@global.role hasRole="'INVESTOR'">
                                <#if !loan.investNoPassword>
                                    <dd>
                                        <a class="fl open-no-password-invest" id="noPasswordTips" data-open-agreement="${loan.autoInvest?c}" >
                                            推荐您开通免密投资
                                            <i class="fa fa-question-circle text-m" title="开通后您可以简化投资过程，理财快人一步"></i>
                                        </a>
                                    </dd>
                                </#if>
                            </@global.role>
                        </dl>
                    </form>
                </#if>
                <#if ["REPAYING", "RECHECK", "CANCEL", "OVERDUE", "COMPLETE"]?seq_contains(loan.loanStatus)>
                    <form action="/loan-list" method="get">
                        <dl class="account-list">
                            <dd class="img-status">
                                <img src="${staticServer}/images/sign/loan/${loan.loanStatus?lower_case}.png" alt=""/>
                            </dd>
                            <dd>
                                <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                            </dd>
                        </dl>
                    </form>
                </#if>
            </div>
        </div>
        <div class="chart-info-responsive bg-w">
            项目金额：<@amount>${loan.loanAmount?string.computer}</@amount> 元<br/>
            代理人：${loan.agentLoginName}<br/>
            借款人：${loan.loanerLoginName}<br/>
            项目期限：${loan.periods}<#if loan.type.getLoanPeriodUnit() == "MONTH"> 月<#else> 天</#if><br/>
            还款方式：${loan.type.getName()}<br/>
            投资要求：${loan.minInvestAmount} 元起投，投资金额为 ${loan.investIncreasingAmount} 元的整数倍<br/>
            <a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">借款协议样本</a>
        </div>
        <div class="bg-w borderBox mt-20 project-model">
            <div class="model-nav">
                <h3>项目描述</h3>
            </div>
            <div class="model-content">
                <ul class="info-list">
                    <li>1、新手体验项目是由拓天速贷专门提供给平台各类型新手客户体验平台流程的活动项目。</li>
                    <li>2、投资体验项目无需充值。</li>
                    <li>3、新手体验券是由拓天速贷用平台活动方式，为新注册用于提供平台项目投资体验的活动金额，新手体验券只能投资体验项目，不可提现，使用后可产生红包奖励</li>
                    <li>4、新注册用户通过获得体验券后，在体验项目专区点击使用。</li>
                    <li>5、新手体验项目不可转让。</li>
                    <li>6、为防止不法分子恶意刷取平台奖励，红包奖励需投资真实项目后方可提现。</li>
                    <li>本活动规则解释权归拓天速贷所有，如有疑问请联系在线客服或拨打400-169-1188</li>
                </ul>
            </div>
        </div>
        <div class="bg-w borderBox mt-20 project-model">
            <div class="model-nav">
                <h3>安全保障</h3>
            </div>
            <div class="model-content">
                <ul class="text-list">
                    <li>
                        <p class="intro-title">资金保障</p>
                        <p class="intro-icon icon-one"></p>
                        <p class="intro-text"><span>拓天速贷平台项目都是抵押债权，每笔债权都对应着相应的抵押物。如果投资过程中发生了资金风险，抵押物资将会被处理来为用户的资金提供保障。</span></p>
                    </li>
                    <li>
                        <p class="intro-title">交易保障</p>
                        <p class="intro-icon icon-two"></p>
                        <p class="intro-text"><span>拓天速贷接入了联动优势电子商务有限公司的资金托管系统。交易过程中的充值、投资、提现都在第三方支付平台进行，保证了资金流转的透明和安全。</span></p>
                    </li>
                    <li>
                        <p class="intro-title">信息保障</p>
                        <p class="intro-icon icon-three"></p>
                        <p class="intro-text"><span>采用银行级别的数据传输加密技术，保障用户的信息安全。在同城和异地均建立灾备设备，避免因自然灾害导致用户信息的损失</span></p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
    <#include "coupon-alert.ftl" />
</div>
    <#include "red-envelope-float.ftl" />
</@global.main>