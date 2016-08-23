<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="orders-list.js" headLab="point-manage" title="订单详情">

<div class="col-md-10">
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
    <div>
        <span><a class="loan_repay"
                 href="/point-manage/product-list?type=${product.type.name()!}">返回></a></span>
    </div>
    <div>
        <span>商品名称:${product.name!}</span>
    </div>
    <div>
        <span>商品价格:${product.points?string('0')!}</span>
    </div>
    <div>
        <span>商品数量:${product.totalCount?string('0')!}</span>
    </div>
    <div>
        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
            <#if (orders?size>0)>
                <button type="button" class="btn btn-sm btn-primary btnSend" data-id="${product.id?string('0')!}">
                    全部发货
                </button>
            </#if>
        </@security.authorize>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>用户名</th>
                <th>兑换时间</th>
                <th>兑换数量</th>
                <th>姓名</th>
                <th>手机号码</th>
                <th>收货地址</th>
                <th>操作</th>
                <th>发货时间</th>
            </tr>
            </thead>
            <tbody>
                <#list orders as order>
                <tr>
                    <td>${order.loginName}</td>
                    <td>${(order.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${order.num?string('0')}</td>
                    <td>${order.contact}</td>
                    <td>${order.mobile}</td>
                    <td>${order.address!}</td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                            <#if order.consignment>
                                <label>
                                    <i class="check-btn add-check"></i>
                                    <button class="loan_repay already-btn btn-link inactive-btn" disabled
                                            data-id="${order.id?string('0')}">已发货
                                    </button>
                                </label>
                            <#else>
                                <label>
                                    <i class="check-btn"></i>
                                    <a class="loan_repay confirm-btn" href="javascript:void(0)"
                                       data-id="${order.id?string('0')}">确认发货</a>
                                </label>
                            </#if>
                        </@security.authorize>
                        <@security.authorize access="hasAuthority('OPERATOR')">
                            -
                        </@security.authorize>
                    </td>
                    <td>${(order.consignmentTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                </#list>
            </tbody>
        </table>
    </div>
    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${ordersCount}条,每页显示${pageSize}条</span>
        </div>
        <#if orders?has_content>
            <ul class="pagination">

                <li>
                    <#if hasPreviousPage >
                    <a href="?index=${index-1}&pageSize=${pageSize}&productId=${productId}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="?index=${index+1}&pageSize=${pageSize}&productId=${productId}"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                    <button class="btn btn-default pull-left export-product" type="button" data-pid="${productId}">
                        导出Excel
                    </button>
                </@security.authorize>

            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>
