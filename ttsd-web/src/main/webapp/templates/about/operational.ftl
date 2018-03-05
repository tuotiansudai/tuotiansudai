<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="信息披露" activeLeftNav="运营数据" title="运营数据_信息安全数据_拓天速贷" keywords="安全信息,安全平台,数据信息,信息披露,拓天速贷" description="拓天速贷运营数据全景展示,平台投资明细、注册投资用户、累计投资金额及平台数据总览,为您提供安全投资的平台运营数据.">
    <div class="about-us-container">
        <h2 class="column-title"><em>运营数据</em></h2>
        <div class="operater-days-wrap section-wrap">
            <div class="operater-day clearfix">
                <span class="assurance">安全运营</span><span class="data-bg">4</span><span>年</span><span class="data-bg">3</span><span class="data-bg">3</span><span class="data-bg">3</span><span>天</span>
            </div>
        </div>
        <div class="data-model">
            <h3>平台数据总览 <span class="font-right">（数据截止到2018年02月28日）</span> </h3>
            <div class="data-wrap">
                <ul class="clearfix">
                    <li>
                        <p><em id="tradeAmount"></em>元</p>
                        <p>累计交易金额</p>
                    </li>
                    <li class="bl">
                        <p><em>28,652</em>笔</p>
                        <p>累计交易笔数</p>
                    </li>
                    <li class="pt">
                        <p><em id="usersCount"></em>人</p>
                        <p>注册投资用户数</p>
                    </li>
                    <li class="bl pt">
                        <p><em>11,081,967.03</em>元</p>
                        <p>累计为用户赚取</p>
                    </li>
                </ul>
            </div>
        </div>
        <div class="data-model cheat-model">
            <h3 class="font16">平台投资明细</h3>
            <h4 class="font14">月度交易金额（近半年）</h4>
            <div class="model-container chart-dom" id="dataRecord">
                <!--[if gte IE 8]>
                请使用更高版本浏览器查看
                <![endif]-->
            </div>
        </div>
        <div class="invest-wrap clearfix">
            <div class="data-model cheat-model">
                <h4 class="font14">借款人基本信息</h4>
                <div class="model-container" id="loanBaseRecord">
                    <!--[if gte IE 8]>
                    请使用更高版本浏览器查看
                    <![endif]-->
                </div>
            </div>
            <div class="data-model cheat-model" style="margin-top: 85px;">
               
                <div class="model-container" id="loanBaseSexRecord">
                    <!--[if gte IE 8]>
                    请使用更高版本浏览器查看
                    <![endif]-->
                </div>
            </div>
        </div>
        <div class="data-model region-wrap">
            <h3 class="font14">投资人地域分布</h3>
            <div class="model-container chart-dom" id="investRegionRecord">
                <!--[if gte IE 8]>
                请使用更高版本浏览器查看
                <![endif]-->
            </div>

        </div>
        <div class="invest-wrap clearfix">
            <div class="data-model cheat-model">
                <h3 class="font16">平台用户统计</h3>
                <h4 class="font14">投资人基本信息</h4>
                <div class="model-container" id="investRecord">
                    <!--[if gte IE 8]>
                    请使用更高版本浏览器查看
                    <![endif]-->
                </div>
            </div>
            <div class="data-model cheat-model" style="margin-top: 85px;">

                <div class="model-container" id="investSexRecord">
                    <!--[if gte IE 8]>
                    请使用更高版本浏览器查看
                    <![endif]-->
                </div>
            </div>
        </div>
        <div class="data-model region-wrap">
            <h3 class="font14">借款人地域分布</h3>
            <div class="model-container chart-dom" id="loanRegionRecord">
                <!--[if gte IE 8]>
                请使用更高版本浏览器查看
                <![endif]-->
            </div>

        </div>


    </div>
</@global.main>