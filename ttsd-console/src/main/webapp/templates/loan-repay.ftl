<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="loan-repay.js" headLab="project-manage" sideLab="repaymentInfoList" title="项目还款明细">

<#assign loanRepays = baseDto.data>

<!-- content area begin -->
<div class="col-md-10">
    <form action="${requestContext.getContextPath()}/project-manage/loan-repay" method="post" class="form-inline query-build">
        <div class="form-group">
            <label for="number">项目编号:</label>
            <input type="text" class="form-control" id="loanId" placeholder="" value="${(loanId?string("0"))!}">
        </div>
        <div class="form-group">
            <label for="number">用户名:</label>
            <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${loginName!}"/>
        </div>
        <div class="form-group">
            <label for="number">开始时间:</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" id="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>

            <label for="number">结束时间:</label>

            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" id="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>

        </div>
        <div class="form-group">
            <label for="">标的类型: </label>

            <select class="selectpicker" name="repayStatus" id="repayStatus">
                <option value="">全部</option>
                <#list repayStatusList as status>
                    <option value="${status}"
                            <#if repayStatus?has_content && status == repayStatus>selected</#if>
                            >${status.description}</option>
                </#list>
            </select>
        </div>
        <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnRepayQuery" pageIndex="1">查询</button>
        <button type="reset" class="btn btn-sm btn-default btnSearch" id="btnRepayReset">重置</button>

    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>项目编号</th>
                <th>项目名称</th>
                <th>还款人</th>
                <th>预计还款日期</th>
                <th>实际还款日期</th>
                <th>当前期数</th>
                <th>应还本金</th>
                <th>应还利息(元)</th>
                <th>应还总数(元)</th>
                <th>还款状态(元)</th>
            </tr>
            </thead>
            <tbody>
                <#list loanRepays.records as loanRepay>
                <tr>
                    <td>${loanRepay.loanId?string('0')}</td>
                    <td>${loanRepay.loanName}</td>
                    <td>${loanRepay.agentLoginName!}</td>
                    <td>${loanRepay.repayDate?string("yyyy-MM-dd")}</td>
                    <td>${(loanRepay.actualRepayDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}</td>
                    <td>第${loanRepay.period}期</td>
                    <td>${loanRepay.corpus}</td>
                    <td>${loanRepay.expectedInterest}</td>
                    <td>${loanRepay.totalAmount}</td>
                    <td>${loanRepay.loanRepayStatus.getDescription()}</td>
                </tr>

                </#list>

            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav>

        <div>
            <span class="bordern">总共${loanRepays.count}条,每页显示${loanRepays.pageSize}条</span>
        </div>
        <ul class="pagination">

            <#if loanRepays.hasPreviousPage>
                <li>
                    <a href="#" aria-label="Previous">
                        <span class="Previous" aria-hidden="true" pageIndex="${loanRepays.index - 1}">&laquo;
                            Prev</span>
                    </a>
                </li>
            <#else >
                <li>
                    <a href="#" aria-label="Previous">
                        <span class="Previous" aria-hidden="true" pageIndex="${loanRepays.index}">&laquo; Prev</span>
                    </a>
                </li>
            </#if>
            <li><a>${loanRepays.index}</a></li>

            <#if loanRepays.hasNextPage>
                <li>
                    <a href="#" aria-label="Next">
                        <span class="Next" aria-hidden="true" pageIndex="${loanRepays.index + 1}">Next &raquo;</span>
                    </a>
                </li>
            <#else >
                <li>
                    <a href="#" aria-label="Next">
                        <span class="Next" aria-hidden="true" pageIndex="${loanRepays.index}">Next &raquo;</span>
                    </a>
                </li>
            </#if>

        </ul>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>