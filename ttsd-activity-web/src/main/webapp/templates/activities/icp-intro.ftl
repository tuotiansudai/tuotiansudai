
<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'icp_intro' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>


<@global.main pageCss="${css.icp_intro}" pageJavascript="${js.icp_intro}" activeNav="" activeLeftNav="" title="女神节活动_活动中心_拓天速贷" keywords="拓天速贷,妇女节活动,女神节,少女节" description="拓天速贷3月8日推出女神节活动,少女总动员,开启全民抓娃娃,参与幸运女神时光机,集花瓣赢礼盒等投资活动获得相应蜜汁礼盒.">
<div class="icp-intro-container" id="icpIntroContainer">
	<div class="top-item">
		<img src="" width="100%" class="media-pc">
		<img src="" width="100%" class="media-phone">
	</div>
	<div class="wp clearfix">
		<div class="content-item">
			<div class="title-item">
				dsd
			</div>
		</div>
		<div class="content-item">
			<div class="title-item">
				sds
			</div>
		</div>
	</div>
</div>
</@global.main>