<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="membership-give-list.js" headLab="membership-manage" sideLab="membershipGiveList" title="会员发放管理">

<!-- content area begin -->
<div class="col-md-10" id="membershipList">
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
                    会员发放等级
                </th>
                <th>
                    会员期限
                </th>
                <th>
                    活动期限
                </th>
                <th>
                    发放对象
                </th>
                <th>
                    操作
                </th>
                <th>
                    批准
                </th>
                <th>
                    查看详情
                </th>
            </tr>
            </thead>
            <tbody>
                <#list dataDto.records as membershipGiveDto>
                <tr>
                    <td>V${membershipGiveDto.membershipLevel}</td>
                    <td>${membershipGiveDto.deadline}天</td>
                    <td>${(membershipGiveDto.startTime?date)!"-"}
                        至 ${(membershipGiveDto.endTime?date)!"-"}</td>
                    <td><#if (membershipGiveDto.userGroup) == "IMPORT_USER">
                        <span class="import-user-list"
                              data-id="${membershipGiveDto.id}"> ${membershipGiveDto.userGroup.getDescription()}</span>
                    <#else>
                    ${membershipGiveDto.userGroup.getDescription()}
                    </#if>
                    </td>
                    <td class="edit-list">

                        <@security.authorize access="hasAnyAuthority('OPERATOR','OPERATOR_ADMIN','ADMIN')">
                            <a href="/membership-manage/give/edit-view/${membershipGiveDto.id?c}"
                               class="edit-btn <#if !membershipGiveDto.active>active</#if>">编辑</a>
                        </@security.authorize>
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                            <#if membershipGiveDto.active>
                                <#if membershipGiveDto.userGroup == "IMPORT_USER">
                                    <input type="checkbox" name="valid" checked="checked"
                                           data-type="${membershipGiveDto.userGroup}" disabled/>
                                <#else>
                                    <input type="checkbox" name="valid" checked="checked"
                                           data-type="${membershipGiveDto.userGroup}"
                                           data-id="${membershipGiveDto.id}" class="give-membership"/>
                                </#if>
                            <#else>
                                <input type="checkbox" name="valid"
                                       data-type="${membershipGiveDto.userGroup}"
                                       data-id="${membershipGiveDto.id}" class="give-membership"/>
                            </#if>
                        </@security.authorize>
                    </td>
                    <td><a href="/membership-manage/give/${membershipGiveDto.id}/details">查看详情</a></td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${dataDto.count!0}条,每页显示${dataDto.pageSize!10}条</span>
        </div>
        <ul class="pagination">
            <li>
                <#if dataDto.hasPreviousPage>
                <a href="?index=${dataDto.index - 1}&pageSize=${dataDto.pageSize!10}" aria-label="Previous">
                <#else>
                <a href="#" aria-label="Previous">
                </#if>
                <span aria-hidden="true">&laquo; Prev</span>
            </a>
            </li>
            <li><a>${dataDto.index!1}</a></li>
            <li>
                <#if dataDto.hasNextPage>
                <a href="?index=${dataDto.index + 1}&pageSize=${dataDto.pageSize!10}" aria-label="Next">
                <#else>
                <a href="#" aria-label="Next">
                </#if>
                <span aria-hidden="true">Next &raquo;</span>
            </a>
            </li>
        </ul>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->

</@global.main>