<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-funds.js" headLab="finance-manage" sideLab="userFund" title="用户资金管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label class="control-label">账户类型: </label>&nbsp;&nbsp;
            <input type="radio" name="role" value="UMP_INVESTOR"
                          <#if role?? && role == 'UMP_INVESTOR'>checked="checked"</#if>
                          />联动优势 &nbsp;&nbsp;
            <input type="radio" name="role" value="LOANER"
                  <#if role?? && role=='LOANER'>checked="checked"</#if>
            />富滇银行-借款人 &nbsp;&nbsp;
            <input type="radio" name="role" value="INVESTOR"
                  <#if role?? && role=='INVESTOR'>checked="checked"</#if>
            />富滇银行-出借人 &nbsp;&nbsp;

        </div>
        </br>
        <div class="row">
            <div class="form-group">
                <label for="control-label">电话号码</label>
                <input type="text" id="mobile" name="mobile" class="form-control jq-loginName" value="${mobile!}">
            </div>
            <div class="form-group">
                <label for="control-label">时间</label>

                <div class='input-group date' id="investDateBegin">
                    <input type='text' class="form-control jq-startTime" name="startTime"
                           value="${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                    <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id="investDateEnd">
                    <input type='text' class="form-control jq-endTime" name="endTime"
                           value="${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                    <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
            <div class="form-group operationTypeDiv  <#if role.name() == 'INVESTOR'>hidden</#if>">
                <label for="control-label">费用类型</label>
                <select class="selectpicker operationType" data-style="btn-default" name="operationType">
                    <option value="">请选择费用类型</option>
                    <#list operationTypes as item>
                        <option value="${item}"
                                <#if operationType?has_content && operationType == item>selected</#if>>${item.description}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group operationTypeUMPDiv <#if role.name() != 'INVESTOR'>hidden</#if>">
                <label for="control-label">费用类型</label>
                <select class="selectpicker operationTypeUMP" data-style="btn-default" name="operationTypeUMP">
                    <option value="">请选择费用类型</option>
                    <#list operationTypeUMPList as item>
                        <option value="${item}"
                                <#if operationTypeUMP?has_content && operationTypeUMP == item>selected</#if>>${item.description}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group businessTypeDiv <#if role.name() == 'INVESTOR'>hidden</#if>">
                <label for="control-label">操作类型</label>
                <select class="selectpicker businessType" data-style="btn-default" name="businessType">
                    <option value="">全部</option>
                    <#list businessTypes as item>
                            <option value="${item}"
                                <#if businessType?has_content && businessType == item>selected</#if>>${item.description}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group  businessTypeUMPDiv <#if role.name() != 'INVESTOR'>hidden</#if>">
                <label for="control-label">操作类型</label>
                <select class="selectpicker businessTypeUMP" data-style="btn-default" name="businessTypeUMP">
                    <option value="">全部</option>
                    <#list businessTypeUMPList as item>
                        <option value="${item}"
                                <#if businessTypeUMP?has_content && businessTypeUMP == item>selected</#if>>${item.description}</option>
                    </#list>
                </select>
            </div>
            <button class="btn btn-primary search" type="button">查询</button>
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
                <#list userBillModels as userBillModel>
                <tr>
                    <td>${(userBillModel.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${userBillModel.id?string('0')}</td>
                    <#if role?? && role == 'UMP_INVESTOR'>
                        <td>${userBillModel.umpUserName!''}</td>
                    <#else>
                        <td>${userBillModel.userName!''}</td>
                    </#if>
                    <td>${userBillModel.userName!}</td>
                    <td>${userBillModel.mobile!}</td>
                    <td>${userBillModel.operationType.getDescription()}</td>
                    <td>${userBillModel.businessType.getDescription()}</td>
                    <td>${userBillModel.amount/100}</td>
                    <td>${userBillModel.balance/100}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${userFundsCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/finance-manage/user-funds?role=${role!}&mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}&operationType=${operationType!}&operationTypeUMP=${operationTypeUMP!}&businessType=${businessType!}&businessTypeUMP=${businessTypeUMP!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/finance-manage/user-funds?role=${role!}&mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}&operationType=${operationType!}&operationTypeUMP=${operationTypeUMP!}&businessType=${businessType!}&businessTypeUMP=${businessTypeUMP!}&index=${index+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
            <@security.authorize access="hasAnyAuthority('DATA')">
                <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
            </@security.authorize>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>
