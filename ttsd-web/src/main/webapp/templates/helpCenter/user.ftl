<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.help_center}" pageJavascript="${js.help_center}" activeNav="帮助中心" activeLeftNav="账户管理" title="拓天速贷公司介绍_拓天理念_拓天资质_拓天速贷" keywords="拓天速贷,拓天速贷公司,拓天资质,拓天价值" description="拓天速贷以透明、公平、高效为原则,为有贷款需求的小微企业及有投资需求的个人提供规范、安全、专业的互联网金融信息服务.">
    <#include "./helpLeftMenu.ftl"/>
<div class="help-center-group">
    <h3>
        <a href="/help/help-center"><< 返回帮助中心</a>
    </h3>

    <div class="problem-list-group">
        <div class="problem-title-item">
            <span class="active"><a href="/help/user#11">密码设置</a></span>
            <#--<span>优惠券</span>-->
            <span><a href="/help/user#21">VIP会员</a></span>
            <span><a href="/help/user#31">银行卡认证及更换</a></span>
        </div>
        <div class="problem-content-item">
            <ul class="list-group active" id="passwordList">

            </ul>
            <#--<ul class="list-group">-->
                <#--<li class="problem-single-item" >-->
                    <#--<i class="fa fa-angle-down"></i>-->

                    <#--<p class="single-title">1、红包、加息券使用时间一般是多久？</p>-->

                    <#--<p class="single-answer">答：自发放日起30天。</p>-->
                <#--</li>-->
                <#--<li class="problem-single-item">-->
                    <#--<i class="fa fa-angle-down"></i>-->

                    <#--<p class="single-title">2、怎么使用红包、加息券？</p>-->

                    <#--<p class="single-answer">答：登录官方网站—选择标的进行投资—输入投资金额—勾选优惠劵（投资金额下方）—勾选红色优惠劵（红色为您可使用优惠劵）。</p>-->
                <#--</li>-->
                <#--<li class="problem-single-item">-->
                    <#--<i class="fa fa-angle-down"></i>-->

                    <#--<p class="single-title">3、怎么将兑换码兑换成投资红包？</p>-->

                    <#--<p class="single-answer">答：登录官方网站—我的账户—我的宝藏—输入兑换码（右上角），进行兑换。</p>-->
                <#--</li>-->
                <#--<li class="problem-single-item">-->
                    <#--<i class="fa fa-angle-down"></i>-->

                    <#--<p class="single-title">4、使用后的优惠券（红包、体验券、加息券），我在哪里可以看到？</p>-->

                    <#--<p class="single-answer">答：登录官方网站—我的账户—我的宝藏—未使用/已使用/已过期，点击“已使用”即可。</p>-->
                <#--</li>-->
            <#--</ul>-->
            <ul class="list-group" id="vipList">
            </ul>
            <ul class="list-group" id="bankAccountList">
            </ul>
        </div>
    </div>
</div>
</@global.main>
