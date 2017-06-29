<#import "../../macro/global-dev.ftl" as global>

<#assign jsName = 'sport_play_2017' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>


<@global.main pageCss="${css.sport_play_2017}" pageJavascript="${js.sport_play_2017}" activeNav="" activeLeftNav="" title="领券专场_拓天周年庆_活动中心_拓天速贷" keywords="拓天速贷,拓天周年庆,红包奖励,加息券" description="拓天周年庆-领券专场活动,微信扫描二维码关注拓天速贷服务号,回复我要领券即可领取1000红包奖励+0.8%加息券.">
<div class="sport-play-container" id="sportPlayContainer">
    <div class="top-img">
        <img src="" width="100%" class="media-pc">
        <img src="" width="100%" class="media-phone">
    </div>
    <div class="shaking-item">
        <div class="wp clearfix">
            第三方士大夫
        </div>
    </div>
    <div class="select-item">
        <div class="wp">
            sdasd
        </div>
    </div>
    <div class="wp clearfix">
        <div class="content-item">
            sdfsd
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