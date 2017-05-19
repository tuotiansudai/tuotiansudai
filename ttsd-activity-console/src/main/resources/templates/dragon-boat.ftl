<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="dragonBoat" title="端午节活动">

<!-- content area begin -->
<div class="col-md-10">
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    姓名
                </th>
                <th>
                    手机号
                </th>
                <th>
                    活动期内累计投资金额
                </th>
                <th>
                    PK阵营
                </th>
                <th>
                    参与PK金额
                </th>
                <th>
                    邀请新用户获得体验金
                </th>
                <th>
                    PK获得体验金
                </th>
                <th>
                    邀请老用户领取数量
                </th>
                <th>
                    邀请新用户注册数量
                </th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>
                    ${item.userName!}
                    </td>
                    <td>
                    ${item.mobile!}
                    </td>
                    <td>
                    ${(item.totalInvestAmount/100)?string('0.00')}元
                    </td>
                    <td>
                    <#if item.pkGroup??><#if item.pkGroup=='SWEET'>甜粽派<#elseif item.pkGroup=='SALTY'>咸粽派</#if><#else>-</#if>
                    </td>
                    <td>
                    ${(item.pkInvestAmount/100)?string('0.00')}元
                    </td>
                    <td>
                    ${(item.inviteExperienceAmount/100)?string('0.00')}元
                    </td>
                    <td>
                    ${(item.pkExperienceAmount/100)?string('0.00')}元
                    </td>
                    <td>
                    ${item.inviteOldUserCount!}
                    </td>
                    <td>
                    ${item.inviteNewUserCount!}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="panel panel-default">
        <a href="/activity-console/activity-manage/export-dragon-boat">请点击此处导出EXCEl</a>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
        </div>
        <#if data?has_content>
            <ul class="pagination">
                <li>
                    <#if data.hasPreviousPage>
                    <a href="?index=${data.index - 1}"
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
                    <a href="?index=${data.index + 1}"
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
<!-- content area end -->
</@global.main>