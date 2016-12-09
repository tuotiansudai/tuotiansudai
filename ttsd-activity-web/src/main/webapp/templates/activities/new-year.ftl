<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.new_year}" pageJavascript="${js.new_year}" activeNav="" activeLeftNav="" title="活动中心_投资活动_拓天速贷" keywords="拓天活动中心,拓天活动,拓天投资列表,拓天速贷" description="拓天速贷活动中心为投资用户提供投资大奖,投资奖励,收益翻倍等福利,让您在赚钱的同时体验更多的投资乐趣.">
    <@global.isNotAnonymous>
    <div style="display: none" class="login-name" data-login-name='<@global.security.authentication property="principal.username" />'></div>
    <div style="display: none" class="mobile" id="MobileNumber" data-mobile='<@global.security.authentication property="principal.mobile" />'></div>
    </@global.isNotAnonymous>
<div class="new-year-slide" id="newYearSlide"></div>

<div class="new-year-frame page-width" id="newYearDayFrame">
    <div class="reg-tag-current" style="display: none">
        <#include '../module/register.ftl' />
    </div>

    <div class="title-main-col">
        <i class="line-left"></i>
        <i class="line-right"></i>
        <em>签到砸金蛋，100%中奖</em>
    </div>

    <div class="box-normal clearfix">

        <div class="draw-area-box">
            <img src="${staticServer}/activity/images/new-year/panzi.jpg" class="draw">
            <img src="${staticServer}/activity/images/new-year/egg.png" class="gold-egg">
        </div>

        <div class="button-group tc">
            <a href="#" class="normal-button">签到</a>
            <a href="#" class="normal-button">邀请好友</a>
            <a href="#" class="normal-button">马上投资</a>
        </div>

        <div class="activity-rule-box clearfix">
            <div class="text-box">
                <span>
                活动规则说明： <br/>
                1、活动期间每日签到即可获得一次免费砸蛋机会；<br/>
                2、活动期间每邀请1名好友注册可砸5次，上不封顶；<br/>
                3、活动期间投资新年专享标，每满5000元即可砸1次，如单笔
                投资10000元，可直接获得2次砸金蛋机会，每名用户活动
                期间凭投资最多砸10次；<br/>
                4、砸蛋机会需在活动有效期内使用，过期作废，请及时使用；<br/>
                5、活动中有恶意刷奖的行为，一经查出拓天速贷有权追溯已发
                放的奖励。
                    </span>
            </div>
            <div class="gift-info-box">
                <ul class="gift-record clearfix">
                    <li class="active"><span>中奖记录</span></li>
                    <li><span>我的奖品</span></li>
                </ul>
                <div class="record-list">
                    <ul class="record-model user-record" >
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                        <li>恭喜130****7149抽中了新马泰七日游</li>
                    </ul>
                    <ul class="record-model own-record" style="display: none"></ul>
                </div>
            </div>

        </div>
    </div>

    <div class="title-main-col">
        <i class="line-left"></i>
        <i class="line-right"></i>
        <em>新年开神灯，许你10个愿望</em>
    </div>

    <div class="box-normal clearfix">
        <div class="magic-lamp">
            <i class="lamp-left"></i>
            <i class="lamp-right"></i>
            <div class="slide-text">
                2016即将画上一个圆满的句号，你的新年愿望是什么？ <br/>
                是一部心水已久的手机？一次不看价签的买买买？<br/>
                还是一张回家的车票？一次放空身心的度假之旅？<br/>
                在这个辞旧迎新的时刻，能帮你实现一个愿望的是朋友<br/>
                实现两个的是亲戚，实现三个的是爱人<br/>
                实现五个的是父母<br/>
                而拓天速贷，帮你实现十个愿望！<br/>
                新年新气象，当然十全十美！<br/>
            </div>
        </div>

        <p class="activity-note">活动期间用户投资新年专享标，累计投资金额达到一个目标，即可打开一盏神灯，神灯会送你一个奖励，愿望奖励可累计领取。
            栗子：如拓小天在活动第一天投资5000元，最后一天投资10000元，则拓小天在活动期间共投资了15000元，可开两盏神灯，获得20元红包+爱奇艺会员月卡。</p>
    </div>

</div>
</@global.main>