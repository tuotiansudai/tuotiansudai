<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="promotion-list.js" headLab="activity-manage" sideLab="promotion" title="APP弹窗推送管理">

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
        <@security.authorize access="hasAnyAuthority('OPERATOR', 'ADMIN')">
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/promotion/create" role="button">添加活动弹窗</a>
        </@security.authorize>
        <div></div>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>活动图</th>
                <th>活动名称</th>
                <th>活动顺序</th>
                <th>目标地址</th>
                <th>推送日期</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#if promotionList??>
                    <#list promotionList as promotion>
                    <tr>
                        <td style="text-align:center;vertical-align:middle;"><span class="imageUrl"><img id="imageUrl" src="${commonStaticServer}${promotion.imageUrl!}" width="40" height="60"/></td>
                        <td style="text-align:center;vertical-align:middle;">${promotion.name!}</td>
                        <td style="text-align:center;vertical-align:middle;">${promotion.seq!}</td>
                        <td style="text-align:center;vertical-align:middle;">${promotion.linkUrl!}</td>
                        <td style="text-align:center;vertical-align:middle;">${promotion.startTime?string('yyyy-MM-dd')!} 至 ${promotion.endTime?string('yyyy-MM-dd')!}</td>
                        <td style="text-align:center;vertical-align:middle;"><#if "APPROVED" == promotion.status>已生效<#else>未生效</#if></td>
                        <td style="text-align:center;vertical-align:middle;">
                            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN', 'ADMIN')">
                                <#if "APPROVED" == promotion.status>
                                    <button class="btn-link promotion-delete" data-link="" data-operator=""></button>
                                <#else>
                                    <button class="btn-link promotion-delete" data-link="/activity-console/activity-manage/promotion/${promotion.id?c}" data-operator="approved">通过</button> |
                                    <button class="btn-link promotion-delete" data-link="/activity-console/activity-manage/promotion/${promotion.id?c}" data-operator="rejection">驳回</button> |
                                </#if>
                                <button class="btn-link promotion-delete" data-link="/activity-console/activity-manage/promotion/${promotion.id?c}" data-operator="delete">删除</button>
                            </@security.authorize>

                            <@security.authorize access="hasAnyAuthority('OPERATOR', 'ADMIN')">
                                <#if "APPROVED" == promotion.status>
                                   --
                                <#else>
                                    <a href="/activity-console/activity-manage/promotion/${promotion.id?c}/edit">编辑</a> |
                                    <button class="btn-link promotion-delete"  data-link="/activity-console/activity-manage/promotion/${promotion.id?c}" data-operator="delete">删除</button>
                                </#if>
                            </@security.authorize>
                        </td>
                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>