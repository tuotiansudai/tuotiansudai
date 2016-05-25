<div class="count-form">
	<h3 class="hander">
		<span>投资计算器</span>
		<i class="close-count"></i>
	</h3>
	<form id="countForm" action="">
		<div class="form-text">
			<input type="text" placeholder="投资金额" class="int-text" name="money" id="moneyNum">
			<span class="unit-text">元</span>
		</div>
		<div class="form-text">
			<input type="text" placeholder="投资时长" class="int-text" name="month" id="monthNum">
			<span class="unit-text">期</span>
		</div>
		<div class="form-text">
			<input type="text" placeholder="年化利率" class="int-text" name="bite" id="biteNum">
			<span class="unit-text">%</span>
		</div>
		<div class="form-text">
			<input class="submit-btn" type="submit" value="计算">
			<input class="reset-btn" type="reset" value="重置" id="resetBtn">
		</div>
	</form>
	<div class="form-text">
		<p class="result-text">本息合计 <span id="resultNum">0</span> 元</p>
		<p class="tip-text">计算结果仅供参考，以实际收益为准</p>
	</div>
</div>
<ul class="fix-nav-list">
	<li class="cal-btn">
		<i class="icon-calculator"></i>
		<span class="nav-text" onclick="cnzzPush.trackClick('62首页','悬浮窗模块','计算器')">投资<br/>计算器</span>
	</li>
	<li>
		<i class="icon-phone"></i>
		<span class="nav-text" onclick="cnzzPush.trackClick('63首页','悬浮窗模块','手机APP')">APP<br/>下载</span>
		<div class="fix-nav app-img"></div>
	</li>
	<li>
		<i class="icon-qq"></i>
		<a href="tencent://message/?Menu=yes&amp;uin=800036446&amp;Service=58&amp;SigT=A7F6FEA02730C988DA2483CA6903A080CC7905D38ECC80415E377A30894437533088F17869028B5CF9675930F948083A76B63C5509B87F7EC6D325A5AC42065850DEB02195B1A62893F12BB501EFB8062A12A1EC83EA4837B54D2A32184DC73715B1B8246CA1C8CE93962A2D19C00D74096B80DB3886760B&amp;SigU=30E5D5233A443AB2258601C95FEA1C12147CDD66AEBDE12C0FF13E859174956236CF419B4F779D9F7E6649DCEB6A0C0D2A42D4EEF76E7C429E51CFEEEB22AAC2FBCC4F686D2CFE3D" onclick="cnzzPush.trackClick('64首页','悬浮窗模块','QQ')" target="_blank"><span class="nav-text">在线<br/>沟通</span></a>
	</li>
	<li class="show-feed">
		<i class="icon-edit"></i>
		<span class="nav-text">意见<br/>反馈</span>
	</li>
</ul>
<ul class="back-top">
	<li>
		<i class="icon-top"></i>
		<span class="nav-text" onclick="cnzzPush.trackClick('65首页','悬浮窗模块','回到顶部')">返回<br/>顶部</span>
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
			<textarea class="text-area" name="content" placeholder="欢迎反馈您遇到的问题，或者想要的功能（文字限制在14～200字）" id="textArea"></textarea>
		</div>
		<div class="content-list">
			<label class="name-text">联系方式：</label>
			<input type="text" class="phone-text ignore" name="contact" value="" id="phoneText">
		</div>
		<div class="content-list">
			<label class="name-text">验证码：</label>
			<input type="text" class="code-text" name="captcha" value="" id="captchaText">
			<img src="/feedback/captcha" id="captcha">
			<label class="error captcha-error" id="captchaError">验证码错误！</label>
		</div>
		<div class="content-list tc">
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
