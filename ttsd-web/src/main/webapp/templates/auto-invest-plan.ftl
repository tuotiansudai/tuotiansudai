<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.auto_invest}" activeNav="我的账户" activeLeftNav="自动投标" title="自动投标">
<div class="content-container auto-invest">
    <h4 class="column-title"><em class="tc">自动投标</em></h4>

        <div class="borderBox panel">
            注意： 自动投标一旦设置并保存成功，将在次日零点自动进行投标。 <br/>
            <span style="padding-left: 45px;">如不想复投，请提前关闭自动投标，以免影响提现。</span>
        </div>
            <div class="planSwitch" id="planSwitchDom">
                <form name="planForm" id="signPlanForm" method="post" action="/auto-invest/turn-on">
                    <dl>
                        <dt>功能状态：</dt>
                        <dd class="switchBtn">
                            <input type="radio" name="enable" value="1" id="plan-open" ${((model.enabled)!true)?string('checked','')}>
                            <label for="plan-open" class="radio">开启</label>
                            <input type="radio" name="enable" value="2" id="plan-close" ${((model.enabled)!true)?string('','checked')}>
                            <label for="plan-close" class="radio">关闭并保存</label></dd>
                    </dl>
                    <dl>
                        <dt class="requireOpt">投资金额：</dt>
                        <dd><input type="text" name="minInvestAmount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" class="autoNumeric" value="${(model.minInvestAmount)!}"> ~
                            <input type="text" name="maxInvestAmount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" class="autoNumeric" value="${(model.maxInvestAmount)!}"> 元
                        </dd>
                    </dl>
                    <dl>
                        <dt class="requireOpt">保留金额：</dt>
                        <dd><input type="text" name="retentionAmount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" class="autoNumeric" value="${(model.retentionAmount)!}"> 元
                            （不会加入自动投标的金额）
                        </dd>
                    </dl>
                    <dl class="clear">
                        <dt>本金复投：</dt>
                        <dd class="projectLimit">本金收回后将继续自动投标， <br/>
                            若想提现请提前设置保留金额或关闭自动投标。
                        </dd>
                    </dl>
                    <dl>
                        <dt class="requireOpt">项目期限：</dt>
                        <dd class="projectLimit" data-value="${(model.autoInvestPeriods?string('0'))!'0'}">
                            <#list periods as period>
                                <span data-value="${period.periodValue?string('0')}">${period.periodName}<i class="badge"></i></span>
                            </#list>
                        </dd>
                    </dl>
                    <div class="btnBox tc clear">
                    <#--<div class="agreeStatus"><input type="checkbox" name="agreement" id="plan-agreement" checked><label for="plan-agreement" class="checkbox"> 我已阅读并同意拓天速贷的<a href="#" target="_blank"> 《自动投标协议》</a></label></div>-->
                        <button type="button" class="btn" id="saveInvestPlan" disabled>保存</button>
                    </div>
                </form>
            </div>

        <div class="borderBox">
            <p class="notice">
                <b>自动投标设置说明：</b> <br/>
                1. 用户点击“开启”并成功“保存”才视为开启自动投标设置成功。<br/>
                2. 投资金额：可设置，最小为1元。<br/>
                3. 保留金额：不会加入自动投标的金额。<br/>
                4. 本金复投：本金收回后将继续自动投标，若想提现请提前设置保留金额或关闭自动投标。<br/>
                5. 项目期限：选择您想投资项目的期限，可多选。<br/>
                6. 自动投标成交金额：用户可用余额、设置的最大金额、项目剩余金额三者中的最小值，并根据标的设置进行调整。<br/>
                7. 用户开通自动投标功能即视为委托拓天速贷平台与达到用户指定标准的借款人签署借款合同，并承担该合同项下的一切权利及义务。<br/>
                8. 自动投标会根据您的设置，筛选并自动为您投资，但我们不能保证对所有的项目投资成功。<br/>
                9. 全部设置完成后，会在次日零点开启自动投标。<br/>
                10. 自动投标开启后，若有多个项目可投，将尽可能优先新手专享进行投资。<br/>
                11. 自动投标开启后，如果不想自动投标，请手动关闭。<br/>
            </p>
        </div>

</div>
</@global.main>