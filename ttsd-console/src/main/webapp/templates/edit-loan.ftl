<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>筹款编辑</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
<@global.csrf></@global.csrf>
    <!-- link bootstrap css and js -->
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <!-- link bootstrap css and js -->
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/index.css">
    <!-- 日历插件 -->
    <link href="../style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <!--上传图片插件-->
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/libs/fileinput.css"/>
    <!--下拉框-->
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/libs/bootstrap-select.css"/>
    <!--自动补全-->
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/libs/jquery-ui/jquery-ui-1.11.4.min.css"/>
    <script src="/js/libs/template.js"></script>
<#--当前页面js-->
<@global.javascript pageJavascript="editLoan.js"></@global.javascript>
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

                        <input type="hidden" class="jq-txt" value="{{_data[0]['id']}}" />
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
</head>
<body>
<@menu.header label="projectMain"></@menu.header>
<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">

        <@menu.sidebar headLab="projectMain" sideLab=""></@menu.sidebar>

            <!-- content area begin -->
            <div class="col-md-10">
                <form class="form-horizontal jq-form">
                    <input type="hidden" class="jq-loanId" value="${loanInfo.id}">
                    <input type="hidden" class="jq-status" value="${loanInfo.status}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款项目名称: </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-user" placeholder="" datatype="*" errormsg="借款项目名称不能为空" value="${loanInfo.name}" <#if loanInfo.status!= "WAITING_VERIFY">disabled="disabled"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">代理用户: </label>

                        <div class="col-sm-4">
                            <input type="text" id="tags_1" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off"
                                   placeholder="" errormsg="代理用户不能为空" value="${loanInfo.agentLoginName}" <#if loanInfo.status!= "WAITING_VERIFY">disabled="disabled"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款用户: </label>

                        <div class="col-sm-4">
                            <input type="text" id="tags" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off"
                                   placeholder="" errormsg="借款用户不能为空" value="${loanInfo.loanerLoginName}" <#if loanInfo.status!= "WAITING_VERIFY">disabled="disabled"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">标的类型: </label>

                        <div class="col-sm-4">
                            <select class="selectpicker jq-b-type" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                            <#list loanTypes as loanType>
                                <option value="${loanType.name()}" data-repayTimeUnit="${loanType.getLoanPeriodUnit()}" data-repayTimePeriod="1" <#if loanType.name()==loanInfo.type>selected</#if>>
                                    ${loanType.getName()}
                                </option>
                            </#list>
                            </select>
                            <input type="hidden" class="jq-mark-type" value="${loanInfo.type}" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款期限: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-timer" placeholder="" datatype="num"  errormsg="借款期限需要填写数字" value="${loanInfo.periods}" <#if loanInfo.status!= "WAITING_VERIFY">disabled="disabled"</#if>>

                        </div>
                        <div class="col-sm-3">
                            <div class="form-control-static">(单位：
                                <label class="jq-day">1</label>
                                <label class="jq-piex"><#if loanInfo.type == 'INVEST_INTEREST_MONTHLY_REPAY' || loanInfo.type == 'LOAN_INTEREST_MONTHLY_REPAY'>月<#else>天</#if></label>
                                )
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">项目描述: </label>

                        <div class="col-sm-10">
                            <script id="editor" type="text/plain"><#if loanInfo.descriptionHtml??>${loanInfo.descriptionHtml}</#if></script>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">申请材料: </label>

                        <div class="col-sm-10">
                            <button type="button" class="btn-upload btn btn-info">＋</button>
                        </div>
                    </div>
                    <div class="upload-box"></div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">预计出借金额（元）: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-pay jq-money" placeholder="" datatype="money_fl" errormsg="预计出借金额需要正确填写" value="${(loanInfo.loanAmount/100)?string('0.00')}" <#if loanInfo.status!= "WAITING_VERIFY">disabled="disabled"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">投资手续费比例(%): </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-fee jq-money" placeholder="" datatype="money_fl" errormsg="投资手续费比例需要正确填写" value="${(loanInfo.investFeeRate*100)?string('0.00')}" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-control-static"> 还款时收取所得利息的百分比。</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">最小投资金额（元）: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-min-pay jq-money" datatype="money_fl" errormsg="最小投资金额需要正确填写" value="${(loanInfo.minInvestAmount/100)?string('0.00')}" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING" && loanInfo.status!="RECHECK">disabled="disabled"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">投资递增金额（元）: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-add-pay jq-money" datatype="money_fl" errormsg="投资递增金额需要正确填写" value="${(loanInfo.investIncreasingAmount/100)?string('0.00')}" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">个人最大投资金额（元）: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-max-pay jq-money" datatype="money_fl" errormsg="单笔最大投资金额需要正确填写" value="${(loanInfo.maxInvestAmount/100)?string('0.00')}" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">活动类型: </label>

                        <div class="col-sm-4">
                            <select class="selectpicker " <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                            <#list activityTypes as activityType>
                                <option value="${activityType.name()}" <#if activityType.name() == loanInfo.activityType>selected</#if>>
                                    ${activityType.getActivityTypeName()}
                                </option>
                            </#list>
                            </select>
                            <input type="hidden" class="jq-impact-type" value="${loanInfo.activityType}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">活动利率(%): </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-percent jq-money" placeholder="" datatype="money_fl" errormsg="活动利率需要正确填写" value="${(loanInfo.activityRate*100)?string('0.00')}" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-control-static">适用于所有标(0 表示无),站点前端以(基本利率%+加息利率%)方式展现,如(10%+2%)。</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">基本利率(%): </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-base-percent jq-money" placeholder="" datatype="money_fl" errormsg="基本利率需要正确填写" value="${(loanInfo.baseRate*100)?string('0.00')}" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">合同: </label>

                        <div class="col-sm-4">
                            <select class="selectpicker" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                            <#list contracts as contract>
                                <option value="${contract.id}">
                                ${contract.contractName}
                                </option>
                            </#list>
                            </select>
                            <input type="hidden" class="jq-pact"/>
                        </div>
                    </div>
                    <div class="form-group input-append">
                        <label class="col-sm-2 control-label">筹款启动时间: </label>

                        <div class="col-sm-4">
                            <div class='input-group date' id='datetimepicker6'>
                                <input type='text' class="form-control jq-star-date" datatype="date" errormsg="筹款启动时间需要正确填写" value="${(loanInfo.fundraisingStartTime?string('yyyy-MM-dd HH:mm'))!}" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>/>
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
                                <input type='text' class="form-control jq-end-date" datatype="date" errormsg="筹款截止时间需要正确填写" value="${(loanInfo.fundraisingEndTime?string('yyyy-MM-dd HH:mm'))!}" <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING" && loanInfo.status!="RECHECK">disabled="disabled"</#if>/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                        </div>
                    </div>

                    <!--<div class="form-group">-->
                    <!--<label  class="col-sm-2 control-label">初审是否通过: </label>-->

                    <!--<div class="col-sm-4">-->
                    <!--<span class="label label-success "> 是</span>-->
                    <!--</div>-->
                    <!--</div>-->
                    <div class="form-group">
                        <label class="col-sm-2 control-label">属性: </label>

                        <div class="col-sm-4">
                            <div class="checkbox jq-checkbox">
                                <label>
                                    <input type="checkbox" class="jq-index" value="1" <#if loanInfo.showOnHome>checked</#if> <#if loanInfo.status!="PREHEAT" && loanInfo.status!= "WAITING_VERIFY" && loanInfo.status!= "RAISING">disabled="disabled"</#if>>
                                    首页
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"></label>
                        <div class="col-sm-4 form-error">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">操作: </label>

                        <div class="col-sm-4">
                            <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="save">保存</button>
                            <#if loanInfo.status == "WAITING_VERIFY">
                                <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="ok">审核通过</button>
                            </#if>
                            <#if loanInfo.status == "RECHECK">
                                <button TYPE="button" class="btn jq-btn-form btn-primary" data-operate="recheck">放款</button>
                            </#if>
                        </div>
                    </div>
                </form>
            </div>
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->

</body>
</html>
<script>
    var API_SELECT = '${requestContext.getContextPath()}/loan/titles';  // 申请资料标题url
    var API_POST_TITLE = '${requestContext.getContextPath()}/loan/title';  //
    var API_FORM = '${requestContext.getContextPath()}/loan/';
    var api_url = '${requestContext.getContextPath()}/loan/loaner';
    var rereq = {};
    <#if (loanTitleRelationModels?size>0)>
        <#list loanTitleRelationModels as loanTitleRelationModel>
            var initialPreview = [];
            <#list loanTitleRelationModel.applicationMaterialUrls?split(",") as title>
                initialPreview.push("<img src='${title}' class='file-preview-image' alt='${title}' title='${title}'>");
            </#list>
            rereq['${loanTitleRelationModel.titleId}']=initialPreview;
        </#list>
    </#if>

</script>