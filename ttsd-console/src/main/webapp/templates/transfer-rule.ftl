<#import "macro/global.ftl" as global>

<@global.main pageCss="" pageJavascript="transfer-rule.js" headLab="transfer-manage" sideLab="transfer-rule" title="转让规则">

<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal" action="/transfer-manage/transfer-rule" method="post">
        <#if transferRule.updatedBy??>
            <div class="form-group">
                <div class="col-sm-2 alert alert-danger" role="alert">
                    正在被[${transferRule.updatedBy}]修改
                </div>
            </div>
        </#if>

        <div class="form-group">
            <label class="col-sm-2 control-label">手续费</label>

            <div class="col-sm-3 input-group <#if transferRule.updateLevelOneFee>has-error</#if>">
                <input name="levelOneFee" id="levelOneFee" type="number" class="form-control" maxlength="11" value="${transferRule.levelOneFee}"/>
                <div class="input-group-addon">%（持有30天以内）</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label"></label>

            <div class="col-sm-3 input-group <#if transferRule.updateLevelTwoFee>has-error</#if>">
                <input name="levelTwoFee" id="levelTwoFee" type="number" class="form-control" maxlength="11" value="${transferRule.levelTwoFee}"/>
                <div class="input-group-addon">%（持有30至90天）</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label"></label>

            <div class="col-sm-3 input-group <#if transferRule.updateLevelThreeFee>has-error</#if>">
                <input name="levelThreeFee" id="levelThreeFee" type="number" class="form-control" value="${transferRule.levelThreeFee}"/>
                <div class="input-group-addon">%（持有90天以上）</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">折价本金上限</label>

            <div class="col-sm-2 input-group <#if transferRule.updateDiscount>has-error</#if>">
                <input name="discount" id="discount" type="number" class="form-control" value="${transferRule.discount}"/>
                <div class="input-group-addon">%</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">不可转让天数</label>

            <div class="col-sm-3 input-group <#if transferRule.updateDaysLimit>has-error</#if>">
                <div class="input-group-addon">下次回款</div>
                <input name="daysLimit" id="daysLimit" type="number" class="form-control" value="${transferRule.daysLimit}"/>
                <div class="input-group-addon">天内不可转让</div>
            </div>
        </div>
        <div class="form-group" style="display: none">
            <div class="col-sm-offset-2 input-group <#if transferRule.updateMultipleTransferEnabled>has-error</#if>">
                <div class="checkbox">
                    <label>
                        <input type="checkbox"
                               name="multipleTransferEnabled"
                               <#if transferRule.multipleTransferEnabled>checked="checked"</#if>
                               value="true"> 允许多次转让
                    </label>
                </div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <#if applicationCommit || verificationCommit>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-2">
                    <div class="alert alert-danger message" role="alert">
                    <#if applicationCommit>修改申请已经提交</#if>
                    <#if verificationCommit>审批完成</#if>
                    </div>
                </div>
            </div>
        </#if>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-2">
                <#if transferRule.hasTask>
                    <@global.role hasRole="'ADMIN','OPERATOR_ADMIN'">
                        <input class="btn btn-default btn-submit" type="submit" value="同意">
                        <input class="btn btn-default btn-refuse" type="button" value="拒绝">
                    </@global.role>
                <#else>
                    <@global.role hasRole="'ADMIN','OPERATOR'">
                        <input class="btn btn-default btn-submit" type="submit" value="提交">
                        <input class="btn btn-default" type="reset" value="重置">
                    </@global.role>
                </#if>
            </div>
        </div>
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