<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="ask.js" headLab="ask-manage" sideLab="answerManage" title="回答管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/ask-manage/answers" class="form-inline query-build" method="get">
        <div class="form-group">
            <label for="loginName">问题</label>
            <input type="text" id="question" name="question" value="${question!}"/>
        </div>
        <div class="form-group">
            <label for="mobile">回答人</label>
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
        <button type="reset" class="btn btn-sm btn-default" id="answersReset">重置</button>
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
                <th>回答</th>
                <th>提问人</th>
                <th>提问时间</th>
                <th>回答人</th>
                <th>回答时间</th>
                <th>审核人</th>
                <th>审核时间</th>
                <th>状态</th>
            </tr>
            </thead>
            <tbody>
                <#list answers.data.records as answer>
                <tr>
                    <td>
                        <#if answer.status == 'UNAPPROVED'>
                            <input type="checkbox" name="unapproved" value="${answer.id?string.computer}"/>
                        </#if>
                    </td>
                    <td>${answer.question.question}</td>
                    <td>${answer.answer}</td>
                    <td>${answer.question.mobile}</td>
                    <td>${answer.question.createdTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${answer.mobile}</td>
                    <td>${answer.createdTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <#if answer.approvedBy??>
                        <td>${answer.approvedBy}</td>
                        <td>${answer.approvedTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    </#if>
                    <#if answer.rejectedBy??>
                        <td>${answer.rejectedBy}</td>
                        <td>${answer.rejectedTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    </#if>
                    <#if !(answer.approvedBy??) && !(answer.rejectedBy??)>
                        <td></td>
                        <td></td>
                    </#if>
                    <td>${answer.status.description}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="10">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if answers.data.count &gt; 0>
            <div>
                <span class="bordern">总共${answers.data.count}条,每页显示${answers.data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if answers.data.hasPreviousPage >
                        <a href="?question=${question!}&mobile=${mobile!}&status=${status!}&index=${answers.data.index-1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${answers.data.index}</a></li>
                <li>
                    <#if answers.data.hasNextPage >
                        <a href="?question=${question!}&mobile=${mobile!}&status=${status!}&index=${answers.data.index+1}" aria-label="Next">
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

<div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <h5 class="confirm-title"></h5>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-default btn-submit approve" data-url="/ask-manage/answer/approve">审核通过</button>
                <button type="button" class="btn btn-default btn-submit reject" data-url="/ask-manage/answer/reject">审核拒绝</button>
            </div>
        </div>
    </div>
</div>
</@global.main>