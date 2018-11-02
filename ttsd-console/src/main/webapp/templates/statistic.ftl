<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="statistic.js" headLab="statistic" sideLab="userDate" title="系统首页">
<div class="col-md-10 home-report">

    <div class="row">

        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户注册时间分布</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" id="formUserDateReport">
                        <select class="form-control search-category granularity-select" name="granularity">
                            <option value="Daily" selected>日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly">月</option>
                            <option value="Hourly">小时</option>
                        </select>
                        <em class="start-time-word">开始时间</em>： <input type="text" class="form-control start-date" name="startTime">
                        <span class="over-end-date">结束时间：<input type="text" class="form-control end-date" name="endTime"></span>
                        <select class="form-control" name="province"></select>
                        <select class="form-control" name="userStage">
                            <option value="ALL" selected>全部</option>
                            <option value="Certification">实名认证</option>
                            <option value="BindCard">绑卡</option>
                        </select>
                        <select class="form-control" name="roleStage">
                            <option value="ALL" selected>全部用户</option>
                            <option value="ZC_STAFF">资产业务员</option>
                            <option value="SD_STAFF">速贷业务员</option>
                            <option value="SD_STAFF_RECOMMENDATION">速贷业务员的一级推荐</option>
                            <option value="ZC_STAFF_RECOMMENDATION">资产业务员的一级推荐</option>
                            <option value="AGENT">渠道用户</option>
                            <option value="NOT_STAFF_RECOMMEND">自然用户</option>
                            <option value="OTHERS">其他用户</option>
                        </select>
                        <select class="form-control" name="channel"></select>
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
                        <select class="form-control search-category granularity-select" name="granularity">
                            <option value="Daily" selected>日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly">月</option>
                            <option value="Hourly">小时</option>
                        </select>
                        <em class="start-time-word">开始时间</em>：<input type="text" class="form-control start-date" name="startTime">
                        <span class="over-end-date">结束时间：<input type="text" class="form-control end-date" name="endTime"></span>
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                        </select>
                        <select class="form-control" name="role">
                            <option value="">全部</option>
                            <option value="LOANER">权证人</option>
                            <option value="UN_LOANER">非权证人</option>
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
                        <select class="form-control search-category granularity-select" name="granularity">
                            <option value="Daily" selected>日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly">月</option>
                            <option value="Hourly">小时</option>
                        </select>

                        <em class="start-time-word">开始时间</em>： <input type="text" class="form-control start-date" name="startTime">
                        <span class="over-end-date">结束时间：<input type="text" class="form-control end-date" name="endTime"></span>
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                        </select>
                        <select class="form-control" name="role">
                            <option value="">全部</option>
                            <option value="LOANER">权证人</option>
                            <option value="UN_LOANER">非权证人</option>
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
                        <select class="form-control" name="role">
                            <option value="">全部</option>
                            <option value="LOANER">权证人</option>
                            <option value="UN_LOANER">非权证人</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="withdrawUserCountDistribution" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户续投情况-投资标的数</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formUserInvestViscosityReport">
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>
                    <div id="userInvestViscosity" style="width:100%; height:400px;">
                    </div>

                    <div class="row" id="boxUserInvest" style="display: none">
                        <div class="title-list">
                            <span>合计投资金额： <em class="sumAmount"></em> 元</span>
                        </div>
                        <table class="table table-bordered" >
                            <thead>
                            <tr>
                                <th>用户名</th>
                                <th>真实姓名</th>
                                <th>电话</th>
                                <th>推荐人id</th>
                                <th>推荐人姓名</th>
                                <th>投资总金额(元)</th>
                                <th>投资标的数</th>
                                <th>上次投资时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td class="pageNumber" colspan="10">
                                    <span class="pageBtn"></span>总共<span class="TotalRecords"></span>条
                                </td>
                            </tr>
                            </tfoot>
                        </table>

                        <button class="btn btn-default pull-left down-load viscosity-export" type="button">导出Excel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户续投情况-投资次数</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formUserInvestCountViscosityReport">
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <select class="form-control" name="province">
                            <option value="">请选择</option>
                        </select>
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>
                    <div id="userInvestCountViscosity" style="width:100%; height:400px;">
                    </div>

                    <div class="row" id="boxUserInvestCount" style="display: none">
                        <div class="title-list">
                            <span>合计投资金额： <em class="sumAmount"></em> 元</span>
                        </div>
                        <table class="table table-bordered" >
                            <thead>
                            <tr>
                                <th>用户名</th>
                                <th>真实姓名</th>
                                <th>电话</th>
                                <th>推荐人id</th>
                                <th>推荐人姓名</th>
                                <th>投资总金额(元)</th>
                                <th>投资次数</th>
                                <th>上次投资时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td class="pageNumber" colspan="10">
                                    <span class="pageBtn"></span>总共<span class="TotalRecords"></span>条
                                </td>
                            </tr>
                            </tfoot>
                        </table>

                        <button class="btn btn-default pull-left down-load viscosity-export" type="button">导出Excel</button>
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
                        </select>
                        <select class="form-control" name="roleStage">
                            <option value="ALL" selected>全部用户</option>
                            <option value="ZC_STAFF">资产业务员</option>
                            <option value="SD_STAFF">速贷业务员</option>
                            <option value="SD_STAFF_RECOMMENDATION">速贷业务员的一级推荐</option>
                            <option value="ZC_STAFF_RECOMMENDATION">资产业务员的一级推荐</option>
                            <option value="AGENT">渠道用户</option>
                            <option value="NOT_STAFF_RECOMMEND">自然用户</option>
                        </select>
                        <select class="form-control" name="channel"></select>
                        <select class="form-control" name="isTransfer">
                            <option value="">全部</option>
                            <option value="true">债权转让</option>
                            <option value="false">直投项目</option>
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
                    <h3 class="panel-title">放款金额时间分布</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formloanOutAmountReport">
                        <select class="form-control search-category" name="granularity">
                            <option value="Daily" selected>日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly">月</option>
                        </select>
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>
                    <div id="loanOutAmountDistribution" style="width:100%; height:400px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户投资次数时间分布</h3>
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
                        </select>
                        <select class="form-control" name="isTransfer">
                            <option value="">全部</option>
                            <option value="true">债权转让</option>
                            <option value="false">直投项目</option>
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

        <div class="row">
            <div class="col-lg-12 col-sm-12">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title">标的满标周期分布</h3>
                    </div>
                    <div class="panel-body">
                        <form class="form-inline" id="formLoanRaisingTimeCostingReport">
                            开始时间： <input type="text" class="form-control start-date" name="startTime">
                            结束时间：<input type="text" class="form-control end-date" name="endTime">
                            <button class="btn btn-primary" type="button">查询</button>
                        </form>

                        <div id="loanRaisingTimeCostingDistribution" style="width:100%; height:400px;">

                            <span class="loading-report">加载中...</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">标的资金分布</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formLoanAmountReport">
                        开始时间： <input type="text" class="form-control start-date" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime">
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="loanAmountDistribution" style="width:100%; height:8100px;">
                        <span class="loading-report">加载中...</span>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">平台待收</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="platformSumRepayByTimeReport">
                        <select class="form-control search-category granularity-select" name="granularity">
                            <option value="Daily">日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly" selected>月</option>
                        </select>
                        开始时间： <input type="text" class="form-control start-date" id="expenseStartTime" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime" id="expenseEndTime">
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="platformSumRepayByTimeDistribution" style="width:100%; height:400px;">
                        <span class="loading-report">加载中...</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">平台支出</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="platformOut">
                        <select class="form-control search-category granularity-select" name="granularity">
                            <option value="Daily">日</option>
                            <option value="Weekly">周</option>
                            <option value="Monthly" selected>月</option>
                        </select>
                        开始时间： <input type="text" class="form-control start-date" id="repayStartTime" name="startTime">
                        结束时间：<input type="text" class="form-control end-date" name="endTime" id="repayEndTime">
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="platformOutDistribution" style="width:100%; height:400px;">
                        <span class="loading-report">加载中...</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">安心签用户状态统计</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="anxinUserStatus">
                        开始时间： <input type="text" class="form-control anxin-start-date" id="startTime" name="startTime">
                        结束时间：<input type="text" class="form-control  anxin-end-date" name="endTime" id="endTime">
                        <button class="btn btn-primary" type="button">查询</button>
                    </form>
                    <div id="anxinUserStatusStatistics" style="width:100%; height:400px;">
                        <span class="loading-report">加载中...</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

</@global.main>