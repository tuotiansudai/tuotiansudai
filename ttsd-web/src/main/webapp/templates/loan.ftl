<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="loan-detail-content" data-loan-status="${loan.loanStatus}" data-loan-progress="${loan.progress?string.computer}" data-loan-countdown="${loan.preheatSeconds?string.computer}"
     data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>">
    <div class="borderBox bg-w clearfix">
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
                        <i>年化收益</i>
                    </div>
                </div>
            </div>
            <div class="chart-info">
                项目金额：<@amount>${loan.loanAmount?string.computer}</@amount> 元<br/>
                代理人：${loan.agentLoginName}<br/>
                借款人：${loan.loanerLoginName}<br/>
                项目期限：${loan.periods}<#if loan.type.getLoanPeriodUnit() == "MONTH"> 月<#else> 天</#if><br/>
                还款方式：${loan.type.getName()}<br/>
                投资要求：<@amount>${loan.minInvestAmount?string.computer}</@amount> 元起投，投资金额为<@amount>${loan.investIncreasingAmount?string.computer}</@amount> 元的整数倍<br/>
                <a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">借款协议样本</a>
            </div>
            <#if loan.productType??>
                <div class="product-type-text">${loan.productType.getName()}</div>
            </#if>
        </div>
        <div class="account-info fl">
            <h5 class="l-title">拓天速贷提醒您：理财非存款，投资需谨慎！</h5>
            <#if ["PREHEAT", "RAISING"]?seq_contains(loan.loanStatus)>
                <form action="/invest" method="post">
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
                            <#assign defaultInvestAmount = loan.maxAvailableInvestAmount!>
                            <#if investAmount??>
                                <#assign defaultInvestAmount = investAmount>
                            </#if>
                            <span class="fl">投资金额：</span>
                            <input type="text" name="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" value="${defaultInvestAmount}"
                                   class="text-input-amount fr position-width"/>
                            <#if errorMessage?has_content>
                                <span class="errorTip hide"><i class="fa fa-times-circle"></i>${errorMessage!}</span>
                            </#if>
                        </dd>

                        <dd class="experience-ticket clearfix" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                            <span class="fl">优惠券：</span>
                            <div class="fr experience-ticket-box">
                                <em class="experience-ticket-input <#if !coupons?has_content>disabled</#if>" id="use-experience-ticket">
                                    <span><#if coupons?has_content>请选择优惠券<#else>当前无可用优惠券</#if></span>
                                    <i class="fa fa-sort-down fr"></i>
                                    <i class="fa fa-sort-up hide fr"></i>
                                </em>
                                <#if coupons?has_content>
                                    <ul class="ticket-list hide">
                                        <#list coupons as coupon>
                                            <#if !coupon.shared>
                                                <li data-coupon-id="${coupon.couponId?string.computer}"
                                                    data-coupon-type="${coupon.couponType}"
                                                    data-coupon-created-time="${coupon.createdTime?string("yyyy-MM-dd HH:mm:ss")}"
                                                    <#if coupon.investLowerLimit!=0 && coupon.investUpperLimit!=0>class="lower-upper-limit"</#if>>
                                                    <input type="radio" id="${coupon.id?string.computer}" name="userCouponIds" value="${coupon.id?string.computer}"  class="input-use-ticket" />
                                                    <label>
                                                        <span class="sign">${coupon.couponType.getAbbr()}</span>
                                                        <span class="ticket-info">
                                                            <i class="ticket-title">
                                                                <#if coupon.couponType=='INTEREST_COUPON'>
                                                                    +${coupon.rate * 100}%${coupon.name}
                                                                <#else>
                                                                ${coupon.name}${(coupon.amount / 100)?string("0.00")}元
                                                                </#if>
                                                            </i>
                                                            <#if coupon.investLowerLimit!=0>
                                                                <br/>
                                                                <i class="ticket-term lower-limit" data-invest-lower-limit="${coupon.investLowerLimit?string.computer}">
                                                                    [投资满${(coupon.investLowerLimit / 100)?string("0.00")}元可用]
                                                                </i>
                                                            </#if>
                                                            <#if coupon.investUpperLimit!=0>
                                                                <br/>
                                                                <i class="ticket-term upper-limit" data-invest-upper-limit="${coupon.investUpperLimit?string.computer}">
                                                                    [投资限${(coupon.investUpperLimit / 100)?string("0.00")}元内可用]
                                                                </i>
                                                            </#if>
                                                            <#if coupon.investLowerLimit==0 && coupon.investUpperLimit==0>
                                                                <br/>
                                                                <i class="ticket-term">
                                                                    [投资即返]
                                                                </i>
                                                            </#if>
                                                        </span>
                                                    </label>
                                                </li>
                                            </#if>
                                        </#list>
                                    </ul>
                                    <#list coupons as coupon>
                                        <#if coupon.shared && coupon.investLowerLimit==0 && coupon.investUpperLimit==0>
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
                            <span class="experience-income"></span>元
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
                            <button class="btn-pay btn-normal" type="submit" <#if loan.loanStatus == "PREHEAT">disabled="disabled"</#if>>
                                <#if loan.loanStatus == "PREHEAT">预热中</#if>
                                <#if loan.loanStatus == "RAISING">马上投资</#if>
                            </button>
                        </dd>

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
        <div class="chart-info-responsive">
            项目金额：<@amount>${loan.loanAmount?string.computer}</@amount> 元<br/>
            代理人：${loan.agentLoginName}<br/>
            借款人：${loan.loanerLoginName}<br/>
            项目期限：${loan.periods}<#if loan.type.getLoanPeriodUnit() == "MONTH"> 月<#else> 天</#if><br/>
            还款方式：${loan.type.getName()}<br/>
            投资要求：<@amount>${loan.minInvestAmount?string.computer}</@amount> 元起投，投资金额为<@amount>${loan.investIncreasingAmount?string.computer}</@amount> 元的整数倍<br/>
            <a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">借款协议样本</a>
        </div>
        <div class="bg-w clear-blank">
            <div class="loan-nav">
                <ul>
                    <li class="active">借款详情<i class="fa fa-caret-up"></i></li>
                    <li>出借记录<i class="fa fa-caret-up"></i></li>
                </ul>
            </div>
            <div class="loan-list pad-s">
                <div class="loan-list-con">
                    <div class="borderBox">
                        <h3 class="b-title">借款详情：</h3>
                    ${loan.descriptionHtml}
                    </div>

                    <div class="loan-material">
                        <h3>申请材料：</h3>
                        <div class="pic-list" id="picListBox">
                            <#list loan.loanTitleDto as loanTitle>
                                <#list loan.loanTitles as loanTitleRelation >
                                    <#if loanTitle.id == loanTitleRelation.titleId>
                                        <div class="title">${loanTitle.title}：</div>
                                        <#list loanTitleRelation.applicationMaterialUrls?split(",") as title>
                                            <img layer-src="${title}" src="${title}" alt="${loanTitle.title}"/>
                                        </#list>
                                    </#if>
                                </#list>
                            </#list>
                        </div>
                    </div>
                </div>
                <div class="loan-list-con">
                    <table class="table-striped">
                    </table>
                    <div class="pagination" data-url="/loan/${loan.id?string.computer}/invests" data-page-size="10">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#include "coupon-alert.ftl" />
</div>
    <#include "red-envelope-float.ftl" />
</@global.main>