<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="ask.js" headLab="ask-manage" sideLab="questionManage" title="提问管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/ask-manage/questions" class="form-inline query-build" method="get">
        <div class="form-group">
            <label for="question">问题</label>
            <input type="text" id="question" name="question" class="form-control" value="${question!}"/>
        </div>
        <div class="form-group">
            <label for="mobile">提问人</label>
            <input type="text" id="mobile" name="mobile" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${mobile!}">
        </div>
        <div class="form-group">
            <label for="project">状态</label>
            <select class="selectpicker" name="status">
                <option value="" <#if !(status??)>selected</#if>>全部</option>
                <#list statusOptions as statusOption>
                    <option value="${statusOption}" <#if status?? && status == statusOption>selected</#if>>
                    ${statusOption.description}
                    </option>
                </#list>
            </select>
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
        <button type="reset" class="btn btn-sm btn-default" id="questionsReset">重置</button>
        <button type="button" class="btn btn-sm btn-default approve">审核</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>
                    <input type="checkbox" id="checkall"/> 全选
                </th>
                <th>问题</th>
                <th>标签</th>
                <th>问题补充</th>
                <th>提问时间</th>
                <th>提问人</th>
                <th>审核人</th>
                <th>审核时间</th>
                <th>状态</th>
            </tr>
            </thead>
            <tbody>
                <#list questions.data.records as question>
                <tr>
                    <td>
                        <#if question.status == 'UNAPPROVED'>
                            <input type="checkbox" name="unapproved" value="${question.id?string.computer}"/>
                        </#if>
                    </td>
                    <td>${question.question}</td>
                    <td>
                        <#list question.tags as tag>
                        ${tag.description}
                        </#list>
                    </td>
                    <td>${question.addition!}</td>
                    <td>${question.createdTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${question.mobile}</td>
                    <#if question.approvedBy??>
                        <td>${question.approvedBy}</td>
                        <td>${question.approvedTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    </#if>
                    <#if question.rejectedBy??>
                        <td>${question.rejectedBy}</td>
                        <td>${question.rejectedTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    </#if>
                    <#if !(question.approvedBy??) && !(question.rejectedBy??)>
                        <td></td>
                        <td></td>
                    </#if>
                    <td>${question.status.description}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="9">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if questions.data.count &gt; 0>
            <div>
                <span class="bordern">总共${questions.data.count}条,每页显示${questions.data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if questions.data.hasPreviousPage >
                        <a href="?question=${question!}&mobile=${mobile!}&status=${status!}&index=${questions.data.index-1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${questions.data.index}</a></li>
                <li>
                    <#if questions.data.hasNextPage >
                        <a href="?question=${question!}&mobile=${mobile!}&status=${status!}&index=${questions.data.index+1}" aria-label="Next">
                            <span aria-hidden="true">Next &raquo;</span>
                        </a>
                    </#if>
                </li>
            </ul>
            <@security.authorize access="hasAnyAuthority('ASK_ADMIN')">
                <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
            </@security.authorize>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->

<div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <h5 class="confirm-title"></h5>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-default btn-submit approve" data-url="/ask-manage/question/approve">审核通过</button>
                <button type="button" class="btn btn-default btn-submit reject" data-url="/ask-manage/question/reject">审核拒绝</button>
            </div>
        </div>
    </div>
</div>

</@global.main>