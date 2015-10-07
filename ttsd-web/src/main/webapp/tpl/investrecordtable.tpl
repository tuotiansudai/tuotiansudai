<table class="invest-list">
    <thead>
        <tr>
            <th>交易时间</th>
            <th>交易详情</th>
            <th>交易状态</th>
            <th>下次回款</th>
            <th>我的投资</th>
            <th>操作</th>
        </tr>
    </thead>
    <tbody>
       {{#recordDtoList}}
        <tr>
            <td>{{createdTime}}</td>
            <td><a class="red" href="/loan/{{loanId}}">{{loanName}}</td>
            <td>{{investStatus}}</td>
            <td>
            {{#nextRepayDay}}{{nextRepayDay}} / {{nextRepayAmount}}元{{/nextRepayDay}}
            {{^nextRepayDay}}/{{/nextRepayDay}}
            </td>
            <td>{{amount}}元</td>
            <td>
            {{#hasContract}}<a class="red" href="">合同</a> /{{/hasContract}}
            <a class="red" href="">回款记录</a>
            </td>
        </tr>
        {{/recordDtoList}}
        {{^recordDtoList}}
         <td colspan="6" class="txtc">暂时没有投资纪录</td>
        {{/recordDtoList}}
    </tbody>
</table>
{{#totalPages}}

<div class="pagination" data-currentpage="{{index}}">
            <span class="total">共{{totalCount}}条,当前第{{index}}页</span>
            {{#hasPreviousPage}}<a href="javascript:;" class="prevPage">上一页</a>{{/hasPreviousPage}}
            {{#hasNextPage}}<a href="javascript:;" class="nextPage">下一页</a>{{/hasNextPage}}
        </div>
{{/totalPages}}

