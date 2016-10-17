<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "../macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="iphone7-lottery.js" headLab="activity-manage" sideLab="iphone7-lottery" title="iphone7活动参与情况">

<div class="col-md-10">
    <div class="table-responsive">
        <div class="form-horizontal activity-form">
            <div class="form-group">
                <label class="col-sm-2 control-label">抽奖阶段</label>
                <div class="col-sm-4">
                    <input name="investAmount" id="investAmount" class="form-control" maxlength="8"/>
                </div>
                <label class="col-sm-1 control-label">(万元)</label>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">中奖号码</label>
                <div class="col-sm-4">
                    <input name="lotteryNumber" id="lotteryNumber" class="form-control" maxlength="8"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2"></div>
                <div class="col-sm-4 text-danger error-message">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-2"></div>
                <div class="col-sm-4">
                    <button type="button" class="btn btn-sm btn-primary config-item-submit-btn">提交</button>
                </div>
            </div>
        </div>
        <table class="table table-bordered table-hover">
            <caption>中奖码配置</caption>
            <thead>
            <tr>
                <th>抽奖阶段</th>
                <th>抽奖号码</th>
                <th>状态</th>
                <th>审核人</th>
                <th>审核时间</th>
            </tr>
            </thead>
            <tbody>
                <#list approvedConfigList as configItem>
                <tr>
                    <td>${configItem.investAmount}万</td>
                    <td>${configItem.lotteryNumber}</td>
                    <td>${configItem.status.description}</td>
                    <td>${configItem.auditedBy}</td>
                    <td>${configItem.auditedTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                </tr>
                </#list>
            </tbody>
        </table>

        <table class="table table-bordered table-hover">
            <caption>待审核列表</caption>
            <thead>
            <tr>
                <th>抽奖阶段</th>
                <th>抽奖号码</th>
                <th>提交人</th>
                <th>提交时间</th>
                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                    <th>审核</th>
                </@security.authorize>
            </tr>
            </thead>
            <tbody>
                <#list toApproveConfigList as configItem>
                <tr>
                    <td>${configItem.investAmount}万元</td>
                    <td>${configItem.lotteryNumber}</td>
                    <td>${configItem.createdBy}</td>
                    <td>${configItem.createdTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                        <td>
                            <button class="config-item-approve-btn" data-id="${configItem.id}">审核</button>
                            <button class="config-item-refuse-btn" data-id="${configItem.id}">驳回</button>
                        </td>
                    </@security.authorize>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>