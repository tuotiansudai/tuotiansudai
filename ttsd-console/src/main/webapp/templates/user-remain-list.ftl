<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-point-list.js" headLab="point-manage" sideLab="userPointList" title="用户积分查询">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="control-label">用户名：</label>
            <input type="text" class="form-control jq-loginName" name="loginName" value="${loginName!}">
        </div>
        <div class="form-group">
            <label for="control-label">手机号：</label>
            <input type="text" class="form-control jq-mobile" name="mobile" value="${mobile!}">
        </div>
        <div class="form-group">
            <label for="control-label">注册时间：</label>
            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="registerBeginTime"
                       value="${(reigisterBeginTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="registerEndTime"
                       value="${(registerEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">是否使用体验金：</label>
            <select class="selectpicker" name="usedExperienceCoupon">
                <option value="">全部</option>
                <option value="true">是</option>
                <option value="false">否</option>
            </select>
        </div>
        <div class="form-group">
            <label for="control-label">使用体验金时间：</label>
            <div class='input-group date' id='datetimepicker3'>
                <input type='text' class="form-control" name="experienceBeginTime"
                       value="${(experienceBeginTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker4'>
                <input type='text' class="form-control" name="experienceEndTime"
                       value="${(experienceEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">投资总次数：</label>
            <input type="text" class="form-control jq-userName" name="investCount" value="${investCount!}">
        </div>
        <div class="form-group">
            <label for="control-label">投资总金额：</label>
            <input type="text" class="form-control jq-userName" name="investSum" value="${investSum!}">
        </div>
        <div class="form-group">
            <label for="control-label">首投时间：</label>
            <div class='input-group date' id='datetimepicker5'>
                <input type='text' class="form-control" name="firstInvestBeginTime"
                       value="${(firstInvestBeginTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker6'>
                <input type='text' class="form-control" name="firstInvestEndTime"
                       value="${(firstInvestEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">二次投资时间：</label>
            <div class='input-group date' id='datetimepicker5'>
                <input type='text' class="form-control" name="secondInvestBeginTime"
                       value="${(secondInvestBeginTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker6'>
                <input type='text' class="form-control" name="secondInvestEndTime"
                       value="${(secondInvestEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>

        <button class="btn btn-primary" type="submit">查询</button>
        <button class="btn btn-default" type="reset">重置</button>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户名</th>
                <th>手机号</th>
                <th>真实姓名</th>
                <th>注册时间</th>
                <th>是否使用新手体验金</th>
                <th>使用体验金时间</th>
                <th>投资总次数</th>
                <th>投资总金额</th>
                <th>首次投资时间</th>
                <th>首次投资金额</th>
                <th>二次投资时间</th>
                <th>二次投资金额</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as user>
                <tr>
                    <td>${user.loginName!}</td>
                    <td>${user.mobile!}</td>
                    <td>${user.userName!}</td>
                    <td>${(user.registerTime?date)!}</td>
                    <td><#if user.useExperienceCoupon>是<#else>否</#if></td>
                    <td>${(user.experienceTime?date)!}</td>
                    <td>${user.investCount!}</td>
                    <td>${user.investSum!}</td>
                    <td>${(user.firstInvestTime?date)!}</td>
                    <td>${user.firstInvestAmount!}</td>
                    <td>${(user.secondInvestTime?date)!}</td>
                    <td>${user.secondInvestAmount!}</td>
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
                    <a href="/point-manage/user-point-list?loginName=${loginName!}&userName=${userName!}&mobile=${mobbile!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                    <a href="/point-manage/user-point-list?loginName=${loginName!}&userName=${userName!}&mobile=${mobbile!}&index=${index+1}&pageSize=${pageSize}">
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
