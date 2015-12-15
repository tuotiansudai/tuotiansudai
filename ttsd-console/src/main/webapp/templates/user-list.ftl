<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-list.js" headLab="user-manage" sideLab="userMan" title="用户管理">

<#assign pagination = baseDto.data />
<#assign userList = pagination.records />

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="loginName">用户名</label>
            <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${loginName!}" />
        </div>
        <div class="form-group">
            <label for="number">注册时间</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="beginTime"
                       value="${(beginTime?string('yyyy-MM-dd HH:mm'))!}"/>
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
            <label for="project">角色</label>
            <select class="selectpicker" name="role">
                <option value="">全部</option>
                <#list roleList as roleItem>
                    <option value="${roleItem.name()}"
                            <#if (role.name())?has_content && role.name() == roleItem.name()>selected</#if>
                            >${roleItem.description}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label for="mobile">手机号</label>
            <input type="text" class="form-control" name="mobile" placeholder="" value="${mobile!}">
        </div>
        <div class="form-group">
            <label for="email">电子邮件</label>
            <input type="text" class="form-control" name="email" placeholder="" value="${email!}">
        </div>
        <div class="form-group">
            <label for="project">渠道</label>
            <select class="selectpicker" name="channel">
                <option value="">全部</option>
                <#list channelList as channelName>
                    <option value="${channelName}"
                            <#if (channel?has_content && channel == channelName) >selected</#if>
                            >${channelName}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label for="project">来源</label>
            <select class="selectpicker" name="source">
                <option value="">全部</option>
                <#list sourceList as sourceItem>
                    <#if sourceItem.name() != 'AUTO'>
                        <option value="${sourceItem}"
                                <#if (source?has_content && source.name() == sourceItem.name()) >selected</#if>
                                >${sourceItem.name()}</option>
                    </#if>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label for="referrer">推荐人</label>
            <input type="text" class="form-control ui-autocomplete-input" id="input-referrer" name="referrer" placeholder=""  datatype="*" autocomplete="off" value="${referrer!}">
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
        <button type="reset" class="btn btn-sm btn-default">重置</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>手机号</th>
                <th>电子邮件</th>
                <th>推荐人</th>
                <th>来源</th>
                <th>渠道</th>
                <th>注册时间</th>
                <th>角色</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list userList as userItem>
                <tr <#if userItem.status!='ACTIVE'> class="bg-warning" </#if> >
                    <td>${userItem.loginName}</td>
                    <td>${userItem.userName!}</td>
                    <td>${userItem.mobile}</td>
                    <td>${userItem.email!}</td>
                    <td>${userItem.referrer!}</td>
                    <td>${userItem.source!}</td>
                    <td>${userItem.channel!}</td>
                    <td>${userItem.registerTime?string('yyyy-MM-dd HH:mm')}</td>
                    <td><#list userItem.userRoles as rs> ${rs.role.description}<#if rs_has_next>,</#if> </#list></td>
                    <td>${(userItem.status=='ACTIVE')?then('正常','禁用')}</td>
                    <td><a href="/user-manage/user/${userItem.loginName}">编辑</a> |
                        <#if userItem.status=='ACTIVE'>
                            <a class="user-status-modifier" href="#" data-url="/user-manage/user/${userItem.loginName}/disable">禁止</a>
                        <#else>
                            <a class="user-status-modifier" href="#" data-url="/user-manage/user/${userItem.loginName}/enable">解禁</a>
                        </#if>
                    </td>
                </tr>
                <#else>
                <tr>
                    <td colspan="10">Empty</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if userList?has_content>
            <div>
                <span class="bordern">总共${pagination.count}条,每页显示${pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if pagination.hasPreviousPage >
                    <a href="?loginName=${loginName!}&email=${email!}&mobile=${mobile!}&beginTime=${(beginTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&role=${(role.name())!}&source=${(source.name())!}&referrer=${referrer!}&channel=${channel!}&pageSize=${pageSize}&index=${pageIndex-1}"
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
                    <a href="?loginName=${loginName!}&email=${email!}&mobile=${mobile!}&beginTime=${(beginTime?string('yyyy-MM-dd HH:mm'))!}&endTime=${(endTime?string('yyyy-MM-dd HH:mm'))!}&role=${(role.name())!}&source=${(source.name())!}&referrer=${referrer!}&channel=${channel!}&pageSize=${pageSize}&index=${pageIndex+1}"
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