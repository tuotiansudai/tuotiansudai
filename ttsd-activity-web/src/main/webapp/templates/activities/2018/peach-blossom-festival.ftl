<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.peach_blossom_festival_2018}" pageJavascript="${js.peach_blossom_festival_2018}" activeNav="" activeLeftNav="" title="新年加息活动_活动中心_拓天速贷" keywords="拓天速贷,加息券,秒杀标,实物奖励" description='拓天速贷新年加息活动,活动期间微信关注"拓天速贷服务号"即可领取8张加息券,投资秒杀标可获得在原收益基础上增加1%的福利,累计投资1万元以上即可获得实物奖励.'>
<div class="spring-banner">
</div>
<div class="activity-wrap-main" id="peach_blossom">
    <div class="activity-page-frame " id="activityPageFrame">
     <div class="part-wrap part-one-wrap">
         <div class="part-bg part-one-bg">
             <div class="part-title part-one-title">
                 <h2>春风十里，扔掉枸杞</h2>
             </div>
             <div class="part-desc part-one-desc">
                 <p>
                     活动期间，微信扫描下方二维码关注<strong>“拓天速贷服务号”</strong>关注公众号，
                     <span class="mobile-style">回复口令<strong>“扔掉枸杞有惊喜”</strong>， 即可领取280元<strong>“春风投资大礼包”</strong>，每人限领1次。</span>
                 </p>
             </div>
             <div class="part-img qr-code">

             </div>
             <span class="left-leaf">

             </span>
             <span class="right-leaf">

             </span>
         </div>

     </div>
        <div class="part-one-bot">

        </div>

        <div class="part-wrap part-two-wrap">
        <div class="part-bg part-two-bg">
            <div class="part-title part-two-title">
                <h2>和远方有个约会，假日投资有礼</h2>
            </div>
            <div class="part-desc part-two-desc">
                <p>
                    活动期间，每逢<strong>国家法定节假日</strong>（4月5日、6日、7日、29日、30日、5月1日）或<strong>周末</strong>
                    <span class="mobile-style">（4月1日、14日、15日、21日、22日），平台将发放带有<strong>“0.6%返现”</strong>标签的项目，</span>
                    <span class="mobile-style">用户投资该标签项目，可享受<strong>年化</strong>投资额的<strong>0.6%返现奖励</strong>。</span>
                </p>
            </div>
            <div class="part-img calendar">

            </div>
            <div class="example">
                <p>
                    <span class="exam-title">举个栗子：</span>拓小天成功投资带有“0.6%返现”标签的项目，其中360天项目10万元，180天项目6万元，<span class="mobile-style">
                    则拓小天在该两个项目放款后，共计可获得返现奖励10万*0.6%+6万*180/360*0.6%=780元。</span>
                </p>
            </div>

            <span class="left-leaf">

             </span>
            <span class="right-leaf">

             </span>
            <span class="branch left-top-branch"></span>
            <span class="branch right-top-branch"></span>
            <span class="branch left-bot-branch"></span>
            <span class="branch right-bot-branch"></span>
        </div>

    </div>


        <div class="part-wrap part-three-wrap">
            <div class="wrap-bg"></div>
            <div class="part-bg part-three-bg">
                <div class="left-leaf"></div>
                <div class="part-title part-three-title">
                    <h2>四月桃花节，拈花夺魁英雄榜</h2>
                </div>
                <div class="part-desc part-three-desc">
                    <p>
                        活动期间，每天24点计算当日新增投资排名，上榜者可<strong>获丰厚奖励</strong>，投资者在当日<span class="mobile-style">
                        24点之前进行的多次投资，金额可<strong>累计计算</strong>。</span>
                    </p>
                </div>
                <div class="part-img heroes-list">
                    <div class="top"></div>
                    <div class="heroes-content">
                        <div class="prize-wrap clearfix">
                            <div class="ranking-seconed">
                                <div class="prize">

                                </div>
                                <div class="prize-desc">
                                    2~4名<br/>
                                    1%加息券
                                </div>
                            </div>

                            <div class="ranking-third">
                                <div class="prize">

                                </div>
                                <div class="prize-desc">
                                5~10名<br/>
                                0.5%加息券
                                </div>
                            </div>

                            <div class="big-prize">
                                <div id="bigPrize" class="prize"></div>
                                <div class="prize-desc">
                                    第一名<br/>
                                    实物大奖 XXXXX

                                </div>

                            </div>
                        </div>
                        <div class="ranking-list">
                            <div class="ranking-title">
                                    <div class="date-time">日期：<strong id="dateTime" data-starttime="2018-02-04" data-endtime="2018-03-01">2018-03-02</strong></div>
                                    <div class="total-invest">当日累计投资：<strong>999元</strong></div>
                                <div class="my-rank">我的排名：<strong>100</strong></div>
                            </div>
                            <div class="ranking-con">
                                <div class="ranking-list-item">
                                <table>
                                    <thead>
                                        <th>排名</th> <th>用户</th> <th>投资额（元）</th> <th>奖励</th>
                                    </thead>
                                    <tbody id="investRanking-tbody">
                                        <tr>
                                            <td>1</td> <td>xxxxx</td> <td>1,000.00</td> <td>实物大奖</td>
                                        </tr>
                                        <tr>
                                            <td>1</td> <td>xxxxx</td> <td>1,000.00</td> <td>实物大奖</td>
                                        </tr>
                                        <tr>
                                            <td>1</td> <td>xxxxx</td> <td>1,000.00</td> <td>实物大奖</td>
                                        </tr>
                                        <tr>
                                            <td>1</td> <td>xxxxx</td> <td>1,000.00</td> <td>实物大奖</td>
                                        </tr>
                                        <tr>
                                            <td>1</td> <td>xxxxx</td> <td>1,000.00</td> <td>实物大奖</td>
                                        </tr>
                                    </tbody>

                                </table>
                                </div>
                                <div class="read-more">
                                    <a href="javascript:;">显示更多></a>
                                </div>
                                <div class="ranking-btns clearfix" id="investRanking-button">
                                    <div class="pre-btn look-btn" id="rankingPre">查看前一天</div>
                                    <div class="next-btn look-btn" id="rankingNext">查看后一天</div>
                                    <div class="invest-btn">立即投资抢占排行榜</div>
                                </div>

                            </div>
                        </div>

                    </div>
                    <div class="bot"></div>

                </div>

            </div>

        </div>

    <#--温馨提示 begin-->
        <div class="kindly-tips activity-page">
            <dl>
                <dt class="title">温馨提示</dt>
                <dd>1. 本活动仅限直投项目，债权转让及新手专享项目不参与累计；</dd>
                <dd>2. 用户扫码所获280元“春风投资大礼包”系投资红包，为方便您分散投资，将即时以红包组的形式发放到您的账户，可在PC端“我的账户”或App端“我的”中进行查看；</dd>
                <dd>3. 用户参加“假日投资0.6%返现”活动时，请看准“0.6%返现”标签，假日期间凡没有“0.6%返现”标签的项目，均不参与投资返现;</dd>
                <dd>4. 假日投资0.6%返现奖励将于用户所投项目成功放款后1个工作日内发放至用户账户，返现奖励可直接提现或再次投资；</dd>
                <dd>5. 每日投资排行榜排名将在活动页面实时更新，排行榜中奖人数最多10名，如遇金额一致，则当日先达到该投资额的用户优先获奖，其他用户名次顺延；</dd>
                <dd>6. 拓天速贷会根据活动的情况，以等值、增值为基础调整奖品类型；</dd>
                <dd>7. 排行榜活动加息券将于获奖后三个工作日内统一发放，实物奖品将于活动结束后七个工作日内统一安排发放；</dd>
                <dd>8. 为了保证获奖结果的公平性，排行榜活动中实物大奖获奖用户在活动期间所进行的所有投标不允许进行债权转让，否则奖品将不予发放；</dd>
                <dd>9. 活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
                <dd>10. 活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
            </dl>
        </div>
    <#--温馨提示 end-->

    </div>
    <#include "../../module/login-tip.ftl" />
    <script type="text/template" id="tplTable">

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
                <td><%=i+1%></td>
                <td><%=item.loginName%></td>
                <td><%=item.centSumAmount%></td>
                <td><%=reward%></td>
            </tr>
            <% } %>

            </tbody>

    </script>

</div>

</@global.main>

