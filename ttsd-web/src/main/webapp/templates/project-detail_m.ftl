<div class="project-detail show-page" id="projectDetail" data-url="/loan/${loan.id?string.computer}/invests" style="display: none">
    <div class="m-header"><em id="iconProjectDetail" class="icon-left"><i></i></em>项目详情 </div>
    <div class="menu-category">
        <span class="current"><a>项目材料</a></span>
        <span><a id="transaction_record">交易记录</a></span>
    </div>

    <div id="wrapperOut" class="loan-list-frame2">
        <div class="loan-list-content" >

            <div class="category-detail overview-content">
                <#include 'mortgage-kind_m.ftl'>
            </div>

            <div class="category-detail2 transaction-record overview-content" style="display: none">

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

                    </div>
                    <div id="noData" style="display: none">没有更多数据了</div>


                </div>


            </div>




        </div>



    </div>

</div>

</div>



