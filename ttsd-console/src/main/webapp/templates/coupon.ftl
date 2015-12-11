<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-coupon.js" headLab="activity-manage" sideLab="createCoupon" title="创建投资体验券">

<!-- content area begin -->
<div class="col-md-10">
	<form action="" method="get" class="form-horizontal form-list">
		<div class="form-group">
			<label class="col-sm-2 control-label">投资体验券名称: </label>
			<div class="col-sm-4">
				<input type="text" class="form-control" name="couponName" placeholder="" value="" datatype="*" errormsg="投资体验券名称不能为空">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label">投资体验券金额(元): </label>
			<div class="col-sm-4">
				<input type="text" class="form-control coupon-number" name="moneyNumber" placeholder="" value="" datatype="*" errormsg="投资体验券金额不能为空">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label">活动期限: </label>
			<div class="col-sm-2">
				<div class='input-group date' id='startTime'>
					<input type='text' class="form-control" name="startTime" value="" datatype="date" errormsg="请选择活动开始时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
			<div class="line-size">-</div>
			<div class="col-sm-2">
				<div class='input-group date' id='endTime'>
					<input type='text' class="form-control" name="endTime" value="" datatype="date" errormsg="请选择活动结束时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">发放数量(张): </label>
			<div class="col-sm-4">
				<input type="text" class="form-control" name="giveNumber" placeholder="" value="" datatype="n" errormsg="发放数量需要填写数字">
			</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label">发放对象: </label>
			<div class="col-sm-2">
				<select class="selectpicker" name="giveObject">
					<option value="新注册用户">新注册用户</option>
				</select>
			</div>
			<div class="line-size">[自动发放]</div>
		</div>
		<div class="form-group">
			<label  class="col-sm-2 control-label">标的使用条件: </label>
			<div class="col-sm-2">
				<select class="selectpicker" name="markCondition">
					<option value="所有标的">所有标的</option>
				</select>
			</div>
		</div>
		<div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
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