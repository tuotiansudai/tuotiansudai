<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<script>
    var API_SELECT = '${requestContext.getContextPath()}/project-manage/loan/titles';  // 申请资料标题url
    var API_POST_TITLE = '${requestContext.getContextPath()}/project-manage/loan/title';  //
    var API_FORM = '${requestContext.getContextPath()}/project-manage/loan/';
    var rereq = {};
    <#if (loanTitleRelationModels?size>0)>
        <#list loanTitleRelationModels as loanTitleRelationModel>
        var initialPreview = [];
            <#list loanTitleRelationModel.applicationMaterialUrls?split(",") as title>
            initialPreview.push("<img src='${title}' class='file-preview-image' alt='${title}' title='${title}'>");
            </#list>
        rereq['${loanTitleRelationModel.titleId?string.computer}'] = initialPreview;
        </#list>
    </#if>
</script>

<script id="upload" type="text/html">
    <div class="form-group">
        <label class="col-sm-2 control-label"></label>

        <div class="col-sm-10">
            <div class="row col-file-box">
                <div class="select-box">
                    <select class="selectpicker col-sm-5 rereq">
                        {{each _data}}
                        <option value="{{$value.id}}">{{$value.title}}</option>
                        {{/each}}
                    </select>

                    <input type="hidden" class="jq-txt" value="{{_data[0]['id']}}"/>
                </div>
                <input type="text" name="file-name[]" class="files-input form-control" placeholder="请输入资料名称"/>
                <button type="button" class="btn btn-default jq-add">添加</button>
                <button type="button" class="btn btn-danger jq-delete">删除</button>
            </div>
            <input type="file" multiple=true class="file-loading" name="upfile">
        </div>
    </div>
</script>

<script id="select" type="text/html">
    <select class="selectpicker col-s _datam-5">
        {{each _data}}
        <option value="{{$value.id}}">{{$value.title}}</option>
        {{/each}}
    </select>
</script>


<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="editLoan.js" headLab="project-manage" sideLab="" title="筹款编辑">

<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal jq-form">
        <input type="hidden" class="jq-loanId" value="${loanInfo.id?c}">
        <input type="hidden" class="jq-status" value="${loanInfo.loanStatus}">

        <h3><span>项目信息</span></h3>
        <hr class="top-line">
        <div>
            <input type="text" hidden class="jq-pledge-type" value="${loanInfo.pledgeType}">
            <div class="form-group">
                <label class="col-sm-2 control-label">借款项目名称: </label>
                <div class="col-sm-4">
                    <select class="selectpicker b-width jq-name"
                            <#if loanInfo.loanStatus != "WAITING_VERIFY">disabled="disabled"</#if>>>
                        <option value="房产抵押借款" <#if loanInfo.projectName == "房产抵押借款">selected</#if>>房产抵押借款</option>
                        <option value="车辆抵押借款" <#if loanInfo.projectName == "车辆抵押借款">selected</#if>>车辆抵押借款</option>
                    </select>
                </div>
                <div class="checkbox jq-activity-checkbox">
                    <label>
                        <input type="checkbox" class="jq-activity"  <#if loanInfo.activity>checked</#if>
                               <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING">disabled="disabled"</#if>>
                        活动专享
                    </label>
                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">代理用户: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-agent" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="代理用户不能为空" value="${loanInfo.agentLoginName!('')}"
                           <#if loanInfo.loanStatus!= "WAITING_VERIFY">disabled="disabled"</#if>>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">标的类型: </label>

                <div class="col-sm-4">
                    <select class="selectpicker jq-b-type b-width"
                            <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING">disabled="disabled"</#if>>
                        <#list loanTypes as loanType>
                            <option value="${loanType.name()}" data-repayTimeUnit="${loanType.getLoanPeriodUnit()}"
                                    data-repayTimePeriod="1" <#if loanType.name()==loanInfo.type>selected</#if>>
                            ${loanType.getName()}
                            </option>
                        </#list>
                    </select>
                    <input type="hidden" class="jq-mark-type" value="${loanInfo.type}"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款期限: </label>

                <div class="col-sm-4 product-line-period">
                    <select class="selectpicker b-width"
                            <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING">disabled="disabled"</#if>>
                        <option value="">请选择</option>
                        <#list productTypes as productType>
                            <#if productType.name() != 'EXPERIENCE'>
                                <option value="${productType.getDuration()}"
                                        <#if loanInfo.productType?? && productType.name() == loanInfo.productType>selected</#if>
                                        data-duration="${productType.getDuration()}"
                                        data-period="${productType.getPeriods()}"
                                        data-product-line="${productType.name()}">
                                ${productType.getDuration()}
                                </option>
                            </#if>
                        </#list>
                    </select>
                    <input type="hidden" class="jq-duration" value="${loanInfo.duration}"/>
                    <input type="hidden" class="jq-product-line" value="${loanInfo.productType.name()}">
                    <input type="hidden" class="jq-timer" value="${loanInfo.periods}">
                </div>
                <div class="col-sm-3">
                    <div class="form-control-static">(单位：<label>天</label>)</div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">预计出借金额（元）: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-pay jq-money" placeholder="" datatype="money_fl"
                           errormsg="预计出借金额需要正确填写" value="${loanInfo.loanAmount?string}"
                           <#if loanInfo.loanStatus!= "WAITING_VERIFY">disabled="disabled"</#if>>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">最小投资金额（元）: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-min-pay jq-money" datatype="money_fl"
                           errormsg="最小投资金额需要正确填写"
                           value="${loanInfo.minInvestAmount?string}"
                           <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING" && loanInfo.loanStatus!="RECHECK"&& loanInfo.loanStatus!="COMPLETE" >disabled="disabled"</#if>>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">投资递增金额（元）: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-add-pay jq-money" datatype="money_fl"
                           errormsg="投资递增金额需要正确填写"
                           value="${loanInfo.investIncreasingAmount?string}"
                           <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING" && loanInfo.loanStatus!="COMPLETE" >disabled="disabled"</#if>>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">个人最大投资金额（元）: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-max-pay jq-money" datatype="money_fl"
                           errormsg="单笔最大投资金额需要正确填写" value="${loanInfo.maxInvestAmount?string}"
                           <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING" && loanInfo.loanStatus!="COMPLETE">disabled="disabled"</#if>>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">活动类型: </label>

                <div class="col-sm-4">
                    <select class="selectpicker "
                            <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING">disabled="disabled"</#if>>
                        <#list activityTypes as activityType>
                            <#if activityType.name() != 'PROMOTION'>
                                <option value="${activityType.name()}"
                                        <#if activityType.name() == loanInfo.activityType>selected</#if>>
                                ${activityType.getActivityTypeName()}
                                </option>
                            </#if>
                        </#list>
                    </select>
                    <input type="hidden" class="jq-impact-type" value="${loanInfo.activityType}"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">活动利率（%）: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-percent jq-money" placeholder="" datatype="money_fl"
                           errormsg="活动利率需要正确填写" value="${((loanInfo.activityRate?number)*100)?string('0.00')}"
                           <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING">disabled="disabled"</#if>>
                </div>
                <div class="col-sm-6">
                    <div class="form-control-static">适用于所有标(0 表示无),站点前端以(基本利率%+加息利率%)方式展现,如(10%+2%)。</div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">基本利率（%）: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control jq-base-percent jq-money" placeholder="" datatype="money_fl"
                           errormsg="请选择产品线类型" value="${((loanInfo.basicRate?number)*100)?string('0.00')}">
                </div>
            </div>
            <div class="form-group input-append">
                <label class="col-sm-2 control-label">筹款启动时间: </label>

                <div class="col-sm-4">
                    <div class='input-group date' id='datetimepicker6'>
                        <input type='text' class="form-control jq-star-date" datatype="date" errormsg="筹款启动时间需要正确填写"
                               value="${(loanInfo.fundraisingStartTime?string('yyyy-MM-dd HH:mm'))!}"
                               <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING">disabled="disabled"</#if>/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                    </div>
                </div>
            </div>
            <div class="form-group input-append">
                <label class="col-sm-2 control-label">筹款截止时间: </label>

                <div class="col-sm-4">
                    <div class='input-group date' id='datetimepicker7'>
                        <input type='text' class="form-control jq-end-date" datatype="date" errormsg="筹款截止时间需要正确填写"
                               value="${(loanInfo.fundraisingEndTime?string('yyyy-MM-dd HH:mm'))!}"
                               <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING" && loanInfo.loanStatus!="RECHECK">disabled="disabled"</#if>/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                    </div>
                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label">投资奖励: </label>

                <div class="col-sm-4 checkbox">
                    <label for="extra"><input type="checkbox" id="extra"
                                              <#if loanInfo.loanStatus!= "WAITING_VERIFY">disabled="disabled"</#if>
                                              <#if extraLoanRates?? && extraLoanRates?size gt 0>checked</#if>>
                        选中后此标的采用投资奖励
                    </label>
                </div>
            </div>

            <#if extraLoanRates?? && extraLoanRates?size gt 0>
                <div class="form-group extra-rate">
                        <#list extraLoanRates as extraLoanRate>
                            <input type="hidden" class="extra-rate-id" value="${(extraLoanRate.extraRateRuleId)?string('0')}">
                        </#list>
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
                                    <td>${(extraLoanRate.minInvestAmount/100)?string('0')}
                                        ≤投资额<#if extraLoanRate.maxInvestAmount gt 0>
                                            <${(extraLoanRate.maxInvestAmount/100)?string('0')}</#if></td>
                                    <td>${extraLoanRate.rate * 100}</td>
                                </tr>
                                </#list>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="form-group extra-source">
                    <label class="col-sm-2 control-label">投资奖励来源:</label>
                    <#if  loanInfo?? && loanInfo.extraSource?? && extraLoanRates?? && extraLoanRates?size gt 0>
                        <input type="hidden" class="extraSource" value="${loanInfo.extraSource}">
                    </#if>

                    <div class="col-sm-4" id="extraSource">
                        <#list sources as source>
                            <label>
                            <input type="checkbox" name="extraSource" class="extraSource" value="${source.name()}" <#if loanInfo?? && loanInfo.extraSource?? && extraLoanRates?? && extraLoanRates?size gt 0 && loanInfo.extraSource?contains(source.name())>checked="checked"</#if>">${source.name()}
                            </label>
                        </#list>
                    </div>
                </div>
            <#else>
                <div class="form-group extra-rate hidden">
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
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="form-group extra-source hidden">
                    <label class="col-sm-2 control-label">投资奖励来源:</label>

                    <div class="col-sm-4" id="extraSource">
                        <#list sources as source>
                            <label>
                                <input type="checkbox" name="extraSource"  class="extraSource" value="${source.name()}">${source.name()}
                            </label>
                        </#list>
                    </div>
                </div>

            </#if>

        <h3><span>借款人基本信息</span></h3>
        <hr class="top-line">
        <div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款人姓名: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-loaner-user-name" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="借款人姓名不能为空" value="${loanInfo.loanerUserName}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款人性别: </label>
                <div class="col-sm-4">
                    <select class="selectpicker b-width jq-loaner-gender">
                        <option value="MALE" <#if loanInfo.loanerGender == 'MALE'>selected</#if>>男</option>
                        <option value="FEMALE" <#if loanInfo.loanerGender == 'FEMALE'>selected</#if>>女</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款人年龄: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-loaner-age" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="借款人年龄填写有误" value="${loanInfo.loanerAge}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款人身份证号: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-loaner-identity-number"
                           datatype="idcard" autocomplete="off"
                           placeholder="" errormsg="借款人身份证号填写有误" value="${loanInfo.loanerIdentityNumber}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款人婚姻情况: </label>

                <div class="col-sm-4">
                    <select class="selectpicker b-width jq-loaner-marriage">
                        <option value="UNMARRIED" <#if loanInfo.loanerMarriage == 'UNMARRIED'>selected</#if>>未婚</option>
                        <option value="MARRIED" <#if loanInfo.loanerMarriage == 'MARRIED'>selected</#if>>已婚</option>
                        <option value="DIVORCE" <#if loanInfo.loanerMarriage == 'DIVORCE'>selected</#if>>离异</option>
                        <option value="NONE" <#if loanInfo.loanerMarriage == 'NONE'>selected</#if>>不明</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款人所在地区: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-loaner-region" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="借款人所在地区不能为空" value="${loanInfo.loanerRegion}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款人年收入: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-loaner-income" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="借款人年收入不能为空" value="${loanInfo.loanerIncome}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款人从业情况: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-loaner-employment" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="借款人从业情况不能为空" value="${loanInfo.loanerEmploymentStatus}">
                </div>
            </div>
        </div>
        <h3><span>抵押物信息</span></h3>
        <hr class="top-line">
        <div id="pledge-details">
            <div class="form-group">
                <label class="col-sm-2 control-label">抵押物所在地: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-pledge-location" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="抵押物所在地不能为空" value="${loanInfo.pledgeLocation}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">借款抵押物估值: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-pledge-estimate-amount" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="借款抵押物估值不能为空" value="${loanInfo.estimateAmount}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">抵押物借款金额: </label>

                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-pledge-loan-amount" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="抵押物借款金额不能为空" value="${loanInfo.pledgeLoanAmount}">
                </div>
            </div>
            <#if loanInfo.pledgeType == 'HOUSE'>
                <div class="form-group">
                    <label class="col-sm-2 control-label">不动产登记证明:编号: </label>

                    <div class="col-sm-4">
                        <input type="text" class="form-control ui-autocomplete-input jq-pledge-estate-register-id"
                               datatype="*" autocomplete="off"
                               placeholder="" errormsg="不动产登记证明不能为空" value="${loanInfo.estateRegisterId}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">房本编号: </label>

                    <div class="col-sm-4">
                        <input type="text" class="form-control ui-autocomplete-input jq-pledge-property-card-id"
                               datatype="*" autocomplete="off"
                               placeholder="" errormsg="房本编号不能为空" value="${loanInfo.propertyCardId}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">借款抵押房产面积: </label>

                    <div class="col-sm-4">
                        <input type="text" class="form-control ui-autocomplete-input jq-pledge-square" datatype="*"
                               autocomplete="off"
                               placeholder="" errormsg="借款抵押房产面积不能为空" value="${loanInfo.square}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">公证书: </label>

                    <div class="col-sm-4">
                        <input type="text" class="form-control ui-autocomplete-input jq-pledge-authentic-act"
                               datatype="*" autocomplete="off"
                               placeholder="" errormsg="公证书不能为空" value="${loanInfo.authenticAct}">
                    </div>
                </div>
            </#if>
            <#if loanInfo.pledgeType == 'VEHICLE'>
                <div class="form-group">
                    <label class="col-sm-2 control-label">借款抵押车辆型号: </label>

                    <div class="col-sm-4">
                        <input type="text" class="form-control ui-autocomplete-input jq-pledge-model" datatype="*"
                               autocomplete="off"
                               placeholder="" errormsg="抵押车辆型号不能为空" value="${loanInfo.model}">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">借款抵押车辆品牌: </label>

                    <div class="col-sm-4">
                        <input type="text" class="form-control ui-autocomplete-input jq-pledge-brand" datatype="*"
                               autocomplete="off"
                               placeholder="" errormsg="抵押车辆品牌不能为空" value="${loanInfo.brand}">
                    </div>
                </div>
            </#if>
        </div>
        <h3><span>声明</span></h3>
        <hr class="top-line"/>
        <div>
            <div class="form-group">
                <div class="col-sm-4">
                    <input type="text" class="form-control ui-autocomplete-input jq-loan-declaration" datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="声明不能为空" value="${loanInfo.declaration}">
                </div>
            </div>
        </div>
        <h3><span>审核材料</span></h3>
        <hr class="top-line"/>
        <div>
            <div class="form-group">
                <label class="col-sm-2 control-label">申请材料: </label>

                <div class="col-sm-10">
                    <button type="button" class="btn-upload btn btn-info">＋</button>
                </div>
            </div>
            <div class="upload-box"></div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">属性: </label>

            <div class="col-sm-4">
                <div class="checkbox jq-checkbox">
                    <label>
                        <input type="checkbox" class="jq-index" value="1" <#if loanInfo.showOnHome>checked</#if>
                               <#if loanInfo.loanStatus!="PREHEAT" && loanInfo.loanStatus!= "WAITING_VERIFY" && loanInfo.loanStatus!= "RAISING">disabled="disabled"</#if>>
                        首页
                    </label>
                </div>
            </div>
        </div>
        <#if loanInfo.verifyTime??>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                初审时间:
            </label>
            <div class="col-sm-4">
                <input type="text" class="form-control" disabled="disabled" value="${loanInfo.verifyTime?string('yyyy-MM-dd HH:mm:ss')}"/>
            </div>
        </div>
        </#if>
        <#if loanInfo.verifyLoginName??>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                初审人员:
            </label>
            <div class="col-sm-4">
                <input type="text" class="form-control" disabled="disabled" value="${loanInfo.verifyLoginName}"/>
            </div>
        </div>
        </#if>
        <#if loanInfo.recheckTime??>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                复审时间:
            </label>
            <div class="col-sm-4">
                <input type="text" class="form-control" disabled="disabled" value="${loanInfo.recheckTime?string('yyyy-MM-dd HH:mm:ss')}"/>
            </div>
        </div>
        </#if>
        <#if loanInfo.recheckLoginName??>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                复审人员:
            </label>
            <div class="col-sm-4">
                <input type="text" class="form-control" disabled="disabled" value="${loanInfo.recheckLoginName}"/>
            </div>
        </div>
        </#if>
        <input type="hidden" class="jq-creator" value="${loanInfo.createdLoginName}"/><!-- 创建者 -->

        <div class="form-group">
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <input type="hidden" class="jq-pact" value="${contractId}"/><!-- 默认合同ID -->
                <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                    <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="save">保存</button>
                </@security.authorize>
                <#if loanInfo.loanStatus == "WAITING_VERIFY">
                    <@security.authorize access="hasAnyAuthority('OPERATOR')">
                        <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="apply-audit">提交审核</button>
                    </@security.authorize>
                    <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                        <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="ok">审核通过</button>
                        <button TYPE="button" class="btn jq-btn-refuse btn-danger" data-loanId="${loanInfo.id?c}">审核拒绝</button>
                    </@security.authorize>
                </#if>
                <#if loanInfo.loanStatus == "RECHECK">
                    <#if loanInfo.raisingCompleteTime??>
                        <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="recheck">放款</button>
                    <#else>
                        <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="delay">延期</button>
                        <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="cancel">流标</button>
                    </#if>
                </#if>
            </div>
        </div>
    </form>
</div>
<!-- content area end -->
</@global.main>
