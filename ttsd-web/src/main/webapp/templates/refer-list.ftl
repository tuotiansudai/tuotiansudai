<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.refer_list}" activeNav="我的账户" activeLeftNav="推荐管理" title="推荐管理">
<div class="content-container invest-list-content">
    <h4 class="column-title"><em class="tc">推荐管理</em></h4>

        <p class="notice">
            用户在平台采用2级推荐机制，每级奖励金额为被推荐人投资本金年化的1％，推荐奖励在放款后一次性发放。详细推荐奖励细则请点 <a href="/about/refer-reward">此处链接</a>。<br/>
            您的推荐链接：<a href="/register/user?referrer=${referrer}" target="_blank">https://tuotiansudai.com/register/user?referrer=${referrer}</a> <br/>
        </p>

        <div class="date-filter " id="search-box">
            <span class="rl-title">起止时间：</span>
            <input type="text" id="date-picker" class="input-control" size="35"/>
            <span class="rl-title">推荐用户名：</span>
            <input type="text" id="loginName" class="input-control login-name" size="25"/>
            <input type="button" class="btn-normal btn-search" value="查询" onclick=""/>
            <input type="button" class="btn-primary btn-reset" value="重置"/>
        </div>

    <div class="table-out-border">
        <ul class="search-type tab-switch-menu clearfix">
            <li class="select-item current" data-type="referRelation"><span>我的推荐列表</span></li>
            <li class="select-item" data-type="referInvest"><span>推荐人投资列表</span></li>
        </ul>
        <div class="search-content-tab">

        </div>
        <table class="refer-relation table-striped table-center"></table>
        <table class="refer-invest table-striped"></table>
    </div>

    <div class="pagination" id="referRelationPagination" data-url="/referrer/refer-relation" data-page-size="10">
    </div>

    <div class="pagination" id="referInvestPagination" style="display: none;" data-url="/referrer/refer-invest" data-page-size="10">
    </div>
</div>
</@global.main>