<div class="project-detail show-page" id="projectDetail" data-url="/loan/${loan.id?string.computer}/invests" style="display: none">
    <div class="menu-category">
        <span class="current"><a>项目材料</a></span>
        <span><a id="transaction_record">交易记录</a></span>
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
            <div id="box_content" data-url="/loan/${loan.id?string.computer}/invests">
                <div id="scroll">
                    <div id="content">
                        <script type="text/html" id="recordsTpl">

                                {{ each records}}
                                <div class="box-item">
                                    <dl>
                                        <dt><a href="#">{{$value.mobile}}</a> </dt>
                                        <dd>{{$value.createdTime}}</dd>
                                    </dl>
                                    <em class="amount">{{$value.amount}}元</em>
                                </div>
                                {{/each}}
                        </script>
                        <div id="pullUp"><span class="pullUpLabel">上拉加载更多</span></div>
                    </div>


                </div>


            </div>




        </div>



    </div>

</div>
</div>



