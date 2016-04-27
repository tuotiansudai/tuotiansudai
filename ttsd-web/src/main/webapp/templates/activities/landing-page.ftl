<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.landingpage}" pageJavascript="${js.landingpage}" activeNav="" activeLeftNav="" title="拓天速贷手机客户端_理财客户端_拓天速贷" keywords="拓天速贷,APP理财,移动客户端,网络理财,P2P理财,拓天速贷APP" description="拓天速贷手机客户端为理财,投资,贷款等投融资用户提供安全、专业、便捷的互联网金融理财服务。">

<div class="landing-container">
	<div class="landing-top">
        <img src="${staticServer}/images/sign/actor/landingpage/landing-top.png" width="100%">
        <div class="register-box">
            <form class="register-user-form" action="/register/user" method="post" autocomplete="off" novalidate="novalidate">
                <ul class="reg-list tl register-step-one">
                    <li >
                        <label for="" class="reg-title">用户名:</label>
                        <input type="text" class="login-name long" name="loginName" placeholder="用户名" maxlength="25" value="">
                    </li>
                    <li>
                        <label for="" class="reg-title">密码:</label>
                        <input type="password" name="password" placeholder="密码" maxlength="20" class="password long" value="">
                    </li>
                    <li>
                        <label for="" class="reg-title">手机号:</label>
                        <input type="text" name="mobile" class="mobile long" placeholder="手机号" maxlength="11" value="">
                    </li>
                    <li class="code">
                        <label for="" class="reg-title">验证码:</label>
                        <input type="text" name="referrer" placeholder="验证码" maxlength="25" class="referrer" value="">
                        <em class="image-captcha">
                            <img src="/login/captcha" alt=""/>
                        </em>
                        <span class="img-change">换一张</span>
                    </li>
                    <li>
                        <label for="" class="reg-title">手机验证码:</label>
                    <span class="captcha-tag">
                        <input type="text" name="captcha" class="captcha" autocomplete="off" autocorrect="off" autocapitalize="off" placeholder="手机验证码" maxlength="6" value="">
                        <button type="button" class="fetch-captcha btn" disabled="disabled">获取验证码</button>
                    </span>

                    </li>


                    <li class="agree-last">
                        <label for="agreement" class="check-label">同意拓天速贷<a href="javascript:void(0);" class="show-agreement">《服务协议》</a></label>
                        <input type="checkbox" name="agreement" id="agreementInput" class="agreement-check">

                    </li>
                    <li class="error-box"></li>
                    <input type="hidden" name="_csrf" value="010e19c3-6b6e-4f43-bbfd-186c3970e9b8">


                    <input type="submit" class="register-user" value="立即注册">
                </ul>

            </form>
        </div>
    </div>
    <div class="content-one">
    	<div class="one-title">
    		拓天速贷的优势
    	</div>
    	<div class="content-wp">
    		<ul>
    			<li>
					 <a href="/about/assurance?aid=1" onclick="cnzzPush.trackClick('28首页','安全保障模块','1')" target="_blank">
						<div class="icon-one"></div>
						 <span class="one-text">
							 <b class="clearfix">稳健收益</b>
							预期年化收益可达13%
						</span>
					</a>
    			</li>
    			<li>
                    <a href="/about/assurance?aid=2" onclick="cnzzPush.trackClick('29首页','安全保障模块','2')" target="_blank">
                        <div class="icon-two"></div>
						<span class="clearfix">
							<b class="clearfix">抵押模式</b>
							公认的安全抵押债权
						</span>
                    </a>
				</li>
    			<li>
    				<a href="/about/assurance?aid=3" onclick="cnzzPush.trackClick('30首页','安全保障模块','3')" target="_blank">
                    <div class="icon-three"></div>
                    <span class="clearfix">
                        <b class="clearfix">资金透明</b>
                        第三方资金托管
                    </span>
                </a>
    			</li>
    		</ul>
    	</div>
    </div>
	<div class="content-two">
        <div class="two-title">
            为您精选投资产品
        </div>
		<div class="product-wp">
			<div class="product-box-list fl">
                <div class="product-box-inner">

                        <div class="product-box tc product-type active-after">
                            <i class="img-syl"></i>
                            <div class="pad-m" title="新手专享" data-url="/loan/31020002818144" onclick="cnzzPush.trackClick('36首页','热门产品模块','速盈利')">
                                <h2 class="pr-title">新手专享</h2>
                                <div class="pr-square tc">
                                    <div class="pr-square-in">
                                        <em><b>10</b>
                                            +1
                                                %</em>
                                        <i>预期年化收益</i>
                                    </div>
                                </div>
                                <dl class="pr-info">
                                    <dd>投资期限30天</dd>

                                </dl>

                            </div>
                                <div class="time-item preheat" data-time="-342358">
                                        <i class="time-clock"></i><strong class="minute_show">00分</strong><em>:</em><strong class="second_show">00秒</strong>以后可投资
                                </div>

                                <a href="/loan/31020002818144" class="btn-normal now">立即查看</a>
                                <a href="/loan/31020002818144" class="btn-normal wait-invest will">预热中</a>
                        </div>

                        <div class="product-box tc product-type active-after">
                            <i class="img-wyx"></i>
                            <div class="pad-m" title="稳健灵活" data-url="/loan/32448033321072" onclick="cnzzPush.trackClick('37首页','热门产品模块','稳盈秀')">
                                <h2 class="pr-title">稳健灵活</h2>
                                <div class="pr-square tc">
                                    <div class="pr-square-in">
                                        <em><b>12</b>
                                            %</em>
                                        <i>预期年化收益</i>
                                    </div>
                                </div>
                                <dl class="pr-info">
                                    <dd>投资期限90天</dd>

                                </dl>

                            </div>
                                <div class="time-item preheat" data-time="-338758">
                                        <i class="time-clock"></i><strong class="minute_show">00分</strong><em>:</em><strong class="second_show">00秒</strong>以后可投资
                                </div>

                                <a href="/loan/32448033321072" class="btn-normal now">立即查看</a>
                                <a href="/loan/32448033321072" class="btn-normal wait-invest will">预热中</a>
                        </div>

                        <div class="product-box tc product-type active-after">
                            <i class="img-jyf"></i>
                            <div class="pad-m" title="财富法宝" data-url="/loan/31903021266032" onclick="cnzzPush.trackClick('38首页','热门产品模块','久赢富')">
                                <h2 class="pr-title">财富法宝</h2>
                                <div class="pr-square tc">
                                    <div class="pr-square-in">
                                        <em><b>13</b>
                                            +20
                                                %</em>
                                        <i>预期年化收益</i>
                                    </div>
                                </div>
                                <dl class="pr-info">
                                    <dd>投资期限180天</dd>

                                </dl>

                            </div>
                                <div class="time-item preheat" data-time="-338158">
                                        <i class="time-clock"></i><strong class="minute_show">00分</strong><em>:</em><strong class="second_show">00秒</strong>以后可投资
                                </div>

                                <a href="/loan/31903021266032" class="btn-normal now">立即查看</a>
                                <a href="/loan/31903021266032" class="btn-normal wait-invest will">预热中</a>
                        </div>
                </div>
            </div>
		</div>
	</div>
	<div class="content-three">
        <div class="three-title">
            <span>投资</span>即有豪礼相送
        </div>
		<div class="content-wp">
			<img src="${staticServer}/images/sign/actor/landingpage/three-title.png" />
			<div class="table">
				<div class="title">
					投资攒天豆，<span>冲击排行榜</span>，大奖等您拿
				</div>
				<ul>
					 <li class="border">天豆获取方式：霸道总裁活动期间，投资即可获得</li>
					 <li>财豆获取方式：投资、签到、完成新手任务即可获得</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="content-four">
        <div class="four-title">
            呼唤朋友<span>赚佣金</span>
        </div>
		<div class="text">
			<p>拓天速贷推荐制度是为了广大用户可以在投资的时候，邀请朋友共同赚取奖励的贴心活动。</p>
			<p>当您在平台完成一系列操作之后，即可将自己的<span>用户名发送给身边的朋友</span>当您的推荐人</p>
			<p>成功投资后，您即可<span>获得奖励</span></p>
		</div>
		<table class="imagetable">
		    <tr>
		      <th></th>
		      <th>投资人</th>
		      <th>一级推荐人</th>
		      <th>二级推荐人</th>
		    </tr>
		    <tr>
		      <td>奖励机制</td>
		      <td></td>
		      <td>奖励预期年货<span>收益<span class="big">1%</span></span></td>
		      <td>奖励预期年化<span>收益<span class="big">1%</span></span></td>
		    </tr>
		    <tr>
		      <td>投资实例</td>
		      <td>投资半年期标的10万元</td>
		      <td><span>奖励</span>约500元</td>
		      <td><span>奖励</span>约500元</td>
		    </tr>
  		</table>
	</div>
	<div class="content-five">
        <div class="five-title">
            主流媒体眼中的我们
        </div>
		<div class="content-wp">
			<div class="left">
				<a href="">
					
				</a>
			</div>
			<div class="right">
				<ul>
					<li><img src="${staticServer}/images/sign/actor/landingpage/media-one.png" ></li>
					<li><img src="${staticServer}/images/sign/actor/landingpage/media-two.png" ></li>
					<li><img src="${staticServer}/images/sign/actor/landingpage/media-three.png" ></li>					
				</ul>
				<div class="title"><span>今日头条</span>拓天速贷以卓越风控打造高效资产平台</div>
				<p>
					随着拓天速贷终端产品的不断改进和完善，用户将得到更好的服务体验，在未来持续性优化改进的基础上，拓天速贷将重点把风控体系打造的更加系统性、完善化，使其超越百分之百的为用户做到本金及收益无风险化保障，最终使拓天速贷风控体系成为行业中的典范和标杆，引领行业持续健康的发展... ...<a href="">查看更多</a>
				</p>
			</div>
		</div>
		<div class="registered"><a href="/register/user">立即注册</a></div>
	</div>
</div>

</@global.main>