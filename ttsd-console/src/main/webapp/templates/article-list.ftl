<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="article-list.js" headLab="announce-manage" sideLab="articleMan" title="理财圈列表">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">标题</label>
                <input type="text" class="form-control jq-title" name="title" value="${title!}">
            </div>
            <div class="form-group">
                <label for="control-label">类型</label>
                <select class="selectpicker" name="articleSectionType">
                    <option value="">全部</option>
                    <#list articleSectionTypeList as sectionName>
                        <option value="${sectionName}"
                                <#if (selected?has_content && selected == sectionName.articleSectionTypeName) >selected</#if>
                        >${sectionName.articleSectionTypeName}</option>
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
                    ${article.section.articleSectionTypeName!}
                    </td>
                    <td>${article.title!}</td>
                    <td>${(article.updateTime?string('yyyy-MM-dd'))!}</td>
                    <td>${article.author!}</td>
                    <td>${article.checker!}</td>
                    <td>${article.articleStatus.description!}</td>
                    <td>
                        <#if article.articleStatus.description == '待审核'>
                            <@security.authorize access="hasAuthority('ADMIN')">
                                <a href="/announce-manage/article/check/${article.articleId?c}" >审核 </a>/
                            </@security.authorize>
                            <a href="/announce-manage/article/retrace/${article.articleId?c}" > 撤销</a>
                        </#if>
                        <#if article.articleStatus.description == '已发布'>
                            <a href="/announce-manage/article/edit/${article.articleId?c}" >编辑 </a>/
                            <a href="/announce-manage/article/deleteArticle/${article.articleId?c}" onclick="return confirm('确定删除吗?')"> 删除</a>
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
                    <a href="/announce-manage/article/list?title=${title!}&index=${data.index - 1}&pageSize=${data.pageSize}" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage>
                    <a href="/announce-manage/article/list?title=${title!}&index=${data.index + 1}&pageSize=${data.pageSize}" aria-label="Next">
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