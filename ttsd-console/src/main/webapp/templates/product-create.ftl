<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-product.js" headLab="point-manage" sideLab="productCreate" title="添加商品">

<div class="col-md-10">
    <div class="col-md-12" style="margin-bottom: 40px">
        <a href="/point-manage/coupon-exchange" class="btn btn-default" style="margin-right: 60px">添加优惠券</a>
        <a href="/point-manage/create?type=VIRTUAL" class="btn btn-default <#if type == 'VIRTUAL'>btn-warning</#if>"
           style="margin-right: 60px">添加虚拟商品</a>
        <a href="/point-manage/create?type=PHYSICAL" class="btn btn-default <#if type == 'PHYSICAL'>btn-warning</#if>">添加实物商品</a>
    </div>
    <form action="/point-manage/create" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">商品类别:</label>
            <div class="col-sm-4">
                <span class="form-control">${type.description!}</span>
                <input class="type" name="type" value="${type.name()!}" type="hidden">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品名称</label>

            <div class="col-sm-4">
                <input type="text" class="form-control product-name" name="name" placeholder="" datatype="*"
                       errormsg="商品名称不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品图片:</label>
            <div class="col-sm-4 ">
                <input type="text" name="imageUrl" class="form-control form-imageUrl" readonly placeholder=""
                       datatype="*" errormsg="请上传商品图片">

                <div class="imageUrlImage" style="margin-top: 10px">
                    <#if product?? && product.imageUrl??>
                        <img style="width:100%" src="${commonStaticServer}${product.imageUrl!}" alt="缩略图" width="750"
                             height="450"/>
                    </#if>
                </div>
            </div>

            <div class="col-sm-6">
                <div class="imageUrlProduct">
                    <input type="file" imageWidth="750" imageHeight="450"/>
                </div>
                <div class="text-danger">
                    (图片必须是750px * 450px)
                </div>

            </div>

        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">商品数量</label>

            <div class="col-sm-4">
                <input type="text" class="form-control total-count" name="totalCount" placeholder="" datatype="n"
                       errormsg="商品数量只能为数字">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">兑换限制</label>

            <div class="col-sm-4">
                <input type="text" class="form-control total-count" name="monthLimit" placeholder="" datatype="n"
                       errormsg="兑换限制只能为数字"> 个/人/月（0表示无限制）
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品介绍</label>
            <div class="col-sm-4">
                <input type="text" class="form-control description" name="description" placeholder="" datatype="*"
                       errormsg="商品介绍不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品顺序</label>

            <div class="col-sm-4">
                <input data-type="${type!}" class="order-number input-sm" name="seq" value="1" disabled>- <input
                    type="text" class="form-control seq" name="seq" placeholder="" datatype="n" errormsg="商品顺序只能为数字">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label ">商品有效期限: </label>
            <div class="col-sm-2">
                <div class='input-group date' id='startTime'>
                    <input type='text' class="form-control product-start" name="startTime" datatype="date"
                           errormsg="请选择商品有效开始时间"/>
                    <span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
            <div class="line-size">-</div>
            <div class="col-sm-2">
                <div class='input-group date' id='endTime'>
                    <input type='text' class="form-control product-end" name="endTime" datatype="date"
                           errormsg="请选择商品有效结束时间"/>
                    <span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品价格</label>

            <div class="col-sm-3">
                <input type="text" class="form-control points" name="points" placeholder="" datatype="n"
                       errormsg="商品价格只能为数字">
            </div>
            <div class="col-sm-1"><span style="line-height: 34px">积分</span></div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品详情(移动端):</label>
            <div class="col-sm-4 ">
                <input type="text" name="appPictureUrl" class="form-control appPictureUrl" readonly placeholder=""
                       errormsg="请上传商品详情图片">

                <div class="appThumbnail" style="margin-top: 10px">
                    <#if product?? && product.appPictureUrl??>
                        <img style="width:100%" src="${commonStaticServer}${product.appPictureUrl!}" alt="缩略图"
                             width="750"/>
                    </#if>
                </div>
            </div>

            <div class="col-sm-6">
                <div class="appUrlProduct">
                    <input type="file" imageWidth="750"/>
                </div>
                <div class="text-danger">
                    (图片大小:宽度为750px,长度不限制)
                </div>

            </div>

        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">商品详情(PC端):</label>
            <div class="col-sm-4 ">
                <input type="text" name="webPictureUrl" class="form-control webPictureUrl" readonly placeholder=""
                       errormsg="请上传商品详情图片">

                <div class="webThumbnail" style="margin-top: 10px">
                    <#if product?? && product.webPictureUrl??>
                        <img style="width:100%" src="${commonStaticServer}${product.webPictureUrl!}" alt="缩略图"
                             width="1000"/>
                    </#if>
                </div>
            </div>

            <div class="col-sm-6">
                <div class="webUrlProduct">
                    <input type="file" imageWidth="1000"/>
                </div>
                <div class="text-danger">
                    (图片大小:宽度为1000px,长度不限制)
                </div>

            </div>

        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="form-group ">
            <label class="col-sm-2 control-label"></label>

            <div class="col-sm-4 form-error">
                <#if errorMessage?has_content>
                    <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close"
                         role="alert">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                                aria-hidden="true">×</span></button>
                        <span class="txt">创建失败：${errorMessage!}</span></div>
                </#if>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">操作: </label>
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
