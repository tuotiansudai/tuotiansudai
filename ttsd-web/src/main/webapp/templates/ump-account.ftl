<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.ump_account}" pageJavascript="${js.ump_account}" title="联动优势账户">
<div class="auto-height personal-info" id="personInfoBox">
    <div class="ump-nav">
        <div class="ump-tip">
            联动优势资金托管账号
        </div>

    </div>
    <div class="content-container">
        <h4 class="column-title"><em class="tc">个人资料</em>
        </h4>
        <ul class="info-list">
            <li><span class="info-title"> 实名认证</span>
                <em class="info">${userName}</em>
                <span class="binding-set"><i class="fa fa-check-circle ok"></i>已认证</span>

            </li>
            <li><span class="info-title"> 绑定银行卡</span>
                <#if bankCard??>
                    <em class="info">${bankCard?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</em>
                    <span class="binding-set">
                    <i class="fa fa-check-circle ok"></i>已绑定<a class="setlink setBankCard" href="javascript:void(0)"
                                                               id="update-bank-card"
                                                               data-url="${requestContext.getContextPath()}/ump/bind-card/replace">修改</a>
                </span>
                <#else>
                    <em class="info">绑定银行卡后，您可以进行快捷支付和提现操作</em>
                    <span class="binding-set">
                    <i class="fa fa-times-circle no"></i>未绑定<a class="setlink setBankCard" href="/ump/bind-card">绑定</a>
                </span>
                </#if>
            </li>

            <li><span class="info-title"> 支付密码</span>
                <em class="info">********</em>
                <span class="binding-set">
            </span>
            </li>
        </ul>
    </div>
    <div class="content-container">
        <h4 class="column-title"><em class="tc">账户总览</em></h4>
        <div class="account-overview">

            <div class="column-box bg-w clearfix amount-sum">
                <h3><b>账户总额：</b><span>${(((balance+expectedTotalCorpus+expectedTotalInterest)/100)?string('0.00'))!}元</span>
                    <ul class="proList fr">
                        <@global.role hasRole="'UMP_LOANER'">
                            <li class="fr"><a class="btn-normal" href="/ump/recharge">充值</a></li>
                        </@global.role>
                        <li class="fr"><a class="btn-primary" href="/ump/withdraw">提现</a></li>
                    </ul>
                </h3>
            </div>

            <div class="column-box bg-w clearfix amount-sum ">
                <h3><b>可用余额：</b><span>${((balance/100)?string('0.00'))!}元</span> <i class="icon-has-con"></i></h3>

                <ul class="detail-list">
                    <li>提现冻结中：<span>${((withdrawFrozeAmount/100)?string('0.00'))!}</span>元</li>
                    <li>投资冻结中：<span>${((investFrozeAmount/100)?string('0.00'))!}</span>元</li>
                </ul>
            </div>

            <div class="column-box bg-w clearfix amount-sum ">
                <h3><b>累计收益：</b><span>${(((totalIncome)/100)?string('0.00'))!}</span>元  <i class="icon-has-con"></i></h3>
                <ul class="detail-list">
                    <li>已收投资收益：<span>${((actualTotalInterest)/100)?string('0.00')!}</span>元</li>
                    <li>已收投资奖励：<span>${((actualTotalExtraInterest)/100)?string('0.00')!}</span>元</li>
                    <li>已收推荐奖励：<span>${((referRewardAmount/100)?string('0.00'))!}</span>元</li>
                    <li>已收优惠券奖励：<span>${((actualCouponInterest/100)?string('0.00'))!}</span>元</li>
                    <li>已收体验金奖励：<span>${((actualExperienceInterest/100)?string('0.00'))!}</span>元</li>
                </ul>
            </div>

            <div class="column-box bg-w clearfix amount-sum ">
                <h3> <b>待收回款：</b><span>${(((expectedTotalCorpus+expectedTotalInterest+expectedTotalExtraInterest+expectedExperienceInterest+expectedCouponInterest)/100)?string('0.00'))!}</span>元 <i class="icon-has-con"></i></h3>
                <ul class="detail-list">
                    <li>待收投资本金：<span>${((expectedTotalCorpus/100)?string('0.00'))!}</span>元</li>
                    <li>待收预期收益：<span>${((expectedTotalInterest/100)?string('0.00'))!}</span>元</li>
                    <li>待收投资奖励：<span>${((expectedTotalExtraInterest/100)?string('0.00'))!}</span>元</li>
                    <li>待收优惠券奖励：<span>${((expectedCouponInterest/100)?string('0.00'))!}</span>元</li>
                    <li>待收体验金收益：<span>${((expectedExperienceInterest/100)?string('0.00'))!}</span>元</li>
                </ul>
            </div>
        </div>
    </div>
    <div class="content-container">
        <h4 class="column-title"><em>资金管理</em></h4>
        <div class="money-box recharge-cash">

        <p><span class="icon-small icon-recharge"></span><span>累计充值(元)：</span><span class="money">${rechargeAmount} </span></p>
        <p><span class="icon-small icon-cash"></span><span>累计提现(元)：<span><span class="money">${withdrawAmount} </span></p>

        </div>

        <div class="clear-blank-m"></div>
        <div class="item-block date-filter ">
            <span class="sub-hd">起止时间:</span>
            <input type="text" id="date-picker" class="input-control" size="35" readonly/>
            <span class="select-item current" data-day="1">今天</span>
            <span class="select-item" data-day="7">最近一周</span>
            <span class="select-item" data-day="30">一个月</span>
            <span class="select-item" data-day="180">六个月</span>
            <span class="select-item" data-day="">全部</span>
        </div>

        <div class="item-block status-filter">
            <span class="sub-hd">交易状态:</span>
            <span class="select-item current" data-status="">全部</span>
            <span class="select-item" data-status="WITHDRAW_SUCCESS,WITHDRAW_FAIL,APPLY_WITHDRAW">提现</span>
            <span class="select-item" data-status="RECHARGE_SUCCESS">充值</span>
            <span class="select-item" data-status="ACTIVITY_REWARD,REFERRER_REWARD">奖励</span>
            <span class="select-item" data-status="NORMAL_REPAY,ADVANCE_REPAY">本息</span>
            <span class="select-item" data-status="INVEST_SUCCESS">投标</span>
            <span class="select-item"
                  data-status="NEWBIE_COUPON,INVEST_COUPON,INTEREST_COUPON,RED_ENVELOPE,BIRTHDAY_COUPON">宝藏</span>
            <span class="select-item" data-status="INVEST_CASH_BACK">现金补贴</span>
        </div>

        <div class="clear-blank"></div>
        <table class="user-bill-list table-striped"></table>
        <div class="pagination" data-url="/ump/account/user-bill-list-data" data-page-size="10"></div>
    </div>
    <div class="kindly-tip">
        <p class="title">温馨提示：</p>
        <p>1.应国家相关监管要求，拓天速贷已开通富滇银行存管服务。</p>
        <p>2.联动优势托管账号余额和待收回款的资金不能再次投资，需要您提现至银行卡，然后充值到富滇银行存管账号后，才能参加新的投资项目。</p>
    </div>
</div>

<div id="resetUmpayPassDOM" class="pad-m popLayer" style="display: none;">
    <form name="resetUmpayPasswordForm" id="resetUmpayPasswordForm">
        <dl class="identityCodeTitle" align="center">
            通过身份证号重置支付密码
        </dl>
        <dl>
            <dt class="requireOpt">请输入您的身份证号</dt>
            <dd><input type="text" id="identityNumber" name="identityNumber" class="input-control">
            </dd>
        </dl>
        <div class="error-box tc"></div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-normal">确认重置</button>
    </form>
</div>

<div id="successUmpayPass" class="pad-m popLayer" style="display: none;">
    <dl>
        <dt>您的支付密码已被重置，请注意查收相关短信，查看新密码！</dt>
    </dl>
    <button type="button" class="btn btn-normal" id="readUmpayPass">我已查看</button>
</div>

<script type="text/template" id="userBillTableTemplate">
    <table class="user-bill-list">
        <thead>
        <tr>
            <th>交易时间</th>
            <th>交易类型</th>
            <th class="tr">收入(元)</th>
            <th class="tr">支出(元)</th>
            <th class="tr">冻结(元)</th>
            <th class="tr">可用余额(元)</th>
            <th>备注</th>
        </tr>
        </thead>
        <tbody>
        <% for(var i = 0; i < records.length; i++) {
        var item = records[i];
        %>
        <tr>
            <td><%=item.createdTime%></td>
            <td><%=item.businessType%></td>
            <td class="tr">+<%=item.income%></td>
            <td class="tr">-<%=item.cost%></td>
            <td class="tr"><%=item.freeze%></td>
            <td class="tr"><%=item.balance%></td>
            <td>编号:<%=item.id%></td>
        </tr>
        <% } %>
        <%=records.length?'':'<tr><td colspan="7" class="no-data">暂时没有投资记录</td></tr>'%>
        </tbody>
    </table>
</script>


</@global.main>


