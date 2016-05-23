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

        </#if>

        <div class="form-group">
            <label  class="col-sm-2 control-label ">有效期限: </label>
            <div class="col-sm-2">
                <div class='input-group date' id='startTime'>
                    <input type='text' class="form-control coupon-start" name="startTime" <#if exchangeCouponDto??>value="${(exchangeCouponDto.startTime?string("yyyy-MM-dd HH:mm"))!}"</#if>  datatype="date" errormsg="请选择活动开始时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
            <div class="line-size">-</div>
            <div class="col-sm-2">
                <div class='input-group date' id='endTime'>
                    <input type='text' class="form-control coupon-end" name="endTime" <#if exchangeCouponDto??>value="${(exchangeCouponDto.endTime?string("yyyy-MM-dd HH:mm"))!}"</#if>  datatype="date" errormsg="请选择活动结束时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
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
                <div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number invest_limit" name="investLowerLimit"
                                                         <#if exchangeCouponDto??>value="${exchangeCouponDto.investLowerLimit}"</#if> placeholder="" datatype="*" errormsg="使用条件金额不能为空"><div class="item-invest">元可用</div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="multiple" value="1">
        <input type="hidden" name="id" <#if exchangeCouponDto??>value="${exchangeCouponDto.id?string('0')}"</#if>/>
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
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave">确认创建</button>
            </div>
        </div>

    </form>

</div>

</@global.main>