<#import "macro/global.ftl" as global>

<@global.main pageCss="" pageJavascript="product-list.js" headLab="point-manage" sideLab="${goodsTypeDesc!}" title="添加商品">
<div class="col-md-10">
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
    <div class="table-responsive" id="productListContainer">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>商品类别</th>
                <th>当前顺序</th>
                <th>名称</th>
                <th>商品图片</th>
                <th>商品介绍</th>
                <th>总数量</th>
                <th>已兑换数量</th>
                <th>商品价格</th>
                <th>商品有效期限</th>
                <th/>
                <th/>
            </tr>
            </thead>
            <tbody>
                <#list products as product>
                <tr>
                    <td>${goodsTypeDesc!}</td>
                    <td>${product.seq}</td>
                    <td>${product.productName}</td>
                    <td>${product.imageUrl}</td>
                    <td>${product.description}</td>
                    <td>${product.totalCount}</td>
                    <td>${product.usedCount}</td>
                    <td>${product.productPrice}</td>
                    <td>${(product.endTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>
                        <input type="hidden" data-id="${product.id!}">
                        <#if product.active>
                            <a href="/product-manage/find-orders?productId=${product.id!}">查看详情</a>
                        <#else>
                            <a href="/product-manage/edit/${product.id!}">编辑</a>
                            <a href="javascript:void(0);" class="delete-record">删除</a>
                        </#if>
                    </td>
                    <td>
                        <#if product.active>
                            <input type="checkbox" checked="checked" disabled="disabled"> 已生效
                        <#else>
                            <input class="confirm-btn" type="checkbox" data-id="${product.id}"> 确认生效
                        </#if>
                    </td>
                </#list>
            </tbody>
        </table>
    </div>
    <div class="table-responsive">
        <div class="col-sm-1 col-md-offset-11">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="button" class="btn btn-sm btn-primary" id="btnSave">
                确认创建
            </button>
        </div>
    </div>
    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${goodsCount}条,每页显示${pageSize}条</span>
        </div>
        <#if products?has_content>
            <ul class="pagination">

                <li>
                    <#if hasPreviousPage >
                    <a href="?goodsType=${goodsType}"
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
                    <a href="?goodsType=${goodsType}"
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
</@global.main>
