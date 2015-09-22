<table class="fund-list">
    <thead>
    <tr>
        <th>交易时间</th>
        <th>交易类型</th>
        <th>收入</th>
        <th>支出</th>
        <th>冻结</th>
        <th>可用余额</th>
        <th>备注</th>
    </tr>
    </thead>
    <tbody>
    {{#records}}
    <tr>
        <td>{{createdTime}}</td>
        <td>{{businessType}}</td>
        <td>+{{income}}元</td>
        <td>-{{expenditure}}元</td>
        <td>￥{{freeze}}</td>
        <td>￥{{balance}}</td>
        <td>编号:{{id}}</td>
    </tr>
    {{/records}}
    {{^records}}
    <td colspan="6" class="txtc">暂时没有投资纪录</td>
    {{/records}}
    </tbody>
</table>


<div class="pagination" data-currentpage="{{index}}">
    <span class="total">共{{count}}条,当前第{{index}}页</span>
    {{#hasPreviousPage}}<a href="javascript:;" class="prevPage">上一页</a>{{/hasPreviousPage}}
    {{#hasNextPage}}<a href="javascript:;" class="nextPage">下一页</a>{{/hasNextPage}}
</div>