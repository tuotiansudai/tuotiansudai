<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="标的详情" pageCss="${css.loan_detail}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main">
    <div class="item-block bg-loan">
        <div class="news-share">
            <h2 class="hd">

                <#if loan.activityType == "NOVICE">
                    <span class="hot"></span>
                </#if>
                ${loan.projectName}
                <input class="jq-loan-user" type="hidden" value="${loan.id}">
                <input id="loanStatus" type="hidden" value="${loan.loanStatus}">
            </h2>

            <div class="chart-box">
                <div class="box" title="已投${(loan.raiseCompletedRate * 100)?string("0.00")}%">
                    <div class="bg"></div>
                    <div class="rount" ></div>
                    <div class="bg2"></div>
                    <div class="rount2" style="display: none;"></div>
                    <span class="sub-percent">+${loan.activityRate}%</span>

                    <div id="num" class="num">${loan.basicRate}%</div>
                    <span class="title">年化收益率</span>
                </div>
            </div>
            <div class="chart-info">
                <#--<p>已投：<span class="point">${loan.raiseCompletedRate?string("0.00")}%</span></p>-->
                <p>项目金额： ${loan.loanAmount}万元</p>

                <p>代理人：${loan.agentLoginName}</p>

                <p>借款人：${loan.loanerLoginName}</p>
            <#if loan.type.getLoanPeriodUnit() == "MONTH">
                <p>项目期限：${loan.periods}月 </p>
            </#if>
            <#if loan.type.getLoanPeriodUnit() == "DAY">
                <p>项目期限：${loan.periods}天 </p>
            </#if>
                <p>还款方式：${loan.type.getName()}</p>
                <p>投资要求：${loan.minInvestAmount}元起投,投资金额为${loan.investIncreasingAmount}的整数倍</p>
                <a href="/pdf/loanAgreementSample.pdf" target="_Blank">借款协议样本</a>
            </div>
        </div>
        <div class="account-info">
        <#if loan.loanStatus == "RAISING">
            <form action="/invest" method="post">
                <h2 class="hd"></h2>
            <div class="ttsd-tips">拓天速贷提醒您：理财非存款，投资需谨慎！</div>
            <div class="item-block">
                <span class="sub-hd">可投金额：</span>
                <span class="num amountNeedRaised"><i class="amountNeedRaised-i">${loan.amountNeedRaised?string("0.00")}</i>元</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">账户余额：</span>
                <span class="num balance"><i class="account-amount">${loan.balance?string("0.00")}</i>元</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">每人限投：</span>
                <span class="num">${loan.maxInvestAmount}元</span>
            </div>
            <div class="item-block clearfix" style="padding:0;">
                <#assign defaultInvestAmount = loan.maxAvailableInvestAmount!>
                <#if investAmount?has_content>
                    <#assign defaultInvestAmount = investAmount>
                </#if>
                <input type="text" name="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" value="${defaultInvestAmount}" class="text-input-amount"/>


                <#if errorMessage?has_content>

                    <span class="loan-detail-error-msg"><i class="loan-detail-error-msg-li">x</i>${errorMessage!}</span>
                <#else >
                    <span class="loan-detail-error-msg" style="display: none"><i class="loan-detail-error-msg-li">x</i></span>
                </#if>
            </div>
            <div class="btn-position">
                <div class="item-block" >
                    <span class="sub-hd" >预计总收益：</span>
                    <span class="num "><i class="expected-interest"></i>元</span>
                </div>
                <div class="item-block">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string("0")}"/>
                    <button class="btn-pay" type="submit">马上投资</button>
                </div>
            </div>
            </form>
        </#if>
        <#if loan.loanStatus == "PREHEAT">
            <form action="/invest" method="post">
            <h2 class="hd"></h2>
            <div class="ttsd-tips">拓天速贷提醒您：理财非存款，投资需谨慎！</div>
            <div class="item-block">
                <span class="sub-hd">可投金额：</span>
                <span class="num amountNeedRaised"><i class="amountNeedRaised-i">${loan.amountNeedRaised?string("0.00")}<i>元</span>
            </div>

            <div class="item-block">
                <span class="sub-hd">账户余额：</span>
                <span class="num"><i class="red account-amount">${loan.balance?string("0.00")}</i>元</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">每人限投：</span>
                <span class="num">${loan.maxInvestAmount}元</span>
            </div>
            <div class="item-block clearfix">
                <#assign defaultInvestAmount = loan.maxAvailableInvestAmount!>
                <#if investAmount?has_content>
                    <#assign defaultInvestAmount = investAmount>
                </#if>
                <input type="text" name="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" value="${defaultInvestAmount}" class="text-input-amount"/>
                <#if errorMessage?has_content>

                    <span class="loan-detail-error-msg"><i class="loan-detail-error-msg-li">x</i>${errorMessage!}</span>
                <#else >
                    <span class="loan-detail-error-msg" style="display: none"><i class="loan-detail-error-msg-li">x</i></span>
                </#if>
            </div>
            <div class="item-block">
                <div class="time-item">
                    <#if loan.preheatSeconds lte 1800>
                        <strong id="minute_show">00分</strong>
                        <strong>：</strong>
                        <strong id="second_show">00秒</strong>
                        以后可投资
                    <#else >
                        ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                    </#if>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string("0")}"/>
            </div>
            <div class="item-block">
                <button class="btn-pay grey" type="submit" disabled="disabled">预热中</button>
            </div>
            </form>
        </#if>
        <#if loan.loanStatus == "REPAYING">
            <form action="/loanList/web" method="get">
                <div class="item-block" style="text-align: center">
                    <img src="/images/loan/repaying.png" width="200" height="200" alt=""/>
                </div>
                <div class="item-block">
                    <span class="sub-hd"></span>
                    <span class="num"></span>
                </div>
                <div class="item-block">
                    <button class="btn-pay" type="submit">查看其他项目</button>
                </div>
            </form>
        </#if>
        <#if loan.loanStatus == "RECHECK">
            <form action="/loanList/web" method="get">
                <div class="item-block" style="text-align: center">
                    <img src="/images/loan/recheck.png" width="200" height="200" alt=""/>
                </div>
                <div class="item-block">
                    <span class="sub-hd"></span>
                    <span class="num"></span>
                </div>
                <div class="item-block">
                    <button class="btn-pay" type="submit">查看其他项目</button>
                </div>
            </form>
        </#if>
        <#if loan.loanStatus == "CANCEL">
            <form action="/loanList/web" method="get">
                <div class="item-block" style="text-align: center">
                    <img src="/images/loan/cancel.png" width="200" height="200" alt=""/>
                </div>
                <div class="item-block">
                    <span class="sub-hd"></span>
                    <span class="num"></span>
                </div>
                <div class="item-block">
                    <button class="btn-pay" type="submit">查看其他项目</button>
                </div>
            </form>
        </#if>
        <#if loan.loanStatus == "OVERDUE">
            <form action="/loanList/web" method="get">
                <div class="item-block" style="text-align: center">
                    <img src="/images/loan/overdue.png" width="200" height="200" alt=""/>
                </div>
                <div class="item-block">
                    <span class="sub-hd"></span>
                    <span class="num"></span>
                </div>
                <div class="item-block">
                    <button class="btn-pay" type="submit">查看其他项目</button>
                </div>
            </form>
        </#if>
        <#if loan.loanStatus == "COMPLETE">
            <form action="/loanList/web" method="get">
                <div class="item-block" style="text-align: center">
                    <img src="/images/loan/complete.png" width="200" height="200" alt=""/>
                </div>
                <div class="item-block">
                    <span class="sub-hd"></span>
                    <span class="num"></span>
                </div>
                <div class="item-block">
                    <button class="btn-pay" type="submit">查看其他项目</button>
                </div>
            </form>
        </#if>
        </div>
    </div>

    <div class="item-block bg-loan loan-box">
        <div class="nav">
            <ul>
                <li class="active"><span>借款详情</span></li>
                <li><span>出借纪录</span></li>
            </ul>
        </div>
        <div class="loan-list">
            <div class="loan-list-con loan-details active">
                <div class="loan-detail">
                    <h3>借款详情：</h3>
                ${loan.descriptionHtml}
                </div>

                <div class="loan-material">
                    <h3>申请材料：</h3>
                    <div class="pic-list">
                    <#list loan.loanTitleDto as loanTitle>
                        <div class="title">${loanTitle_index + 1}、${loanTitle.title}：</div>
                        <ul class="img-list">
                            <#list loan.loanTitles as loanTitleRelation >
                                <#if loanTitle.id == loanTitleRelation.titleId>
                                    <#list loanTitleRelation.applyMetarialUrl?split(",") as title>
                                        <li><img src="${title}" alt="${loanTitle.title}"/></li>
                                    </#list>
                                </#if>
                            </#list>
                        </ul>
                    </#list>
                    </div>
                </div>
            </div>

            <div class="loan-list-con">
                <table class="table-striped">
                </table>
                <div class="pagination" data-url="/loan/${loan.id}/invests" data-page-size="2">
                </div>
            </div>
        </div>
    </div>
</div>

<#include "footer.ftl">
<@global.javascript pageJavascript="${js.loan_detail}">
</@global.javascript>
</body>
</html>
<script>
    var intDiff = parseInt(${loan.preheatSeconds});//倒计时总秒数量
    var java_point = ${loan.amountNeedRaised}; //后台传递数据
</script>