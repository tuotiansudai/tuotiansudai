<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="membership-list.js" headLab="membership-manage" sideLab="membershipQuery" title="会员等级查询">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-form">
        <div class="row">
            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" class="form-control" name="loginName" value="${loginName!}"/>
            </div>
            <div class="form-group">
                <label for="control-label">注册时间</label>

                <div class='input-group date' id='datetimepickerStartTime'>
                    <input type='text' class="form-control" name="startTime"
                           value="${(startTime?string('yyyy-MM-dd HH:mm'))!}"/>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"/>
                        </span>
                </div>
                <span>-</span>

                <div class='input-group date' id='datetimepickerEndTime'>
                    <input type='text' class="form-control" name="endTime" value="${(endTime?string('yyyy-MM-dd HH:mm'))!}"/>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"/>
                        </span>
                </div>
            </div>
            <div class="form-group">
                <label for="control-label">手机号</label>
                <input type="text" class="form-group" name="mobile" value="${mobile!}">
            </div>
            <div class="form-group">
                <label for="control-label">获取方式</label>
                <select class="selectpicker" name="type">
                    <#list userMembershipTypeList as typeDescription>
                        <option value="${typeDescription}"
                                <#if (selectedType?has_content && selectedType == typeDescription)>selected</#if>>
                        ${typeDescription.description}
                        </option>
                    </#list>
                </select>
            </div>
            <div class="form-group levelCheckbox">
                <label for="control-label">会员等级</label>
                <#list levels as level>
                    <#assign checkedLevels = selectedLevels?split(',')>
                    <label><input class="level-box" data-id="${level}" type="checkbox" name="levels" value="${level}"
                                  <#if checkedLevels?seq_contains(level?string)>checked=1</#if>>V${level}</label>
                </#list>
            </div>
        </div>
    </form>
    <button class="btn btn-sm btn-primary search">查询</button>
    <a href="/membership-manage/membership-list" class="btn btn-sm btn-default">重置</a>

    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    用户名
                </th>
                <th>
                    真实姓名
                </th>
                <th>
                    手机号
                </th>
                <th>
                    成长值
                </th>
                <th>
                    会员等级
                </th>
                <th>
                    获取方式
                </th>
                <th>
                    明细查询
                </th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as userMember>
                <tr>
                    <td>${userMember.loginName!}</td>
                    <td>${userMember.realName!}</td>
                    <td>${userMember.mobile!}</td>
                    <td>${userMember.membershipPoint!}</td>
                    <td>V${userMember.membershipLevel!}</td>
                    <td>${userMember.userMembershipType.description!}</td>
                    <td><a href="/membership-manage/membership-detail?loginName=${userMember.loginName!}">查看明细</a></td>
                </tr>
                </#list>
            </tbody>
        </table>
        <!-- pagination  -->
        <nav class="pagination-control">
            <div>
                <span class="bordern">总共${data.count}条, 每页显示${data.pageSize}条</span>
            </div>
            <#if data.records?has_content>
                <ul class="pagination pull-left">
                    <li>
                        <#if data.hasPreviousPage >
                        <a href="/membership-manage/membership-list?index=${data.index - 1}&pageSize=${data.pageSize}&loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&mobile=${mobile!}&type=${selectedType!}&levels=${selectedLevels!}"
                           aria-label="Previous">
                        <#else>
                        <a href="#" aria-label="Previous">
                        </#if>
                        <span aria-hidden="true">&laquo; Prev</span>
                    </a>
                    </li>
                    <li><a>${data.index}</a></li>
                    <li>
                        <#if data.hasNextPage>
                        <a href="/membership-manage/membership-list?index=${data.index + 1}&pageSize=${data.pageSize}&loginName=${loginName!}&startTime=${(startTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&mobile=${mobile!}&type=${selectedType!}&levels=${selectedLevels!}"
                           aria-label="Next">
                        <#else>
                        <a href="#" aria-label="Next">
                        </#if>
                        <span aria-hidden="true">Next &raquo;</span>
                    </a>
                    </li>
                </ul>
            </#if>
        </nav>
        <!-- pagination -->
    </div>
</@global.main>