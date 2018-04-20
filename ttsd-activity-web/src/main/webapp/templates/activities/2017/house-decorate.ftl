<#import "../../macro/global.ftl" as global>

<@global.main pageCss="${css.house_decorate_2017}" pageJavascript="${js.house_decorate_2017}" activeNav="" activeLeftNav="" title="818居家投资节_活动中心_拓天速贷" keywords="拓天速贷,818投资节,投资红包,体验金,实物奖励" description="拓天速贷818居家投资节活动,每日登录抢夺投资红包及加息劵,天降红包雨,全家抢不停,当日累计投资可获得该日投资额的2.18倍体验金奖励,以及相应的居家实物奖励.">
<div class="house-decorate-container" id="houseDecorateContainer">
    <div class="top-img"></div>
        <div class="content-item page-width">
            <i class="bg-top"></i>
            <i class="bg-bottom"></i>
            <i class="column-title-page one"></i>
            <div class="day-bag-item">
                <p>活动期间，用户每日登录，可在活动页面抢到一个“每日红包”，“每日红包”包含投资红包或加息券，金额随机。</p>
                <p class="tc">
                    <span class="bag-item">
                        <strong></strong>
                    </span>
                </p>
            </div>
        </div>

    <div class="bg-fullscreen">
        <div class="content-item page-width even">
            <i class="bg-top"></i>
            <i class="bg-bottom"></i>
            <i class="column-title-page two"></i>
            <div class="free-money-item">

                <div class="info-item">
                    活动期间，每日24:00结算用户当日累计投资额，根据用户当日累计投资额，最高可获得其该日投资额的<span>2.18倍</span>体验金奖励。
                </div>
                <div class="list-item">
                    <div class="loan-number">
                        今日累计投资额:<@global.isAnonymous><strong class="to-login">登录后查看</strong></@global.isAnonymous>
                        <@global.isNotAnonymous><strong><#if toDayAmount??>${toDayAmount}</#if>元</strong></@global.isNotAnonymous>
                    </div>
                    <table class="table table-pc">
                        <thead>
                        <tr>
                            <th>用户当日累计投资额（元）</th>
                            <th>1万≤当日累计投资额＜10万</th>
                            <th>10万≤当日累计投资额＜30万</th>
                            <th>30万≤当日累计投资额＜60万</th>
                            <th>当日累计投资额≥60万</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>体验金奖励</td>
                            <td>当日投资额*<strong>0.5倍</strong>体验金</td>
                            <td>当日投资额*<strong>0.8倍</strong>体验金</td>
                            <td>当日投资额*<strong>1.8倍</strong>体验金</td>
                            <td>当日投资额*<strong>2.18倍</strong>体验金</td>
                        </tr>
                        </tbody>
                    </table>
                    <table class="table table-phone">
                        <thead>
                        <tr>
                            <th>用户当日累计投资额（元）</th>
                            <th>体验金奖励</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>1万≤当日累计投资额＜10万</td>
                            <td>当日投资额*<strong>0.5倍</strong>体验金</td>
                        </tr>
                        <tr>
                            <td>10万≤当日累计投资额＜30万</td>
                            <td>当日投资额*<strong>0.8倍</strong>体验金</td>
                        </tr>
                        <tr>
                            <td>30万≤当日累计投资额＜60万</td>
                            <td>当日投资额*<strong>1.8倍</strong>体验金</td>
                        </tr>
                        <tr>
                            <td>当日累计投资额≥60万</td>
                            <td>当日投资额*<strong>2.18倍</strong>体验金</td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="tip-item">
                        例如，拓小天在8月18日当天累计投资30万元，则拓小天获得的体验金奖励为：300000*1.8=540000元
                    </div>
                </div>
            </div>

        </div>
    </div>

   <div class="content-item page-width">
       <i class="bg-top"></i>
       <i class="bg-bottom"></i>
       <i class="column-title-page three"></i>

            <div class="gift-list-item">
                <div class="info-text">活动结束后，计算用户活动期间累计投资额，可领取相应的居家实物奖励，实物奖不可累计获得。</div>
                <dl class="gift-list clearfix">
                    <dt>活动期间总累计投资额:<@global.isAnonymous><strong class="to-login">登录后查看</strong></@global.isAnonymous>
                        <@global.isNotAnonymous><strong><#if amount??>${amount}</#if>元</strong></@global.isNotAnonymous></dt>
                    <dd class="row-item">
                        <p class="img-item gift-one"></p>
                        <p class="name-item">法国进口红酒</p>
                        <p>累计投资额：8万元</p>
                    </dd>
                    <dd class="row-item">
                        <p class="img-item gift-two"></p>
                        <p class="name-item">按摩披肩</p>
                        <p>累计投资额：15万元</p>
                    </dd>
                    <dd class="row-item">
                        <p class="img-item gift-three"></p>
                        <p class="name-item">足浴盆</p>
                        <p>累计投资额：18万元</p>
                    </dd>
                    <dd>
                        <p class="img-item gift-four"></p>
                        <p class="name-item">艾美特遥控塔扇</p>
                        <p>累计投资额：38万元</p>
                    </dd>
                    <dd>
                        <p class="img-item gift-five"></p>
                        <p class="name-item">格兰仕极光微波炉</p>
                        <p>累计投资额：56万元</p>
                    </dd>
                    <dd>
                        <p class="img-item gift-six"></p>
                        <p class="name-item">松下吸尘器</p>
                        <p>累计投资额：80万元</p>
                    </dd>
                    <dd>
                        <p class="img-item gift-seven"></p>
                        <p class="name-item">小天鹅滚筒洗衣机</p>
                        <p>累计投资额：100万元</p>
                    </dd>
                </dl>
            </div>
        </div>

    <div class="bg-fullscreen">
        <div class="content-item page-width none">
            <dl class="rule-item clearfix">
                <dt>温馨提示</dt>
                <dd>1.本活动仅限直投项目，债权转让及新手专享项目不参与累计；</dd>
                <dd>2.用户在本活动中所获所有红包及加息券奖励即时发放，用户可在PC端“我的账户”或App端“我的”中进行查看；</dd>
                <dd>3.体验金奖励将于获奖后次日发放至用户账户；</dd>
                <dd>4.实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
                <dd>5.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
                <dd>6.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
            </dl>
        </div>
        </div>




    <#include "../../module/login-tip.ftl" />
    <div class="lottery-tip" id="lotteryTip">
        <div class="lottery-content">
            <p class="info-text">恭喜您获得了</p>
            <p class="gift-name">{{prizeValue}}</p>
            <!-- <p class="tip-text">没有抽奖机会,明日再来！</p>
            <p class="tip-text">不在活动时间范围内</p> -->
        </div>
        <div class="lottery-link">
            <a href="javascript:void(0)" class="close-tip">知道了</a>
        </div>
    </div>
    <script type="text/html" id="lotteryTipTpl">
        <div class="lottery-content">
        {{if returnCode==0}}
            <p class="info-text">恭喜您获得了</p>
            <p class="gift-name">{{prizeValue}}</p>
        {{else if returnCode==1}}
            <p class="tip-text">没有抽奖机会,明日再来！</p>
        {{else if returnCode==3}}
            <p class="tip-text">不在活动时间范围内</p>
        {{/if}}
        </div>
        <div class="lottery-link">
            <a href="javascript:void(0)" class="close-tip">知道了</a>
        </div>
    </script>
</div>

</@global.main>