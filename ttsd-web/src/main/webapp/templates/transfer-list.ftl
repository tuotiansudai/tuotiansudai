<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer_list}" pageJavascript="${js.transfer_list}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="transfer-list-content">
    <ul class="project-type">
        <li><a href="/loan-list">直投项目</a></li>
        <li class="active"><a href="/transfer-list">转让项目</a></li>
    </ul>
    <div class="filter-list clearfix">
        <ul class="wrapper-list">
            <li>
                <span>项目状态: </span>
                <#assign statusUrl = "/transfer-list?transferStatus={transferStatus}&rateStart=${rateStart!}&rateEnd=${rateEnd!}">
                <#assign statusMap = {"":"全部","TRANSFERRING":"转让中","SUCCESS":"已完成"}>
                <#assign statusKeys = statusMap?keys>
                <#list statusKeys as key>
                    <a <#if transferStatus?? && transferStatus == key>class="active"
                       <#elseif !(transferStatus??) && key=="">class="active"</#if>
                       href=${statusUrl?replace("{transferStatus}",key)}>${statusMap[key]}</a>
                </#list>
            </li>
            <li>
                <span>年化收益: </span>
                <#assign rateUrl = "/transfer-list?transferStatus=${transferStatus!}&{rateType}">
                <#assign rateMap = {"":"全部","rateStart=0.1&rateEnd=0.12":"10-12%","rateStart=0.12&rateEnd=0.14":"12-14%","rateStart=0.14&rateEnd=0":"14%以上"}>
                <#assign rateKeys = rateMap?keys>
                <#list rateKeys as key>
                    <a <#if rateStart == 0 && rateEnd == 0 && key=="">class="active"
                       <#elseif rateStart == 0.1 && rateEnd == 0.12 && rateMap[key]=="10-12%">class="active"
                       <#elseif rateStart == 0.12 && rateEnd == 0.14 && rateMap[key]=="12-14%">class="active"
                       <#elseif rateStart == 0.14 && rateEnd == 0 && rateMap[key]=="14%以上">class="active"
                    </#if>
                       href=${rateUrl?replace("{rateType}",key)}>${rateMap[key]}</a>
                </#list>
            </li>
        </ul>
    </div>
    <div class="transfer-list-box">
        <ul>
            <#if transferApplicationItemList??>
                <#list transferApplicationItemList as transferApplicationItem>
                    <li data-url="#" class="clearfix">
                        <div class="transfer-info-frame fl">
                            <div class="transfer-top">
                                <span class="l-title fl">${transferApplicationItem.transferName}</span>
                            </div>
                            <div class="transfer-info-dl clearfix">
                                <dl>
                                    <dt>转让价格</dt>
                                    <dd><em>${transferApplicationItem.transferAmount}</em>元
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>代收本金</dt>
                                    <dd><em>${transferApplicationItem.investAmount}</em>元</dd>
                                </dl>
                                <dl>
                                    <dt>年化收益</dt>
                                    <dd><em>${transferApplicationItem.baseRate}%</em></dd>
                                </dl>
                                <dl>
                                    <dt>剩余期数</dt>
                                    <dd><em>${transferApplicationItem.leftPeriod}</em></dd>
                                </dl>
                            </div>
                        </div>
                        <div class="transfer-right">
                            <div class="transfer-time">
                                <span>截止时间：${transferApplicationItem.deadLine?string("yyyy-MM-dd HH:mm:ss")}</span>
                            </div>
                            <div class="rest-amount">
                                <i class="btn-invest btn-normal">马上投资</i>
                            </div>
                        </div>
                    </li>
                </#list>
            </#if>
        </ul>
        <div class="pagination">
            <span class="total">共 <span class="subTotal">${count}</span> 条记录，当前第 <span class="index-page">${index}</span> 页</span>
            <span class="prev page-list <#if hasPreviousPage>active</#if>"
                  data-url="/transfer-list?transferStatus=${transferStatus!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}&index=${index - 1}">上一页</span>
            <span class="next page-list <#if hasNextPage>active</#if>"
                  data-url="/transfer-list?transferStatus=${transferStatus!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}&index=${index + 1}">下一页</span>
        </div>
    </div>
    <#include "coupon-alert.ftl" />
</div>
    <#include "red-envelope-float.ftl" />
</@global.main>