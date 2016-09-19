<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="membership-give-edit.js" headLab="membership-manage" sideLab="" title="会员发放创建">

<div class="col-md-10">
    <form action="/membership-manage/give/edit" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">发放对象:</label>

            <div class="col-sm-4">
                <select class="selectpicker b-width jq-userGroup">
                    <#list userGroups as userGroup>
                        <option value="${userGroup.name()}">${userGroup.getDescription()}</option>
                    </#list>
                </select>
                <input type="hidden" class="jq-mark-type" value=""/>
            </div>
            <div class="file-btn">
                <input type="file" id="importBtn">
                导入用户
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">会员等级:</label>

            <div class="col-sm-4">
                <select class="selectpicker b-width jq-membershipLevel">
                    <#list membershipLevels as membershipLevel>
                        <option value="${membershipLevel}">V${membershipLevel}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="form-group input-append">
            <label class="col-sm-2 control-label">活动期限: </label>

            <div class="col-sm-4">
                <div class='input-group date' id='datetimepicker6'>
                    <input type='text' class="form-control jq-start-date" datatype="date" errormsg="活动开始时间需要正确填写"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id='datetimepicker7'>
                    <input type='text' class="form-control jq-end-date" datatype="date" errormsg="活动截止时间需要正确填写"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">会员期限: </label>

            <div class="col-sm-4">
                用户领取会员后
                <input type="text" class="form-control ui-autocomplete-input jq-loaner-income" datatype="number"
                       autocomplete="off"
                       placeholder="" errormsg="有效期需要正确填写">天内有效
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">短信通知: </label>

            <div class="col-sm-4 receiver-type">
                <input type="checkbox" name="smsNotify">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>

            <div class="col-sm-4 form-error">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">操作: </label>

            <div class="col-sm-4">
                <button type="button" class="btn jq-btn-form btn-primary">确认创建</button>
            </div>
        </div>

    </form>

</div>

</@global.main>