<#import "../macro/global-dev.ftl" as global>
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<#assign jsName = 'wx_lottery' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>


<@global.main pageCss="${css.wx_lottery}" pageJavascript="${js.wx_lottery}" activeNav="" activeLeftNav="" title="拓天速贷注册_用户注册_拓天速贷" keywords="拓天速贷,拓天速贷会员,拓天速贷注册，用户注册" description="拓天速贷会员注册为您提供规范、专业、安全有保障的互联网金融信息服务.">
<div class="activity-container" id="lanternFrame">
    <div class="top-intro-img img-item"></div>
    <div class="top-intro-img img-item-phone"></div>
    <div class="actor-content-group bg-one">
        <div class="wp clearfix">
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
                     <div class="gift-circle">
                    <div class="max-gift">
                        <#--id="pointerTdPhone"-->
                        <div class="pointer-img actor-finish"  data-is-login="<@global.isNotAnonymous>true</@global.isNotAnonymous>">
                            <img src="${staticServer}/activity/images/sign/actor/ranklist/pointer.png" width="100%" alt="pointer"/>
                        </div>
                        <div class="rotate-btn">
                            <img id="rotateTdPhone" src="${staticServer}/activity/images/sign/actor/ranklist/turntable.png"
                                 width="100%" alt="turntable"/>
                        </div>
                    </div>
                </div>
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
        </div>
    </div>

    <#include "../module/login-tip.ftl" />
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