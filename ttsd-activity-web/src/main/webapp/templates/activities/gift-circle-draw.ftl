<div class="gift-circle-frame clearfix">
    <div class="gift-circle-out">
            <div class="pointer-img"></div>
            <div class="rotate-btn"></div>
    </div>
    <div class="gift-circle-detail">
        <h3><span class="my-category">我的积分：</span>
            <#--登录后查看-->
<@global.isNotAnonymous>
    <span class="my-property"></span>
</@global.isNotAnonymous>
<@global.isAnonymous>
    <a href="javascript:void(0)" class="show-login">登录后查看</a>
</@global.isAnonymous>
        </h3>

        <div class="trim-strip">
            <i class="icon-square"></i>
            <i class="icon-square"></i>
            <i class="icon-square"></i>
            <i class="icon-square"></i>
            <i class="icon-vertical-line"></i>
            <i class="icon-vertical-line"></i>
        </div>
        <div class="gift-info-box">
            <ul class="gift-record clearfix">
                <li class="active"><span>中奖记录</span></li>
                <li><span>我的奖品</span></li>
            </ul>
            <div class="record-list">
                <ul class="record-model user-record" ></ul>
                <ul class="record-model own-record" style="display: none"></ul>
            </div>
        </div>
    </div>
</div>