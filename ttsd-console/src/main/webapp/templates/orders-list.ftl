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
    <div class="col-md-12">
        <span>商品名称:${product.name!}</span>
    </div>
    <div class="col-md-12">
        <span>商品价格:${product.points?string('0')!}</span>
    </div>
    <div class="col-md-12">
        <span>商品数量:${product.totalCount?string('0')!}</span>
    </div>
    <div  class="col-md-12" style="text-align:right">
        <#if product.type != 'COUPON'>
            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                <#if (orders?size>0)>
                    <button type="button" class="btn btn-sm btn-primary btnSend" data-id="${product.id?string('0')!}">
                        全部发货
                    </button>
                </#if>
            </@security.authorize>
        <#else>
            <button type="button" class="btn btn-sm btn-primary btnShowAll" data-id="${product.couponId?string('0')}">
                查看本优惠券全部详情
            </button>
        </#if>
    </div>
    <div class="table-responsive col-md-12">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>用户名</th>
                <th>实际花费积分</th>
                <th>兑换时间</th>
                <th>兑换数量</th>
                <th>姓名</th>
                <th>手机号码</th>
                <#if product.type != 'COUPON'>
                    <th>收货地址</th>
                    <th>备注</th>
                    <th>操作</th>
                    <th>发货时间</th>
                <#else>
                    <th>操作</th>
                </#if>
            </tr>
            </thead>
            <tbody>
                <#list orders as order>
                <tr>
                    <td>${order.loginName}</td>
                    <td>${(order.actualPoints * order.num)!}</td>
                    <td>${(order.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${order.num?string('0')}</td>
                    <td>${order.contact}</td>
                    <td>${order.mobile}</td>
                    <#if product.type != 'COUPON'>
                        <td>${order.address!}</td>
                        <td>${order.comment!}</td>
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
                                           data-id="${order.id?string('0')}" data-product-id="${product.id?string('0')}">确认发货</a>
                                    </label>
                                </#if>
                            </@security.authorize>
                            <@security.authorize access="hasAuthority('OPERATOR')">
                                -
                            </@security.authorize>
                        </td>
                        <td>${(order.consignmentTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <#else>
                        <td><a href="/point-manage/coupon/${product.couponId?string('0')}/${(order.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}/detail"
                               class="btn-link">查看详情</a></td>
                    </#if>
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
                    <button class="btn btn-default pull-left export-product" type="button" data-pid="${productId?string('0')}">
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
