<table class="invest-list">
 {{#step1}}
    <thead>
    <tr>
        <th>项目名称</th>
        <th>放款时间</th>
        <th>借款金额</th>
        <th>待还总额</th>
        <th>下次还款</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    {{#data.records}}
    <tr>
        <td>{{loanName}}</td>
        <td>{{recheckTime}}</td>
        <td>{{loanAmount}}</td>
        <td>{{unpaidAmount}}元</td>
        <td>{{nextRepayDate}}</td>
        <td><span class="plan" data-loan ='/loaner/loan-repay/{{loanId}}'>还款计划</span> | <a class="red" href="">合同</a></td>
    </tr>
    {{/data.records}}
    {{^data.records}}
    <tr>
    <td colspan="6" class="txtc">暂时没有投资纪录</td>
    </tr>
    {{/data.records}}
    </tbody>
    {{/step1}}
    {{#step2}}
        <thead>
        <tr>
            <th>借款标题</th>
            <th>放款时间</th>
            <th>应还总额</th>
            <th>实还总额</th>
            <th>还款日期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        {{#data.records}}
        <tr>
            <td>{{loanName}}</td>
            <td>{{recheckTime}}</td>
            <td>{{expectedRepayAmount}}</td>
            <td>{{actualRepayAmount}}元</td>
            <td>{{completedDate}}</td>
            <td><span class="plan" data-loan ='/loaner/loan-repay/{{loanId}}'>还款计划</span> | <a class="red" href="">合同</a></td>
        </tr>
        {{/data.records}}
        {{^data.records}}
        <tr>
        <td colspan="6" class="txtc">暂时没有投资纪录</td>
        </tr>
        {{/data.records}}
        </tbody>
        {{/step2}}

        {{#step3}}
                <thead>
                <tr>
                    <th>借款标题</th>
                    <th>借款金额</th>
                    <th>流标时间</th>
                </tr>
                </thead>
                <tbody>
                {{#data.records}}
                <tr>
                    <td>{{loanName}}</td>
                    <td>{{loanAmount}}</td>
                    <td>{{recheckTime}}</td>
                </tr>
                {{/data.records}}
                {{^data.records}}
                <tr>
                <td colspan="3" class="txtc">暂时没有投资纪录</td>
                </tr>
                {{/data.records}}
                </tbody>
                {{/step3}}
</table>

<div class="pagination" data-currentpage="{{data.index}}">
    <span class="total">共{{data.count}}条,当前第{{data.index}}页</span>
    {{#data.hasPreviousPage}}<a href="javascript:;" class="prevPage">上一页</a>{{/data.hasPreviousPage}}
    {{#data.hasNextPage}}<a href="javascript:;" class="nextPage">下一页</a>{{/data.hasNextPage}}
</div>
