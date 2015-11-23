<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.index}" pageJavascript="${js.index}" activeNav="首页" activeLeftNav="">
<div class="banner-box">
    <div class="banner-img-list">
        <a href="/activity/ranking" target="_blank">
            <img src="${staticServer}/images/sign/activities/ranking/qph.jpg" alt="抢排行，送大礼">
        </a>
        <a href="/activity/grand" target="_blank">
            <img src="${staticServer}/images/sign/activities/grand/ad2.jpg" alt="累计收益兑大奖">
        </a>
         <a href="/activity/recruit" target="_blank">
             <img src="${staticServer}//images/sign/activities/daili/ad.jpg" alt="招募代理">
         </a>
    </div>
    <ul class="scroll-num">
        <li class="selected"></li>
        <li></li>
        <li></li>
    </ul>

    <div class="page-width">
        <@global.isAnonymous>
        <div class="register-ad-box fr tc">
            <em class="percent clearfix">16%</em>
            <b class="h-title clear-blank">最高年化收益率</b>
            <a class="btn-normal" href="/register/user">免费注册 </a>
            <i class="clearfix tr">已有账户？<a href="/login"> 立即登录</a></i>
        </div>
        </@global.isAnonymous>
    </div>
</div>
<div class="main-advantage page-width">
    <dl>
        <dd>
            <a href="/about/assurance" target="_blank">
            <img src="${staticServer}/images/icons/hs01.png" alt="超高收益 最低门槛" >
            <span class="clearfix">
                 <b class="clearfix">超高收益 最低门槛</b>
                最高46倍活期存款收益 <br/>
                    最低投资门槛1元
            </span>
            </a>
        </dd>
        <dd>
            <a href="/about/assurance" target="_blank">
            <img src="${staticServer}/images/icons/hs02.png" alt="三方托管 放心理财">
            <span class="clearfix">
                <b class="clearfix">三方托管 放心理财</b>
                第三方银行托管 <br/>
                第三方支付
            </span>
                </a>
        </dd>
        <dd>
            <a href="/about/assurance" target="_blank">
            <img src="${staticServer}/images/icons/hs03.png" alt="实力雄厚 安全保障">
            <span class="clearfix">
                <b class="clearfix">实力雄厚 安全保障</b>
                上市企业投资 <br/>
                稳定安全债权来源
            </span>
                </a>
        </dd>
    </dl>
</div>

<div class="home-content">
    <div class="clearfix page-width">
        <section class="product-box-list fl">
            <div class="product-box-inner">
                <#list loans as loan>
                <div class="product-box tc <#if loan.activityType=="NEWBIE">new-standard</#if>">
                    <#if loan.activityType=='NEWBIE'><i class="hot-new"></i></#if>
                    <div class="pad-m" title="${loan.name}" data-url="/loan/${loan.id?string.computer}">
                        <h2 class="pr-title">${loan.name}</h2>
                        <div class="pr-square tc">
                            <div class="pr-square-in">
                                <em><b>${loan.baseRateInteger}</b><#if loan.baseRateFraction??>.<@percentFraction>${loan.baseRateFraction}</@percentFraction></#if><#if loan.activityRateInteger??>+${loan.activityRateInteger}</#if><#if loan.activityRateFraction??>.<@percentFraction>${loan.activityRateFraction}</@percentFraction></#if>%</em>
                                <i>年化收益</i>
                            </div>
                        </div>
                        <dl class="pr-info">
                            <dd class="dl-month"><i>${loan.periods}</i>个月<br/><span>项目期限</span></dd>
                            <dd class="dl-amount"><i><@amount>${loan.amount}</@amount>元</i><br/><span>项目金额</span></dd>
                        </dl>
                        <div class="project-schedule clear-blank">
                            <div class="p-title">
                                <span class="fl">项目进度</span>
                                <span class="point fr">${loan.progress}%</span>
                            </div>
                            <div class="process-percent">
                                <div class="percent" style="width:${loan.progress}%"></div>
                            </div>
                        </div>  
                    </div>
                    <#if loan.status=="RAISING">
                    <a href="/loan/${loan.id?string.computer}" class="btn-normal">立即投资</a>
                    <#elseif loan.status=="PREHEAT">
                    <a href="/loan/${loan.id?string.computer}" class="btn-normal wait-invest">预热中</a>
                    <#else>
                    <button type="button" disabled class="btn-normal">已售罄</button>
                    </#if>
                </div>
                </#list>
            </div>
            <div class="clear-blank invest-total">
                <span class="fl">投资人数:<i>190000</i> 人</span>
                <a href="/loan-list" class="fr">更多>></a>
            </div>
        </section>
        <aside class="home-ad fr">
            <a href="/activity/ranking" target="_blank"> <img src="${staticServer}/images/sign/ad-h.jpg"></a>
        </aside>
    </div>
</div>
</@global.main>