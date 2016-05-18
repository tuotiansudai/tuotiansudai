<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-coupon.js" headLab="activity-manage" sideLab="createCoupon" title="创建投资体验券">

<!-- content area begin -->
<div class="col-md-10">
	<form action="/activity-manage/coupon" method="post" class="form-horizontal form-list">
		<div class="form-group">
			<label class="col-sm-2 control-label">体验券名称:</label>
			<div class="col-sm-4">

                <select class="selectpicker jq-b-type couponType" name="couponType">
					<#list couponTypes as couponType>
						<#if couponType.name() == 'NEWBIE_COUPON' || couponType.name() == 'INVEST_COUPON'>
                        <option value="${couponType.name()}">${couponType.getName()}</option>
						</#if>
					</#list>
                </select>

			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label">体验券金额(元): </label>
			<div class="col-sm-4">
				<input type="text" class="form-control coupon-number" name="amount" placeholder="" <#if coupon??>value="${coupon.amount!}"</#if> datatype="*" errormsg="投资体验券金额不能为空">
			</div>
		</div>
        <div class="form-group">
            <label class="col-sm-2 control-label">发放对象:</label>
            <div class="col-sm-2 coupon-hide invest-coupon">

                <select class="selectpicker jq-b-type userGroup" name="userGroup">
					<#list userGroups as userGroup>
						<#if userGroup.name() != 'NEW_REGISTERED_USER' && userGroup.name() != 'EXCHANGER' && userGroup.name() != 'WINNER'>
                            <option value="${userGroup.name()}">${userGroup.getDescription()}</option>
						</#if>
					</#list>
                </select>

            </div>

            <div class="file-btn coupon-hide">
                <input type="file" id="file-in">
                重新导入
            </div>
            <input type="hidden" name="file" id="import-file">

            <div class="col-sm-4 newbie-coupon">
                <input type="text" class="form-control invest-coupon-total_count"  value="新注册用户"  readonly="true">
                <input type="hidden" class="user-group-hid" name="userGroup" value="NEW_REGISTERED_USER"  >
            </div>
        </div>

        <div class="form-group coupon-hide coupon-table">
            <label class="col-sm-2"></label>
            <div class="col-sm-4 data-table">
                <table class="table table-bordered">
                </table>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">预计发放数量(张): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control give-number" name="totalCount" placeholder="" <#if coupon??>value="${coupon.totalCount?string('0')!}"</#if>  datatype="n" errormsg="发放数量需要填写数字" >
            </div>
        </div>

        <div class="form-group coupon-hide coupon-deposit">
            <label class="col-sm-2"></label>
            <div class="col-sm-4 coupon-agent-channel">
            </div>
        </div>

		<div class="form-group">
			<label  class="col-sm-2 control-label ">活动期限: </label>
			<div class="col-sm-2">
				<div class='input-group date' id='startTime'>
					<input type='text' class="form-control coupon-start" name="startTime" <#if coupon??>value="${(coupon.startTime?string("yyyy-MM-dd HH:mm"))!}"</#if>  datatype="date" errormsg="请选择活动开始时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
			<div class="line-size">-</div>
			<div class="col-sm-2">
				<div class='input-group date' id='endTime'>
					<input type='text' class="form-control coupon-end" name="endTime" <#if coupon??>value="${(coupon.endTime?string("yyyy-MM-dd HH:mm"))!}"</#if>  datatype="date" errormsg="请选择活动结束时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
		</div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">优惠券有效天数(天): </label>
            <div class="col-sm-8">
                <div class="item-invest">用户收到优惠券后</div><input type="text" class="form-control invest-quota coupon-deadline" name="deadline" placeholder="" <#if coupon??>value="${coupon.deadline!}"</#if> datatype="n"  errormsg="有效天数需要填写数字"><div class="item-invest">天内有效</div>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">使用条件: </label>

			<div class="col-sm-8">
				<div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number " name="investLowerLimit" placeholder="" <#if coupon??>value="${coupon.investLowerLimit!}"</#if> datatype="*" errormsg="使用条件金额不能为空"><div class="item-invest">元可用</div>
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
        <div class="form-group coupon-hide invest-coupon" >
            <label  class="col-sm-2 control-label">短信提醒: </label>
            <div class="col-sm-3">

                    <label><input type="checkbox" name="smsAlert" class="smsAlert"/>
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
				<button type="button" class="btn btn-sm btn-primary btnSearch" id="btnSave" <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN')">disabled</@security.authorize>>确认创建</button>
			</div>
		</div>
	</form>
</div>
<!-- content area end -->
</@global.main>