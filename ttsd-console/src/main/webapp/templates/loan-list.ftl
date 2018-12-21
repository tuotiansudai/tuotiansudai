<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>

<@global.main pageCss="" pageJavascript="loanList.js" headLab="project-manage" sideLab="ALL" title="项目管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build" id="formLoanList">

        <div class="form-group">
            <label for="number">项目编号</label>
            <input type="text" class="form-control loanId" name="loanId" placeholder=""
                   value="${(loanId?string('0'))!}">
        </div>
        <div class="form-group">
            <label for="project">项目状态</label>
            <select class="selectpicker" name="status">
                <option value="">全部</option>
                <#list loanStatusList as status>
                    <option value="${status.name()}"
                            <#if (selectedStatus?? && selectedStatus.name() == status.name()) >selected</#if>>${status.description}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label for="number">项目名称</label>
            <input type="text" class="form-control loanName" name="loanName" placeholder="" value="${loanName!}">
        </div>
        <div class="form-group">
            <label for="number">发起日期</label>

            <div class='input-group date' id='datepickerBegin'>
                <input type='text' class="form-control" name="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
            </div>
            -
            <div class='input-group date' id='datepickerEnd'>
                <input type='text' class="form-control" name="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
            </div>
        </div>

        <button type="button" class="btn btn-sm btn-primary search">查询</button>
    </form>
    <div class="table-responsive" style="overflow: visible">
        <table class="table table-bordered table-hover " id="tabFormData">
            <thead>
            <tr>
                <th>项目编号</th>
                <th>项目名称</th>
                <th>借款期限</th>
                <th>借款人</th>
                <th>权证人</th>
                <th>借款金额(元)</th>
                <th>年化/活动(利率)</th>
                <th>投资奖励</th>
                <th>禁止转让</th>
                <th>项目状态</th>
                <th>发起时间</th>
                <th>投资/还款记录</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list loanListDtos as loanListDto>
                <tr>
                    <td>${loanListDto.id?string('0')}</td>
                    <td>
                        <span class="add-tooltip" data-placement="top" data-toggle="tooltip"
                              data-original-title="${loanListDto.name}">
                            <a href="${webServer}/loan/${loanListDto.id?string('0')}"
                               target="_blank">${loanListDto.name}</a>
                        </span>
                    </td>
                    <td><#if loanListDto.productType??>${loanListDto.productType.getName()}</#if></td>
                    <td>${loanListDto.loanerUserName}</td>
                    <td>${loanListDto.agentLoginName!}</td>
                    <td class="td">${loanListDto.loanAmount/100}</td>
                    <td>${loanListDto.basicRate}/${loanListDto.activityRate}</td>

                    <td class="ladder-table-activity">
                        <#if (loanListDto.extraLoanRateModels?size>0)>
                            <table class="table table-bordered tip-activity">
                                <thead>
                                <tr>
                                    <th>投资金额范围(元)</th>
                                    <th>投资奖励比例(%)</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <#list loanListDto.extraLoanRateModels as extraLoanRateModel>
                                    <tr>
                                        <td>${extraLoanRateModel.amountLower} ≤ 投资额
                                        ${(extraLoanRateModel.amountUpper!="0.00")?string('<${extraLoanRateModel.amountUpper}','')}
                                        </td>
                                        <td>${extraLoanRateModel.rate}</td>
                                    </tr>
                                    </#list>
                                </tbody>
                            </table>
                            <span class="ladderInvest" style="display: block;">
                                <#if loanListDto.extraSource??>
                                    <#list loanListDto.extraSource as extraSource>
                                        ${extraSource.name()}<#sep>, </#sep>
                                    </#list>
                                </#if>
                            </span>
                        <#else>
                            -
                        </#if>
                    <#--${(!loanListDto.extraLoanRateModels??)?string('是','-')}-->
                    </td>
                    <td>${loanListDto.nonTransferable?string('是','否')}</td>
                    <td>${loanListDto.status.getDescription()}</td>
                    <td>${loanListDto.createdTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td><a class="invest_repay"
                           href="/finance-manage/invests?loanId=${loanListDto.id?string('0')}">投资</a>/<a
                            class="loan_repay"
                            href="/project-manage/loan-repay?loanId=${loanListDto.id?string('0')}&loginName=&repayStartDate=&repayEndDate=&repayStatus=&index=1&pageSize=10">还款记录</a>
                    </td>
                    <#if loanListDto.pledgeType != 'NONE'>
                        <td><a class="loan_edit" href="/project-manage/loan/${loanListDto.id?string('0')}">编辑</a>
                        </td></#if>
                    <#if loanListDto.pledgeType == 'NONE'>
                        <td><a class="loan_edit" ${loanListDto.id?string('0')}">无</a></td></#if>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${loanListCount}条</span>
        </div>
        <#if loanListDtos?has_content>
            <ul class="pagination">

                <li>
                    <#if hasPreviousPage >
                    <a href="?status=${selectedStatus!}&index=${index-1}&loanId=${loanId!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&loanName=${loanName!}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="?status=${selectedStatus!}&index=${index+1}&loanId=${loanId!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&loanName=${loanName!}"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>

                </li>
            </ul>
            <@security.authorize access="hasAnyAuthority('DATA')">
                <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
            </@security.authorize>
        </#if>
    </nav>
    <!-- pagination -->

</div>
<!-- content area end -->
</@global.main>
