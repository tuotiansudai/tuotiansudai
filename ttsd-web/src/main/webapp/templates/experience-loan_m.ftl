<#import "macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.experience_loan}" pageJavascript="${m_js.experience_loan}" activeNav="我要投资" activeLeftNav="" title="拓天速贷-互联网金融信息服务平台" >
<div class="my-account-content experience-amount" id="experienceAmount">
    <div class="account-summary">
        <div class="collection">
            <span class="title">${loan.name}</span>
            <span class="summary-box">
                <b><@percentInteger>${loan.baseRate}</@percentInteger><@percentFraction>${loan.baseRate}</@percentFraction>
                    <i>%</i></b>
                <em>预期年化收益</em>
            </span>
        </div>

        <div class="amount-balance">
            仅限体验金投资 不支持债权转让
        </div>
    </div>

    <div class="experience-total">
        <span>
            <b><em><@amount>${loan.minInvestAmount?string.computer}</@amount></em>元</b>
            <i>起投金额</i>
        </span>
        <span>
            <b>${loan.duration}天</b>
            <i>项目期限</i>
        </span>
        <span>
            <b>${interestPerTenThousands}元</b>
            <i>万元收益</i>
        </span>
    </div>

    <ul class="detail-list">
        <li>
            <label>计息方式</label>
            <span>${interestPointName}</span>

        </li>
        <li>
            <label>还款方式</label>
            <span>
            ${loan.loanType.getRepayType()}
            </span>
        </li>
        <li>
            <label>发布日期</label>
            <span>${verifyTime}</span>
        </li>
    </ul>

    <button class="to-invest-project" type="button">立即投资</button>
</div>

</@global.main>
