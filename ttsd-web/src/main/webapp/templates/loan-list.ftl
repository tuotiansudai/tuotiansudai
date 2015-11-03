<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="标的列表" pageCss="${css.loan_list}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main">
    <div class="wrapper-all bg">
        <div class="wrapper pr">
            <div class="loan-tags">投资项目</div>
            <div class="item-block">
                <span class="hd">项目类型: </span>
                <a <#if activityType??><#else>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}">全部</a>
                <a <#if activityType?? && activityType=="NOVICE">class="active"</#if> href="/loanList/web?activityType=NOVICE&<#if status??>status=${status}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}" data-type="NOVICE">新手专享</a>
                <a <#if activityType?? && activityType=="NORMAL">class="active"</#if> href="/loanList/web?activityType=NORMAL&<#if status??>status=${status}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}" data-type="NORMAL">普通投资</a>
            </div>
            <div class="item-block">
                <span class="hd">项目状态: </span>
                <a <#if status??><#else>class="active"</#if> href="/loanList/web?<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}">全部</a>
                <a <#if status?? && status =="RAISING">class="active"</#if> href="/loanList/web?status=RAISING&<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}" data-status="RAISING">可投资</a>
                <a <#if status?? && status =="REPAYING">class="active"</#if> href="/loanList/web?status=REPAYING&<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}" data-status="REPAYING">还款中</a>
                <a <#if status?? && status =="COMPLETE">class="active"</#if> href="/loanList/web?status=COMPLETE&<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}" data-status="COMPLETE">还款完成</a>
                <a <#if status?? && status =="PREHEAT">class="active"</#if> href="/loanList/web?status=PREHEAT&<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}" data-status="PREHEAT">预热中</a>
            </div>
            <div class="item-block">
                <span class="hd">借款期限:</span>
                <a <#if periodsStart == 0 && periodsEnd == 0>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>rateStart=${rateStart}&rateEnd=${rateEnd}">全部</a>
                <a <#if periodsStart == 0 && periodsEnd == 3>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=0&periodsEnd=3&rateStart=${rateStart}&rateEnd=${rateEnd}" data-month-start="0" data-month-end="3">0-3个月</a>
                <a <#if periodsStart == 4 && periodsEnd == 6>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=4&periodsEnd=6&rateStart=${rateStart}&rateEnd=${rateEnd}" data-month-start="4" data-month-end="6">4-6个月</a>
                <a <#if periodsStart == 7 && periodsEnd == 12>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=7&periodsEnd=12&rateStart=${rateStart}&rateEnd=${rateEnd}" data-month-start="7" data-month-end="12">7-12个月</a>
                <a <#if periodsStart == 12 && periodsEnd == 0>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=12&periodsEnd=0&rateStart=${rateStart}&rateEnd=${rateEnd}" data-month-start="12" data-month-end="0">12个月以上 </a>
            </div>
            <div class="item-block laster">
                <span class="hd"> 年化收益:</span>
                <a <#if rateStart == 0 && rateEnd == 0>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}">全部</a>
                <a <#if rateStart == 0 && rateEnd == 0.14>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=0&rateEnd=0.14" data-percent-start="0" data-percent-end="0.14">14%以内</a>
                <a <#if rateStart == 0.14 && rateEnd == 0.16>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=0.14&rateEnd=0.16" data-percent-start="0.14" data-percent-end="0.16">14-16%</a>
                <a <#if rateStart == 0.16 && rateEnd == 0>class="active"</#if> href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=0.16&rateEnd=0" data-percent-start="0.16" data-percent-end="0">16%以上 </a>
            </div>
        </div>
    </div>
    <div class="wrapper loan-list">
        <ul>
            <#list loanListWebDtos as loanListWebDto>
                <li urlLink="/loan/${(loanListWebDto.id?string('0'))!}">
                    <#if loanListWebDto.activityType == 'NOVICE'>
                        <span class="hot"></span>
                    </#if>
                    <div class="loan-info">
                        <div class="hd">
                            <span class="sub-hd">
                               <td>${loanListWebDto.name}</td>
                            </span>
                            <#if loanListWebDto.status== 'RAISING'>
                                <a class="btn-invest" href="">马上投资</a>
                            <#elseif loanListWebDto.status== 'PREHEAT'>
                                <a class="btn-wait-invest" href="">预热中</a>
                            <#else>
                                <a class="btn-finish-invest" href="">投资完成</a>
                            </#if>
                        </div>
                        <div class="sub-item-block">
                            <span class="name">年化收益：</span>
                            <span class="num">
                                <td>${loanListWebDto.basicRate}</td>
                                <#if loanListWebDto.activityRate != '0.0%'>
                                    <i>+${loanListWebDto.activityRate}</i>
                                </#if>
                            </span>
                            <span class="name">项目期限（月）：</span>
                            <span class="month">${loanListWebDto.periods}个月</span> <br/>
                            <span class="name">还款方式：</span>
                            <span class="money-style">${loanListWebDto.type.getName()}</span>
                        </div>
                    </div>
                    <#if loanListWebDto.status== 'RAISING'>
                        <div class="loan-process">
                            <div class="process">
                                <span class="hd">正在招募</span>
                        <span class="process-percent" style="width:${loanListWebDto.rateOfAdvance}px;">
                            <span class="percent"></span>
                        </span>
                                <span class="point">${loanListWebDto.rateOfAdvance}%</span>
                            </div>
                            <div class="sub-item-block">
                                <p>招募金额: <span class="all-total">${loanListWebDto.loanAmount}</span>元</p>
                                <p>剩余金额: <span class="all-total">${loanListWebDto.added}</span>元</p>
                            </div>
                        </div>
                    <#elseif loanListWebDto.status== 'PREHEAT'>
                        <div class="loan-process">
                            <div class="process">
                                <span class="hd">正在招募</span>
                        <span class="process-percent" style="width:0px;">
                            <span class="percent"></span>
                        </span>
                                <span class="point">0%</span>
                            </div>
                            <div class="sub-item-block">
                                <p>招募金额: <span class="all-total">${loanListWebDto.loanAmount}</span>元</p>
                                <p>${loanListWebDto.added} 放标</p>
                            </div>
                        </div>
                    <#elseif loanListWebDto.status== 'RECHECK'>
                        <div class="loan-process pr">
                            <span class="img"><img src="../../images/loan/pic-recheck.png" alt=""/></span>
                            <p class="status"><span class="grey">招募金额:</span>${loanListWebDto.loanAmount}元</p>
                            <p class="status"><span class="grey">剩余金额:</span>${loanListWebDto.added}元</p>
                        </div>
                    <#elseif loanListWebDto.status== 'REPAYING'>
                        <div class="loan-process">
                            <span class="img"><img src="../../images/loan/pic-doing.png" alt=""/></span>
                            <p class="status"><span class="grey">招募金额:</span>${loanListWebDto.loanAmount}元</p>
                            <p class="status"><span class="grey">回款进度:</span>${loanListWebDto.added}元</p>
                        </div>
                    <#else>
                        <div class="loan-process">
                            <span class="img"><img src="../../images/loan/pic-finish.png" alt=""/></span>
                            <p class="status"><span class="grey">招募金额:</span>${loanListWebDto.loanAmount}元</p>
                            <p class="status"><span class="grey">回款进度:</span>${loanListWebDto.added}元</p>
                        </div>
                    </#if>

                </li>

            </#list>
        </ul>
        <div class="pagination">
            <span class="total">共<span class="subTotal">${loanListCountWeb}</span>条,当前第 <span class="index-page">${currentPageNo}</span>页</span>
            <#if hasPreviousPage>
                <span class="prev"><a href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}&currentPageNo=${currentPageNo+1}">上一页</a></span>
            </#if>
            <a class="current">${currentPageNo}</a>
            <#if hasNextPage>
                <span class="next"><a href="/loanList/web?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}&currentPageNo=${currentPageNo-1}">下一页</a></span>
            </#if>

        </div>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.loan_list}">
</@global.javascript>
</body>
</html>