<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.year_award_2017}" pageJavascript="${js.year_award_2017}" activeNav="" activeLeftNav="" title="年终奖活动_活动中心_拓天速贷" keywords="拓天速贷,年终奖,抽奖,岁末专享,实物奖励" description='拓天速贷年终奖活动,活动期间用户每日登录即可获得一次抽奖,投资"岁末专享"项目累计年化投资额可按对应比率获得返现奖励,每日24点计算当日新增投资排名,上榜者可获丰厚实物奖励.'>
<div class="banner"></div>
<div id="doubleElevenContainer">
    <div class="show_middle_box">
        <div class="title_sign title_sign1"></div>
        <div class="active_desc limitHeight">12月4日-12月31日活动期间，每日登录可获一次免费抽奖机会，用户当日所获抽奖机会仅限当日使用。</div>
        <div class="nine-lottery-group">
            <div id="drawLotteryAreaSub">
                <div class="lottery-unit lottery-unit-0" data-unit="0">
                    <div class="prize_jd prize_icon"></div>
                    <div class="prize_desc">200元京东E卡</div>
                </div>
                <div class="lottery-unit lottery-unit-1 active" data-unit="1">
                    <div class="prize_hb prize_icon"></div>
                    <div class="prize_desc">120元红包</div>
                </div>
                <div class="lottery-unit lottery-unit-2" data-unit="2">
                    <div class="prize_hb prize_icon"></div>
                    <div class="prize_desc">50元红包</div>
                </div>
                <div class="lottery-unit lottery-unit-7" data-unit="7">
                    <div class="prize_hb prize_icon"></div>
                    <div class="prize_desc">10元红包</div>
                </div>
                <div class="lottery-btn lottery-unit"></div>
                <div class="lottery-unit lottery-unit-3" data-unit="3">
                    <div class="prize_ty prize_icon"></div>
                    <div class="prize_desc">500元体验金</div>
                </div>
                <div class="lottery-unit lottery-unit-6" data-unit="6">
                    <div class="prize_hb prize_icon"></div>
                    <div class="prize_desc">100元红包</div>
                </div>
                <div class="lottery-unit lottery-unit-5" data-unit="5">
                    <div class="prize_hb prize_icon"></div>
                    <div class="prize_desc">20元红包</div>
                </div>
                <div class="lottery-unit lottery-unit-4" data-unit="4">
                    <div class="prize_hb prize_icon"></div>
                    <div class="prize_desc">200元红包</div>
                </div>
            </div>
        </div>
        <div id="stateContainerTip_b">
            <div id="stateContainerTip_m">
                <div class="lottery-right-group" id="lotteryList">
                    <h3>
                        <span class="active switchBtn" id="tabBtn_record">中奖记录</span>
                        <span class="switchBtn" id="tabBtn_mine">我的奖品</span>
                    </h3>
                    <div class="record-group record-list">
                        <ul class="record-item user-record my-gift" id="recordList">
                        <#--<li class="myReward_item"><div class="myReward_item_head">恭喜182****5693</div><div class="myReward_item_foot">抽中了100元红包</div></li>-->
                        </ul>
                        <div class="myRecordWrapper record-item" style="display: none">
                            <ul class="own-record" id="myRecord">
                            <#--<li class="myRecord_right_item"><div class="mineReward_name">200元红包</div><div class="mineReward_year">2017-11-13</div><div class="mineReward_year_hour"> 21:15:24</div></li>-->
                            </ul>
                        </div>
                    <#--<div class="pageNo_container" id="pageNo_container" style="display: none">-->
                    <#--<span class="prePage page-number" id="prePage">上一页</span>-->
                    <#--<span class="nextPage page-number" id="nextPage">下一页</span>-->
                    <#--</div>-->

                    </div>
                </div>
            </div>
        </div>
    </div>
<#--奖品提示-->
    <div class="tip-list-frame">
        <!--虚拟奖品的提示-->
        <div class="tip-list" id="test"  data-return="virtual">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text"></p>
                <p class="icon_prize"></p>
                <p class="reward-text"><em class="prizeValue"></em></p>
            </div>
            <div class="btn-list"><a href="/loan-list" class="go-on small-btn">去使用</a></div>
        </div>
        <div class="tip-list" id="test"  data-return="experience">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text"></p>
                <p class="icon_prize"></p>
                <p class="reward-text"><em class="prizeValue"></em></p>
            </div>
            <div class="btn-list"><a href="/loan-list" class="go-on small-btn">去使用</a></div>
        </div>
        <!--没有抽奖机会-->
        <div class="tip-list no-change-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text"></p>
                <p class="des-text"></p>
            </div>
            <div class="btn-list btn-invest go-close"><a>我知道啦</a></div>
        </div>
    </div>


    <div class="show_middle_box">
        <div class="title_sign title_sign2"></div>
        <b class="active_desc active_desc1 limitHeight">12月4日-12月31日活动期间，计算平台所有“岁末专享”项目累计年化投资金额，活动结束后进行结算，每达到一定金额，即对投资“岁末专享”项目的所有用户发放与其投资额对应比率的返现奖励。用户投资“岁末专享”项目即可参与本活动，最高可得自身专享标累计投资额的年化1%返现奖励。
            <br/><span class="strongText">注：每档返现奖励不累计发放。</span>
        </b>
        <div class="title_ammount">
            <div class="current_ammount">当前年化投资额：${sumAnnualizedAmount!}万元</div>
            <div class="my_ammount">我的奖励：<@global.isNotAnonymous>${rewards!}元</@global.isNotAnonymous><@global.isAnonymous><a class="myRank_info_No1 login_pop" style="color:#fff600">登录</a></@global.isAnonymous>
            </div>
        </div>
        <div class="conversion_chart">
            <div class="head_title">
                <div class="left_head_title"><span class="left_head_title1">平台 “岁末专享”</span><br/><span class="left_head_title2">项目累计投资额（年化）</span></div>
                <div class="right_head_title">返现比率（年化）</div>
            </div>
            <div class="account_item">
                <span class="accountNum">200万元</span>
                <span class="accountRate">0.2%</span>
            </div>
            <div class="account_item">
                <span class="accountNum">600万元</span>
                <span class="accountRate">0.4%</span>
            </div>
            <div class="account_item">
                <span class="accountNum">800万元</span>
                <span class="accountRate">0.6%</span>
            </div>
            <div class="account_item">
                <span class="accountNum">1500万元</span>
                <span class="accountRate">0.8%</span>
            </div>
            <div class="account_item">
                <span class="accountNum">2000万元</span>
                <span class="accountRate">1%</span>
            </div>
        </div>
        <div class="percent_chat">
            <div class="percent_chat_title">返现比率（年化）</div>
            <div class="percent_wrapper">
                <div class="percent_wrapper_sub" data-percent="${ratio!}"></div>
                <div class="scaleRate scaleRate1">
                    <div class="toHideIcon">
                        <div class="scaleRateIcon"></div>
                    </div>
                    <div class="scaleRateNo scaleRateNo1" style="right: 5%">0</div>
                </div>
                <div class="scaleRate scaleRate2">
                    <div class="toHideIcon">
                        <div class="scaleRateIcon"></div>
                    </div>
                    <div class="scaleRateNo">0.2%</div>
                </div>
                <div class="scaleRate scaleRate3">
                    <div class="toHideIcon">
                        <div class="scaleRateIcon"></div>
                    </div>
                    <div class="scaleRateNo">0.4%</div>
                </div>
                <div class="scaleRate scaleRate4">
                    <div class="toHideIcon">
                        <div class="scaleRateIcon"></div>
                    </div>
                    <div class="scaleRateNo">0.6%</div>
                </div>
                <div class="scaleRate scaleRate5">
                    <div class="toHideIcon">
                        <div class="scaleRateIcon"></div>
                    </div>
                    <div class="scaleRateNo">0.8%</div>
                </div>
                <div class="scaleRate scaleRate6">
                    <div class="toHideIcon">
                        <div class="scaleRateIcon"></div>
                    </div>
                    <div class="scaleRateNo scaleRateNo6">1%</div>
                </div>
            </div>
        </div>
        <b class="for_example_desc limitHeight">
            <span class="for_example_desc_icon">举个栗子：</span>拓小天投资180天“岁末专享”项目10万元，360天“岁末专享”项目15万元，则拓小天投资年化金额=10万×180÷360+15万×360÷360=20万元，如活动结束后平台“岁末专享”项目累计投资额（年化）满2000万，返现比例1%，则拓小天可获返现奖励为20万×1%=2000元。
        </b>
    </div>
    <div class="show_middle_box">
        <div class="title_sign title_sign3"></div>
        <b class="active_desc limitHeight">12月4日-12月12日活动期间，每日24点计算当日新增投资排名，上榜者可获丰厚奖励，投资者在当日24点之前进行的多次投资，金额可累计计算。</b>
        <div class="prize_list_wrapper">
            <div class="left_prize_wrapper">
                <div class="prize_icon1"></div>
                <div class="prize_icon_desc prize_icon_desc1"><span class="prize_icon_desc_head">2~3名</span> <span class="prize_icon_desc_foot">0.5%加息券</span></div>
            </div>
            <div class="middle_prize_wrapper">
                <div class="prize_icon2" data-awardsrc="<#if prizeDto??>${commonStaticServer}${prizeDto.goldImageUrl}</#if>"></div>
                <div class="prize_icon_desc"><span class="prize_icon_desc_head">今日大奖</span> <span class="prize_icon_desc_foot"><#if prizeDto??>${prizeDto.goldPrizeName}<#else>实物大奖</#if></span></div>
            </div>
            <div class="right_prize_wrapper">
                <div class="prize_icon3"></div>
                <div class="prize_icon_desc prize_icon_desc3"><span class="prize_icon_desc_head">4~10名</span> <span class="prize_icon_desc_foot">0.2%加息券</span></div>
            </div>
        </div>
        <div class="rank_list_survey" id="rank_list_survey">
            <span class="date_info">日期：<span class="date_info_No" data-starttime="${activityStartTime!}" data-endtime="${activityEndTime!}">${currentTime?string('yyyy-MM-dd')}</span></span>
            <@global.isNotAnonymous>
                <span class="myRank_info">我的排名：<span class="myRank_info_No">${investRanking!}</span></span>
            </@global.isNotAnonymous>
            <@global.isAnonymous>
                <span class="myRank_info">我的排名：<a class="myRank_info_No1 login_pop">登录</a></span>
            </@global.isAnonymous>
            <@global.isNotAnonymous>
                <span class="today_totalAccount"><span class="is-today">当日</span>累计投资：<span class="today_totalAccountNo">${(investAmount/100)?string('0.00')}元</span></span>
            </@global.isNotAnonymous>
            <@global.isAnonymous>
                <span class="today_totalAccount">当日累计投资：<a class="myRank_info_No2 login_pop" style="color: #fff600">登录</a></span>
            </@global.isAnonymous>
        </div>
        <div class="nodata-invest tc" style="display: none;"></div>
        <div id="tableListWrapper">
            <table class="table-reward">
                <thead class="thead">
                <tr>
                    <th width="20%">排名</th>
                    <th width="25%">用户</th>
                    <th width="25%">投资额(元)</th>
                    <th >奖励</th>
                </tr>
                </thead>
                <tbody id="investRanking-tbody"></tbody>
            </table>
        </div>
        <div class="show_data_btn">
            <div class="show_listData" id="show_more_listData" style="display: none">显示更多<span class="show_more_icon"></span></div>
            <div class="show_listData" id="show_less_listData" style="display: none">收起<span class="show_less_icon"></span></div>
        </div>

        <div class="date-button" id="investRanking-button">
            <span class="button-small button-small-pre" id="heroPre">查看前一天</span>
            <span class="btn-to-invest" id="toInvest">立即投资抢占排行榜</span>
            <span class="button-small button-small-next" id="heroNext">查看后一天</span>
        </div>
    </div>

    <div class="show_middle_box">
        <div class="note-box limitHeight">
            <b class="note-box-title"><span class="square_icon"></span><span class="square_icon"></span><span class="square_icon"></span>温馨提示<span class="square_icon square_icon1"></span><span class="square_icon"></span><span class="square_icon"></span></b>
            <b class="textLeft"><span class="No_icon">1、</span> 抽奖活动所获红包及体验金奖励即时发放，实物奖品将于活动结束后七个工作日内统一安排发放；</b>
            <b class="textLeft"><span class="No_icon">2、</span> 返现奖励将于活动结束后1个工作日内统一发放至用户账户，可直接用于提现或继续投资；</b>
            <b class="textLeft"><span class="No_icon">3、</span> 排行榜活动仅限所有直投项目，债权转让及拓天体验金项目不参与累计；每日投资排行榜排名将在活动页面实时更新，排行榜中奖人数最多10名，如遇金额一致，则当日先达到该投资额的用户优先获奖，其他用户名次顺延；</b>
            <b class="textLeft"><span class="No_icon">4、</span> 排行榜活动加息券奖励将于获奖后三个工作日内发放，实物奖品将于活动结束后七个工作日内统一安排发放；</b>
            <b class="textLeft"><span class="No_icon">5、</span> 拓天速贷会根据活动的情况，以等值、增值为基础调整奖品类型；</b>
            <b class="textLeft"><span class="No_icon">6、</span> 为了保证获奖结果的公平性，排行榜活动中实物大奖获奖用户在活动期间所进行的所有投标不允许进行债权转让，否则奖品将不予发放；</b>
            <b class="textLeft"><span class="No_icon">7、</span> 活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</b>
            <b class="textLeft"><span class="No_icon">8、</span> 活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</b>
        </div>
    </div>
</div>
    <#include "../../module/login-tip.ftl" />

<script type="text/template" id="tplTable">
    <% for(var i = 0; i < records.length; i++) {
    var item = records[i];
    var reward;
    if(i==0) {
    reward='实物大奖';
    }
    else if(i>0 && i<3) {
    reward='0.5%加息券';
    }
    else {
    reward='0.2%加息券';
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

</@global.main>
