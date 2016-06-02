<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer}" pageJavascript="${js.transfer}" activeNav="我的账户" activeLeftNav="债权转让" title="债权转让">
<div class="content-container create-transfer-content">
    <h4 class="column-title"><em class="tc">债权转让</em></h4>
    <ul class="filters-list">
        <li <#if transferStatus?? && transferStatus == "TRANSFERABLE">class="active"</#if> data-status="TRANSFERABLE"><a href="/transferrer/transfer-application-list/TRANSFERABLE">可转让债权</a></li>
        <li <#if transferStatus?? && transferStatus == ("TRANSFERRING")>class="active"</#if> data-status="TRANSFERRING"><a href="/transferrer/transfer-application-list/TRANSFERRING">转让中债权</a></li>
        <li <#if !(transferStatus??) >class="active"</#if> data-status="SUCCESS,CANCEL"><a href="/transferrer/transfer-application-list">转让记录</a></li>
    </ul>
    <div class="list-container">
        <div class="record-list active">
            
        </div>
        <div class="pagination" data-url="/transferrer/transfer-application-list-data" data-page-size="10"></div>
    </div>
</div>
</@global.main>