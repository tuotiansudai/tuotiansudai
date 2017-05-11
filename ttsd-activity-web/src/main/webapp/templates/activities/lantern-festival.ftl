<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.lantern_festival}" pageJavascript="${js.lantern_festival}" activeNav="" activeLeftNav="" title="元宵节活动_活动中心_拓天速贷" keywords="拓天速贷,投资摇红包,微信猜灯谜,元宵节投资活动,拓天速贷排行榜" description="拓天速贷元宵节投资活动,天降红包雨,十五摇红包,微信猜灯谜,神秘大奖拿不停,拓天速贷龙虎排行榜邀您共度元宵好时光.">
<div class="activity-container" id="lanternFrame">
    <@global.isNotAnonymous>
        <div style="display: none" class="login-name"
             data-login-name='<@global.security.authentication property="principal.username" />'></div>
        <div style="display: none" class="mobile" id="MobileNumber"
             data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
    </@global.isNotAnonymous>
    <div class="top-intro-img img-item"></div>
    <div class="top-intro-img img-item-phone"></div>
    <div class="actor-content-group bg-one">
        <div class="wp clearfix">
            <div class="reg-tag-current" style="display: none">
                <#include '../module/register.ftl' />
            </div>
            <div class="tree-model">
                <h3 class="img-item"></h3>
                <h3 class="img-item-phone"></h3>
                <div class="info-item">
                    活动期间投资额每满1000元即可获得一次摇一摇机会，如单笔投资10000元，可直接获得10次摇一摇机会，用户每日最多可获得20次摇一摇机会，当日超出部分的投资额不予累计。
                </div>
                <div class="gift-item text-c reward-gift-box">
                    <div class="time-item">
                        我的抽奖机会:<span class="draw-time">0</span>次
                    </div>
                    <div class="rotate-btn pointer-img"></div>
                    <div class="record-list-box">
                        <div class="menu-switch">
                            <span class="active">中奖记录</span>
                            <span>我的奖品</span>
                        </div>
                        <div class="record-list">
                            <i class="icon-left"></i><i class="icon-right"></i>
                            <ul class="user-record" id="userRecordList">
                            </ul>
                            <ul class="own-record" id="ownRecordList" style="display: none">
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="list-model">
                <h3 class="title-tow img-item"></h3>
                <h3 class="title-tow img-item-phone"></h3>
                <div class="info-item">
                    活动期间，根据用户当日的累计投资总额进行排名，当日前十名用户可以获得相应奖励，其中第一名的用户将夺得当日至尊大奖。
                </div>
                <ul class="list-info">
                    <li>
                        <i class="icon-date"></i>
                        <span>日期：<strong
                                id="HistoryAwards"><#if currentTime??>${currentTime?string('yyyy-MM-dd')}</#if></strong></span>
                    </li>
                    <li>
                        <i class="icon-list"></i>
                        <span class="app-list-text">
							<@global.isAnonymous>
                                当前排名：<a href="javascript:void(0)" class="get-rank show-login">登录查看</a>
                            </@global.isAnonymous>
                            <@global.isNotAnonymous>
                                <#if investRanking?? &&investRanking &gt; 0>
                                    当前排名:&nbsp;<strong><#if investRanking &gt; 20>20+ <#else >${investRanking}
                                    &nbsp;</strong>名 </#if>
                                <#else >
                                    未参与排行
                                </#if>
                            </@global.isNotAnonymous>
						</span>
                    </li>
                    <li>
                        <i class="icon-money"></i>
                        <span class="app-loan-text">
							<@global.isAnonymous>
                                今日投资额：0.00元
                            </@global.isAnonymous>
                            <@global.isNotAnonymous>
                                今日投资额：<strong>${investAmount}</strong>元
                            </@global.isNotAnonymous>
						</span>
                    </li>
                </ul>
                <div class="today-gift">
                    <div class="gift-img-item">
                        <img src="<#if mysteriousPrizeDto??>${commonStaticServer}${mysteriousPrizeDto.imageUrl}</#if>">
                    </div>
                    <div class="gift-name">
                        <p class="name-text"><#if mysteriousPrizeDto??>${mysteriousPrizeDto.prizeName}</#if></p>
                        <p class="tip-text">（当日第一名可得）</p>
                    </div>
                </div>
                <h3 class="mt-50 img-item"></h3>
                <h3 class="mt-50 img-item-phone"></h3>
                <div class="table-group">
                    <input type="hidden" id="activityStartTime" value="${activityStartTime!}">
                    <input type="hidden" id="activityEndTime" value="${activityEndTime!}">
                    <input type="hidden" id="TodayAwards"
                           value="<#if currentTime??>${currentTime?string('yyyy-MM-dd')}</#if>">
                    <table class="list-table">
                        <thead>
                        <tr>
                            <th>排名</th>
                            <th>用户</th>
                            <th>投资额</th>
                            <th>奖励</th>
                        </tr>
                        </thead>
                        <tbody id="investRanking-tbody">
                        </tbody>
                    </table>
                    <script type="text/html" id="tplTable">
                        {{each records}}
                        <tr>
                            <td>
                                {{if $index==0}}
                                <i class="icon-cat one">
                                {{else if $index==1}}
                                <i class="icon-cat two">
                                {{else if $index==2}}
                                <i class="icon-cat three">
                                {{else}}
                                {{$index+1}}
                                {{/if}}
                            </td>
                            <td>{{$value.loginName}}</td>
                            <td>{{$value.centSumAmount}}</td>
                            <td>
                                {{if $index==0}}
                                至尊大奖
                                {{else if $index>0 && $index<5}}
                                200元红包
                                {{else}}
                                100元红包
                                {{/if}}
                            </td>
                        </tr>
                        {{/each}}

                    </script>
                </div>
                <div class="other-list">
                    <p class="text-c" id="investRanking-button">
                        <span id="heroPre">查看前一天排行</span>
                        <span id="heroNext">查看后一天排行</span>
                    </p>
                    <p>
                        <a href="/loan-list" class="list-btn">立即投资抢排行</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
    <div class="actor-content-group bg-two">
        <div class="wp clearfix">
            <div class="rule-model">
                <dl class="rule-list">
                    <dt>活动说明：</dt>
                    <dd>1、活动期间，每天24点计算当日新增投资排名，投资者在当日24点之前进行的多次投资，金额可累计计算；排行榜仅限于直投项目，其余投资不计入今日投资金额中；</dd>
                    <dd>2、排行榜中奖人数最多10名，如遇金额一致，则当日先达到该投资额的用户优先获奖，其他用户名次顺延；</dd>
                    <dd>3、每日投资排行榜排名将在活动页面实时更新。中奖结果将于次日由客服联系确认，红包在中奖后三个工作日内发放，实物奖品将于活动结束后七个工作日内统一安排发放；</dd>
                    <dd>4、拓天速贷会根据活动的情况，以等值、增值为基础调整奖品类型；</dd>
                    <dd>5、为了保证获奖结果的公平性，获奖用户在活动期间所进行的投标不允许进行债权转让；</dd>
                    <dd>6、拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
                </dl>
            </div>
            <div class="wx-model text-c">
                <h3 class="img-item"></h3>
                <h3 class="img-item-phone"></h3>
                <div class="wx-item">
                    <p>扫描二维码，关注<strong>“拓天速贷财富”</strong>，并回复<strong>“猜灯谜”</strong>；</p>
                    <p>每天中午12点，微信公众号更新三条灯谜，用户回复“猜灯谜”可获取题目，将答案回复至后台，有机会获得红包奖励</p>
                    <p class="wx-tip">活动结束后，累计答对最多的用户，将获得发光地球仪一个。</p>
                    <i class="text-c wx-one"></i>
                    <i class="text-c wx-two"></i>
                </div>
            </div>
        </div>
    </div>
    <#include "login-tip.ftl" />
    <div class="tip-list-frame">
    <#--真实奖品的提示-->
        <div class="tip-list" data-return="concrete">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text">恭喜您！</p>
                <p class="reward-text">抽中了<em class="prizeValue"></em>！</p>
                <p class="des-text">拓天客服将在7个工作日内联系您发放奖品</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
        </div>

    <#--虚拟奖品的提示-->
        <div class="tip-list" data-return="virtual">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text">恭喜您！</p>
                <p class="reward-text">抽中了<em class="prizeValue"></em>！</p>
                <p class="des-text">奖品已发放至“我的宝藏”当中。</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">继续抽奖</a></div>
        </div>

    <#--没有抽奖机会-->
        <div class="tip-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您暂无抽奖机会啦～</p>
                <p class="des-text">赢取机会后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>

    <#--不在活动时间范围内-->
        <div class="tip-list" data-return="expired">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">不在活动时间内~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>

    <#--实名认证-->
        <div class="tip-list" data-return="authentication">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您还未实名认证~</p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">知道了</a></div>
        </div>
    </div>
</div>
</@global.main>