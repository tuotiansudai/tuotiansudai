<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-search.js" headLab="user-manage" sideLab="userSearchMan" title="用户查询">

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="loginName">用户名</label>
            <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${loginName!}" />
        </div>

        <div class="form-group">
            <label for="referrer">推荐人手机</label>
            <input type="text" class="form-control ui-autocomplete-input" id="input-referrer" name="referrerMobile"
                   placeholder="" datatype="*" autocomplete="off" value="${referrerMoible!}">
        </div>

        <div class="form-group">
            <label for="mobile">手机号</label>
            <input type="text" class="form-control" name="mobile" placeholder="" value="${mobile!}">
        </div>

        <div class="form-group">
            <label for="email">身份证号</label>
            <input type="text" class="form-control" name="identityNumber" placeholder="" value="${identityNumber!}">
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
                <th>推荐人手机</th>
                <th>来源</th>
                <th>渠道</th>
                <th>注册时间</th>
                <th>身份证号</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#if userList?has_content>
                <#list userList as userItem>
                <tr <#if userItem.status!='ACTIVE'> class="bg-warning" </#if> >
                    <td>${userItem.loginName}</td>
                    <td>${userItem.account.userName!}</td>
                    <td>${userItem.mobile}</td>
                    <td>${userItem.referrerMobile!}</td>
                    <td>${userItem.source!}</td>
                    <td>${userItem.channel!}</td>
                    <td>${userItem.registerTime?string('yyyy-MM-dd HH:mm')}</td>
                    <td>${userItem.account.identityNumber!}</td>
                    <td>${(userItem.status=='ACTIVE')?then('正常','禁用')}</td>
                    <td><a href="/user-manage/user/${userItem.loginName}">查看</a></td>
                </tr>
                </#list>
                <#else>
                <tr>
                    <td colspan="10">Empty</td>
                </tr>
                </#if>
            </tbody>

        </table>
    </div>

</div>

</@global.main>