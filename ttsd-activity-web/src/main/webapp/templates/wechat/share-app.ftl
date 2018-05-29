<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.share_app}" pageJavascript="${js.app_shareSuc}"  title="新手福利_拓天新手投资_拓天速贷">
    <#--<#include "../pageLayout/header.ftl" />-->
<div class="share-app-container clearfix" id="shareAppContainer">
	<div class="share-container" >
        <div id="register_btn">注册</div>
		<div class="share-item">
				<#if isOldUser?? && isOldUser>
                    <!-- 老用户信息 start -->
                    <div class="item-intro share-old"></div>
                    <!-- 老用户信息  end -->
				<#else>
                    <div class="item-tel">
                        <span>${referrerInfo!}</span>
                    </div>
                    <div class="item-intro">
                        送你<span>6888元</span>体验金+<span>668元</span>投资红包
                    </div>
				</#if>


			<div class="item-form">
				<div class="item-int tc">
                    <span class="gift-icon"></span>
				</div>
				<div class="item-int tc">
					<a href="/app/download" class="btn item-submit" >礼包到手下载APP赚钱</a>
				</div>
                <div class="item-int pad-m-tb">
                    <p class="tc">好友<span>${referrerInfo!}</span>邀请你来拓天速贷投资</p>
                    <p class="tc">新手活动收益高，奖不停，拿红包到手软！</p>
                </div>
			</div>
		</div>
	</div>
    <div class="newbie-step-one page-width">
        <div class="image-title"></div>
        <dl class="new-user-list clearfix">
            <dt class="clearfix tc">新手体验项目</dt>
            <dd><i>13</i>% <br/><em>约定年化利率</em></dd>
            <dd><i>3</i>天 <br/><em>项目期限</em></dd>
        </dl>

    </div>

    <div class="newbie-step-two">
        <div class="image-decoration-bg"></div>
        <div class="image-decoration-bg-app-l"></div>
        <div class="image-decoration-bg-app-r"></div>
        <div class="image-title"></div>
        <div class="image-red-envelope"></div>
    </div>

    <div class="newbie-step-three page-width">
        <div class="image-title"></div>
        <div class="subtitle tc">
            <div class="subtitle-container">
                <i>新人独享11%高息新手标，30天灵活期限，满足您对资金流动性的需求</i>
            </div>
        </div>

        <dl class="new-user-list clearfix">
            <dt class="clearfix tc">新手专享标</dt>
            <dd><i>11</i>% <br/><em>约定年化利率</em></dd>
            <dd><i>30</i>天 <br/><em>项目期限</em></dd>
        </dl>
    </div>

    <div class="newbie-step-four">
        <div class="image-decoration-bg"></div>
        <div class="image-decoration-bg-app-l"></div>
        <div class="image-decoration-bg-app-r"></div>
        <div class="image-title"></div>
        <div class="subtitle tc">
            <div class="subtitle-container">
                <i>注册后15天内完成首次投资，可获得3%加息券</i>
            </div>
        </div>

        <div class="image-coupon"></div>

        <div class="image-steps tc">
            <span class="step-one"></span>
            <span class="step-two"></span>
            <span class="step-three"></span>

        </div>
    </div>
    <#include '../module/register-reason.ftl' />

    <div class="newbie-step-six">
        <div class="newbie-step-six-box">
            <p>温馨提示</p>
            <p>1. 平台新注册用户可使用6888元体验金投资新手体验项目，投资周期为3天，到期可获得收益，该笔收益可在 "我的账户"
                中查看，投资累计满1000元即可提现（投资债权转让项目除外）；</p>
            <p>2. 30天 "新手专享" 债权每次限投50-10000元，每人仅限投1次；</p>
            <p>3. 用户所获红包及加息券可在 "我的账户-我的宝藏" 查看；</p>
            <p>4. 每笔投资仅限使用一张优惠券，用户可在投资时优先选择收益最高的优惠券使用，并在 "优惠券" 一栏中进行勾选；</p>
            <p>5. 使用红包金额将于所投债权放款后返至您的账户；</p>
            <p>6. 使用加息券所得收益，将体现在该笔投资项目收益中，用户可在 "我的账户" 中查询；</p>
            <p>7. 每个身份证仅限参加一次，刷奖、冒用他人身份证、银行卡者一经核实，取消活动资格，所得奖励不予承兑；</p>
            <p>8. 活动遵循拓天速贷法律声明，最终解释权归拓天速贷平台所有。</p>
        </div>

    </div>
</div>
</@global.main>