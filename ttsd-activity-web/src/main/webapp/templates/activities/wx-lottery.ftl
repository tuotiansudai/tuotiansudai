<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.wx_register}" pageJavascript="${js.wx_register}" activeNav="" activeLeftNav="" title="拓天速贷注册_用户注册_拓天速贷" keywords="拓天速贷,拓天速贷会员,拓天速贷注册，用户注册" description="拓天速贷会员注册为您提供规范、专业、安全有保障的互联网金融信息服务.">
<div class="activity-container" id="registerContainer" style="display:block">
	<div class="actor-content-group">
		<div class="wp clearfix">
			<div class="register-item">
				<div class="top-intro-img tc">
					<p class="big-title">
						恭喜您获得
					</p>
					<p class="intro-text">
						<span>50</span>元微信红包
					</p>
				</div>
	            <div class="reg-tag-current">
					<div class="register-common-frame" id="registerCommonFrame">
						<div class="landing-top">
						    <div class="landing-inner">
						        <div class="register-box">
						            <form class="register-user-form" action="/register/user" method="post" autocomplete="off" novalidate="novalidate" id="registerUserForm">
						                <ul class="reg-list tl register-step-one">
						                    <li>
						                        <label for="" class="reg-title">手机号:</label>
						                        <input validate type="text" id="mobile" name="mobile" class="mobile long" placeholder="手机号"
						                               maxlength="11" value="">
						                    </li>
						                    <li>
						                        <label for="" class="reg-title">密码:</label>
						                        <input validate type="password" id="password" name="password" placeholder="密码" maxlength="20"
						                               class="password long" value="">
						                    </li>

						                    <li class="code">
						                        <label for="" class="reg-title">验证码:</label>
						                        <em class="image-captcha">
						                            <img id="image-captcha-image" src="" alt=""/>
						                        </em>
						                        <span class="img-change">换一张</span>
						                        <input validate type="text" id="appCaptcha" name="appCaptcha" placeholder="验证码" maxlength="5"
						                               class="appCaptcha" value="">
						                    </li>
						                    <li id="appCaptchaErr" class="height appCaptchaErr"></li>
						                    <li>
						                        <label for="captcha" class="reg-title">手机验证码:</label>
						                            <span class="captcha-tag" id="pcCaptcha">
						                                <button type="button" class="fetch-captcha btn" disabled="disabled">获取验证码</button>
						                                <input validate type="text" class="captcha" autocomplete="off" name="captcha" id="captcha"
						                                       autocorrect="off" autocapitalize="off" placeholder="手机验证码" maxlength="6">
						                            </span>
						                    </li>
						                    <li class="agree-last">
						                        <input type="hidden" name="agreement" id="agreementInput" class="agreement-check" value="true">
						                        <label for="agreementInput" class="check-label">同意拓天速贷<a href="javascript:void(0);" class="show-agreement">《服务协议》</a></label>
						                    </li>
						                    <li  class="tc">
						                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						                        <input type="submit" class="register-user btn-normal-invest" value="立即注册">
						                    </li>
						                    <li class="tc mobile-agreement">
						                        <label>点击领取红包即同意拓天速贷<a href="javascript:void(0);" class="show-agreement">《服务协议》</a></label>
						                    </li>
						                </ul>
						            </form>

						        </div>
						    </div>
						</div>
					    <#include '../module/register-agreement.ftl' />
					</div>
	            </div>
			</div>
			<dl class="intro-group">
				<dt>
				</dt>
				<dd>
					<i class="icon-one"></i>
					<span>50元即可投资</span>
				</dd>
				<dd>
					<i class="icon-two"></i>
					<span>预期年化8%-11%</span>
				</dd>
				<dd>
					<i class="icon-three"></i>
					<span>30-360天</span>
				</dd>
				<dd>
					<i class="icon-four"></i>
					<span>CFCA权威认证</span>
				</dd>
				<dd>
					<i class="icon-five"></i>
					<span>第三方资金托管</span>
				</dd>
				<dd>
					<i class="icon-six"></i>
					<span>超额产权抵押</span>
				</dd>
			</dl>
		</div>
	</div>
</div>
<div class="getbag-container" id="getbagContainer" style="display:none">
	<p class="get-bag">
		<span id="getBag"></span>
	</p>
</div>
<div class="success-container" id="successContainer">
	<p class="success-title">
	</p>
	<p class="info-text">
		所有奖励已发放至您的账户
	</p>
	<p class="info-text">
		快来下载APP投资查看吧
	</p>
	<p class="logo-item">
		<span class="logo-icon"></span>
	</p>
	<p>
		<a href="javascript:void(0)" class="btn-item" id="btnExperience">
		</a>
	</p>
	<p class="footer-text">
		活动解释权归拓天速贷所有
	</p>
</div>
</@global.main>