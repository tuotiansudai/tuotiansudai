<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_transfer_list}" pageJavascript="${js.my_transfer_list}" activeNav="我的账户" activeLeftNav="我的投资" title="投资记录">
<div class="content-container invest-list-content">
    <h4 class="column-title">
        <a href="/investor/invest-list"><em class="tc normal">直投项目</em></a>
        <a href="/investor/invest-transfer-list"><em class="tc">转让项目</em></a>
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
        <span class="select-item" data-status="REPAYING">正在回款</span>
        <span class="select-item" data-status="COMPLETE">回款完毕</span>
    </div>
    <div class="clear-blank"></div>
    <div class="invest-list">
        
    </div>
    <div class="pagination" data-url="/investor/invest-transfer-list-data" data-page-size="10">
    </div>
</div>
<div id="elementInvestRepay" style="display: none"></div>
<script type="text/template" id="investRepayTemplate">
    <#--underscore template-->
    <div class="layer-content">
        <div class="summary-top clearfix">
            <span>已收回款总额 : <em><%=sumActualInterest%></em>元</span>
            <span>待收回款总额 : <em><%=sumExpectedInterest%></em>元</span>
            <span>出借红包奖励 : <em><%=redInterest%></em>元</span>
        </div>
        <table class="table table-repay">
            <thead>
            <tr>
                <th class="tr">到期回款日</th>
                <th class="tr">应收回款(元)</th>
                <th class="tr">应收本金(元)</th>
                <th class="tr">应收收益(元)</th>
                <th class="tr">应收奖励(元)
                    <%=couponMessage?'<i class="fa fa-question-circle text-b coupon" style="color:#ff7a2a;" data-benefit="'+couponMessage+'"></i>':''%>
                </th>
                <th class="tr">应缴服务费(元)
                    <%=levelMessage?'<i class="fa fa-question-circle text-b fee" data-benefit="'+levelMessage+'"></i>':''%>

                </th>
                <th class="tr spec-bg">已收回款(元)</th>
                <th class="tr spec-bg">回款时间(元)</th>
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
                    <%=(item.actualAmount && item.defaultInterest)?'<i class="fa fa-question-circle text-b repay" data-benefit="逾期'+overdueDay+'天，已收违约金'+defaultInterest+'元"></i>':'' %>
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
        <p class="bottom-note">应收回款=应收本金+应收收益+应收奖励-应缴服务费</p>
    </div>
</script>

<script type="text/template" id="investListTemplate">
    <table class="invest-list table-striped">
        <thead>
        <tr>
            <th>转让项目名称</th>
            <th>转让价格(元)</th>
            <th>项目本金(元)</th>
            <th>承接时间</th>
            <th>下次回款(元)</th>
            <th>详情</th>
        </tr>
        </thead>
        <tbody>
        <% for(var i = 0; i < records.length; i++) {
        var item = records[i];
        %>
        <tr>
            <td>
                <a href="/transfer/<%=item.id%>" class="project-name"><%=item.loanName%> </a>
            </td>
            <td><%=item.transferAmountStr%></td>
            <td><%=item.investAmountStr%></td>
            <td><%=item.transferTime%></td>
            <td>
                <%=item.nextRepayAmountStr ? (item.nextRepayAmountStr):'--'%>
            </td>
            <td>
                <a class="show-invest-repay" data-url="/investor/invest/<%=item.investId%>/repay-data">回款详情</a>
                <%=item.contractOK ?'<a class="red" href="/contract/invest/contractNo/'+item.contractNo+'" target="_blank"> | 合同</a>':'' %>
                <%=item.contractOld ? '<a class="red" href="/contract/transfer/transferApplicationId/'+item.id+'" target="_blank"> | 合同</a>' : '' %>
            </td>
        </tr>
        <% } %>
        <%=records.length?'':'<td colspan="7" class="no-data">暂时没有债权转让出借记录</td>'%>
        </tbody>
    </table>
</script>

</@global.main>