<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.create_transfer}" pageJavascript="${js.create_transfer}" activeNav="我的账户" activeLeftNav="债权转让" title="债权转让">
<div class="content-container create-transfer-content">
    <h4 class="column-title"><em class="tc">债权转让</em></h4>
    <ul class="filters-list">
        <li class="active">可转让债权</li>
        <li>转让中债权</li>
        <li>转让记录</li>
    </ul>
    <div class="clear-blank"></div>
    <div class="invest-list">
        <table class="invest-list table-striped">
            <thead>
            <tr>
                <th>项目名称</th>
                <th class="tr">我的投资(元)</th>
                <th>交易时间</th>
                <th>交易状态</th>
                <th>下次回款(元)</th>
                <th>详情</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            {{#records}}
            <tr>
                <td>
                    <i {{#birthdayCoupon}}class="birth-icon" data-benefit="{{birthdayBenefit}}" {{/birthdayCoupon}}></i>
                    <a href="/loan/{{loanId}}" class="project-name">{{loanName}}
                </td>
                <td class="tr">{{amount}}</td>
                <td>{{createdTime}}</td>
                <td>{{status}}</td>
                <td>
                    {{#nextRepayDate}}
                    {{nextRepayDate}} / {{nextRepayAmount}}
                    {{/nextRepayDate}}
                    {{^nextRepayDate}} -- {{/nextRepayDate}}
                </td>
                <td>
                    {{#hasInvestRepay}}
                    <a class="show-invest-repay" data-url="/investor/invest/{{investId}}/repay-data">回款记录</a> |
                    <a class="red" href="/contract/investor/loanId/{{loanId}}" target="_blank">合同</a>
                    {{/hasInvestRepay}}
                    {{^hasInvestRepay}} -- {{/hasInvestRepay}}
                </td>
                <td>
                    <a href="">{{transferStatus}}</a>
                </td>
            </tr>
            {{/records}}

            {{^records}}
            <td colspan="7" class="no-data">暂时没有投资记录</td>
            {{/records}}
            </tbody>
        </table>
    </div>
    <div class="pagination" data-url="/investor/invest-list-data" data-page-size="10">
    </div>
</div>
</@global.main>