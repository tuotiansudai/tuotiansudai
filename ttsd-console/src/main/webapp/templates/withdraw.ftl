<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="withdraw.js" headLab="finance-manage" sideLab="withdraw" title="提现记录">

<#assign pagination = baseDto.data />
<#assign withdrawList = pagination.records />

<!-- content area begin -->

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">编号</label>
                <input type="text" class="form-control" name="withdrawId" placeholder="" value="${withdrawId!}">
            </div>
            <div class="form-group" id="recordDate">
                <label for="control-label">时间</label>

                <div class='input-group date' id='datetimepicker1'>
                    <input type='text' class="form-control" name="startTime"
                           value="${(startTime?string('yyyy-MM-dd HH:mm'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id='datetimepicker2'>
                    <input type='text' class="form-control" name="endTime"
                           value="${(endTime?string('yyyy-MM-dd HH:mm'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input"
                       datatype="*" autocomplete="off" value="${loginName!}"/>
            </div>
            </br>
            <div class="form-group">
                <label for="control-label">状态</label>
                <select class="selectpicker" name="status">
                    <option value="">全部</option>
                    <#list withdrawStatusList as statusItem>
                        <option value="${statusItem.name()}"
                                <#if (status.name())?has_content && status.name() == statusItem.name()>selected</#if>
                                >${statusItem.description}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label for="control-label">来源</label>
                <select class="selectpicker" name="source">
                    <option value="">全部</option>
                    <#list withdrawSourceList as sourceItem>
                        <#if sourceItem.name() != 'AUTO'>
                            <option value="${sourceItem.name()}"
                                    <#if (source.name())?has_content && source.name() == sourceItem.name()>selected</#if>
                                    >${sourceItem.name()}</option>
                        </#if>
                    </#list>
                </select>
            </div>

            <button type="submit" class="btn btn-sm btn-primary">查询</button>
            <button type="reset" class="btn btn-sm btn-default">重置</button>
        </div>

    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th colspan="12">合计提现金额：${sumAmount/100} 元 &nbsp;&nbsp;&nbsp; 合计提现手续费：${sumFee/100} 元</th>
            </tr>
            <tr>
                <th>编号</th>
                <th>申请时间</th>
                <th>初审时间</th>
                <th>复核时间</th>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>提现金额</th>
                <th>手续费</th>
                <th>银行卡</th>
                <th>状态</th>
                <th>来源</th>
            </tr>
            </thead>

            <tbody>
                <#if withdrawList?has_content>
                    <#list withdrawList as withdrawItem>
                    <tr>
                        <td>${withdrawItem.withdrawId?string('0')}</td>
                        <td>${(withdrawItem.createdTime?string('yyyy-MM-dd HH:mm'))!}</td>
                        <td>${(withdrawItem.applyNotifyTime?string('yyyy-MM-dd HH:mm'))!}</td>
                        <td>${(withdrawItem.notifyTime?string('yyyy-MM-dd HH:mm'))!}</td>
                        <td>${withdrawItem.loginName}
                            <#if withdrawItem.isStaff()>
                                <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                            </#if>
                        </td>
                        <td>${withdrawItem.userName}</td>
                        <td>${withdrawItem.mobile}</td>
                        <td>${withdrawItem.amount}</td>
                        <td>${withdrawItem.fee}</td>
                        <td>${withdrawItem.bankCard}</td>
                        <td>${withdrawItem.status}</td>
                        <td>${(withdrawItem.source.name())!}</td>
                    </tr>
                    </#list>
                <#else>
                <tr>
                    <td colspan="12">Empty</td>
                </tr>
                </#if>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if withdrawList?has_content>
            <div>
                <span class="bordern">总共${pagination.count}条,每页显示${pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if pagination.hasPreviousPage >
                    <a href="?withdrawId=${withdrawId!}&loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&source=${source!}&status=${status!}&pageSize=${pageSize}&index=${index-1}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${pagination.index}</a></li>
                <li>
                    <#if pagination.hasNextPage >
                    <a href="?withdrawId=${withdrawId!}&loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&source=${source!}&status=${status!}&pageSize=${pageSize}&index=${index+1}"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
            <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>