<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.newman_tyrant}" pageJavascript="${js.newman_tyrant}" activeNav="" activeLeftNav="" title="新贵富豪争霸赛_活动中心_拓天速贷" keywords="拓天速贷,投资排行榜,实物大奖,体验金奖励" description="拓天速贷新贵富豪争霸赛活动,新用户每日累计投资额形成'新贵打擂榜'老用户每日累计投资额形成'富豪守擂榜'上榜者可获实物大奖,加息劵,体验金奖励.">
<div class="regal-list-container" id="newmanTyrant">
    <div class="top-container">
    </div>
    <div class="rule-container clearfix">
        <div class="wp">
            <div class="rule-content">
                <i class="rule-one"></i>
                <i class="rule-two"></i>
                <i class="rule-three"></i>
                <div class="content-item">
                    <h3>活动规则</h3>
                    <div class="rule-text">
                        <p><i>1</i>活动期间，针对3月11日-4月30日新注册用户，每日累计投资额形成”新贵排行榜“；针对4月11日之前注册的用户，每日累计投资额形成”富豪排行榜“，每日双方上榜者均可获赠丰厚奖励。</p>
                        <p><i>2</i>活动期间，拓天速贷每日发放金、银两项实物大奖，每日24点结算当日榜单，分别由当日“富豪排行榜”与“新贵排行榜”第一名获奖者获得，投资额较高的可获金奖奖励，投资额较低的可获银奖奖励。</p>
                    </div>
                    <ul class="gift-list">
                        <li>
                            <div class="gift-item">
                                <img src="http://placehold.it/202x202" alt="金奖" width="100%">
                            </div>
                            <div class="gift-num icon-first">
                                金奖：xxxxxx
                            </div>
                        </li>
                        <li>
                            <div class="gift-item">
                                <img src="http://placehold.it/202x202" alt="银奖" width="100%">
                            </div>
                            <div class="gift-num icon-twice">
                                银奖：xxxxxxx
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="rank-container clearfix">
        <div class="wp clearfix">
            <div class="rank-content">
                <i class="one-bg"></i>
                <i class="two-bg"></i>
                <h3 class="rank-title">新贵VS富豪排行榜</h3>
                <ul class="rank-info">
                    <li class="info-date">
                        2017-3-29
                    </li>
                    <li class="info-rank">
                        我的排名：<span>未上榜</span>
                    </li>
                    <li class="info-money">
                        今日投资金额 : <span>6660.66</span>元
                    </li>
                </ul>
                <div class="rank-board">
                    <div class="board-line">
                        <div class="board-item">
                            <div class="board-tab">
                                <span class="active" data-type="tyrant">富豪排行榜</span>
                                <span data-type="newman">新贵排行榜</span>
                            </div>
                            <table class="rank-table">
                                <thead>
                                    <tr>
                                        <th>排名</th>
                                        <th>用户</th>
                                        <th>投资额</th>
                                        <th>奖励</th>
                                    </tr>
                                </thead>
                                <tbody id="rankTable">
                                    
                                </tbody>
                                <script type="text/html" id="rankTableTpl">
                                {{if records.length>0}}
                                    {{each records}}
                                    <tr>
                                        {{if $index==0}}
                                        <td class="one">1</td>
                                        {{else if $index==1}}
                                        <td class="two">2</td>
                                        {{else if $index==2}}
                                        <td class="three">3</td>
                                        {{else}}
                                            <td>{{$index}}</td>
                                        {{/if}}
                                        <td>{{$value.loginName}}</td>
                                        <td>{{$value.centSumAmount}}</td>
                                        {{if $index==0}}
                                        <td>“金/银”实物大奖</td>
                                        {{else if $index==1}}
                                        <td>1%加息券</td>
                                        {{else if $index==2}}
                                        <td>1%加息券</td>
                                        {{else}}
                                            <td>100元红包</td>
                                        {{/if}}
                                    </tr>
                                    {{/each}}
                                {{/if}}
                                </script>
                            </table>
                        </div>
                    </div>
                    <div class="board-btn">
                        <span>查看后一天结果</span>
                        <span>查看后一天结果</span>
                    </div>
                    <div class="link-btn">
                        <a href="/loan-list">立即投资抢排行</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="dare-container clearfix">
        <div class="wp">
            <div class="dare-content">
                <h3 class="dare-title">双榜对抗赛</h3>
                <div class="dare-intro">
                    每日24点，针对当日”新贵排行榜“10名上榜者平均投资额，与”富豪排行榜“3名上榜者平均投资额进行PK，平均投资额较大的一方所有上榜者每人均可获得5888元体验金奖励。
                </div>
                <div class="dare-list">
                    <div class="list-content">
                        <div class="list-title">富豪排行榜今日投资均值
                        </div>
                        <div class="list-number">
                            <i class="icon-line left"></i>
                            <i class="icon-line right"></i>
                            <div class="number-bg">
                                56,234.23元
                            </div>
                        </div>
                    </div>
                    <div class="list-icon"></div>
                    <div class="list-content new-rank">
                        <div class="list-title">新贵排行榜今日投资均值
                        </div>
                        <div class="list-number">
                            <i class="icon-line left"></i>
                            <i class="icon-line right"></i>
                            <div class="number-bg">
                            156,234.23元
                            </div>
                        </div>
                    </div>
                </div>
                <div class="list-btn">
                    <span>查看历史比赛结果</span>
                </div>
                <div class="actor-content">
                    <div class="content-item">
                        <h3>五一专享 不劳而获加息标</h3>
                        <div class="actor-text">
                            <p>5月1日-5月3日，投资“五一专享”债权≥1000元，即可享受<span>0.2%-1.2%阶梯加息奖励</span>。专享标加息奖励可与排行榜活动同时参与。</p>
                        </div>
                    </div>
                </div>
                <dl class="actor-rule">
                    <dt>温馨提示：</dt>
                    <dd>1、活动期间，每天24点计算当日新增投资排名，投资者在当日24点之前进行的多次投资，金额可累计计算；排行榜仅限于直投项目，其余投资不计入今日投资金额中；</dd>
                    <dd>2、排行榜中奖人数最多10名，如遇金额一致，则当日先达到该投资额的用户优先获奖，其他用户名次顺延；</dd>
                    <dd>3、每日投资排行榜排名将在活动页面实时更新。中奖结果将于次日由客服联系确认，加息券及体验金奖励在中奖后三个工作日内发放，实物奖品将于活动结束后七个工作日内统一安排发放；</dd>
                    <dd>4、拓天速贷会根据活动的情况，以等值、增值为基础调整奖品类型；</dd>
                    <dd>5、为了保证获奖结果的公平性，金、银实物大奖的获奖用户在活动期间所进行的投标不允许进行债权转让，否则奖品将不予发放；</dd>
                    <dd>6、拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
                </dl>
            </div>
        </div>
    </div>
</div>
<#include "login-tip.ftl" />
</@global.main>