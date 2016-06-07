<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="hero_ranking.js" headLab="activity-manage" sideLab="heroRanking" title="周年庆管理">

<div class="col-md-10" style="margin-bottom:50px;">
    <div class="col-md-12">
        <div class="btn btn-default invest active">投资榜单查看</div>
        <div class="btn btn-default referrer">推荐榜单查看</div>
        <div class="btn btn-default upload">上传神秘大奖</div>
    </div>
</div>
<div class="col-md-10" style="margin-bottom:20px;">
    <div class="col-md-2">
        <div class='input-group date' id='datepicker'>
            <input type='text' class="form-control" id = 'tradingTime' value="${tradingTime?string('yyyy-MM-dd')}"/>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
    <div class="col-md-10" style="line-height: 34px;">
        <strong>当日前十平均投资金额：${(avgInvestAmount/100)?string('0.00')}元</strong>
    </div>
</div>
<div class="col-md-10">
    <div class="table-responsive col-md-8 invest-ranking">
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
            <#list heroRankingViewInvestList as heroRankingViewInvest>
                <#assign varInvest = 0>
            <tr>
                <td>${varInvest+1}</td>
                <td>${heroRankingViewInvest.loginName!}</td>
                <td>${heroRankingViewInvest.mobile?string('0')}</td>
                <td>${heroRankingViewInvest.userName!}</td>
                <td>${(heroRankingViewInvest.sumAmount/100)?string('0.00')}</td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="table-responsive col-md-8 referrer-ranking" style="display: none">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>排名</th>
                <th>用户名</th>
                <th>手机号</th>
                <th>姓名</th>
                <th>今日推荐投资金额(元)</th>
            </tr>
            </thead>
            <tbody>
            <#list heroRankingViewReferrerList as heroRankingViewReferrer>
                <#assign varReferrer = 0>
            <tr>
                <td>${varReferrer+1}</td>
                <td>${heroRankingViewReferrer.loginName!}</td>
                <td>${heroRankingViewReferrer.mobile?string('0')}</td>
                <td>${heroRankingViewReferrer.userName!}</td>
                <td>${(heroRankingViewReferrer.sumAmount/100)?string('0.00')}</td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="mysteriousPrize upload-image" style="display: none">
        <form class="prize-form" action="/activity-manage/upload-image" method="post">
            <div class="form-group">
                <label class="col-sm-1 control-label">名称: </label>
                <div class="col-sm-4">
                    <input type="text" name="prizeName"  class="form-control prize-ame" value="<#if mysteriousPrizeDto??>${mysteriousPrizeDto.prizeName!}</#if>" placeholder="" datatype="*" errormsg="名称不能为空">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">图片: </label>
                <div class="col-sm-4 ">
                    <input type="text" name="imageUrl" value="<#if mysteriousPrizeDto??>${mysteriousPrizeDto.imageUrl!}</#if>" readonly class="form-control image-url" placeholder="" datatype="*" errormsg="图片不能为空">
                    <div class="thumbImage">
                        <#if mysteriousPrizeDto??>
                            <img style="width:100%" src="/${mysteriousPrizeDto.imageUrl!}" alt="神秘大奖缩略图">
                        </#if>
                    </div>
                </div>
                <div class="col-sm-4 prize-image">
                    <input type="file" name="prizeImage"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"></label>
                <div class="col-sm-4 form-error">
                </div>
             </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">操作: </label>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="col-sm-4">
                    <button type="button" class="btn jq-btn-form btn-primary prize-save">更新</button>
                </div>
            </div>
        </form>
    </div>

</div>

</@global.main>