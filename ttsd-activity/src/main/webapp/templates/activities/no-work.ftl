<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.no_work}" pageJavascript="${js.no_work}" activeNav="" activeLeftNav="" title="iphone7活动_活动中心_拓天速贷" keywords="投资活动,APP投资,iphone7,拓天速贷" description="参与拓天速贷'厚惠有7'投资活动的用户即可获得128Giphone7一部，使用APP投资还可获得投资红包大奖">
<div class="activity-container">
	<div class="top-intro-img">
		<img src="${staticServer}/activity/images/iphone7/top-intro.png" alt="英豪榜" width="100%" class="top-img">
		<img src="${staticServer}/activity/images/iphone7/top-intro-phone.png" alt="英豪榜" width="100%" class="top-img-phone">
	</div>
	<div class="actor-content-group">
		<div class="wp clearfix">
            <div class="reg-tag-current" style="display: none">
				<#include '../module/register.ftl' />
            </div>
		</div>
	</div>
</div>
<#include "login-tip.ftl" />
</@global.main>