<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-remain-list.js" headLab="user-manage" sideLab="userRemain" title="留存用户查询">
    <#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

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
            <div class='input-group date' id='registerStartDatetimepicker'>
                <input type='text' class="form-control" name="registerStartTime"
                       value="${(registerStartTime?string('yyyy-MM-dd HH:mm'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='registerEndDatetimepicker'>
                <input type='text' class="form-control" name="registerEndTime"
                       value="${(registerEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">是否使用体验金：</label>
            <select class="selectpicker" name="useExperienceCoupon">
                <option value="">全部</option>
                <option <#if useExperienceCoupon?? && useExperienceCoupon>selected="selected"</#if> value="true">是</option>
                <option <#if useExperienceCoupon?? && !useExperienceCoupon>selected="selected"</#if> value="false">否</option>
            </select>
        </div>
        <div class="form-group">
            <label for="control-label">使用体验金时间：</label>
            <div class='input-group date' id='experienceStartDatetimepicker'>
                <input type='text' class="form-control" name="experienceStartTime"
                       value="${(experienceStartTime?string('yyyy-MM-dd HH:mm'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='experienceEndDatetimepicker'>
                <input type='text' class="form-control" name="experienceEndTime"
                       value="${(experienceEndTime?string('yyyy-MM-dd HH:mm'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">投资总次数：</label>
            <input type="text" class="form-control jq-userName" name="investCountLowLimit" value="${investCountLowLimit!}">
            -
            <input type="text" class="form-control jq-userName" name="investCountHighLimit" value="${investCountHighLimit!}">
        </div>
        <div class="form-group">
            <label for="control-label">投资总金额：</label>
            <input type="text" class="form-control jq-userName" name="investSumLowLimit" value="${investSumLowLimit!}">
            -
            <input type="text" class="form-control jq-userName" name="investSumHighLimit" value="${investSumHighLimit!}">
        </div>
        <div class="form-group">
            <label for="control-label">首投时间：</label>
            <div class='input-group date' id='firstInvestStartDatetimepicker'>
                <input type='text' class="form-control" name="firstInvestStartTime"
                       value="${(firstInvestStartTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='firstInvestEndDatetimepicker'>
                <input type='text' class="form-control" name="firstInvestEndTime"
                       value="${(firstInvestEndTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">二次投资时间：</label>
            <div class='input-group date' id='secondInvestStartDatetimepicker'>
                <input type='text' class="form-control" name="secondInvestStartTime"
                       value="${(secondInvestStartTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='secondInvestEndDatetimepicker'>
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
                    <td>${(user.registerTime?datetime)!}</td>
                    <td><#if user.useExperienceCoupon>是<#else>否</#if></td>
                    <td>${(user.experienceTime?datetime)!}</td>
                    <td>${user.investCount!}</td>
                    <td>${user.investSum!}</td>
                    <td>${(user.firstInvestTime?datetime)!}</td>
                    <td>${user.firstInvestAmount!}</td>
                    <td>${(user.secondInvestTime?datetime)!}</td>
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
                    <a href="/user-manage/remain-users?loginName=${loginName!}&mobile=${mobile!}&registerStartTime=${(registerStartTime?string('yyyy-MM-dd HH:mm'))!}&registerEndTime=${(registerEndTime?string('yyyy-MM-dd HH:mm'))!}&useExperienceCoupon=${(useExperienceCoupon?c)!}&experienceStartTime=${(experienceStartTime?string('yyyy-MM-dd HH:mm'))!}&experienceEndTime=${(experienceEndTime?string('yyyy-MM-dd HH:mm'))!}&investCountLowLimit=${investCountLowLimit!}&investCountHighLimit=${investCountHighLimit!}&investSumLowLimit=${investSumLowLimit!}&investSumHighLimit=${investSumHighLimit!}&firstInvestStartTime=${(firstInvestStartTime?string('yyyy-MM-dd HH:mm'))!}&firstInvestEndTime=${(firstInvestEndTime?string('yyyy-MM-dd HH:mm'))!}&secondInvestStartTime=${(secondInvestStartTime?string('yyyy-MM-dd HH:mm'))!}&secondInvestEndTime=${(secondInvestEndTime?string('yyyy-MM-dd HH:mm'))!}&index=${data.index-1}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                    <a href="/user-manage/remain-users?loginName=${loginName!}&mobile=${mobile!}&registerStartTime=${(registerStartTime?string('yyyy-MM-dd HH:mm'))!}&registerEndTime=${(registerEndTime?string('yyyy-MM-dd HH:mm'))!}&useExperienceCoupon=${(useExperienceCoupon?c)!}&experienceStartTime=${(experienceStartTime?string('yyyy-MM-dd HH:mm'))!}&experienceEndTime=${(experienceEndTime?string('yyyy-MM-dd HH:mm'))!}&investCountLowLimit=${investCountLowLimit!}&investCountHighLimit=${investCountHighLimit!}&investSumLowLimit=${investSumLowLimit!}&investSumHighLimit=${investSumHighLimit!}&firstInvestStartTime=${(firstInvestStartTime?string('yyyy-MM-dd HH:mm'))!}&firstInvestEndTime=${(firstInvestEndTime?string('yyyy-MM-dd HH:mm'))!}&secondInvestStartTime=${(secondInvestStartTime?string('yyyy-MM-dd HH:mm'))!}&secondInvestEndTime=${(secondInvestEndTime?string('yyyy-MM-dd HH:mm'))!}&index=${data.index+1}">
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
