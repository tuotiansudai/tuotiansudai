<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.help_center}" pageJavascript="${js.help_center}" activeNav="帮助中心" activeLeftNav="资金相关" title="拓天速贷公司介绍_拓天理念_拓天资质_拓天速贷" keywords="拓天速贷,拓天速贷公司,拓天资质,拓天价值" description="拓天速贷以透明、公平、高效为原则,为有贷款需求的小微企业及有投资需求的个人提供规范、安全、专业的互联网金融信息服务.">

    <#include "./helpLeftMenu.ftl"/>
<div class="help-center-group">
    <h3>
        <a href="/help/help-center"><< 返回帮助中心</a>
    </h3>

    <div class="problem-list-group">
        <div class="problem-title-item">
            <span class="active">充值</span>
            <span>投资</span>
            <span>回款</span>
            <span>提现</span>
            <span>资金存管</span>
        </div>
        <div class="problem-content-item">
            <ul class="list-group active" id="rechargeList">
            </ul>
            <ul class="list-group" id="investList">
            </ul>
            <ul class="list-group" id="paymentsList">
            </ul>
            <ul class="list-group" id="cashList">
            </ul>
            <ul class="list-group" id="fundList">
            </ul>
        </div>
    </div>
</div>
</@global.main>
