<table width="822px;" class="rec_tab">
    <thead>
    <tr>
        <td>交易时间</td>
        <td>交易类型</td>
        <td>交易金额</td>
        <td>余额</td>
        <td>备注</td>
    </tr>
    </thead>
    <tbody>
       {{#rows}}
        <tr>
            <td>{{dealDate}}</td>
            <td>{{dealType}}</td>
            <td>￥{{money}}</td>
            <td>￥{{balance}}</td>
            <td>{{comment}}</td>
        </tr>
        {{/rows}}
        {{^rows}}
        <span>暂时没有资金记录</span>
        {{/rows}}
    </tbody>
</table>
{{#currentPage}}
<div class="page_record" data-currentpage="{{currentPage}}">
    <p><i>共 <small>{{totalPages}}</small> 页, <small>{{totalCount}}</small> 条记录,当前第<small>{{currentPage}}</small>页</i>
        {{#hasPreviousPage}}<em class="prevbtn">上一页</em>{{/hasPreviousPage}}
        {{#hasNextPage}}<em class="nextbtn">下一页</em>{{/hasNextPage}}
    </p>
</div>
{{/currentPage}}