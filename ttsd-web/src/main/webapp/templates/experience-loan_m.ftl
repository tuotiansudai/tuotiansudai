<#import "macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.experience_loan}" pageJavascript="${m_js.experience_loan}" activeNav="我要投资" activeLeftNav="" title="拓天速贷-互联网金融信息服务平台" >
<div class="my-account-content experience-amount" id="experienceAmount" style="display: none">
    <div class="account-summary">
        <div class="go-back-container" id="goBack_experienceAmount">
            <span class="go-back"></span>
        </div>
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
            <b>0.00元</b>
            <i>万元收益</i>
        </span>
    </div>

    <ul class="detail-list">
        <li>
            <label>计息方式</label>
            <span>按天计息，即投即生息</span>

        </li>
        <li>
            <label>还款方式</label>
            <span>
            体验金收回，收益归您
            </span>
        </li>
        <li>
            <label>发布日期</label>
            <span>${.now}</span>
        </li>
    </ul>

    <button class="to-invest-project" type="button" id="investment_btn">立即投资</button>
</div>

<div class="my-account-content apply-transfer" id="applyTransfer" style="display: none">
    <div class="goBack_applyTransfer">
        购买详情
        <div class="go-back-container" id="goBack_applyTransfer">
            <span class="go-back"></span>
        </div>
    </div>
    <div class="benefit-box">
        <div class="target-category-box" data-url="">
            <div class="newer-title">
                <span class="line_icon"></span>
                <span>${loan.name}</span>
            </div>
            <ul class="loan-info clearfix">
                <li class="experience-total">
                    <span>
                         <b>${loan.duration}天</b>
                         <i>项目期限</i>
                    </span>
                </li>
                <li class="experience-total">
                    <span>
                        <b><@percentInteger>${loan.baseRate}</@percentInteger><@percentFraction>${loan.baseRate}</@percentFraction>%</b>
                         <i>预期年化收益</i>
                    </span>
                </li>
                <li class="experience-total">
                    <span>
                        <b>0.00元</b>
                        <i>万元收益</i>
                    </span>
                </li>
            </ul>
        </div>
        <div class="bg-square-box"></div>
    </div>
    <form id="investForm">
        <div class="input-amount-box">
            <ul class="input-list">
                <li class="investmentAmount">
                    <span style="display: none" id="my_experience_balance">${(experienceBalance / 100)}</span>
                    <label>投资金额</label>
                    <input id="experience_balance" type="text" data-start_investment="${loan.minInvestAmount / 100}" data-experience_balance="${(experienceBalance / 100)}" value="${(experienceBalance / 100)?string("0.00")}" name="price" class="" placeholder=${(loan.minInvestAmount / 100)?string("0.00")}元起投>
                    <span class="close_btn" id="close_btn"></span>
                    <em>元</em>
                </li>
                <li class="mt-10">
                    <label style="font-family: PingFangSC-Light;">预期收益</label>
                    <span class="number-text"><strong id="expect-amount">0.00</strong>元</span>
                </li>
            </ul>
        </div>

        <button type="submit" class="btn-wap-normal" id="submitBtn">立即体验</button>
        <div class="shade_mine" style="display: none"></div>
    </form>
    <div class="transfer-notice" style="font-family: PingFangSC-Light;">

        <b>温馨提示:</b>
        用户首次提现体验金投资所产生的收益时，需要投资其他定期项目（债权转让项目除外）累计满1000元才可以提现。
    </div>
    <div id="freeSuccess"  style="display: none">
        <div class="success-info-tip">
            <div class="pop_title">温馨提示</div>
            <div class="pop_content">体验金余额不足，<br/>快去参与活动赢取体验金吧！</div>
        </div>
    </div>
</div>
<div id="investmentSuc" style="display: none">
    <div class="goBack_applyTransfer">
        投资成功
    </div>
    <div class="my-account-content apply-transfer-success">
        <div class="info">
            <div class="icon-success-wrapper">
                <i class="icon-success"></i>
                <div class="investment-success-text">投资成功</div>
            </div>
            <ul class="input-list">
                <li class="item">
                    <label>投资金额</label>
                    <em id="investment_suc_amount"></em>
                </li>
                <li class="item">
                    <label>所投项目</label>
                    <em>${loan.name}</em>
                </li>
            </ul>
        </div>
    </div>
    <div class="button-note">
        <a href="/m/" class="btn-wap-normal next-step">确定</a>
    </div>
</div>
</@global.main>
