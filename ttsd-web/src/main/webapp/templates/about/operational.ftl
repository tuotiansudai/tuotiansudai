<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="运营数据" activeLeftNav="运营数据" title="运营数据_信息安全数据_拓天速贷" keywords="安全信息,安全平台,数据信息,信息披露,拓天速贷" description="拓天速贷运营数据全景展示,平台投资明细、注册投资用户、累计投资金额及平台数据总览,为您提供安全投资的平台运营数据.">
    <div class="about-us-container operational-wrap">
        <div class="operater-days-wrap section-wrap">
            <div class="operater-day clearfix" id="operationDays"></div>
        </div>
        <div class="data-model data-overview">
            <h3 class="total-view"><span class="circle_icon"></span>平台数据总览 <span class="font-right" id="dateTime"></span></h3>
            <div class="data-wrap">
                <ul class="clearfix">
                    <li>
                        <div class="item_li_wrapper">
                            <p class="item_li_text total-first">累计交易金额</p>
                            <p>
                                <em id="tradeAmount" class="item_li_amount"></em>
                                <span class="item_li_unit">万元</span>
                                <span class="item_li_total_orders total-first">（<span class="total_trade_count"></span>笔）</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="item_li_text total-first">累计为用户赚取</p>
                            <p>
                                <em id="earn_total_amount" class="item_li_amount"></em>
                                <span class="item_li_unit">万元</span>
                                <span class="item_li_total_orders total-first">（<span class="total_trade_count"></span>笔）</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="item_li_text total-first">待偿金额</p>
                            <p>
                                <em class="item_li_amount sumExpectedAmount"></em>
                                <span class="item_li_unit">万元</span>
                                <span class="item_li_total_orders total-first">（<span class="sumRepayIngInvestCount"></span>笔）</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="item_li_text total-first">待偿利息金额</p>
                            <p>
                                <em id="sumExpectedInterestAmount" class="item_li_amount"></em>
                                <span class="item_li_unit">万元</span>
                                <span class="item_li_total_orders total-first">（<span class="sumRepayIngInvestCount"></span>笔）</span>
                            </p>
                        </div>
                    </li>

                    <li>
                        <div class="item_li_wrapper">
                            <p class="item_li_text total-first">总注册用户数</p>
                            <p>
                                <em id="usersCount" class="item_li_amount"></em>
                                <span class="item_li_unit">人</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="item_li_text total-first">累计出借用户数</p>
                            <p>
                                <em class="investUsersCount" class="item_li_amount"></em>
                                <span class="item_li_unit">人</span>
                            </p>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="data-model cheat-model marginTop20 data-detail">
            <h3 class="total-view"><span class="circle_icon"></span>平台交易明细 <span class="font-right" id="dateTime">月度交易金额（近半年）</span></h3>
            <div class="model-container chart-dom" id="dataRecord">
                <!--[if gte IE 8]>
                请使用更高版本浏览器查看
                <![endif]-->
            </div>
        </div>
        <div class="data-loan-wrapper">
            <h3 class="total-view"><span class="circle_icon"></span>借款标的情况</h3>
            <ul class="clearfix">
                <li class="loan_item_wrapper loan_item_wrapper_left">
                    <span class="loan_count_wrapper"><em class="loan_count" id="sumLoanAmount"></em>万元</span>
                    <span class="loan_text">累计借贷金额</span>
                </li>
                <li class="loan_item_wrapper loan_item_wrapper_right">
                    <span class="loan_count_wrapper"><em class="loan_count" id="sumLoanCount"></em>笔</span>
                    <span class="loan_text">累计借贷笔数</span>
                </li>
            </ul>
        </div>
        <div class="invest-section">
            <div class="invest-wrap clearfix">
                <h3 class="total-view"><span class="circle_icon"></span>出借人信息</h3>
                <div class="data-wrap">
                    <ul class="clearfix">
                        <li>
                            <div class="item_li_wrapper">
                                <p class="clearfix">
                                    <span id="tradeAmount" class="item_li_text item_info">累计出借人数</span>
                                    <span class="item_li_total_orders item_info"><span class="investUsersCount"></span>人</span>
                                </p>
                            </div>
                        </li>
                        <li>
                            <div class="item_li_wrapper">
                                <p class="clearfix">
                                    <span id="tradeAmount" class="item_li_text item_info">最大单户出借余额占比</span>
                                    <span class="item_li_total_orders item_info"><span id="maxSingleInvestAmountRate"></span>%</span>
                                </p>
                            </div>
                        </li>
                        <li>
                            <div class="item_li_wrapper">
                                <p class="clearfix">
                                    <span id="tradeAmount" class="item_li_text item_info">人均累计出借金额</span>
                                    <span class="item_li_total_orders item_info"><span id="avgInvestAmount"></span>万元</span>
                                </p>
                            </div>
                        </li>
                        <li>
                            <div class="item_li_wrapper">
                                <p class="clearfix">
                                    <span id="tradeAmount" class="item_li_text item_info">最大十户出借余额占比</span>
                                    <span class="item_li_total_orders item_info"><span id="maxTenInvestAmountRate"></span>%</span>
                                </p>
                            </div>
                        </li>

                        <li>
                            <div class="item_li_wrapper">
                                <p class="clearfix">
                                    <span id="tradeAmount" class="item_li_text item_info">当前出借人数</span>
                                    <span class="item_li_total_orders item_info"><span id="sumNotCompleteInvestorCount"></span>人</span>
                                </p>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="data-model cheat-model">
                    <div class="model-container" id="investRecord">
                        <!--[if gte IE 8]>
                        请使用更高版本浏览器查看
                        <![endif]-->
                    </div>
                </div>
                <div class="data-model cheat-model">

                    <div class="model-container" id="investSexRecord">
                        <!--[if gte IE 8]>
                        请使用更高版本浏览器查看
                        <![endif]-->
                    </div>
                </div>
            </div>
            <div class="data-model region-wrap">
                <#--<h3 class="font14 paddingLeft">出借人地域分布</h3>-->
                <div class="model-container geographical-item" id="investRegionRecord">
                    <ul id="geographicalWrap">
                    </ul>
                </div>

            </div>
        </div>
        <div class="loan-section">
        <div class="invest-wrap clearfix">
            <h3 class="total-view"><span class="circle_icon"></span>借款人信息</h3>
            <div class="data-wrap">
                <ul class="clearfix">
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span id="tradeAmount" class="item_li_text item_info">累计借款人数</span>
                                <span class="item_li_total_orders item_info"><span class="sumLoanerCount"></span>人</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span id="tradeAmount" class="item_li_text item_info">最大单一借款人待还金额占比</span>
                                <span class="item_li_total_orders item_info"><span id="maxSingleLoanAmountRate"></span>%</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span id="tradeAmount" class="item_li_text item_info">人均累计借款金额</span>
                                <span class="item_li_total_orders item_info"><span id="avgLoanAmount"></span>万元</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span id="tradeAmount" class="item_li_text item_info">前十大借款人待还金额占比</span>
                                <span class="item_li_total_orders item_info"><span id="maxTenLoanAmountRate"></span>%</span>
                            </p>
                        </div>
                    </li>

                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span id="tradeAmount" class="item_li_text item_info">当前借款人数</span>
                                <span class="item_li_total_orders item_info"><span id="sumNotCompleteLoanerCount"></span>人</span>
                            </p>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="data-wrap">
                <ul class="clearfix">
                    <li class="loan_data_wrapper">
                        <div class="loan_data_wrapper_count"><em class="sumLoanerCount" class="loan_amount_font"></em>人</div>
                        <div>借款人数</div>
                    </li>
                    <li class="loan_data_wrapper">
                        <div class="loan_data_wrapper_count"><em class="loan_amount_font sumExpectedAmount"></em>元</div>
                        <div>待偿金额</div>
                    </li>
                    <li class="loan_data_wrapper">
                        <div class="loan_data_wrapper_count"><em id="sumOverDueAmount" class="loan_amount_font"></em>元</div>
                        <div>逾期金额</div>
                    </li>
                </ul>
            </div>
            <div class="data-model cheat-model">
                <div class="model-container" id="loanBaseRecord">
                    <!--[if gte IE 8]>
                    请使用更高版本浏览器查看
                    <![endif]-->
                </div>
            </div>
            <div class="data-model cheat-model">

                <div class="model-container" id="loanBaseSexRecord">
                    <!--[if gte IE 8]>
                    请使用更高版本浏览器查看
                    <![endif]-->
                </div>
            </div>
        </div>
        <div class="data-model region-wrap">
            <#--<h3 class="font14">借款人地域分布</h3>-->
            <div class="model-container geographical-item" id="loanRegionRecord">
                <ul id="geographicalWrapLoan">
                </ul>
            </div>

        </div>
        </div>

        <div class="data-overdue-wrapper clearfix">
            <h3 class="total-view"><span class="circle_icon"></span>逾期情况</h3>
            <ul class="clearfix">
                <li class="loan_item_wrapper loan_item_wrapper_left">
                    <span class="loan_count_wrapper"><em class="loan_count" id="loanerOverDueCount"></em>次</span>
                    <span class="loan_text">借款人平台逾期次数</span>
                </li>
                <li class="loan_item_wrapper loan_item_wrapper_right">
                    <span class="loan_count_wrapper"><em class="loan_count" id="loanOverDueRate"></em>%</span>
                    <span class="loan_text">项目逾期率</span>
                </li>
                <li class="loan_item_wrapper loan_item_wrapper_left">
                    <span class="loan_count_wrapper"><em class="loan_count" id="loanerOverDueAmount"></em>元</span>
                    <span class="loan_text">平台逾期总金额</span>
                </li>
                <li class="loan_item_wrapper loan_item_wrapper_right">
                    <span class="loan_count_wrapper"><em class="loan_count" id="amountOverDueRate"></em>%</span>
                    <span class="loan_text">金额逾期率</span>
                </li>
            </ul>
            <div class="data-wrap data-overdue-info">
                <ul class="clearfix">
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span class="item_li_text item_info">金额逾期率（90天及以内）</span>
                                <span class="item_li_total_orders item_info"><span id="amountOverDueLess90Rate"></span>%</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span class="item_li_text item_info">项目逾期率（90天及以内）</span>
                                <span class="item_li_total_orders item_info"><span id="loanOverDueLess90Rate"></span>%</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span id="tradeAmount" class="item_li_text item_info">金额逾期率（90天以上至180天）</span>
                                <span class="item_li_total_orders item_info"><span id="amountOverDue90To180Rate"></span>%</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span class="item_li_text item_info">项目逾期率（90天以上至180天）</span>
                                <span class="item_li_total_orders item_info"><span id="loanOverDue90To180Rate"></span>%</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span class="item_li_text item_info">金额逾期率（181天及以上）</span>
                                <span class="item_li_total_orders item_info"><span id="amountOverDueGreater180Rate"></span>%</span>
                            </p>
                        </div>
                    </li>
                    <li>
                        <div class="item_li_wrapper">
                            <p class="clearfix">
                                <span class="item_li_text item_info">项目逾期率（181天及以上）</span>
                                <span class="item_li_total_orders item_info"><span id="loanOverDueGreater180Rate">5</span>%</span>
                            </p>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="year-report-operation">
            <h3 class="total-view"><span class="circle_icon"></span>运营年报</h3>
            <div class="report-detail-wrapper">暂无数据</div>
        </div>
        <div class="quarter-report-operation">
            <h3 class="total-view"><span class="circle_icon"></span>运营季报</h3>
            <div class="report-detail-wrapper">暂无数据</div>
        </div>
        <div class="month-report-operation">
            <h3 class="total-view"><span class="circle_icon"></span>运营月报</h3>
            <div class="report-detail-wrapper">暂无数据</div>
        </div>

        <#if investDetailList??>
            <#list investDetailList as investDetailItem>
                <div style="display: none" id="investItem${investDetailItem_index}" data-day="${investDetailItem.productName!}" data-amount="${investDetailItem.totalInvestAmount!}" data-count="${(investDetailItem.countInvest?string.computer)!}"></div>
            </#list>
        </#if>
    </div>
</@global.main>