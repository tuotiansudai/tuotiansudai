<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="experience-repay-detail" headLab="experience-manage" sideLab="experienceRepay" title="体验金还款明细">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="number">投资人手机号:</label>
            <input type="text" id="mobile" name="mobile" class="form-control ui-autocomplete-input jq-loginName" datatype="*"
                   autocomplete="off" value="${mobile!}"/>
        </div>
        <div class="form-group">
            <label for="number">预计还款时间:</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" id="startTime" name="startTime"
                       value="${(startTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" id="endTime" name="endTime"
                       value="${(endTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>

        </div>
        <div class="form-group">
            <label for="">还款状态: </label>

            <select class="selectpicker" name="repayStatus" id="repayStatus">
                <option value="">全部</option>
                <#list repayStatusList as status>
                    <option value="${status}"
                            <#if repayStatus?has_content && status == repayStatus>selected</#if>
                    >${status.description}</option>
                </#list>
            </select>
        </div>
        <button type="submit" class="btn btn-sm btn-primary btnSearch" id="btnRepayQuery">查询</button>
        <button type="reset" class="btn btn-sm btn-default btnSearch" id="btnRepayReset">重置</button>

    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>投资人手机号</th>
                <th>投资体验金金额（元）</th>
                <th>预计还款时间</th>
                <th>实际还款日期</th>
                <th>应还收益(元)</th>
                <th>实际收益(元)</th>
                <th>还款状态</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.mobile}</td>
                    <td>${item.amount/100}</td>
                    <td>${item.repayDate?string("yyyy-MM-dd HH:mm:ss")!}</td>
                    <td>${(item.actualRepayDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}</td>
                    <td>${item.expectedInterest/100}</td>
                    <td>${item.repayAmount/100}</td>
                    <td>${item.status.getDescription()!}</td>
                </tr>

                </#list>

            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                    <a href="/experience-manage/repay-detail?mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&repayStatus=${repayStatus!}&index=${data.index-1}&pageSize=${data.pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                    <a href="/experience-manage/repay-detail?mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&repayStatus=${repayStatus!}&index=${data.index+1}&pageSize=${data.pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
        </nav>
    </div>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>