<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="loan-application-list.js" headLab="customer-center" sideLab="platform-loan-list" title="借款申请信息">

<!-- content area begin -->
<div class="col-md-10" id="loanApplicationList">
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    申请时间
                </th>
                <th>
                    申请人姓名
                </th>
                <th>
                    申请人手机号
                </th>
                <th>
                    地址
                </th>
                <th>
                    借款金额（万元）
                </th>
                <th>
                    借款周期（月）
                </th>
                <th>
                    借款类型
                </th>
                <th>
                    抵押物信息
                </th>
                <th>
                    备注
                </th>
                <th>
                    操作
                </th>
            </tr>
            </thead>
            <tbody>
                <#list dataDto.records as loanApplicationView>
                <tr>
                    <td>
                    ${loanApplicationView.createdTime?datetime!}
                    </td>
                    <td>
                    ${loanApplicationView.userName!}
                    </td>
                    <td>
                    ${loanApplicationView.mobile!}
                    </td>
                    <td>
                    ${(loanApplicationView.address)!""}
                    </td>
                    <td>
                    ${loanApplicationView.amount!}
                    </td>
                    <td>
                    ${loanApplicationView.period!}
                    </td>
                    <td>
                    ${loanApplicationView.pledgeType.getDescription()!}
                    </td>
                    <td>
                    ${loanApplicationView.pledgeInfo!}
                    </td>
                    <td>
                        <span class="loanApplication-comment">${loanApplicationView.comment!}</span>
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('OPERATOR','OPERATOR_ADMIN','ADMIN')">
                            <input type="button" class="loanApplication-comment" value="添加备注"
                                   data-loanApplication-id="${loanApplicationView.id?c}">
                        </@security.authorize>
                        <a href="/loan-application/${loanApplicationView.id?c}">查看详情</a>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${dataDto.count!0}条,每页显示${dataDto.pageSize!10}条</span>
        </div>
        <ul class="pagination">
            <li>
                <#if dataDto.hasPreviousPage>
                <a href="?index=${dataDto.index - 1}&pageSize=${dataDto.pageSize!10}" aria-label="Previous">
                <#else>
                <a href="#" aria-label="Previous">
                </#if>
                <span aria-hidden="true">&laquo; Prev</span>
            </a>
            </li>
            <li><a>${dataDto.index!1}</a></li>
            <li>
                <#if dataDto.hasNextPage>
                <a href="?index=${dataDto.index + 1}&pageSize=${dataDto.pageSize!10}" aria-label="Next">
                <#else>
                <a href="#" aria-label="Next">
                </#if>
                <span aria-hidden="true">Next &raquo;</span>
            </a>
            </li>
        </ul>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->

<!-- 模态框（Modal） -->
<div class="modal fade" id="update" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">添加备注</h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="loanApplicationId" id="loanApplicationId"/>
                备注信息：<br/>
                <textarea class="form-control" name="comment" id="comment" rows="3"></textarea>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="update()">提交</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal -->
</div>
<!-- 模态框（Modal）end -->

</@global.main>