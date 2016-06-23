<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="banner-list.js" headLab="announce-manage" sideLab="bannerMan" title="banner管理">

<!-- content area begin -->
<div class="col-md-10">

    <div class="table-responsive">
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
                            <td><img src="${banner.webImageUrl!}"/></td>
                            <td><img src="${banner.appImageUrl!}"/></td>
                            <td><a href="${banner.url!}" target="_blank">${banner.url!}</td>
                            <td>${banner.authenticated?then('登录后可见','非登录可见')}</td>
                            <td>
                                <#list banner.source as source>${source.name()}<#sep>/</#list>
                            </td>
                            <td>${banner.activatedTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>
                                <#if banner.deactivatedTime??>
                                    ${banner.deactivatedTime?string('yyyy-MM-dd HH:mm:ss')}
                                <#else>
                                    --
                                </#if>
                            </td>
                            <td>${banner.active?then('是','否')}</td>
                            <td>
                                <#if banner.active>
                                    <a href="">编辑</a> <a href="">下线</a>
                                <#else>
                                    <a href="">复用</a> <a href="">删除</a>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${count}条, 每页显示${pageSize}条</span>
        </div>
        <#if bannerList?has_content>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="?index=${index - 1}" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?index=${index + 1}" aria-label="Next">
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