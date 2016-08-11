<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="orders-list.js" headLab="point-manage" title="商品详情">

<div class="col-md-10">
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
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
                    <td>${order.createdTime}</td>
                    <td>${order.usedCount}</td>
                    <td>${order.realName}</td>
                    <td>${order.mobile}</td>
                    <td>${order.address!}</td>
                    <a href=""></a>
                    <td>${order.consignmentTime}</td>
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
                    <a href="?productId=${productId}"
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
                    <a href="?productId=${productId}"
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
