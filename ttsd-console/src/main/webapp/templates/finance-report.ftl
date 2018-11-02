<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="finance-report.js" headLab="finance-manage" sideLab="financeReport" title="债权财务数据">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/finance-manage/financeReport" method="get" class="form-inline query-form">
        <div class="form-group">
            <label>项目编号</label>
            <input type="text" class="form-control" name="loanId" placeholder="" value="${(loanId?c)!}">
        </div>
        <div class="form-group">
            <label>期限</label>
            <select class="selectpicker" name="period">
                <option value="" <#if !(selectedPeriod??)>selected</#if>>全部</option>
                <option value="1" <#if selectedPeriod?? && 1==selectedPeriod>selected</#if>>第1期</option>
                <option value="2" <#if selectedPeriod?? && 2==selectedPeriod>selected</#if>>第2期</option>
                <option value="3" <#if selectedPeriod?? && 3==selectedPeriod>selected</#if>>第3期</option>
                <option value="4" <#if selectedPeriod?? && 4==selectedPeriod>selected</#if>>第4期</option>
                <option value="5" <#if selectedPeriod?? && 5==selectedPeriod>selected</#if>>第5期</option>
                <option value="6" <#if selectedPeriod?? && 6==selectedPeriod>selected</#if>>第6期</option>
                <option value="7" <#if selectedPeriod?? && 7==selectedPeriod>selected</#if>>第7期</option>
                <option value="8" <#if selectedPeriod?? && 8==selectedPeriod>selected</#if>>第8期</option>
                <option value="9" <#if selectedPeriod?? && 9==selectedPeriod>selected</#if>>第9期</option>
                <option value="10" <#if selectedPeriod?? && 10==selectedPeriod>selected</#if>>第10期</option>
                <option value="11" <#if selectedPeriod?? && 11==selectedPeriod>selected</#if>>第11期</option>
                <option value="12" <#if selectedPeriod?? && 12==selectedPeriod>selected</#if>>第12期</option>
            </select>
        </div>
        <div class="form-group">
            <label>投资人</label>
            <input type="text" id="tags" name="investLoginName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${investLoginName!}"/>
        </div>
        <div class="form-group">
            <label>投资时间</label>

            <div class='input-group date' id='datetimepickerStartTime'>
                <input type='text' class="form-control" name="investStartTime"
                       value="${(investStartTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            <span>-</span>

            <div class='input-group date' id='datetimepickerEndTime'>
                <input type='text' class="form-control" name="investEndTime"
                       value="${(investEndTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>
        <div class="form-group">
            <label>回款时间</label>

            <div class='input-group date' id='repayDatetimepickerStartTime'>
                <input type='text' class="form-control" name="repayStartTime"
                       value="${(repayStartTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            <span>-</span>

            <div class='input-group date' id='repayDatetimepickerEndTime'>
                <input type='text' class="form-control" name="repayEndTime"
                       value="${(repayEndTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>
        <div class="form-group">
            <label>使用优惠类型</label>
            <select class="selectpicker" name="usedPreferenceType">
                <option value="">全部</option>
                <#list preferenceTypes as preferenceType>
                    <option value="${preferenceType.name()}"
                            <#if (selectedPreferenceType?has_content && preferenceType == selectedPreferenceType.name()) >selected</#if>
                            >${preferenceType.getDescription()}</option>
                </#list>
            </select>
        </div>
        <button type="submit" class="btn btn-sm btn-primary search">查询</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>项目编号</th>
                <th>项目名称</th>
                <th>计息模式</th>
                <th>借款人</th>
                <th>权证人</th>
                <th>收益率</th>
                <th>周期</th>
                <th>标的金额(元)</th>
                <th>起标日期</th>
                <th>投资时间</th>
                <th>放款时间</th>
                <th>投资人</th>
                <th>姓名</th>
                <th>推荐人</th>
                <th>投资金额(元)</th>
                <th>计息天数</th>
                <th>回款时间</th>
                <th>期限</th>
                <th>预期回款(元)</th>
                <th>逾期收益(元)</th>
                <th>实际收益(元)</th>
                <th>逾期服务费(元)</th>
                <th>实际服务费(元)</th>
                <th>实际回款(元)</th>
                <th>推荐奖励</th>
                <th>使用优惠(使用优惠信息／实际返款)</th>
                <th>阶梯加息优惠(阶梯加息利率/实际返款)</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as financeReportDto>
                <tr>
                    <td>${(financeReportDto.loanId?c)!}</td>
                    <td>${(financeReportDto.loanName)!}</td>
                    <td>${(financeReportDto.loanType)!}</td>
                    <td>${(financeReportDto.loanerUserName)!}</td>
                    <td>${(financeReportDto.agentLoginName)!}</td>
                    <td>${(financeReportDto.benefitRate)!}</td>
                    <td>${(financeReportDto.duration)!}</td>
                    <td>${(financeReportDto.loanAmount)!}</td>
                    <td>${(financeReportDto.verifyTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${(financeReportDto.investTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${(financeReportDto.recheckTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${(financeReportDto.investLoginName)!}</td>
                    <td>${(financeReportDto.investRealName)!}</td>
                    <td>${(financeReportDto.referrer)!}</td>
                    <td>${(financeReportDto.investAmount)!}</td>
                    <td>${(financeReportDto.benefitDays)!}</td>
                    <td>${(financeReportDto.repayTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${(financeReportDto.period)!}</td>
                    <td>${(financeReportDto.expectInterest)!}</td>
                    <td>${(financeReportDto.overdueInterest)!}</td>
                    <td>${(financeReportDto.actualInterest)!}</td>
                    <td>${(financeReportDto.overdueFee)!}</td>
                    <td>${(financeReportDto.fee)!}</td>
                    <td>${(financeReportDto.actualRepayAmount)!}</td>
                    <td>${(financeReportDto.recommendAmount)!}</td>
                    <td>${financeReportDto.couponDetail!'-'} / ${financeReportDto.couponActualInterest!'-'}</td>
                    <td>${financeReportDto.extraDetail!'-'} / ${financeReportDto.extraActualInterest!'-'}</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${data.count}条, 每页显示${data.pageSize}条</span>
        </div>
        <#if data.records?has_content>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                    <a href="/finance-manage/financeReport?index=${data.index - 1}&pageSize=${data.pageSize}&loanId=${(loanId?c)!}&period=${selectedPeriod!}&investLoginName=${investLoginName!}&investStartTime=${(investStartTime?string('yyyy-MM-dd HH:mm:ss'))!}&investEndTime=${(investEndTime?string('yyyy-MM-dd HH:mm:ss'))!}&repayStartTime=${(repayStartTime?string('yyyy-MM-dd HH:mm:ss'))!}&repayEndTime=${(repayEndTime?string('yyyy-MM-dd HH:mm:ss'))!}&<#if selectedPreferenceType??>usedPreferenceType=${selectedPreferenceType.name()}</#if>"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage>
                    <a href="/finance-manage/financeReport?index=${data.index + 1}&pageSize=${data.pageSize}&loanId=${(loanId?c)!}&period=${selectedPeriod!}&investLoginName=${investLoginName!}&investStartTime=${(investStartTime?string('yyyy-MM-dd HH:mm:ss'))!}&investEndTime=${(investEndTime?string('yyyy-MM-dd HH:mm:ss'))!}&repayStartTime=${(repayStartTime?string('yyyy-MM-dd HH:mm:ss'))!}&repayEndTime=${(repayEndTime?string('yyyy-MM-dd HH:mm:ss'))!}&<#if selectedPreferenceType??>usedPreferenceType=${selectedPreferenceType.name()}</#if>"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
            <@security.authorize access="hasAnyAuthority('ADMIN', 'DATA')">
                <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
            </@security.authorize>
        </#if>
    </nav>
    <!-- pagination -->

</div>
<!-- content area end -->
</@global.main>