<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-birthday-coupon.js" headLab="activity-manage" sideLab="createBirthdayCoupon" title="创建生日红包">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/activity-manage/coupon" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">名称:</label>
            <div class="col-sm-4">
                <span class="form-control">生日福利</span>
                <input class="couponType" name="couponType" value="BIRTHDAY_COUPON" type="hidden">
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
            <label  class="col-sm-2 control-label">首月利率翻倍倍数: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control birthday-benefit" name="birthdayBenefit" placeholder="" datatype="*" errormsg=">首月利率翻倍倍数不能为空" <#if coupon??>value="${coupon.birthdayBenefit!}"</#if>>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label ">有效期限: </label>
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
                <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave" <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN')">disabled</@security.authorize>>确认创建</button>
            </div>
        </div>
    </form>
</div>
<!-- content area end -->
</@global.main>