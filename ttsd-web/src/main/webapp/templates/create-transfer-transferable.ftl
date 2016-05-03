<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.create_transfer}" pageJavascript="${js.create_transfer}" activeNav="我的账户" activeLeftNav="债权转让" title="债权转让">
<div class="content-container create-transfer-content">
    <h4 class="column-title"><em class="tc">债权转让</em></h4>
    <ul class="filters-list">
        <li class="active" data-status="TRANSFERABLE"><a href="/transferrer/transfer-application-list/transferable">可转让债权</a></li>
        <li data-status="TRANSFERRING"><a href="/transferrer/transfer-application-list/transferring">转让中债权</a></li>
        <li data-status="SUCCESS,CANCEL"><a href="/transferrer/transfer-application-list/success">转让记录</a></li>
    </ul>
    <div class="list-container">
        <div class="record-list active">
            
        </div>
        <div class="pagination" data-url="/transferrer/transfer-application-list-data" data-page-size="10"></div>
    </div>
</div>
</@global.main>