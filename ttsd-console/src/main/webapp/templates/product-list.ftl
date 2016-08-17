<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>

<@global.main pageCss="" pageJavascript="product-list.js" headLab="point-manage" sideLab="product${goodsType.name()!}Manage" title="添加商品">
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
                    <th>操作</th>
                    <th> </th>
                    <th> </th>
                </tr>
                </thead>
                <tbody>
                    <#list products as product>
                    <tr>
                        <td>${goodsType.description!}</td>
                        <td>2-${product.seq?string('0')!}</td>
                        <td>${product.productName}</td>
                        <td><img src="${product.imageUrl}"></td>
                        <td>${product.description}</td>
                        <td>${product.totalCount?string('0')}</td>
                        <td>${product.usedCount?string('0')}</td>
                        <td>${product.productPrice?string('0')}</td>
                        <td>${(product.startTime?string('yyyy-MM-dd'))!}至${(product.endTime?string('yyyy-MM-dd'))!}</td>
                        <td>
                            <#if product.active>
                                -
                            <#else>
                                <@security.authorize access="hasAuthority('OPERATOR_ADMIN')">
                                    -
                                </@security.authorize>
                                <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                                    <a href="/product-manage/${product.id?string('0')}/edit">编辑</a>
                                </@security.authorize>
                            </#if>
                        </td>
                        <td>
                            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                <#if product.active>
                                    <label>
                                        <i class="check-btn add-check"></i>
                                        <button class="loan_repay already-btn btn-link inactive-btn" disabled
                                                data-id="${product.id?string('0')}">已生效
                                        </button>
                                    </label>
                                <#else>
                                    <label>
                                        <i class="check-btn"></i>
                                        <a class="loan_repay confirm-btn" href="javascript:void(0)"
                                           data-id="${product.id?string('0')}">确认生效</a>
                                    </label>
                                </#if>
                            </@security.authorize>
                            <@security.authorize access="hasAuthority('OPERATOR')">
                                -
                            </@security.authorize>
                        </td>
                        <td>
                            <a href="/product-manage/${product.id?string('0')}/detail"
                               class="btn-link">查看详情</a>
                        </td>
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
                        <a href="?index=${index-1}&pageSize=${pageSize}&goodsType=${goodsType}"
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
                        <a href="?index=${index+1}&pageSize=${pageSize}&goodsType=${goodsType}"
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
    </form>
</div>
</@global.main>
