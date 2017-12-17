<#--<#import "macro/global_m.ftl" as global>-->

<#--<@global.main pageCss="${m_css.project_detail}" pageJavascript="${m_js.project_detail}" title="项目详情">-->

<div class="project-detail" id="projectDetail" style="display: none">
    <div class="menu-category">
        <span class="current"><a href="#">项目材料</a></span>
        <span><a href="#">交易记录</a></span>
    </div>

    <div id="wrapperOut" class="loan-list-frame2">
        <div class="loan-list-content" >

            <div class="category-detail overview-content">
                <#include 'mortgage-kind_m.ftl'>
            </div>

            <div class="category-detail transaction-record overview-content" style="display: none">
                <div class="record-top">
                    <span data-kind="xianfeng">
                        <i></i>
                        <b>拓荒先锋  <em>${(loan.achievement.firstInvestAchievementMobile)!"虚以待位"}</em></b>

                    </span>
                    <span data-kind="biaowang">
                        <i></i>
                        <b>拓天标王  <em>${(loan.achievement.maxAmountAchievementMobile)!"虚以待位"}</em></b>
                    </span>
                    <span data-kind="dingying">
                         <i></i>
                        <b>一锤定音  <em>${(loan.achievement.lastInvestAchievementMobile)!"虚以待位"}</em></b>
                    </span>
                </div>

                <div class="record-note">称号奖励将于标满放款后发放</div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

                <div class="box-item">
                    <dl>
                        <dt><a href="#">138****5498</a> </dt>
                        <dd>2016-11-23  23:19:36</dd>
                    </dl>
                    <em class="amount">2,000.00元</em>
                </div>

            </div>
        </div>



    </div>

</div>


<#--</@global.main>-->
