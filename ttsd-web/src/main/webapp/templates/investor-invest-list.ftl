<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.investor_invest_list}" activeNav="我的账户" activeLeftNav="我的投资" title="投资记录">
<div class="content-container invest-list-content">
    <h4 class="column-title">
        <a href="/investor/invest-list"><em class="tc active">直投项目</em></a>
        <a href="/investor/invest-transfer-list"><em class="tc">转让项目</em></a>
    </h4>

    <div class="item-block date-filter">
        <span class="sub-hd">起止时间:</span>
        <input type="text" id="date-picker" class="input-control" size="35"/>
        <span class="select-item" data-day="1">今天</span>
        <span class="select-item" data-day="7">最近一周</span>
        <span class="select-item current" data-day="30">一个月</span>
        <span class="select-item" data-day="180">六个月</span>
        <span class="select-item" data-day="">全部</span>
    </div>

    <div class="item-block status-filter">
        <span class="sub-hd">交易状态:</span>
        <span class="select-item current" data-status="">全部</span>
        <span class="select-item" data-status="RAISING">正在招募</span>
        <span class="select-item" data-status="RECHECK">招募成功</span>
        <span class="select-item" data-status="REPAYING">正在回款</span>
        <span class="select-item" data-status="COMPLETE">回款完毕</span>
    </div>
    <div class="clear-blank"></div>
    <div class="invest-list" id="investList"></div>
    <script type="text/html" id="investListTpl">
        <table class="invest-list table-striped">
            <thead>
                <tr>
                    <th>项目名称</th>
                    <th class="tr">我的投资(元)</th>
                    <th>交易时间</th>
                    <th>交易状态</th>
                    <th>下次回款(元)</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
            {{if records.length>0}}
            {{each records}}
                <tr>
                    <td>
                        <span class="icon-list">
                            {{if $value.productType != 'EXPERIENCE'}}
                                {{each $value.userCoupons}}
                                {{if $value.couponType=='BIRTHDAY_COUPON'}}
                                <i class="birth-icon" data-benefit="{{$value.birthdayBenefit}}"></i>
                                {{/if}}
                                {{if $value.couponType=='INTEREST_COUPON'}}
                                <i class="coupon-icon" data-benefit="{{$value.rate * 100}}"></i>
                                {{/if}}
                                {{if $value.couponType=='RED_ENVELOPE'}}
                                <i class="money-icon" data-benefit="{{$value.amount / 100}}"></i>
                                {{/if}}
                                {{if $value.couponType=='NEWBIE_COUPON'}}
                                <i class="newbie-icon" data-benefit="{{$value.amount / 100}}"></i>
                                {{/if}}
                                {{if $value.couponType=='INVEST_COUPON'}}
                                <i class="ticket-icon" data-benefit="{{$value.amount / 100}}"></i>
                                {{/if}}
                                {{/each}}

                                {{if $value.achievement=='FIRST_INVEST'}}
                                <i class="first-icon"></i>
                                {{/if}}
                                {{if $value.achievement=='LAST_INVEST'}}
                                <i class="last-icon"></i>
                                {{/if}}
                                {{if $value.achievement=='MAX_AMOUNT'}}
                                <i class="max-icon"></i>
                                {{/if}}
                                {{if $value.extraRate !== null}}
                                <i class="extra-rate" data-benefit="{{$value.extraRate * 100}}"></i>
                                {{/if}}
                            {{/if}}
                        </span>
                        <a href="/loan/{{$value.loanId}}" class="project-name">{{$value.loanName}}</a>
                    </td>
                    <td>
                        {{$value.amount}}
                        {{if $value.productType=='EXPERIENCE'}}
                        体验金
                        {{/if}}
                    </td>
                    <td>{{$value.createdTime}}</td>
                    <td>{{$value.status}}</td>
                    <td>
                        {{if $value.productType=='EXPERIENCE'}}
                        {{$value.nextRepayAmount}}(现金红包)
                        {{else}}
                        {{if $value.nextRepayDate}}
                        {{$value.nextRepayDate}} / {{$value.nextRepayAmount}}
                        {{else}}
                        --
                        {{/if}}
                        {{/if}}
                    </td>
                    <td>
                    {{if $value.investRepayExist}}
                        <a class="show-invest-repay" data-url="/investor/invest/{{$value.investId}}/repay-data">回款记录</a>
                        {{if $value.productType!='EXPERIENCE'}}
                        <a class="red" href="/contract/investor/loanId/{{$value.loanId}}" target="_blank"> | 合同</a>
                        {{/if}}
                    {{else}}
                    --
                    {{/if}}
                    </td>
                </tr>
            {{/each}}
            {{else}}
            <td colspan="6" class="no-data">暂时没有投资记录</td>
            {{/if}}
            </tbody>
        </table>
    </script>


    <div class="pagination" data-url="/investor/invest-list-data" data-page-size="10">
    </div>
</div>
</@global.main>