<#import "../macro/global.ftl" as global>
    <@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="关于我们" activeLeftNav="运营数据" title="运营数据_拓天速贷地址_拓天速贷联系方式" keywords="拓天速贷地址,拓天速贷电话,拓天速贷公司" description="拓天速贷中国领先互联网金融P2P投资平台,提供网络理财,小额理财,短期理财,个人理财,公司理财服务,获得高年收益率回报,超低门槛,超高收益.">
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
        <div class="data-model">
            <h3>平台累计投资</h3>
            <div class="model-container" id="dataRecord" style="width:765px;height:250px;">
                
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