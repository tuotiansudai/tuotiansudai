<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="信息披露" activeLeftNav="运营数据" title="运营数据_信息安全数据_拓天速贷" keywords="安全信息,安全平台,数据信息,信息披露,拓天速贷" description="拓天速贷运营数据全景展示,平台投资明细、注册投资用户、累计投资金额及平台数据总览,为您提供安全投资的平台运营数据.">
    <div class="about-us-container operational-wrap">
        <h2 class="column-title"><em>运营数据</em></h2>
        <div class="operater-days-wrap section-wrap">
            <div class="operater-day clearfix" id="operationDays">
            </div>
        </div>
        <div class="data-model">
            <h3 class="total-view">平台数据总览 <span class="font-right" id="dateTime"></span> </h3>
            <div class="data-wrap">
                <ul class="clearfix">
                    <li>
                        <p><em id="tradeAmount"></em>元</p>
                        <p>累计交易金额</p>
                    </li>
                    <li>
                        <p><em id="total_trade_count"></em>笔</p>
                        <p>累计交易笔数</p>
                    </li>
                    <li>
                        <p><em id="usersCount"></em>人</p>
                        <p>注册投资用户数</p>
                    </li>
                    <li>
                        <p><em id="earn_total_amount"></em>元</p>
                        <p>累计为用户赚取</p>
                    </li>
                    <li>
                        <p><em id="earn_total_amount"></em>元</p>
                        <p>累计投资用户数</p>
                    </li>
                </ul>
            </div>
        </div>
        <div class="data-model cheat-model marginTop20">
            <h3 class="font16">平台投资明细</h3>
            <h4 class="font14 marginBottom10">月度交易金额（近半年）</h4>
            <div class="model-container chart-dom" id="dataRecord">
                <!--[if gte IE 8]>
                请使用更高版本浏览器查看
                <![endif]-->
            </div>
        </div>
        <h3 class="font16">平台用户统计</h3>
        <div class="invest-section">
            <div class="invest-wrap clearfix">
                <h4 class="font14 paddingLeft">投资人基本信息</h4>
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
                <h3 class="font14 paddingLeft">投资人地域分布</h3>
                <div class="model-container geographical-item" id="investRegionRecord">
                    <ul id="geographicalWrap">
                    </ul>
                </div>

            </div>
        </div>
        <div class="data-model">
            <h3 class="font16" >借款标的情况</h3>
            <div class="data-wrap">
                <span class="line"></span>
                <ul class="clearfix">
                    <li class="loan">
                        <p><em>3333，3333</em>元</p>
                        <p>累计借贷金额</p>
                    </li>
                    <li class="loan">
                        <p><em>3333，3333</em>笔</p>
                        <p>累计借贷笔数</p>
                    </li>
                </ul>
            </div>

        </div>

        <h3 class="font16 loan-title marginTop20">平台借款用户统计</h3>
        <div class="loan-section">
        <div class="invest-wrap clearfix">
            <h4 class="font14">借款人基本信息</h4>
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
            <h3 class="font14">借款人地域分布</h3>
            <div class="model-container geographical-item" id="loanRegionRecord">
                <ul id="geographicalWrapLoan">
                </ul>
            </div>

        </div>
        </div>


        <div class="data-model">
            <h3 class="font16">逾期情况</h3>
            <div class="data-wrap">
                <span class="line"></span>
                <ul class="clearfix">
                    <li class="loan">
                        <p><em>3333，3333</em>元</p>
                        <p>借款人平台逾期次数</p>
                    </li>
                    <li class="loan">
                        <p><em>3333，3333</em>笔</p>
                        <p>平台逾期总金额</p>
                    </li>
                </ul>
            </div>

        </div>

        <#if investDetailList??>
            <#list investDetailList as investDetailItem>
                <div style="display: none" id="investItem${investDetailItem_index}" data-day="${investDetailItem.productName!}" data-amount="${investDetailItem.totalInvestAmount!}" data-count="${(investDetailItem.countInvest?string.computer)!}"></div>
            </#list>
        </#if>
    </div>
</@global.main>