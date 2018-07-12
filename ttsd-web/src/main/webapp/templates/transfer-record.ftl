<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer}" pageJavascript="${js.transfer}" activeNav="我的账户" activeLeftNav="债权转让" title="债权转让">
<div class="content-container create-transfer-content" id="myTransferCon" data-loaner-role="<@global.role hasRole="'LOANER'">LOANER</@global.role>">
    <h4 class="column-title">
        <em class="tc title-navli active">债权转让</em>
        <span class="rule-show">规则说明<i class="fa fa-question-circle text-b"></i></span>
    </h4>
    <ul class="filters-list">
        <li <#if transferStatus?? && transferStatus == "TRANSFERABLE">class="active"</#if> data-status="TRANSFERABLE"><a href="/transferrer/transfer-application-list/TRANSFERABLE">可转让债权</a></li>
        <li <#if transferStatus?? && transferStatus == ("TRANSFERRING")>class="active"</#if> data-status="TRANSFERRING"><a href="/transferrer/transfer-application-list/TRANSFERRING">转让中债权</a></li>
        <li <#if !(transferStatus??) >class="active"</#if> data-status="SUCCESS,CANCEL"><a href="/transferrer/transfer-application-list">转让记录</a></li>
    </ul>
    <div class="list-container">
        <div class="record-list active">
        </div>
        <div class="pagination" data-url="/transferrer/transfer-application-list-data" data-page-size="10"></div>
    </div>

    <div class="rule-list" id="ruleList">
        <div class="rule-com">
            <div class="close-icon close-btn"></div>
            <h3><span>债权转让规则</span></h3>
            <dl>
                <dd>1、持有的处于正常回款状态的债权，距离回款日5天以上时可转让；</dd>
                <dd>2、您只能对某一项目全部转让，转让成功后剩余债权价值归承接人所有；</dd>
                <dd>3、申请转让时，需为转让的债权设定转让价格，转让价格需≤债权本金；</dd>
                <dd>4、转让期间，原债权提前回款，则系统自动取消转让；</dd>
                <dd>5、每次转让的有效期为5天，过期未转让成功则自动取消转让；</dd>
                <dd>6、服务费用的收取：持有债权不足30天的，收取转让债权本金的1%；持有30-90天的，收取本金的0.5%；持有90天以上的，暂不收取服务费用。</dd>
                <dd> </dd>
            </dl>
            <div class="close-text">
                <span class="close-btn">知道了</span>
            </div>
        </div>
    </div>
<#--切换为出借人 -->
    <div id="turnInvestorDOM" class="pad-m popLayer" style="display: none">

        <div class="tc text-m">是否切换为出借人身份？</div>
        <form action="/account/switch?redirect=/transferrer/transfer-application-list/TRANSFERRING" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="tc person-info-btn" style="margin-top:40px;">
                <button class="btn  btn-cancel btn-close" type="button">取消</button>
                <button class="btn btn-success" type="submit">确定</button>
            </div>
        </form>
    </div>

</div>

<script type="text/template" id="transferableListTemplate">
    <table class="table-striped transfer-list">
        <thead>
        <tr>
            <th>项目名称</th>
            <th class="tr">项目本金(元)</th>
            <th>约定年化利率</th>
            <th>剩余天数</th>
            <th>到期时间</th>
            <th>下次回款(元)</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>

        <% for(var i = 0; i < records.length; i++) {
        var item = records[i];
        %>
        <tr>
            <td>
                <a href="/loan/<%=item.loanId%>" class="project-name"><%=item.loanName%></a>
            </td>
            <td><%=item.amount%></td>
            <td><%=item.sumRate%>%</td>
            <td><%=item.leftDays%></td>
            <td><%=item.lastRepayDate%></td>
            <td>
                <%=item.nextRepayAmount?item.nextRepayAmount:'--'%>
            </td>
            <td>
                <%=item.transferStatus?'<a href="javascript:void(0)" class="apply-transfer" data-invest-id="'+item.investId+'">'+item.transferStatus+'</a>':'--'%>
            </td>
        </tr>
        <% } %>
        <%=records.length?'':'<td colspan="7" class="no-data">暂时没有可转让债权记录</td>'%>
        </tbody>
    </table>


</script>

<script type="text/template" id="transferrerListTemplate">
    <table class="table-striped transfer-list">
        <thead>
        <tr>
            <th>转让项目名称</th>
            <th>转让价格(元)</th>
            <th>项目本金(元)</th>
            <th>约定年化利率</th>
            <th>剩余天数</th>
            <th>截止时间</th>
            <th>转让状态</th>
        </tr>
        </thead>
        <tbody>
        <% for(var i = 0; i < records.length; i++) {
        var item = records[i];
        %>
        <tr>
            <td>
                <a href="/transfer/<%=item.transferApplicationId%>" class="project-name"><%=item.name%></a>
            </td>
            <td><%=item.transferAmount%></td>
            <td><%=item.investAmount%></td>
            <td><%=item.sumRate%>%</td>
            <td><%=item.leftDays%></td>
            <td><%=item.deadLine%>
            </td>
            <td>
                <a href="javascript:void(0)" class="cancel-btn" data-transfer-application-id="<%=item.transferApplicationId%>">取消转让</a>
            </td>
        </tr>
        <% } %>
        <%=records.length?'':'<td colspan="7" class="no-data">暂时没有可转让债权记录</td>'%>
        </tbody>
    </table>
</script>

<script type="text/template" id="transferrerRecordTemplate">
    <table class="table-striped transfer-list">
        <thead>
        <tr>
            <th>转让项目名称</th>
            <th>转让价格(元)</th>
            <th>项目本金(元)</th>
            <th>约定年化利率</th>
            <th>剩余天数</th>
            <th>完成时间</th>
            <th>转让状态</th>
            <th style="width:8%;">操作 </th>
        </tr>
        </thead>
        <tbody>
        <% for(var i = 0; i < records.length; i++) {
        var item = records[i];
        %>
        <tr>
            <td>
                <a href="/transfer/<%=item.transferApplicationId%>" class="project-name"><%=item.name%></a>
            </td>
            <td><%=item.transferAmount%></td>
            <td><%=item.investAmount%></td>
            <td><%=item.sumRate%>%</td>
            <td><%=item.leftDays%></td>
            <td><%=item.transferTime?item.transferTime:'--'%>
            </td>
            <td><%=item.transferStatus%>
            </td>
            <td>
                <%=item.transferNewSuccess?'<a class="red" href="/contract/invest/contractNo/'+item.contractNo+'"  target="_blank"> 合同 </a>':''%>
                <%=item.transferOldSuccess?'<a class="red" href="/contract/transfer/transferApplicationId/'+item.transferApplicationId+'" target="_blank"> 合同 </a>':''%>
                <%=item.cancelTransfer?'--':''%>
            </td>
        </tr>
        <% } %>
        <%=records.length?'':'<td colspan="7" class="no-data">暂时没有可转让债权记录</td>'%>

        </tbody>
    </table>

</script>
</@global.main>