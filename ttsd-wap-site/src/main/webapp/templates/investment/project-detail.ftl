<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'project_detail' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.project_detail}" pageJavascript="${js.project_detail}" title="项目详情">

<div class="project-detail" id="projectDetail">
    <div class="menu-category">
        <span class="current"><a href="#">项目材料</a></span>
        <span><a href="#">交易记录</a></span>
    </div>

    <div id="wrapperOut" class="loan-list-frame">
        <div class="loan-list-content" >

            <div class="category-detail" style="display: none">
                <#include 'mortgage-kind.ftl'>
            </div>

            <div class="category-detail transaction-record">
                <div class="record-top">
                    <span data-kind="xianfeng">
                        <i></i>
                        <b>拓天先锋  <em>138****5498</em></b>

                    </span>
                    <span data-kind="biaowang">
                        <i></i>
                        <b>拓天标王  <em>虚以待位</em></b>
                    </span>
                    <span data-kind="dingying">
                         <i></i>
                        <b>一锤定音  <em>虚以待位</em></b>
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


</@global.main>
