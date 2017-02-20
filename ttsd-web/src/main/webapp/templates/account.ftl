<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.account_overview}" activeNav="我的账户" activeLeftNav="账户总览" title="账户总览">
<script type="text/javascript">
    var pydata = {
        balance: '${((balance/100)?string('0.00'))!}',
        collectingPrincipal: '${((collectingPrincipal/100)?string('0.00'))!}',
        collectingInterest: '${((collectingInterest/100)?string('0.00'))!}'
    };
</script>
<div class="content-container account-overview">
    <div class="bRadiusBox spad bg-w clearfix">
        <img src="${staticServer}/images/sign/profile_20161011.jpg" class="fl accountImg">
        <div class="profile-box">
            <span><em>您好：${mobile!}</em></span>
            <span class="vip vip${userMembershipLevel!}"></span>
            <a href="/personal-info" class="user-info"></a>
            <ul class="proList">
                <#if signedIn?? && signedIn>
                    <li class="fl sign-top no-click"><span class="btn-sign finish-sign" data-url="/point-shop/sign-in" id="signBtn">已签到</span></li>
                <#else >
                    <li class="fl sign-top"><span class="btn-sign will-sign" data-url="/point-shop/sign-in" id="signBtn">签到</span></li>
                </#if>
                <li class="fl beans-number">可用积分:<span id="MyAvailablePoint">${myPoint?string.computer}</span></li>
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
                <li><b>累计收益：</b><span>${(((collectedReward+collectedInterest+collectedBirthdayAndInterest+collectedRedEnvelopeInterest)/100)?string('0.00'))!}</span>元</li>
                <li><b>冻结金额：</b><span>${((freeze/100)?string('0.00'))!}</span>元</li>
                <li><b>已收投资收益：</b><span>${((collectedInterest+collectedBirthdayAndInterest)/100)?string('0.00')!}</span>元</li>
                <li><b>待收投资本金：</b><span>${((collectingPrincipal/100)?string('0.00'))!}</span>元</li>
                <li><b>已收推荐奖励：</b><span>${((collectedReward/100)?string('0.00'))!}</span>元</li>
                <li><b>待收预期收益：</b><span>${((collectingInterest/100)?string('0.00'))!}</span>元</li>
                <li><b>已收红包奖励：</b><span>${((collectedRedEnvelopeInterest/100)?string('0.00'))!}</span>元</li>
            </ul>
        </div>
    </div>
    <div class="new-user-award">
        <span>HOT</span>
        <#if isUsableCouponExist>
            您还有优惠券没有使用，<a href="/my-treasure" title="快来使用吧">快来使用吧</a>
        <#else>
            新手注册就送5888体验金，体验再得588红包+3%加息券，<a href="/activity/landing-page" title="详情请点击">详情请点击</a>
        </#if>
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
            <caption>本月已收回款总额：￥${((successSumInvestRepay/100)?string('0.00'))!}元 <a href="/investor/invest-list" class="fr">更多...</a></caption>
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
                <#if successSumInvestRepayList??>
                    <#list successSumInvestRepayList as successSumInvestRepay>
                    <tr>
                        <td>
                            <i <#if successSumInvestRepay.birthdayCoupon>class="birth-icon" data-benefit="${successSumInvestRepay.birthdayBenefit}"</#if>></i>
                            <a href="/loan/${successSumInvestRepay.loan.id?string('0')}" class="month-title">${successSumInvestRepay.loan.name!}</a>
                        </td>
                        <td>${(((successSumInvestRepay.loan.activityRate+successSumInvestRepay.loan.baseRate)*100)?string('0.00'))!}%</td>
                        <td>${(successSumInvestRepay.loan.duration?string('0'))!}天</td>
                        <td>第${(successSumInvestRepay.period?string('0'))!}期/${(successSumInvestRepay.loan.periods?string('0'))!}期</td>
                        <td>${successSumInvestRepay.actualAmount!}
                            <#if successSumInvestRepay.loan.productType == 'EXPERIENCE'>
                                (现金红包)
                            </#if>
                        </td>
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
            <caption>本月待收回款总额：￥${((notSuccessSumInvestRepay/100)?string('0.00'))!}元<a href="/investor/invest-list" class="fr">更多...</a></caption>
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
                <#if notSuccessSumInvestRepayList??>
                    <#list notSuccessSumInvestRepayList as notSuccessSumInvestRepay>
                    <tr>
                        <td>
                            <i <#if notSuccessSumInvestRepay.birthdayCoupon>class="birth-icon" data-benefit="${notSuccessSumInvestRepay.birthdayBenefit}"</#if>></i>
                            <a href="/loan/${notSuccessSumInvestRepay.loan.id?string('0')}" class="month-title">${notSuccessSumInvestRepay.loan.name!}</a>
                        </td>
                        <td>${(((notSuccessSumInvestRepay.loan.activityRate+notSuccessSumInvestRepay.loan.baseRate)*100)?string('0.00'))!}%</td>
                        <td>${(notSuccessSumInvestRepay.loan.duration?string('0'))!}天</td>
                        <td>第${(notSuccessSumInvestRepay.period?string('0'))!}期/${(notSuccessSumInvestRepay.loan.periods?string('0'))!}期</td>
                        <td>${notSuccessSumInvestRepay.amount!}
                            <#if notSuccessSumInvestRepay.loan.productType == 'EXPERIENCE'>
                                (现金红包)
                            </#if>
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