<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="real-time-status.js" headLab="finance-manage" sideLab="realTimeStatus" title="联动优势余额查询">

<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">
        <div class="form-group">
            <label for="type">查询类型</label>
            <select class="selectpicker" id="type" name="type">
                <option value="user" <#if !(type??) || type=='user'>selected</#if>>用户账户</option>
                <option value="platform" <#if type?? && type=='platform'>selected</#if>>平台账户</option>
                <option value="loan" <#if type?? && type=='loan'>selected</#if>>标的账户</option>
                <option value="transfer" <#if type?? && type=='transfer'>selected</#if>>交易状态</option>
            </select>
        </div>

        <div class="login-name form-group" <#if type?? && type != 'user'>style="display: none"</#if>>
            <label for="loginName">用户名</label>
            <input type="text" id="loginName" name="login-name" class="form-control ui-autocomplete-input" value="${loginName!}"/>
        </div>

        <div class="loan form-group" <#if !(type??) || type != 'loan'>style="display: none"</#if>>
            <label for="loanId">标的号</label>
            <input type="text" id="loanId" name="loan-id" class="form-control" value="${loanId!}"/>
        </div>

        <div class="transfer form-group" <#if !(type??) || type != 'transfer'>style="display: none"</#if>>
            <label for="businessType">交易类型</label>
            <select class="selectpicker" id="businessType" name="business-type">
                <option value="01" <#if !(businessType??) || businessType=='01'>selected</#if>>充值</option>
                <option value="02" <#if businessType?? && businessType=='02'>selected</#if>>提现</option>
                <option value="03" <#if businessType?? && businessType=='03'>selected</#if>>标的转账</option>
                <option value="04" <#if businessType?? && businessType=='04'>selected</#if>>转账</option>
            </select>
        </div>

        <div class="transfer form-group" <#if !(type??) || type != 'transfer'>style="display: none"</#if>>
            <label for="orderId">订单号</label>
            <input type="text" id="orderId" name="order-id" class="form-control" value="${orderId!}"/>
        </div>

        <div class="transfer form-group" <#if !(type??) || type != 'transfer'>style="display: none"</#if>>
            <label for="merDate">交易时间</label>
            <div class='input-group date'>
                <input type='text' class="form-control" name="mer-date" id='merDate' value="${(merDate?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>

        <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
    </form>

    <#if data??>
        <div class="panel panel-default">
            <div class="panel-body form-horizontal">
                <#list data?keys as key>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">${key}</label>

                        <div class="col-sm-3">
                            <p class="form-control-static">${data[key]}</p>
                        </div>
                    </div>
                <#else>
                    无结果
                </#list>
            </div>
        </div>
    </#if>
</div>

</@global.main>