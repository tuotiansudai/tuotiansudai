<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="real-time-status.js" headLab="finance-manage" sideLab="realTimeStatus" title="联动优势余额查询">

<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">
        <div class="form-group">
            <label for="project">账户类型</label>
            <select class="selectpicker" name="type">
                <option value="user" <#if !(type??) || type=='user'>selected</#if>>用户账户</option>
                <option value="platform" <#if type?? && type=='platform'>selected</#if>>平台账户</option>
                <option value="loan" <#if type?? && type=='loan'>selected</#if>>标的账户</option>
            </select>
        </div>

        <div class="login-name form-group" <#if type?? && type != 'user'>style="display: none"</#if>>
            <label for="loginName">用户名</label>
            <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input"/>
        </div>

        <div class="loan form-group" <#if !(type??) || type != 'loan'>style="display: none"</#if>>
            <label for="loanId">标的号</label>
            <input type="text" id="loanId" name="loanId" class="form-control ui-autocomplete-input"/>
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