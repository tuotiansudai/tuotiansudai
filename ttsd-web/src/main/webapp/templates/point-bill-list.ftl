<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.point_bill_list}" activeNav="我的账户" activeLeftNav="我的财豆" title="财豆明细">
<div class="content-container invest-list-content">
    <h4 class="column-title"><em class="tc">财豆明细</em></h4>

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
        <span class="sub-hd">往来类型:</span>
        <span class="select-item current" data-status="">全部</span>
        <span class="select-item" data-status="obtain">已获得</span>
        <span class="select-item" data-status="use">已使用</span>
    </div>
    <div class="clear-blank"></div>
    <div class="point-bill-list">

    </div>
    <div class="pagination" data-url="/point/point-bill-list-data" data-page-size="10">
    </div>
</div>
</@global.main>