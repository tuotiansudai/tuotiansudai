<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="${js.refer_list}" activeNav="我的账户" activeLeftNav="推荐管理" title="推荐管理">
<div class="content-container invest-list-content">
    <h4 class="column-title"><em class="tc">推荐管理</em></h4>

    <div class="borderBox">
        <p class="notice">
            用户在平台采用2级推荐机制，每级奖励金额为被推荐人投资本金年化的1％，按照标的的周期，每期平均返现。奖励的钱会在标的还款时发放到推荐人的账户。详细推荐奖励细则请点<a href="/events/refer-reward-instruction">此处链接</a>。<br/>
            您的推荐链接：<a href="/register/user?referrer=${referrer}" target="_blank">http://tuotiansudai.com/register/user?referrer=${referrer}</a> <br/>
        </p>

        <div class="date-filter">
            <span class="sub-hd">起止时间：</span>
            <input type="text" id="date-picker" class="input-control" size="35"/>
            <span class="sub-hd" style="margin-left: 10px">推荐用户名：</span>
            <input type="text" id="loginName" class="input-control" style="width: 140px" size="25"/>
            <input type="button" class="btn-normal" style="margin-left: 10px" value="查询" onclick=""/>
            <input type="button" class="btn" style="margin-left: 10px" value="重置"/>
        </div>
    </div>

    <div class="item-block search-type">
        <span class="select-item current" data-type="referRelation">我的推荐列表</span>
        <span class="select-item" data-type="referInvest">推荐人投资列表</span>
    </div>

    <div class="bRadiusBox bg-w" id="tMonthBox">
        <table class="refer-relation table-striped"></table>
        <table class="refer-invest table-striped clear-blank"></table>
    </div>

    <div class="pagination" id="referRelationPagination" data-url="/referrer/refer-relation" data-page-size="10">
    </div>

    <div class="pagination" id="referInvestPagination" style="display: none;" data-url="/referrer/refer-invest" data-page-size="10">
    </div>
</div>
</@global.main>