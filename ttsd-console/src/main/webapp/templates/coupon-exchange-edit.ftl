<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-coupon-exchange.js" headLab="point-manage" sideLab="createCouponExchange" title="优惠券兑换创建">

<div class="col-md-10">

    <form action="/activity-manage/coupon-exchange" method="post" class="form-horizontal form-list">

        <div class="form-group">
            <label class="col-sm-2 control-label">优惠券类型:</label>
            <div class="col-sm-4">
                    <input type="text" class="form-control invest-coupon-total_count couponType" name="couponTypeDesc"
                           placeholder="" <#if exchangeCouponDto??>value="${exchangeCouponDto.couponType.getName()!}"</#if> readonly="true"/>

                    <input type="hidden" class="form-control coupon-type-hid" name="couponType" placeholder=""
                           <#if exchangeCouponDto??>value="${exchangeCouponDto.couponType.name()!}"</#if> />
            </div>
        </div>

        <#if exchangeCouponDto?? && exchangeCouponDto.couponType.name() == "INVEST_COUPON">
            <div class="form-group invest-coupon">
                <label  class="col-sm-2 control-label">体验券金额(元): </label>
                <div class="col-sm-4">
                    <input type="text" class="form-control coupon-number" name="amount" <#if exchangeCouponDto??>value="${exchangeCouponDto.amount!}"</#if> placeholder="" datatype="*" errormsg="体验券金额不能为空">
                </div>
            </div>
        </#if>

        <#if exchangeCouponDto?? && exchangeCouponDto.couponType.name() == "INTEREST_COUPON">
            <div class="form-group interest-coupon">
                <label  class="col-sm-2 control-label">加息券利率(%): </label>
                <div class="col-sm-4">
                    <input type="text" class="form-control coupon-rate" name="rate" <#if exchangeCouponDto??>value="${(exchangeCouponDto.rate*100)!}"</#if> placeholder="" datatype="*" errormsg="加息劵利率不能为空">
                </div>
            </div>

            <div class="form-group interest-coupon">
                <label  class="col-sm-2 control-label">投资上限(元): </label>
                <div class="col-sm-4">
                    <input type="text" class="form-control coupon-number" name="investUpperLimit" <#if exchangeCouponDto??>value="${exchangeCouponDto.investUpperLimit!}"</#if> placeholder="" datatype="*" errormsg="加息券投资上限不能为空">
                </div>
            </div>
        </#if>

        <div class="form-group" >
            <label  class="col-sm-2 control-label">有效期限(天): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-deadline" name="deadline" <#if exchangeCouponDto??>value="${exchangeCouponDto.deadline!}"</#if> placeholder="" datatype="*" errormsg="有效期限不能为空">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">所需财豆</label>
            <div class="col-sm-4">
                <input type="text" class="form-control exchange-point" name="exchangePoint" <#if exchangeCouponDto??>value="${exchangeCouponDto.exchangePoint?string('0')!}"</#if> placeholder="" data-type="n" errormsg="所需财豆需要填写数字">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">总数量(张): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control give-number" name="totalCount" <#if exchangeCouponDto??>value="${exchangeCouponDto.totalCount?string('0')!}"</#if> placeholder="" datatype="n" errormsg="总数量需要填写数字" >
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">可投资标的: </label>
            <div class="col-sm-3">
                <#list productTypes as productType>
                    <label>
                        <input type="checkbox" name="productTypes" class="productType"
                               <#if exchangeCouponDto?? && exchangeCouponDto.productTypes?seq_contains(productType.name())>checked="checked"</#if>
                               value="${productType.name()}">${productType.getName()}
                    </label>
                </#list>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">使用条件: </label>
            <div class="col-sm-8">
                <div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number " name="investLowerLimit"
                                                         <#if exchangeCouponDto??>value="${exchangeCouponDto.investLowerLimit}"</#if> placeholder="" datatype="*" errormsg="使用条件金额不能为空"><div class="item-invest">元可用</div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="multiple" value="1">
        <input type="hidden" name="id" <#if exchangeCouponDto??>value="${exchangeCouponDto.id?string('0')}"</#if>/>
        <input type="hidden" name="userGroup" value="EXCHANGER">
        <div class="form-group">
            <label  class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave">确认创建</button>
            </div>
        </div>

    </form>

</div>

</@global.main>