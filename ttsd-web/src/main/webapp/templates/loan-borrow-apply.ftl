<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.loan_borrow}" pageJavascript="${js.loan_borrow}" activeNav="我要借款" activeLeftNav="" title="我要借款_抵押借款_拓天速贷" keywords="抵押房产借款,抵押车辆借款,拓天借款,拓天速贷" description="拓天速贷为借款用户提供抵押房产借款和抵押车辆借款服务,拓天借款额度高,门槛低,速度快,利息低,24H放款,借款轻松还.">
<div class="loan_apply_wrapper">
    <h2 class="loan_title">借款申请-${pledgeType}</h2>
    <div class="base_info_wrapper">
        <h3 class="info_title">申请人基础信息</h3>
        <div class="info_wrapper">
            <div>
                <span class="name item">${userName!}</span>
                <span class="cardId item">身份证：${identityNumber!}</span>
                <span class="address item">地址：${address}</span>
            </div>
            <div>
                <span class="sex item">性别：${sex!}</span>
                <span class="age item">年龄：${age!}</span>
                <span class="tel item">电话：${mobile!}</span>
            </div>
        </div>
    </div>
    <div class="supplement_info_wrapper">
        <h3 class="info_title">请输入补充信息</h3>
        <div class="info_wrapper">
            <div class="supplement_info_item">
                <span class="required-icon">*</span>
                <span class="item_text">婚姻状况：</span>
                <input type="radio" name="isMarried" class="check_radio" id="married"/><label for="married" class="check_label">已婚</label>
                <input type="radio" name="isMarried" class="check_radio" id="noMarried"/><label for="noMarried" class="check_label">未婚</label>
            </div>
            <div class="supplement_info_item">
                <span class="required-icon">*</span>
                <span class="item_text">提供个人征信报告：</span>
                <input type="radio" name="haveCreditReport" class="check_radio" id="creditReport"/><label for="creditReport" class="check_label">提供</label>
                <input type="radio" name="haveCreditReport" class="check_radio" id="noCreditReport"/><label for="noCreditReport" class="check_label">不提供</label>
            </div>
            <div class="supplement_info_item">
                <span class="required-icon required-icon-none">*</span>
                <span class="item_text workPosition-text">职位：</span>
                <input type="text" placeholder="例如：技术总监" class="workPosition item-input" maxlength="20"/> 最多20个字
            </div>
            <div class="supplement_info_item">
                <span class="required-icon required-icon-none">*</span>
                <span class="item_text sesameCredit-text">芝麻信用分：</span>
                <input type="text" placeholder="请输入（选填）" class="sesameCredit item-input" />
            </div>
        </div>
    </div>
    <div class="loan_application_wrapper">
        <h3 class="info_title">请输入借款申请信息</h3>
        <div class="info_wrapper">
            <div class="loan_application_item">
                <span class="required-icon">*</span>
                <span class="item_text amount_text">借款金额：</span>
                <input type="text" placeholder="请输入（万元）" class="amount item-input" />
            </div>
            <div class="loan_application_item">
                <span class="required-icon">*</span>
                <span class="item_text period_text">借款周期：</span>
                <input type="text" placeholder="请输入（月）" class="period item-input" />
            </div>
            <div class="loan_application_item">
                <span class="required-icon">*</span>
                <span class="item_text homeIncome_text">家庭年收入：</span>
                <input type="text" placeholder="请输入（万元）" class="homeIncome item-input" />
            </div>
            <div class="loan_application_item">
                <span class="required-icon">*</span>
                <span class="item_text loanUsage_text textarea_text">借款用途：</span>
                <textarea type="text" placeholder="请输入（200字以内）" class="loanUsage item-textarea" maxlength="200"></textarea>
            </div>
            <div class="loan_application_item">
                <span class="required-icon">*</span>
                <span class="item_text pledgeInfo_text textarea_text">房产信息：</span>
                <textarea type="text" placeholder="请输入（200字以内）" class="pledgeInfo item-textarea" maxlength="200"></textarea>
            </div>
            <div class="loan_application_item">
                <span class="required-icon required-icon-none">*</span>
                <span class="item_text elsePledge_text textarea_text">其他资产：</span>
                <textarea type="text" placeholder="请输入（200字以内）" class="elsePledge item-textarea" maxlength="200"></textarea>
            </div>
        </div>
    </div>
    <div class="confirm_btn">确认申请</div>
</div>
</@global.main>
