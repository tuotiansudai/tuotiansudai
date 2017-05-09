<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.birthday_month}" pageJavascript="${js.birthday_month}" activeNav="" activeLeftNav="" title="生日月特权_拓天活动_拓天速贷" keywords="拓天速贷,拓天活动.生日活动,生日月特权" description="拓天速贷专属生日月特权,生日月投资收益翻倍,拓天速贷专属活动超高收益等你拿.">

<div class="image-banner-slide"></div>
<div class="birth-month-container">

        <img src="${commonStaticServer}/activity/images/birthday-month/sc01.png" alt="生日想要什么礼物" class="sc-list" style="margin-top: 30px;">
        <img src="${commonStaticServer}/activity/images/birthday-month/sc02.png" alt="统统都需要" class="sc-list">
        <div class="product-list clearfix" id="productListFrame">
            <i class="arrow-left"></i>
            <i class="arrow-right"></i>
            <div class="product-box">
                <ul>
                    <li>
                        <img src="${commonStaticServer}/activity/images/birthday-month/pr01.png" alt="90天产品">
                        <#if !isAppSource>
                            <a href="/loan-list?durationStart=31&durationEnd=90&name=&status=&rateStart=0&rateEnd=0" ></a>
                        <#else>
                            <a href="/loan-list?productType=WYX"></a>
                        </#if>

                    </li>
                    <li>
                        <img src="${commonStaticServer}/activity/images/birthday-month/pr02.png" alt="180天产品">
                        <#if !isAppSource>
                            <a href="/loan-list?durationStart=91&durationEnd=180&name=&status=&rateStart=0&rateEnd=0"></a>
                        <#else>
                            <a href="/loan-list?productType=JYF"></a>
                        </#if>
                    </li>
                    <li>
                        <img src="${commonStaticServer}/activity/images/birthday-month/pr03.png" alt="360天产品">
                        <#if !isAppSource>
                            <a href="/loan-list?durationStart=181&durationEnd=366&name=&status=&rateStart=0&rateEnd=0"></a>
                        <#else>
                            <a href="/loan-list?productType=JYF"></a>
                        </#if>
                    </li>
                </ul>
            </div>

        </div>
        <div class="tc btn-activity">
            <img src="${commonStaticServer}/activity/images/birthday-month/btn-activity0308.png" alt="拓天速贷活动">
        </div>
        <div class="activity-notice">
            1.本活动适用于平台注册用户生日当月（以绑定的身份证为准）； <br/>
            2.活动期间投资产品享受首月收益加倍福利；<br/>
            3.翻倍所得收益，体现在该笔投资项目收益中，可在“我的账户”中查询;<br/>
            4.本次活动不限买入金额，不限购买笔数，多买多得;<br/>
            5.本次活动不可与平台其他优惠券同时使用。<br/>
            活动遵循拓天速贷法律声明，最终解释权归拓天速贷平台所有。<br/>
        </div>


</div>
</@global.main>