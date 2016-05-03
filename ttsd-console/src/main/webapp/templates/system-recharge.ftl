<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="system-recharge.js" headLab="finance-manage" sideLab="systemRecharge" title="平台账户充值">

<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal" action="/finance-manage/system-recharge" method="post">
        <div class="form-group">
            <label for="login-name" class="col-sm-1 control-label system-recharge-label">资金来源账户</label>

            <div class="col-sm-8 col-lg-3">
                <input id="login-name" name="loginName" class="form-control" />
            </div>
            <label for="balance" class="col-sm-1 control-label system-recharge-label">来源账户余额:</label>
            <input id="balance" name="balance" class="system-recharge-balance" value="" />
        </div>
        <div class="form-group">
            <label for="amount" class="col-sm-1 control-label system-recharge-label">充值金额</label>

            <div class="col-sm-8 col-lg-7 system-recharge-amount">
                <input id="amount" name="amount" type="text" class="form-control" data-l-zero="deny"
                       data-v-min="0.00" placeholder="0.00" />
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="form-group console-error-message" style="display: none">
            <div class="col-sm-offset-1 col-sm-8 col-lg-7">
                <div class="alert alert-danger" role="alert"></div>
            </div>
        </div>

        <div class="form-group">
            <label for="description" class="col-sm-1 control-label system-recharge-label">操作</label>
            <div class="col-sm-offset-1 col-sm-2 system-recharge-btn">
                <input class="btn btn-default btn-submit" type="submit" value="提交">
                <input class="btn btn-default btn-submit" type="reset" value="重置">
            </div>
        </div>
    </form>
</div>

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

</@global.main>