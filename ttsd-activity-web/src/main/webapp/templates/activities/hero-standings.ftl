<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.hero_standings}" pageJavascript="${js.hero_standings}" activeNav="" activeLeftNav="" title="拓天英豪榜_英豪榜活动_拓天速贷" keywords="拓天英豪榜,英豪榜活动,投资排行榜,拓天速贷" description="拓天速贷投资排行榜活动,狂欢大轰趴,连嗨三周半,打响排行保卫战,拓天英豪榜投资活动每日神秘大奖悬赏最具实力的投资人.">
<div class="hero-standings-container">
    <div class="top-intro-img">
        <img src="${commonStaticServer}/activity/images/hero-standings/top-intro.png" alt="英豪榜" width="100%" class="top-img">
        <img src="${commonStaticServer}/activity/images/hero-standings/top-intro-phone.png" alt="英豪榜" width="100%" class="top-img-phone">
    </div>
    <div class="hero-content-group">
        <div class="wp clearfix">
            <div class="reg-tag-current" style="display: none">
                <#include '../module/register.ftl' />
            </div>
            <div class="actor-intro-group">
                <i class="left-bubble animate-twink"></i>
                <i class="right-bubble animate-twink"></i>
                <p class="pc-item">为庆祝拓天速贷累计交易额<span> 突破1亿 </span>平台举办狂欢大轰趴，<span> 连嗨三周半 </span></p>
                <p class="pc-item">活动期间，每日进行投资<span> 英豪榜排名上榜者 </span>可获丰厚奖励，第一名更能夺得<span> 神秘实物大奖 </span>！</p>
                <p class="phone-item">
                    为庆祝拓天速贷累计交易额<span>突破1亿</span><br />平台举办狂欢大轰趴，<span>连嗨三周半</span><br />活动期间，每日进行投资<br /><span>英豪榜排名上榜者</span>可获丰厚奖励，<br />第一名更能夺得<span>神秘实物大奖</span>！
                </p>
            </div>
            <div class="rank-number-group">
                <div class="date-time-item"><#if currentTime??>${currentTime?string('yyyy-MM-dd')}</#if></div>
                <div class="my-rank-item">
                    <@global.isAnonymous>我的排名：登录后查看</@global.isAnonymous>
                    <@global.isNotAnonymous><#if investRanking &gt; 20 || investRanking == 0>未参加排行<#else>我的排名：${investRanking}</#if></@global.isNotAnonymous>
                </div>
                <@global.isAnonymous>
                    <div class="login-item">
                        <a href="javascript:void(0)" class="login-btn show-login">登录查看我的排名</a>
                    </div>
                </@global.isAnonymous>

                <div class="today-item">
                    <@global.isAnonymous></@global.isAnonymous>
                    <@global.isNotAnonymous>我的今日投资额：${investAmount}元</@global.isNotAnonymous>
                </div>
            </div>
            <div class="gift-group">
                <div class="gift-left-item">
                    <div class="left-bubble animate-bounce-up">
                        <div class="min-gift-picture">
                            <img src="${commonStaticServer}/activity/images/hero-standings/card-one.png">
                        </div>
                    </div>
                    <div class="text-des-item">
                        第二名可得<br />200元京东卡
                    </div>
                </div>
                <div class="gift-center-item">
                    <div class="center-bubble animate-bounce-up">
                        <div class="max-gift-picture">
                            <h3><#if mysteriousPrizeDto??>${mysteriousPrizeDto.prizeName}</#if></h3>
                            <img src="<#if mysteriousPrizeDto??>${commonStaticServer}${mysteriousPrizeDto.imageUrl}</#if>">
                        </div>
                    </div>
                    <div class="text-des-item max-text">
                        今日神秘大奖
                    </div>
                    <div class="text-tip-item">
                        第一名可得
                    </div>
                </div>
                <div class="gift-right-item">
                    <div class="right-bubble animate-bounce-up">
                        <div class="min-gift-picture">
                            <img src="${commonStaticServer}/activity/images/hero-standings/card-two.png">
                        </div>
                    </div>
                    <div class="text-des-item">
                        第三名可得<br />100元京东卡
                    </div>
                </div>
            </div>
            <div class="rank-list-group">
                <i class="left-bubble animate-twink"></i>
                <i class="right-bubble animate-twinking"></i>
                <div class="rank-table-item">
                    <h3><span id="historyTime"><#if currentTime??>${currentTime?string('yyyy-MM-dd')}</#if></span>投资英豪榜</h3>
                    <div class="table-list-item">
                        <div class="table-content">
                            <table>
                                <thead>
                                    <tr>
                                        <th>排名</th>
                                        <th>用户</th>
                                        <th>投资额</th>
                                        <th>奖励</th>
                                    </tr>
                                </thead>
                                <input type="hidden" value="<#if currentTime??>${currentTime?string('yyyy-MM-dd')}</#if>" id="TodayAwards">
                                <input type="hidden" value="${activityStartTime}" id="startTime">
                                <input type="hidden" value="${activityEndTime}" id="endTime">
                                <tbody id="investRanking-tbody">
                                </tbody>
                            </table>
                            <script type="text/template" id="tplTable">
                                <% for(var i = 0; i < records.length; i++) { %>
                                <% var item = records[i];
                                var reward;
                                if(type=='invest') {
                                    if(i==0) {
                                        reward='神秘大奖';
                                    }
                                    else if(i>0 && i<2) {
                                        reward='200元京东卡';
                                    }
                                    else if(i>1 && i<3){
                                        reward='100元京东卡';
                                    }else{
                                        reward='100元红包';
                                    }
                                }

                                %>
                                <tr>
                                    <td><%=i+1%></td>
                                    <td><%=item.loginName%></td>
                                    <td><%=item.centSumAmount%></td>
                                    <td><%=reward%></td>
                                </tr>
                                <% } %>
                            
                                

                            </script>
                            <ul class="table-bg-item" id="bgItem">

                            </ul>
                            <script type="text/template" id="bgtplTable">
                                <li></li>
                                <% for(var i = 0; i < records.length; i++) { %>
                                <li>
                                </li>
                                <% } %>
                                

                            </script>
                            <div class="change-btn-item" id="investRanking-button">
                                <div class="change-btn prev-btn" id="heroPre">前一天</div>
                                <div class="change-btn next-btn" id="heroNext">后一天</div>
                            </div>
                        </div>
                        <div class="get-rank-item">
                            <#if activityStatus == "false">
                                <a href="javascript:void(0)" class="disabled">立即投资抢排行</a>
                            <#else>
                                <a href="/loan-list">立即投资抢排行</a>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
            <div class="rule-list-group">
                <dl class="rule-item">
                    <dt>温馨提示：</dt>
                    <dd>1、活动期间，每天24点计算当日新增投资排名，投资者在当日24点之前进行的多次投资，金额可累计计算；英豪榜仅限于直投项目，其余投资不计入今日投资金额中；</dd>
                    <dd>2、英豪榜中奖人数最多10名，如遇金额一致，则当日先达到该投资额的用户优先获奖，其他用户名次顺延；</dd>
                    <dd>3、每日英豪榜排名将在活动页面实时更新。中奖结果将于次日由客服联系确认，红包在中奖后三个工作日内发放，实物奖品将于活动结束后七个工作日内统一安排发放；</dd>
                    <dd>4、活动期间一旦提交债权转让申请，则不可在活动期间继续参与英豪榜，即使发起债权转让申请的当天，累计投资额已入围当日前10名，也不可参与当日英豪榜排名及获取奖励；</dd>
                    <dd>5、拓天速贷会根据活动的情况，以等值、增值为基础调整奖品类型；</dd>
                    <dd>6、拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
                </dl>
            </div>
        </div>
    </div>
</div>
<#include "login-tip.ftl" />
</@global.main>