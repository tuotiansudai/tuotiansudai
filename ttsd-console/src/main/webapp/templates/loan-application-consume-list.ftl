<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>

<@global.main pageCss="" pageJavascript="loan-application-list.js" headLab="project-manage" sideLab="loanApplicationList" title="借款申请列表">

<script type="text/javascript">

</script>

<!-- content area begin -->
<div class="col-md-10">
    <form action="/loan-application/consume-list" class="form-inline query-build" method="get">

        <div class="form-group">
            <label for="number">借款人用户名/手机号</label>
            <input type="text" class="form-control loanId" name="keyWord" placeholder=""
                   value="${keyWord!}">
        </div>
        <div class="form-group">
            <label for="project">申请状态</label>
            <select name="status" class="selectpicker">
                <option value="">全部</option>
                <#list statusList as status>
                    <option value="${status.name()}"
                            <#if (selectedStatus?? && selectedStatus.name() == status.name()) >selected</#if>>${status.description}</option>
                </#list>
            </select>
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

        <button type="submit" class="btn btn-sm btn-primary search">查询</button>
    </form>
    <div class="table-responsive" style="overflow: visible">
        <table class="table table-bordered table-hover " id="tabFormData">
            <thead>
            <tr>
                <th>申请时间</th>
                <th>申请人姓名</th>
                <th>申请人用户名</th>
                <th>申请人手机号</th>
                <th>地址</th>
                <th>借款金额(万元)</th>
                <th>借款周期(月)</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list dataDto.records as dto>
                <tr>
                    <td>${dto.createdTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${dto.userName}</td>
                    <td>${dto.loginName}</td>
                    <td>${dto.mobile}</td>
                    <td>${dto.address}</td>
                    <td>${dto.amount}</td>
                    <td>${dto.period}</td>
                    <td>${dto.status.description}</td>
                    <td><a href="/loan-application/consume/${dto.id?c}">详情</a></td>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${dataDto.count!0}条,每页显示${dataDto.pageSize!10}条</span>
        </div>
        <ul class="pagination">
            <li>
                <#if dataDto.hasPreviousPage>
                <a href="?keyWord=${keyWord!}&status=${status!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&index=${dataDto.index - 1}" aria-label="Previous">
                <#else>
                <a href="#" aria-label="Previous">
                </#if>
                <span aria-hidden="true">&laquo; Prev</span>
            </a>
            </li>
            <li><a>${dataDto.index!1}</a></li>
            <li>
                <#if dataDto.hasNextPage>
                <a href="?keyWord=${keyWord!}&status=${status!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&index=${dataDto.index + 1}" aria-label="Next">
                <#else>
                <a href="#" aria-label="Next">
                </#if>
                <span aria-hidden="true">Next &raquo;</span>
            </a>
            </li>
        </ul>
    </nav>
    <!-- pagination -->

</div>
<!-- content area end -->
</@global.main>
