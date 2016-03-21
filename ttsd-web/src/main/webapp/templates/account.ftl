<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.account_overview}" activeNav="我的账户" activeLeftNav="账户总览" title="账户总览">
<script type="text/javascript">
    var pydata = {
        balance:'${((balance/100)?string('0.00'))!}',
        collectingPrincipal:'${((collectingPrincipal/100)?string('0.00'))!}',
        collectingInterest:'${((collectingInterest/100)?string('0.00'))!}'
    };
</script>
<div class="content-container account-overview">
    <div class="bRadiusBox spad bg-w clearfix">
        <img src="${staticServer}/images/sign/profile.jpg" class="fl accountImg" >
        <div class="profile-box">
            <span><em>您好：${loginName!}</em></span>
            <ul class="proList">
                <li class="fl"><a class="fa fa-envelope-o fa-fw" href="/personal-info"></a></li>
                <#if signedIn?? && signedIn>
                    <li class="fl sign-top no-click"><span class="btn-sign finish-sign">已签到</span></li>
                <#else >
                    <li class="fl sign-top"><span class="btn-sign will-sign" data-url="/point/sign-in" id="signBtn">签到</span></li>
                </#if>
                <li class="fl beans-number">可用财豆:${myPoint!}</li>
                <li class="fr"><a class="btn-normal" href="/recharge">充值</a></li>
                <li class="fr"><a class="btn-primary" href="/withdraw">提现</a></li>
            </ul>
        </div>
    </div>
    <div class="assets-box clear-blank">
        <div class="assets-report bRadiusBox fl bg-w">
            <h3>资产总额：<span>${(((balance+freeze+collectingPrincipal+collectingInterest)/100)?string('0.00'))!}元</span></h3>

            <div id="ReportShow" style="width:100%; height:115px; "></div>
        </div>
        <div class="assets-detail bRadiusBox fr bg-w">
            <ul class="detail-list">
                <li><b>我的余额：</b><span id="balance">${((balance/100)?string('0.00'))!}</span>元</li>
                <li><b>累计收益：</b><span>${(((collectedReward+collectedInterest)/100)?string('0.00'))!}</span>元</li>
                <li><b>待收本金：</b><span>${((collectingPrincipal/100)?string('0.00'))!}</span>元</li>
                <li><b>待收利息：</b><span>${((collectingInterest/100)?string('0.00'))!}</span>元</li>
                <li><b>已收利息：</b><span>${((collectedInterest/100)?string('0.00'))!}</span>元</li>
                <li><b>冻结金额：</b><span>${((freeze/100)?string('0.00'))!}</span>元</li>
            </ul>
        </div>
    </div>
    <#if successSumRepay??>
        <div class="LastMonth bRadiusBox clear-blank bg-w">
            <ul class="PaymentSwitch">
                <li class="current"><a href="javascript:void(0);">本月未还款</a></li>
            </ul>
            <table class="table table-striped">
                <caption>本月未还款总额：￥${((successSumRepay/100)?string('0.00'))!}元 <a href="/loaner/loan-list" class="fr">更多...</a></caption>
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>年利率</th>
                    <th>贷款周期</th>
                    <th>项目周期</th>
                    <th>预计还款(元)</th>
                    <th>还款日期</th>
                </tr>
                </thead>
                <tbody>
                    <#if repayList??>
                        <#list repayList as repay>
                        <tr>
                            <td><a href="/loan/${repay.loan.id?string('0')}">${repay.loan.name!}</a></td>
                            <td>${(((repay.loan.baseRate+repay.loan.activityRate)*100)?string('0.00'))!} %</td>
                            <td>${(repay.loan.periods?string('0'))!}<#if repay.loan.type == 'INVEST_INTEREST_MONTHLY_REPAY' || repay.loan.type == 'LOAN_INTEREST_MONTHLY_REPAY'>个月<#else>天</#if></td>
                            <td>第${(repay.period?string('0'))!}期/${(repay.loan.periods?string('0'))!}期</td>
                            <td><#if repay.status == 'COMPLETE'>${(((repay.corpus+repay.actualInterest+repay.defaultInterest)/100)?string('0.00'))!}<#else>${(((repay.corpus+repay.expectedInterest+repay.defaultInterest)/100)?string('0.00'))!}</#if>

                            </td>
                            <td><#if repay.status == 'COMPLETE'>${(repay.actualRepayDate?string('MM月dd日'))!}<#else>${(repay.repayDate?string('MM月dd日'))!}</#if></td>
                        </tr>
                        </#list>
                    <#else>
                    <tr>
                        <td colspan="6" class="tc">暂时没有记录</td>
                    </tr>
                    </#if>
                </tbody>
            </table>
        </div>
    </#if>
    <div class="tMonthPayment bRadiusBox clear-blank bg-w" id="tMonthBox">
        <ul class="PaymentSwitch">
            <li><a href="javascript:void(0);"> 本月已收回款</a></li>
            <li class="current"><a href="javascript:void(0);">本月待收回款</a></li>
        </ul>
        <table class="table table-striped">
            <caption>本月已收回款总额：￥${((successSumInvestRepay/100)?string('0.00'))!}元 <a href="/investor/invest-list" class="fr">更多...</a> </caption>
            <thead>
            <tr>
                <th>项目名称</th>
                <th>年利率</th>
                <th>贷款周期</th>
                <th>项目周期</th>
                <th>预计还款(元)</th>
                <th>还款日期</th>
            </tr>
            </thead>
            <tbody>
                <#if successSumInvestRepayList??>
                    <#list successSumInvestRepayList as successSumInvestRepay>
                    <tr>
                        <td>
                            <i <#if successSumInvestRepay.birthdayCoupon>class="birth-icon" data-benefit="${successSumInvestRepay.birthdayBenefit}"</#if>></i>
                            <a href="/loan/${successSumInvestRepay.loan.id?string('0')}" class="month-title">${successSumInvestRepay.loan.name!}</a>
                        </td>
                        <td>${(((successSumInvestRepay.loan.activityRate+successSumInvestRepay.loan.baseRate)*100)?string('0.00'))!}%</td>
                        <td>${(successSumInvestRepay.loan.periods?string('0'))!}<#if successSumInvestRepay.loan.type == 'INVEST_INTEREST_MONTHLY_REPAY' || successSumInvestRepay.loan.type == 'LOAN_INTEREST_MONTHLY_REPAY'>个月<#else>天</#if></td>
                        <td>第${(successSumInvestRepay.period?string('0'))!}期/${(successSumInvestRepay.loan.periods?string('0'))!}期</td>
                        <td>${successSumInvestRepay.actualAmount!}</td>
                        <td>${(successSumInvestRepay.actualRepayDate?string('MM月dd日'))!}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="6" class="tc">本月应收本息${(((successSumInvestRepay+notSuccessSumInvestRepay)/100)?string('0.00'))!}元</td>
            </tr>
            </tfoot>
        </table>
        <div class="clear-blank"></div>
        <table class="table table-striped">
            <caption>本月待收回款总额：￥${((notSuccessSumInvestRepay/100)?string('0.00'))!}元<a href="/investor/invest-list" class="fr">更多...</a> </caption>
            <thead>
            <tr>
                <th>项目名称</th>
                <th>年利率</th>
                <th>贷款周期</th>
                <th>项目周期</th>
                <th>预计还款(元)</th>
                <th>还款日期</th>
            </tr>
            </thead>
            <tbody>
                <#if notSuccessSumInvestRepayList??>
                    <#list notSuccessSumInvestRepayList as notSuccessSumInvestRepay>
                    <tr>
                        <td>
                            <i <#if notSuccessSumInvestRepay.birthdayCoupon>class="birth-icon" data-benefit="${notSuccessSumInvestRepay.birthdayBenefit}"</#if>></i>
                            <a href="/loan/${notSuccessSumInvestRepay.loan.id?string('0')}" class="month-title">${notSuccessSumInvestRepay.loan.name!}</a>
                        </td>
                        <td>${(((notSuccessSumInvestRepay.loan.activityRate+notSuccessSumInvestRepay.loan.baseRate)*100)?string('0.00'))!}%</td>
                        <td>${(notSuccessSumInvestRepay.loan.periods?string('0'))!}<#if notSuccessSumInvestRepay.loan.type == 'INVEST_INTEREST_MONTHLY_REPAY' || notSuccessSumInvestRepay.loan.type == 'LOAN_INTEREST_MONTHLY_REPAY'>个月<#else>天</#if></td>
                        <td>第${(notSuccessSumInvestRepay.period?string('0'))!}期/${(notSuccessSumInvestRepay.loan.periods?string('0'))!}期</td>
                        <td>${notSuccessSumInvestRepay.amount!}
                        </td>
                        <td>${(notSuccessSumInvestRepay.repayDate?string('MM月dd日'))!}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="6" class="tc">本月应收本息${(((successSumInvestRepay+notSuccessSumInvestRepay)/100)?string('0.00'))!}元</td>
            </tr>
            </tfoot>
        </table>
    </div>
    <div class="newProjects bRadiusBox clear-blank bg-w">
        <table class="table">
            <caption>最新投资项目 <a href="/investor/invest-list" class="fr">更多...</a> </caption>
            <thead>
            <tr>
                <th>交易时间</th>
                <th>交易详情</th>
                <th>交易状态</th>
                <th>下次回款(元)</th>
                <th>我的投资(元)</th>
            </tr>
            </thead>
            <tbody>
                <#if (latestInvestList?size>0)>
                    <#list latestInvestList as latestInvest>
                    <tr>
                        <td>${(latestInvest.investTime?string('yyyy-MM-dd'))!}</td>
                        <td>
                            <i <#if latestInvest.birthdayCoupon>class="birth-icon" data-benefit="${latestInvest.birthdayBenefit}"</#if>></i>
                            <a href="/loan/${latestInvest.loanId?string('0')}" class="trade-detail">${latestInvest.loanName!}</a>
                        </td>
                        <td>投资成功</td>
                        <td><#if latestInvest.status??>${(latestInvest.repayDate?string('yyyy-MM-dd'))!} /
                            ${(((latestInvest.corpus+latestInvest.defaultInterest+latestInvest.expectedInterest-latestInvest.expectedFee)/100)?string('0.00'))!}<#else>-/-</#if>
                        </td>
                        <td>￥${((latestInvest.investAmount/100)?string('0.00'))!}</td>
                    </tr>
                    </#list>
                <#else>
                <tr>
                    <td colspan="6" class="tc">暂时没有记录</td>
                </tr>
                </#if>
            </tbody>
        </table>
    </div>
    <div class="sign-layer" id="signLayer">
        <div class="sign-layer-list">
            <div class="sign-top">
                <div class="close-btn" id="closeSign"></div>
                <p class="sign-text">签到成功，领取5财豆！</p>

                <p class="tomorrow-text">明日可领10财豆</p>

                <p class="img-beans">
                    <img src="${staticServer}/images/sign/sign-beans.png"/>
					<span class="add-dou">
						+5
					</span>
                </p>

                <p class="intro-text">连续签到，财豆翻倍送，最多每天可领<span>80</span>财豆！</p>
            </div>
            <div class="sign-bottom">
                <ul>
                    <li>
                        <p class="day-name">第1天</p>

                        <p class="day-beans">
                            <span>5</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第2天</p>

                        <p class="day-beans">
                            <span>10</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第3天</p>

                        <p class="day-beans">
                            <span>20</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第4天</p>

                        <p class="day-beans">
                            <span>40</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第5天</p>

                        <p class="day-beans">
                            <span>80</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第6天</p>

                        <p class="day-beans">
                            <span>80</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li>
                        <p class="day-name">第7天</p>

                        <p class="day-beans">
                            <span>80</span>
                            <i class="bean-img"></i>
                        </p>
                    </li>
                    <li class="last-day">
                        <p class="day-name">第N天</p>

                        <p class="day-beans">
                            <span>...</span>
                        </p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
</@global.main>