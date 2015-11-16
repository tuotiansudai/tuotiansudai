<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.index}" pageJavascript="${js.index}" activeNav="首页" activeLeftNav="">
<div class="banner-box">
    <div class="page-width">
        <div class="register-ad-box fr tc">
            <em class="percent clearfix">16%</em>
            <b class="h-title clear-blank">最高年化收益率</b>
            <a class="btn-normal" href="/register/user">免费注册 </a>
            <i class="clearfix tr">已有账户？<a href="/login"> 立即登录</a></i>
        </div>
    </div>
</div>

<div class="main-advantage page-width">
    <dl>
        <dd>
            <a href="#" target="_blank">
            <img src="${staticServer}/images/icons/hs01.png" alt="超高收益 最低门槛" >
            <span class="clearfix">
                 <b class="clearfix">超高收益 最低门槛</b>
                最高46倍活期存款收益 <br/>
                    最低投资门槛1元

            </span>
            </a>
        </dd>
        <dd>
            <a href="#" target="_blank">
            <img src="${staticServer}/images/icons/hs02.png" alt="三方托管 放心理财">
            <span class="clearfix">
                <b class="clearfix">三方托管 放心理财</b>
                第三方银行托管 <br/>
                第三方支付
            </span>
                </a>
        </dd>
        <dd>
            <a href="#" target="_blank">
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
    <div class="bg-w clearfix page-width">
        <section class="product-box-list fl">
            <div class="product-box-inner">
                <#list loans as loan>
                <div class="product-box tc <#if loan.activityType=="NOVICE">new-standard</#if>">
                    <#if loan.activityType=='NOVICE'><i class="hot-new"></i></#if>
                    <div class="pad-m">
                        <h2 class="pr-title">${loan.name}</h2>
                        <div class="pr-square tc">
                            <div class="pr-square-in">
                                <em><b>${loan.baseRateInteger}</b><#if loan.baseRateFraction??>.${loan.baseRateFraction}</#if><#if loan.activityRateInteger??>+${loan.activityRateInteger}</#if><#if loan.activityRateFraction??>.${loan.activityRateFraction}</#if>%</em>
                                <i class="clearfix">年化收益</i>
                            </div>
                        </div>
                        <dl class="pr-info">
                            <dd><i>${loan.periods}</i>个月<br/><span>项目期限</span></dd>
                            <dd>${loan.amount}万<br/><span>项目金额</span></dd>
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
            <div class="tr clear-blank">
                <a href="/loan-list">更多>></a>
            </div>
        </section>
        <aside class="home-ad fr">
            <img src="${staticServer}/images/sign/ad-h.jpg">
        </aside>
    </div>
</div>
</@global.main>