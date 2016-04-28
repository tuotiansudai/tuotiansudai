////////////////////////////////解决ie8不支持bind方法的问题/////////////////////////
if (!Function.prototype.bind) {
	  Function.prototype.bind = function(oThis) {
	    if (typeof this !== 'function') {
	      // closest thing possible to the ECMAScript 5
	      // internal IsCallable function
	      throw new TypeError('Function.prototype.bind - what is trying to be bound is not callable');
	    }

	    var aArgs   = Array.prototype.slice.call(arguments, 1),
	        fToBind = this,
	        fNOP    = function() {},
	        fBound  = function() {
	          return fToBind.apply(this instanceof fNOP && oThis
	                 ? this
	                 : oThis,
	                 aArgs.concat(Array.prototype.slice.call(arguments)));
	        };

	    fNOP.prototype = this.prototype;
	    fBound.prototype = new fNOP();

	    return fBound;
	  };
	}

//更换验证码
function nextVerifyCode() {
	$(".verifyCode").attr("src",window.CONTEXT_PATH+
			"/verifyCodeServlet?" + new Date().getTime());
}
////////////////////////////////////页面提示框--开始/////////////////////////////////////////////////

function picDialog(imgSrc){
	$.artDialog({
		fixed: true,
	     lock:true,
		content: "<div style='overflow:auto;max-width:600px;max-height:480px;'><img src='"+imgSrc+"'></img></div>"
	});
}

/**
 * 短暂提示
 * @param	{String}	提示内容
 * @param	{Number}	显示时间 (默认1.5秒)
 */
artDialog.tips = function (content, time, isLock, icon) {
    return $.dialog({
        id: 'Tips',
        icon: icon,
        title: false,
        fixed: true,
        lock: isLock?isLock:false,
        opacity: .5
    })
    .content('<div style="padding: 0 1em;">' + content + '</div>')
    .time(time || 10);
};

/**
 * 确认
 * @param	{String}	消息内容
 * @param	{Function}	确定按钮回调函数
 * @param	{Function}	取消按钮回调函数
 */
artDialog.confirm = function (content, yes, no) {
    return $.dialog({
        id: 'Confirm',
        icon: 'question',
        fixed: true,
        lock: true,
        opacity: .1,
        content: content,
        ok: function (here) {
            return yes.call(this, here);
        },
        cancel: function (here) {
            return no && no.call(this, here);
        }
    });
};

/**
 * 警告
 * @param	{String}	消息内容
 */
artDialog.alert = function (content, yes, icon) {
    return artDialog({
        id: 'Alert',
        icon: icon,
        fixed: true,
        lock: true,
        esc:false,
        content: content,
        ok: function (here) {
            return yes.call(this, here);
        },
        cancel:false
    });
};

////////////////////////////////页面提示框--结束////////////////////////////////////
//验证码发送消息提示
function timerCountB(buttonId) {
		timer(buttonId, {
			"count" : 60,
			"animate" : true,
			initText:"获取验证码",
			initTextBefore : "已发送...",
			initTextAfter : "s"
		}).begin();
}

//验证码发送消息提示
function timerCount(xhr, status, args, buttonId) {
	$('#'+buttonId).removeAttr("disabled");
	if (!args.validationFailed) {
		timer(buttonId, {
			"count" : 60,
			"animate" : true,
			initText:"获取验证码",
			initTextBefore : "已发送...",
			initTextAfter : "s"
		}).begin();
	}
}

function enableBtn(buttonId){
	$('#'+buttonId).removeAttr("disabled");
}
$(function(){
	$('.jq_mobile').attr('disabled',true);
})


$(function(){
	if ($('.isOpenFastPay').val() == '1'){
		$('.quickPayment').addClass('current');
		$('.isOpenFastPayment').prop("checked", true);
	} else {
		$('.onlineRecharge').addClass('current');
		$('.isOpenFastPayment').prop("checked", false);
	}
});

$(function(){
	$('.quickPayment').on('click',function(){
		if ($(this).hasClass('current')) {
			return;
		} else {
			if ($('.isOpenFastPay').val() == '1') {
				$(this).addClass('current');
				$('.onlineRecharge').removeClass('current');
				$('.zhcz').hide();
				$('.isOpenFastPayment').prop("checked", true);
			} else {
				window.location.href = '/user/bankCardList';
			}
		}
	});
	$('.onlineRecharge').on('click',function(){
		if ($(this).hasClass('current')) {
			return;
		} else {
			$(this).addClass('current');
			$('.quickPayment').removeClass('current');
			$('.isOpenFastPayment').prop("checked", false);
			$('.zhcz').show();
		}
	});
});

$(function(){
	$('.referrerList').on('click',function(){
		if ($(this).hasClass('hover')){
			return;
		} else {
			$(this).addClass('hover');
			$('.referrerInvest').removeClass('hover');
			$('.dateOne').show();
			$('.dateTwo').hide();
			$('.referrerTable').show();
			$('.investTable').hide();
		}
	});

	$('.referrerInvest').on('click',function(){
		if ($(this).hasClass('hover')){
			return;
		} else {
			$(this).addClass('hover');
			$('.referrerList').removeClass('hover');
			$('.dateTwo').show();
			$('.dateOne').hide();
			$('.investTable').show();
			$('.referrerTable').hide();
		}
	});
});








