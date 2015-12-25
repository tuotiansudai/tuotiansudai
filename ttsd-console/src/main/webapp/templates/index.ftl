<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="index.js" headLab="sys-manage" sideLab="userDate" title="系统首页">
<div class="col-md-10 home-report">

    <div class="row">

        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户注册时间分布</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" id="formUserDateReport">
                                <select class="form-control search-category" name="granularity">
                                    <option value="Daily" selected>日</option>
                                    <option value="Weekly">周</option>
                                    <option value="Monthly">月</option>
                                </select>
                                开始时间： <input type="text" class="form-control start-date" name="startTime">
                                结束时间：<input type="text" class="form-control end-date" name="endTime">
                                <select class="form-control" name="province"></select>
                                <select class="form-control" name="userStage">
                                    <option value="ALL" selected>全部</option>
                                    <option value="Certification">实名认证</option>
                                    <option value="BindCard">绑卡</option>
                                </select>
                                <select class="form-control" name="roleStage">
                                    <option value="ALL" selected>全部用户</option>
                                    <option value="STAFF">业务员及一级推荐</option>
                                    <option value="AGENT">代理商</option>
                                    <option value="OTHERS">其他用户</option>
                                </select>
                        <#--<select class="form-control" name=""></select>-->
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>
                    <div  id="userDateDistribution" style="width:100%; height:400px;">

                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户充值时间分布</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" id="formUserRechargeReport">
                            <select class="form-control search-category" name="granularity">
                                <option value="Daily" selected>日</option>
                                <option value="Weekly">周</option>
                                <option value="Monthly">月</option>
                            </select>
                            开始时间： <input type="text" class="form-control start-date" name="startTime">
                            结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                            <option>北京</option>
                            <option>天津</option>
                        </select>


                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                   <div id="UserRechargeDistribution" style="width:100%; height:400px;">

                   </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户提现时间分布</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" id="formWithdrawReport">
                        <select class="form-control search-category" name="granularity">
                            <option value="Daily" selected>日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly">月</option>
                        </select>

                            开始时间： <input type="text" class="form-control start-date" name="startTime">
                            结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                            <option>北京</option>
                            <option>天津</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="userWithdrawDistribution" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户续投情况</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formUserInvestViscosityReport">
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                            <option>北京</option>
                            <option>天津</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>
                    <div id="userInvestViscosity" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户投资金额时间分布</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formUserInvestAmountReport">
                        <select class="form-control search-category" name="granularity">
                            <option value="Daily" selected>日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly">月</option>
                        </select>
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                            <option>北京</option>
                            <option>天津</option>
                        </select>
                        <select class="form-control" name="roleStage">
                            <option value="ALL" selected>全部用户</option>
                            <option value="STAFF">业务员及一级推荐</option>
                            <option value="AGENT">代理商</option>
                            <option value="OTHERS">其他用户</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>
                    <div id="userInvestAmountDistribution" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户投资时间分布</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formUserInvestCountReport">
                        <select class="form-control search-category" name="granularity">
                            <option value="Hourly" selected>时</option>
                        </select>
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                            <option>北京</option>
                            <option>天津</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="userInvestCountDistribution" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6 col-sm-6">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">实名认证用户年龄分布</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formRegisterUserAgeReport">
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                            <option>北京</option>
                            <option>天津</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="registerUserAgeDistribution" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6 col-sm-6">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">投资人用户年龄分布</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formInvestorUserAgeReport">
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                            <option>北京</option>
                            <option>天津</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="investorUserAgeDistribution" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">提现人数分布</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" id="formWithdrawUserCountReport">
                        <select class="form-control search-category" name="granularity">
                            <option value="Daily" selected>日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly">月</option>
                        </select>

                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="withdrawUserCountDistribution" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</@global.main>