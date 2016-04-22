<#import "macro/global.ftl" as global>
<#switch productType!"">
    <#case 'SYL'>
        <#assign title="速盈利_拓天产品_拓天速贷">
        <#assign keywords="拓天速贷,拓天速盈利,拓天理财">
        <#assign description="拓天速贷金融精英为您推荐拓天速贷快速理财产品“速盈利”先付利息后还本金,按天计息,放款后生息.">
        <#break>
    <#case 'WYX'>
        <#assign title="稳盈绣_个人资金周转_拓天速贷">
        <#assign keywords="拓天速贷,拓天稳盈绣,资金周转,稳定收益">
        <#assign description="拓天速贷P2P金融信息服务平台为您提供个人资金周转,让您获得稳定收益的投资理财产品.">
        <#break>
    <#case 'JYF'>
        <#assign title="久盈富_个人资金借款_拓天速贷">
        <#assign keywords="拓天速贷,拓天久盈富,拓天高收益,个人借款">
        <#assign description="拓天速贷个人借款投资产品,年化利率13%起,高收益,高效率,低风险.">
        <#break>
    <#default>
        <#assign title="投资列表_拓天投资_拓天速贷">
        <#assign keywords="拓天速贷,拓天产品,速盈利,稳盈绣,久盈富">
        <#assign description="拓天速贷为您提供准确及时的P2P投资项目.投资用户通过拓天速贷平台进行准确投标的方式进行投资,让您的收益速、稳、高.">
</#switch>

<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_list}" activeNav="我要投资" activeLeftNav="" title="${title!}" keywords="${keywords!}" description="${description!}">
<div class="loan-list-content">
    <div class="bRadiusBox bg-w">
        <ul class="wrapper-list">
            <li>
                <span>项目类型: </span>
                <#assign productUrl = "/loan-list?productType={productType}&status=${status!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}">
                <#assign productMap = {"":"全部","SYL":"速盈利","WYX":"稳盈绣","JYF":"久盈富"}>
                <#assign productKeys = productMap?keys>
                <#list productKeys as key>
                    <a <#if productType?? && productType == key>class="active"
                       <#elseif !(productType??) && key=="">class="active"</#if>
                       href=${productUrl?replace("{productType}",key)}>${productMap[key]}</a>
                </#list>
            </li>
            <li>
                <span>项目状态: </span>
                <#assign statusUrl = "/loan-list?status={status}&productType=${productType!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}">
                <#assign statusMap = {"":"全部","RAISING":"可投资","REPAYING":"还款中","COMPLETE":"还款完成","PREHEAT":"预热中"}>
                <#assign statusKeys = statusMap?keys>
                <#list statusKeys as key>
                    <a <#if status?? && status == key>class="active"
                       <#elseif !(status??) && key=="">class="active"</#if>
                       href=${statusUrl?replace("{status}",key)}>${statusMap[key]}</a>
                </#list>
            </li>
            <li class>
                <span>年化收益: </span>
                <#assign rateUrl = "/loan-list?{rateType}&status=${status!}&productType=${productType!}">
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
    <div class="loan-list-box">
        <ul>
            <#list loanItemList as loanItem>
                <li data-url="/loan/${(loanItem.id?string.computer)!}" class="clearfix">
                    <#if loanItem.productType??>
                        <span class="${loanItem.productType?lower_case}"></span>
                    </#if>
                    <div class="loan-info-frame fl">
                        <div class="loan-top">
                            <span class="l-title fl">${loanItem.name}</span>
                            <span class="l-way fr">${loanItem.type.getName()}</span>
                        </div>
                        <div class="loan-info-dl">
                            <dl>
                                <dt>年化收益</dt>
                                <dd><em><@percentInteger>${loanItem.baseRate}</@percentInteger></em>
                                    <i><@percentFraction>${loanItem.baseRate}</@percentFraction>
                                        <#if (loanItem.activityRate > 0)>
                                            +<@percentInteger>${loanItem.activityRate}</@percentInteger><@percentFraction>${loanItem.activityRate}</@percentFraction>
                                        </#if>%
                                    </i>
                                </dd>
                            </dl>

                            <dl>
                                <dt>项目期限</dt>
                                <dd><em>${loanItem.periods}</em>
                                    <#if loanItem.type == 'INVEST_INTEREST_MONTHLY_REPAY' || loanItem.type = 'LOAN_INTEREST_MONTHLY_REPAY'>
                                        个月
                                    <#else>
                                        天
                                    </#if>
                                </dd>
                            </dl>
                            <dl>
                                <dt>招募金额</dt>
                                <dd><em><@amount>${loanItem.loanAmount?string.computer}</@amount></em>元</dd>
                            </dl>
                        </div>
                    </div>

                    <div class="loan-process project-schedule now-active">
                        <#if loanItem.status== 'PREHEAT'>
                            <div class="time-item preheat" data-time="${loanItem.preheatSeconds?string.computer}">
                                <#if loanItem.preheatSeconds lte 1800>
                                    <i class="time-clock" ></i><strong class="minute_show">00</strong><em>:</em><strong class="second_show">00</strong>以后可投资
                                <#else>
                                ${(loanItem.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                                </#if>
                            </div>
                            <div class="rest-amount wait-invest will">
                                <i class="btn-wait-invest btn-normal">预热中</i>
                            </div>

                            <div class="pro">
                                <div class="p-title">
                                    <span class="fl">项目进度</span>
                                    <span class="point fr">${loanItem.progress?string("0.00")} %</span>
                                </div>
                                <div class="process-percent">
                                    <div class="percent" style="width:${loanItem.progress}%"></div>
                                </div>
                                <div class="rest-amount">
                                    <span>可投额度：<i>${loanItem.alert}</i></span>
                                    <i class="btn-invest btn-normal">马上投资</i>
                                </div>
                            </div>
                        </#if>
                        <#if loanItem.status== 'RAISING'>
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">${loanItem.progress?string("0.00")} %</span>
                            </div>
                            <div class="process-percent">
                                <div class="percent" style="width:${loanItem.progress}%"></div>
                            </div>
                            <div class="rest-amount">
                                <span>可投额度：<i>${loanItem.alert}</i></span>
                                <i class="btn-invest btn-normal">马上投资</i>
                            </div>
                        </#if>
                        <#if ['RECHECK', 'REPAYING', 'OVERDUE', 'COMPLETE']?seq_contains(loanItem.status)>
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">${loanItem.progress?string("0.00")} %</span>
                            </div>
                            <div class="process-percent">
                                <div class="percent" style="width:${loanItem.progress}%"></div>
                            </div>
                            <div class="rest-amount finish-invest">
                                <span>${loanItem.alert}</span>
                                <button class="btn-normal" disabled>已售罄</button>
                            </div>
                        </#if>
                    </div>

                </li>

            </#list>
        </ul>
        <div class="pagination">
            <span class="total">共 <span class="subTotal">${count}</span> 条记录，当前第 <span class="index-page">${index}</span> 页</span>
            <span class="prev <#if hasPreviousPage>active</#if>"
                  data-url="/loan-list?status=${status!}&productType=${productType!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}&index=${index - 1}">上一页</span>
            <span class="next <#if hasNextPage>active</#if>"
                  data-url="/loan-list?status=${status!}&productType=${productType!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}&index=${index + 1}">下一页</span>
        </div>
    </div>
    <#include "coupon-alert.ftl" />
</div>
    <#include "red-envelope-float.ftl" />
</@global.main>
