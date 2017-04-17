<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.heavy_benefits}" pageJavascript="${js.heavy_benefits}" activeNav="" activeLeftNav="" title="邀好友活动_活动中心_拓天速贷" keywords="拓天速贷,邀请好友,话费奖励,京东E卡" description="拓天速贷活动期间邀请好友注册投资,当所有受邀好友们在活动期间首投或累计投资达到指定金额,邀请人领取最高220元话费奖励+3000元京东E卡.">

<div class="tour-slide" id="topHeader"></div>

<div class="heavy-benefits-container" id="heavyBenefits">
    <div class="box-column bill-column">
        <i class="border-top-col"></i>
        <i class="border-bottom-col"></i>
        <span class="title-one"></span>
        <em class="sector">
            <i class="top-border"></i>
            <span class="pr-mobile"></span>
        </em>
        <p>活动期间邀请好友完成首次投资，根据首投金额≥5000元的受邀好友人数，邀请人可领取<b>最高220元话费奖励</b>（话费金额可累计获得）。</p>
        <span class="note-text">首投<b>≥5000+</b>元好友人数我已达到 <b class="number" id="totalRewardCalls">${referrerCount}</b> 人</span>

        <div class="investment-box-pc clearfix">

            <div class="inner-progress"></div>
            <div class="inner-progress-active" id="progressReward"></div>

            <span class="step-option step-one <#if referrerCount?? && referrerCount gte 2>current</#if> ">
                    <i class="square "></i>
                    <em class="step-text">达到2人<br/>
                    领取30元话费</em>
            </span>

            <span class="step-option step-two <#if referrerCount?? && referrerCount gte 4>current</#if> ">
                    <i class="square"></i>
                     <em class="step-text">达到4人<br/>再领取40元话费</em>
                </span>

            <span class="step-option step-three <#if referrerCount?? && referrerCount gte 6>current</#if> ">
                    <i class="square "></i>
                    <em class="step-text">达到6人<br/>
再领取50元话费</em>
                </span>

            <span class="step-option step-four <#if referrerCount?? && referrerCount gte 9>current</#if> ">
                    <i class="square "></i>
                    <em class="step-text">达到9人<br/>
再领取60元话费</em>
                </span>

            <span class="step-option step-five <#if referrerCount?? && referrerCount gte 11>current</#if> ">
                     <i class="square "></i>
                    <em class="step-text">达到11人<br/>
再领取40元话费</em>
                </span>

        </div>

        <div class="investment-box-mobile">
            <div class="align-box">
                <div class="inner-progress"></div>
                <div class="inner-progress-active"></div>
                <span class="step-option step-one <#if referrerCount?? && referrerCount gte 2>current</#if>">
                    <i class="square "></i>
                    <em class="step-text">达到2人<br/>
                    领取30元话费</em>
            </span>

                <span class="step-option step-two <#if referrerCount?? && referrerCount gte 4>current</#if>">
                    <i class="square"></i>
                     <em class="step-text">达到4人<br/>再领取40元话费</em>
                </span>
            </div>

            <div class="align-box">
                <div class="inner-progress"></div>
                <div class="inner-progress-active"></div>
                <span class="step-option step-three <#if referrerCount?? && referrerCount gte 6>current</#if>">
                    <i class="square "></i>
                    <em class="step-text">达到6人<br/>
再领取50元话费</em>
                </span>

            </div>


            <div class="align-box">
                <div class="inner-progress"></div>
                <div class="inner-progress-active"></div>
                <span class="step-option step-four <#if referrerCount?? && referrerCount gte 9>current</#if>">
                    <i class="square "></i>
                    <em class="step-text">达到9人<br/>
再领取60元话费</em>
                </span>

                <span class="step-option step-five <#if referrerCount?? && referrerCount gte 11>current</#if>">
                     <i class="square "></i>
                    <em class="step-text">达到11人<br/>
再领取40元话费</em>
                </span>
            </div>

        </div>

        <div class="customized-button">
            <a href="/referrer/refer-list" class="btn-normal invite-friend">立即邀请好友</a>
        </div>

    </div>

    <div class="box-column JD-CARD">
        <i class="border-top-col"></i>
        <i class="border-bottom-col"></i>
        <span class="title-two"></span>
        <em class="sector">
            <i class="top-border"></i>
            <span class="pr-card"></span>
        </em>

        <p>活动期间邀请好友注册投资，当所有受邀好友们在活动期间累计投资达到指定金额，您可获得<b>最高3000元的京东E卡</b>（奖品不可累计获得）。</p>
        <span class="note-text">我的好友们已累计投资  <b class="jd-card-amount">${referrerSumInvestAmount}</b>  元 </span>

        <div class="investment-box-pc clearfix">

            <div class="inner-progress"></div>
            <div class="inner-progress-active" id="progressJDCard"></div>
            <span class="step-option step-one">
                    <i class="square <#if referrerSumInvestAmount gte 50000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 50000 && referrerSumInvestAmount lt 100000>current</#if>"><i class="count">50000</i> <br/>
100元京东E卡</em>
                </span>

            <span class="step-option step-two ">
                    <i class="square <#if referrerSumInvestAmount gte 100000>current</#if>"></i>
                     <em class="step-text <#if referrerSumInvestAmount gte 100000 && referrerSumInvestAmount lt 200000>current</#if>"><i class="count">100000</i> <br/>
200元京东E卡</em>
                </span>

            <span class="step-option step-three">
                    <i class="square <#if referrerSumInvestAmount gte 200000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 200000 && referrerSumInvestAmount lt 300000>current</#if>"><i class="count">200000</i> <br/>
400元京东E卡</em>
                </span>

            <span class="step-option step-four">
                    <i class="square <#if referrerSumInvestAmount gte 300000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 300000 && referrerSumInvestAmount lt 500000>current</#if>"><i class="count">300000</i> <br/>
600元京东E卡</em>
                </span>

            <span class="step-option step-five">
                     <i class="square <#if referrerSumInvestAmount gte 500000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 500000 && referrerSumInvestAmount lt 800000>current</#if>"><i class="count">500000</i> <br/>
1000元京东E卡</em>
                </span>

            <span class="step-option step-six">
                     <i class="square <#if referrerSumInvestAmount gte 800000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 800000 && referrerSumInvestAmount lt 1000000>current</#if>"><i class="count">800000</i> <br/>
1600元京东E卡</em>
                </span>

            <span class="step-option step-seven">
                     <i class="square <#if referrerSumInvestAmount gte 1000000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 1000000>current</#if>"><i class="count">1000000</i> <br/>
3000元京东E卡</em>
                </span>

        </div>

        <div class="investment-box-mobile">

            <div class="align-box">
                <div class="inner-progress"></div>
                <div class="inner-progress-active"></div>
                <span class="step-option step-one">
                    <i class="square <#if referrerSumInvestAmount gte 50000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 50000 && referrerSumInvestAmount lt 100000>current</#if>"><i class="count">50000</i> <br/>
100元京东E卡</em>
                </span>

                <span class="step-option step-two ">
                    <i class="square <#if referrerSumInvestAmount gte 100000>current</#if>"></i>
                     <em class="step-text <#if referrerSumInvestAmount gte 100000 && referrerSumInvestAmount lt 200000>current</#if>"><i class="count">100000</i> <br/>
200元京东E卡</em>
                </span>
            </div>

            <div class="align-box">
                <div class="inner-progress"></div>
                <div class="inner-progress-active"></div>
                <span class="step-option step-three">
                    <i class="square <#if referrerSumInvestAmount gte 200000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 200000 && referrerSumInvestAmount lt 300000>current</#if>"><i class="count">200000</i> <br/>
400元京东E卡</em>
                </span>
            </div>

            <div class="align-box">
                <div class="inner-progress"></div>
                <div class="inner-progress-active"></div>
                <span class="step-option step-four">
                    <i class="square <#if referrerSumInvestAmount gte 300000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 300000 && referrerSumInvestAmount lt 500000>current</#if>"><i class="count">300000</i> <br/>
600元京东E卡</em>
                </span>
            </div>

            <div class="align-box">
                <div class="inner-progress"></div>
                <div class="inner-progress-active"></div>
                <span class="step-option step-five">
                     <i class="square <#if referrerSumInvestAmount gte 500000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 500000 && referrerSumInvestAmount lt 800000>current</#if>"><i class="count">500000</i> <br/>
1000元京东E卡</em>
                </span>
                </div>

            <div class="align-box">
                <div class="inner-progress"></div>
                <div class="inner-progress-active"></div>
                <span class="step-option step-six">
                     <i class="square <#if referrerSumInvestAmount gte 800000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 800000 && referrerSumInvestAmount lt 1000000>current</#if>"><i class="count">800000</i> <br/>
1600元京东E卡</em>
                </span>

                <span class="step-option step-seven">
                     <i class="square <#if referrerSumInvestAmount gte 1000000>current</#if>"></i>
                    <em class="step-text <#if referrerSumInvestAmount gte 1000000>current</#if>"><i class="count">1000000</i> <br/>
3000元京东E卡</em>
                </span>
            </div>

            </div>

        <div class="customized-button">
            <a href="/referrer/refer-list" class="btn-normal invite-friend">立即邀请好友</a>
        </div>

    </div>

    <dl class="note-box">
        <dt>温馨提示：</dt>
        <dd>
            1.本活动遵循一级推荐奖励，仅累计推荐人直接邀请投资的好友，如推荐人邀请的好友再次邀他人进行投资，其投资额不计入间接推荐人的奖励累计； <br/>
            2.本活动仅限直投项目，债权转让不参与累计； <br/>
            3.奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励； <br/>
            4.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格； <br/>
            5.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。 <br/>
        </dd>

    </dl>
</div>
</@global.main>