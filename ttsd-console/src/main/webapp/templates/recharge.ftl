<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="recharge.js" headLab="finance-manage" sideLab="recharge" title="充值记录">

<#assign pagination = baseDto.data />
<#assign rechargeList = pagination.records />

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
                <label for="control-label">编号</label>
                <input type="text" class="form-control" name="rechargeId" placeholder="" value="${rechargeId!}">
            </div>
            <div class="form-group" id="recordDate">
                <label for="control-label">时间</label>

                <div class='input-group date' id='datetimepicker1'>
                    <input type='text' class="form-control" name="startTime"
                           value="${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id='datetimepicker2'>
                    <input type='text' class="form-control" name="endTime"
                           value="${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
            <div class="form-group">
                <label for="control-label">电话号码</label>
                <input type="text" id="loginName" name="mobile" class="form-control ui-autocomplete-input"
                       datatype="*" autocomplete="off" value="${mobile!}"/>
            </div>
            <div></div>
            <div class="form-group">
                <label for="control-label">状态</label>
                <select class="selectpicker" name="status">
                    <option value="">全部</option>
                    <#list rechargeStatusList as statusItem>
                        <option value="${statusItem.name()}"
                                <#if (status.name())?has_content && status.name() == statusItem.name()>selected</#if>
                                >${statusItem.description}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label for="control-label">充值来源</label>
                <select class="selectpicker" name="source">
                    <option value="">全部</option>
                    <#list rechargeSourceList as sourceItem>
                        <option value="${sourceItem.name()}"
                                <#if (source.name())?has_content && source.name() == sourceItem.name()>selected</#if>
                                >${sourceItem.name()}</option>
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
                <th colspan="12">合计充值金额：${sumAmount/100} 元</th>
            </tr>
            <tr>
                <th>编号</th>
                <th>时间</th>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>充值金额</th>
                <th>快捷充值</th>
                <th>充值状态</th>
                <th>充值来源</th>
                <th>渠道</th>
            </tr>
            </thead>

            <tbody>
                <#if rechargeList?has_content>
                    <#list rechargeList as rechargeItem>
                    <tr>
                        <td>${rechargeItem.rechargeId?string('0')}</td>
                        <td>${(rechargeItem.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                        <td>${rechargeItem.loginName!}
                            <#if rechargeItem.isStaff == "1">
                                <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                            </#if>
                        </td>
                        <td>${rechargeItem.userName!}</td>
                        <td>${rechargeItem.mobile!}</td>
                        <td>${rechargeItem.amount!}</td>
                        <td><#if rechargeItem.fastPay>是<#else>否</#if></td>
                        <td>${rechargeItem.status!}</td>
                        <td>${rechargeItem.source!}</td>
                        <td>${rechargeItem.channel!}</td>
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
        <#if rechargeList?has_content>
            <div>
                <span class="bordern">总共${pagination.count}条,每页显示${pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if pagination.hasPreviousPage >
                    <a href="?accountType=${accountType!}&rechargeId=${rechargeId!}&mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}&source=${source!}&status=${status!}&role=${role!}&index=${index-1}"
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
                    <a href="?accountType=${accountType!}&rechargeId=${rechargeId!}&mobile=${mobile!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm:ss'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm:ss'))!}&source=${source!}&status=${status!}&role=${role!}&index=${index+1}"
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