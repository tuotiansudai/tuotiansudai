<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-red-envelope.js" headLab="activity-manage" sideLab="createRedEnvelope" title="创建红包">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/activity-manage/coupon" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">红包名称:</label>
            <div class="col-sm-4">
                <span class="form-control">现金红包</span>
                <input class="couponType" name="couponType" value="RED_ENVELOPE" type="hidden">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">红包金额(元): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-number" name="amount" placeholder="" datatype="*" errormsg="红包金额不能为空" <#if coupon??>value="${coupon.amount!}"</#if>>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label ">活动日期: </label>
            <div class="col-sm-2">
                <div class='input-group date' id='startTime'>
                    <input type='text' class="form-control coupon-start" name="startTime" datatype="date" errormsg="请选择活动开始时间" <#if coupon??>value="${(coupon.startTime?string("yyyy-MM-dd"))!}"</#if>/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
            <div class="line-size">-</div>
            <div class="col-sm-2">
                <div class='input-group date' id='endTime'>
                    <input type='text' class="form-control coupon-end" name="endTime" datatype="date" errormsg="请选择活动结束时间" <#if coupon??>value="${(coupon.endTime?string("yyyy-MM-dd"))!}"</#if>/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">发放对象:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control invest-coupon-total_count"  value="全部用户"  readonly="true">
                <input type="hidden" class="user-group-hid" name="userGroup" value="ALL_USER"  >
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">可投资标的: </label>
            <div class="col-sm-3">
                <#list productTypes as productType>
                    <label><input type="checkbox" name="productTypes" class="productType"
                                  <#if productType_index == 0>checked="checked"</#if>
                                  value="${productType.name()}">${productType.getName()}
                    </label>
                </#list>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">使用条件: </label>

            <div class="col-sm-8">
                <div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number " name="investLowerLimit" placeholder="" datatype="*" errormsg="使用条件金额不能为空" <#if coupon??>value="${coupon.investLowerLimit}"</#if>><div class="item-invest">元可用</div>
            </div>
        </div>

        <div class="form-group" >
            <label  class="col-sm-2 control-label">与其他优惠券共用:</label>
            <div class="col-sm-3">

                <label><input type="checkbox" name="shared" class="shared" <#if coupon??&&coupon.shared>checked</#if>/>
                </label>

            </div>
        </div>

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
            <label  class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave">确认创建</button>
            </div>
        </div>
    </form>
</div>
<!-- content area end -->
</@global.main>