<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="index.js" headLab="sysMain" title="">
<div class="col-md-10">

    <div class="row">
        <div class="col-lg-12 col-sm-12">
        <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户地域分布</h3>
                </div>
                <div class="panel-body">

                    <form class="form-inline" id="formUserAreaReport">
                        <div class="form-group">
                            <select class="form-control search-category">
                                <option value="">请选择</option>
                                <option value="D" selected>日</option>
                                <option value="W">周</option>
                                <option value="M">月</option>
                            </select>
                        </div>
                        <div class="form-group search-input">
                           开始时间： <input type="text" class="form-control start-date">
                           结束时间： <input type="text" class="form-control end-date">

                        </div>
                        <div class="form-group search-input">
                            <select>
                                <option></option>
                            </select>
                            </div>

                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="userAreaDistribution" style="width:100%; height:400px;">

                    </div>

                </div>
            </div>
        </div>
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户时间分布</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" id="formUserDateReport">
                        <div class="form-group">
                            <select class="form-control search-category">
                                <option value="">请选择</option>
                                <option value="D" selected>日</option>
                                <option value="W">周</option>
                                <option value="M">月</option>
                            </select>
                        </div>
                        <div class="form-group search-input">
                            开始时间： <input type="text" class="form-control start-date">
                            结束时间：<input type="text" class="form-control end-date">

                        </div>

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

                        <div class="form-group">
                            <select class="form-control search-category">
                                <option value="">请选择</option>
                                <option value="D" selected>日</option>
                                <option value="W">周</option>
                                <option value="M">月</option>
                            </select>
                        </div>
                        <div class="form-group search-input">
                            开始时间： <input type="text" class="form-control start-date">
                            结束时间：<input type="text" class="form-control end-date">

                        </div>

                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                   <div id="UserRechargeDistribution" style="width:100%; height:400px;"></div>
                </div>
            </div>
        </div>
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户提现时间分布</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" id="formWithdrawReport">
                        <div class="form-group">
                            <select class="form-control search-category">
                                <option value="">请选择</option>
                                <option value="D" selected>日</option>
                                <option value="W">周</option>
                                <option value="M">月</option>
                            </select>
                        </div>
                        <div class="form-group search-input">
                            开始时间： <input type="text" class="form-control start-date">
                            结束时间：<input type="text" class="form-control end-date">

                        </div>

                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="userWithdrawDistribution" style="width:100%; height:400px;"></div>
                </div>
            </div>
        </div>
        <div class="col-lg-12 col-sm-12">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">用户账户余额时间分布</h3>
                </div>
                <div class="panel-body" id="">
                    <form class="form-inline" id="formUserAccountReport">
                        <div class="form-group">
                            <select class="form-control search-category">
                                <option value="">请选择</option>
                                <option value="D" selected>日</option>
                                <option value="W">周</option>
                                <option value="M">月</option>
                            </select>
                        </div>
                        <div class="form-group search-input">
                            开始时间： <input type="text" class="form-control start-date">
                            结束时间：<input type="text" class="form-control end-date">

                        </div>

                        <button class="btn btn-primary" type="button">查询</button>
                    </form>

                    <div id="userAccountDistribution" style="width:100%; height:400px;"></div>
                </div>
            </div>
        </div>
        </div>

</div>

</@global.main>