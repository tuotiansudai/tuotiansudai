<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.investor_invest_list}" pageJavascript="${js.investor_invest_list}" activeNav="我的账户" activeLeftNav="我的投资" title="投资记录">
<div class="content-container invest-list-content">
    <h4 class="column-title">
        <a href="/investor/invest-list"><em class="tc">直投项目</em></a>
        <a href="/investor/invest-transfer-list"><em class="tc normal">转让项目</em></a>
    </h4>

    <div class="item-block date-filter">
        <span class="sub-hd">起止时间:</span>
        <input type="text" id="date-picker" class="input-control" size="35" readonly/>
        <span class="select-item" data-day="1">今天</span>
        <span class="select-item" data-day="7">最近一周</span>
        <span class="select-item" data-day="30">一个月</span>
        <span class="select-item" data-day="180">六个月</span>
        <span class="select-item current" data-day="">全部</span>
    </div>

    <div class="item-block status-filter">
        <span class="sub-hd">交易状态:</span>
        <span class="select-item current" data-status="">全部</span>
        <span class="select-item" data-status="RAISING">正在招募</span>
        <span class="select-item" data-status="RECHECK">招募成功</span>
        <span class="select-item" data-status="REPAYING">正在回款</span>
        <span class="select-item" data-status="COMPLETE">回款完毕</span>
    </div>
    <div class="clear-blank"></div>
    <div class="invest-list" id="investList"></div>
    <script type="text/html" id="investListTpl">
        <table class="invest-list table-striped">
            <thead>
            <tr>
                <th>项目名称</th>
                <th class="tr">我的投资(元)</th>
                <th>交易时间</th>
                <th>交易状态</th>
                <th>下次回款(元)</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            {{if records.length>0}}
            {{each records}}
            <tr>
                <td>
                        <span class="icon-list">


                            {{if $value.productType != 'EXPERIENCE'}}
                                {{if $value.bankPlatForm}}
                                <i class="fudian-icon">富</i>
                                {{else}}
                                <i class="liandong-icon">联</i>
                                {{/if}}
                                {{each $value.userCoupons}}
                                {{if $value.couponType=='BIRTHDAY_COUPON'}}
                                <i class="birth-icon" data-benefit="{{$value.birthdayBenefit}}"></i>
                                {{/if}}
                                {{if $value.couponType=='INTEREST_COUPON'}}
                                <i class="coupon-icon" data-benefit="{{$value.rate * 100}}"></i>
                                {{/if}}
                                {{if $value.couponType=='RED_ENVELOPE'}}
                                <i class="money-icon" data-benefit="{{$value.amount / 100}}"></i>
                                {{/if}}
                                {{if $value.couponType=='NEWBIE_COUPON'}}
                                <i class="newbie-icon" data-benefit="{{$value.amount / 100}}"></i>
                                {{/if}}
                                {{if $value.couponType=='INVEST_COUPON'}}
                                <i class="ticket-icon" data-benefit="{{$value.amount / 100}}"></i>
                                {{/if}}
                                {{/each}}

                                {{if $value.achievement=='FIRST_INVEST'}}
                                <i class="first-icon"></i>
                                {{/if}}
                                {{if $value.achievement=='LAST_INVEST'}}
                                <i class="last-icon"></i>
                                {{/if}}
                                {{if $value.achievement=='MAX_AMOUNT'}}
                                <i class="max-icon"></i>
                                {{/if}}
                                {{if $value.extraRate !== null}}
                                <i class="extra-rate" data-benefit="{{$value.extraRate * 100}}"></i>
                                {{/if}}
                            {{/if}}
                        </span>
                    <a href="/loan/{{$value.loanId}}" class="project-name">{{$value.loanName}}</a>
                </td>
                <td>
                    {{$value.amount}}
                    {{if $value.productType=='EXPERIENCE'}}
                    体验金
                    {{/if}}
                </td>
                <td>{{$value.createdTime}}</td>
                <td>{{$value.status}}</td>
                <td>
                    {{if $value.nextRepayDate}}
                    {{if $value.productType=='EXPERIENCE'}}
                    {{$value.nextRepayAmount}}
                    {{else}}
                    {{$value.nextRepayDate}} / {{$value.nextRepayAmount}}
                    {{/if}}
                    {{else}}
                    --
                    {{/if}}
                </td>
                <td>
                    {{if $value.investRepayExist}}
                    <a class="show-invest-repay" data-url="/investor/invest/{{$value.investId}}/repay-data">回款详情</a>
                    {{if $value.productType!='EXPERIENCE'}}
                    {{if $value.contractNo == 'OLD'}}
                    <a class="red" href="/contract/investor/loanId/{{$value.loanId}}/investId/{{$value.investId}}"
                       target="_blank"> | 合同</a>
                    {{else if $value.contractNo != '' && $value.contractNo != null }}
                    <a class="red" href="/contract/invest/contractNo/{{$value.contractNo}}" target="_blank"> | 合同</a>
                    {{/if}}
                    {{/if}}
                    {{else}}
                    --
                    {{/if}}
                </td>
            </tr>
            {{/each}}
            {{else}}
            <td colspan="6" class="no-data">暂时没有投资记录</td>
            {{/if}}
            </tbody>
        </table>
    </script>

    <div class="pagination" data-url="/investor/invest-list-data">
    </div>
</div>

<script type="text/template" id="investRepayTemplate">
    <#--underscore template-->
    <div class="layer-content">
        <div class="summary-top clearfix">
            <span>已收回款总额 : <em><%=sumActualInterest%></em>元</span>
            <span>待收回款总额 : <em><%=sumExpectedInterest%></em>元</span>
            <span>投资红包奖励 : <em><%=redInterest%></em>元</span>
        </div>
        <table class="table table-repay">
            <thead>
            <tr>
                <th class="tr">到期回款日</th>
                <th class="tr">应收回款(元)</th>
                <th class="tr">应收本金(元)</th>
                <th class="tr">应收收益(元)</th>
                <th class="tr">应收奖励(元)
                    <%=couponMessage?'<i class="fa fa-question-circle text-b coupon" style="color:#ff7a2a;"
                                         data-benefit="'+couponMessage+'"></i>':''%>
                </th>
                <th class="tr">应缴服务费(元)
                    <%=levelMessage?'<i class="fa fa-question-circle text-b fee" data-benefit="'+levelMessage+'"></i>':''%>

                </th>
                <th class="tr spec-bg">已收回款(元)</th>
                <th class="tr spec-bg">回款时间</th>
                <th class="spec-bg">状态</th>
            </tr>
            <tbody>
            <% for(var i = 0; i < records.length; i++) {
            var item = records[i];
            %>
            <tr>
                <td><%=item.repayDate%></td>
                <td class="tr"><%=item.amount%></td>
                <td class="tr"><%=item.corpus%></td>
                <td class="tr"><%=item.expectedInterest%></td>
                <td class="tr">
                    <%=item.couponExpectedInterest?item.couponExpectedInterest:'--'%>
                </td>
                <td class="tr">
                    <%=item.expectedFee?'-'+item.expectedFee:'--'%>
                </td>
                <td class="tr spec-bg">
                    <%=item.actualAmount ? item.actualAmount : '--'%>
                    <%=(item.actualAmount && item.defaultInterest)?'<i class="fa fa-question-circle text-b repay"
                                                                       data-benefit="逾期'+item.overdueDay+'天，已收违约金'+item.defaultInterest+'元"></i>':''
                    %>

                </td>
                <td class="tr spec-bg">
                    <%=item.actualRepayDate?item.actualRepayDate:'--'%>
                </td>
                <td class="spec-bg">
                    <%=item.status%>
                </td>
            </tr>
            <% } %>
            </tbody>
            </thead>
        </table>
        <p class="bottom-note" style="float: left">应收回款=应收本金+应收收益+应收奖励-应缴服务费</p>
        <p class="bottom-note" style="color: #ff7200;float: right">备注：此投资项目回款将发放至联动优势资金托管账号</p>
    </div>
</script>

<script type="text/template" id="investExperienceRepayTemplate">
    <div class="layer-content">

        <div class="summary-top clearfix">
            <span>已收体验金收益 : <em><%=sumActualInterest%></em>元</span>
            <span>待收体验金收益 : <em><%=sumExpectedInterest%></em>元</span>
        </div>
        <table class="table table-repay">
            <thead>
            <tr>
                <th class="tr">到期回款日</th>
                <th class="tr">应收体验金收益</th>
                <th class="tr">投资金额(体验金)</th>
                <th class="tr spec-bg">已收体验金收益</th>
                <th class="tr spec-bg">回款时间</th>
                <th class="spec-bg">状态</th>
            </tr>
            <tbody>
            <% for(var i = 0; i < records.length; i++) {
            var item = records[i];
            %>
            <tr>
                <td><%=item.repayDate%></td>
                <td class="tr"><%=item.expectedInterest%></td>
                <td class="tr"><%=item.investExperienceAmount%></td>
                <td class="tr spec-bg">
                    <%=item.actualInterest ? item.actualInterest : '--'%>
                </td>
                <td class="tr spec-bg">
                    <%=item.actualRepayDate ? item.actualRepayDate : '--'%>
                </td>
                <td class="spec-bg">
                    <%=item.status%>
                </td>
            </tr>
            <% } %>

            </tbody>
            </thead>
        </table>
        <p class="bottom-note">温馨提示：投资体验项目的收益，用户需投资直投项目累计满1000元即可提现（投资债权转让项目除外）；</p>

    </div>
</script>

</@global.main>