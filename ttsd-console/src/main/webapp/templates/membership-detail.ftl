<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="auto-app-push-list.js" headLab="membership-manage" sideLab="membershipQuery" title="会员等级查询">

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

    <div class="form-group">
        <label>用户名: ${loginName}</label>
    </div>
    <div class="form-group">
        <label>成长值: ${membershipPoint?string('0')}</label>
    </div>
    <div class="form-group">
        <label>会员等级: V${membershipLevel}</label>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    时间
                </th>
                <th>
                    成长值
                </th>
                <th>24小时随时提现，16:00点前提现
                    累计成长值
                </th>
                <th>
                    会员等级
                </th>
                <th>
                    行为
                </th>
            </tr>
            </thead>
            <tbody>
                <#if membershipExperienceList??>
                    <#list membershipExperienceList as membershipExperience>
                        <tr>
                            <td>${membershipExperience.createdTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>+${membershipExperience.experience?string('0')}</td>
                            <td>${membershipExperience.totalExperience?string('0')}</td>
                            <td>
                                <#assign x = membershipExperience.totalExperience>
                                <#if (membershipExperience.totalExperience > 0 && membershipExperience.totalExperience < V1Experience)>
                                    V0
                                <#elseif (membershipExperience.totalExperience >= V1Experience && membershipExperience.totalExperience < V2Experience)>
                                    V1
                                <#elseif (membershipExperience.totalExperience >= V2Experience && membershipExperience.totalExperience < V3Experience)>
                                    V2
                                <#elseif (membershipExperience.totalExperience >= V3Experience && membershipExperience.totalExperience < V4Experience)>
                                    V3
                                <#elseif (membershipExperience.totalExperience >= V4Experience && membershipExperience.totalExperience < V5Experience)>
                                    V4
                                <#elseif (membershipExperience.totalExperience >= V5Experience)>
                                    V5
                                <#else>
                                    V0
                                </#if>
                            </td>
                            <td>${membershipExperience.description!}</td>
                        </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${membershipExperienceCount}条,每页显示${pageSize}条</span>
        </div>
        <#if membershipExperienceList?has_content>
            <ul class="pagination">
                <li>
                    <#if hasPreviousPage>
                    <a href="?index=${index-1}&pageSize=${pageSize}<#if loginName??>&loginName=${loginName}</#if>" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?index=${index+1}&pageSize=${pageSize}<#if loginName??>&loginName=${loginName}</#if>" aria-label="Next">
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
</@global.main>