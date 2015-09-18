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
        <td><a class="red" href="/loan/{{loanId}}">{{recheckTime}}</td>
        <td>{{loanAmount}}</td>
        <td>{{unpaidAmount}}元</td>
        <td>{{nextRepayDate}}</td>
        <td><a class="red" href="">还款计划合同</a></td>
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
            <th>nice</th>
            <th>交易详情</th>
            <th>交易状态</th>
            <th>下次回款</th>
            <th>我的投资</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        {{#data.records}}
        <tr>
            <td>{{createdTime}}</td>
            <td><a class="red" href="/loan/{{loanId}}">{{loanName}}</td>
            <td>{{investStatus}}</td>
            <td>{{nextRepayDay}}/{{nextRepayAmount}}元</td>
            <td>{{amount}}元</td>
            <td><a class="red" href="">合同</a></td>
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
                    <th>哈哈</th>
                    <th>交易详情</th>
                    <th>交易状态</th>
                    <th>下次回款</th>
                    <th>我的投资</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                {{#data.records}}
                <tr>
                    <td>{{createdTime}}</td>
                    <td><a class="red" href="/loan/{{loanId}}">{{loanName}}</td>
                    <td>{{investStatus}}</td>
                    <td>{{nextRepayDay}}/{{nextRepayAmount}}元</td>
                    <td>{{amount}}元</td>
                    <td><a class="red" href="">合同</a></td>
                </tr>
                {{/data.records}}
                {{^data.records}}
                <tr>
                <td colspan="6" class="txtc">暂时没有投资纪录</td>
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
