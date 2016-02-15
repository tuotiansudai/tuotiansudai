<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="index.js" headLab="sys-manage" sideLab="myTasks" title="系统首页">
<div class="col-md-10 home-report">
	<div class="title-type">
		<h4 class="title-task">我的任务</h4>
	</div>
	<div class="table-data">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>发起者</th>
					<th>内容</th>
					<th>发起时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>张三</td>
					<td>审批内容</td>
					<td>2016-02-16</td>
					<td>
						<a class="btn btn-danger btn-xs" href="javascript:void(0);">拒绝</a>
					</td>
				</tr>
				<tr>
					<td>张三</td>
					<td>审批内容</td>
					<td>2016-02-16</td>
					<td>
						<a class="btn btn-danger btn-xs" href="javascript:void(0);">拒绝</a>
					</td>
				</tr>
				<tr>
					<td>张三</td>
					<td>审批内容</td>
					<td>2016-02-16</td>
					<td>
						<a class="btn btn-danger btn-xs" href="javascript:void(0);">拒绝</a>
					</td>
				</tr>
				<tr>
					<td>张三</td>
					<td>审批内容</td>
					<td>2016-02-16</td>
					<td>
						<a class="btn btn-danger btn-xs" href="javascript:void(0);">拒绝</a>
					</td>
				</tr>
				<tr>
					<td>张三</td>
					<td>审批内容</td>
					<td>2016-02-16</td>
					<td>
						<a class="btn btn-danger btn-xs" href="javascript:void(0);">拒绝</a>
					</td>
				</tr>
				<tr>
					<td>张三</td>
					<td>审批内容</td>
					<td>2016-02-16</td>
					<td>
						<a class="btn btn-danger btn-xs" href="javascript:void(0);">拒绝</a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="table-data">
		<div class="two-flex">
			<div class="title-type">
				<h4 class="person-num">新增用户 (人)</h4>
			</div>
			<div class="table-data">
				<ul class="data-list yellow-bg">
					<li>
						<p class="num-text">${userToday!}</p>
						<p class="name-text">今日新增</p>
					</li>
					<li>
						<p class="num-text">${user7Days!}</p>
						<p class="name-text">本周新增</p>
					</li>
					<li>
						<p class="num-text">${user30Days!}</p>
						<p class="name-text">本月新增</p>
					</li>
				</ul>
			</div>
		</div>
		<div class="two-flex">
			<div class="title-type">
				<h4 class="send-money">平台累计交易金额 (元)</h4>
			</div>
			<div class="table-data">
				<ul class="data-list red-bg">
					<li>
						<p class="num-text">${totalInvest!}</p>
						<p class="name-text">累计交易总额</p>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="title-type">
		<h4 class="give-charge">充值金额 (元)</h4>
	</div>
	<div class="table-data">
		<ul class="data-list list-width blue-bg">
			<li>
				<p class="num-text">${rechargeToday!}</p>
				<p class="name-text">今日充值</p>
			</li>
			<li>
				<p class="num-text">${recharge7Days!}</p>
				<p class="name-text">本周充值</p>
			</li>
			<li>
				<p class="num-text">${recharge30Days!}</p>
				<p class="name-text">本月充值</p>
			</li>
		</ul>
	</div>
	<div class="title-type">
		<h4 class="get-money">提现金额 (元)</h4>
	</div>
	<div class="table-data">
		<ul class="data-list list-width green-bg">
			<li>
				<p class="num-text">${withdrawToday!}</p>
				<p class="name-text">今日提现</p>
			</li>
			<li>
				<p class="num-text">${withdraw7Days!}</p>
				<p class="name-text">本周提现</p>
			</li>
			<li>
				<p class="num-text">${withdraw30Days!}</p>
				<p class="name-text">本月提现</p>
			</li>
		</ul>
	</div>
	<div class="title-type">
		<h4 class="send-money">投资金额 (元)</h4>
	</div>
	<div class="table-data">
		<ul class="data-list list-width red-bg">
			<li>
				<p class="num-text">${investToday!}</p>
				<p class="name-text">今日投资</p>
			</li>
			<li>
				<p class="num-text">${invest7Days!}</p>
				<p class="name-text">本周投资</p>
			</li>
			<li>
				<p class="num-text">${invest30Days!}</p>
				<p class="name-text">本月投资</p>
			</li>
		</ul>
	</div>

</div>

</@global.main>