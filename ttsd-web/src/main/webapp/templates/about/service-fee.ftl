<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="关于我们" activeLeftNav="服务费用" title="服务费用详情">
    <div class="content-container service-fee-container">
        <h2 class="column-title"><em>服务费用</em></h2>
        <table class="table-info clear-blank-m">
            <thead>
            <tr>
                <th>费用类型</th>
                <th>收费对象</th>
                <th>收费标准</th>
                <th>收取形式</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>服务费</td>
                <td>投资人</td>
                <td>投资所得利息的10%</td>
                <td>项目回款后自动扣除</td>
            </tr>
            <tr>
                <td>充值费用</td>
                <td></td>
                <td>充值费用全部由平台承担</td>
                <td>充值时自动扣除</td>
            </tr>
            <tr>
                <td>提现费用</td>
                <td>投资人</td>
                <td>每笔提现手续费${withdrawFee}元，费用由投资人承担</td>
                <td>提现时自动扣除</td>
            </tr>
            </tbody>

        </table>
    </div>
</@global.main>
