<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="referer-manage.js" headLab="user-manage" sideLab="referMan" title="推荐人管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">推荐人</label>
                <input type="text" class="form-control referrerLoginName" name="referrerLoginName"
                       value="${referrerLoginName!}">
            </div>
            <div class="form-group">
                <label for="control-label">投资人</label>
                <input type="text" class="form-control investLoginName" name="investLoginName"
                       value="${investLoginName!}">
            </div>
            <div class="form-group" id="investDate">
                <label for="control-label">投资时间</label>

                <div class='input-group date' id="investDateBegin">
                    <input type='text' class="form-control investStartTime"
                           value="${(investStartTime?string('yyyy-MM-dd'))!}"/>

					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id="investDateEnd">
                    <input type='text' class="form-control investEndTime"
                           value="${(investEndTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>

            <div class="form-group">
                <label for="control-label">推荐层级</label>
                <input type="text" class="form-control level" value="${(level?string('0'))!}">
            </div>
            <div class="form-group" id="">
                <label for="control-label">奖励时间</label>

                <div class='input-group date' id="RewardDateBegin">
                    <input type='text' class="form-control rewardStartTime"
                           value="${(rewardStartTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id="RewardDateEnd">
                    <input type='text' class="form-control rewardEndTime"
                           value="${(rewardEndTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>
            <div class="form-group">
                <label for="control-label">推荐人角色</label>
                <select class="selectpicker role" data-style="btn-default">
                    <option value="" <#if role?has_content><#else>selected</#if>>全部</option>
                    <option value="STAFF" <#if role?has_content && role == 'STAFF'>selected</#if>>业务员</option>
                    <option value="USER" <#if role?has_content && role != 'STAFF'>selected</#if>>用户(非业务员)</option>
                </select>
            </div>
            <div class="form-group">
                <label for="control-label">来源</label>
                <select class="selectpicker source" data-style="btn-default">
                    <option value="">全部</option>
                    <#list sources as sourceItem>
                        <option value="${sourceItem.name()}" <#if (source?has_content && source == sourceItem.name()) >selected</#if>>${sourceItem.name()}</option>
                    </#list>
                </select>
            </div>
            <button class="btn btn-primary search" type="button">查询</button>
            <button class="btn btn-default" type="reset">重置</button>
        </div>

    </form>

    <div class="row">
        <div class="title-list">
            <span>合计投资金额： ${investAmountSum/100} </span>
            <span>合计推荐奖励金额： ${rewardAmountSum/100} </span>
        </div>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>项目名称</th>
                <th>期数</th>
                <th>投资人</th>
                <th>投资人姓名</th>
                <th>投资金额(元)</th>
                <th>投资时间</th>
                <th>来源</th>
                <th>推荐人</th>
                <th>推荐人姓名</th>
                <th>推荐人是否业务员</th>
                <th>推荐层级</th>
                <th>推荐奖励</th>
                <th>奖励状态</th>
                <th>奖励时间</th>
            </tr>
            </thead>
            <tbody>
                <#list referrerManageViews as referrerManageView>
                <tr>
                    <td>${referrerManageView.loanName!}</td>
                    <td>${referrerManageView.periods?string('0')}</td>
                    <td>${referrerManageView.investLoginName!}</td>
                    <td>${referrerManageView.investName!}</td>
                    <td>${referrerManageView.investAmount/100}</td>
                    <td>${referrerManageView.investTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${referrerManageView.source}</td>
                    <td>${referrerManageView.referrerLoginName!}</td>
                    <td>${referrerManageView.referrerName!}</td>
                    <td><#if referrerManageView.role?? && referrerManageView.role == 'STAFF'>是<#else>否</#if></td>
                    <td>${referrerManageView.level?string('0')}</td>
                    <td>${referrerManageView.rewardAmount/100}</td>
                    <td><#if referrerManageView.status?? && referrerManageView.status == 'SUCCESS'>已入账<#else>
                        入账失败</#if></td>
                    <td>${referrerManageView.rewardTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${referrerManageCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage>
                    <a href="/user-manage/referrer?referrerLoginName=${referrerLoginName!}&investLoginName=${investLoginName!}&investStartTime=${(investStartTime?string('yyyy-MM-dd'))!}&investEndTime=${(investEndTime?string('yyyy-MM-dd'))!}&level=${(level?string('0'))!}&rewardStartTime=${(rewardStartTime?string('yyyy-MM-dd'))!}&rewardEndTime=${(rewardEndTime?string('yyyy-MM-dd'))!}&role=${role!}&source=${source!}&currentPageNo=${currentPageNo-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span>
                </a>
                </li>
                <li><a>${currentPageNo}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="/user-manage/referrer?referrerLoginName=${referrerLoginName!}&investLoginName=${investLoginName!}&investStartTime=${(investStartTime?string('yyyy-MM-dd'))!}&investEndTime=${(investEndTime?string('yyyy-MM-dd'))!}&level=${(level?string('0'))!}&rewardStartTime=${(rewardStartTime?string('yyyy-MM-dd'))!}&rewardEndTime=${(rewardEndTime?string('yyyy-MM-dd'))!}&role=${role!}&source=${source!}&currentPageNo=${currentPageNo+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span>
                </a>
                </li>
            </ul>
            <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>