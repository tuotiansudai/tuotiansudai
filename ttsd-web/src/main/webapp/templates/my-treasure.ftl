<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.my_treasure}" activeNav="我的账户" activeLeftNav="我的宝藏" title="我的宝藏">

<div class="content-container my-treasure-content">
    <div class="rule-list" id="ruleList">
        <div class="rule-com">
            <div class="close-icon close-btn"></div>
            <h3><span>使用规则</span></h3>
            <dl>
                <dt>体验券使用规则：</dt>
                <dd>1. 体验券仅适用于标的投资；</dd>
                <dd>2. 体验结束后系统自动收回本金，收益转回用户账户内，详见“我的账户”-->“资金管理”；</dd>
                <dd>3. 如体验券中有限制条件， 用户必须按照限制条件使用。</dd>
            </dl>
            <dl>
                <dt>加息券使用规则：</dt>
                <dd>1. 加息券仅适用于标的投资；</dd>
                <dd>2. 标的回款后，加息券所得收益转回用户账户内，详见“我的账户”-->“资金管理”；</dd>
                <dd>3. 如加息券中有限制条件， 用户必须按照限制条件使用；</dd>
                <dd>4. 使用方式：在“我要投资”-->“标的详情”页面选择使用加息券。</dd>
            </dl>
            <dl>
                <dt>红包使用规则：</dt>
                <dd>1. 在投资过程中使用红包，投资成功放款后即可返现；</dd>
                <dd>2. 现金红包不可与平台其他优惠券同时使用（3元现金红包除外）；</dd>
                <dd>3. 投资成功放款后用户获得的现金可在“我的账户”中查询，提现；</dd>
                <dd>4. 如红包有使用条件，用户需要按照条件使用。</dd>
            </dl>
            <div class="close-text">
                <span class="close-btn">我已了解</span>
            </div>
        </div>
    </div>
    <h4 class="column-title">
        <em class="tc title-navli active">我的宝藏</em>
        <span class="rule-show">使用规则？</span>
    </h4>
    <ul class="filters-list">
         <li class="active">未使用</li>
         <li>已使用</li>
         <li>已过期</li>
     </ul> 
     <div class="model-list">
        <div class="coupon-com active">
            <ul class="coupon-list">
                <li>
                    <div class="left-name">
                        现金红包
                    </div>
                    <div class="right-coupon">
                        dsd
                    </div>
                    <div class="bottom-time">
                        
                    </div>
                </li>
                <li></li>
            </ul>
        </div>
        <div class="coupon-com">
            2
        </div>
        <div class="coupon-com">
            3
        </div>
     </div>
    <#include "coupon-alert.ftl" />
</div>
</@global.main>