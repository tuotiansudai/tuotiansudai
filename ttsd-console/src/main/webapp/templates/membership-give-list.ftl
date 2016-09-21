<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="membership-give-list.js" headLab="membership-manage" sideLab="membershipGiveList" title="会员发放管理">

<!-- content area begin -->
<div class="col-md-10">
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
                <#list membershipGiveDtos as membershipGiveDto>
                <tr>
                    <td>V${membershipGiveDto.membershipLevel}</td>
                    <td>${membershipGiveDto.validPeriod}天</td>
                    <td>${(membershipGiveDto.receiveStartTime?date)!"-"}
                        至 ${(membershipGiveDto.receiveEndTime?date)!"-"}</td>
                    <td>${membershipGiveDto.userGroup.getDescription()}</td>
                    <td class="edit-list">

                        <@security.authorize access="hasAnyAuthority('OPERATOR','OPERATOR_ADMIN','ADMIN')">
                            <a href="/membership-manage/give/edit-view/${membershipGiveDto.id?c}"
                               class="edit-btn <#if !membershipGiveDto.valid>active</#if>">编辑</a>
                        </@security.authorize>
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                            <#if membershipGiveDto.valid>
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
            <span class="bordern">总共${totalCount}条,每页显示${pageSize}条</span>
        </div>
        <#if membershipGiveDtos?has_content>
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