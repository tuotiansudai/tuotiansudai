<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-coupon-exchange.js" headLab="point-manage" sideLab="productCreate" title="添加优惠券">

<div class="col-md-10">
    <div class="col-md-12" style="margin-bottom: 40px">
        <a href="/point-manage/coupon-exchange" class="btn btn-default btn-warning" style="margin-right: 60px">添加优惠券</a>
        <a href="/point-manage/create?type=VIRTUAL" class="btn btn-default" style="margin-right: 60px">添加虚拟商品</a>
        <a href="/point-manage/create?type=PHYSICAL" class="btn btn-default">添加实物商品</a>
    </div>
    <form action="/point-manage/coupon-exchange" method="post" class="form-horizontal form-list">

        <div class="form-group">
            <label class="col-sm-2 control-label">优惠券类型:</label>
            <div class="col-sm-4">
                <select class="selectpicker jq-b-type couponType" name="couponType">
                    <#list couponTypes as couponType>
                        <option value="${couponType.name()}">${couponType.getName()}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="form-group invest-seq">
            <label  class="col-sm-2 control-label">当前顺序: </label>
            <div class="col-sm-8">
                <div class="item-invest">1-</div>
                <input type="text" class="form-control invest-quota" name="seq" placeholder="" datatype="n"
                       errormsg="当前顺序只能为有效数字">
            </div>

        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">商品图片:</label>

            <div class="col-sm-4 ">
                <input type="text" name="imageUrl" class="form-control form-imageUrl" readonly placeholder=""
                       value="<#if exchangeCouponDto??>${exchangeCouponDto.imageUrl!}</#if>" datatype="*"
                       errormsg="请上传商品图片">

                <div class="imageUrlImage" style="margin-top: 10px">
                    <#if exchangeCouponDto?? && exchangeCouponDto.imageUrl??>
                        <img style="width:100%" src="${staticServer}${exchangeCouponDto.imageUrl!}" alt="缩略图" width="542"
                             height="340"/>
                    </#if>
                </div>
            </div>

            <div class="col-sm-6">
                <div class="imageUrlProduct">
                    <input type="file" imageWidth="542" imageHeight="340"/>
                </div>
                <div class="text-danger">
                    (图片必须是542px * 340px)
                </div>
            </div>
        </div>


        <div class="form-group invest-coupon">
            <label  class="col-sm-2 control-label">金额(元): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-number" name="amount" placeholder="" datatype="*" errormsg="体验券金额不能为空">
            </div>
        </div>

        <div class="form-group coupon-hide interest-coupon">
            <label  class="col-sm-2 control-label">加息券利率(%): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-rate" name="rate" placeholder="" datatype="*" errormsg="加息劵利率不能为空">
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label ">活动期限: </label>
            <div class="col-sm-2">
                <div class='input-group date' id='startTime'>
                    <input type='text' class="form-control coupon-start" name="startTime" datatype="date" errormsg="请选择活动开始时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
            <div class="line-size">-</div>
            <div class="col-sm-2">
                <div class='input-group date' id='endTime'>
                    <input type='text' class="form-control coupon-end" name="endTime" datatype="date" errormsg="请选择活动结束时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">优惠券有效天数(天): </label>
            <div class="col-sm-8">
                <div class="item-invest">用户收到优惠券后</div>
                <input type="text" class="form-control invest-quota coupon-deadline" name="deadline" placeholder=""
                       datatype="n" errormsg="有效天数需要填写数字">

                <div class="item-invest">天内有效</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">所需积分: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control exchange-point" name="exchangePoint" placeholder="" datatype="n"
                       errormsg="所需积分需要填写数字">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">总数量(张): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control give-number" name="totalCount" placeholder="" datatype="n" errormsg="总数量需要填写数字"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">兑换限制: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control give-number" name="monthLimit" placeholder="" datatype="n" errormsg="兑换限制需要填写数字"/> 个/人/月（0表示无限制）
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">可投资标的: </label>
            <div class="col-sm-3">
                <#list productTypes as productType>
                    <#if productType.name() != 'EXPERIENCE'>
                    <label>
                        <input type="checkbox" name="productTypes" class="productType" value="${productType.name()}">${productType.getName()}
                    </label>
                    </#if>
                </#list>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">使用条件: </label>
            <div class="col-sm-8">
                <div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number invest_limit" name="investLowerLimit" placeholder="" datatype="*" errormsg="使用条件金额不能为空"><div class="item-invest">元可用</div>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">来源: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-source" name="couponSource" placeholder="" datatype="*" errormsg="来源不能为空">
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">备注: </label>
            <div class="col-sm-8">
                <input type="text" class="form-control coupon-comment" name="comment" placeholder="">
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="multiple" value="1">
        <input type="hidden" name="userGroup" value="EXCHANGER">
        <div class="form-group ">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
                <#if errorMessage?has_content>
                    <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button><span class="txt">创建失败：${errorMessage!}</span></div>
                </#if>
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave" <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN')">disabled</@security.authorize>>确认创建</button>
            </div>
        </div>

    </form>

</div>

</@global.main>