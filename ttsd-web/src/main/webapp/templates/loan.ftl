<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="loan-detail-content">
    <div class="borderBox bg-w clearfix">
        <div class="news-share fl">
            <h2 class="hd">
                <#if loan.activityType == "NEWBIE">
                    <span class="hot"></span>
                </#if>
            ${loan.projectName}
                <input class="jq-loan-user" type="hidden" value="${loan.id}">
                <input id="loanStatus" type="hidden" value="${loan.loanStatus}">
            </h2>

            <div class="chart-box">
                <div class="box" title="已投${(loan.raiseCompletedRate * 100)?string("0.00")}%">
                    <div class="bg"></div>
                    <div class="rount"></div>
                    <div class="bg2"></div>
                    <div class="rount2" style="display: none;"></div>
                    <span class="sub-percent"><#if loan.activityRate??>+<@percentInteger>${loan.activityRate}</@percentInteger><@percentFraction>${loan.activityRate}</@percentFraction>%</#if></span>
                    <div id="num" class="num"><@percentInteger>${loan.basicRate}</@percentInteger><@percentFraction>${loan.basicRate}</@percentFraction>%</div>
                    <span class="title">年化收益率</span>
                </div>
            </div>
            <div class="chart-info">
                项目金额： <@amount>${loan.loanAmount}</@amount>元<br/>
                代理人：${loan.agentLoginName}<br/>
                借款人：${loan.loanerLoginName} <br/>
                <#if loan.type.getLoanPeriodUnit() == "MONTH">
                    项目期限：${loan.periods}月 <br/>
                </#if>
                <#if loan.type.getLoanPeriodUnit() == "DAY">
                    项目期限：${loan.periods}天 <br/>
                </#if>
                还款方式：${loan.type.getName()}<br/>
                投资要求：<@amount>${loan.minInvestAmount}</@amount>元起投,投资金额为<@amount>${loan.investIncreasingAmount}</@amount>的整数倍<br/>
                <a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_Blank">借款协议样本</a>
            </div>
        </div>
        <div class="account-info fl">
            <h5 class="l-title">拓天速贷提醒您：理财非存款，投资需谨慎！</h5>
            <#if loan.loanStatus == "RAISING">
                <form action="/invest" method="post">

                    <dl class="account-list">
                        <dd><span class="fl">可投金额：</span><em class="fr"><i class="amountNeedRaised-i">${loan.amountNeedRaised?string("0.00")}</i> 元</em></dd>
                        <dd><span class="fl">账户余额：</span><em class="fr account-amount">${loan.balance?string("0.00")} 元</em></dd>
                        <dd><span class="fl">每人限投：</span><em class="fr">${loan.maxInvestAmount} 元</em></dd>
                        <dd class="invest-amount tl"><#assign defaultInvestAmount = loan.maxAvailableInvestAmount!>
                            <#if investAmount?has_content>
                                <#assign defaultInvestAmount = investAmount>
                            </#if>
                            <input type="text" name="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" value="${defaultInvestAmount}" class="text-input-amount"/>
                            <#if errorMessage?has_content>
                                <span class="errorTip hide">${errorMessage!}</span>
                            </#if>
                        </dd>
                        <#list coupons as coupon>
                        <dd class="experience-ticket">
                            <span class="fl"><i class="fa fa-money"></i> ${coupon.name}<@amount>${coupon.amount?string(0)}</@amount>元</span>
                            <em class="fr"><label for="use-experience-ticket"> 使用体验劵</label> <input type="checkbox" id="use-experience-ticket" name="userCouponId" value="${coupon.id?string('0')}"></em>
                        </dd>
                        <dd class="experience-revenue hide"><span class="fl">体验劵预期收益：</span><em class="fr"><i class="experience-interest"><@amount>${coupon.interest?string(0)}</@amount></i>元</em></dd>
                        </#list>

                        <dd><span class="fl">预计总收益：</span><em class="fr"><i class="expected-interest"></i>元</em></dd>
                        <dd>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string("0")}"/>
                            <button class="btn-pay btn-normal" type="submit">马上投资</button>
                        </dd>

                    </dl>
                </form>
            </#if>
            <#if loan.loanStatus == "PREHEAT">
                <form action="/invest" method="post">
                    <dl class="account-list">
                       <dd><span class="fl">可投金额：</span><em class="fr"><i class="amountNeedRaised-i">${loan.amountNeedRaised?string("0.00")}</i>元</em>
                       <dd><span class="fl">账户余额：</span><em class="fr account-amount">${loan.balance?string("0.00")} 元</em></dd>
                       <dd><span class="fl">每人限投：</span><em class="fr">${loan.maxInvestAmount}元</em></dd>
                   <dd class="invest-amount tl">
                       <#assign defaultInvestAmount = loan.maxAvailableInvestAmount!>
                       <#if investAmount?has_content>
                           <#assign defaultInvestAmount = investAmount>
                       </#if>
                       <input type="text" name="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" value="${defaultInvestAmount}" class="text-input-amount"/>
                       <#if errorMessage?has_content>
                           <span class="errorTip hide">${errorMessage!}</span>
                       </#if>
                    </dd>
                    <#list coupons as coupon>
                        <dd class="experience-ticket">
                            <span class="fl"><i class="fa fa-money"></i> ${coupon.name}<@amount>${coupon.amount?string(0)}</@amount>元</span>
                            <em class="fr"><label for="use-experience-ticket"> 使用体验劵</label> <input type="checkbox" id="use-experience-ticket" name="userCouponId" value="${coupon.id?string('0')}"></em>
                        </dd>
                        <dd class="experience-revenue hide"><span class="fl">体验劵预期收益：</span><em class="fr"><i class="experience-interest"><@amount>${coupon.interest?string(0)}</@amount></i>元</em></dd>
                    </#list>

                    <dd class="time-item">
                        <#if loan.preheatSeconds lte 1800>
                            <i class="time-clock"></i>
                            <strong id="minute_show">00</strong>
                            <em>:</em>
                            <strong id="second_show">00</strong>
                            以后可投资
                        <#else>
                        ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                        </#if>
                    </dd>
                        <dd class="hide"><span class="fl">预计总收益：</span><em class="fr"><i class="expected-interest"></i>元</em></dd>
                        <dd><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string("0")}"/>
                            <button class="btn-pay btn-normal" type="submit" disabled="disabled">预热中</button>
                        </dd>
                    </dl>

                </form>
            </#if>
            <#if loan.loanStatus == "REPAYING">
            <form action="/loan-list" method="get">
                <dl class="account-list">
                    <dd class="img-status">
 <img src="${staticServer}/images/sign/loan/repaying.png"  alt=""/>
                    </dd>
                    <dd>
                        <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                    </dd>
                </dl>
            </form>
            </#if>
            <#if loan.loanStatus == "RECHECK">
            <form action="/loan-list" method="get">
                <dl class="account-list">
                    <dd class="img-status">
                    <img src="${staticServer}/images/sign/loan/recheck.png"  alt=""/>
                    </dd>
                    <dd>
                        <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                    </dd>
                </dl>
            </form>
            </#if>
            <#if loan.loanStatus == "CANCEL">
            <form action="/loan-list" method="get">
                <dl class="account-list">
                    <dd class="img-status">
                     <img src="${staticServer}/images/sign/loan/cancel.png"  alt=""/>
                    </dd>
                    <dd>
                        <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                    </dd>
                </dl>
            </form>
            </#if>
            <#if loan.loanStatus == "OVERDUE">
            <form action="/loan-list" method="get">
                <dl class="account-list">
                    <dd class="img-status">
                    <img src="${staticServer}/images/sign/loan/overdue.png" alt=""/>
                    </dd>
                    <dd>
                        <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                    </dd>
                </dl>
            </form>
            </#if>
            <#if loan.loanStatus == "COMPLETE">
            <form action="/loan-list" method="get">
                <dl class="account-list">
                    <dd class="img-status">
                      <img src="${staticServer}/images/sign/loan/complete.png"  alt=""/>
                    </dd>
                    <dd>
                        <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                    </dd>
                </dl>
            </form>
            </#if>
    </div>
    <div class="bg-w clear-blank">
            <div class="loan-nav">
                <ul>
                    <li class="active">借款详情<i class="fa fa-caret-up"></i> </li>
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
                    <div class="pagination" data-url="/loan/${loan.id?string("0")}/invests" data-page-size="10">
                    </div>
                </div>
            </div>
    </div>
    </div>
</div>
<script>
    var intDiff = parseInt(${loan.preheatSeconds?string('0')});//倒计时总秒数量
    var java_point = ${(loan.raiseCompletedRate * 100)?string('0')}; //后台传递数据
    <@global.role hasRole="'INVESTOR'">
    var user_can_invest = true;
    </@global.role>
</script>
</@global.main>