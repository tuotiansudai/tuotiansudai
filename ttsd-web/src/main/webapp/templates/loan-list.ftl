<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_list}" activeNav="我要投资" activeLeftNav="" title="投资列表">
<div class="loan-list-content">
    <div class="bRadiusBox bg-w">
        <ul class="wrapper-list">
            <li>
                <span>项目类型: </span>
                <a <#if activityType??><#else>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}">全部</a>
                <a <#if activityType?? && activityType=="NEWBIE">class="active"</#if>
                   href="/loan-list?activityType=NEWBIE&<#if status??>status=${status}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-type="NEWBIE">新手专享</a>
                <a <#if activityType?? && activityType=="NORMAL">class="active"</#if>
                   href="/loan-list?activityType=NORMAL&<#if status??>status=${status}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-type="NORMAL">普通投资</a>
            </li>
            <li>
                <span>项目状态: </span>
                <a <#if status??><#else>class="active"</#if>
                   href="/loan-list?<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}">全部</a>
                <a <#if status?? && status =="RAISING">class="active"</#if>
                   href="/loan-list?status=RAISING&<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-status="RAISING">可投资</a>
                <a <#if status?? && status =="REPAYING">class="active"</#if>
                   href="/loan-list?status=REPAYING&<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-status="REPAYING">还款中</a>
                <a <#if status?? && status =="COMPLETE">class="active"</#if>
                   href="/loan-list?status=COMPLETE&<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-status="COMPLETE">还款完成</a>
                <a <#if status?? && status =="PREHEAT">class="active"</#if>
                   href="/loan-list?status=PREHEAT&<#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-status="PREHEAT">预热中</a>
            </li>
            <li>
                <span>借款期限:</span>
                <a <#if periodsStart == 0 && periodsEnd == 0>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>rateStart=${rateStart}&rateEnd=${rateEnd}">全部</a>
                <a <#if periodsStart == 0 && periodsEnd == 3>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=0&periodsEnd=3&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-month-start="0" data-month-end="3">0-3个月</a>
                <a <#if periodsStart == 4 && periodsEnd == 6>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=4&periodsEnd=6&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-month-start="4" data-month-end="6">4-6个月</a>
                <a <#if periodsStart == 7 && periodsEnd == 12>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=7&periodsEnd=12&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-month-start="7" data-month-end="12">7-12个月</a>
                <a <#if periodsStart == 12 && periodsEnd == 0>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=12&periodsEnd=0&rateStart=${rateStart}&rateEnd=${rateEnd}"
                   data-month-start="12" data-month-end="0">12个月以上 </a>
            </li>
            <li class="laster">
                <span> 年化收益:</span>
                <a <#if rateStart == 0 && rateEnd == 0>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}">全部</a>
                <a <#if rateStart == 0 && rateEnd == 0.14>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=0&rateEnd=0.14"
                   data-percent-start="0" data-percent-end="0.14">14%以内</a>
                <a <#if rateStart == 0.14 && rateEnd == 0.16>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=0.14&rateEnd=0.16"
                   data-percent-start="0.14" data-percent-end="0.16">14-16%</a>
                <a <#if rateStart == 0.16 && rateEnd == 0>class="active"</#if>
                   href="/loan-list?<#if status??>status=${status}&</#if><#if activityType??>activityType=${activityType}&</#if>periodsStart=${periodsStart}&periodsEnd=${periodsEnd}&rateStart=0.16&rateEnd=0"
                   data-percent-start="0.16" data-percent-end="0">16%以上 </a>
            </li>
        </ul>
    </div>
    <div class="loan-list-box">
        <ul>
            <#list loanListWebDtos as loanListWebDto>
                <li data-url="/loan/${(loanListWebDto.id?string('0'))!}" class="clearfix">
                    <#if loanListWebDto.activityType == 'NEWBIE'>

                        <span class="hot"></span>
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
                                <dd><em>${loanListWebDto.baseRateInteger}</em><i><#if loanListWebDto.baseRateFraction??>.<@percentFraction>${loanListWebDto.baseRateFraction}</@percentFraction></#if>
                                    <#if loanListWebDto.activityRateInteger??>+${loanListWebDto.activityRateInteger}</#if><#if loanListWebDto.activityRateFraction??>.<@percentFraction>${loanListWebDto.activityRateFraction}</@percentFraction></#if>%</i>
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
                    <#if loanListWebDto.status== 'RAISING'>
                        <div class="loan-process project-schedule">
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">${loanListWebDto.rateOfAdvance}%</span>
                            </div>
                            <div class="process-percent">
                                <div class="percent" style="width:${loanListWebDto.rateOfAdvance}%"></div>
                            </div>

                            <div class="rest-amount">
                                <span>可投金额: <i><@amount>${loanListWebDto.added}</@amount></i>元</span>
                                <a class="btn-invest btn-normal" href="">马上投资</a>
                            </div>


                        </div>
                    <#elseif loanListWebDto.status== 'PREHEAT'>
                        <div class="loan-process project-schedule">
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">0%</span>
                            </div>
                            <div class="process-percent">
                                <div class="percent" style="width: 0.0%;"></div>
                            </div>
                            <div class="rest-amount wait-invest">
                                <span>${loanListWebDto.added} 放标</span>
                                <a class="btn-wait-invest btn-normal" href="">预热中</a>
                            </div>
                        </div>
                    <#elseif loanListWebDto.status== 'RECHECK'>
                        <div class="loan-process project-schedule">
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">${loanListWebDto.rateOfAdvance}%</span>

                                <div class="process-percent finish-project">
                                    <div class="percent" style="width: ${loanListWebDto.rateOfAdvance}%;"></div>
                                </div>
                            </div>
                            <div class="rest-amount finish-invest">
                                <span>可投额度:<@amount>${loanListWebDto.added}</@amount>元</span>
                                <button class="btn-normal" disabled>已售罄</button>
                            </div>
                        </div>
                    <#elseif loanListWebDto.status== 'REPAYING'>
                        <div class="loan-process project-schedule">
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">100%</span>
                            </div>
                            <div class="process-percent">
                                <div class="percent" style="width: 100%;"></div>
                            </div>
                            <div class="rest-amount finish-invest">
                                <span>可投金额:<i><@amount>${loanListWebDto.loanAmount}</@amount></i> 元</span>
                                <button class="btn-normal" disabled>已售罄</button>
                            </div>
                        </div>
                    <#else>
                        <div class="loan-process project-schedule">
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">100%</span>
                            </div>
                            <div class="process-percent">
                                <div class="percent" style="width:100%;"></div>
                            </div>
                            <div class="rest-amount finish-invest">
                                <span>可投金额: <i><@amount>${loanListWebDto.loanAmount}</@amount></i>元</span>
                                <button class="btn-invest btn-normal" disabled>已售罄</button>
                            </div>
                        </div>
                    </#if>

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
