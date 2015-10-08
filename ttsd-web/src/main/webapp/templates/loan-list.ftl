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
                <a class="active" href="">全部</a>
                <a href="" data-type="NOVICE">新手专享</a>
                <a href="" data-type="NORMAL">普通投资</a>
            </div>
            <div class="item-block">
                <span class="hd">项目状态: </span>
                <a class="active" href="">全部</a>
                <a href="" data-status="RAISING">可投资</a>
                <a href="" data-status="REPAYING">还款中</a>
                <a href="" data-status="COMPLETE">还款完成</a>
                <a href="" data-status="PREHEAT">预热中</a>
            </div>
            <div class="item-block">
                <span class="hd">借款期限:</span>
                <a class="active" href="">全部</a>
                <a href="" data-month-start="0" data-month-end="3">0-3个月</a>
                <a href="" data-month-start="4" data-month-end="6">4-6个月</a>
                <a href="" data-month-start="7" data-month-end="12">7-12个月</a>
                <a href="" data-month-start="12" data-month-end="0">12个月以上 </a>
            </div>
            <div class="item-block laster">
                <span class="hd"> 年化收益:</span>
                <a class="active" href="">全部</a>
                <a href="" data-percent-start="0" data-percent-end="0.14">14%以内</a>
                <a href="" data-percent-start="0.14" data-percent-end="0.16">14-16%</a>
                <a href="" data-percent-start="0.16" data-percent-end="0">16%以上 </a>
            </div>
        </div>
    </div>
    <div class="wrapper loan-list">
        <ul>
            <#list loanListWebDtos as loanListWebDto>
                <li>
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
                            <span class="month">${loanListWebDto.periods}个月</span>
                            <span class="name">还款方式：</span>
                            <span class="money-style">${loanListWebDto.type.getName()}</span>
                        </div>
                    </div>
                    <div class="loan-process">
                        <div class="process">
                            <span class="hd">正在招募</span>
                        <span class="process-percent" style="width:100px;">
                            <span class="percent"></span>
                        </span>
                            <span class="point">50%</span>
                        </div>
                        <div class="sub-item-block">
                            <p>招募金额: <span class="all-total">${loanListWebDto.loanAmount}</span>元</p>
                            <p>剩余金额: <span class="all-total">600000</span>元</p>
                        </div>
                    </div>
                </li>
            </#list>
                <li>
                    <span class="hot"></span>

                    <div class="loan-info">
                        <div class="hd">
                       <span class="sub-hd">
                           生意周转
                       </span>
                            <a class="btn-invest" href="">马上投资</a>
                        </div>
                        <div class="sub-item-block">
                            <span class="name">年化收益：</span>
                        <span class="num">8.0%
                            <i>+8.0%</i>
                        </span>
                            <span class="name">项目期限（月）：</span>
                            <span class="month">3个月</span>
                            <span class="name">还款方式：</span>
                            <span class="money-style">到期一次性还本付息</span>
                        </div>
                    </div>
                    <div class="loan-process">
                        <div class="process">
                            <span class="hd">正在招募</span>
                        <span class="process-percent">
                            <span class="percent"></span>
                        </span>
                            <span class="point">50%</span>
                        </div>
                        <div class="sub-item-block">
                            <p>招募金额: <span class="all-total">60000000</span>元</p>

                            <p>剩余金额: <span class="all-total">600000</span>元</p>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="loan-info">
                        <div class="hd">
                       <span class="sub-hd">
                           生意周转
                       </span>
                            <a class="btn-wait-invest" href="">预热中</a>
                        </div>
                        <div class="sub-item-block">
                            <span class="name">年化收益：</span>
                            <span class="num">8.0%</span>
                            <span class="name">项目期限（月）：</span>
                            <span class="month">3个月</span>
                            <span class="name">还款方式：</span>
                            <span class="money-style">到期一次性还本付息</span>
                        </div>
                    </div>
                    <div class="loan-process">
                        <div class="process">
                            <span class="hd">正在招募</span>
                        <span class="process-percent">
                            <span class="percent"></span>
                        </span>
                            <span class="point">50%</span>
                        </div>
                        <div class="sub-item-block">
                            <p>招募金额: <span class="all-total">60000000</span>元</p>

                            <p> 2015-08-24 15:00 放标</p>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="loan-info">
                        <div class="hd">
                       <span class="sub-hd">
                           生意周转
                       </span>
                            <a class="btn-finish-invest" href="">投资完成</a>
                        </div>
                        <div class="sub-item-block">
                            <span class="name">年化收益：</span>
                            <span class="num">8.0%</span>
                            <span class="name">项目期限（月）：</span>
                            <span class="month">3个月</span>
                            <span class="name">还款方式：</span>
                            <span class="money-style">到期一次性还本付息</span>
                        </div>
                    </div>
                    <div class="loan-process pr">
                        <span class="img"><img src="../../images/loan/pic-recheck.png" alt=""/></span>

                        <p class="status"><span class="grey">招募金额:</span>600000元</p>

                        <p class="status"><span class="grey">剩余金额:</span> 0元</p>
                    </div>
                </li>
                <li>
                    <div class="loan-info">
                        <div class="hd">
                       <span class="sub-hd">
                           生意周转
                       </span>
                            <a class="btn-finish-invest" href="">投资完成</a>
                        </div>
                        <div class="sub-item-block">
                            <span class="name">年化收益：</span>
                            <span class="num">8.0%</span>
                            <span class="name">项目期限（月）：</span>
                            <span class="month">3个月</span>
                            <span class="name">还款方式：</span>
                            <span class="money-style">到期一次性还本付息</span>
                        </div>
                    </div>
                    <div class="loan-process">
                        <span class="img"><img src="../../images/loan/pic-doing.png" alt=""/></span>
                        <p class="status"><span class="grey">招募金额:</span>600000元</p>
                        <p class="status"><span class="grey">回款进度:</span> 0元</p>
                    </div>
                </li>
                <li>
                    <div class="loan-info">
                        <div class="hd">
                       <span class="sub-hd">
                           生意周转
                       </span>
                            <a class="btn-finish-invest" href="">投资完成</a>
                        </div>
                        <div class="sub-item-block">
                            <span class="name">年化收益：</span>
                            <span class="num">8.0%</span>
                            <span class="name">项目期限（月）：</span>
                            <span class="month">3个月</span>
                            <span class="name">还款方式：</span>
                            <span class="money-style">到期一次性还本付息</span>
                        </div>
                    </div>
                    <div class="loan-process">
                        <span class="img"><img src="../../images/loan/pic-finish.png" alt=""/></span>
                        <p class="status"><span class="grey">招募金额:</span>600000元</p>
                        <p class="status"><span class="grey">回款进度:</span> 0元</p>
                    </div>
                </li>
        </ul>
        <div class="pagination">
            <span class="total">共 <span class="subTotal">${loanListCountWeb}</span>条,当前第 <span class="index-page">${currentPageNo}</span>页</span>
            <span class="prev">上一页</span>
            <a class="current" href="">1</a>
            <a href="">2</a>
            <a href="">20</a>
            <span class="next">下一页</span>
        </div>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.loan_list}">
</@global.javascript>
</body>
</html>