<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="prepare-users.js" headLab="activity-manage" sideLab="prepareUsers" title="APP分享管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build" id="formPrepareUsers">
        <div class="form-group">
            <label>领取人手机号码</label>
            <input type="text" class="form-control mobile" name="mobile" placeholder=""
                   value="${mobile!}">
        </div>

        <div class="form-group">
            <label>日期</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="beginTime"
                       value="${(beginTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="endTime"
                       value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>

        <button type="button" class="btn btn-sm btn-primary search">查询</button>
    </form>

    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    编号
                </th>
                <th>
                    领取时间
                </th>
                <th>
                    领取人手机号
                </th>
                <th>
                    渠道
                </th>
                <th>
                    状态
                </th>
                <th>
                    注册时间
                </th>
                <th>
                    推荐人手机号
                </th>
                <th>
                    推荐人姓名
                </th>
            </tr>
            </thead>
            <tbody>
                <#list prepareUsers as prepareUser>
                <tr>
                    <td>
                    ${prepareUser.id}
                    </td>
                    <td>
                    ${prepareUser.useTime?string('yyyy-MM-dd hh:mm:ss')}
                    </td>
                    <td>
                    ${prepareUser.mobile}
                    </td>
                    <td>
                    ${prepareUser.channel}
                    </td>
                    <td>
                    ${prepareUser.register?string('未注册','已注册')}
                    </td>
                    <td>
                    ${prepareUser.registerTime?string('yyyy-MM-dd hh:mm:ss')}
                    </td>
                    <td>
                    ${prepareUser.referrerMobile}
                    </td>
                    <td>
                    ${(prepareUser.referrerName!'')}
                    </td>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${prepareUserCount}条,每页显示${pageSize}条</span>
        </div>
        <#if prepareUsers?has_content>
            <ul class="pagination">
                <li>
                    <#if hasPreviousPage>
                    <a href="?index=${index-1}&pageSize=${pageSize}" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?index=${index+1}&pageSize=${pageSize}" aria-label="Next">
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
<!-- content area end -->
</@global.main>