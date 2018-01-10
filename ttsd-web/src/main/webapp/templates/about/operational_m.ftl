<#import "../macro/global_m.ftl" as global>
<@global.main pageCss="${m_css.operational}" pageJavascript="${m_js.operational}" activeNav="信息披露" activeLeftNav="运营数据" title="运营数据_信息安全数据_拓天速贷" keywords="安全信息,安全平台,数据信息,信息披露,拓天速贷" description="拓天速贷运营数据全景展示,平台投资明细、注册投资用户、累计投资金额及平台数据总览,为您提供安全投资的平台运营数据.">
<div class="about-us-container">
    <div class="go-back-container" id="goBack_experienceAmount">
        <span class="go-back"></span>
        运营数据
    </div>
    <div class="swiper-container">
        <div class="swiper-wrapper">
            <div class="swiper-item swiper-slide1 swiper-slide">
                <div class="data_dealine">数据截止到<span class="currentMonth"></span>月<span class="currentDay"></span>日</div>
                <div class="safe_operational_text">拓天速贷已安全运营</div>
                <div class="safe_day_wrapper_center">
                    <div class="safe_day_wrapper">
                        <span class="safe_day_unit">天</span>
                    </div>
                </div>
                <div class="grand_total">
                    <div class="grand_total_icon"></div>
                    <div class="grand_total_text">累计交易金额</div>
                    <div class="text_underLine"></div>
                    <div class="grand_total_amount"><span id="grand_total_amount"></span>元</div>
                </div>
                <div class="earn_total">
                    <div class="earn_total_icon"></div>
                    <div class="earn_total_text">拓天速贷已累计为用户赚取</div>
                    <div class="text_underLine"></div>
                    <div class="earn_total_amount"><span id="earn_total_amount"></span>元</div>
                </div>
                <div class="button_under_arrow side_to_page" data-index="1"></div>
            </div>

            <div class="swiper-item swiper-slide2 swiper-slide">
                <div class="part-2-topBar"><div class="trade-detail">平台交易明细</div></div>
                <div class="total_trade_text">累计交易笔数</div>
                <div class="text_underLine_blue"></div>
                <div class="total_trade_count"><span id="total_trade_count"></span>笔</div>
                <div class="unit_symbol">[单位:万元]</div>
                <div id="main_part1"></div>
                <div class="chart1_title">近半年<span class="chart1_title_strong">标的投资数据</span>统计</div>
                <div class="unit_symbol">[单位:万元]</div>
                <div id="main_part2"></div>
                <div class="chart1_title">近半年<span class="chart1_title_strong">交易金额数据</span>统计</div>
                <div class="button_under_arrow side_to_page" data-index="2"></div>
            </div>

            <div class="swiper-item swiper-slide3 swiper-slide">
                <div class="part-3-topBar"><div class="user-statistics">平台用户统计</div></div>
                <div class="total_trade_text">累计注册投资用户</div>
                <div class="text_underLine_blue"></div>
                <div class="total_trade_count"><span id="total_trade_person"></span>人</div>
                <div id="main_part3"></div>
                <div id="main_part4"></div>
                <div class="button_under_arrow side_to_page" data-index="3"></div>
            </div>

            <div class="swiper-item swiper-slide4 swiper-slide">
                <div class="part-4-topBar"><div class="user-area">平台地区统计</div></div>
                <div class="count_proportion">
                    <div class="symbol_line"></div>
                    <div class="count_title">投资人数<span class="title_strong">TOP3占比</span></div>
                </div>
                <div id="main_part5"></div>
                <div class="amount_proportion">
                    <div class="symbol_line"></div>
                    <div class="amount_title">投资金额<span class="title_strong">TOP3占比</span></div>
                </div>
                <div id="main_part6"></div>
                <div class="button_under_arrow side_to_page" data-index="4"></div>
            </div>

            <div class="swiper-item swiper-slide5 swiper-slide">
                <div class="tuotian_logo"></div>
                <div class="button_up_arrow side_to_page" data-index="5"></div>
                <div class="company_desc_text">
                    <div style="margin-bottom: .25rem">拓天速贷</div>
                    <div>互联网金融信息服务平台</div>
                </div>
            </div>
        </div>
    </div>

    <#if investDetailList??>
        <#list investDetailList as investDetailItem>
            <div style="display: none" id="investItem${investDetailItem_index}" data-day="${investDetailItem.productName!}" data-amount="${investDetailItem.totalInvestAmount!}" data-count="${investDetailItem.countInvest!}"></div>
        </#list>
    </#if>
</div>
</@global.main>