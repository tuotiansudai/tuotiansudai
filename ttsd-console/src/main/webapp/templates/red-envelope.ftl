<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-red-envelope.js" headLab="activity-manage" sideLab="createCoupon" title="创建红包优惠券">

<!-- content area begin -->
<div class="col-md-10">

    <form action="/activity-manage/coupon" method="post" class="form-horizontal form-list">
        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4">
                <select class="selectpicker jq-b-type" id="businessType">
                    <option value="RED_ENVELOPE" selected>创建投资红包</option>
                    <option value="INTEREST_COUPON">创建加息券</option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">红包名称:</label>
            <div class="col-sm-4">
                <span class="form-control">投资红包</span>
                <input class="couponType" name="couponType" value="RED_ENVELOPE" type="hidden">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">红包来源描述</label>

            <div class="col-sm-4">
                <input type="text" class="form-control coupon-source" name="couponSource" placeholder=""
                       <#if coupon??>value="${coupon.couponSource!}"</#if> datatype="*" errormsg="红包来源描述不能为空">
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">红包金额(元): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-number" name="amount" placeholder="" datatype="*" errormsg="红包金额不能为空" <#if coupon??>value="${coupon.amount!}"</#if>>
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label ">活动期限: </label>
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
            <label  class="col-sm-2 control-label">优惠券有效天数(天): </label>
            <div class="col-sm-7">
                <div class="item-invest"><input type="radio" name="useDeadline" value="0" <#if !coupon?? || (coupon?? && coupon.deadline !=0)>checked</#if> >用户收到优惠券后</div><input type="text" class="form-control invest-quota coupon-deadline" name="deadline" placeholder="" value="${(coupon.deadline)!'0'}" datatype="n"><div class="item-invest">天内有效</div>
            </div>
            <div class="col-sm-5">
                <div class="item-invest" style="margin-left: 220px"><input type="radio" name="useDeadline" value="1" <#if (coupon.failureTime)??>checked</#if>>截止日期</div>
                <div class='input-group date' id='failureTime'>
                    <input type='text' class="form-control coupon-failureTime" name="failureTime" datatype="date" <#if (coupon.failureTime)??>value="${(coupon.failureTime?string("yyyy-MM-dd"))!}"</#if>/>
                    <span class="input-group-addon">
					<span class="glyphicon glyphicon-calendar"></span>
					</span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">发放对象:</label>
            <div class="col-sm-2">
                <select class="selectpicker jq-b-type userGroup" name="userGroup">
                    <#assign notUserGroups = ['INVESTED_USER','REGISTERED_NOT_INVESTED_USER','STAFF','STAFF_RECOMMEND_LEVEL_ONE','AGENT','NEW_REGISTERED_USER','EXCHANGER','WINNER','EXPERIENCE_INVEST_SUCCESS','EXPERIENCE_REPAY_SUCCESS','NOT_ACCOUNT_NOT_INVESTED_USER'] />
                    <#list userGroups as userGroup>
                        <#if !(notUserGroups?seq_contains(userGroup.name()))>
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
                <input type="text" readonly class="form-control give-number" name="totalCount" placeholder="" value="${initNum?string('0')!}"  datatype="n" errormsg="发放数量需要填写数字" >
            </div>
        </div>

        <div class="form-group coupon-hide coupon-deposit">
            <label class="col-sm-2"></label>
            <div class="col-sm-4 coupon-agent-channel">
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">可投资标的: </label>
            <div class="col-sm-5">
                <label>
                    <input type="radio" name="productTypes" class="productType" value="_30,_90,_180,_360">全部
                    <input type="radio" name="productTypes" class="productType" value="_90,_180,_360">大于60天
                    <input type="radio" name="productTypes" class="productType" value="_180,_360">大于120天
                    <input type="radio" name="productTypes" class="productType" value="_360">大于200天
                </label>
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

        <div class="form-group">
            <label class="col-sm-2 control-label">备注</label>

            <div class="col-sm-4">
                <input type="text" class="form-control coupon-comment" name="comment"
                       <#if coupon??>value="${coupon.comment!}"</#if>>
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