<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.ump_account}" pageJavascript="${js.ump_account}" activeNav="我的账户" activeLeftNav="个人资料" title="个人资料">
<div class="content-container auto-height personal-info" id="personInfoBox">
    <h4 class="column-title"><em class="tc">个人资料</em>
    </h4>
    <ul class="info-list">
        <li><span class="info-title"> 实名认证</span>
                <em class="info">dfdf</em>
                <span class="binding-set"><i class="fa fa-check-circle ok"></i>已认证</span>

        </li>
        <li><span class="info-title"> 绑定银行卡</span>
            <#if bankCard??>
                <em class="info">${bankCard?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}</em>
                <span class="binding-set">
                    <i class="fa fa-check-circle ok"></i>已绑定<a class="setlink setBankCard" href="javascript:void(0)" id="update-bank-card" data-url="${requestContext.getContextPath()}/bind-card/replace">修改</a>
                </span>
            <#else>
                <em class="info">绑定银行卡后，您可以进行快捷支付和提现操作</em>
                <span class="binding-set">
                    <i class="fa fa-times-circle no"></i>未绑定<a class="setlink setBankCard" href="/bind-card">绑定</a>
                </span>
            </#if>
        </li>

        <#--<#if identityNumber??>-->
            <li><span class="info-title"> 支付密码</span>
                <em class="info">********</em>
                <span class="binding-set">
               <i class="fa fa-check-circle ok"></i>已设置<a class="setlink setUmpayPass" href="javascript:void(0);">重置</a>
            </span>
            </li>
        <#--</#if>-->

    </ul>

    <div class="column-box bg-w clearfix amount-sum">
        <#--<h3><b>账户总额：</b><span>${(((balance+freeze+expectedTotalCorpus+expectedTotalInterest)/100)?string('0.00'))!}元</span>-->
            <ul class="proList fr">
                <li class="fr"><a class="btn-normal" href="/recharge">充值</a></li>
                <li class="fr"><a class="btn-primary" href="/withdraw">提现</a></li>
            </ul>
        </h3>
    </div>

    <div class="column-box bg-w clearfix amount-sum ">
        <h3><b>可用余额：</b><span>33元</span> <i class="icon-has-con"></i> </h3>

        <ul class="detail-list">
            <li>提现冻结中：<span>3</span>元</li>
            <li>投资冻结中：<span>3</span>元</li>
        </ul>
    </div>

    <div class="column-box bg-w clearfix amount-sum ">
        <h3><b>累计收益：</b><span>3</span>元  <i class="icon-has-con"></i></h3>
        <ul class="detail-list">
            <li>已收投资收益：<span>3</span>元</li>
            <li>已收投资奖励：<span>3</span>元</li>
            <li>已收推荐奖励：<span>3</span>元</li>
            <li>已收优惠券奖励：<span>3</span>元</li>
            <li>已收体验金奖励：<span>3</span>元</li>
        </ul>
    </div>

    <div class="column-box bg-w clearfix amount-sum ">
        <h3> <b>待收回款：</b><span>3</span>元 <i class="icon-has-con"></i></h3>
        <ul class="detail-list">
            <li>待收投资本金：<span>3</span>元</li>
            <li>待收预期收益：<span>3</span>元</li>
            <li>待收投资奖励：<span>3</span>元</li>
            <li>待收优惠券奖励：<span>3</span>元</li>
            <li>待收体验金收益：<span>3</span>元</li>
        </ul>
    </div>
</div>

<div id="resetUmpayPassDOM" class="pad-m popLayer" style="display: none;">
    <form name="resetUmpayPasswordForm" id="resetUmpayPasswordForm">
        <dl class="identityCodeTitle" align="center">
            通过身份证号重置支付密码
        </dl>
        <dl>
            <dt class="requireOpt">请输入您的身份证号</dt>
            <dd><input type="text" id="identityNumber" name="identityNumber" class="input-control">
            </dd>
        </dl>
        <div class="error-box tc"></div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-normal">确认重置</button>
    </form>
</div>

<div id="successUmpayPass" class="pad-m popLayer" style="display: none;">
    <dl>
        <dt>您的支付密码已被重置，请注意查收相关短信，查看新密码！</dt>
    </dl>
    <button type="button" class="btn btn-normal" id="readUmpayPass">我已查看</button>
</div>


</@global.main>


