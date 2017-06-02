<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="newman_tyrant.js" headLab="activity-manage" sideLab="newmanTyrant" title="新贵富豪争霸赛活动管理">

<div class="col-md-10" style="margin-bottom:50px;">
    <div class="col-md-12">
        <div class="btn btn-default invest active">投资榜单查看</div>
        <div class="btn btn-default upload">上传神秘大奖</div>
    </div>
</div>
<div class="col-md-10 select-date" style="margin-bottom:20px;">
    <div class="col-md-2">
        <div class='input-group date' id='datepicker'>
            <input type='text' class="form-control" id='tradingTime' value="${tradingTime?string('yyyy-MM-dd')}"/>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
    <div class="col-md-10 avg-tyrant" style="line-height: 34px;">
        <strong>富豪榜当日前十平均投资金额：${(avgTyrantInvestAmount/100)?string('0.00')}元</strong>
    </div>
</div>
<div class="col-md-10">
    <div class="table-responsive col-md-8 tyrant-ranking">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>排名</th>
                <th>用户名</th>
                <th>手机号</th>
                <th>姓名</th>
                <th>今日投资金额(元)</th>
            </tr>
            </thead>
            <tbody>
                <#list tyrantViews as tyrant>
                <tr>
                    <td>${tyrant_index + 1}</td>
                    <td>${tyrant.loginName!}</td>
                    <td>${tyrant.mobile!}</td>
                    <td>${tyrant.userName!}</td>
                    <td>${(tyrant.sumAmount/100)?string('0.00')}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <div class="col-md-10 avg-newman" style="line-height: 34px;">
        <strong>新贵榜当日前三平均投资金额：${(avgNewmanInvestAmount/100)?string('0.00')}元</strong>
    </div>
    <div class="table-responsive col-md-8 newman-ranking">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>排名</th>
                <th>用户名</th>
                <th>手机号</th>
                <th>姓名</th>
                <th>今日投资金额(元)</th>
            </tr>
            </thead>
            <tbody>
                <#list newmanViews as newman>
                <tr>
                    <td>${newman_index + 1}</td>
                    <td>${newman.loginName!}</td>
                    <td>${newman.mobile!}</td>
                    <td>${newman.userName!}</td>
                    <td>${(newman.sumAmount/100)?string('0.00')}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="upload-image" style="display: none">
        <form class="prize-form" action="/activity-console/activity-manage/newman-tyrant/upload-image" method="post">

            <div class="form-group clearfix">
                <label class="col-sm-1 control-label">金奖名称: </label>
                <div class="col-sm-4">
                    <input type="text" name="goldPrizeName" class="form-control gold-prize-name" placeholder="" datatype="*"
                           errormsg="金奖名称不能为空">
                </div>
                <div class="col-sm-7"></div>
            </div>

            <div class="form-group clearfix">
                <label class="col-sm-1 control-label">金奖图片: </label>
                <div class="col-sm-4 ">
                    <input type="text" name="goldImageUrl" readonly class="form-control gold-image-url" placeholder=""
                           datatype="*" errormsg="奖金图片不能为空">
                    <div class="goldThumbImage">

                    </div>
                </div>
                <div class="col-sm-4 gold-prize-image">
                    <input type="file" name="prizeImage"/>
                </div>
                <div class="col-sm-4 text-danger">
                    (图片最大为:202px * 202px)
                </div>
            </div>

            <div class="form-group clearfix">
                <label class="col-sm-1 control-label">银奖名称: </label>
                <div class="col-sm-4">
                    <input type="text" name="silverPrizeName" class="form-control silver-prize-name" placeholder="" datatype="*"
                           errormsg="银奖名称不能为空">
                </div>
                <div class="col-sm-7"></div>
            </div>

            <div class="form-group clearfix">
                <label class="col-sm-1 control-label">银奖图片: </label>
                <div class="col-sm-4 ">
                    <input type="text" name="silverImageUrl" readonly class="form-control silver-image-url" placeholder=""
                           datatype="*" errormsg="银奖图片不能为空">
                    <div class="silverThumbImage">

                    </div>
                </div>
                <div class="col-sm-4 silver-prize-image">
                    <input type="file" name="prizeImage"/>
                </div>
                <div class="col-sm-4 text-danger">
                    (图片最大为:202px * 202px)
                </div>
            </div>


            <div class="form-group clearfix">
                <label class="col-sm-1 control-label"></label>
                <div class="col-sm-2 form-error">
                </div>
            </div>


            <div class="form-group clearfix">
                <label class="col-sm-1 control-label">操作: </label>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="col-sm-11">
                    <button type="button" class="btn jq-btn-form btn-primary tomorrow-prize-save">更新第二天奖品</button>
                    更新后当日24:00自动生效
                    <div class="tomorrowThumbImage">
                        <#if tomorrowDto??>
                            <img src="${commonStaticServer}${tomorrowDto.goldImageUrl!}"
                                 alt="金奖缩略图">（金奖）
                            <img src="${commonStaticServer}${tomorrowDto.silverImageUrl!}"
                                 alt="银奖缩略图">（银奖）
                        </#if>
                    </div>


                </div>
            </div>
            <div class="form-group clearfix">
                <label class="col-sm-1 control-label"></label>
                <div class="col-sm-11">
                    <button type="button" class="btn jq-btn-form btn-primary today-prize-save">修改今日奖品</button>
                    修改后即时发布到页面,请慎用
                    <div class="todayThumbImage">
                        <#if todayDto??>
                            <img src="${commonStaticServer}${todayDto.goldImageUrl!}"
                                 alt="金奖缩略图" />（金奖）
                            <img src="${commonStaticServer}${todayDto.silverImageUrl!}"
                                 alt="银奖缩略图">（银奖）
                        </#if>
                    </div>
                </div>
                <div class="col-sm-7">
                </div>
            </div>

        </form>
    </div>
</div>
</@global.main>