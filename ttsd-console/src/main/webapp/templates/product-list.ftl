<#import "macro/global.ftl" as global>

<@global.main pageCss="" pageJavascript="product-list.js" headLab="point-manage" sideLab="${productTypeDesc!}" title="添加商品">
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
                <th>名称</th>
                <th>当前顺序</th>
                <th>名称</th>
                <th>商品图片</th>
                <th>商品介绍</th>
                <th>总数量</th>
                <th>已兑换数量</th>
                <th>商品价格</th>
                <th>商品有效期限</th>
                <th>查看详情</th>
                <th>状态</th>
            </tr>
            </thead>
            <tbody>
                <#list products as product>
                <tr>
                    <td>${productTypeDesc!}</td>
                    <td>${product.seq}</td>
                    <td>${product.productName}</td>
                    <td>${product.imageUrl}</td>
                    <td>${product.totalCount}</td>
                    <td>${product.usedCount}</td>
                    <td>${product.productPrice}</td>
                    <td>${(product.endTime?string('yyyy-MM-dd'))!}</td>
                    <td>${product.active}</td>
                </#list>
            </tbody>
        </table>
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
