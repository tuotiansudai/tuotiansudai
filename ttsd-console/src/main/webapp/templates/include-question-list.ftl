<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="include-questions.js" headLab="ask-manage" sideLab="includeQuestionManage" title="收录文章管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="#" class="form-inline query-build" method="get">
        <div class="file-btn">
            <input type="file" id="file-in">批量导入
        </div>
        <input type="hidden" name="file" id="import-file">
    </form><br/>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>文章标题</th>
                <th>文章链接</th>
            </tr>
            </thead>
            <tbody>
                <#list includeQuestions.data.records as includeQuestion>
                <tr>
                    <td>${includeQuestion.questionTitle!}</td>
                    <td><a href="${includeQuestion.questionLink!}" target="_blank">${includeQuestion.questionLink!}</a></td>
                </tr>
                <#else>
                <tr>
                    <td colspan="2">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if includeQuestions.data.count &gt; 0>
            <div>
                <span class="bordern">总共${includeQuestions.data.count}条,每页显示${includeQuestions.data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if includeQuestions.data.hasPreviousPage >
                        <a href="?index=${includeQuestions.data.index-1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${includeQuestions.data.index}</a></li>
                <li>
                    <#if includeQuestions.data.hasNextPage >
                        <a href="?index=${includeQuestions.data.index+1}" aria-label="Next">
                            <span aria-hidden="true">Next &raquo;</span>
                        </a>
                    </#if>
                </li>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->

</@global.main>