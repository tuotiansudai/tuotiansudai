<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-coupon-exchange.js" headLab="point-manage" sideLab="createCouponExchange" title="优惠券兑换创建">

<div class="col-md-10">

    <form action="/point-manage/coupon-exchange" method="post" class="form-horizontal form-list">

        <div class="form-group">
            <label class="col-sm-2 control-label">优惠券类型:</label>
            <div class="col-sm-4">
                    <input type="text" class="form-control invest-coupon-total_count couponType" name="couponTypeDesc"
                           placeholder="" <#if exchangeCouponDto??>value="${exchangeCouponDto.couponType.getName()!}"</#if> readonly="true"/>

                    <input type="hidden" class="form-control coupon-type-hid" name="couponType" placeholder=""
                           <#if exchangeCouponDto??>value="${exchangeCouponDto.couponType.name()!}"</#if> />
            </div>
        </div>

        <div class="form-group invest-coupon">
            <label  class="col-sm-2 control-label">当前顺序: </label>
            <div class="col-sm-8">
                <div class="item-invest">1-</div>
                <input type="text" class="form-control invest-quota" name="seq" placeholder=""
                       <#if exchangeCouponDto??>value="${exchangeCouponDto.seq?string('0')!}"</#if> datatype="n"
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

        <#if exchangeCouponDto?? && (exchangeCouponDto.couponType.name() == "INVEST_COUPON" || exchangeCouponDto.couponType.name() == "RED_ENVELOPE") >
            <div class="form-group invest-coupon">
                <label  class="col-sm-2 control-label">金额(元): </label>
                <div class="col-sm-4">
                    <input type="text" class="form-control coupon-number" name="amount" <#if exchangeCouponDto??>value="${exchangeCouponDto.amount!}"</#if> placeholder="" datatype="*" errormsg="金额不能为空">
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
            <label  class="col-sm-2 control-label ">活动期限: </label>
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
            <label  class="col-sm-2 control-label">优惠券有效天数(天): </label>
            <div class="col-sm-8">
                <div class="item-invest">优惠券发放后</div><input type="text" class="form-control invest-quota coupon-deadline" name="deadline" placeholder="" <#if exchangeCouponDto??>value="${exchangeCouponDto.deadline!}"</#if> datatype="n"  errormsg="有效天数需要填写数字"><div class="item-invest">天内有效</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">所需积分: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control exchange-point" name="exchangePoint"
                       <#if exchangeCouponDto??>value="${exchangeCouponDto.exchangePoint?string('0')!}"</#if>
                       placeholder="" datatype="n" errormsg="所需积分需要填写数字">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">总数量(张): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control give-number" name="totalCount" <#if exchangeCouponDto??>value="${exchangeCouponDto.totalCount?string('0')!}"</#if> placeholder="" datatype="n" errormsg="总数量需要填写数字" >
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">兑换限制: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control give-number" name="monthLimit" <#if exchangeCouponDto??>value="${exchangeCouponDto.monthLimit?string('0')!}"</#if>placeholder="" datatype="n" errormsg="兑换限制需要填写数字"/> 个/人/月
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">可投资标的: </label>
            <div class="col-sm-3">
                <#list productTypes as productType>
                    <#if productType.name() != 'EXPERIENCE'>
                    <label>
                        <input type="checkbox" name="productTypes" class="productType"
                               <#if exchangeCouponDto?? && exchangeCouponDto.productTypes?seq_contains(productType.name())>checked="checked"</#if>
                               value="${productType.name()}">${productType.getName()}
                    </label>
                    </#if>
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


        <div class="form-group">
            <label  class="col-sm-2 control-label">来源: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-source" name="couponSource" <#if exchangeCouponDto??>value="${exchangeCouponDto.couponSource!}"</#if> placeholder="" datatype="*" errormsg="来源不能为空">
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">备注: </label>
            <div class="col-sm-8">
                <input type="text" class="form-control coupon-comment" name="comment" <#if exchangeCouponDto??>value="${exchangeCouponDto.comment!}"</#if> placeholder="">
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