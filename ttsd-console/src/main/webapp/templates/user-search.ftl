<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-search.js" headLab="user-manage" sideLab="userSearchMan" title="用户查询">

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="loginName">用户名</label>
            <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${loginName!}" />
        </div>

        <div class="form-group">
            <label for="referrer">推荐人</label>
            <input type="text" class="form-control ui-autocomplete-input" id="input-referrer" name="referrer" placeholder=""  datatype="*" autocomplete="off" value="${referrer!}">
        </div>

        <div class="form-group">
            <label for="mobile">手机号</label>
            <input type="text" class="form-control" name="mobile" placeholder="" value="${mobile!}">
        </div>

        <div class="form-group">
            <label for="email">身份证号</label>
            <input type="text" class="form-control" name="identityNumber" placeholder="" value="${email!}">
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
                <th>推荐人</th>
                <th>来源</th>
                <th>渠道</th>
                <th>注册时间</th>
                <th>身份证号</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list userList as userItem>
                <tr <#if userItem.status!='ACTIVE'> class="bg-warning" </#if> >
                    <td>${userItem.loginName}</td>
                    <td>${userItem.account.userName!}</td>
                    <td>${userItem.mobile}</td>
                    <td>${userItem.referrer!}</td>
                    <td>${userItem.source!}</td>
                    <td>${userItem.channel!}</td>
                    <td>${userItem.registerTime?string('yyyy-MM-dd HH:mm')}</td>
                    <td>${userItem.account.identityNumber!}</td>
                    <td>${(userItem.status=='ACTIVE')?then('正常','禁用')}</td>
                    <td><a href="/user-manage/user/${userItem.loginName}">编辑</a></td>
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
                <span class="bordern">总共${userCount}条,每页显示${pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="?loginName=${loginName!}&mobile=${mobile!}&referrer=${referrer!}&identityNumber=${identityNumber!}&pageSize=${pageSize}&index=${index-1}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="?loginName=${loginName!}&mobile=${mobile!}&referrer=${referrer!}&identityNumber=${identityNumber!}&pageSize=${pageSize}&index=${index+1}"
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