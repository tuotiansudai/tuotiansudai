
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="article-list.js" headLab="content-manage" sideLab="articleMan" title="投资圈列表">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build" id="formArticleList">
        <div class="row">
            <div class="form-group">
                <label for="control-label">标题</label>
                <input type="text" class="form-control jq-title" name="title" value="${title!}">
            </div>
            <div class="form-group">
                <label for="control-label">类型</label>
                <select class="selectpicker" name="articleSectionType" id="section">
                        <option value="">全部</option>
                    <#list articleSectionTypeList as sectionName>
                        <option value="${sectionName}"
                                <#if (selected?? && selected == sectionName) >selected</#if>
                                >${sectionName.articleSectionTypeName}</option>
                    </#list>
                </select>
            </div>
            <div id="subSectionDiv" class="form-group" <#if  !(selected)?? || selected != 'KNOWLEDGE'>style="display: none"</#if>>
                <label for="control-label">子栏目</label>
                <select class="selectpicker" name="subArticleSectionType" >
                    <option value="">全部</option>
                    <#list subArticleSectionTypeList as subSectionName>
                        <option value="${subSectionName}"
                                <#if (subSelected?? && subSelected == subSectionName) >selected</#if>
                        >${subSectionName.articleSectionTypeName}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label>状态：</label>
                <select class="selectpicker" name="status">
                    <option value="" <#if !(status??)>selected</#if>>全部</option>
                    <#list articleStatus as item>
                        <option value="${item}" <#if status?? && item==status>selected</#if>>
                        ${item.description}
                        </option>
                    </#list>
                </select>
            </div>
            <button class="btn btn-primary search" type="button">查询</button>
            <button class="btn btn-default pull-right publishAD" type="button"> 添加信息</button>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>栏目</th>
                <th>标题</th>
                <th>更新时间</th>
                <th>定时发布时间</th>
                <th>创建人</th>
                <th>审核人</th>
                <th>状态</th>
                <th>操作</th>
                <th>点赞/阅读</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as article>
                <tr>
                    <td>
                        <#if article.carousel>
                            <i class="lunbo-icon"></i>
                        </#if>
                        <#if article.section??>
                            ${article.section.articleSectionTypeName!}
                        <#else >
                            全部
                        </#if>
                    </td>
                    <td>
                        ${article.title!}
                        <#if article.original >
                            <a target="_Blank" style="color: red;" href="/announce-manage/article/${article.articleId?c}/original">( 原文 )</a>
                        </#if>
                    </td>
                    <td>${(article.updateTime?string('yyyy-MM-dd'))!}</td>
                    <td>${(article.timingTime?string('yyyy-MM-dd HH:mm'))!}</td>
                    <td>${article.creator!}</td>
                    <td>${article.checker!}</td>
                    <td>${article.articleStatus.description!}</td>
                    <td>
                        <#if article.articleStatus.description == '审核驳回'>
                            <a href="/announce-manage/article/${article.articleId?c}/edit">编辑 </a>/
                            <a href="/announce-manage/article/${article.articleId?c}/retrace"> 撤销</a>
                        </#if>
                        <#if article.articleStatus.description == '待审核'>
                            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                <a href="javascript:void(0)" class="check-apply" data-id="${article.articleId?c}">审核 </a>/
                            </@security.authorize>
                            <@security.authorize access="hasAuthority('OPERATOR')">
                                <a href="/announce-manage/article/${article.articleId?c}/edit">编辑 </a>/
                            </@security.authorize>
                            <a href="/announce-manage/article/${article.articleId?c}/retrace"> 撤销</a>
                        </#if>
                        <#if article.articleStatus.description == '审核中'>
                            <#if article.creator == userName>
                                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                    <a href="javascript:void(0)" class="check-apply" data-id="${article.articleId?c}">审核 </a>
                                </@security.authorize>
                            </#if>
                        </#if>
                        <#if article.articleStatus.description == '已发布'>
                            <a href="/announce-manage/article/${article.articleId?c}/edit">编辑 </a>/
                            <a href="/announce-manage/article/${article.articleId?c}/deleteArticle" onclick="return confirm('确定删除吗?')"> 删除</a>
                        </#if>
                    </td>
                    <td>
                    ${article.likeCount!} /
                    ${article.readCount!}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${data.count}条, 每页显示${data.pageSize}条</span>
        </div>
        <#if data.records?has_content>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                    <a href="/announce-manage/article/list?title=${title!}<#if status??>&status=${status}</#if><#if selected??>&articleSectionType=${selected}</#if>&index=${data.index - 1}&pageSize=${data.pageSize}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a id="pageIndex">${data.index}</a></li>
                <li>
                    <#if data.hasNextPage>
                    <a href="/announce-manage/article/list?title=${title!}<#if status??>&status=${status}</#if><#if selected??>&articleSectionType=${selected}</#if>&index=${data.index + 1}&pageSize=${data.pageSize}"
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