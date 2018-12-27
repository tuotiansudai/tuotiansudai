<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="consumeDetail.js" headLab="project-manage" sideLab="loanApplicationList" title="借款申请信息">
<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal jq-form" id="formDom">
        <input type="hidden" id="loanApplicationId" value="${loanApplicationId}"/>
        <section id="section-four">
            <h3><span>借款人基本信息</span></h3>
            <hr class="top-line">
            <div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人姓名: </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.userName)!}
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人性别: </label>

                    <div class="col-sm-8 font_mid">
                        <#if (data.loanApplicationModel.sex)?? && data.loanApplicationModel.sex= 'MALE'>
                        男
                        <#else>
                        女
                        </#if>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人年龄: </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.age)!}
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人身份证号: </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.identityNumber)!}
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人婚姻情况: </label>

                    <div class="col-sm-8 font_mid">
                        <#if (data.loanApplicationModel.marriage)??>
                             ${data.loanApplicationModel.marriage.description}
                        </#if>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人所在地区: </label>

                    <div class="col-sm-3">
                        <input name="address" value="${(data.loanApplicationModel.address)!}" type="text" class="form-control" datatype="*" errormsg="借款人所在地区不能为空">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款金额(万元): </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.amount)!}
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款周期(月): </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.period)!}
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人家庭年收入(万元): </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.homeIncome)!}
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款人从业情况: </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.workPosition)!}
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">芝麻信用分: </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.sesameCredit)!}
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款用途: </label>

                    <div class="col-sm-3">
                        <input name="loanUsage" value="${(data.loanApplicationModel.loanUsage)!}" type="text" maxlength="6" class="form-control" datatype="*" errormsg="借款用途不能为空">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">资产信息: </label>

                    <div class="col-sm-10 font_mid">
                        ${(data.loanApplicationModel.elsePledge)!}
                    </div>
                </div>

                <div class="form-group">

                </div>
                <div class="form-group">

                </div>

            </div>
        </section>

        <section id="section-four">
            <h3><span>证明材料</span></h3>
            <hr class="top-line">
            <div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">身份信息认证: </label>

                    <ul class="col-sm-8 img_list">
                        <#list data.materialsList.identityProveUrls as url>
                            <li class="img_item">
                                <img src="${commonStaticServer}${url}" alt="" width="100%">
                            </li>
                        </#list>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">收入认证: </label>

                    <ul class="col-sm-8 img_list">
                        <#list data.materialsList.incomeProveUrls as url>
                            <li class="img_item">
                                <img src="${commonStaticServer}${url}" alt="" width="100%">
                            </li>
                        </#list>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">信用报告: </label>

                    <ul class="col-sm-8 img_list">
                        <#list data.materialsList.creditProveUrls as url>
                            <li class="img_item">
                                <img src="${commonStaticServer}${url}" alt="" width="100%">
                            </li>
                        </#list>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">婚姻状况证明: </label>

                    <ul class="col-sm-8 img_list">
                        <#list data.materialsList.marriageProveUrls as url>
                            <li class="img_item">
                                <img src="${commonStaticServer}${url}" alt="" width="100%">
                            </li>
                        </#list>
                    </ul>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">资产证明: </label>

                    <ul class="col-sm-8 img_list">
                        <#list data.materialsList.propertyProveUrls as url>
                            <li class="img_item">
                                <img src="${commonStaticServer}${url}" alt="" width="100%">
                            </li>
                        </#list>
                    </ul>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">共同借款人: </label>

                    <div class="col-sm-8 font_mid">
                        ${(data.loanApplicationModel.togetherLoaner)!},${(data.loanApplicationModel.togetherLoanerIdentity)!}
                    </div>
                </div>

                <div class="form-group">

                    <ul class="col-sm-8 img_list col-lg-offset-2">
                        <#list data.materialsList.togetherProveUrls as url>
                            <li class="img_item">
                                <img src="${commonStaticServer}${url}" alt="" width="100%">
                            </li>
                        </#list>
                    </ul>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">机动车驾驶证: </label>

                    <ul class="col-sm-8 img_list">
                        <#list data.materialsList.driversLicense as url>
                            <li class="img_item">
                                <img src="${commonStaticServer}${url}" alt="" width="100%">
                            </li>
                        </#list>
                    </ul>
                </div>

            </div>
        </section>

        <section id="section-five">
            <h3><span>风控信息</span></h3>
            <hr class="top-line">
            <div class="wind_control" id="wind_control">

            </div>
            <div class="form-group" id="add_input" style="display: none">
                <label class="col-sm-3 control-label">
                    <input type="text" class="form-control">
                </label>

                <label class="col-sm-1 control-label">
                    <button class="btn btn-primary" type="button" style="margin-top:0px" id="add_wind_control">保存</button>
                </label>
            </div>
            <button class="btn btn-warning" type="button" id="add_wind_input" style="margin-top:30px">添加</button>
        </section>

        <#if (data.loanApplicationModel.status)?? && data.loanApplicationModel.status != 'REJECT'>
            <section id="section-six">
                <h3><span>标的信息</span></h3>
                <hr class="top-line">
                <div class="form-group">
                    <label class="col-sm-1 control-label">
                        <#if data.loanApplicationModel.loanId??>
                            <button class="btn btn-warning" ><a href="/loan-application/consume/${data.loanApplicationModel.id?c}/loan/${data.loanApplicationModel.loanId?c}/edit">查看标的相关信息</a></button>
                        <#else>
                            <button class="btn btn-warning" ><a href="/loan-application/consume/${data.loanApplicationModel.id?c}/create-loan">添加标的相关信息</a></button>
                        </#if>
                    </label>
                </div>
            </section>

            <div class="form-group">
                <div class="col-sm-12">
                    <button type="button" class="btn btn-primary" data-url="/loan-application/consume/${data.loanApplicationModel.id}/save" id="form-save-btn">
                        保存
                    </button>
                    <#if data.loanApplicationModel.status != 'APPROVE'>
                        <button type="button" class="btn btn-primary" data-url="/loan-application/consume/${data.loanApplicationModel.id}/reject" id="form-refuse-btn">
                            驳回
                        </button>
                        <#if data.loanApplicationModel.loanId??>
                            <@security.authorize access="hasAnyAuthority('RISK_CONTROL_STAFF')">
                                <button type="button" class="btn btn-primary"
                                        data-url="/loan-application/consume/${data.loanApplicationModel.id}/submit-audit"
                                        id="form-submut-audit-btn">
                                    提交审核
                                </button>
                            </@security.authorize>
                            <@security.authorize access="hasAnyAuthority('OPERATOR', 'OPERATOR_ADMIN')">
                                <button type="button" class="btn btn-primary"
                                        data-url="/loan-application/consume/${data.loanApplicationModel.id}/approve"
                                        id="form-approve-btn">
                                    审核通过
                                </button>
                            </@security.authorize>
                        </#if>
                    </#if>
                </div>
            </div>
        </#if>
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

    <div class="modal fade" id="pledge-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <h5>抵押物信息最多添加4项</h5>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>


</div>

</@global.main>
