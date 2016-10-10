<script>
    var API_SELECT = '${requestContext.getContextPath()}/project-manage/loan/titles';  // 申请资料标题url
    var API_POST_TITLE = '${requestContext.getContextPath()}/project-manage/loan/title';  //
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
        <section id="section-one">
            <h3><span>项目信息</span></h3>
            <hr class="top-line">
            <div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">借款项目名称: </label>

                    <div class="col-sm-4">
                        <select name="name" class="selectpicker">
                            <option value="房产抵押借款" selected="selected">房产抵押借款</option>
                            <option value="车辆抵押借款">车辆抵押借款</option>
                            <option value="税易经营借款">税易经营借款</option>
                        </select>
                    </div>
                </div>

                <input name="pledgeType" type="hidden" value="NONE">

                <div class="form-group">
                    <label class="col-sm-2 control-label">代理用户: </label>

                    <div class="col-sm-2">
                        <input name="agent" type="text" class="form-control ui-autocomplete-input" datatype="*"
                               autocomplete="off"
                               placeholder="" errormsg="代理用户不能为空">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">标的类型: </label>

                    <div class="col-sm-4">
                        <select name="loanType" class="selectpicker b-width">
                            <option value="">请选择</option>
                            <#list loanTypes as loanType>
                                <option value="${loanType.name()}">${loanType.getName()}</option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">借款期限（天）: </label>

                    <div class="col-sm-2">
                        <select name="productType" class="selectpicker">
                            <option value="">请选择</option>
                            <#list productTypes as productType>
                                <option value="${productType.name()}">${productType.getDuration()}</option>
                            </#list>
                        </select>
                    </div>

                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">预计出借金额（元）: </label>

                    <div class="col-sm-4">
                        <input name="loanAmount" type="text" class="form-control jq-money" placeholder=""
                               datatype="money_fl"
                               errormsg="预计出借金额需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">单笔最小投资金额（元）: </label>

                    <div class="col-sm-4">
                        <input name="minInvestAmount" type="text" class="form-control jq-money" value="50.00"
                               datatype="money_fl"
                               errormsg="单笔最小投资金额需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">投资递增金额（元）: </label>

                    <div class="col-sm-4">
                        <input name="investIncreasingAmount" type="text" class="form-control jq-money" value="50.00"
                               datatype="money_fl"
                               errormsg="投资递增金额需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">个人最大投资金额（元）: </label>

                    <div class="col-sm-4">
                        <input name="maxInvestAmount" type="text" class="form-control jq-money" value="999999.00"
                               datatype="money_fl"
                               errormsg="个人最大投资金额需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">活动类型: </label>

                    <div class="col-sm-2">
                        <select name="activityType" class="selectpicker">
                            <#list activityTypes as activityType>
                                <option value="${activityType.name()}">
                                ${activityType.getActivityTypeName()}
                                </option>
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

                    <div class="col-sm-4">
                        <input name="activityDesc" type="text" class="form-control" disabled="disabled" maxlength="4">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">基本利率（%）: </label>

                    <div class="col-sm-4">
                        <input name="baseRate" type="text" class="form-control jq-money"
                               datatype="money_fl" errormsg="基本利率需要正确填写">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">活动利率（%）: </label>

                    <div class="col-sm-4">
                        <input name="activityRate" type="text" class="form-control jq-money" datatype="money_fl"
                               errormsg="活动利率需要正确填写">
                    </div>
                    <div class="col-sm-6">
                        <div class="form-control-static">适用于所有标(0 表示无),站点前端以(基本利率%+加息利率%)方式展现,如(10%+2%)。</div>
                    </div>
                </div>

                <div class="form-group input-append">
                    <label class="col-sm-2 control-label">筹款启动时间: </label>

                    <div class="col-sm-4">
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

                    <div class="col-sm-4">
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

                    <div class="col-sm-4 checkbox">
                        <label for="extra">
                            <input type="checkbox" id="extra">选中后此标的采用投资奖励
                        </label>
                    </div>
                </div>

                <div class="form-group extra-rate hidden">
                </div>

                <div class="form-group extra-source hidden">
                    <label class="col-sm-2 control-label">投资奖励来源:</label>

                    <div class="col-sm-2 checkbox" id="extraSource">
                        <#list extraSources as source>
                            <label>
                                <input name="extraSource" type="checkbox" value="${source.name()}">${source.name()}</input>
                            </label>
                        </#list>
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
                <div class="col-sm-4">
                    <input name="declaration" type="text" class="form-control ui-autocomplete-input jq-loan-declaration"
                           datatype="*"
                           autocomplete="off"
                           placeholder="" errormsg="声明不能为空">
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
            <label class="col-sm-2 control-label"></label>
            <div class="col-sm-4 form-error">
            </div>
        </div>

        <div class="form-group">
            <input name="contractId" type="hidden" value="${contractId}"/><!-- 默认合同ID -->
            <label class="col-sm-2 control-label">操作: </label>
            <div class="col-sm-4">
                <button type="button" class="btn jq-btn-form btn-primary">保存</button>
            </div>
        </div>

    </form>
</div>
<!-- content area end -->
</@global.main>
