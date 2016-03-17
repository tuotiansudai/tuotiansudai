<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-coupon-exchange.js" headLab="point-manage" sideLab="createCouponExchange" title="优惠券兑换创建">

<div class="col-md-10">

    <form action="/activity-manage/coupon-exchange" method="post" class="form-horizontal form-list">

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

        <div class="form-group invest-coupon">
            <label  class="col-sm-2 control-label">体验券金额(元): </label>
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

        <div class="form-group coupon-hide interest-coupon">
            <label  class="col-sm-2 control-label">投资上限(元): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-number" name="investUpperLimit" placeholder="" datatype="*" errormsg="加息券投资上限不能为空">
            </div>
        </div>

        <div class="form-group" >
            <label  class="col-sm-2 control-label">有效期限(天): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-deadline" name="deadline" placeholder="" datatype="*" errormsg="有效期限不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">所需财豆</label>
            <div class="col-sm-4">
                <input type="text" class="form-control exchange-point" name="exchangePoint" placeholder="" data-type="n" errormsg="所需财豆需要填写数字">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">总数量(张): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control give-number" name="totalCount" placeholder="" datatype="n" errormsg="总数量需要填写数字" >
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">可投资标的: </label>
            <div class="col-sm-3">
                <#list productTypes as productType>
                    <label>
                        <input type="checkbox" name="productTypes" class="productType" value="${productType.name()}">${productType.getName()}
                    </label>
                </#list>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">使用条件: </label>
            <div class="col-sm-8">
                <div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number " name="investLowerLimit" placeholder="" datatype="*" errormsg="使用条件金额不能为空"><div class="item-invest">元可用</div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="multiple" value="1">
        <input type="hidden" name="userGroup" value="EXCHANGER">
        <div class="form-group">
            <label  class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave" <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN')">disabled</@security.authorize>>确认创建</button>
            </div>
        </div>

    </form>

</div>

</@global.main>