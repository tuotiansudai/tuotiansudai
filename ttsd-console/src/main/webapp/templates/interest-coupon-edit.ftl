<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-interest-coupon.js" headLab="activity-manage" sideLab="createInterestCoupon" title="创建加息券">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/activity-manage/coupon" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label">加息券名称:</label>
            <div class="col-sm-4">
                <span class="form-control">加息体验券</span>
                <input class="couponType" name="couponType" value="INTEREST_COUPON" type="hidden">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">红包来源描述</label>

            <div class="col-sm-4">
                <input type="text" class="form-control coupon-source" name="couponSource" placeholder=""
                       <#if coupon??>value="${coupon.couponSource!}"</#if> datatype="*" errormsg="创建失败，请输入来源描述">
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">加息券利率(%): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-rate" name="rate" placeholder="" <#if coupon??>value="${(coupon.rate*100)!}"</#if> datatype="*" errormsg="加息劵利率不能为空">
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
                <div class="item-invest">用户收到优惠券后</div>
                <input type="text" class="form-control invest-quota coupon-deadline" name="deadline" placeholder=""
                       <#if coupon??>value="${coupon.deadline?c!}"</#if> datatype="n" errormsg="有效天数需要填写数字">

                <div class="item-invest">天内有效</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">发放对象:</label>
            <div class="col-sm-2 invest-coupon">

                <select class="selectpicker jq-b-type userGroup" name="userGroup">
                    <#assign notUserGroups = ['EXCHANGER','WINNER','EXPERIENCE_INVEST_SUCCESS','EXPERIENCE_REPAY_SUCCESS'] />
                    <#list userGroups as userGroup>
                        <#if !(notUserGroups?seq_contains(userGroup.name()))>
                            <option value="${userGroup.name()}"
                                <#if coupon??&&coupon.userGroup==userGroup>selected</#if>>${userGroup.getDescription()}</option>
                        </#if>
                    </#list>
                </select>
            </div>
            <div class="file-btn <#if coupon.userGroup != 'IMPORT_USER'>import-hidden</#if>">
                <input type="file" id="file-in">
                重新导入
            </div>
            <input type="hidden" name="file" id="import-file" >
        </div>

        <div class="form-group <#if coupon.userGroup != 'IMPORT_USER'>coupon-hide</#if> coupon-table">
            <label class="col-sm-2"></label>
            <div class="col-sm-4 data-table">
                <table class="table table-bordered">
                    <#if coupon.userGroup == 'IMPORT_USER'>
                    <#list importUsers as importUser>
                        <tr class="name-tr"><td>${(importUser_index+1)!}</td><td>${importUser!}</td></tr>
                    </#list>
                    </#if>
                </table>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">预计发放数量(张): </label>
            <div class="col-sm-4">
                <input type="text" readonly class="form-control give-number" name="totalCount" placeholder="" <#if coupon??>value="${coupon.totalCount?string('0')!}"</#if>  datatype="n" errormsg="发放数量需要填写数字" >
            </div>
        </div>

        <div class="form-group coupon-deposit <#if !(agentsOrChannels?? && agentsOrChannels?size gt 0)>coupon-hide</#if>">
            <label class="col-sm-2"></label>
            <div class="col-sm-4 coupon-agent-channel">
                <#if agentsOrChannels?? && agentsOrChannels?size gt 0>
                <#if coupon.userGroup == 'AGENT'>
                    <#list agents as agent>
                    <label><input type="checkbox" class="agent" name="agents" value="${agent!}" <#if agentsOrChannels?seq_contains(agent)>checked</#if>>${agent!}</label>
                    </#list>
                <#else>
                    <#list channels as channel>
                    <label><input type="checkbox" class="channel" name="channels" value="${channel!}" <#if agentsOrChannels?seq_contains(channel)>checked</#if>>${channel!}</label>
                    </#list>
                </#if>
                </#if>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">可投资标的: </label>
            <div class="col-sm-3">
                <#list productTypes as productType>
                    <#if productType.name() != 'EXPERIENCE'>
                    <label><input type="checkbox" name="productTypes" class="productType"
                                  <#if coupon?? && coupon.productTypes?seq_contains(productType.name())>checked="checked"</#if>
                                  value="${productType.name()}">${productType.getName()}
                    </label>
                    </#if>
                </#list>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">使用条件: </label>

            <div class="col-sm-8">
                <div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number " name="investLowerLimit" placeholder="" <#if coupon??>value="${coupon.investLowerLimit!}"</#if> datatype="*" errormsg="使用条件金额不能为空"><div class="item-invest">元可用</div>
            </div>
        </div>

        <div class="form-group invest-coupon" >
            <label  class="col-sm-2 control-label">短信提醒: </label>
            <div class="col-sm-3">

                <label><input type="checkbox" name="smsAlert" class="smsAlert"
                              <#if coupon??&&coupon.smsAlert>checked</#if> />
                </label>

            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">备注</label>

            <div class="col-sm-4">
                <input type="text" class="form-control coupon-comment" name="comment"
                       <#if coupon??>value="${coupon.comment!}"</#if>>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="id" <#if coupon??>value="${coupon.id?string('0')}"</#if>/>
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