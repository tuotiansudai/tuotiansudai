<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.integral_draw}" pageJavascript="${js.integral_draw}" activeNav="" activeLeftNav="" title="拓荒计划_旅游活动_拓天速贷" keywords="拓荒计划,免费旅游,旅游活动投资,拓天速贷" description="拓天速贷金秋拓荒计划,多条旅游线路免费玩,你旅游我买单,邀请好友注册也可获得免费旅游大奖.">
<@global.isNotAnonymous>
<div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
<div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
</@global.isNotAnonymous>
<script type="text/javascript">
    var myPoint='${myPoint}';
</script>
<div class="tour-slide">
</div>
<div class="integral-draw-frame" id="integralDrawPage">
    <div class="reg-tag-current" style="display: none">
        <#include '../register.ftl' />
    </div>

    <div class="good-news-frame clearfix">

        <div class="good-news-box">
            <em></em>
            <em></em>
            <em></em>
            <em></em>
            <b>好消息</b>
            <p>
                亲爱的用户： <br/>
                经过程序猿的日夜赶工，积分商城即将华丽丽上线辣！在积分上线之前，平台要做一次热身运动，<i>只要使用现有积分参与抽奖，抽奖次数无上限不用任何投资，100%中奖，奖品白拿！</i>入冬前最后的限时狂欢，海量尤物等你抽，让我们全“礼”以赴！
            </p>
        </div>

        <div class="good-news-box">
            <em></em>
            <em></em>
            <em></em>
            <em></em>
            <b>小贴士</b>
            <p>为了公平起见，积分商城将于10月22日开始新一轮的兑换规则。为此，平台将于10月21日24时对之前的积分进行清零。还等什么？赶快来参与抽奖狂欢趴吧！</p>
        </div>
    </div>

    <div class="luck-draw-frame-one">
        <div class="title-column-top title-one"></div>
        <div class="one-thousand-points">
            <#include "gift-circle-draw.ftl"/>
            <div class="draw-instructions">每次抽奖消耗1000积分</div>
            <div class="button-col">
                <a href="#" class="btn-to-invest">立即投资赚积分</a>
            </div>
        </div>

    </div>

    <div class="luck-draw-frame-two">
        <div class="title-column-top title-two"></div>
        <div class="ten-thousand-points">
            <#include "gift-circle-draw.ftl"/>
            <div class="draw-instructions">每次抽奖消耗10000积分</div>
            <div class="button-col">
                <a href="#" class="btn-to-invest">立即投资赚积分</a>
            </div>
        </div>

    </div>

    <div class="activity-note-tip">
        <b>温馨提示</b>
        <p>
            1.活动期间，用户可使用现有积分参加抽奖，抽奖次数上不封顶，活动结束后用户所有积分清零，未使用则视为自动放弃，详细情况请查看“拓天公告”； <br/>
            2.投资红包和加息券实时发放，用户可在“我的账户-我的宝藏”中查看，实物奖品将于活动结束后七个工作日内统一安排发放，部分地区邮费自付，详询客服；<br/>
            3.活动期间用户新增投资积分=投资金额×项目期限÷365，用户可使用新增积分参与本抽奖，逾期未抽奖的积分视为自动放弃，10月22日0:00，用户在本活动期间新增投资积分将与累计积分一并清零；<br/>
            4.抽奖过程中，如果出现违规行为（如作弊领取等），拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，收回活动中所得的奖品，必要时将追究法律责任；<br/>
            5.拓天速贷在法律范围内保留本活动的最终解释权。
        </p>

    </div>

    <div class="tip-list" style="display: none">
        <div class="close-btn go-close"></div>
        <div class="text-tip"></div>
        <div class="btn-list"></div>
    </div>

</div>
</@global.main>


