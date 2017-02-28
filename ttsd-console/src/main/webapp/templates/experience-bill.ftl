<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="experience-bill.js" headLab="experience-manage" sideLab="experienceBill" title="体验金流水明细">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">投资人手机号</label>
                <input type="text" id="mobile" name="mobile" class="form-control jq-loginName" value="${mobile!}">
            </div>
            <div class="form-group">
                <label for="control-label">时间</label>

                <div class='input-group date' id="investDateBegin">
                    <input type='text' class="form-control jq-startTime" name="startTime" value="${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id="investDateEnd">
                    <input type='text' class="form-control jq-endTime" name="endTime" value="${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
            <div class="form-group">
                <label for="control-label">费用类型</label>
                <select class="selectpicker operationType" data-style="btn-default" name="experienceBillOperationType">
                    <option value="">请选择费用类型</option>
                    <#list operationTypeList as operationTypeItem>
                        <option value="${operationTypeItem}"
                                <#if operationType?has_content && operationTypeItem == operationType>selected</#if>>${operationTypeItem.description}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label for="control-label">操作类型</label>
                <select class="selectpicker businessType" data-style="btn-default" name="experienceBusinessType">
                    <option value="">全部</option>
                    <#list businessTypeList as businessTypeItem>
                        <option value="${businessTypeItem}"
                                <#if businessType?has_content && businessType == businessTypeItem>selected</#if>>${businessTypeItem.description}</option>
                    </#list>
                </select>
            </div>
            <button class="btn btn-primary search" type="submit">查询</button>
            <button class="btn btn-default reset" type="reset">重置</button>
        </div>

    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>时间</th>
                <th>序号</th>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>费用类型</th>
                <th>操作类型</th>
                <th>金额(元)</th>
                <th>余额(元)</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${(item.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${item.id?string('0')}</td>
                    <td>${item.loginName!''}</td>
                    <td>${item.userName}</td>
                    <td>${item.mobile}</td>
                    <td>${item.operationType.getDescription()}</td>
                    <td>${item.businessType.getDescription()}</td>
                    <td>${item.amount/100}</td>
                    <td></td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                    <a href="/finance-manage/experience-bill?mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&experienceBillOperationType=${experienceBillOperationType!}&experienceBusinessType=${experienceBusinessType!}&index=${data.index-1}&pageSize=${data.pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                    <a href="/finance-manage/experience-bill?mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&experienceBillOperationType=${experienceBillOperationType!}&experienceBusinessType=${experienceBusinessType!}&index=${data.index+1}&pageSize=${data.pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>
