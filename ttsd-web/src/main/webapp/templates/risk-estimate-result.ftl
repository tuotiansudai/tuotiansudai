<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.risk_estimate}" pageJavascript="${js.risk_estimate}" activeNav="我的账户" activeLeftNav="个人资料" title="投资偏好评估结果">
<div class="content-container invetsment-preferences" id="investmentBox">
    <h4 class="column-title"><em class="tc">投资偏好评估</em></h4>
    <div class="result-item">
        <div class="title-text">您的投资偏好为</div>
        <div class="result-text">进取型</div>
        <div class="intro-text">
            您具有较高的风险承受能力，在投资的过程中可以非常好的认识好风险和收益之间的关系，能承担投资带来的风险，同时可以接受投资带来的结果。投资目标主要是取得超额的收益，为了实现投资目标愿意承担较大的投资风险。
        </div>
        <div class="link-item">
            <a href="/risk-estimate" class="btn-item to-risk">重新评估</a>
            <a href="/loan-list" class="btn-item">立即投资</a>
        </div>
    </div>
</div>
</@global.main>


