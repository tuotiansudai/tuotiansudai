webpackJsonp([5],{

/***/ 1:
/***/ function(module, exports) {

	'use strict';

	function refreshCaptcha(dom, url) {
	    var captcha = url + '?' + new Date().getTime().toString();
	    dom.setAttribute('src', captcha);
	}
	/* init radio style */
	function initRadio($radio, $radioLabel) {
	    var numRadio = $radio.length;
	    if (numRadio) {
	        $radio.each(function (key, option) {
	            var $this = $(this);
	            if ($this.is(':checked')) {
	                $this.next('label').addClass('checked');
	            }
	            $this.next('label').click(function () {
	                var $thisLab = $(this);
	                if (!/checked/.test(this.className)) {
	                    $radioLabel.removeClass('checked');
	                    $thisLab.addClass('checked');
	                }
	            });
	        });
	    }
	}

	// 验证身份证有效性
	function IdentityCodeValid(code) {
	    var city = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江 ", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北 ", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏 ", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外 " };
	    var pass = true;

	    if (!code || !/\d{17}[\d|x]/i.test(code)) {
	        pass = false;
	    } else if (!city[code.substr(0, 2)]) {
	        pass = false;
	    } else {
	        //18位身份证需要验证最后一位校验位
	        if (code.length == 18) {
	            code = code.split('');
	            //∑(ai×Wi)(mod 11)
	            //加权因子
	            var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
	            //校验位
	            var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
	            var sum = 0;
	            var ai = 0;
	            var wi = 0;
	            for (var i = 0; i < 17; i++) {
	                ai = code[i];
	                wi = factor[i];
	                sum += ai * wi;
	            }
	            if (parity[sum % 11] != code[17]) {
	                pass = false;
	            }
	        }
	    }
	    return pass;
	}
	//检测年龄是否大于18
	function checkedAge(birthday) {
	    var getAge = birthday.substring(6, 14),
	        currentDay = new Date(),
	        checkedAge = true;
	    var y = currentDay.getFullYear(),
	        m = currentDay.getMonth() + 1,
	        d = currentDay.getDate();
	    var today = y + '' + (m < 10 ? '0' + m : m) + '' + (d < 10 ? '0' + d : d);
	    var myAge = Math.floor((today - getAge) / 10000);
	    if (myAge < 18) {
	        checkedAge = false;
	    }
	    return checkedAge;
	}
	//弹框样式
	function popWindow(contentHtml, area) {
	    var $shade = $('<div class="shade-body-mask"></div>');
	    var $popWindow = $(contentHtml),
	        size = $.extend({ width: '460px', height: '370px' }, area);
	    $popWindow.css({
	        width: size.width,
	        height: size.height
	    });
	    var adjustPOS = function adjustPOS() {
	        var scrollHeight = document.body.scrollTop || document.documentElement.scrollTop,
	            pTop = $(window).height() - $popWindow.height(),
	            pLeft = $(window).width() - $popWindow.width();
	        $popWindow.css({ 'top': pTop / 2, left: pLeft / 2 });
	        $shade.height($('body').height());
	        $('body').append($popWindow).append($shade);
	    };
	    adjustPOS();

	    $('.close-btn,.go-close', $popWindow).on('click', function () {
	        $popWindow.remove();
	        $shade.remove();
	    });
	}

	// 验证用户是否处于登陆状态
	function isUserLogin() {
	    var LoginDefer = $.Deferred(); //在函数内部，新建一个Deferred对象
	    $.ajax({
	        url: '/isLogin',
	        type: 'GET'
	    }).done(function (data) {
	        if (data) {
	            //如果data有值，说明token已经过期，用户处于未登陆状态，并且需要更新token
	            LoginDefer.reject(data);
	        } else {
	            //如果data为空，说明用户处于登陆状态，不需要做任何处理
	            LoginDefer.resolve();
	        }
	    }).fail(function () {
	        LoginDefer.reject();
	    });

	    return LoginDefer.promise(); // 返回promise对象
	}

	function useAjax(opt, callbackDone, callbackAlways) {
	    var defaultOpt = {
	        type: 'POST',
	        dataType: 'json'
	    };
	    var option = $.extend(defaultOpt, opt);

	    //防止跨域，只有post请求需要，get请求不需要
	    $(document).ajaxSend(function (e, xhr, options) {
	        var token = $("meta[name='_csrf']").attr("content");
	        var header = $("meta[name='_csrf_header']").attr("content");
	        xhr.setRequestHeader(header, token);
	    });
	    //当ajax请求失败的时候重定向页面
	    $(document).ajaxError(function (event, jqXHR, ajaxSettings, thrownError) {
	        if (jqXHR.status == 403) {
	            if (jqXHR.responseText) {
	                var data = JSON.parse(jqXHR.responseText);
	                window.location.href = data.directUrl + (data.refererUrl ? "?redirect=" + data.refererUrl : '');
	            }
	        }
	    });

	    $.ajax(option).done(function (data) {
	        callbackDone && callbackDone(data);
	    }).fail(function (data) {
	        console.error('接口错误，请联系客服');
	    }).always(function () {
	        callbackAlways && callbackAlways();
	    });
	}
	//倒计时
	function countDownLoan(option, callback) {
	    var defaultOpt = {
	        btnDom: '',
	        time: 60,
	        textCounting: '秒后重新发送'
	    };
	    var options = $.extend({}, defaultOpt, option),
	        downtimer = void 0;
	    var $countBtn = options.btnDom;

	    var countDownStart = function countDownStart() {
	        $countBtn.text(options.time-- + options.textCounting).prop('disabled', true).addClass('count-downing');
	        if (options.time == 0) {
	            //结束倒计时
	            clearInterval(downtimer);
	            callback && callback();
	            $countBtn.text('重新发送').prop('disabled', false).removeClass('count-downing');
	        }
	    };
	    if (options.time > 0) {
	        countDownStart(); //立即调用一次，解决延迟加载的问题
	        $countBtn.val(options.textCounting);
	        downtimer = setInterval(function () {
	            countDownStart();
	        }, 1000);
	    }
	}
	var MathDecimal = {
	    MathFloor: function MathFloor(math) {
	        //小数点保留2位小数，不要四舍五入,但是不强制2位小数点
	        var re = /([0-9]+\.[0-9]{2})[0-9]*/;
	        var mathString = math + '';
	        var aNew = mathString.replace(re, "$1");
	        return aNew;
	    },
	    MathRound: function MathRound(math) {
	        //小数点保留2位小数，要四舍五入
	        var newNum = Math.round(math * 100) / 100;
	        return newNum;
	    }
	};
	var decrypt = {
	    //加密
	    compile: function compile(strId, realId) {
	        var realIdStr = realId + '';
	        var strIdObj = realIdStr.split(''),
	            realLen = realIdStr.length;
	        for (var i = 0; i < 11; i++) {
	            strIdObj[2 * i + 2] = realIdStr[i] ? realIdStr[i] : 'a';
	        }
	        return strIdObj.join('');
	    },
	    //解密
	    uncompile: function uncompile(strId) {
	        var strIdString = strId + '';
	        var strIdObj = strIdString.split(''),
	            realId = [];
	        for (var i = 0; i < 11; i++) {
	            realId[i] = strIdObj[2 * i + 2];
	        }

	        var stringRealId = realId.join(''),
	            getNum = stringRealId.match(/\d/gi);
	        return getNum.join('');
	    }
	};

	exports.refreshCaptcha = refreshCaptcha;
	exports.initRadio = initRadio;
	exports.IdentityCodeValid = IdentityCodeValid;
	exports.checkedAge = checkedAge;
	exports.popWindow = popWindow;
	exports.isUserLogin = isUserLogin;
	exports.useAjax = useAjax;
	exports.countDownLoan = countDownLoan;
	exports.MathDecimal = MathDecimal;
	exports.decrypt = decrypt;

	// export {
	//     refreshCaptcha,initRadio,IdentityCodeValid,checkedAge,popWindow,isUserLogin,
	//     useAjax,countDownLoan,MathDecimal,decrypt};


	// module.exports.refreshCaptcha=refreshCaptcha;

/***/ },

/***/ 53:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	__webpack_require__.e/* nsure */(5, function () {
	    __webpack_require__(54);
	    var commonFun = __webpack_require__(1);
	    var ValidatorForm = __webpack_require__(55);
	    var $redEnvelopFrame = $('#redEnvelopFloatFrame');
	    // 回到顶部
	    (function () {
	        var $backTopBtn = $redEnvelopFrame.find('.back-top');
	        $(window).scrollTop() > $(window).height() / 2 ? $backTopBtn.fadeIn('fast') : $backTopBtn.fadeOut('fast');
	        $(window).scroll(function () {
	            if ($(window).scrollTop() > $(window).height() / 2) {
	                $backTopBtn.fadeIn('fast');
	            } else {
	                $backTopBtn.fadeOut('fast');
	            }
	        });
	        $backTopBtn.find('.nav-text').on('click', function (event) {
	            //back top
	            event.preventDefault();
	            $('body,html').animate({ scrollTop: 0 }, 'fast');
	        });
	    })();

	    // **************** 投资计算器开始****************
	    (function () {
	        var countForm = globalFun.$('#countForm');
	        var $countFormOut = $(countForm).parents('.count-form');
	        var $calBtn = $('.cal-btn', $redEnvelopFrame);
	        var $closeBtn = $('.count-form .close-count', $redEnvelopFrame);
	        var errorCountDom = $(countForm).find('.error-box');
	        //弹出计算器
	        $calBtn.on('click', function (event) {
	            event.preventDefault();
	            $(this).addClass('active');
	            $countFormOut.show();
	        });

	        //关闭计算器
	        $closeBtn.on('click', function (event) {
	            event.preventDefault();
	            var $navList = $('.fix-nav-list li', $redEnvelopFrame);
	            $countFormOut.hide();
	            $navList.removeClass('active');
	        });

	        //可拖拽计算器
	        $countFormOut.dragging({
	            move: 'both',
	            randomPosition: false,
	            hander: '.hander'
	        });

	        //充值计算器表单
	        $("#resetBtn").on('click', function (event) {
	            event.preventDefault();
	            $(countForm).find('input:text').val('');
	            $('#resultNum').text('0');
	        });

	        //验证表单
	        var countValidator = new ValidatorForm();

	        countValidator.add(countForm.money, [{
	            strategy: 'isNonEmpty',
	            errorMsg: '请输入投资金额！'
	        }, {
	            strategy: 'isNumber',
	            errorMsg: '请输入有效的数字！'
	        }]);

	        countValidator.add(countForm.month, [{
	            strategy: 'isNonEmpty',
	            errorMsg: '请输入投资时长！'
	        }, {
	            strategy: 'isNumber',
	            errorMsg: '请输入有效的数字！'
	        }]);

	        countValidator.add(countForm.bite, [{
	            strategy: 'isNonEmpty',
	            errorMsg: '请输入年化利率！'
	        }, {
	            strategy: 'isNumber',
	            errorMsg: '请输入有效的数字！'
	        }]);

	        var reInputs = $(countForm).find('input:text');
	        reInputs = Array.from(reInputs);

	        var _iteratorNormalCompletion = true;
	        var _didIteratorError = false;
	        var _iteratorError = undefined;

	        try {
	            for (var _iterator = reInputs[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
	                var el = _step.value;

	                el.addEventListener("blur", function () {
	                    var errorMsg = countValidator.start(this);
	                    if (errorMsg) {
	                        errorCountDom.text(errorMsg);
	                    } else {
	                        errorCountDom.text('');
	                    }
	                });
	            }
	        } catch (err) {
	            _didIteratorError = true;
	            _iteratorError = err;
	        } finally {
	            try {
	                if (!_iteratorNormalCompletion && _iterator.return) {
	                    _iterator.return();
	                }
	            } finally {
	                if (_didIteratorError) {
	                    throw _iteratorError;
	                }
	            }
	        }

	        countForm.onsubmit = function (event) {
	            event.preventDefault();
	            var errorMsg = void 0;
	            for (var i = 0, len = reInputs.length; i < len; i++) {
	                errorMsg = countValidator.start(reInputs[i]);
	                if (errorMsg) {
	                    errorCountDom.text(errorMsg);
	                    break;
	                }
	            }
	            if (!errorMsg) {
	                //计算本息
	                var moneyNum = Math.round(countForm.money.value),
	                    monthNum = Math.round(countForm.month.value),
	                    biteNum = Math.round(countForm.bite.value) / 100,
	                    $resultNum = $('#resultNum'),
	                    resultNum = moneyNum + moneyNum * monthNum * biteNum * 30 * 0.9 / 365;
	                $resultNum.text(resultNum.toFixed(2));
	            }
	        };
	    })();

	    //意见反馈
	    (function () {
	        var $feedbackConatiner = $('#feedbackConatiner');
	        var feedForm = globalFun.$('#feedForm');
	        var $typeList = $('.type-list', $feedbackConatiner);
	        var imageCaptchaFeed = globalFun.$('#imageCaptchaFeed');
	        var errorFeedDom = $(feedForm).find('.error-box');
	        //刷新验证码
	        $(imageCaptchaFeed).on('click', function (event) {
	            event.preventDefault();
	            commonFun.refreshCaptcha(this, '/feedback/captcha?');
	        });

	        //弹出意见反馈层
	        $('.fix-nav-list .show-feed', $redEnvelopFrame).on('click', function (event) {
	            event.preventDefault();
	            //刷新验证码
	            commonFun.refreshCaptcha(imageCaptchaFeed, '/feedback/captcha?');
	            var $self = $(this);
	            $self.addClass('active');
	            $feedbackConatiner.show();
	        });
	        //关闭意见反馈层
	        $('.feed-close', $redEnvelopFrame).on('click', function (event) {
	            event.preventDefault();
	            var $self = $(this),
	                $showFeed = $('.fix-nav-list .show-feed', $redEnvelopFrame),
	                $tipDom = $self.closest('.feedback-model');
	            $tipDom.hide();
	            $showFeed.removeClass('active');
	        });

	        //模拟select下拉框
	        $('dt,i', $typeList).on('click', function (event) {
	            event.preventDefault();
	            var $self = $(this),
	                $list = $self.siblings('dd');
	            $list.slideToggle('fast');
	        });

	        $('dd', $typeList).on('click', function (event) {
	            event.preventDefault();
	            var $self = $(this),
	                $parent = $self.parent('.type-list'),
	                $dt = $parent.find('dt'),
	                $dd = $parent.find('dd');
	            $dt.text($self.text()).attr('data-type', $self.attr('data-type'));
	            $dd.hide();
	        });

	        //验证表单
	        var feedbackValidator = new ValidatorForm();

	        feedbackValidator.add(feedForm.content, [{
	            strategy: 'isNonEmpty',
	            errorMsg: '内容不能为空！'
	        }, {
	            strategy: 'minLength:14',
	            errorMsg: '文字限制最小为14！'
	        }, {
	            strategy: 'maxLength:200',
	            errorMsg: '文字限制最大为200！'
	        }]);

	        feedbackValidator.add(feedForm.contact, [{
	            strategy: 'isMobile',
	            errorMsg: '请输入正确的手机号！'
	        }]);

	        feedbackValidator.add(feedForm.captcha, [{
	            strategy: 'isNonEmpty',
	            errorMsg: '验证码不能为空！'
	        }, {
	            strategy: 'isNumber:5',
	            errorMsg: '请输入5位验证码！'
	        }]);

	        var feedInputs = $(feedForm).find('input:text,textarea');
	        feedInputs = Array.from(feedInputs);

	        var _iteratorNormalCompletion2 = true;
	        var _didIteratorError2 = false;
	        var _iteratorError2 = undefined;

	        try {
	            for (var _iterator2 = feedInputs[Symbol.iterator](), _step2; !(_iteratorNormalCompletion2 = (_step2 = _iterator2.next()).done); _iteratorNormalCompletion2 = true) {
	                var el = _step2.value;

	                el.addEventListener("keyup", function () {
	                    var errorMsg = feedbackValidator.start(this);
	                    if (errorMsg) {
	                        errorFeedDom.text(errorMsg);
	                    } else {
	                        errorFeedDom.text('');
	                    }
	                });
	            }
	        } catch (err) {
	            _didIteratorError2 = true;
	            _iteratorError2 = err;
	        } finally {
	            try {
	                if (!_iteratorNormalCompletion2 && _iterator2.return) {
	                    _iterator2.return();
	                }
	            } finally {
	                if (_didIteratorError2) {
	                    throw _iteratorError2;
	                }
	            }
	        }

	        feedForm.onsubmit = function (event) {
	            event.preventDefault();

	            var errorMsg = void 0;
	            for (var i = 0, len = feedInputs.length; i < len; i++) {
	                errorMsg = feedbackValidator.start(feedInputs[i]);
	                if (errorMsg) {
	                    errorFeedDom.text(errorMsg);
	                    break;
	                }
	            }
	            if (!errorMsg) {
	                (function () {
	                    //提交表单
	                    var type = $feedbackConatiner.find('.type-list dt').data('type');
	                    var feedSubmit = $(feedForm).find(':submit');
	                    feedForm.type.value = type;
	                    commonFun.useAjax({
	                        url: "/feedback/submit",
	                        type: 'POST',
	                        data: $(feedForm).serialize(),
	                        beforeSend: function beforeSend() {
	                            feedSubmit.prop('disabled', true);
	                        }
	                    }, function (data) {
	                        refreshCaptcha(imageCaptchaFeed, '/feedback/captcha?');
	                        if (data.success) {
	                            $feedbackConatiner.hide();
	                            $(feedForm).find(':text,textarea,input[name="type"]').val('');
	                            $('#feedbackModel').show();
	                        } else {
	                            errorFeedDom.text('验证码错误！');
	                        }
	                    });
	                })();
	            }
	        };
	    })();
	});

/***/ },

/***/ 54:
/***/ function(module, exports) {

	'use strict';

	$.fn.extend({
		//---元素拖动插件
		dragging: function dragging(data) {
			var $this = $(this);
			var xPage;
			var yPage;
			var X; //
			var Y; //
			var xRand = 0; //
			var yRand = 0; //
			var father = $('body');
			var defaults = {
				move: 'both',
				randomPosition: true,
				hander: 1
			};
			var opt = $.extend({}, defaults, data);
			var movePosition = opt.move;
			var random = opt.randomPosition;

			var hander = opt.hander;

			if (hander == 1) {
				hander = $this;
			} else {
				hander = $this.find(opt.hander);
			}

			//---初始化
			father.css({ "position": "relative" });
			$this.css({ "position": "fixed" });
			hander.css({ "cursor": "move" });

			var faWidth = father.width();
			var faHeight = father.height();
			var thisWidth = $this.width() + parseInt($this.css('padding-left')) + parseInt($this.css('padding-right'));
			var thisHeight = $this.height() + parseInt($this.css('padding-top')) + parseInt($this.css('padding-bottom'));

			var mDown = false; //
			var positionX;
			var positionY;
			var moveX;
			var moveY;

			if (random) {
				$thisRandom();
			}
			function $thisRandom() {
				//随机函数
				$this.each(function (index) {
					var randY = parseInt(Math.random() * (faHeight - thisHeight)); ///
					var randX = parseInt(Math.random() * (faWidth - thisWidth)); ///
					if (movePosition.toLowerCase() == 'x') {
						$(this).css({
							left: randX
						});
					} else if (movePosition.toLowerCase() == 'y') {
						$(this).css({
							top: randY
						});
					} else if (movePosition.toLowerCase() == 'both') {
						$(this).css({
							top: randY,
							left: randX
						});
					}
				});
			}

			hander.mousedown(function (e) {
				father.children().css({ "zIndex": "0" });
				$this.css({ "zIndex": "99" });
				mDown = true;
				X = e.pageX;
				Y = e.pageY;
				positionX = $this.position().left;
				positionY = $this.position().top;
				return false;
			});

			$(document).mouseup(function (e) {
				mDown = false;
			});

			$(document).mousemove(function (e) {
				xPage = e.pageX; //--
				moveX = positionX + xPage - X;

				yPage = e.pageY; //--
				moveY = positionY + yPage - Y;

				function thisXMove() {
					//x轴移动
					if (mDown == true) {
						$this.css({ "left": moveX });
					} else {
						return;
					}
					if (moveX < 0) {
						$this.css({ "left": "0" });
					}
					if (moveX > faWidth - thisWidth) {
						$this.css({ "left": faWidth - thisWidth });
					}
					return moveX;
				}

				function thisYMove() {
					//y轴移动
					if (mDown == true) {
						$this.css({ "top": moveY });
					} else {
						return;
					}
					if (moveY < 0) {
						$this.css({ "top": "0" });
					}
					if (moveY > faHeight - thisHeight) {
						$this.css({ "top": faHeight - thisHeight });
					}
					return moveY;
				}

				function thisAllMove() {
					//全部移动
					if (mDown == true) {
						$this.css({ "left": moveX, "top": moveY });
					} else {
						return;
					}
					if (moveX < 0) {
						$this.css({ "left": "0" });
					}
					if (moveX > faWidth - thisWidth) {
						$this.css({ "left": faWidth - thisWidth });
					}

					if (moveY < 0) {
						$this.css({ "top": "0" });
					}
					if (moveY > faHeight - thisHeight) {
						$this.css({ "top": faHeight - thisHeight });
					}
				}
				if (movePosition.toLowerCase() == "x") {
					thisXMove();
				} else if (movePosition.toLowerCase() == "y") {
					thisYMove();
				} else if (movePosition.toLowerCase() == 'both') {
					thisAllMove();
				}
			});
		}
	});

/***/ },

/***/ 55:
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	var commonFun = __webpack_require__(1);

	function createElement(element, errorMsg) {
	    if (element && element.nextElementSibling) {
	        element.nextElementSibling.innerHTML = errorMsg;
	        return;
	    }
	    var span = document.createElement("span");
	    span.className = "error";
	    span.innerHTML = errorMsg;
	    element && element.parentElement.appendChild(span);
	}
	function removeElement(element) {
	    element && element.nextElementSibling && element.parentElement.removeChild(element.nextElementSibling);
	}
	/*******策略对象********/
	var strategies = {
	    isNonEmpty: function isNonEmpty(errorMsg, showErrorAfter) {
	        if (this.value === '') {
	            globalFun.addClass(this, 'error');
	            showErrorAfter && createElement(this, errorMsg);
	            return errorMsg;
	        } else {
	            globalFun.removeClass(this, 'error');
	            globalFun.addClass(this, 'valid');
	            showErrorAfter && removeElement(this);
	        }
	    },
	    minLength: function minLength(errorMsg, length, showErrorAfter) {
	        if (this.value.length < Number(length)) {
	            globalFun.addClass(this, 'error');
	            showErrorAfter && createElement(this, errorMsg);
	            return errorMsg;
	        } else {
	            globalFun.removeClass(this, 'error');
	            globalFun.addClass(this, 'valid');
	            showErrorAfter && removeElement(this);
	        }
	    },
	    maxLength: function maxLength(errorMsg, length, showErrorAfter) {
	        if (this.value.length > Number(length)) {
	            globalFun.addClass(this, 'error');
	            showErrorAfter && createElement(this, errorMsg);
	            return errorMsg;
	        } else {
	            globalFun.removeClass(this, 'error');
	            globalFun.addClass(this, 'valid');
	            showErrorAfter && removeElement(this);
	        }
	    },
	    isNumber: function isNumber(errorMsg, length, showErrorAfter) {
	        if (length) {
	            var reg = new RegExp('^\\d{' + length + '}$', 'g');
	            if (reg.test(this.value)) {
	                globalFun.removeClass(this, 'error');
	                globalFun.addClass(this, 'valid');
	                showErrorAfter && removeElement(this);
	            } else {
	                globalFun.addClass(this, 'error');
	                showErrorAfter && createElement(this, errorMsg);
	                return errorMsg;
	            }
	        } else {
	            //判断是非为数字，无需固定判断长度
	            if (/^\d+[.]{0,1}\d*$/.test(this.value)) {
	                globalFun.removeClass(this, 'error');
	                globalFun.addClass(this, 'valid');
	                showErrorAfter && removeElement(this);
	            } else {
	                globalFun.addClass(this, 'error');
	                showErrorAfter && createElement(this, errorMsg);
	                return errorMsg;
	            }
	        }
	    },
	    isChinese: function isChinese(errorMsg, showErrorAfter) {
	        if (/^[\u4E00-\u9FA5]+$/.test(this.value)) {
	            globalFun.removeClass(this, 'error');
	            globalFun.addClass(this, 'valid');
	            showErrorAfter && removeElement(this);
	        } else {
	            globalFun.addClass(this, 'error');
	            showErrorAfter && createElement(this, errorMsg);
	            return errorMsg;
	        }
	    },
	    equalLength: function equalLength(errorMsg, length, showErrorAfter) {
	        if (this.value.length != Number(length)) {
	            globalFun.addClass(this, 'error');
	            showErrorAfter && createElement(this, errorMsg);

	            return errorMsg;
	        } else {
	            globalFun.removeClass(this, 'error');
	            globalFun.addClass(this, 'valid');
	            showErrorAfter && removeElement(this);
	        }
	    },
	    checkPassword: function checkPassword(errorMsg, showErrorAfter) {
	        var regBool = /^(?=.*[^\d])(.{6,20})$/.test(this.value);
	        if (!regBool) {
	            globalFun.addClass(this, 'error');
	            showErrorAfter && createElement(this, errorMsg);
	            return errorMsg;
	        } else {
	            globalFun.removeClass(this, 'error');
	            globalFun.addClass(this, 'valid');
	            showErrorAfter && removeElement(this);
	        }
	    },
	    isMobile: function isMobile(errorMsg, showErrorAfter) {
	        //只验证手机号不验证是非为空
	        if (this.value == '') {
	            return '';
	        }
	        if (!/(^1[0-9]{10}$)/.test(this.value)) {
	            globalFun.addClass(this, 'error');
	            showErrorAfter && createElement(this, errorMsg);
	            return errorMsg;
	        } else {
	            globalFun.removeClass(this, 'error');
	            globalFun.addClass(this, 'valid');
	            showErrorAfter && removeElement(this);
	        }
	    },
	    identityValid: function identityValid(errorMsg, showErrorAfter) {
	        //验证身份证号
	        var cardValid = commonFun.IdentityCodeValid(this.value);
	        if (!cardValid) {
	            globalFun.addClass(this, 'error');
	            showErrorAfter && createElement(this, errorMsg);
	            return errorMsg;
	        } else {
	            globalFun.removeClass(this, 'error');
	            globalFun.addClass(this, 'valid');
	            showErrorAfter && removeElement(this);
	        }
	    },
	    ageValid: function ageValid(errorMsg, showErrorAfter) {
	        //验证年龄是否满18
	        var cardValid = commonFun.IdentityCodeValid(this.value);
	        if (cardValid) {
	            var ageValid = commonFun.checkedAge(this.value);
	            if (!ageValid) {
	                globalFun.addClass(this, 'error');
	                showErrorAfter && createElement(this, errorMsg);
	                return errorMsg;
	            } else {
	                globalFun.removeClass(this, 'error');
	                globalFun.addClass(this, 'valid');
	                showErrorAfter && removeElement(this);
	            }
	        }
	    },
	    isCardExist: function isCardExist(errorMsg, showErrorAfter) {
	        var getResult = '',
	            that = this;
	        if (this.value.length != 18) {
	            return;
	        }
	        commonFun.useAjax({
	            type: 'GET',
	            async: false,
	            url: '/authentication/identityNumber/' + this.value + '/is-exist'
	        }, function (response) {
	            if (response.data.status) {
	                // 身份证已存在
	                getResult = errorMsg;
	                globalFun.addClass(that, 'error');
	                showErrorAfter && createElement(this, errorMsg);
	            } else {
	                getResult = '';
	                globalFun.removeClass(that, 'error');
	                globalFun.addClass(that, 'valid');
	                showErrorAfter && removeElement(this);
	            }
	        });
	        return getResult;
	    },
	    isMobileExist: function isMobileExist(errorMsg, showErrorAfter) {
	        var getResult = '',
	            that = this;
	        commonFun.useAjax({
	            type: 'GET',
	            async: false,
	            url: '/register/user/mobile/' + this.value + '/is-exist'
	        }, function (response) {
	            if (response.data.status) {
	                // 如果为true说明手机已存在或已注册
	                getResult = errorMsg;
	                globalFun.addClass(that, 'error');
	                showErrorAfter && createElement(that, errorMsg);
	            } else {
	                getResult = '';
	                globalFun.removeClass(that, 'error');
	                globalFun.addClass(that, 'valid');
	                showErrorAfter && removeElement(that);
	            }
	        });
	        return getResult;
	    },
	    //推荐人是非存在
	    isReferrerExist: function isReferrerExist(errorMsg, showErrorAfter) {
	        var getResult = '',
	            that = this;
	        //只验证推荐人是否存在，不验证是否为空
	        if (this.value == '') {
	            getResult = '';
	            globalFun.removeClass(that, 'error');
	            showErrorAfter && removeElement(that);
	            return '';
	        }
	        commonFun.useAjax({
	            type: 'GET',
	            async: false,
	            url: '/register/user/referrer/' + this.value + '/is-exist'
	        }, function (response) {
	            if (response.data.status) {
	                // 如果为true说明推荐人存在
	                getResult = '';
	                globalFun.removeClass(that, 'error');
	                showErrorAfter && removeElement(that);
	            } else {
	                getResult = errorMsg;
	                globalFun.addClass(that, 'error');
	                showErrorAfter && createElement(that, errorMsg);
	            }
	        });
	        return getResult;
	    }

	};
	// *****Validator验证类*******

	function ValidatorForm(cache, checkOption) {
	    this.cache = [];
	    this.checkOption = {};
	    this.newStrategy = function (name, callback) {
	        strategies[name] = function (data) {
	            callback && callback(data);
	        };
	    };
	    this.add = function (dom, rules, errorAfter) {
	        var self = this;
	        self.checkOption[dom.name] = [];
	        for (var i = 0, rule; rule = rules[i++];) {
	            self.checkOption[dom.name].push(rule);
	        }

	        self.cache.push(function (thisDom) {
	            var domName = thisDom.name;
	            var domOption = self.checkOption[domName];
	            var len = domOption.length,
	                getErrorMsg;

	            for (var j = 0; j < len; j++) {
	                var strategy = domOption[j].strategy.split(':').shift();
	                var errorMsg = domOption[j].errorMsg;
	                var optionParams = [];
	                optionParams.push(errorMsg);
	                var secondParam = domOption[j].strategy.split(':')[1];

	                secondParam && optionParams.push(secondParam);
	                errorAfter && optionParams.push(errorAfter);
	                getErrorMsg = strategies[strategy].apply(thisDom, optionParams);

	                if (getErrorMsg) {
	                    break; //跳出for循环
	                }
	            }
	            return getErrorMsg;
	        });
	    };
	    this.start = function (dom) {
	        var validatorFunc = this.cache[0];
	        var errorMsg = validatorFunc(dom);
	        if (errorMsg) {
	            return errorMsg;
	        }
	    };
	}

	module.exports = ValidatorForm;

/***/ }

});