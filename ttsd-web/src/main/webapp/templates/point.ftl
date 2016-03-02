<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="" activeNav="我的账户" activeLeftNav="我的财豆" title="我的财豆">

<div class="content-container my-choi-beans">
    <h4 class="column-title">
        <em class="tc title-navli active">我的财豆</em>
        <em class="tc title-navli">财豆兑换</em>
        <em class="tc title-navli">财豆明细</em>
    </h4>
    <div class="choi-beans-list">
    	<div class="beans-intro">
    		<div class="beans-list">
    			<i class="icon-result icon-beans"></i>
    			<span class="title-text">财豆总览</span>
    			<span class="title-href">财豆明细></span>
    		</div>
    		<div class="beans-list mt-20">
				<span class="beans-num">可用财豆：10000</span>
				<i class="icon-result icon-dou"></i>
    		</div>
    		<div class="beans-list mt-20">
				<ul class="beans-recent">
					<li class="one-day">
						<p>
							<i class="icon-circle"></i>
							<span class="text-date">2月16日</span>
							<span class="text-money">
								<strong>+1000</strong>
								<i class="icon-result icon-sm-dou"></i>
							</span>
						</p>
					</li>
					<li class="two-day">
						<p>
							<i class="icon-circle"></i>
							<span class="text-date">2月16日</span>
							<span class="text-money">
								<strong>+1000</strong>
								<i class="icon-result icon-sm-dou"></i>
							</span>
						</p>
					</li>
					<li class="three-day">
						<p>
							<i class="icon-circle"></i>
							<span class="text-date">2月16日</span>
							<span class="text-money">
								<strong>+1000</strong>
								<i class="icon-result icon-sm-dou"></i>
							</span>
						</p>
					</li>
				</ul>
    		</div>
    	</div>
    	<div class="beans-operat">
    		<h3>赚取财豆</h3>
    		<ul class="object-list">
    			<li>
    				<p class="icon-com invest-bg"><i class="icon-result icon-invest"></i></p>
    				<p class="btn-list">
    					<a href="#"><span class="btn-invest">去投资</span></a>
    				</p>
    			</li>
    			<li>
    				<p class="icon-com sign-bg"><i class="icon-result icon-sign"></i></p>
    				<p class="btn-list">
    					<span class="btn-sign">签到</span>
    				</p>
    			</li>
    			<li>
    				<p class="icon-com task-bg"><i class="icon-result icon-task"></i></p>
    				<p class="btn-list">
    					<span class="btn-task">做任务</span>
    				</p>
    			</li>
    		</ul>
    	</div>
    	<div class="beans-infotext">
    		<dl>
    			<dt>财豆说明：</dt>
    			<dd>1.财豆可用于兑换体验券、加息券等优惠券；</dd>
    			<dd>2.在平台投资，投资金额与所获财豆的比率为1：1，即每投资100元现金奖励100个财豆；</dd>
    			<dd>3.连续签到，财豆翻倍送！第一天签到领5财豆，最多每天可领80财豆；</dd>
    			<dd>4.完成任务，赠送财豆，完成任务越多，赠送财豆越多；</dd>
    			<dd>5.在平台进行抽奖，最高可抽88888财豆，每次抽奖消耗10财豆；</dd>
    			<dd>6.财豆不可以提现，不可以转让，不可以用于其他平台。</dd>
    		</dl>
    	</div>
    </div>
</div>
</@global.main>