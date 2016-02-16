<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="edit-user.js" headLab="user-manage" sideLab="user-manage" title="用户编辑">

<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal" action="/user-manage/user/edit" method="post">
        <div class="form-group">
            <label class="col-sm-2 control-label">登录名：</label>

            <div class="col-sm-3">
                <p class="form-control-static">${user.loginName}</p>
                <input type="hidden" class="loginName" name="loginName" value="${user.loginName}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">姓名：</label>

            <div class="col-sm-3">
                <p class="form-control-static">${user.userName!}</p>
                <input type="hidden" name="userName" value="${user.userName!}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">身份证：</label>

            <div class="col-sm-3">
                <p class="form-control-static">${user.identityNumber!}</p>
                <input type="hidden" name="identityNumber" value="${user.identityNumber!}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">银行卡：</label>

            <div class="col-sm-3">
                <p class="form-control-static">${user.bankCardNumber!}</p>
                <input type="hidden" name="identityNumber" value="${user.bankCardNumber!}"/>
            </div>
        </div>
        <div class="form-group">
            <label for="mobile" class="col-sm-2 control-label">手机号码：</label>

            <div class="col-sm-3">
                <@global.role hasRole="'ADMIN','OPERATOR','OPERATOR_ADMIN'">
                <input name="mobile" id="mobile" type="text" class="form-control" maxlength="11" value="${(user.mobile)!}"/>
                </@global.role>

                <@global.role hasRole="'CUSTOMER_SERVICE'">
                <p class="form-control-static">${(user.mobile)!}</p>
                </@global.role>
            </div>
        </div>
        <div class="form-group">
            <label for="email" class="col-sm-2 control-label">电子邮件：</label>

            <div class="col-sm-3">
                <@global.role hasRole="'ADMIN','OPERATOR','OPERATOR_ADMIN'">
                <input name="email" id="email" type="email" class="form-control" value="${(user.email)!}"/>
                </@global.role>

                <@global.role hasRole="'CUSTOMER_SERVICE'">
                <p class="form-control-static">${(user.email)!}</p>
                </@global.role>
            </div>
        </div>
        <div class="form-group">
            <label for="referrer" class="col-sm-2 control-label">
                <#if user.isReferrerStaff>
                <span class="glyphicon glyphicon-user"></span>
                </#if>
                推荐人：
            </label>

            <div class="col-sm-3">
                <@global.role hasRole="'ADMIN','OPERATOR','OPERATOR_ADMIN'">
                <input name="referrer" id="referrer" type="text" class="form-control" value="${(user.referrer)!}"/>
                </@global.role>

                <@global.role hasRole="'CUSTOMER_SERVICE'">
                <p class="form-control-static">${(user.referrer)!}</p>
                </@global.role>
            </div>
        </div>
        <div class="form-group">
            <label for="status" class="col-sm-2 control-label">状态：</label>

            <div class="col-sm-3">
                <label class="radio-inline">
                    <input type="radio" name="status" id="status-active" value="ACTIVE"
                           <@global.role hasRole="'CUSTOMER_SERVICE'">disabled="disabled"</@global.role>
                           <#if user.status?? && user.status=="ACTIVE">checked="checked"</#if>>正常
                </label>
                <label class="radio-inline">
                    <input type="radio" name="status" id="status-in-active" value="INACTIVE"
                           <@global.role hasRole="'CUSTOMER_SERVICE'">disabled="disabled"</@global.role>
                           <#if user.status?? && user.status=="INACTIVE">checked="checked"</#if>>禁用
                </label>
            </div>
        </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">已开通自动投标：</label>

                    <div class="col-sm-3">
                        <p class="form-control-static"><#if user.autoInvestStatus=="1">是<#else>否</#if></p>
                        <input type="hidden" name="autoInvestStatus" value="${user.autoInvestStatus!}"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="referrer" class="col-sm-2 control-label">角色：</label>

                    <div class="col-sm-3">
                <input type="hidden" name="roles" value="USER"/>
                <#list roles as roleItem>
                    <#if roleItem.name() != 'USER'>
                        <div class="checkbox">
                            <label><input type="checkbox" name="roles"
                                          <#if user.roles?? && user.roles?seq_contains(roleItem.name())>checked="checked"</#if>
                                          <@global.role hasRole="'CUSTOMER_SERVICE'">disabled="disabled"</@global.role>
                                          value="${roleItem.name()}">${roleItem.getDescription()}
                            </label>
                        </div>
                    </#if>
                </#list>
            </div>
        </div>

        <#if errorMessage??>
            <div class="form-group console-error-message">
                <div class="col-sm-offset-2 col-sm-3">
                    <div class="alert alert-danger" role="alert">${errorMessage}</div>
                </div>
            </div>
        </#if>

        <div class="form-group web-error-message">
            <div class="col-sm-offset-2 col-sm-3">
                <div class="alert alert-danger message" role="alert"></div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <@global.role hasRole="'ADMIN','OPERATOR'">
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-3">
                <input class="btn btn-default btn-submit" type="submit" value="提交">
                <input class="btn btn-default" type="reset" value="重置">
            </div>
        </div>
        </@global.role>

        <@global.role hasRole="'ADMIN','OPERATOR_ADMIN'">
        <#if taskId??>
        <input type="hidden" value="${taskId}" class="taskId">
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-3">
                <input class="btn btn-default btn-submit" type="submit" value="同意">
                <input class="btn btn-default btn-refuse" type="button" value="拒绝">
            </div>
        </div>
        </#if>
        </@global.role>
    </form>
</div>

<!-- Modal -->
<div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <h5>确认修改？</h5>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-default btn-submit">确认</button>
            </div>
        </div>
    </div>
</div>
<!-- content area end -->
</@global.main>