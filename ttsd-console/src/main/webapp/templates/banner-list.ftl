<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="banner-list.js" headLab="content-manage" sideLab="bannerMan" title="banner管理">

<!-- content area begin -->
<div class="col-md-10">

    <div class="table-responsive">
        <button class="btn btn-default pull-left bannerAD" type="button" onclick="/banner-manage/create"> 添加banner
        </button>

        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>当前顺序</th>
                <th>名称</th>
                <th>大图-WEB</th>
                <th>小图-APP</th>
                <th>链接</th>
                <th>属性</th>
                <th>终端</th>
                <th>上线时间</th>
                <th>下线时间</th>
                <th>是否在线</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#if bannerList??>
                    <#list bannerList as banner>
                    <tr>
                        <td>${banner.order!}</td>
                        <td>${banner.name!}</td>
                        <td><span class="webImg"><img id="webUrl" src="${commonStaticServer}${banner.webImageUrl!}" width="100" height="30"/></span>
                        </td>
                        <td><span class="appImg"><img id="appUrl" src="${commonStaticServer}${banner.appImageUrl!}" width="100" height="30"/>
                        </td>
                        <td><a href="${banner.url!}" target="_blank">${banner.url!}</td>
                        <td>${banner.authenticated?then('登录后可见','非登录可见')}</td>
                        <td>
                            <#list banner.source as source>${source.name()}<#sep>/</#list>
                        </td>
                        <td>${banner.activatedTime?string('yyyy-MM-dd HH:mm')}</td>
                        <td>
                            <#if banner.deactivatedTime??>
                            ${banner.deactivatedTime?string('yyyy-MM-dd HH:mm')}
                            <#else>
                                --
                            </#if>
                        </td>
                        <td><#if banner.deactivatedTime?date gt .now?date>是<#else>否</#if></td>
                        <td>
                            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                <#if banner.deactivatedTime?date gt .now?date>
                                    <a href="/banner-manage/banner/${banner.id?c}/edit">编辑</a>
                                <#else>
                                    <a href="javascript:void(0)" class="banner-delete"
                                       data-link="/banner-manage/banner/${banner.id?c}/delete">删除</a>
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