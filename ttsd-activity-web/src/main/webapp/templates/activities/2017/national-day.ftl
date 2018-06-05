<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.mid_national_2017}" pageJavascript="${js.mid_national_2017}" activeNav="" activeLeftNav="" title="中秋遇上国庆_拓天周国庆节_活动中心_拓天速贷" keywords="拓天速贷,国庆节,实物大奖,加息券" description="拓天周年庆-英雄排位场活动,每天24点计算当日新增投资排名,上榜者可获得实物大奖及加息券奖励,奖励丰厚礼物多多.">

    <div class="header compliance-banner">
        <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
    </div>
<div  id="activityPageFrame">
    <!--第一部分 begin-->
    <div class="mid-tit mid-tit1">
        <h2></h2>
        <div class="mid-con mid-con1">
            <div class="house-mortgage page-width">
                <div class="pos left_top"></div>
                <div class="pos right_top"></div>
                <div class="pos left_bottom"></div>
                <div class="pos right_bottom"></div>
                <div class="hourse-con">
                    <a href="javascript:;" class="toInvest invest"></a>
                </div>
                <div class="red-ware clearfix">
                    <div class="red-ware-info fl ">
                        <div class="top">
                            <div class="title">
                                <h2>投资逢万返百</h2>
                                <p>活动期间，投资带有“逢万返百”标签的项目，每投资满1万元，即返100元现金，如投资 10万元则返1000元现金，以此类推。 </p>
                            </div>
                        </div>
                        <div class="bottom"></div>
                        <div class="ware">

                        </div>
                    </div>

                    <div class="red-ware-info fr">
                        <div class="top">
                            <div class="title">
                                <h2>最高万元返现</h2>
                                <p>每个用户在活动期间最高可返1万元现金奖励（投资100万），如投资达100万以上则不再累计现金奖励；<br/></p>
                            </div>
                        </div>
                        <div class="bottom"></div>
                        <div class="ware">

                        </div>
                    </div>
                </div>
                <div class="tip">
                    <p>注：在同一项目中不满1万的投资额自动舍去，不可与其他项目投资额累计。</p>
                </div>
                <div class="invest">
                    <a href="javascript:;" class="toInvest investYellow invest">立即投资</a>
                </div>

            </div>
        </div>
    </div>
    <!--第一部分 end-->
    <!--第二部分 begin-->
    <div class="mid-tit mid-tit2">
        <h2></h2>
        <div class="mid-con mid-con2">
            <div class="house-mortgage page-width">
                <h2>活动期间，投资带有 <strong> “加息6.8%”</strong>标签的项目，可享受该笔投资资金的首期加息6.8%。</h2>
                <div class="pos left_top"></div>
                <div class="pos right_top"></div>
                <div class="pos left_bottom"></div>
                <div class="pos right_bottom"></div>
                <div class="hourse-con">
                    <a href="javascript:;" class="invest"></a>
                </div>

                <div class="invest">
                    <a href="javascript:;" class="toInvest insRed invest">立即投资</a>
                </div>

            </div>
        </div>
    </div>
    <!--第二部分 end-->
    <!--大奖公布 begin-->
    <div class="lottery_warp">
        <h2></h2>
        <div class="page-width">

            <div class="lottery">
                <h4>活动时间：10月1日-10月8日</h4>
                <p>活动期间，每天24点计算当日新增投资排名，<strong>上榜者可获丰厚奖励</strong>，投资者在当日24点之前进行的多次投资，金额可累计计算。</p>
                <div class="lottery-pai clearfix">
                    <div class="lottery_ware fl">
                        <div class="img"></div>
                        <p>2~3名 </p><p>300元红包</p>
                    </div>
                    <#if prizeDto??>
                    <div class="big-lottery" id="bigLottery">
                        <div class="img">
                            <div class="big-lottery-con">
                                <img src="${commonStaticServer}${prizeDto.goldImageUrl}" alt="">
                            </div>
                        </div>

                        <p>今日大奖</p> <p>${prizeDto.goldPrizeName}</p>

                    </div>
                    <#else>
                        <div class="big-lottery">
                            <div class="img">
                                <div class="big-lottery-con">

                                </div>
                            </div>
                            <p>今日大奖</p> <p>实物大奖</p>
                        </div>
                    </#if>
                    <div class="lottery_ware fr lottery_ware_fr">
                        <div class="img"></div>
                        <p>4~10名 </p><p>168元红包</p>
                    </div>
                </div>
                <!--英雄排行榜 bg-->
                <div class="ranking-list">
                    <div class="wrap">
                        <h2></h2>
                        <div class="title clearfix" id="sortBox">
                            <div class="fl">日期：<i class="date media-style" data-starttime="${activityStartTime!}" data-endtime="${activityEndTime!}"> ${currentTime?string('yyyy-MM-dd')}</i></div>
                            <div class="line"></div>
                            <div class="fl">我的排名:<@global.isAnonymous>
                                 <a href="javascript:void(0);" target="_blank" class="get-rank media-style">登录</a>
                            </@global.isAnonymous>

                            <@global.isNotAnonymous>
                                <i class="ranking-order media-style">${investRanking}</i>
                            </@global.isNotAnonymous></div>
                            <div class="line"></div>

                            <div class="last fr">当日累计投资：
                            <@global.isAnonymous>
                                <a href="javascript:void(0);" target="_blank" class="get-rank media-style">登录</a>
                            </@global.isAnonymous>
                            <@global.isNotAnonymous>
                                <span class="media-style total">${(investAmount/100)?string('0.00')}元</span>
                            </@global.isNotAnonymous></div>

                        </div>
                        <div class="nodata-invest tc" style="display: none;"></div>
                        <div id="investRanking-tbody">

                        </div>

                        <div class="tab-footer clearfix" id="investRanking-button">
                            <a class="pre-day button-small" id="rankingPre">查看前一天</a>
                            <a class="invest-button toInvest invest">立即投资抢排行</a>
                            <a class="next-day button-small" id="rankingNext">查看后一天</a>
                        </div>
                    </div>
                </div>
                <!--英雄排行榜 ed-->

            </div>
        </div>
        <!--温馨提示 begin-->
        <div class="ranking-er page-width">
            <h4>温馨提示：</h4>
            <p>1. 活动一仅限带有 “逢万返百” 标签的项目，活动二仅限带有 “加息6.8%” 标签的项目，请用户投资时看准项目标签，没有标签的项目不参与该两项活动；<br/>
                2.“逢万返百” 活动不可与平台其他优惠券同享；<br/>
            3.参与 “加息6.8%” 活动时，请用户投标时在 “优惠券” 一栏中手动点选 “6.8%加息券” ，如未点选视为自动放弃该奖励；<br/>
            4.现金奖励将于项目放款后1个工作日内发放到用户账户，用户可直接提现，也可用于投资其他项目；<br/>
            5.加息6.8%所得收益，将于项目首期回款后与收益一同转至用户账户；<br/>
            6. 排行榜活动仅限所有直投项目，债权转让及拓天体验金项目不参与累计；<br/>
            7.为方便您分散投资，排行榜中的红包奖励将以红包组的形式发放到您的账户，红包在活动结束后三个工作日内统一发放，实物奖品将于活动结束后七个工作日内统一安排发放；<br/>
            8.每日投资排行榜排名将在活动页面实时更新，排行榜中奖人数最多10名，如遇金额一致，则当日先达到该投资额的用户优先获奖，其他用户名次顺延；<br/>
            9.拓天速贷会根据活动的情况，以等值、增值为基础调整奖品类型；<br/>
            10.为了保证获奖结果的公平性，排行榜活动中实物大奖获奖用户在活动期间所进行的所有投标不允许进行债权转让，否则奖品将不予发放；<br/>
            11.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；<br/>
            12.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。<br/>

        </div>
        <!--温馨提示 end-->
    </div>
</div>
    <#include "../../module/login-tip.ftl" />
<script type="text/template" id="tplTable">
    <table>

    <thead id="headHide">
    <th>排名</th> <th>用户</th> <th class="investTh">投资额（元）</th> <th class="last">奖励</th>
    </thead>
    <tbody >
    <% for(var i = 0; i < records.length; i++) {
    var item = records[i];
    var reward;
    if(i==0) {
    reward='实物大奖';
    }
    else if(i>0 && i<3) {
    reward='300元红包（组）';
    }
    else {
    reward='168元红包（组）';
    }
    %>
    <tr>
        <td class="ranking-number"><%=i+1%></td>
        <td><%=item.loginName%></td>
        <td class="three"><%=item.centSumAmount%></td>
        <td class="last"><%=reward%></td>
    </tr>
    <% } %>

    </tbody>
    </table>

</script>

</@global.main>
