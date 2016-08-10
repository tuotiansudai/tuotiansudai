<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-product.js" headLab="point-manage" sideLab="createProduct" title="添加商品">

<div class="col-md-10">
    <form action="/product-manage/create" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">商品类别:</label>
            <div class="col-sm-4">
                <span class="form-control">${goodsTypeDesc!}</span>
                <input class="goodsType" name="goodsType" value="${goodsType!}" type="hidden">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品名称</label>

            <div class="col-sm-4">
                <input type="text" class="form-control product-name" name="productName" placeholder=""
                       <#if product??>value="${product.productName!}"</#if> datatype="*" nullmsg="商品名称不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品图片:</label>
            <div class="col-sm-4 ">
                <input type="text" name="imageUrl" class="form-control form-imageUrl" readonly placeholder=""
                       value="<#if product??>${banner.imageUrl!}</#if>" datatype="*" nullmsg="商品图片">
            </div>
            <div class="col-sm-6">
                <div class="imageUrlProduct">
                    <input type="file" imageWidth="300" imageHeight="244"/>
                </div>
                <div class="text-danger">
                    (图片必须是300px * 244px)
                </div>
                <div class="col-sm-4" style="position:relative">
                    <div class="imageUrlImage" style="position:absolute;top:0;left:0;width:300px;height:244px;">
                        <#if banner?? && banner.imageUrl??>
                            <img style="width:100%" src="/${banner.imageUrl!}" alt="缩略图"/>
                        </#if>
                    </div>
                </div>
            </div>

        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">商品数量</label>

            <div class="col-sm-4">
                <input type="text" class="form-control total-count" name="totalCount" placeholder=""
                       <#if product??>value="${product.totalCount!}"</#if> datatype="*" nullmsg="商品数量不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品介绍</label>
            <div class="col-sm-4">
                <input type="text" class="form-control description" name="description" placeholder=""
                       <#if product??>value="${product.description!}"</#if> datatype="*" nullmsg="商品介绍不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品顺序</label>

            <div class="col-sm-4">
                <input data-type="${goodsType!}" class="order-number input-sm" name="seq" value="1" disabled>- <input
                    type="text" class="form-control seq" name="seq" placeholder=""
                    <#if product??>value="${product.seq!}"</#if> datatype="*" nullmsg="商品顺序不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label ">商品有效期限: </label>
            <div class="col-sm-2">
                <div class='input-group date' id='startTime'>
                    <input type='text' class="form-control product-start" name="startTime"
                           <#if coupon??>value="${(product.startTime?string("yyyy-MM-dd HH:mm:ss"))!}"</#if>
                           datatype="date" nullmsg="请选择商品开始时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
            <div class="line-size">-</div>
            <div class="col-sm-2">
                <div class='input-group date' id='endTime'>
                    <input type='text' class="form-control product-end" name="endTime"
                           <#if coupon??>value="${(coupon.endTime?string("yyyy-MM-dd HH:mm:ss"))!}"</#if> datatype="date"
                           nullmsg="请选择商品结束时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品价格</label>

            <div class="col-sm-4">
                <input type="text" class="form-control productPrice" name="productPrice" placeholder=""
                       <#if product??>value="${product.productPrice!}"</#if> datatype="*" nullmsg="商品价格不能为空">财豆
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">操作: </label>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="col-sm-4">
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave"
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN')">disabled</@security.authorize>>
                    确认创建
                </button>
            </div>
        </div>

    </form>
</div>
<!-- content area end -->
</@global.main>
