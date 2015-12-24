<script>
    var API_SELECT = '${requestContext.getContextPath()}/project-manage/loan/titles';  // 申请资料标题url
    var API_POST_TITLE = '${requestContext.getContextPath()}/project-manage/loan/title';  //
    var API_FORM = '${requestContext.getContextPath()}/project-manage/loan/';
</script>

<script id="upload" type="text/html">
    <div class="form-group">
        <label class="col-sm-2 control-label"></label>

        <div class="col-sm-10">
            <div class="row col-file-box">
                <div class="select-box">
                    <select class="selectpicker col-sm-5">
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
            <input type="file" multiple=true class="file-loading upload" name="upfile"/>
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
<@global.main pageCss="" pageJavascript="createLoan.js" headLab="project-manage" sideLab="start" title="发起借款">
<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal jq-form">
        <div class="form-group">
            <label class="col-sm-2 control-label">借款项目名称: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control jq-user" placeholder="" datatype="*" errormsg="借款项目名称不能为空" maxlength="6">
            </div>
            <div class="col-sm-6">
                <div class="form-control-static"> 限制6个字以内,不可写标的总额、期数、天数以及标的符号。</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">产品类型: </label>

            <div class="col-sm-4">
                <select class="selectpicker b-width product-type-name">
                    <option value="请选择产品类型" data-percent="0%" data-time="">请选择产品类型</option>
                    <option value="速盈利" data-percent="10%" data-time="1">速盈利</option>
                    <option value="稳盈绣" data-percent="12%" data-time="3">稳盈绣</option>
                    <option value="久盈富" data-percent="14%" data-time="6">久盈富</option>
                </select>
                <input type="hidden" class="product-type" value=""/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">代理用户: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control ui-autocomplete-input jq-agent" datatype="*" autocomplete="off"
                       placeholder="" errormsg="代理用户不能为空">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">借款人姓名: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control ui-autocomplete-input jq-loaner-user-name" datatype="*" autocomplete="off"
                       placeholder="" errormsg="借款人姓名不能为空">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">借款人身份证号: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control ui-autocomplete-input jq-loaner-identity-number" datatype="idcard" autocomplete="off"
                       placeholder="" errormsg="借款人身份证号填写有误">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">借款人平台ID: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control ui-autocomplete-input jq-loaner-login-name" datatype="*" autocomplete="off"
                       placeholder="" errormsg="借款人平台ID不能为空">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">标的类型: </label>

            <div class="col-sm-4">
                <select class="selectpicker jq-b-type b-width">
                    <#list loanTypes as loanType>
                        <option value="${loanType.name()}" data-repayTimeUnit="${loanType.getLoanPeriodUnit()}"
                                data-repayTimePeriod="1">
                        ${loanType.getName()}
                        </option>
                    </#list>
                </select>
                <input type="hidden" class="jq-mark-type" value=""/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">借款期限: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control jq-timer" placeholder="" datatype="num" errormsg="借款期限需要填写数字" id="loanPeriod" disabled="disabled">
            </div>
            <div class="col-sm-3">
                <div class="form-control-static">(单位：
                    <label class="jq-piex">月</label>
                    )
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">项目描述: </label>

            <div class="col-sm-10">
                <script id="editor" type="text/plain"></script>
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
                <input type="text" class="form-control jq-pay jq-money" placeholder="" datatype="money_fl"
                       errormsg="预计出借金额需要正确填写">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">投资手续费比例（%）: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control jq-fee jq-money" placeholder="" value="10%" datatype="money_fl"
                       errormsg="投资手续费比例需要正确填写">
            </div>
            <div class="col-sm-6">
                <div class="form-control-static"> 还款时收取所得利息的百分比。</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">最小投资金额（元）: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control jq-min-pay jq-money" value="50.00" datatype="money_fl"
                       errormsg="最小投资金额需要正确填写">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">投资递增金额（元）: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control jq-add-pay jq-money" value="50.00" datatype="money_fl"
                       errormsg="投资递增金额需要正确填写">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">个人最大投资金额（元）: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control jq-max-pay jq-money" value="999999.00" datatype="money_fl"
                       errormsg="单笔最大投资金额需要正确填写">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">活动类型: </label>

            <div class="col-sm-4">
                <select class="selectpicker ">
                    <#list activityTypes as activityType>
                        <#if activityType.name() != 'PROMOTION'>
                            <option value="${activityType.name()}">
                            ${activityType.getActivityTypeName()}
                            </option>
                        </#if>
                    </#list>
                </select>
                <input type="hidden" class="jq-impact-type"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">产品线类型: </label>
            <div class="col-sm-4">
                <select class="selectpicker">
                    <option value="">请选择</option>
                    <#list productLineTypes as productLineType>
                        <option value="${productLineType.name()}" data-period="${productLineType.getProductLineTypePeriod()}" data-baserate="${productLineType.getProductLineTypeBaseRate()?string('0.00')}">
                            ${productLineType.getProductLineTypeName()}
                        </option>
                    </#list>
                </select>
                <input type="hidden" class="jq-product-type" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">活动利率（%）: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control jq-percent jq-money" placeholder="" datatype="money_fl"
                       errormsg="活动利率需要正确填写">
            </div>
            <div class="col-sm-6">
                <div class="form-control-static">适用于所有标(0 表示无),站点前端以(基本利率%+加息利率%)方式展现,如(10%+2%)。</div>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">基本利率（%）: </label>

            <div class="col-sm-4">
                <input type="text" class="form-control jq-base-percent jq-money" placeholder="" id="baseRate" datatype="money_fl" disabled="disabled"
                       errormsg="基本利率需要正确填写">
            </div>
        </div>
        <div class="form-group input-append">
            <label class="col-sm-2 control-label">筹款启动时间: </label>

            <div class="col-sm-4">
                <div class='input-group date' id='datetimepicker6'>
                    <input type='text' class="form-control jq-star-date" datatype="date" errormsg="筹款启动时间需要正确填写"/>
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
                    <input type='text' class="form-control jq-end-date" datatype="date" errormsg="筹款截止时间需要正确填写"/>
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
                        <input type="checkbox" class="jq-index" value="1" checked>
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
            <input type="hidden" class="jq-pact" value="${contractId}"/><!-- 默认合同ID -->
            <label class="col-sm-2 control-label">操作: </label>

            <div class="col-sm-4">
                <button type="button" class="btn jq-btn-form btn-primary">保存</button>
            </div>
        </div>
    </form>
</div>
<!-- content area end -->
</@global.main>
