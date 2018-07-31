<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.loaner_loan_list}" pageJavascript="${js.loaner_loan_list}" activeNav="我的账户" activeLeftNav="我的借款" title="借款记录">
<div class="content-container loan-list-content">
    <h4 class="column-title">
        <em class="tc">借款记录</em>
    </h4>

    <div class="item-block date-filter">
        <span class="">起止时间:</span>
        <input type="text" id="date-picker" class="start-time filter input-control" size="35" readonly/>
        <span class="select-item current" data-day="1">今天</span>
        <span class="select-item" data-day="7">最近一周</span>
        <span class="select-item" data-day="30">一个月</span>
        <span class="select-item" data-day="180">六个月</span>
        <span class="select-item" data-day="">全部</span>
    </div>
    <div class="item-block status-filter">
        <span class="sub-hd">交易状态:</span>
        <span class="select-item current" data-status="REPAYING">还款中</span>
        <span class="select-item" data-status="COMPLETE">已结清</span>
        <span class="select-item" data-status="CANCEL">流标</span>
    </div>
    <div class="clear-blank"></div>
    <table class="loan-list table-striped">
    </table>
    <div class="pagination" data-url="/loaner/loan-list-data" data-page-size="10">
    </div>
</div>

<script type="text/template" id="loanListTemplate">
    <table class="loan-list table-striped">
        <#--isRepaying 状态-->
    <% if(isRepaying) {  %>
        <thead>
        <tr>
            <th>项目名称</th>
            <th>放款时间</th>
            <th class="tr">借款金额(元)</th>
            <th class="tr">待还总额(元)</th>
            <th>下次还款</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <% for(var i=0,len=records.length; i < len; i++) {
            var item=records[i]
        %>
        <tr>
            <td><%=item.loanName%></td>
            <td><%=item.recheckTime%></td>
            <td class="tr"><%=item.loanAmount%> 元</td>
            <td class="tr"><%=item.unpaidAmount%> 元</td>
            <td>
                <%=item.nextRepayDate?item.nextRepayDate:'--'%>
            </td>
            <td><a class="show-loan-repay" data-url='/loaner/loan/<%=item.loanId%>/repay-data' data-isbank="<%=item.bankPlatForm%>">还款计划</a> |
                <a href="https://www.anxinsign.com/" target="_blank">合同</a>
            </td>
        </tr>
        <% } %>
        <%=records.length?'':'<tr><td colspan="6" class="no-data">暂时没有记录</td></tr>'%>
        </tbody>
        <% } %>
            <#--isComplete 状态-->
        <% if(isComplete) {  %>
        <thead>
        <tr>
            <th>项目名称</th>
            <th>放款时间</th>
            <th>应还总额</th>
            <th>实还总额</th>
            <th>结清时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <% for(var i=0,len=records.length; i < len; i++) {
        var item=records[i]
        %>
        <tr>
            <td><%=item.loanName%></td>
            <td><%=item.recheckTime%></td>
            <td><%=item.expectedRepayAmount%> 元</td>
            <td><%=item.actualRepayAmount%> 元</td>
            <td><%=item.completedDate%></td>
            <td>
                <a class="show-loan-repay" data-url='/loaner/loan/<%=item.loanId%>/repay-data'>还款计划</a> |
                <a href="https://www.anxinsign.com/" target="_blank">合同</a>
            </td>
        </tr>
        <% } %>

        <%=records.length?'':'<tr><td colspan="6" class="no-data">暂时没有记录</td></tr>'%>
        </tbody>
        <% } %>

        <#--isCancel 状态-->
        <% if(isCancel) {  %>
        <thead>
        <tr>
            <th>项目名称</th>
            <th>借款金额</th>
            <th>流标时间</th>
        </tr>
        </thead>
        <tbody>

        <% for(var i=0,len=records.length; i < len; i++) {
        var item=records[i]
        %>
        <tr>
            <td><%=item.loanName%></td>
            <td><%=item.loanAmount%>元</td>
            <td><%=item.recheckTime%></td>
        </tr>
        <% } %>
        <%=records.length?'':'<tr><td colspan="3" class="no-data">暂时没有记录</td></tr>'%>
        </tbody>
         <% } %>
    </table>
</script>

<script type="text/template" id="loanRepayTemplate">

    <div class="layer-content">
        <div class="pad-s">
            <% if(isAdvanceRepayEnabled) { %>

            <a class="advanced-repay btn btn-normal" href="javascript:">提前还款</a>
            <span class="repay-total">(还款总额：<%=advanceRepayAmount%> 元) </span>
            <form id="advanced-repay-form" action="/repay" method="post" target="_blank">
                <input type="hidden" name="loanId" value="<%=loanId%>"/>
                <input type="hidden" name="isAdvanced" value="true"/>
                <input type="hidden" name="_csrf" value="<%=csrfToken%>"/>
            </form>
            <span class="repay-alert" style="display: none"></span>
            <% } %>
        </div>

        <div class="table-list-box">
            <table class="table-list table">
                <thead>
                <tr>
                    <th>期数</th>
                    <th class="tr">本金(元)</th>
                    <th class="tr">利息(元)</th>
                    <th class="tr">罚息(元)</th>
                    <th class="tr">应还总额(元)</th>
                    <th>还款日</th>
                    <th>实还总额(元)</th>
                    <th>还款时间</th>
                    <th>状态</th>
                </tr>
                </thead>

                <tbody>
                <% for(var i=0,len=records.length; i < len; i++) {
                var item=records[i]
                %>
                <tr>
                    <td><%=item.period%></td>
                    <td class="tr"><%=item.corpus%></td>
                    <td class="tr"><%=item.expectedInterest%></td>
                    <td class="tr"><%=item.defaultInterest%></td>
                    <td class="tr"><%=item.expectedRepayAmount%></td>
                    <td><%=item.repayDate%></td>
                    <td class="tr">
                        <%=item.actualRepayAmount?item.actualRepayAmount:'--'%>
                    </td>
                    <td>
                        <%=item.actualRepayDate?item.actualRepayDate:'--'%>
                    </td>
                    <td>
                       <% if(isNormalRepayEnabled) {
                            if(item.isEnabled) {
                        %>

                        <a class="normal-repay btn btn-normal btn-sm" href="javascript:"><%=item.status%></a>
                        <form id="normal-repay-form" action="/repay" method="post" target="_blank">
                            <input type="hidden" name="loanId" value="<%=loanId%>"/>
                            <input type="hidden" name="_csrf" value="<%=csrfToken%>"/>
                        </form>

                        <% } else { %>
                        <%=item.status%>
                        <% } %>
                    <% } else { %>
                        <%=item.status%>
                    <% } %>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</script>
</@global.main>