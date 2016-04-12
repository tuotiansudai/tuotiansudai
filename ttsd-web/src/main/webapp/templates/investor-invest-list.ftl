<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.investor_invest_list}" activeNav="我的账户" activeLeftNav="我的投资" title="投资记录">
<div class="content-container invest-list-content">
    <h4 class="column-title"><em class="tc">投资记录</em></h4>

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
    <div class="invest-list">

    </div>
    <div class="pagination" data-url="/investor/invest-list-data" data-page-size="10">
    </div>
</div>
</@global.main>