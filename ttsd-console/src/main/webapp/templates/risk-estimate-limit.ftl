<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="risk-estimate-limit.js" headLab="user-manage" sideLab="riskEstimateLimit" title="投资限额管理">

<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal showForm" >
        <div class="form-group">
            <label class="col-sm-1 control-label">保守型</label>
            <div class="col-sm-2">以下问题根据您的财务状况、出借经验、出借风格、风险偏好和风险承受能力等对您进
                <span class="form-control">${CONSERVATIVE!}元</span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-1 control-label">稳健型</label>
            <div class="col-sm-2">
                <span class="form-control">${STEADY!}元</span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-1 control-label">积极型</label>
            <div class="col-sm-2">
                <span class="form-control">${POSITIVE!}元</span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-1 control-label"></label>
            <div class="col-sm-2">
                <button type="button" id="edit-redict" class="btn jq-btn-form btn-primary message-save ">修改</button>
            </div>
        </div>
    </form>


    <form class="form-horizontal editForm" action="/anxin-sign/whitelist" method="post"  hidden="hidden">
        <div class="form-group">
            <label class="col-sm-1 control-label">保守型(元):</label>
            <div class="col-sm-2">
                <input type="text"  name="conservative" class="form-control number-input" value="${CONSERVATIVE!}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-1 control-label">稳健型(元):</label>
            <div class="col-sm-2">
                <input type="text" name="steady" class="form-control number-input" value="${STEADY!}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-1 control-label">积极型(元):</label>
            <div class="col-sm-2">
                <input type="text" name="positive" class="form-control number-input" value="${POSITIVE!}"/>
            </div>
        </div>
        <div class="form-group web-error-message">
            <div class="col-sm-3">
                <div class="alert alert-danger message" role="alert"></div>
            </div>
        </div>
        <div class="form-group">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <label class="col-sm-1 control-label"></label>
            <div class="col-sm-2">
                <button type="button" id="btnCancel" class="btn jq-btn-form btn-primary message-save">取消</button>
                <button type="button" id="btnSave" class="btn jq-btn-form btn-primary message-save">保存</button>
            </div>
        </div>
    </form>

</div>
<!-- content area end -->
</@global.main>