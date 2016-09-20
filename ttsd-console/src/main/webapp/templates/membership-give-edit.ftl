<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="membership-give-edit.js" headLab="membership-manage" sideLab="membershipGiveCreate" title="会员发放创建">

<div class="col-md-10">
    <form action="/membership-manage/give/edit-view" method="post" class="form-horizontal form-list">
        <input type="text" id="membershipGiveId" hidden="hidden" value="${(membershipGiveId?c)!"0"}">
        <input type="text" id="importUsersId" hidden="hidden" class="importUsersId" value="${(membershipGiveId?c)!"0"}">

        <div class="form-group">
            <label class="col-sm-2 control-label">发放对象:</label>

            <div class="col-sm-4">
                <select class="selectpicker b-width jq-userGroup">
                    <#list userGroups as userGroup>
                        <option value="${userGroup.name()}"
                                <#if originUserGroup?? && originUserGroup.name() == userGroup.name()>selected</#if>>${userGroup.getDescription()}</option>
                    </#list>
                </select>
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
                        <option value="${membershipLevel}"
                                <#if originMembershipLevel?? && originMembershipLevel == membershipLevel>selected</#if>>
                            V${membershipLevel}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="form-group input-append">
            <label class="col-sm-2 control-label">活动期限: </label>

            <div class="col-sm-2">
                <div class='input-group date' id='datetimepicker1'>
                    <input type='text' class="form-control jq-start-date" <#if receiveStartTime??>
                           value="${receiveStartTime?date}" </#if>datatype="date" errormsg="活动开始时间需要正确填写"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
            <div class="line-size">-</div>
            <div class="col-sm-2">
                <div class='input-group date' id='datetimepicker2'>
                    <input type='text' class="form-control jq-end-date" <#if receiveEndTime??>
                           value="${receiveEndTime?date}" </#if>datatype="date" errormsg="活动截止时间需要正确填写"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">会员期限: </label>

            <div class="col-sm-4">
                <div class="item-invest">用户领取会员后</div>
                <input type="text" class="form-control ui-autocomplete-input jq-valid-period"
                       <#if validPeriod??>value="${validPeriod}"</#if>
                       datatype="number" autocomplete="off" placeholder="" errormsg="有效期需要正确填写">

                <div class="item-invest">
                    天内有效
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">短信通知: </label>

            <div class="col-sm-4">
                <input type="checkbox" class="jq-smsNotify" <#if smsNotify?? && smsNotify>checked="checked"</#if>>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">操作: </label>

            <div class="col-sm-4">
                <button type="button" class="btn btn-primary submit-btn">确认创建</button>
            </div>
        </div>

    </form>

</div>

</@global.main>