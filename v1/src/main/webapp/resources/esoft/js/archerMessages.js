var Archer = Archer || {};

Archer.Messages = {

	showMessages : function(messages, styleClass, doFocus, isShowGlobalMessages) {
		messages = eval("(" + messages + ")");
		var durationTimeForCommon = 2;
		var durationTimeForError = 12;

		for ( var i = 0; i < messages.length; i++) {
			var message = messages[i];

			if (!message.clientId) {
				if (isShowGlobalMessages) {
					var tipIcon;
					// INFO：0，WARN：1，ERROR：2，FATAL：3
					if (message.message.severity == '0') {
						tipIcon = "info-primefaces";
					} else if (message.message.severity == '1') {
						tipIcon = "warn-primefaces";
					} else if (message.message.severity == '2') {
						tipIcon = "error-primefaces";
					} else if (message.message.severity == '3') {
						tipIcon = "fatal-primefaces";
					}
					if (tipIcon) {
						$.dialog.tips(message.message.summary, durationTimeForError, true, tipIcon);
					} else {
						$.dialog.tips(message.message.summary, durationTimeForCommon, true);
					}
				}
				continue;
			}

			var element = document.getElementById(message.clientId);

			if (!element) {
				var elements = document.getElementsByName(message.clientId); // #21

				if (elements && elements.length) {
					element = elements[0];
				}
			}

			$(document.getElementById(message.clientId + "_message"))
			.remove();
			if (element && message.message) {
				var $input = $(element);
				var isMobile = location.href.indexOf("mobile") != -1;
				var $span = $("<div id='"
						+ message.clientId
						+ "_message"
						+ "' class='circle_bot' onclick='$(this).hide()' ><span class='s_b'> <b class='b1'></b> <b class='b2'></b></span><div class='info'>"
						+ message.message.summary
						+ "</div><span class='s_b'><b class='b2'></b> <b class='b1'></b></span><span class='s_i'> <i class='i9'></i><i class='i8'></i><i class='i7'></i><i class='i6'></i> <i class='i5'></i> <i class='i4'></i> <i class='i3'></i> <i class='i2'></i> <i class='i1'></i></span></div>");
				var $archerMessageFrame = $input
						.closest('.archer-message-frame');
				if (!$archerMessageFrame[0]) {
					var $parent = $input.parent();
					$span.css("position", "absolute").css("z-index", "10000").css(
							"top", $input.offset().top - 43).css("left",
							$input.offset().left + $input.width() - (isMobile?95:20)).hide();
					$("body:eq(0)").append($span);
				} else {
					$archerMessageFrame.css("position", "relative").css(
							"overflow", "visible");
					$span.css("position", "absolute").css("top", -40).css(
							"left",
							$input.offset().left
									- $archerMessageFrame.offset().left
									+ $input.width() - 20);
					$archerMessageFrame.append($span);
				}
				//hch start
				//当存在afterCreateArcherMessages方法的时候执行页面的afterCreateArcherMessages（）方法，为了改掉提示信息的位置
				if((typeof afterCreateArcherMessages) != 'undefined'){
					afterCreateArcherMessages();
				}
				//hch end
				$span.show(300);
				if (doFocus) {
					element.focus();
					doFocus = false;
				}
			}
		}
	},

	/**
	 * demo:Archer.Messages
					.showErrorMessages(
							"[{'message':{'summary':'请填写正确的金额!','detail':'','severity':'2'},'clientId':'form\:money'}]",
							"", false);
	 * */
	showErrorMessages : function(messages, styleClass, doFocus) {
		this.showMessages(messages, styleClass, doFocus, false);
	},

	clearAllMessages : function() {
		$(".circle_bot").remove();
	}
};