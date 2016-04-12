<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loaner_loan_list}" activeNav="我的账户" activeLeftNav="我的借款" title="借款记录">
<div class="content-container loan-list-content">
    <h4 class="column-title">
        <em class="tc">借款记录</em>
        <#if !autoRepay>
            <i class="fr"><a href="/agreement/repay">开通自动还款 ></a></i>
        </#if>
    </h4>

    <div class="item-block date-filter">
        <span class="">起止时间:</span>
        <input type="text" id="date-picker" class="start-time filter input-control" size="35"/>
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
</@global.main>