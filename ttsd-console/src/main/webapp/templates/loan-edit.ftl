<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<script>
    var rereq = {};
    <#if loan.loan.loanTitles?has_content>
        <#list loan.loan.loanTitles as loanTitle>
        var initialPreview = [];
        <#list loanTitle.applicationMaterialUrls?split(",") as title>
            initialPreview.push("<img src='${title}' class='file-preview-image' alt='${title}' title='${title}'>");
        </#list>
        rereq['${loanTitle.titleId?string.computer}'] = initialPreview;
        </#list>
    </#if>
</script>

<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="createLoan.js" headLab="project-manage" sideLab="" title="筹款编辑">
<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal jq-form">
        <section id="section-one">
            <h3><span>项目信息</span></h3>
            <hr class="top-line">
            <div>
                <input name="id" type="hidden" value="${loan.loan.id?c}"/>
                <input name="pledgeType" type="hidden" value="${loan.loan.pledgeType}"/>
                <input name="status" type="hidden" value="${loan.loan.status}"/>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款项目名称:</label>
                    <div class="col-sm-4">
                        <select name="name" class="selectpicker" id="projectName" <#if loan.loan.status != "WAITING_VERIFY">disabled="disabled"</#if>>
                            <option value="房产抵押借款" data-pledgeType="HOUSE" <#if loan.loan.pledgeType == "HOUSE">selected</#if>>房产抵押借款</option>
                            <option value="车辆抵押借款" data-pledgeType="VEHICLE" <#if loan.loan.pledgeType == "VEHICLE">selected</#if>>车辆抵押借款</option>
                            <option value="经营性借款" data-pledgeType="ENTERPRISE_CREDIT" <#if loan.loan.pledgeType == "ENTERPRISE_CREDIT">selected</#if>>税易经营性借款信用类</option>
                            <option value="经营性借款" data-pledgeType="ENTERPRISE_PLEDGE" <#if loan.loan.pledgeType == "ENTERPRISE_PLEDGE">selected</#if>>税易经营性借款抵押类</option>
                            <option value="经营性借款" data-pledgeType="ENTERPRISE_FACTORING" <#if loan.loan.pledgeType == "ENTERPRISE_FACTORING">selected</#if>>企业经营性借款—保理</option>
                            <option value="经营性借款" data-pledgeType="ENTERPRISE_BILL" <#if loan.loan.pledgeType == "ENTERPRISE_BILL">selected</#if>>企业经营性借款—票据</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">代理用户:</label>

                    <div class="col-sm-2">
                        <input name="agent" type="text" value="${loan.loan.agent!}" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off"
                               errormsg="代理用户不能为空" <#if loan.loan.status != "WAITING_VERIFY">disabled="disabled"</#if>>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">标的类型:</label>

                    <div class="col-sm-4">
                        <select name="loanType" class="selectpicker b-width" <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>>
                            <#list loanTypes as loanType>
                                <option value="${loanType.name()}" <#if loanType==loan.loan.loanType>selected="selected"</#if>>${loanType.getName()}</option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">原借款期限（天）: </label>

                    <div class="col-sm-3">
                        <input name="originalDuration" type="text" class="form-control" value="${loan.loan.originalDuration?c}"
                               datatype="/^\d+$/"
                               errormsg="原借款期限需要正确填写">
                    </div>
                </div>

                <div class="form-group input-append">
                    <label class="col-sm-2 control-label">借款截止时间: </label>

                    <div class="col-sm-3">
                        <div class='input-group date' id='deadline'>
                            <input name="deadline" type='text' class="form-control" datatype="date" value="${(loan.loan.deadline?string('yyyy-MM-dd'))!}"
                                   <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>
                                   errormsg="借款截止时间需要正确填写"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">映射旧版本期限（天）:</label>

                    <div class="col-sm-4">
                        <select name="productType" class="selectpicker b-width" <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>>
                            <#list productTypes as productType>
                                <option value="${productType.name()}" <#if productType == loan.loan.productType>selected="selected"</#if>>
                                ${productType.getDuration()}
                                </option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">预计出借金额（元）:</label>

                    <div class="col-sm-4">
                        <input name="loanAmount" type="text" class="form-control amount" datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/"
                               errormsg="预计出借金额需要正确填写" value="${loan.loan.loanAmount}" <#if loan.loan.status != "WAITING_VERIFY">disabled="disabled"</#if>>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">最小投资金额（元）:</label>

                    <div class="col-sm-4">
                        <input name="minInvestAmount" type="text" class="form-control amount" datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/"
                               errormsg="最小投资金额需要正确填写"
                               value="${loan.loan.minInvestAmount}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">投资递增金额（元）:</label>

                    <div class="col-sm-4">
                        <input name="investIncreasingAmount" type="text" class="form-control amount" datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/"
                               errormsg="投资递增金额需要正确填写"
                               value="${loan.loan.investIncreasingAmount}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">个人最大投资金额（元）:</label>

                    <div class="col-sm-4">
                        <input name="maxInvestAmount" type="text" class="form-control amount" datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/"
                               errormsg="单笔最大投资金额需要正确填写" value="${loan.loan.maxInvestAmount}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">活动类型:</label>

                    <div class="col-sm-2">
                        <select name="activityType" class="selectpicker">
                            <#list activityTypes as activityType>
                                <option value="${activityType.name()}" <#if activityType == loan.loan.activityType>selected="selected"</#if>>
                                ${activityType.getActivityTypeName()}
                                </option>
                            </#list>
                        </select>
                    </div>

                    <label class="col-sm-1 control-label">活动专享:</label>

                    <div class="col-sm-1 checkbox">
                        <label>
                            <input name="activity" type="checkbox" value="true" <#if loan.loanDetails.activity>checked="checked"</#if>>
                        </label>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">标的所属活动:</label>

                    <div class="col-sm-4">
                        <input name="activityDesc" type="text" class="form-control" <#if !loan.loanDetails.activity>disabled="disabled"</#if> maxlength="10" value="${loan.loanDetails.activityDesc!}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">基本利率（%）:</label>

                    <div class="col-sm-4">
                        <input name="baseRate" type="text" class="form-control rate" datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/" <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>
                               errormsg="基本利率需要正确填写" value="${((loan.loan.baseRate?number)*100)?string('0.00')}">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">活动利率（%）:</label>

                    <div class="col-sm-4">
                        <input name="activityRate" type="text" class="form-control rate" datatype="/^\d+(\.\d{1,2})?$/" <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>
                               errormsg="活动利率需要正确填写" value="${((loan.loan.activityRate?number)*100)?string('0.00')}">
                    </div>
                </div>

                <div class="form-group input-append">
                    <label class="col-sm-2 control-label">筹款启动时间:</label>

                    <div class="col-sm-4">
                        <div class='input-group date' id='fundraisingStartTime'>
                            <input name="fundraisingStartTime" type='text' class="form-control" datatype="date" errormsg="筹款启动时间需要正确填写"
                                   value="${(loan.loan.fundraisingStartTime?string('yyyy-MM-dd HH:mm'))!}"
                                   <#if "WAITING_VERIFY" != loan.loan.status>disabled="disabled"</#if>/>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                        </div>
                    </div>
                </div>

                <div class="form-group input-append">
                    <label class="col-sm-2 control-label">筹款截止时间:</label>

                    <div class="col-sm-4">
                        <div class='input-group date' id='fundraisingEndTime'>
                            <input name="fundraisingEndTime" type='text' class="form-control" datatype="date" errormsg="筹款截止时间需要正确填写"
                                   value="${(loan.loan.fundraisingEndTime?string('yyyy-MM-dd HH:mm'))!}"
                                   <#if !(["WAITING_VERIFY", "PREHEAT", "RAISING", "RECHECK"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>/>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">投资奖励:</label>

                <div class="col-sm-4 checkbox">
                    <label for="extra">
                        <input type="checkbox" id="extra"
                                <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>
                                <#if extraLoanRates?has_content>checked="checked"</#if>>选中后此标的采用投资奖励
                    </label>
                </div>
            </div>

            <div class="form-group extra-rate <#if !(extraLoanRates?has_content)>hidden</#if>">
                <label class="col-sm-2 control-label"></label>

                <div class="col-sm-4">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>投资金额范围（元）</th>
                            <th>投资奖励比例（%）</th>
                        </tr>
                        </thead>
                        <tbody class="extra-rate-rule">
                        <#list extraLoanRates as extraLoanRate>
                        <tr>
                            <td>${(extraLoanRate.minInvestAmount/100)?c} ≤投资额 <#if extraLoanRate.maxInvestAmount gt 0> < ${(extraLoanRate.maxInvestAmount/100)?c}</#if></td>
                            <td>${extraLoanRate.rate * 100}</td>
                            <input name="extraRateRuleIds" type="hidden" class="extra-rate-id" value="${extraLoanRate.extraRateRuleId?c}">
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="form-group extra-source <#if !(extraLoanRates?has_content)>hidden</#if>">
                <label class="col-sm-2 control-label">投资奖励来源:</label>

                <div class="col-sm-2 checkbox" id="extraSource">
                    <#list extraSources as source>
                        <label>
                            <input name="extraSource" type="checkbox" value="${source.name()}" <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if> <#if loan.loanDetails.extraSource??><#if loan.loanDetails.extraSource?seq_contains(source)>checked="checked"</#if></#if>>${source.name()}</input>
                        </label>
                    </#list>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">是否允许债权转让:</label>

                <div class="col-sm-4 checkbox">
                    <label for="extra">
                        <input type="checkbox" id="nonTransferable" name="nonTransferable"
                               <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>
                               <#if loan.loanDetails?? && loan.loanDetails.nonTransferable>checked="checked"</#if> value="true" />（选中后投资此标的不允许债权转让）
                    </label>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">是否可以使用优惠券:</label>

                <div class="col-sm-4 checkbox">
                    <label for="extra">
                        <input type="checkbox" id="disableCoupon" name="disableCoupon"
                               <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>
                               <#if loan.loanDetails?? && loan.loanDetails.disableCoupon>checked="checked"</#if> value="true" />（选中后投资此标的不允许使用任何优惠券）
                    </label>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">是否享有返现奖励: </label>

                <div class="col-sm-1 checkbox">
                    <label for="grantReward">
                        <input type="checkbox" name="grantReward"
                               <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>
                               <#if loan.loanDetails?? && loan.loanDetails.grantReward>checked="checked"</#if> value="true" />
                    </label>
                </div>

                <label class="col-sm-2 control-label">返现奖励（%）: </label>
                <div class="col-sm-2">
                    <input name="rewardRate" type="text" class="form-control rate" <#if !loan.loanDetails.grantReward>disabled="disabled"</#if> datatype="/^\d+(\.\d{1,2})?$/"
                           value="${((loan.loanDetails.rewardRate?number)*100)?string('0.00')}"
                           errormsg="返现奖励需要正确填写">
                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label">项目适合的用户: </label>

                <div class="col-sm-3 checkbox" id="estimates">
                    <label>
                        <input name="estimate" type="radio"
                               <#if (loan.loanDetails.estimate)?? && loan.loanDetails.estimate == 'CONSERVATIVE'>checked="checked"</#if>
                               value="CONSERVATIVE">保守型
                    </label>
                    <label>
                        <input name="estimate" type="radio"
                               <#if (loan.loanDetails.estimate)?? && loan.loanDetails.estimate == 'STEADY'>checked="checked"</#if>
                               value="STEADY">稳健型
                    </label>
                    <label>
                        <input name="estimate" type="radio"
                               <#if (loan.loanDetails.estimate)?? && loan.loanDetails.estimate == 'POSITIVE'>checked="checked"</#if>
                               value="POSITIVE">积极型
                    </label>
                </div>
            </div>

        </section>

        <section id="section-two">
            <#if ['HOUSE', 'VEHICLE']?seq_contains(loan.loan.pledgeType)>
                <#include 'loan-edit-loaner-details.ftl'>
            </#if>

            <#if ['ENTERPRISE_CREDIT', 'ENTERPRISE_PLEDGE']?seq_contains(loan.loan.pledgeType)>
                <#include 'loan-edit-loaner-enterprise-details.ftl'>
            </#if>

            <#if ['ENTERPRISE_FACTORING', 'ENTERPRISE_BILL']?seq_contains(loan.loan.pledgeType)>
                <#include 'loan-edit-loaner-enterprise-info.ftl'>
            </#if>

        </section>

        <section id="section-three">
            <#if 'HOUSE' == loan.loan.pledgeType>
                <#include 'loan-edit-pledge-house.ftl'>
            </#if>

            <#if 'VEHICLE' == loan.loan.pledgeType>
                <#include 'loan-edit-pledge-vehicle.ftl'>
            </#if>

            <#if 'ENTERPRISE_PLEDGE' == loan.loan.pledgeType>
                <#include 'loan-edit-pledge-enterprise.ftl'>
            </#if>

            <#if 'ENTERPRISE_FACTORING' == loan.loan.pledgeType>
            <#include 'loan-edit-loaner-enterprise-factoring-info.ftl'>
        </#if>
        </section>

        <h3><span>声明</span></h3>
        <hr class="top-line"/>
        <div>
            <div class="form-group">
                <label class="col-sm-2 control-label"></label>
                <div class="col-sm-4">
                    <input name="declaration" value="${loan.loanDetails.declaration}" type="text" class="form-control"
                           datatype="*"
                           errormsg="声明不能为空">
                </div>
            </div>
        </div>

        <h3><span>审核材料</span></h3>
        <hr class="top-line"/>
        <div>
            <div class="form-group">
                <label class="col-sm-2 control-label">申请材料:</label>

                <div class="col-sm-10">
                    <button type="button" class="btn-upload btn btn-info">＋</button>
                </div>
            </div>
            <div class="upload-box"></div>
        </div>

        <h3><span>消息中心信息</span></h3>
        <hr class="top-line"/>
        <div>
            <div class="form-group">
                <label class="col-sm-2 control-label">推送消息</label>

                <div class="col-sm-3">
                    <input name="pushMessage" type="text" class="form-control" value="${(loan.loanDetails.pushMessage)!}" <#if !(["PREHEAT", "WAITING_VERIFY"]?seq_contains(loan.loan.status))>disabled="disabled"</#if>>
                </div>
            </div>
        </div>

        <#if loan.loan.verifyTime??>
        <div class="form-group">
            <label class="col-sm-2 control-label">初审时间:</label>
            <div class="col-sm-2">
                <p class="form-control-static">${loan.loan.verifyTime?string('yyyy-MM-dd HH:mm:ss')}</p>
            </div>

            <label class="col-sm-1 control-label">初审人员:</label>
            <div class="col-sm-2">
                <p class="form-control-static">${loan.loan.verifyLoginName!}</p>
            </div>
        </div>
        </#if>

        <#if loan.loan.recheckTime??>
        <div class="form-group">
            <label class="col-sm-2 control-label">复审时间:</label>
            <div class="col-sm-2">
                <p class="form-control-static">${loan.loan.recheckTime?string('yyyy-MM-dd HH:mm:ss')}</p>
            </div>

            <label class="col-sm-1 control-label">复审人员:</label>
            <div class="col-sm-2">
                <p class="form-control-static">${loan.loan.recheckLoginName!}</p>
            </div>
        </div>
        </#if>

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">操作:</label>
            <div class="col-sm-4">
                <input name="contractId" type="hidden" value="${loan.loan.contractId?c}"/><!-- 默认合同ID -->

                <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                    <button type="button" class="btn form-submit-btn btn-primary" data-url="/project-manage/loan/update">保存</button>
                </@security.authorize>

                <#if loan.loan.status == "WAITING_VERIFY">
                    <@security.authorize access="hasAnyAuthority('OPERATOR')">
                        <button type="button" class="btn form-submit-btn btn-primary" data-url="/project-manage/loan/apply-audit">提交审核</button>
                    </@security.authorize>

                    <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                        <button type="button" class="btn form-submit-btn btn-primary" data-url="/project-manage/loan/open">审核通过</button>
                    </@security.authorize>
                </#if>

                <#if loan.loan.status == "RECHECK">
                    <#if loan.loan.raisingCompleteTime??>
                        <button TYPE="button" class="btn form-submit-btn btn-primary" data-url="/project-manage/loan/recheck">放款</button>
                    <#else>
                        <button TYPE="button" class="btn form-submit-btn btn-primary" data-url="/project-manage/loan/delay">延期</button>
                        <button TYPE="button" class="btn form-submit-btn btn-primary" data-url="/project-manage/loan/cancel">流标</button>
                    </#if>
                </#if>
            </div>
        </div>
    </form>

    <!-- Modal -->
    <div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <h5>确认提交？</h5>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-default btn-submit">确认</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- content area end -->
</@global.main>
