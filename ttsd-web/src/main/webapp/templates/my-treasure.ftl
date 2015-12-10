<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="" activeNav="我的账户" activeLeftNav="我的宝藏" title="我的宝藏">

<div class="content-container my-treasure-content">
    <h4 class="column-title"><em class="tc">我的宝藏</em></h4>
    <div class="experience-ticket-box">
        <div class="vertical-line"></div>
        <div class="ticket-amount fl tc">
            <h3>投资体验劵</h3>
            <strong>1000元</strong>
            <time>有效期：2015-11-15 至 2015-12-15</time>
        </div>
        <div class="ticket-info fr">
            <dl class="pad-s">
                <dt>使用条件：</dt>
                <dd>
                    在拓天速贷平台投资时使用。
                </dd>
                <dt>使用方法：</dt>
                <dd>
                    在投资金额输入框的下方勾
                    选“使用体验劵”即可使用。
                </dd>
                <dd class="tc"><a href="javascript:void(0);" class="btn-action">立即使用</a></dd>
            </dl>


        </div>
    </div>
    <div class="experience-ticket-box">
        <div class="vertical-line"></div>
        <div class="ticket-amount fl tc">
            <h3>投资体验劵</h3>
            <strong>1000元</strong>
            <time>有效期：2015-11-15 至 2015-12-15</time>
        </div>
        <div class="ticket-info fr">
            <dl class="pad-s">
                <dt>使用条件：</dt>
                <dd>
                    在拓天速贷平台投资时使用。
                </dd>
                <dt>使用方法：</dt>
                <dd>
                    在投资金额输入框的下方勾
                    选“使用体验劵”即可使用。
                </dd>
                <dd class="tc"><button type="button" class="btn-action">立即使用</button></dd>
            </dl>


        </div>
    </div>
    <div class="experience-ticket-box ticket-status-used">
        <div class="vertical-line"></div>
        <div class="ticket-amount fl tc">
            <h3>投资体验劵</h3>
            <strong>1000元</strong>
            <time>有效期：2015-11-15 至 2015-12-15</time>
        </div>
        <div class="ticket-info fr">
            <dl class="pad-s">
                <dt>使用条件：</dt>
                <dd>
                    在拓天速贷平台投资时使用。
                </dd>
                <dt>使用方法：</dt>
                <dd>
                    在投资金额输入框的下方勾
                    选“使用体验劵”即可使用。
                </dd>
                <dd class="tc"><button type="button" class="btn-action" disabled>立即使用</button></dd>
            </dl>


        </div>

        <div class="sign-seal"><span>已<br/>使用</span></div>
    </div>
    <div class="experience-ticket-box ticket-status-expired">
        <div class="vertical-line"></div>
        <div class="ticket-amount fl tc">
            <h3>投资体验劵</h3>
            <strong>1000元</strong>
            <time>有效期：2015-11-15 至 2015-12-15</time>
        </div>
        <div class="ticket-info fr">
            <dl class="pad-s">
                <dt>使用条件：</dt>
                <dd>
                    在拓天速贷平台投资时使用。
                </dd>
                <dt>使用方法：</dt>
                <dd>
                    在投资金额输入框的下方勾
                    选“使用体验劵”即可使用。
                </dd>
                <dd class="tc"><button type="button" class="btn-action" disabled>立即使用</button></dd>
            </dl>


        </div>

        <div class="sign-seal"><span>已<br/>过期</span></div>
    </div>

    <p class="no-treasure-tip tc pad-m" style="display: none;">您当前没有宝藏，敬请期待！</p>
</div>
</@global.main>