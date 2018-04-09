<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="createLoan.js" headLab="project-manage" sideLab="start" title="发起借款">
<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal jq-form" id="formDom">
        <section id="section-one">
            <h3><span>项目信息</span></h3>
            <hr class="top-line">
            <div>
                <input name="pledgeType" type="hidden" value="NONE">
                <input name="contractId" type="hidden" value="${contractId}"/><!-- 默认合同ID -->
                <input name="status" type="hidden" value="WAITING_VERIFY"/>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款项目名称: </label>

                    <div class="col-sm-4">
                        <select name="name" class="selectpicker" id="projectName">
                            <option value="房产抵押借款" selected="selected" data-pledgeType="HOUSE">房产抵押借款</option>
                            <option value="车辆抵押借款" data-pledgeType="VEHICLE">车辆抵押借款</option>
                            <option value="经营性借款" data-pledgeType="ENTERPRISE_CREDIT">税易经营性借款信用类</option>
                            <option value="经营性借款" data-pledgeType="ENTERPRISE_PLEDGE">税易经营性借款抵押类</option>
                            <option value="经营性借款" data-pledgeType="ENTERPRISE_FACTORING">企业经营性借款—保理</option>
                            <option value="经营性借款" data-pledgeType="ENTERPRISE_BILL">企业经营性借款—票据</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">代理用户: </label>

                    <div class="col-sm-2">
                        <input name="agent" type="text" class="form-control ui-autocomplete-input" datatype="*"
                               autocomplete="off"
                               errormsg="代理用户不能为空">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">标的类型: </label>

                    <div class="col-sm-3">
                        <select name="loanType" class="selectpicker b-width">
                            <#list loanTypes as loanType>
                                <option value="${loanType.name()}"
                                        <#if loanType_index == 0 >selected="selected"</#if>>${loanType.getName()}</option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">原借款期限（天）: </label>

                    <div class="col-sm-3">
                        <input name="originalDuration" type="text" class="form-control"
                               datatype="/^\d+$/"
                               errormsg="原借款期限需要正确填写">
                    </div>
                </div>

                <div class="form-group input-append">
                    <label class="col-sm-2 control-label">借款截止时间: </label>

                    <div class="col-sm-3">
                        <div class='input-group date' id='deadline'>
                            <input name="deadline" type='text' class="form-control" datatype="date"
                                   errormsg="借款截止时间需要正确填写"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">映射旧版本期限（天）: </label>

                    <div class="col-sm-2">
                        <select name="productType" class="selectpicker">
                            <option value="">请选择</option>
                            <#list productTypes as productType>
                                <option value="${productType.name()}"
                                        <#if productType_index == 0 >selected="selected"</#if>>${productType.getDuration()}</option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">预计出借金额（元）: </label>

                    <div class="col-sm-3">
                        <input name="loanAmount" type="text" class="form-control amount"
                               datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/"
                               errormsg="预计出借金额需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">单笔最小投资金额（元）: </label>

                    <div class="col-sm-3">
                        <input name="minInvestAmount" type="text" class="form-control amount"
                               datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/"
                               errormsg="单笔最小投资金额需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">投资递增金额（元）: </label>

                    <div class="col-sm-3">
                        <input name="investIncreasingAmount" type="text" class="form-control amount"
                               datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/"
                               errormsg="投资递增金额需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">个人最大投资金额（元）: </label>

                    <div class="col-sm-3">
                        <input name="maxInvestAmount" type="text" class="form-control amount"
                               datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/"
                               errormsg="个人最大投资金额需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">活动类型: </label>

                    <div class="col-sm-2">
                        <select name="activityType" class="selectpicker">
                            <#list activityTypes as activityType>
                                <option value="${activityType.name()}"
                                        <#if activityType_index == 0 >selected="selected"</#if>>${activityType.getActivityTypeName()}</option>
                            </#list>
                        </select>
                    </div>

                    <label class="col-sm-1 control-label">活动专享: </label>

                    <div class="col-sm-1 checkbox">
                        <label>
                            <input name="activity" type="checkbox" value="true">
                        </label>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">标的所属活动: </label>

                    <div class="col-sm-3">
                        <input name="activityDesc" type="text" class="form-control" disabled="disabled" maxlength="10">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">基本利率（%）: </label>

                    <div class="col-sm-3">
                        <input name="baseRate" type="text" class="form-control rate"
                               datatype="/^([1-9]\d*(\.\d{1,2})?)|(0\.\d*[1-9]\d*)$/" errormsg="基本利率需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">活动利率（%）: </label>

                    <div class="col-sm-3">
                        <input name="activityRate" type="text" class="form-control rate" datatype="/^\d+(\.\d{1,2})?$/"
                               value="0.0"
                               errormsg="活动利率需要正确填写">
                    </div>
                </div>

                <div class="form-group input-append">
                    <label class="col-sm-2 control-label">筹款启动时间: </label>

                    <div class="col-sm-3">
                        <div class='input-group date' id='fundraisingStartTime'>
                            <input name="fundraisingStartTime" type='text' class="form-control"
                                   datatype="date"
                                   errormsg="筹款启动时间需要正确填写"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="form-group input-append">
                    <label class="col-sm-2 control-label">筹款截止时间: </label>

                    <div class="col-sm-3">
                        <div class='input-group date' id='fundraisingEndTime'>
                            <input name="fundraisingEndTime" type='text' class="form-control"
                                   datatype="date"
                                   errormsg="筹款截止时间需要正确填写"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">投资奖励: </label>

                    <div class="col-sm-3 checkbox">
                        <label for="extra">
                            <input type="checkbox" id="extra">选中后此标的采用投资奖励
                        </label>
                    </div>
                </div>

                <div class="form-group extra-rate hidden"></div>

                <div class="form-group extra-source hidden">
                    <label class="col-sm-2 control-label">投资奖励来源:</label>

                    <div class="col-sm-2 checkbox" id="extraSource">
                        <#list extraSources as source>
                            <label>
                                <input name="extraSource" type="checkbox"
                                       value="${source.name()}">${source.name()}</input>
                            </label>
                        </#list>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">是否允许债权转让: </label>

                    <div class="col-sm-4 checkbox">
                        <label for="nonTransferable">
                            <input type="checkbox" name="nonTransferable" value="true">（选中后投资此标的不允许债权转让）
                        </label>
                    </div>

                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">是否可以使用优惠券: </label>

                    <div class="col-sm-4 checkbox">
                        <label for="disableCoupon">
                            <input type="checkbox" name="disableCoupon" value="true">（选中后投资此标的不允许使用任何优惠券）
                        </label>
                    </div>

                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">是否享有返现奖励: </label>

                    <div class="col-sm-1 checkbox">
                        <label for="grantReward">
                            <input type="checkbox" name="grantReward" value="true">
                        </label>
                    </div>

                    <label class="col-sm-2 control-label">返现奖励（%）: </label>
                    <div class="col-sm-2">
                        <input name="rewardRate" type="text" class="form-control rate" disabled="disabled" datatype="/^\d+(\.\d{1,2})?$/"
                               value="0.0"
                               errormsg="返现奖励需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">项目适合的用户: </label>

                    <div class="col-sm-3 checkbox" id="estimate">
                        <label>
                            <input name="estimates" type="checkbox"
                                   value="CONSERVATIVE">保守型
                        </label>
                        <label>
                            <input name="estimates" type="checkbox"
                                   value="STEADY">稳健型
                        </label>
                        <label>
                            <input name="estimates" type="checkbox"
                                   value="POSITIVE">积极型
                        </label>
                    </div>
                </div>

            </div>
        </section>


        <section id="section-two">
        </section>

        <section id="section-three">
        </section>

        <h3><span>声明</span></h3>
        <hr class="top-line"/>
        <div>
            <div class="form-group">
                <label class="col-sm-2 control-label"></label>

                <div class="col-sm-3">
                    <input name="declaration" type="text" class="form-control"
                           datatype="*"
                           placeholder="" errormsg="声明不能为空">
                </div>
            </div>
        </div>

        <h3><span>消息中心信息</span></h3>
        <hr class="top-line"/>
        <div>
            <div class="form-group">
                <label class="col-sm-2 control-label">推送消息</label>

                <div class="col-sm-3">
                    <input name="pushMessage" type="text" class="form-control">
                </div>
            </div>
        </div>

        <h3><span>审核材料</span></h3>
        <hr class="top-line"/>
        <div>
            <div class="form-group">
                <label class="col-sm-2 control-label">申请材料: </label>

                <div class="col-sm-1">
                    <button type="button" class="btn-upload btn btn-info">＋</button>
                </div>
            </div>
            <div class="upload-box"></div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn form-submit-btn btn-primary" data-url="/project-manage/loan/create">
                    保存
                </button>
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
<!-- content area end -->
</@global.main>
