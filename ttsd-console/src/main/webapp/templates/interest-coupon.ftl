<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
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
            <label  class="col-sm-2 control-label">加息券利率(%): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-rate" name="rate" placeholder="" <#if coupon??>value="${coupon.rate!}"</#if> datatype="*" errormsg="加息劵利率不能为空">
            </div>
        </div>

        <div class="form-group">
            <label  class="col-sm-2 control-label">投资上限(元): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-number" name="investUpperLimit" placeholder="" <#if coupon??>value="${coupon.investUpperLimit!}"</#if> datatype="*" errormsg="加息券投资上限不能为空">
            </div>
        </div>

        <div class="form-group invest-coupon" >
            <label  class="col-sm-2 control-label">有效期限(天): </label>
            <div class="col-sm-4">
                <input type="text" class="form-control coupon-deadline" name="deadline" placeholder="" <#if coupon??>value="${coupon.deadline!}"</#if> datatype="*" errormsg="有效期限不能为空">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">发放对象:</label>
            <div class="col-sm-2 invest-coupon">

                <select class="selectpicker jq-b-type userGroup" name="userGroup">
                    <#list userGroups as userGroup>
                        <#if userGroup.name() != 'NEW_REGISTERED_USER'>
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

        <div class="form-group">
            <label class="col-sm-2"></label>
            <div class="col-sm-4 data-table">
                <table class="table table-bordered">
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                    <tr><td>1</td><td>2</td></tr>
                </table>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">预计发放数量(张): </label>
            <div class="col-sm-4">
                <input type="text" readonly class="form-control give-number" name="totalCount" placeholder="" <#if coupon??>value="${coupon.totalCount?string('0')!}"</#if>  datatype="n" errormsg="发放数量需要填写数字" >
            </div>
        </div>

        <div class="form-group coupon-hide coupon-deposit">
            <label class="col-sm-2"></label>
            <div class="col-sm-4 coupon-agent-channel">
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
                <div class="item-invest">投资满</div><input type="text" class="form-control invest-quota coupon-number " name="investLowerLimit" placeholder="" <#if coupon??>value="${coupon.investLowerLimit!}"</#if> datatype="*" errormsg="使用条件金额不能为空"><div class="item-invest">元可用</div>
            </div>
        </div>

        <div class="form-group invest-coupon" >
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