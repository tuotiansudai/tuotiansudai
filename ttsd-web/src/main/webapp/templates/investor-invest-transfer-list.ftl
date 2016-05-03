<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.my_transfer_list}" activeNav="我的账户" activeLeftNav="我的投资" title="投资记录">
<div class="content-container invest-list-content">
    <h4 class="column-title">
        <a href="/investor/invest-list"><em class="tc">投资记录</em></a>
        <a href="/investor/invest-transfer-list"><em class="tc active">转让项目</em></a>
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
        <span class="select-item" data-status="REPAYING">正在回款</span>
        <span class="select-item" data-status="COMPLETE">回款完毕</span>
    </div>
    <div class="clear-blank"></div>
    <div class="invest-list">
        
    </div>
    <div class="pagination" data-url="/investor/invest-transfer-list-data" data-page-size="10">
    </div>
</div>
</@global.main>