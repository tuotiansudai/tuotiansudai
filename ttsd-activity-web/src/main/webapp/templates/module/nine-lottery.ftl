<div class="nine-lottery-group">
	<div class="lottery-left-group">
		<h3>我的抽奖机会：<span>${myCount}</span>次</h3>
		<ul class="lottery-item" id="lottery">
			<li class="lottery-unit lottery-unit-0" data-unit="0">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/gift-one.png">
			</li>
			<li class="lottery-unit lottery-unit-1" data-unit="1">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/gift-two.png">
			</li>
			<li class="lottery-unit lottery-unit-2" data-unit="2">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/gift-three.png">
			</li>
			<li class="lottery-unit lottery-unit-7" data-unit="7">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/gift-eight.png">
			</li>
			<li class="lottery-btn">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/lottery-btn.png">
			</li>
			<li class="lottery-unit lottery-unit-3" data-unit="3">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/gift-four.png">
			</li>
			<li class="lottery-unit lottery-unit-6" data-unit="6">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/gift-seven.png">
			</li>
			<li class="lottery-unit lottery-unit-5" data-unit="5">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/gift-six.png">
			</li>
			<li class="lottery-unit lottery-unit-4" data-unit="4">
				<img src="${commonStaticServer}/activity/images/model/double-eleven/gift-five.png">
			</li>
		</ul>
	</div>
	<div class="lottery-right-group" id="lotteryList">
		<h3>
			<span class="active">中奖纪录</span>
			<span>我的奖品</span>
		</h3>
		<div class="record-group">
			<ul class="record-item active" id="recordList"></ul>
			<script type="text/html" id="recordListTpl">
			{{each list}}
				<li>
					<span class="text-item">恭喜{{$value.mobile}}抽中了{{$value.prizeValue}}</span>
				</li>
			{{/each}}
			</script>

			<ul class="record-item my-gift" id="myRecord"></ul>
			<script type="text/html" id="myRecordTpl">
			{{each list}}
				<li>
					<span class="text-item">{{$value.prizeValue}}</span>
					<span class="time-item">{{$value.lotteryTime}}</span>
				</li>
			{{/each}}
			</script>

		</div>
	</div>
	<div class="lottery-tip" id="lotteryTip"></div>
		<script type="text/html" id="lotteryTipTpl">
		<i class="close-btn close-item"></i>
			<div class="text-list">
			{{if returnCode == 0}}
				{{if prizeType=='CONCRETE'}}
                	<p class="title-text"><img src="${commonStaticServer}/activity/images/model/double-eleven/title-bg.png" width="25%"></p>
					<p class="intro-text">抽中了{{prizeValue}}</p>
					<p class="info-text">拓天客服将会在7个工作日内联系您发放奖品！</p>
				{{else if prizeType=='VIRTUAL'}}
					<p class="title-text"><img src="${commonStaticServer}/activity/images/model/double-eleven/title-bg.png" width="25%"></p>
					<p class="intro-text">抽中了{{prizeValue}}</p>
					<p class="info-text">奖品已发放至“我的宝藏”当中。</p>
				{{else}}
                {{/if}}
			{{else if returnCode == 1}}
				<p class="no-time">您暂无抽奖机会啦～</p>
				<p class="info-text">赢取机会后再来抽奖吧！</p>
			{{else if returnCode == 3}}
                <p class="no-time">活动已过期～</p>
                <p class="info-text">更多活动即将登场！</p>
            {{else if returnCode == 5}}
                <p class="no-time">您的操作太频繁，请稍后再试！</p>
			{{/if}}


			</div>
			<div class="btn-list">
			{{if returnCode == 0}}
				{{if prizeType=='CONCRETE'}}
					<a href="javascript:void(0)" class="tip-btn close-item">继续抽奖</a>
				{{else if prizeType=='VIRTUAL'}}
					<a href="/loan-list" class="tip-btn">去使用</a><a href="javascript:void(0)" class="tip-btn close-item">继续抽奖</a>
				{{else}}
				{{/if}}
			{{else if returnCode == 1}}
				<a href="javascript:void(0)" class="tip-btn close-item">知道了</a>
			{{else if returnCode == 3}}
                <a href="javascript:void(0)" class="tip-btn close-item">知道了</a>
			{{/if}}
			</div>
		</script>

</div>