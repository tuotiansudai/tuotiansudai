<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.hero_ranking}" pageJavascript="${js.hero_ranking}" activeNav="" activeLeftNav="" title="周年庆活动_拓天活动_拓天速贷" keywords="拓天速贷,拓天活动.生日活动,生日月特权" description="拓天速贷专属生日月特权,生日月投资收益翻倍,拓天速贷专属活动超高收益等你拿.">
<div class="anniversary-container">
    <div class="container clearfix">
        <img src="${commonStaticServer}/activity/images/sign/actor/anniver/top.jpg" width="100%" class="responsive-pc">
        <img src="${commonStaticServer}/activity/images/app-banner/anniversary-mobile.jpg" width="100%" class="responsive-phone">
        <div class="wp actor-bg clearfix">
            <div class="step-section-one">
                <div class="box-square-out">
                    <div class="square-title"><img src="${commonStaticServer}/activity/images/sign/actor/anniver/head-title01.png" alt="投资抢排行，赢取每日神秘大奖"></div>
                    <p class="description clearfix">活动期间，每日根据用户当日的累计投资金额进行排名。第一名可得当日神秘大奖，前十名获得丰厚奖励</p>
                    <div class="clearfix">
                        <div class="fl tc">
                            <div class="dotted-normal-box">
                                <h2>今日大奖</h2>
                                <img src="${commonStaticServer}/activity/images/sign/actor/anniver/reward-07-31.jpg" alt="神秘大奖" class="mysterious-img">
                                <span class="reward-name"><em><#if mysteriousPrizeDto??>${mysteriousPrizeDto.prizeName}</#if></em></span>
                            </div>
                            <div class="tc">
                                <button href="/loan-list?productType=JYF" class="get-rank button-big" target="_blank" disabled>活动已结束</button>
                            </div>
                        </div>
                        <div class="fr">
                            <div class="dotted-normal-box">
                                <div class="date-head">
                                    <i class="date-icon"></i>
                                    <span class="date-text " id="TodayAwards"><#if currentTime??>${currentTime?string('yyyy-MM-dd')}</#if></span>
                                   <span class="my-order fr">
                                       <@global.isAnonymous>
                                           我的排名：<a href="/login" target="_blank" class="get-rank">登录</a>
                                       </@global.isAnonymous>
                                       <@global.isNotAnonymous>
                                           <#if investRanking??&&investRanking gt 0>
                                               我的排名:&nbsp;<#if investRanking gte 20>20+ <#else >${investRanking} </#if>
                                           <#else >
                                               未参与排行
                                           </#if>
                                       </@global.isNotAnonymous>
                                </span>
                                </div>
                                <div class="nodata-invest tc" style="display: none;">活动未结束</div>
                                <table class="table-sort-border" style="display: none;">
                                    <caption><span id="HistoryAwards"><em>2016-07-31</em></span>投资排行</caption>
                                    <thead>
                                    <tr>
                                        <th>排名</th>
                                        <th>用户名</th>
                                        <th>投资金额（元）</th>
                                        <th>奖励</th>
                                    </tr>
                                    </thead>
                                    <tbody id="investRanking-tbody">
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <td colspan="4">

                                        </td>
                                    </tr>
                                    </tfoot>
                                </table>

                                <script type="text/template" id="tplTable">
                                    <% for(var i = 0; i < records.length; i++) { %>
                                    <% var item = records[i];
                                    var reward;
                                    if(type=='invest') {
                                    if(i==0) {
                                    reward='神秘大奖＋1%加息券';
                                    }
                                    else if(i>0 && i<5) {
                                    reward='200元红包＋1%加息券';
                                    }
                                    else {
                                    reward='100元红包＋1%加息券';
                                    }
                                    }
                                    else if(type=='referrer') {
                                    if(i==0) {
                                    reward='100元红包';
                                    }
                                    else if(i==1) {
                                    reward='50元红包';
                                    }
                                    else if(i==2) {
                                    reward='30元红包';
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

                            </div>
                            <div class="date-button" id="investRanking-button">
                                <span class="button-small fl" id="heroPre">前一天</span>
                                <span class="button-small fr" id="heroNext">后一天</span>
                            </div>

                        </div>
                    </div>

                </div>
            </div>

            <div class="step-section-two clearfix">
                <div class="step-two-title">
                    <img src="${commonStaticServer}/activity/images/sign/actor/anniver/step02.png">
                </div>
                <div class="box-square-out">
                    <div class="square-title"><img src="${commonStaticServer}/activity/images/sign/actor/anniver/head-title02.png" alt="投资抢排行，赢取每日神秘大奖"></div>
                    <p class="description clearfix">推荐好友投资，拿1%奖金。活动期间，每日直接推荐进行投资的前三名，还可获得投资红包奖励！</p>
                    <div class="clearfix">
                        <div class="fl">
                            <div class="dotted-normal-box">
                                <div class="date-head">
                                    <i class="date-icon"></i>
                                    <span class="date-text"><#if currentTime??>${currentTime?string('yyyy-MM-dd')}</#if></span>
                           <span class="my-order fr">
                               <@global.isAnonymous>
                                   我的排名：<a href="/login" target="_blank" class="get-rank">登录</a>
                               </@global.isAnonymous>
                               <@global.isNotAnonymous>
                                   <#if referRanking??&&referRanking gt 0>
                                       我的排名:&nbsp;<#if referRanking gte 20>20+ <#else >${referRanking} </#if>
                                   <#else >
                                       未参与排行
                                   </#if>
                               </@global.isNotAnonymous>
                           </span>
                                </div>

                                <div class="nodata-refer tc" style="display: none;">活动已结束</div>
                                <table class="table-sort-border" style="display: none;">
                                    <caption><span id="ReferRankingDate"><em>2016-07-31</em></span>推荐排行</caption>
                                    <thead>
                                    <tr>
                                        <th>排名</th>
                                        <th>用户名</th>
                                        <th>推荐投资金额（元）</th>
                                        <th>奖励</th>
                                    </tr>
                                    </thead>
                                    <tbody id="referRanking-tbody">

                                    </tbody>
                                </table>
                            </div>
                            <div class="date-button" id="referRanking-button">
                                <span class="button-small fl" id="referPre">前一天</span>
                                <span class="button-small fr" id="referNext">后一天</span>
                            </div>
                        </div>
                        <div class="fr tc">
                            <div class="dotted-normal-box">
                                <img src="${commonStaticServer}/activity/images/sign/actor/anniver/reward-img.png">
                            </div>
                        </div>

                    </div>
                    <div class="tc clearfix">
                        <button class="button-big" href="/referrer/refer-list" target="_blank" disabled>活动已结束</button>
                    </div>
                </div>
            </div>
            <div class="step-section-three">
                <div class="step-two-title">
                    <img src="${commonStaticServer}/activity/images/sign/actor/anniver/step03.png">
                </div>
                <div class="box-square-out">
                    <div class="square-title"><img src="${commonStaticServer}/activity/images/sign/actor/anniver/head-title03.png" alt="投资抢排行，赢取每日神秘大奖"></div>
                    <p class="description clearfix">活动期间，所有新注册认证用户以及所有投资满1000元用户均可免费领取V5会员</p>
                    <div class="clearfix">
                        <div class="fl">
                            <div class="dotted-normal-box">
                                <img src="${commonStaticServer}/activity/images/sign/actor/anniver/zounian01.png">
                            </div>
                        </div>
                        <div class="fr">
                            <div class="dotted-normal-box">
                                <img src="${commonStaticServer}/activity/images/sign/actor/anniver/zounian02.png">
                            </div>
                        </div>
                    </div>
                    <div class="tc">
                        <a class="button-big get-vip" id="getVip" href="javascript:void(0)">
                            免费领取 <br/>
                            价值25元V5会员</a>
                    </div>
                </div>

            </div>

            <div class="step-section-four">
                <div class="box-square-out">
                    <div class="square-title"><img src="${commonStaticServer}/activity/images/sign/actor/anniver/head-title03.png" alt="投资抢排行，赢取每日神秘大奖"></div>

                    <div class="dotted-normal-box tl">
                        1. 每天24点将计算当日新增投资及新增直接推荐投资的排名，投资者在当日24点之前进行的多次投资，金额可累计计算；投资英雄榜仅限于直投项目，其余投资不计入今日投资金额中; <br/>
                        2. 投资英雄榜中奖人数最多十名,推荐英雄榜最多三名，如遇金额一致则并列获奖，下一奖项名额顺延缩减；<br/>
                        3. 每日英雄榜排名将在活动页面实时更新。中奖结果将于次日由客服联系确认，红包和加息券在中奖次日发放，实物奖品将于月底活动结束后七个工作日内统一安排发放；<br/>
                        4. 特别提示：活动期间一旦提交债权转让申请，则不可在活动期间继续参与投资英雄榜。即使发起债权转让申请的当天，累计投资额已入围当日前10名，也不可参与当日投资英雄榜排名及获取奖励；<br/>
                        5. 拓天速贷会根据活动的情况，以等值，增值为基础调整奖品类型；<br/>
                        6. 本活动最终解释权归拓天速贷所有。
                    </div>
                </div>

            </div>
        </div>
    </div>

    <div class="tip-vip-model" id="vipTipModel"></div>
    <script type="text/html" id="vipTipModelTpl">

        <p class="des-text"><%=data.description%></p>
        <%  var content;
        if(data.url=='')  {
        content='<a href="javascript:void(0)" class="btn" id="closeTip">返回</a>';
        }
        else {

        content='<a href="'+data.url+'" class="btn">'+data.btnName+'</a>';
        }
        %>
        <p class="btn-text"><%=content%></p>
    </script>

</div>
</@global.main>