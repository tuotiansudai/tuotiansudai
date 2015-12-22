<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_list}" activeNav="我要投资" activeLeftNav="" title="投资列表">
<div class="loan-list-content">
    <div class="bRadiusBox bg-w">
        <ul class="wrapper-list">
            <li>
                <span>项目类型: </span>
                <#assign activityUrl = "/loan-list?activityType=activity_type&status=${status!}&periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}">
                <#assign activityMap = {"":"全部","NEWBIE":"新手专享","NORMAL":"普通投资"}>
                <#assign activityKeys = activityMap?keys>
                <#list activityKeys as key>
                    <a <#if activityType?? && activityType == key>class="active"
                       <#elseif !(activityType??) && key=="">class="active"</#if>
                            href=${activityUrl?replace("activity_type",key)}>${activityMap[key]}</a>
                </#list>
            </li>
            <li>
                <span>项目状态: </span>
                <#assign statusUrl = "/loan-list?status=status_type&activityType=${activityType!}&periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}">
                <#assign statusMap = {"":"全部","RAISING":"可投资","REPAYING":"还款中","COMPLETE":"还款完成","PREHEAT":"预热中"}>
                <#assign statusKeys = statusMap?keys>
                <#list statusKeys as key>
                    <a <#if status?? && status == key>class="active"
                       <#elseif !(status??) && key=="">class="active"</#if>
                            href=${statusUrl?replace("status_type",key)}>${statusMap[key]}</a>
                </#list>
            </li>
            <li>
                <span>借款期限:</span>
                <#assign periodsUrl = "/loan-list?periods_type&status=${status!}&activityType=${activityType!}&rateStart=${rateStart}&rateEnd=${rateEnd}">
                <#assign periodsMap = {"":"全部","periodsStart=0&periodsEnd=3":"0-3个月","periodsStart=4&periodsEnd=6":"4-6个月","periodsStart=7&periodsEnd=12":"7-12个月","periodsStart=12&periodsEnd=0":"12个月以上"}>
                <#assign periodsKeys = periodsMap?keys>
                <#list periodsKeys as key>
                    <a <#if periodsStart == 0 && periodsEnd == 0 && key=="">class="active"
                        <#elseif periodsStart == 0 && periodsEnd == 3 && periodsMap[key]=="0-3个月">class="active"
                        <#elseif periodsStart == 4 && periodsEnd == 6 && periodsMap[key]=="4-6个月">class="active"
                        <#elseif periodsStart == 7 && periodsEnd == 12 && periodsMap[key]=="7-12个月">class="active"
                        <#elseif periodsStart == 12 && periodsEnd == 0 && periodsMap[key]=="12个月以上">class="active"
                       </#if>
                        href=${periodsUrl?replace("periods_type",key)}>${periodsMap[key]}</a>
                </#list>
            </li>
            <li class="laster">
                <span> 年化收益:</span>
                <#assign rateUrl = "/loan-list?rate_type&status=${status!}&activityType=${activityType!}&periodsStart=${periodsStart}&periodsEnd=${periodsEnd}">
                <#assign rateMap = {"":"全部","rateStart=0&rateEnd=0.14":"14%以内","rateStart=0.14&rateEnd=0.16":"14-16%","rateStart=0.16&rateEnd=0":"16%以上"}>
                <#assign rateKeys = rateMap?keys>
                <#list rateKeys as key>
                    <a <#if rateStart == 0 && rateEnd == 0 && key=="">class="active"
                       <#elseif rateStart == 0 && rateEnd == 0.14 && rateMap[key]=="14%以内">class="active"
                       <#elseif rateStart == 0.14 && rateEnd == 0.16 && rateMap[key]=="14-16%">class="active"
                       <#elseif rateStart == 0.16 && rateEnd == 0 && rateMap[key]=="16%以上">class="active"
                       </#if>
                        href=${rateUrl?replace("rate_type",key)}>${rateMap[key]}</a>
                </#list>
            </li>
        </ul>
    </div>
    <div class="loan-list-box">
        <ul>
            <#list loanListWebDtos as loanListWebDto>
                <li data-url="/loan/${(loanListWebDto.id?string('0'))!}" class="clearfix">
                    <#if loanListWebDto.activityType == 'NEWBIE'>

                        <span class="hot"></span><!-- 其他产品类型class名为syl,wyx,jyf-->
                    </#if>
                    <div class="loan-info-frame fl">
                        <div class="loan-top">
                                <span class="l-title fl">
                                ${loanListWebDto.name}
                                </span>
                                <span class="l-way fr">
                                ${loanListWebDto.type.getName()}
                                </span>
                        </div>
                        <div class="loan-info-dl">
                            <dl>
                                <dt>年化收益</dt>
                                <dd><em><@percentInteger>${loanListWebDto.baseRate}</@percentInteger></em><i><@percentFraction>${loanListWebDto.baseRate}</@percentFraction>
                                    <#if (loanListWebDto.activityRate>0) >+<@percentInteger>${loanListWebDto.activityRate}</@percentInteger><@percentFraction>${loanListWebDto.activityRate}</@percentFraction></#if>%</i>
                                </dd>
                            </dl>

                            <dl>
                                <dt>项目期限</dt>
                                <dd><em>${loanListWebDto.periods}</em><#if loanListWebDto.type == 'INVEST_INTEREST_MONTHLY_REPAY' || loanListWebDto.type = 'LOAN_INTEREST_MONTHLY_REPAY'>个月<#else>
                                    天</#if></dd>
                            </dl>
                            <dl>
                                <dt>招募金额</dt>
                                <dd><em><@amount>${loanListWebDto.loanAmount}</@amount></em>元</dd>
                            </dl>
                        </div>
                    </div>

                    <div class="loan-process project-schedule">
                        <div class="p-title">
                            <span class="fl">项目进度</span>
                            <span class="point fr"><#if loanListWebDto.rateOfAdvance??>${loanListWebDto.rateOfAdvance}<#else>0.0</#if>%</span>
                        </div>
                        <div class="process-percent">
                            <div class="percent" style="width:<#if loanListWebDto.rateOfAdvance??>${loanListWebDto.rateOfAdvance}<#else>0.0</#if>%"></div>
                        </div>
                        <#if loanListWebDto.status== 'RAISING'>
                            <div class="rest-amount">
                                <span>可投金额: <i>${loanListWebDto.added}</i>元</span>
                                <a class="btn-invest btn-normal" href="">马上投资</a>
                            </div>
                        <#elseif loanListWebDto.status== 'PREHEAT'>
                            <div class="rest-amount wait-invest">
                                <span>${loanListWebDto.added} 放标</span>
                                <a class="btn-wait-invest btn-normal" href="">预热中</a>
                            </div>
                        <#else>
                            <div class="rest-amount finish-invest">
                                <#if loanListWebDto.status == 'RECHECK'>
                                    <span>可投额度:${loanListWebDto.added}元</span>
                                <#else>
                                    <span>还款进度:${loanListWebDto.added}期</span>
                                </#if>
                                <button class="btn-normal" disabled>已售罄</button>
                            </div>
                        </#if>
                    </div>

                </li>

            </#list>
        </ul>
        <div class="pagination">
            <span class="total">共 <span class="subTotal">${loanListCountWeb}</span> 条记录，当前第 <span class="index-page">${currentPageNo}</span> 页</span>
            <span class="prev <#if hasPreviousPage>active</#if>"
                  data-url="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}&currentPageNo=${currentPageNo-1}">上一页</span>
            <span class="next <#if hasNextPage>active</#if>"
                  data-url="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}&currentPageNo=${currentPageNo+1}">下一页</span>
        </div>
    </div>
</div>
</@global.main>
