<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="security" sideLab="anxinSwitch" title="安心签开关">

<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal" action="/anxin-sign/switch" method="post">
        <div class="form-group">
            <label class="col-sm-1 control-label">安心签开关</label>

            <div class="col-sm-2">
                <div class="radio-inline">
                    <label><input type="radio" name="anxinSwitch" value="true" <#if switch==true>checked</#if>>开</label>
                </div>
                <div class="radio-inline">
                    <label><input type="radio" name="anxinSwitch" value="false" <#if switch==false>checked</#if>>关</label>
                </div>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button class="btn btn-sm btn-primary query" type="submit">查询</button>
        </div>
    </form>

    <form class="form-horizontal" action="/anxin-sign/whitelist" method="post">
        <div class="form-group">
            <label class="col-sm-1 control-label">白名单手机号</label>

            <div class="col-sm-2">
                <input type="text" name="mobile" class="form-control"/>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button class="btn btn-sm btn-primary query" type="submit">提交</button>
        </div>
    </form>

    <div class="row" style="width: 400px">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>手机号</th>
                <th>姓名</th>
            </tr>
            </thead>
            <tbody>
                <#list whitelist as item>
                <tr>
                    <td>${item.mobile!}</td>
                    <td>${item.userName!}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

</div>
<!-- content area end -->
</@global.main>