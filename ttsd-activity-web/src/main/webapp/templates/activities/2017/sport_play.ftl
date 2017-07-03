<#import "../../macro/global-dev.ftl" as global>

<#assign jsName = 'sport_play_2017' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>


<@global.main pageCss="${css.sport_play_2017}" pageJavascript="${js.sport_play_2017}" activeNav="" activeLeftNav="" title="运动达人VS职场骄子_活动中心_拓天速贷" keywords="运动达人,职场骄子,活动中心,抽奖,拓天速贷" description="拓天速贷活动期间用户每日登陆即有一次免费抽签机会,累计投资还可兑换不同实物奖品,运动奖VS职场奖,总有一款适合你.">
<div class="sport-play-container" id="sportPlayContainer">
    <div class="top-img">
        <img src="" width="100%" class="media-pc">
        <img src="" width="100%" class="media-phone">
    </div>
    <div class="shaking-item">
        <div class="wp clearfix">
            <h3 class="title-one"></h3>
            <ul class="info-item">
                <li>1.活动期间用户每日登陆即有一次免费抽签机会；</li>
                <li>2. 活动期间单笔投资每满1万元，即可增加1次抽签机会，如单笔投资5万元，则可增加5次抽签机会，以此类推；</li>
                <li>3. 当日所获免费抽签机会，仅限当日使用，如当日未使用，则机会失效。</li>
            </ul>
            <div class="lottery-times">
                剩余抽签机会：<span>XX次</span>
            </div>
            <div class="draw-item">
                <div class="draw-model">
                    <img src="" class="draw-btn">
                </div>
            </div>
        </div>
    </div>
    <div class="wp clearfix">
        <div class="content-item">
            <h3 class="title-two"></h3>
            <div class="user-info">
                <p>我的累计投资金额：<span>223434元</span></p>
                <p>当前选择奖品：<span>暂未选择奖品</span></p>
                <p class="tip-text">活动期间，根据累计投资金额，可兑换不同档奖品， 每档奖品默认可2选1兑换，不同档的奖品不可同时获得。</p>
            </div>
            <div class="gift-list">
                
            </div>
        </div>
        <dl class="rule-item">
            <dt>温馨提示：</dt>
            <dd>1.本活动仅限直投项目，债权转让及新手专享项目不参与累计；</dd>
            <dd>2.活动二中不同档的奖品不可同时获得，拓天速贷将根据用户累计投资额所能获得的最大奖励组合为准进行发放；</dd>
            <dd>3.活动中所有红包、加息券奖励将即时发放，用户可在PC端“我的账户”或App端“我的”中进行查看；</dd>
        </dl>
    </div>
</div>

</@global.main>