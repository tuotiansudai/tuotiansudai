<!DOCTYPE html>
<html>
<#assign loan = baseDto.data>
<#import "macro/global.ftl" as global>
<@global.head title="标的详情" pageCss="${css.loan_detail}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main">
    <div class="item-block bg-loan">
        <div class="news-share">
            <h2 class="hd">

            <#if loan.activityType == "NOVICE">
                <span class="hot"></span>
            </#if>
            ${loan.projectName}
                <input class="jq-loan-user" type="hidden" value="${loan.id}">
            </h2>

            <div class="chart-box">
                <div class="box">
                    <div class="bg"></div>
                    <div class="rount"></div>
                    <div class="bg2"></div>
                    <div class="rount2" style="display: none;"></div>
                    <span class="sub-percent">+${loan.activityRate}%</span>

                    <div id="num" class="num">${loan.basicRate}%</div>
                    <span class="title">年化收益率</span>
                </div>
            </div>
            <div class="chart-info">
                <p>已投：<span class="point">${loan.raiseCompletedRate?string("0.00")}%</span></p>

                <p>代理人： ${loan.agentLoginName}</p>

                <p>借款人：${loan.loanerLoginName}</p>
            <#if loan.type.getRepayTimeUnit() == "month">
                <p>项目期限：${loan.periods}天 </p>
            </#if>
            <#if loan.type.getRepayTimeUnit() == "day">
                <p>项目期限：${loan.periods}天 </p>
            </#if>
                <p>还款方式：${loan.type.getDescription()}</p>
                <a href="/pdf/loanAgreementSample.pdf" target="_Blank">借款协议样本</a>
            </div>
        </div>
        <div class="account-info">
        <#if loan.loanStatus == "RAISING">
            <div class="ttsd-tips">拓天速贷提醒您：理财非存款，投资需谨慎！</div>
            <div class="item-block">
                <span class="sub-hd">项目金额：</span>
                <span class="num"><i>${loan.loanAmount}</i>元</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">可投金额：</span>
                <span class="num">${loan.amountNeedRaised?string("0.00")}元</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">账户余额：</span>
                <span class="num"><i class="red">${loan.balance?string("0.00")}</i>元</span>
            </div>
            <div class="item-block clearfix">
                <input type="text" value="" class="text-input"/>
                <span class="bg-yellow">最大可投金额</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">预计总收益：</span>
                <span class="num">600000元</span>
            </div>
            <div class="item-block">
                <button class="btn-pay" type="button">马上投资</button>
            </div>
        </#if>
        <#if loan.loanStatus == "PREHEAT">
            <div class="ttsd-tips">拓天速贷提醒您：理财非存款，投资需谨慎！</div>
            <div class="item-block">
                <span class="sub-hd">项目金额：</span>
                <span class="num"><i>${loan.loanAmount}</i>元</span>
            </div>
            <div class="item-block">
                <span class="sub-hd">可投金额：</span>
                <span class="num">${loan.amountNeedRaised?string("0.00")}元</span>
            </div>

            <div class="item-block">
                <span class="sub-hd">账户余额：</span>
                <span class="num"><i class="red">${loan.balance?string("0.00")}</i>元</span>
            </div>
            <div class="item-block clearfix">
                <input type="text" value="" class="text-input"/>
                <span class="bg-yellow">最大可投金额</span>
            </div>
            <div class="item-block">
                <div class="time-item">
                    <strong id="minute_show">0分</strong>
                    <strong>：</strong>
                    <strong id="second_show">0秒</strong>
                    以后可投资
                </div>
            </div>
            <div class="item-block">
                <button class="btn-pay grey" type="button" disabled="disabled">预热中</button>
            </div>
        </#if>
        <#if loan.loanStatus == "REPAYING">
            <div class="item-block" style="text-align: center">
                <img src="/images/loan/repaying.png" width="200" height="200" alt=""/>
            </div>
            <div class="item-block">
                <span class="sub-hd">项目金额：</span>
                <span class="num"><i>${loan.loanAmount}</i>元</span>
            </div>
            <div class="item-block">
                <button class="btn-pay" type="button">查看其他项目</button>
            </div>
        </#if>
        <#if loan.loanStatus == "RECHECK">

            <div class="item-block" style="text-align: center">
                <img src="/images/loan/recheck.png" width="200" height="200" alt=""/>
            </div>
            <div class="item-block">
                <span class="sub-hd">项目金额：</span>
                <span class="num"><i>${loan.loanAmount}</i>元</span>
            </div>
            <div class="item-block">
                <button class="btn-pay" type="button">查看其他项目</button>
            </div>
        </#if>
        <#if loan.loanStatus == "CANCEL">
            <div class="item-block" style="text-align: center">
                <img src="/images/loan/cancel.png" width="200" height="200" alt=""/>
            </div>
            <div class="item-block">
                <span class="sub-hd">项目金额：</span>
                <span class="num"><i>${loan.loanAmount}</i>元</span>
            </div>
            <div class="item-block">
                <button class="btn-pay" type="button">查看其他项目</button>
            </div>
        </#if>
        <#if loan.loanStatus == "OVERDUE">
            <div class="item-block" style="text-align: center">
                <img src="/images/loan/overdue.png" width="200" height="200" alt=""/>
            </div>
            <div class="item-block">
                <span class="sub-hd">项目金额：</span>
                <span class="num"><i>${loan.loanAmount}</i>元</span>
            </div>
            <div class="item-block">
                <button class="btn-pay" type="button">查看其他项目</button>
            </div>
        </#if>
        <#if loan.loanStatus == "COMPLETE">
            <div class="item-block" style="text-align: center">
                <img src="/images/loan/complete.png" width="200" height="200" alt=""/>
            </div>
            <div class="item-block">
                <span class="sub-hd">项目金额：</span>
                <span class="num"><i>${loan.loanAmount}</i>元</span>
            </div>
            <div class="item-block">
                <button class="btn-pay" type="button">查看其他项目</button>
            </div>
        </#if>
        </div>
    </div>

    <div class="item-block bg-loan loan-box">
        <div class="nav">
            <ul>
                <li class="current"><span>借款详情</span></li>
                <li><span>出借纪录</span></li>
            </ul>
        </div>
        <div class="loan-list">
            <div class="loan-list-con" style="display: block;">
                <div class="loan-detail">
                    <h3>借款详情：</h3>
                ${loan.descriptionText}
                </div>

                <div class="loan-material">
                    <h3>申请材料：</h3>

                    <div class="pic-list">
                    <#list loan.loanTitleDto as loanTitle>
                        <div class="title">${loanTitle.title}：</div>

                        <ul class="img-list">
                            <#list loan.loanTitles as loanTitleRelation >
                                <#if loanTitle.id == loanTitleRelation.titleId>
                                    <li><img src="${loanTitleRelation.applyMetarialUrl}" alt="${loanTitle.title}"/></li>
                                </#if>
                            </#list>
                        </ul>


                    </#list>
                    </div>
                </div>
            </div>
            <div class="loan-list-con">
                <table class="table-list">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>出借人</th>
                        <th>出借金额（元）</th>
                        <th>出借方式</th>
                        <th>预期利息（元）</th>
                        <th>出借时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list loan.basePaginationDto.recordDtoList as investPaginationDataDto>
                    <tr>
                        <td>${investPaginationDataDto.serialNo}</td>
                        <td>${investPaginationDataDto.loginName}</td>
                        <td>${investPaginationDataDto.amount}</td>
                        <td>
                            <#if investPaginationDataDto.autoInvest>
                                自动
                                <#if investPaginationDataDto.source=="IOS">
                                    <span class="icon-loan-ios"></span>
                                <#elseif investPaginationDataDto.source=="ANDROID">
                                    <span class="icon-loan-Android"></span>
                                <#else>
                                    <span class="icon-loan-ie"></span>
                                </#if>
                            </#if>
                            <#if !investPaginationDataDto.autoInvest>
                                手动
                                <#if investPaginationDataDto.source=="IOS">
                                    <span class="icon-loan-ios"></span>
                                <#elseif investPaginationDataDto.source=="ANDROID">
                                    <span class="icon-loan-Android"></span>
                                <#else>
                                    <span class="icon-loan-ie"></span>
                                </#if>
                            </#if>

                        </td>
                        <td>${investPaginationDataDto.expectedRate}</td>
                        <td>${investPaginationDataDto.createdTime}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

                <div class="pagination"></div>
            </div>

        </div>
    </div>
</div>


<#--弹出层-->
<div class="layer-box">
    <div class="layer-wrapper"></div>
    <div class="content">
        <img src="images/house-1.jpg" alt="" width="760" height="450"/>
    </div>
</div>
<#--弹出层-->
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.loan_detail}">
</@global.javascript>
</body>
</html>
<script>
    var intDiff = parseInt(${loan.preheatSeconds});//倒计时总秒数量
    var java_point = ${loan.amountNeedRaised}; //后台传递数据
    var pageTotal = '${loan.basePaginationDto.totalCount}';
    var pageCurrent = '${loan.basePaginationDto.index}';
</script>