<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-product.js" headLab="point-manage"  sideLab="product${goodsType.name()!}Manage" title="修改商品">

<div class="col-md-10">
    <form action="/product-manage/edit" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">商品类别:</label>
            <div class="col-sm-4">
                <span class="form-control">${goodsType.description!}</span>
                <input class="goodsType" name="goodsType" value="${goodsType.name()!}" type="hidden">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品名称</label>

            <div class="col-sm-4">
                <input type="text" class="form-control product-name" name="productName" placeholder=""
                       <#if product??>value="${product.productName!}"</#if> datatype="*" errormsg="商品名称不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品图片:</label>
            <div class="col-sm-4 ">
                <input type="text" name="imageUrl" class="form-control form-imageUrl" readonly placeholder=""
                       value="<#if product??>${product.imageUrl!}</#if>" datatype="*" errormsg="请上传商品图片">
                <div class="imageUrlImage" style="margin-top: 10px">
                    <#if product?? && product.imageUrl??>
                        <img style="width:100%" src="/${product.imageUrl!}" alt="缩略图" width="300" height="244"/>
                    </#if>
                </div>
            </div>

            <div class="col-sm-6">
                <div class="imageUrlProduct">
                    <input type="file" imageWidth="300" imageHeight="244"/>
                </div>
                <div class="text-danger">
                    (图片必须是300px * 244px)
                </div>

            </div>

        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">商品数量</label>

            <div class="col-sm-4">
                <input type="text" class="form-control total-count" name="totalCount" placeholder=""
                       <#if product??>value="${product.totalCount?string('0')!}"</#if> datatype="n" errormsg="商品数量只能为数字">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品介绍</label>
            <div class="col-sm-4">
                <input type="text" class="form-control description" name="description" placeholder=""
                       <#if product??>value="${product.description!}"</#if> datatype="*" errormsg="商品介绍不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品顺序</label>

            <div class="col-sm-4">
                <input data-type="${goodsType!}" class="order-number input-sm" name="seq" value="1" disabled>- <input
                    type="text" class="form-control seq" name="seq" placeholder=""
                    <#if product??>value="${product.seq?string('0')!}"</#if> datatype="n" errormsg="商品顺序只能为数字">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label ">商品有效期限: </label>
            <div class="col-sm-2">
                <div class='input-group date' id='startTime'>
                    <input type='text' class="form-control product-start" name="startTime"
                           <#if product??>value="${(product.startTime?string("yyyy-MM-dd HH:mm:ss"))!}"</#if> datatype="date" errormsg="请选择商品有效开始时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
            <div class="line-size">-</div>
            <div class="col-sm-2">
                <div class='input-group date' id='endTime'>
                    <input type='text' class="form-control product-end" name="endTime"
                           <#if product??>value="${(product.endTime?string("yyyy-MM-dd HH:mm:ss"))!}"</#if> datatype="date" errormsg="请选择商品有效结束时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品价格</label>

            <div class="col-sm-2">
                <input type="text" class="form-control productPrice" name="productPrice" placeholder=""
                       <#if product??>value="${product.productPrice?string('0')!}"</#if> datatype="n" errormsg="商品价格只能为数字">
            </div>
            <div class="col-sm-2"><span style="line-height: 34px">积分</span></div>
        </div>

        <input type="hidden" name="id" value="${product.id?string('0')}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="form-group ">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
                <#if errorMessage?has_content>
                    <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button><span class="txt">创建失败：${errorMessage!}</span></div>
                </#if>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave"
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN')">disabled</@security.authorize>>
                    确认修改
                </button>
            </div>
        </div>
    </form>
</div>
<!-- content area end -->
</@global.main>
