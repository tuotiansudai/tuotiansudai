<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer}" pageJavascript="${js.transfer}" activeNav="我的账户" activeLeftNav="债权转让" title="债权转让">
<div class="content-container create-transfer-content">
    <h4 class="column-title">
        <em class="tc title-navli active">债权转让</em>
        <span class="rule-show">规则说明<i class="fa fa-question-circle text-b"></i></span>
    </h4>
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

    <div class="rule-list" id="ruleList">
        <div class="rule-com">
            <div class="close-icon close-btn"></div>
            <h3><span>债权转让规则</span></h3>
            <dl>
                <dd>1、持有的处于正常回款状态的债权，距离回款日5天以上时可转让；</dd>
                <dd>2、您只能对某一项目全部转让，转让成功后剩余债权价值归承接人所有；</dd>
                <dd>3、申请转让时，需为转让的债权设定转让价格，转让价格需≤债权本金；</dd>
                <dd>4、转让期间，原债权提前回款，则系统自动取消转让；</dd>
                <dd>5、每次转让的有效期为5天，过期未转让成功则自动取消转让；</dd>
                <dd>6、服务费用的收取：持有债权不足30天的，收取转让债权本金的1%；持有30-90天的，收取本金的0.5%；持有90天以上的，暂不收取服务费用。</dd>
                <dd> </dd>
            </dl>
            <div class="close-text">
                <span class="close-btn">知道了</span>
            </div>
        </div>
    </div>

</div>
</@global.main>