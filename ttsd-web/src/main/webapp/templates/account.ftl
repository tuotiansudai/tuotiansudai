<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.account_overview}" pageJavascript="${js.account_overview}" activeNav="我的账户" activeLeftNav="账户总览" title="账户总览">
<script type="text/javascript">
    var pydata = {
        balance: '${((balance/100)?string('0.00'))!}',
        expectedTotalCorpus: '${((expectedTotalCorpus/100)?string('0.00'))!}',
        expectedTotalInterest: '${((expectedTotalInterest/100)?string('0.00'))!}'
    };
</script>

<div class="content-container account-overview" id="accountOverview" data-bankcard="${hasBankCard?c}"
     data-account="${hasAccount?c}">

    <div class="column-box profile-box bg-w clearfix">
        <span class="fl account-profile"></span>
        <div class="welcome-text">
            <em class="fl tip-hello">您好：${mobile!}</em><em class="fl user-role-font">
            <@global.role hasRole="'INVESTOR'">您当前的身份为<i>出借人</i></@global.role>
            <@global.role hasRole="'LOANER'">您当前的身份为<i>借款人</i></@global.role></em>
            <em class="fr">可用积分:<span id="MyAvailablePoint">${myPoint?string.computer}</span></em>
        </div>
        <div class="sign-list fl">
            <span class="vip vip${userMembershipLevel!}"></span>
            <a href="/personal-info" class="user-info"></a>
        </div>
        <@global.role hasRole="'UMP_INVESTOR'">
            <div class="fr">
                <div class="safety-notification"><a href="/ump/account" target="_blank" class="tip-title">联动优势资金账号</a><i id="noticeBtn"
                                                                                                         class="fa fa-question-circle"
                                                                                                         aria-hidden="true"></i>
                    <div class="notice-tips extra-rate-popup" style="display: none">
                        1.应国家相关监管要求，拓天速贷已开通富滇银行存管服务;<br/>
                        2.当前内容为富滇银行存管账号的资金信息;<br/>
                        3.想要查看老版的资金信息请点击<strong>「联动优势资金账号」</strong>;<br/>
                        4.联动优势存管账号余额和待收回款的资金不能再次投资，需要您提现至银行卡，然后充值到富滇银行存管账号后，才能参加新的投资项目。
                    </div>
                </div>
            </div>
        </@global.role>
    </div>

    <div class="column-box bg-w clearfix amount-sum">
        <h3><b>账户总额：</b><span>${(((balance+expectedTotalCorpus+expectedTotalInterest)/100)?string('0.00'))!}元</span>
            <ul class="proList fr">
                <li class="fr"><a class="btn-normal" href="/recharge">充值</a></li>
                <li class="fr"><a id="cashMoneyBtn" class="btn-primary hoverNo" href="javascript:;">提现</a></li>
            </ul>
        </h3>
    </div>

    <div class="column-box bg-w clearfix amount-sum ">
        <h3><b>可用余额：</b><span>${((balance/100)?string('0.00'))!}元</span> <i class="icon-has-con"></i></h3>
    </div>

    <@global.role hasRole="'INVESTOR'">
        <div class="column-box bg-w clearfix amount-sum ">
            <h3><b>累计收益：</b><span>${(((totalIncome)/100)?string('0.00'))!}</span>元 <i class="icon-has-con"></i></h3>
            <ul class="detail-list">
                <li>已收投资收益：<span>${((actualTotalInterest)/100)?string('0.00')!}</span>元</li>
                <li>已收投资奖励：<span>${((actualTotalExtraInterest)/100)?string('0.00')!}</span>元</li>
                <li>已收推荐奖励：<span>0.00</span>元</li>
                <li>已收优惠券奖励：<span>0.00</span>元</li>
                <li>已收体验金奖励：<span>0.00</span>元</li>
            </ul>
        </div>

        <div class="column-box bg-w clearfix amount-sum ">
            <h3>
                <b>待收回款：</b><span>${(((expectedTotalCorpus+expectedTotalInterest+expectedTotalExtraInterest)/100)?string('0.00'))!}</span>元
                <i class="icon-has-con"></i></h3>
            <ul class="detail-list">
                <li>待收投资本金：<span>${((expectedTotalCorpus/100)?string('0.00'))!}</span>元</li>
                <li>待收预期收益：<span>${((expectedTotalInterest/100)?string('0.00'))!}</span>元</li>
                <li>待收投资奖励：<span>${((expectedTotalExtraInterest/100)?string('0.00'))!}</span>元</li>
                <li>待收优惠券奖励：<span>0.00</span>元</li>
                <li>待收体验金收益：<span>0.00</span>元</li>
            </ul>
        </div>
    </@global.role>

    <div class="column-box bg-w clearfix amount-sum">
        <h3><b>可用体验金余额：</b><span>${((experienceBalance/100)?string('0.00'))!}</span>元</span>
        </h3>
    </div>
    <@global.role hasRole="'LOANER'">
        <#if expectedRepayAmountOfMonth??>
            <div class="last-month  bg-w">
                <div class="payment-switch">
                    <em class="current">本月未还款</em>
                    <span class="total current fr">本月未还款总额：￥${((expectedRepayAmountOfMonth/100)?string('0.00'))!}元</span>
                </div>
                <table class="table">
                    <thead>
                    <tr>
                        <th>项目名称</th>
                        <th>约定年化利率</th>
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
                                <td><a <#if repay.loan.id ==1>href="/loan/${repay.loan.id?string('0')}" class="no-pointer" </#if> >${repay.loan.name!}</a></td>
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
                    <tfoot>
                    <tr>
                        <td colspan="6" class="more tr"><a href="/loaner/loan-list" class="fr"> 查看更多>> </a></td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </#if>
    </@global.role>
    <@global.role hasRole="'INVESTOR'">
        <div class="clear-blank bg-w" id="tMonthBox">
            <div class="payment-switch">
                <em>本月已收回款</em>
                <em class="current">本月待收回款</em>

                <span class="total fr">本月已收回款总额：￥${((actualInvestRepay/100)?string('0.00'))!}元</span>
                <span class="total current fr">本月待收回款总额：￥${((expectedInvestRepay/100)?string('0.00'))!}元</span>
            </div>

            <table class="table" style="display: none">
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>约定年化利率</th>
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
                                <i <#if actualInvestRepayItem.birthdayCoupon>class="birth-icon"
                                   data-benefit="${actualInvestRepayItem.birthdayBenefit}"</#if>></i>
                                <a <#if actualInvestRepayItem.loan.id !=1>href="/loan/${actualInvestRepayItem.loan.id?c}"</#if>
                                   class="month-title <#if actualInvestRepayItem.loan.id ==1>no-pointer</#if>">${actualInvestRepayItem.loan.name!}</a>
                            </td>
                            <td>${(((actualInvestRepayItem.loan.activityRate+actualInvestRepayItem.loan.baseRate)*100)?string('0.00'))!}
                                %
                            </td>
                            <td>${(actualInvestRepayItem.loan.duration?c)!}天</td>
                            <td>第${(actualInvestRepayItem.period?c)!}期/${(actualInvestRepayItem.loan.periods?c)!}期</td>
                            <td>${actualInvestRepayItem.actualAmount!}</td>
                            <td>${(actualInvestRepayItem.actualRepayDate?string('MM月dd日'))!}</td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6" class="tc">
                        本月应收本息${(((actualInvestRepay+expectedInvestRepay)/100)?string('0.00'))!}元
                    </td>
                </tr>
                <tr>
                    <td colspan="6" class="tr last"><a href="/investor/invest-list" class="fr more">查看更多>></a></td>
                </tr>
                </tfoot>
            </table>

            <table class="table ">
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>约定年化利率</th>
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
                                <i <#if expectedInvestRepayItem.birthdayCoupon>class="birth-icon"
                                   data-benefit="${expectedInvestRepayItem.birthdayBenefit}"</#if>></i>
                                <a <#if expectedInvestRepayItem.loan.id !=1> href="/loan/${expectedInvestRepayItem.loan.id?c}"</#if>
                                   class="month-title <#if expectedInvestRepayItem.loan.id ==1>no-pointer</#if>">${expectedInvestRepayItem.loan.name!}</a>
                            </td>
                            <td>${(((expectedInvestRepayItem.loan.activityRate+expectedInvestRepayItem.loan.baseRate)*100)?string('0.00'))!}
                                %
                            </td>
                            <td>${(expectedInvestRepayItem.loan.duration?c)!}天</td>
                            <td>第${(expectedInvestRepayItem.period?c)!}期/${(expectedInvestRepayItem.loan.periods?c)!}期
                            </td>
                            <td>${expectedInvestRepayItem.amount!}</td>
                            <td>${(expectedInvestRepayItem.repayDate?string('MM月dd日'))!}</td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6" class="tc">
                        本月应收本息${(((actualInvestRepay+expectedInvestRepay)/100)?string('0.00'))!}元
                    </td>
                </tr>
                <tr>
                    <td colspan="6" class="tr last"><a href="/investor/invest-list" class="fr more">查看更多>></a></td>
                </tr>
                </tfoot>
            </table>
        </div>
        <div class="new-projects bg-w">
            <div class="payment-switch">
                <em class="current">最新投资项目</em>
            </div>
            <table class="table">

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
                                    <i <#if latestInvest.birthdayCoupon>class="birth-icon"
                                       data-benefit="${latestInvest.birthdayBenefit}"</#if>></i>
                                </#if>

                                <a <#if latestInvest.loanId !=1> href="/loan/${latestInvest.loanId?string('0')}"</#if>
                                   class="trade-detail <#if latestInvest.loanId ==1>no-pointer</#if>">${latestInvest.loanName!}</a>
                            </td>
                            <td>投资成功</td>
                            <td><#if latestInvest.status??>${(latestInvest.repayDate?string('yyyy-MM-dd'))!} /
                            ${(((latestInvest.corpus+latestInvest.defaultInterest+latestInvest.expectedInterest-latestInvest.expectedFee)/100)?string('0.00'))!}<#else>
                                -/-</#if>
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
                <tfoot>
                <tr>
                    <td colspan="6" class="tr last"><a href="/investor/invest-list" class="fr more">查看更多>></a></td>
                </tr>

                </tfoot>
            </table>
        </div>
    </@global.role>

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

<div id="bankCardDOM" class="pad-m popLayer" style="display: none; padding-top:20px;padding-bottom: 0">

    <div class="tc text-m">您还没绑卡，请先进行绑卡</div>
<#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
    <form action="${requestContext.getContextPath()}/bank-card/bind/source/WEB" method="post" style="display: block">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="tc person-info-btn" style="margin-top:40px;">
            <button class="btn  btn-cancel btn-close" type="button">取消</button>&nbsp;&nbsp;&nbsp;
            <input id="accountBtn" class="btn btn-success" value="确定" type="submit"/>
        </div>
    </form>

</div>
</@global.main>