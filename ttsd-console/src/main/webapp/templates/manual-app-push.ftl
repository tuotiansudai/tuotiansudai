<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-coupon.js" headLab="app-push-manage" sideLab="createManualAppPush" title="创建手动推送">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/app-push-manage/manual-app-push" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">通知名称:</label>
            <div class="col-sm-4">
                <input type="text" class="form-control " name="name" placeholder=""  datatype="*" errormsg="通知名称不能为空">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">推送类型: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="name" placeholder=""  datatype="*" errormsg="推送类型不能为空">
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">推送对象: </label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="pushObjects" placeholder=""  datatype="*" >
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">推送渠道: </label>
            <div class="col-sm-2">
                <select class="selectpicker" name="pushSource">
                    <option value="所有标的">所有标的</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label  class="col-sm-2 control-label">活动期限: </label>
            <div class="col-sm-2">
                <div class='input-group date' id='startTime'>
                    <input type='text' class="form-control coupon-start" name="startTime" <#if coupon??>value="${coupon.startTime?string("yyyy-MM-dd")}"</#if>  datatype="date" errormsg="请选择活动开始时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
            <div class="line-size">-</div>
            <div class="col-sm-2">
                <div class='input-group date' id='endTime'>
                    <input type='text' class="form-control coupon-end" name="endTime" <#if coupon??>value="${coupon.endTime?string("yyyy-MM-dd")}"</#if>  datatype="date" errormsg="请选择活动结束时间"/>
					<span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">发放数量(张): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control give-number" name="totalCount" placeholder="" <#if coupon??>value="${coupon.totalCount!}"</#if>  datatype="n" errormsg="发放数量需要填写数字">
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
            <label  class="col-sm-2 control-label">使用条件: </label>

            <div class="col-sm-8">
                <div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number " name="investQuota" placeholder="" <#if coupon??>value="${coupon.investQuota!}"</#if> datatype="*" errormsg="使用条件金额不能为空"><div class="item-invest">元可用</div>
            </div>

        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="form-group">
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