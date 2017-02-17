<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>

<@global.main pageCss="" pageJavascript="product-list.js" headLab="point-manage" sideLab="productManage" title="商品管理">
<div class="col-md-10">

    <div class="col-md-12" style="margin-bottom: 40px">
        <a href="/point-manage/coupon-exchange-manage" class="btn btn-default" style="margin-right: 60px">优惠券商品管理</a>
        <a href="/point-manage/product-list?type=VIRTUAL" class="btn btn-default <#if type == 'VIRTUAL'>btn-warning</#if>" style="margin-right: 60px">虚拟商品管理</a>
        <a href="/point-manage/product-list?type=PHYSICAL" class="btn btn-default <#if type == 'PHYSICAL'>btn-warning</#if>">实物商品管理</a>
    </div>

    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
    <div class="table-responsive" style="width: 100%" id="productListContainer">
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
                <th colspan="3">操作</th>
            </tr>
            </thead>
            <tbody>
                <#list products as product>
                <tr>
                    <td>${type.description!}</td>
                    <td><#if type.name() =="VIRTUAL">1<#else>2</#if>-${product.seq?string('0')!}</td>
                    <td>${product.name}</td>
                    <td><img src="${staticServer}${product.imageUrl}" width="100px" height="50px"></td>
                    <td>${product.description}</td>
                    <td>${product.totalCount?string('0')}</td>
                    <td>${product.usedCount?string('0')}</td>
                    <td>${product.points?string('0')}</td>
                    <td>${(product.startTime?string('yyyy-MM-dd'))!}至${(product.endTime?string('yyyy-MM-dd'))!}</td>
                    <td>
                        <#if product.active>
                            -
                        <#else>
                            <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                                <#if product.usedCount?string('0') != "0">
                                    -
                                <#else>
                                    <a href="/point-manage/${product.id?string('0')}/edit">编辑</a>
                                </#if>
                            </@security.authorize>
                        </#if>
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                            <#if product.active>
                                <label>
                                    <i class="check-btn add-check"></i>
                                    <button class="loan_repay already-btn btn-link inactive-btn"
                                            data-id="${product.id?string('0')}" data-value="${product.usedCount?string('0')}">已生效
                                    </button>
                                </label>
                            <#else>
                                <label>
                                    <i class="check-btn"></i>
                                    <a class="loan_repay confirm-btn" href="javascript:void(0)"
                                       data-id="${product.id?string('0')}" data-value="${product.usedCount?string('0')}">确认生效</a>
                                </label>
                            </#if>
                        </@security.authorize>
                    </td>
                    <td>
                        <a href="/point-manage/${product.id?string('0')}/detail"
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
                    <a href="?index=${index-1}&pageSize=${pageSize}&type=${type}"
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
                    <a href="?index=${index+1}&pageSize=${pageSize}&type=${type}"
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
