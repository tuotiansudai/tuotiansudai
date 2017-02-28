<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.account_overview}" pageJavascript="${js.account_overview}" activeNav="我的账户" activeLeftNav="账户总览" title="账户总览">
<script type="text/javascript">
    var pydata = {
        balance: '${((balance/100)?string('0.00'))!}',
        expectedTotalCorpus: '${((expectedTotalCorpus/100)?string('0.00'))!}',
        expectedTotalInterest: '${((expectedTotalInterest/100)?string('0.00'))!}'
    };
</script>

<div class="content-container account-overview">

    <div class="column-box profile-box bg-w clearfix">
        <span class="fl account-profile"></span>
        <div class="welcome-text">
                <em class="fl tip-hello">您好：${mobile!}</em>
                <em class="fr">可用积分:<span id="MyAvailablePoint">${myPoint?string.computer}</span></em>
         </div>
        <div class="sign-list fl">
            <span class="vip vip${userMembershipLevel!}"></span>
            <a href="/personal-info" class="user-info"></a>
        </div>
        <ul class="proList fr">
            <#if signedIn?? && signedIn>
                <li class="fl sign-top no-click"><span class="btn-sign finish-sign" data-url="/point-shop/sign-in" id="signBtn">已签到</span></li>
            <#else >
                <li class="fl sign-top"><span class="btn-sign will-sign" data-url="/point-shop/sign-in" id="signBtn">签到</span></li>
            </#if>
         </ul>
    </div>
    <div class="column-box">

        </div>
    <div class="bRadiusBox spad bg-w clearfix">

        <div class="profile-box">
            <ul class="proList">

                <#--<li class="fl beans-number">可用积分:<span id="MyAvailablePoint">${myPoint?string.computer}</span></li>-->
                <li class="fr"><a class="btn-normal" href="/recharge">充值</a></li>
                <li class="fr"><a class="btn-primary" href="/withdraw">提现</a></li>
            </ul>
        </div>
    </div>
    <div class="assets-box clear-blank">
        <div class="assets-report bRadiusBox fl bg-w">
            <h3>资产总额：<span>${(((balance+freeze+expectedTotalCorpus+expectedTotalInterest)/100)?string('0.00'))!}元</span></h3>

            <div id="ReportShow" style="width:100%; height:115px; "></div>
        </div>
        <div class="assets-detail bRadiusBox fr bg-w">
            <ul class="detail-list">
                <li><b>我的余额：</b><span id="balance">${((balance/100)?string('0.00'))!}</span>元</li>
                <li><b>累计收益：</b><span>${((totalIncome/100)?string('0.00'))!}</span>元</li>
                <li><b>冻结金额：</b><span>${((freeze/100)?string('0.00'))!}</span>元</li>
                <li><b>已收投资收益：</b><span>${(actualTotalInterest/100)?string('0.00')!}</span>元</li>
                <li><b>待收投资本金：</b><span>${((expectedTotalCorpus/100)?string('0.00'))!}</span>元</li>
                <li><b>已收推荐奖励：</b><span>${((referRewardAmount/100)?string('0.00'))!}</span>元</li>
                <li><b>待收预期收益：</b><span>${((expectedTotalInterest/100)?string('0.00'))!}</span>元</li>
                <li><b>已收红包奖励：</b><span>${((redEnvelopeAmount/100)?string('0.00'))!}</span>元</li>
                <li><b>待收体验金收益：</b><span>${((expectedExperienceInterest/100)?string('0.00'))!}</span>元</li>
                <li><b>已收体验金收益：</b><span>${((actualExperienceInterest/100)?string('0.00'))!}</span>元</li>
            </ul>
        </div>
    </div>
    <div class="new-user-award">
        <span>HOT</span>
        <#if isUsableCouponExist>
            您还有优惠券没有使用，<a href="/my-treasure" title="快来使用吧">快来使用吧</a>
        <#else>
            新用户注册就送6888元体验金+668元现金红包，<a href="/activity/landing-page" title="详情请点击">详情请点击</a>
        </#if>
    </div>
    <#if expectedRepayAmountOfMonth??>
        <div class="LastMonth bRadiusBox clear-blank bg-w">
            <ul class="PaymentSwitch">
                <li class="current"><a href="javascript:void(0);">本月未还款</a></li>
            </ul>
            <table class="table table-striped">
                <caption>本月未还款总额：￥${((expectedRepayAmountOfMonth/100)?string('0.00'))!}元 <a href="/loaner/loan-list" class="fr">更多...</a></caption>
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>预期年化收益率</th>
                    <th>借款周期</th>
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
                            <td>${(repay.loan.duration?string('0'))!}天</td>
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
            <caption>本月已收回款总额：￥${((actualInvestRepay/100)?string('0.00'))!}元 <a href="/investor/invest-list" class="fr">更多...</a></caption>
            <thead>
            <tr>
                <th>项目名称</th>
                <th>预期年化收益率</th>
                <th>借款周期</th>
                <th>项目周期</th>
                <th>预计还款(元)</th>
                <th>还款日期</th>
            </tr>
            </thead>
            <tbody>
                <#if actualInvestRepayList??>
                    <#list actualInvestRepayList as actualInvestRepayItem>
                    <tr>
                        <td>
                            <i <#if actualInvestRepayItem.birthdayCoupon>class="birth-icon" data-benefit="${actualInvestRepayItem.birthdayBenefit}"</#if>></i>
                            <a href="/loan/${actualInvestRepayItem.loan.id?c}" class="month-title">${actualInvestRepayItem.loan.name!}</a>
                        </td>
                        <td>${(((actualInvestRepayItem.loan.activityRate+actualInvestRepayItem.loan.baseRate)*100)?string('0.00'))!}%</td>
                        <td>${(actualInvestRepayItem.loan.duration?c)!}天</td>
                        <td>第${(actualInvestRepayItem.period?c)!}期/${(actualInvestRepayItem.loan.periods?c)!}期</td>
                        <td>${actualInvestRepayItem.actualAmount!}
                            <#if actualInvestRepayItem.loan.productType == 'EXPERIENCE'>(体验金收益)</#if>
                        </td>
                        <td>${(actualInvestRepayItem.actualRepayDate?string('MM月dd日'))!}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="6" class="tc">本月应收本息${(((actualInvestRepay+expectedInvestRepay)/100)?string('0.00'))!}元</td>
            </tr>
            </tfoot>
        </table>
        <table class="table table-striped">
            <caption>本月待收回款总额：￥${((expectedInvestRepay/100)?string('0.00'))!}元<a href="/investor/invest-list" class="fr">更多...</a></caption>
            <thead>
            <tr>
                <th>项目名称</th>
                <th>预期年化收益率</th>
                <th>借款周期</th>
                <th>项目周期</th>
                <th>预计还款(元)</th>
                <th>还款日期</th>
            </tr>
            </thead>
            <tbody>
                <#if expectedInvestRepayList??>
                    <#list expectedInvestRepayList as expectedInvestRepayItem>
                    <tr>
                        <td>
                            <i <#if expectedInvestRepayItem.birthdayCoupon>class="birth-icon" data-benefit="${expectedInvestRepayItem.birthdayBenefit}"</#if>></i>
                            <a href="/loan/${expectedInvestRepayItem.loan.id?c}" class="month-title">${expectedInvestRepayItem.loan.name!}</a>
                        </td>
                        <td>${(((expectedInvestRepayItem.loan.activityRate+expectedInvestRepayItem.loan.baseRate)*100)?string('0.00'))!}%</td>
                        <td>${(expectedInvestRepayItem.loan.duration?c)!}天</td>
                        <td>第${(expectedInvestRepayItem.period?c)!}期/${(expectedInvestRepayItem.loan.periods?c)!}期</td>
                        <td>${expectedInvestRepayItem.amount!}
                            <#if expectedInvestRepayItem.loan.productType == 'EXPERIENCE'>(体验金收益)</#if>
                        </td>
                        <td>${(expectedInvestRepayItem.repayDate?string('MM月dd日'))!}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="6" class="tc">本月应收本息${(((actualInvestRepay+expectedInvestRepay)/100)?string('0.00'))!}元</td>
            </tr>
            </tfoot>
        </table>
    </div>
    <div class="newProjects bRadiusBox clear-blank bg-w">
        <table class="table">
            <caption>最新投资项目 <a href="/investor/invest-list" class="fr">更多...</a></caption>
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
                            <#if latestInvest.productType != 'EXPERIENCE'>
                            <i <#if latestInvest.birthdayCoupon>class="birth-icon" data-benefit="${latestInvest.birthdayBenefit}"</#if>></i>
                            </#if>

                            <a href="/loan/${latestInvest.loanId?string('0')}" class="trade-detail">${latestInvest.loanName!}</a>
                        </td>
                        <td>投资成功</td>
                        <td><#if latestInvest.status??>${(latestInvest.repayDate?string('yyyy-MM-dd'))!} /
                        ${(((latestInvest.corpus+latestInvest.defaultInterest+latestInvest.expectedInterest-latestInvest.expectedFee)/100)?string('0.00'))!}<#else>-/-</#if>
                        </td>
                        <td>
                            ￥${((latestInvest.investAmount/100)?string('0.00'))!}
                            <#if latestInvest.productType == 'EXPERIENCE'>
                                (体验金)
                            </#if>
                        </td>
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
                <div class="sign-text"></div>
                <div class="sign-content">
                    <p class="sign-point"><span></span>积分</p>
                    <p class="tomorrow-text"></p>
                    <p class="intro-text"></p>
                    <p class="next-text"></p>
                    <p class="sign-reward"><a href="/activity/sign-check">查看连续签到奖励</a></p>
                </div>
            </div>
        </div>
    </div>
</div>
</@global.main>