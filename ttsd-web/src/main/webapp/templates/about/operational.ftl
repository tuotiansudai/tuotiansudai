<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="关于我们" activeLeftNav="运营数据" title="运营数据_信息安全数据_拓天速贷" keywords="安全信息,安全平台,数据信息,信息披露,拓天速贷" description="拓天速贷运营数据全景展示,平台投资明细、注册投资用户、累计投资金额及平台数据总览,为您提供安全投资的平台运营数据.">
    <div class="about-us-container">
        <h2 class="column-title"><em>运营数据</em></h2>
        <div class="data-model">
            <h3>平台数据总览</h3>
            <div class="model-container">
                <ul class="model-title">
                    <li class="model-one">
                        <p id="operationDays"></p>
                        <p>安全运营时间</p>
                    </li>
                    <li class="model-two">
                        <p id="usersCount"></p>
                        <p>注册投资用户</p>
                    </li>
                    <li class="model-three">
                        <p id="tradeAmount"></p>
                        <p>累计交易金额</p>
                    </li>
                </ul>
            </div>
        </div>
        <div class="data-model cheat-model">
            <h3>平台累计投资</h3>
            <div class="model-container chart-dom" id="dataRecord">
                
            </div>
        </div>
        <div class="data-model">
            <h3>平台投资明细</h3>
            <div class="model-container">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>类型</th>
                            <th>交易总额(元)</th>
                            <th>投资数(笔)</th>
                            <th>平均单笔交易额(元)</th>
                        </tr>
                    </thead>
                    <tbody>
                    <#if investDetailList??>
                        <#list investDetailList as investDetailItem>
                            <tr>
                                <td>${investDetailItem.productName!}天标的</td>
                                <td>${investDetailItem.totalInvestAmount!}</td>
                                <td>${investDetailItem.countInvest!}</td>
                                <td>${investDetailItem.avgInvestAmount!}</td>
                            </tr>
                        </#list>
                    </#if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</@global.main>