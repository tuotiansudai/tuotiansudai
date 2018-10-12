<div id="redEnvelopFloatFrame">
<div class="count-form">
	<h3 class="hander">
		<span>出借计算器</span>
		<i class="close-count"></i>
	</h3>
	<form id="countForm" action="" class="clearfix">
		<div class="form-text">
			<input type="text" placeholder="出借金额" class="int-text" name="money" id="moneyNum">
			<span class="unit-text">元</span>
		</div>
		<div class="form-text">
			<input type="text" placeholder="出借期限" class="int-text" name="day" id="dayNum">
			<span class="unit-text">天</span>
		</div>
		<div class="form-text">
			<input type="text" placeholder="年化利率" class="int-text" name="rate" id="rateNum">
			<span class="unit-text">%</span>
		</div>
		<div class="form-text">
			<div class="error-box"></div>
			<input class="submit-btn" type="submit" value="计算">
			<input class="reset-btn" type="reset" value="重置" id="resetBtn">
		</div>
	</form>
	<div class="form-text clearfix">
		<p class="result-text">本息合计 <span id="resultNum">0</span> 元</p>
		<p class="tip-text">计算结果仅供参考，以实际收益为准</p>
	</div>
</div>
<ul class="fix-nav-list">

	<#if schoolSeason??>
        <#if drawTime==1 >
            <li class="draw-today-one">
                <a href="activity/school-season" target="_blank" class="sign-school-open"></a>
            </li>
        <#else>
            <li class="draw-today-two">
                <a href="activity/school-season?school=yes" target="_blank" class="sign-school-rank"></a>
            </li>
        </#if>
    </#if>

    <li class="cal-btn">
            <i class="icon-calculator"></i>
            <span class="nav-text">出借<br/>计算器</span>
        </li>
	<li>
		<i class="icon-phone"></i>
		<span class="nav-text">APP<br/>下载</span>
		<div class="fix-nav app-img"></div>
	</li>
	<li class="show-feed">
		<i class="icon-edit"></i>
		<span class="nav-text">意见<br/>反馈</span>
	</li>
</ul>
<ul class="back-top">
	<li>
		<i class="icon-top"></i>
		<span class="nav-text">返回<br/>顶部</span>
	</li>
</ul>
<div class="feedback-container feedback-model" id="feedbackConatiner">
	<div class="feed-top">
		<h3>意见反馈</h3>
		<div class="feed-close"></div>
	</div>
	<div class="feed-content">
	<form action="#" id="feedForm">
		<div class="content-list">
			<label class="name-text">反馈类型：</label>
			<dl class="type-list">
				<dt data-type="opinion">意见</dt>
				<dd data-type="opinion">意见</dd>
				<dd data-type="complain">投诉</dd>
				<dd data-type="consult">咨询</dd>
				<dd data-type="other">其他</dd>
				<i class="fa fa-sort-desc" aria-hidden="true"></i>
			</dl>
		</div>
		<div class="content-list">
			<input type="hidden" name="type" >
			<textarea class="text-area int-text" name="content" placeholder="欢迎反馈您遇到的问题，或者想要的功能（文字限制在14～200字）" maxlength="200" id="textArea"></textarea>
		</div>
		<div class="content-list">
			<label class="name-text">联系方式：</label>
			<input type="text" class="phone-text ignore int-text" name="contact" value="" id="phoneText" maxlength="11">
		</div>
		<div class="content-list">
			<label class="name-text">验证码：</label>
            <img src="" id="imageCaptchaFeed">
			<input type="text" class="code-text int-text" name="captcha" value="" id="captchaText" maxlength="5">
		</div>
		<div class="content-list tc">
			<div class="error-box"></div>
			<input type="submit" class="btn submit-btn" value="提交意见">
		</div>
	</form>
	</div>
</div>
<div class="feed-tip feedback-model" id="feedbackModel">
	<div class="tip-top">
		<h3>意见反馈</h3>
		<div class="feed-close"></div>
	</div>
	<div class="tip-content">
		<div class="content-list">
			亲：
		</div>
		<div class="content-list tc mt-10">
			您的反馈我们已经收到，感谢您对拓天速贷的支持！
		</div>
		<div class="content-list tc btn-model">
			<a href="javascript:void(0)" class="btn feed-close">确定</a>
		</div>
	</div>
</div>

</div>
