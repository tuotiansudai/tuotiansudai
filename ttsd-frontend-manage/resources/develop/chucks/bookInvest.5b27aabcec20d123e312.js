webpackJsonp([7],{

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

/***/ },

/***/ 57:
/***/ function(module, exports) {

	'use strict';

	var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

	/**
	* autoNumeric.js
	* @author: Bob Knothe
	* @author: Sokolov Yura
	* @version: 1.9.26 - 2014-10-07 GMT 2:00 PM
	*
	* Created by Robert J. Knothe on 2010-10-25. Please report any bugs to https://github.com/BobKnothe/autoNumeric
	* Created by Sokolov Yura on 2010-11-07
	*
	* Copyright (c) 2011 Robert J. Knothe http://www.decorplanit.com/plugin/
	*
	* The MIT License (http://www.opensource.org/licenses/mit-license.php)
	*
	* Permission is hereby granted, free of charge, to any person
	* obtaining a copy of this software and associated documentation
	* files (the "Software"), to deal in the Software without
	* restriction, including without limitation the rights to use,
	* copy, modify, merge, publish, distribute, sublicense, and/or sell
	* copies of the Software, and to permit persons to whom the
	* Software is furnished to do so, subject to the following
	* conditions:
	*
	* The above copyright notice and this permission notice shall be
	* included in all copies or substantial portions of the Software.
	*
	* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
	* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
	* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
	* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
	* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
	* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
	* OTHER DEALINGS IN THE SOFTWARE.
	*/
	(function ($) {
	    "use strict";
	    /*jslint browser: true*/
	    /*global jQuery: false*/
	    /* Cross browser routine for getting selected range/cursor position
	     */

	    function getElementSelection(that) {
	        var position = {};
	        if (that.selectionStart === undefined) {
	            that.focus();
	            var select = document.selection.createRange();
	            position.length = select.text.length;
	            select.moveStart('character', -that.value.length);
	            position.end = select.text.length;
	            position.start = position.end - position.length;
	        } else {
	            position.start = that.selectionStart;
	            position.end = that.selectionEnd;
	            position.length = position.end - position.start;
	        }
	        return position;
	    }
	    /**
	     * Cross browser routine for setting selected range/cursor position
	     */
	    function setElementSelection(that, start, end) {
	        if (that.selectionStart === undefined) {
	            that.focus();
	            var r = that.createTextRange();
	            r.collapse(true);
	            r.moveEnd('character', end);
	            r.moveStart('character', start);
	            r.select();
	        } else {
	            that.selectionStart = start;
	            that.selectionEnd = end;
	        }
	    }
	    /**
	     * run callbacks in parameters if any
	     * any parameter could be a callback:
	     * - a function, which invoked with jQuery element, parameters and this parameter name and returns parameter value
	     * - a name of function, attached to $(selector).autoNumeric.functionName(){} - which was called previously
	     */
	    function runCallbacks($this, settings) {
	        /**
	         * loops through the settings object (option array) to find the following
	         * k = option name example k=aNum
	         * val = option value example val=0123456789
	         */
	        $.each(settings, function (k, val) {
	            if (typeof val === 'function') {
	                settings[k] = val($this, settings, k);
	            } else if (typeof $this.autoNumeric[val] === 'function') {
	                /**
	                 * calls the attached function from the html5 data example: data-a-sign="functionName"
	                 */
	                settings[k] = $this.autoNumeric[val]($this, settings, k);
	            }
	        });
	    }
	    function convertKeyToNumber(settings, key) {
	        if (typeof settings[key] === 'string') {
	            settings[key] *= 1;
	        }
	    }
	    /**
	     * Preparing user defined options for further usage
	     * merge them with defaults appropriately
	     */
	    function autoCode($this, settings) {
	        runCallbacks($this, settings);
	        settings.oEvent = null;
	        settings.tagList = ['b', 'caption', 'cite', 'code', 'dd', 'del', 'div', 'dfn', 'dt', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'ins', 'kdb', 'label', 'li', 'output', 'p', 'q', 's', 'sample', 'span', 'strong', 'td', 'th', 'u', 'var'];
	        var vmax = settings.vMax.toString().split('.'),
	            vmin = !settings.vMin && settings.vMin !== 0 ? [] : settings.vMin.toString().split('.');
	        convertKeyToNumber(settings, 'vMax');
	        convertKeyToNumber(settings, 'vMin');
	        convertKeyToNumber(settings, 'mDec'); /** set mDec if not defined by user */
	        settings.mDec = settings.mRound === 'CHF' ? '2' : settings.mDec;
	        settings.allowLeading = true;
	        settings.aNeg = settings.vMin < 0 ? '-' : '';
	        vmax[0] = vmax[0].replace('-', '');
	        vmin[0] = vmin[0].replace('-', '');
	        settings.mInt = Math.max(vmax[0].length, vmin[0].length, 1);
	        if (settings.mDec === null) {
	            var vmaxLength = 0,
	                vminLength = 0;
	            if (vmax[1]) {
	                vmaxLength = vmax[1].length;
	            }
	            if (vmin[1]) {
	                vminLength = vmin[1].length;
	            }
	            settings.mDec = Math.max(vmaxLength, vminLength);
	        } /** set alternative decimal separator key */
	        if (settings.altDec === null && settings.mDec > 0) {
	            if (settings.aDec === '.' && settings.aSep !== ',') {
	                settings.altDec = ',';
	            } else if (settings.aDec === ',' && settings.aSep !== '.') {
	                settings.altDec = '.';
	            }
	        }
	        /** cache regexps for autoStrip */
	        var aNegReg = settings.aNeg ? '([-\\' + settings.aNeg + ']?)' : '(-?)';
	        settings.aNegRegAutoStrip = aNegReg;
	        settings.skipFirstAutoStrip = new RegExp(aNegReg + '[^-' + (settings.aNeg ? '\\' + settings.aNeg : '') + '\\' + settings.aDec + '\\d]' + '.*?(\\d|\\' + settings.aDec + '\\d)');
	        settings.skipLastAutoStrip = new RegExp('(\\d\\' + settings.aDec + '?)[^\\' + settings.aDec + '\\d]\\D*$');
	        var allowed = '-' + settings.aNum + '\\' + settings.aDec;
	        settings.allowedAutoStrip = new RegExp('[^' + allowed + ']', 'gi');
	        settings.numRegAutoStrip = new RegExp(aNegReg + '(?:\\' + settings.aDec + '?(\\d+\\' + settings.aDec + '\\d+)|(\\d*(?:\\' + settings.aDec + '\\d*)?))');
	        return settings;
	    }
	    /**
	     * strip all unwanted characters and leave only a number alert
	     */
	    function autoStrip(s, settings, strip_zero) {
	        if (settings.aSign) {
	            /** remove currency sign */
	            while (s.indexOf(settings.aSign) > -1) {
	                s = s.replace(settings.aSign, '');
	            }
	        }
	        s = s.replace(settings.skipFirstAutoStrip, '$1$2'); /** first replace anything before digits */
	        s = s.replace(settings.skipLastAutoStrip, '$1'); /** then replace anything after digits */
	        s = s.replace(settings.allowedAutoStrip, ''); /** then remove any uninterested characters */
	        if (settings.altDec) {
	            s = s.replace(settings.altDec, settings.aDec);
	        } /** get only number string */
	        var m = s.match(settings.numRegAutoStrip);
	        s = m ? [m[1], m[2], m[3]].join('') : '';
	        if ((settings.lZero === 'allow' || settings.lZero === 'keep') && strip_zero !== 'strip') {
	            var parts = [],
	                nSign = '';
	            parts = s.split(settings.aDec);
	            if (parts[0].indexOf('-') !== -1) {
	                nSign = '-';
	                parts[0] = parts[0].replace('-', '');
	            }
	            if (parts[0].length > settings.mInt && parts[0].charAt(0) === '0') {
	                /** strip leading zero if need */
	                parts[0] = parts[0].slice(1);
	            }
	            s = nSign + parts.join(settings.aDec);
	        }
	        if (strip_zero && settings.lZero === 'deny' || strip_zero && settings.lZero === 'allow' && settings.allowLeading === false) {
	            var strip_reg = '^' + settings.aNegRegAutoStrip + '0*(\\d' + (strip_zero === 'leading' ? ')' : '|$)');
	            strip_reg = new RegExp(strip_reg);
	            s = s.replace(strip_reg, '$1$2');
	        }
	        return s;
	    }
	    /**
	     * places or removes brackets on negative values
	     */
	    function negativeBracket(s, nBracket, oEvent) {
	        /** oEvent = settings.oEvent */
	        nBracket = nBracket.split(',');
	        if (oEvent === 'set' || oEvent === 'focusout') {
	            s = s.replace('-', '');
	            s = nBracket[0] + s + nBracket[1];
	        } else if ((oEvent === 'get' || oEvent === 'focusin' || oEvent === 'pageLoad') && s.charAt(0) === nBracket[0]) {
	            s = s.replace(nBracket[0], '-');
	            s = s.replace(nBracket[1], '');
	        }
	        return s;
	    }
	    /**
	     * truncate decimal part of a number
	     */
	    function truncateDecimal(s, aDec, mDec) {
	        if (aDec && mDec) {
	            var parts = s.split(aDec);
	            /** truncate decimal part to satisfying length
	             * cause we would round it anyway */
	            if (parts[1] && parts[1].length > mDec) {
	                if (mDec > 0) {
	                    parts[1] = parts[1].substring(0, mDec);
	                    s = parts.join(aDec);
	                } else {
	                    s = parts[0];
	                }
	            }
	        }
	        return s;
	    }
	    /**
	     * prepare number string to be converted to real number
	     */
	    function fixNumber(s, aDec, aNeg) {
	        if (aDec && aDec !== '.') {
	            s = s.replace(aDec, '.');
	        }
	        if (aNeg && aNeg !== '-') {
	            s = s.replace(aNeg, '-');
	        }
	        if (!s.match(/\d/)) {
	            s += '0';
	        }
	        return s;
	    }
	    /**
	     * function to handle numbers less than 0 that are stored in Exponential notation ex: .0000001 stored as 1e-7
	     */
	    function checkValue(value, settings) {
	        if (value) {
	            var checkSmall = +value;
	            if (checkSmall < 0.000001 && checkSmall > -1) {
	                value = +value;
	                if (value < 0.000001 && value > 0) {
	                    value = (value + 10).toString();
	                    value = value.substring(1);
	                }
	                if (value < 0 && value > -1) {
	                    value = (value - 10).toString();
	                    value = '-' + value.substring(2);
	                }
	                value = value.toString();
	            } else {
	                var parts = value.split('.');
	                if (parts[1] !== undefined) {
	                    if (+parts[1] === 0) {
	                        value = parts[0];
	                    } else {
	                        parts[1] = parts[1].replace(/0*$/, '');
	                        value = parts.join('.');
	                    }
	                }
	            }
	        }
	        return settings.lZero === 'keep' ? value : value.replace(/^0*(\d)/, '$1');
	    }
	    /**
	     * prepare real number to be converted to our format
	     */
	    function presentNumber(s, aDec, aNeg) {
	        if (aNeg && aNeg !== '-') {
	            s = s.replace('-', aNeg);
	        }
	        if (aDec && aDec !== '.') {
	            s = s.replace('.', aDec);
	        }
	        return s;
	    }
	    /**
	     * checking that number satisfy format conditions
	     * and lays between settings.vMin and settings.vMax
	     * and the string length does not exceed the digits in settings.vMin and settings.vMax
	     */
	    function autoCheck(s, settings) {
	        s = autoStrip(s, settings);
	        s = truncateDecimal(s, settings.aDec, settings.mDec);
	        s = fixNumber(s, settings.aDec, settings.aNeg);
	        var value = +s;
	        if (settings.oEvent === 'set' && (value < settings.vMin || value > settings.vMax)) {
	            $.error("The value (" + value + ") from the 'set' method falls outside of the vMin / vMax range");
	        }
	        return value >= settings.vMin && value <= settings.vMax;
	    }
	    /**
	     * private function to check for empty value
	     */
	    function checkEmpty(iv, settings, signOnEmpty) {
	        if (iv === '' || iv === settings.aNeg) {
	            if (settings.wEmpty === 'zero') {
	                return iv + '0';
	            }
	            if (settings.wEmpty === 'sign' || signOnEmpty) {
	                return iv + settings.aSign;
	            }
	            return iv;
	        }
	        return null;
	    }
	    /**
	     * private function that formats our number
	     */
	    function autoGroup(iv, settings) {
	        iv = autoStrip(iv, settings);
	        var testNeg = iv.replace(',', '.'),
	            empty = checkEmpty(iv, settings, true);
	        if (empty !== null) {
	            return empty;
	        }
	        var digitalGroup = '';
	        if (settings.dGroup === 2) {
	            digitalGroup = /(\d)((\d)(\d{2}?)+)$/;
	        } else if (settings.dGroup === 4) {
	            digitalGroup = /(\d)((\d{4}?)+)$/;
	        } else {
	            digitalGroup = /(\d)((\d{3}?)+)$/;
	        } /** splits the string at the decimal string */
	        var ivSplit = iv.split(settings.aDec);
	        if (settings.altDec && ivSplit.length === 1) {
	            ivSplit = iv.split(settings.altDec);
	        } /** assigns the whole number to the a varibale (s) */
	        var s = ivSplit[0];
	        if (settings.aSep) {
	            while (digitalGroup.test(s)) {
	                /** re-inserts the thousand sepparator via a regualer expression */
	                s = s.replace(digitalGroup, '$1' + settings.aSep + '$2');
	            }
	        }
	        if (settings.mDec !== 0 && ivSplit.length > 1) {
	            if (ivSplit[1].length > settings.mDec) {
	                ivSplit[1] = ivSplit[1].substring(0, settings.mDec);
	            } /** joins the whole number with the deciaml value */
	            iv = s + settings.aDec + ivSplit[1];
	        } else {
	            /** if whole numbers only */
	            iv = s;
	        }
	        if (settings.aSign) {
	            var has_aNeg = iv.indexOf(settings.aNeg) !== -1;
	            iv = iv.replace(settings.aNeg, '');
	            iv = settings.pSign === 'p' ? settings.aSign + iv : iv + settings.aSign;
	            if (has_aNeg) {
	                iv = settings.aNeg + iv;
	            }
	        }
	        if (settings.oEvent === 'set' && testNeg < 0 && settings.nBracket !== null) {
	            /** removes the negative sign and places brackets */
	            iv = negativeBracket(iv, settings.nBracket, settings.oEvent);
	        }
	        return iv;
	    }
	    /**
	     * round number after setting by pasting or $().autoNumericSet()
	     * private function for round the number
	     * please note this handled as text - JavaScript math function can return inaccurate values
	     * also this offers multiple rounding methods that are not easily accomplished in JavaScript
	     */
	    function autoRound(iv, settings) {
	        /** value to string */
	        iv = iv === '' ? '0' : iv.toString();
	        convertKeyToNumber(settings, 'mDec'); /** set mDec to number needed when mDec set by 'update method */
	        if (settings.mRound === 'CHF') {
	            iv = (Math.round(iv * 20) / 20).toString();
	        }
	        var ivRounded = '',
	            i = 0,
	            nSign = '',
	            rDec = typeof settings.aPad === 'boolean' || settings.aPad === null ? settings.aPad ? settings.mDec : 0 : +settings.aPad;
	        var truncateZeros = function truncateZeros(ivRounded) {
	            /** truncate not needed zeros */
	            var regex = rDec === 0 ? /(\.(?:\d*[1-9])?)0*$/ : rDec === 1 ? /(\.\d(?:\d*[1-9])?)0*$/ : new RegExp('(\\.\\d{' + rDec + '}(?:\\d*[1-9])?)0*$');
	            ivRounded = ivRounded.replace(regex, '$1'); /** If there are no decimal places, we don't need a decimal point at the end */
	            if (rDec === 0) {
	                ivRounded = ivRounded.replace(/\.$/, '');
	            }
	            return ivRounded;
	        };
	        if (iv.charAt(0) === '-') {
	            /** Checks if the iv (input Value)is a negative value */
	            nSign = '-';
	            iv = iv.replace('-', ''); /** removes the negative sign will be added back later if required */
	        }
	        if (!iv.match(/^\d/)) {
	            /** append a zero if first character is not a digit (then it is likely to be a dot)*/
	            iv = '0' + iv;
	        }
	        if (nSign === '-' && +iv === 0) {
	            /** determines if the value is zero - if zero no negative sign */
	            nSign = '';
	        }
	        if (+iv > 0 && settings.lZero !== 'keep' || iv.length > 0 && settings.lZero === 'allow') {
	            /** trims leading zero's if needed */
	            iv = iv.replace(/^0*(\d)/, '$1');
	        }
	        var dPos = iv.lastIndexOf('.'),
	            /** virtual decimal position */
	        vdPos = dPos === -1 ? iv.length - 1 : dPos,
	            /** checks decimal places to determine if rounding is required */
	        cDec = iv.length - 1 - vdPos; /** check if no rounding is required */
	        if (cDec <= settings.mDec) {
	            ivRounded = iv; /** check if we need to pad with zeros */
	            if (cDec < rDec) {
	                if (dPos === -1) {
	                    ivRounded += '.';
	                }
	                var zeros = '000000';
	                while (cDec < rDec) {
	                    zeros = zeros.substring(0, rDec - cDec);
	                    ivRounded += zeros;
	                    cDec += zeros.length;
	                }
	            } else if (cDec > rDec) {
	                ivRounded = truncateZeros(ivRounded);
	            } else if (cDec === 0 && rDec === 0) {
	                ivRounded = ivRounded.replace(/\.$/, '');
	            }
	            if (settings.mRound !== 'CHF') {
	                return +ivRounded === 0 ? ivRounded : nSign + ivRounded;
	            }
	            if (settings.mRound === 'CHF') {
	                dPos = ivRounded.lastIndexOf('.');
	                iv = ivRounded;
	            }
	        } /** rounded length of the string after rounding */
	        var rLength = dPos + settings.mDec,
	            tRound = +iv.charAt(rLength + 1),
	            ivArray = iv.substring(0, rLength + 1).split(''),
	            odd = iv.charAt(rLength) === '.' ? iv.charAt(rLength - 1) % 2 : iv.charAt(rLength) % 2,
	            onePass = true;
	        if (odd !== 1) {
	            odd = odd === 0 && iv.substring(rLength + 2, iv.length) > 0 ? 1 : 0;
	        }
	        if (tRound > 4 && settings.mRound === 'S' || /** Round half up symmetric */
	        tRound > 4 && settings.mRound === 'A' && nSign === '' || /** Round half up asymmetric positive values */
	        tRound > 5 && settings.mRound === 'A' && nSign === '-' || /** Round half up asymmetric negative values */
	        tRound > 5 && settings.mRound === 's' || /** Round half down symmetric */
	        tRound > 5 && settings.mRound === 'a' && nSign === '' || /** Round half down asymmetric positive values */
	        tRound > 4 && settings.mRound === 'a' && nSign === '-' || /** Round half down asymmetric negative values */
	        tRound > 5 && settings.mRound === 'B' || /** Round half even "Banker's Rounding" */
	        tRound === 5 && settings.mRound === 'B' && odd === 1 || /** Round half even "Banker's Rounding" */
	        tRound > 0 && settings.mRound === 'C' && nSign === '' || /** Round to ceiling toward positive infinite */
	        tRound > 0 && settings.mRound === 'F' && nSign === '-' || /** Round to floor toward negative infinite */
	        tRound > 0 && settings.mRound === 'U' || settings.mRound === 'CHF') {
	            /** round up away from zero */
	            for (i = ivArray.length - 1; i >= 0; i -= 1) {
	                /** Round up the last digit if required, and continue until no more 9's are found */
	                if (ivArray[i] !== '.') {
	                    if (settings.mRound === 'CHF' && ivArray[i] <= 2 && onePass) {
	                        ivArray[i] = 0;
	                        onePass = false;
	                        break;
	                    }
	                    if (settings.mRound === 'CHF' && ivArray[i] <= 7 && onePass) {
	                        ivArray[i] = 5;
	                        onePass = false;
	                        break;
	                    }
	                    if (settings.mRound === 'CHF' && onePass) {
	                        ivArray[i] = 10;
	                        onePass = false;
	                    } else {
	                        ivArray[i] = +ivArray[i] + 1;
	                    }
	                    if (ivArray[i] < 10) {
	                        break;
	                    }
	                    if (i > 0) {
	                        ivArray[i] = '0';
	                    }
	                }
	            }
	        }
	        ivArray = ivArray.slice(0, rLength + 1); /** Reconstruct the string, converting any 10's to 0's */
	        ivRounded = truncateZeros(ivArray.join('')); /** return rounded value */
	        return +ivRounded === 0 ? ivRounded : nSign + ivRounded;
	    }
	    /**
	     * Holder object for field properties
	     */
	    function AutoNumericHolder(that, settings) {
	        this.settings = settings;
	        this.that = that;
	        this.$that = $(that);
	        this.formatted = false;
	        this.settingsClone = autoCode(this.$that, this.settings);
	        this.value = that.value;
	    }
	    AutoNumericHolder.prototype = {
	        init: function init(e) {
	            this.value = this.that.value;
	            this.settingsClone = autoCode(this.$that, this.settings);
	            this.ctrlKey = e.ctrlKey;
	            this.cmdKey = e.metaKey;
	            this.shiftKey = e.shiftKey;
	            this.selection = getElementSelection(this.that); /** keypress event overwrites meaningful value of e.keyCode */
	            if (e.type === 'keydown' || e.type === 'keyup') {
	                this.kdCode = e.keyCode;
	            }
	            this.which = e.which;
	            this.processed = false;
	            this.formatted = false;
	        },
	        setSelection: function setSelection(start, end, setReal) {
	            start = Math.max(start, 0);
	            end = Math.min(end, this.that.value.length);
	            this.selection = {
	                start: start,
	                end: end,
	                length: end - start
	            };
	            if (setReal === undefined || setReal) {
	                setElementSelection(this.that, start, end);
	            }
	        },
	        setPosition: function setPosition(pos, setReal) {
	            this.setSelection(pos, pos, setReal);
	        },
	        getBeforeAfter: function getBeforeAfter() {
	            var value = this.value,
	                left = value.substring(0, this.selection.start),
	                right = value.substring(this.selection.end, value.length);
	            return [left, right];
	        },
	        getBeforeAfterStriped: function getBeforeAfterStriped() {
	            var parts = this.getBeforeAfter();
	            parts[0] = autoStrip(parts[0], this.settingsClone);
	            parts[1] = autoStrip(parts[1], this.settingsClone);
	            return parts;
	        },
	        /**
	         * strip parts from excess characters and leading zeroes
	         */
	        normalizeParts: function normalizeParts(left, right) {
	            var settingsClone = this.settingsClone;
	            right = autoStrip(right, settingsClone); /** if right is not empty and first character is not aDec, */
	            /** we could strip all zeros, otherwise only leading */
	            var strip = right.match(/^\d/) ? true : 'leading';
	            left = autoStrip(left, settingsClone, strip); /** prevents multiple leading zeros from being entered */
	            if ((left === '' || left === settingsClone.aNeg) && settingsClone.lZero === 'deny') {
	                if (right > '') {
	                    right = right.replace(/^0*(\d)/, '$1');
	                }
	            }
	            var new_value = left + right; /** insert zero if has leading dot */
	            if (settingsClone.aDec) {
	                var m = new_value.match(new RegExp('^' + settingsClone.aNegRegAutoStrip + '\\' + settingsClone.aDec));
	                if (m) {
	                    left = left.replace(m[1], m[1] + '0');
	                    new_value = left + right;
	                }
	            } /** insert zero if number is empty and io.wEmpty == 'zero' */
	            if (settingsClone.wEmpty === 'zero' && (new_value === settingsClone.aNeg || new_value === '')) {
	                left += '0';
	            }
	            return [left, right];
	        },
	        /**
	         * set part of number to value keeping position of cursor
	         */
	        setValueParts: function setValueParts(left, right) {
	            var settingsClone = this.settingsClone,
	                parts = this.normalizeParts(left, right),
	                new_value = parts.join(''),
	                position = parts[0].length;
	            if (autoCheck(new_value, settingsClone)) {
	                new_value = truncateDecimal(new_value, settingsClone.aDec, settingsClone.mDec);
	                if (position > new_value.length) {
	                    position = new_value.length;
	                }
	                this.value = new_value;
	                this.setPosition(position, false);
	                return true;
	            }
	            return false;
	        },
	        /**
	         * helper function for expandSelectionOnSign
	         * returns sign position of a formatted value
	         */
	        signPosition: function signPosition() {
	            var settingsClone = this.settingsClone,
	                aSign = settingsClone.aSign,
	                that = this.that;
	            if (aSign) {
	                var aSignLen = aSign.length;
	                if (settingsClone.pSign === 'p') {
	                    var hasNeg = settingsClone.aNeg && that.value && that.value.charAt(0) === settingsClone.aNeg;
	                    return hasNeg ? [1, aSignLen + 1] : [0, aSignLen];
	                }
	                var valueLen = that.value.length;
	                return [valueLen - aSignLen, valueLen];
	            }
	            return [1000, -1];
	        },
	        /**
	         * expands selection to cover whole sign
	         * prevents partial deletion/copying/overwriting of a sign
	         */
	        expandSelectionOnSign: function expandSelectionOnSign(setReal) {
	            var sign_position = this.signPosition(),
	                selection = this.selection;
	            if (selection.start < sign_position[1] && selection.end > sign_position[0]) {
	                /** if selection catches something except sign and catches only space from sign */
	                if ((selection.start < sign_position[0] || selection.end > sign_position[1]) && this.value.substring(Math.max(selection.start, sign_position[0]), Math.min(selection.end, sign_position[1])).match(/^\s*$/)) {
	                    /** then select without empty space */
	                    if (selection.start < sign_position[0]) {
	                        this.setSelection(selection.start, sign_position[0], setReal);
	                    } else {
	                        this.setSelection(sign_position[1], selection.end, setReal);
	                    }
	                } else {
	                    /** else select with whole sign */
	                    this.setSelection(Math.min(selection.start, sign_position[0]), Math.max(selection.end, sign_position[1]), setReal);
	                }
	            }
	        },
	        /**
	         * try to strip pasted value to digits
	         */
	        checkPaste: function checkPaste() {
	            if (this.valuePartsBeforePaste !== undefined) {
	                var parts = this.getBeforeAfter(),
	                    oldParts = this.valuePartsBeforePaste;
	                delete this.valuePartsBeforePaste; /** try to strip pasted value first */
	                parts[0] = parts[0].substr(0, oldParts[0].length) + autoStrip(parts[0].substr(oldParts[0].length), this.settingsClone);
	                if (!this.setValueParts(parts[0], parts[1])) {
	                    this.value = oldParts.join('');
	                    this.setPosition(oldParts[0].length, false);
	                }
	            }
	        },
	        /**
	         * process pasting, cursor moving and skipping of not interesting keys
	         * if returns true, further processing is not performed
	         */
	        skipAllways: function skipAllways(e) {
	            var kdCode = this.kdCode,
	                which = this.which,
	                ctrlKey = this.ctrlKey,
	                cmdKey = this.cmdKey,
	                shiftKey = this.shiftKey; /** catch the ctrl up on ctrl-v */
	            if ((ctrlKey || cmdKey) && e.type === 'keyup' && this.valuePartsBeforePaste !== undefined || shiftKey && kdCode === 45) {
	                this.checkPaste();
	                return false;
	            }
	            /** codes are taken from http://www.cambiaresearch.com/c4/702b8cd1-e5b0-42e6-83ac-25f0306e3e25/Javascript-Char-Codes-Key-Codes.aspx
	             * skip Fx keys, windows keys, other special keys
	             */
	            if (kdCode >= 112 && kdCode <= 123 || kdCode >= 91 && kdCode <= 93 || kdCode >= 9 && kdCode <= 31 || kdCode < 8 && (which === 0 || which === kdCode) || kdCode === 144 || kdCode === 145 || kdCode === 45) {
	                return true;
	            }
	            if ((ctrlKey || cmdKey) && kdCode === 65) {
	                /** if select all (a=65)*/
	                return true;
	            }
	            if ((ctrlKey || cmdKey) && (kdCode === 67 || kdCode === 86 || kdCode === 88)) {
	                /** if copy (c=67) paste (v=86) or cut (x=88) */
	                if (e.type === 'keydown') {
	                    this.expandSelectionOnSign();
	                }
	                if (kdCode === 86 || kdCode === 45) {
	                    /** try to prevent wrong paste */
	                    if (e.type === 'keydown' || e.type === 'keypress') {
	                        if (this.valuePartsBeforePaste === undefined) {
	                            this.valuePartsBeforePaste = this.getBeforeAfter();
	                        }
	                    } else {
	                        this.checkPaste();
	                    }
	                }
	                return e.type === 'keydown' || e.type === 'keypress' || kdCode === 67;
	            }
	            if (ctrlKey || cmdKey) {
	                return true;
	            }
	            if (kdCode === 37 || kdCode === 39) {
	                /** jump over thousand separator */
	                var aSep = this.settingsClone.aSep,
	                    start = this.selection.start,
	                    value = this.that.value;
	                if (e.type === 'keydown' && aSep && !this.shiftKey) {
	                    if (kdCode === 37 && value.charAt(start - 2) === aSep) {
	                        this.setPosition(start - 1);
	                    } else if (kdCode === 39 && value.charAt(start + 1) === aSep) {
	                        this.setPosition(start + 1);
	                    }
	                }
	                return true;
	            }
	            if (kdCode >= 34 && kdCode <= 40) {
	                return true;
	            }
	            return false;
	        },
	        /**
	         * process deletion of characters
	         * returns true if processing performed
	         */
	        processAllways: function processAllways() {
	            var parts; /** process backspace or delete */
	            if (this.kdCode === 8 || this.kdCode === 46) {
	                if (!this.selection.length) {
	                    parts = this.getBeforeAfterStriped();
	                    if (this.kdCode === 8) {
	                        parts[0] = parts[0].substring(0, parts[0].length - 1);
	                    } else {
	                        parts[1] = parts[1].substring(1, parts[1].length);
	                    }
	                    this.setValueParts(parts[0], parts[1]);
	                } else {
	                    this.expandSelectionOnSign(false);
	                    parts = this.getBeforeAfterStriped();
	                    this.setValueParts(parts[0], parts[1]);
	                }
	                return true;
	            }
	            return false;
	        },
	        /**
	         * process insertion of characters
	         * returns true if processing performed
	         */
	        processKeypress: function processKeypress() {
	            var settingsClone = this.settingsClone,
	                cCode = String.fromCharCode(this.which),
	                parts = this.getBeforeAfterStriped(),
	                left = parts[0],
	                right = parts[1]; /** start rules when the decimal character key is pressed */
	            /** always use numeric pad dot to insert decimal separator */
	            if (cCode === settingsClone.aDec || settingsClone.altDec && cCode === settingsClone.altDec || (cCode === '.' || cCode === ',') && this.kdCode === 110) {
	                /** do not allow decimal character if no decimal part allowed */
	                if (!settingsClone.mDec || !settingsClone.aDec) {
	                    return true;
	                } /** do not allow decimal character before aNeg character */
	                if (settingsClone.aNeg && right.indexOf(settingsClone.aNeg) > -1) {
	                    return true;
	                } /** do not allow decimal character if other decimal character present */
	                if (left.indexOf(settingsClone.aDec) > -1) {
	                    return true;
	                }
	                if (right.indexOf(settingsClone.aDec) > 0) {
	                    return true;
	                }
	                if (right.indexOf(settingsClone.aDec) === 0) {
	                    right = right.substr(1);
	                }
	                this.setValueParts(left + settingsClone.aDec, right);
	                return true;
	            }
	            /**
	             * start rule on negative sign & prevent minus if not allowed
	             */
	            if (cCode === '-' || cCode === '+') {
	                if (!settingsClone.aNeg) {
	                    return true;
	                } /** caret is always after minus */
	                if (left === '' && right.indexOf(settingsClone.aNeg) > -1) {
	                    left = settingsClone.aNeg;
	                    right = right.substring(1, right.length);
	                } /** change sign of number, remove part if should */
	                if (left.charAt(0) === settingsClone.aNeg) {
	                    left = left.substring(1, left.length);
	                } else {
	                    left = cCode === '-' ? settingsClone.aNeg + left : left;
	                }
	                this.setValueParts(left, right);
	                return true;
	            } /** digits */
	            if (cCode >= '0' && cCode <= '9') {
	                /** if try to insert digit before minus */
	                if (settingsClone.aNeg && left === '' && right.indexOf(settingsClone.aNeg) > -1) {
	                    left = settingsClone.aNeg;
	                    right = right.substring(1, right.length);
	                }
	                if (settingsClone.vMax <= 0 && settingsClone.vMin < settingsClone.vMax && this.value.indexOf(settingsClone.aNeg) === -1 && cCode !== '0') {
	                    left = settingsClone.aNeg + left;
	                }
	                this.setValueParts(left + cCode, right);
	                return true;
	            } /** prevent any other character */
	            return true;
	        },
	        /**
	         * formatting of just processed value with keeping of cursor position
	         */
	        formatQuick: function formatQuick() {
	            var settingsClone = this.settingsClone,
	                parts = this.getBeforeAfterStriped(),
	                leftLength = this.value;
	            if ((settingsClone.aSep === '' || settingsClone.aSep !== '' && leftLength.indexOf(settingsClone.aSep) === -1) && (settingsClone.aSign === '' || settingsClone.aSign !== '' && leftLength.indexOf(settingsClone.aSign) === -1)) {
	                var subParts = [],
	                    nSign = '';
	                subParts = leftLength.split(settingsClone.aDec);
	                if (subParts[0].indexOf('-') > -1) {
	                    nSign = '-';
	                    subParts[0] = subParts[0].replace('-', '');
	                    parts[0] = parts[0].replace('-', '');
	                }
	                if (subParts[0].length > settingsClone.mInt && parts[0].charAt(0) === '0') {
	                    /** strip leading zero if need */
	                    parts[0] = parts[0].slice(1);
	                }
	                parts[0] = nSign + parts[0];
	            }
	            var value = autoGroup(this.value, this.settingsClone),
	                position = value.length;
	            if (value) {
	                /** prepare regexp which searches for cursor position from unformatted left part */
	                var left_ar = parts[0].split(''),
	                    i = 0;
	                for (i; i < left_ar.length; i += 1) {
	                    /** thanks Peter Kovari */
	                    if (!left_ar[i].match('\\d')) {
	                        left_ar[i] = '\\' + left_ar[i];
	                    }
	                }
	                var leftReg = new RegExp('^.*?' + left_ar.join('.*?'));
	                /** search cursor position in formatted value */
	                var newLeft = value.match(leftReg);
	                if (newLeft) {
	                    position = newLeft[0].length;
	                    /** if we are just before sign which is in prefix position */
	                    if ((position === 0 && value.charAt(0) !== settingsClone.aNeg || position === 1 && value.charAt(0) === settingsClone.aNeg) && settingsClone.aSign && settingsClone.pSign === 'p') {
	                        /** place caret after prefix sign */
	                        position = this.settingsClone.aSign.length + (value.charAt(0) === '-' ? 1 : 0);
	                    }
	                } else if (settingsClone.aSign && settingsClone.pSign === 's') {
	                    /** if we could not find a place for cursor and have a sign as a suffix */
	                    /** place carret before suffix currency sign */
	                    position -= settingsClone.aSign.length;
	                }
	            }
	            this.that.value = value;
	            this.setPosition(position);
	            this.formatted = true;
	        }
	    };
	    /** thanks to Anthony & Evan C */
	    function autoGet(obj) {
	        if (typeof obj === 'string') {
	            obj = obj.replace(/\[/g, "\\[").replace(/\]/g, "\\]");
	            obj = '#' + obj.replace(/(:|\.)/g, '\\$1');
	            /** obj = '#' + obj.replace(/([;&,\.\+\*\~':"\!\^#$%@\[\]\(\)=>\|])/g, '\\$1'); */
	            /** possible modification to replace the above 2 lines */
	        }
	        return $(obj);
	    }

	    function getHolder($that, settings, update) {
	        var data = $that.data('autoNumeric');
	        if (!data) {
	            data = {};
	            $that.data('autoNumeric', data);
	        }
	        var holder = data.holder;
	        if (holder === undefined && settings || update) {
	            holder = new AutoNumericHolder($that.get(0), settings);
	            data.holder = holder;
	        }
	        return holder;
	    }
	    var methods = {
	        init: function init(options) {
	            return this.each(function () {
	                var $this = $(this),
	                    settings = $this.data('autoNumeric'),
	                    /** attempt to grab 'autoNumeric' settings, if they don't exist returns "undefined". */
	                tagData = $this.data(); /** attempt to grab HTML5 data, if they don't exist we'll get "undefined".*/
	                if ((typeof settings === 'undefined' ? 'undefined' : _typeof(settings)) !== 'object') {
	                    /** If we couldn't grab settings, create them from defaults and passed options. */
	                    var defaults = {
	                        /** allowed numeric values
	                         * please do not modify
	                         */
	                        aNum: '0123456789',
	                        /** allowed thousand separator characters
	                         * comma = ','
	                         * period "full stop" = '.'
	                         * apostrophe is escaped = '\''
	                         * space = ' '
	                         * none = ''
	                         * NOTE: do not use numeric characters
	                         */
	                        aSep: ',',
	                        /** digital grouping for the thousand separator used in Format
	                         * dGroup: '2', results in 99,99,99,999 common in India for values less than 1 billion and greater than -1 billion
	                         * dGroup: '3', results in 999,999,999 default
	                         * dGroup: '4', results in 9999,9999,9999 used in some Asian countries
	                         */
	                        dGroup: '3',
	                        /** allowed decimal separator characters
	                         * period "full stop" = '.'
	                         * comma = ','
	                         */
	                        aDec: '.',
	                        /** allow to declare alternative decimal separator which is automatically replaced by aDec
	                         * developed for countries the use a comma ',' as the decimal character
	                         * and have keyboards\numeric pads that have a period 'full stop' as the decimal characters (Spain is an example)
	                         */
	                        altDec: null,
	                        /** allowed currency symbol
	                         * Must be in quotes aSign: '$', a space is allowed aSign: '$ '
	                         */
	                        aSign: '',
	                        /** placement of currency sign
	                         * for prefix pSign: 'p',
	                         * for suffix pSign: 's',
	                         */
	                        pSign: 'p',
	                        /** maximum possible value
	                         * value must be enclosed in quotes and use the period for the decimal point
	                         * value must be larger than vMin
	                         */
	                        vMax: '99999999999.99',
	                        /** minimum possible value
	                         * value must be enclosed in quotes and use the period for the decimal point
	                         * value must be smaller than vMax
	                         */
	                        vMin: '0.00',
	                        /** max number of decimal places = used to override decimal places set by the vMin & vMax values
	                         * value must be enclosed in quotes example mDec: '3',
	                         * This can also set the value via a call back function mDec: 'css:#
	                         */
	                        mDec: null,
	                        /** method used for rounding
	                         * mRound: 'S', Round-Half-Up Symmetric (default)
	                         * mRound: 'A', Round-Half-Up Asymmetric
	                         * mRound: 's', Round-Half-Down Symmetric (lower case s)
	                         * mRound: 'a', Round-Half-Down Asymmetric (lower case a)
	                         * mRound: 'B', Round-Half-Even "Bankers Rounding"
	                         * mRound: 'U', Round Up "Round-Away-From-Zero"
	                         * mRound: 'D', Round Down "Round-Toward-Zero" - same as truncate
	                         * mRound: 'C', Round to Ceiling "Toward Positive Infinity"
	                         * mRound: 'F', Round to Floor "Toward Negative Infinity"
	                         */
	                        mRound: 'S',
	                        /** controls decimal padding
	                         * aPad: true - always Pad decimals with zeros
	                         * aPad: false - does not pad with zeros.
	                         * aPad: `some number` - pad decimals with zero to number different from mDec
	                         * thanks to Jonas Johansson for the suggestion
	                         */
	                        aPad: true,
	                        /** places brackets on negative value -$ 999.99 to (999.99)
	                         * visible only when the field does NOT have focus the left and right symbols should be enclosed in quotes and seperated by a comma
	                         * nBracket: null, nBracket: '(,)', nBracket: '[,]', nBracket: '<,>' or nBracket: '{,}'
	                         */
	                        nBracket: null,
	                        /** Displayed on empty string
	                         * wEmpty: 'empty', - input can be blank
	                         * wEmpty: 'zero', - displays zero
	                         * wEmpty: 'sign', - displays the currency sign
	                         */
	                        wEmpty: 'empty',
	                        /** controls leading zero behavior
	                         * lZero: 'allow', - allows leading zeros to be entered. Zeros will be truncated when entering additional digits. On focusout zeros will be deleted.
	                         * lZero: 'deny', - allows only one leading zero on values less than one
	                         * lZero: 'keep', - allows leading zeros to be entered. on fousout zeros will be retained.
	                         */
	                        lZero: 'allow',
	                        /** determine if the default value will be formatted on page ready.
	                         * true = automatically formats the default value on page ready
	                         * false = will not format the default value
	                         */
	                        aForm: true,
	                        /** future use */
	                        onSomeEvent: function onSomeEvent() {}
	                    };
	                    settings = $.extend({}, defaults, tagData, options); /** Merge defaults, tagData and options */
	                    if (settings.aDec === settings.aSep) {
	                        $.error("autoNumeric will not function properly when the decimal character aDec: '" + settings.aDec + "' and thousand separator aSep: '" + settings.aSep + "' are the same character");
	                        return this;
	                    }
	                    $this.data('autoNumeric', settings); /** Save our new settings */
	                } else {
	                    return this;
	                }
	                settings.runOnce = false;
	                var holder = getHolder($this, settings);
	                if ($.inArray($this.prop('tagName').toLowerCase(), settings.tagList) === -1 && $this.prop('tagName').toLowerCase() !== 'input') {
	                    $.error("The <" + $this.prop('tagName').toLowerCase() + "> is not supported by autoNumeric()");
	                    return this;
	                }
	                if (settings.runOnce === false && settings.aForm) {
	                    /** routine to format default value on page load */
	                    if ($this.is('input[type=text], input[type=hidden], input[type=tel], input:not([type])')) {
	                        var setValue = true;
	                        if ($this[0].value === '' && settings.wEmpty === 'empty') {
	                            $this[0].value = '';
	                            setValue = false;
	                        }
	                        if ($this[0].value === '' && settings.wEmpty === 'sign') {
	                            $this[0].value = settings.aSign;
	                            setValue = false;
	                        }
	                        if (setValue) {
	                            $this.autoNumeric('set', $this.val());
	                        }
	                    }
	                    if ($.inArray($this.prop('tagName').toLowerCase(), settings.tagList) !== -1 && $this.text() !== '') {
	                        $this.autoNumeric('set', $this.text());
	                    }
	                }
	                settings.runOnce = true;
	                if ($this.is('input[type=text], input[type=hidden], input[type=tel], input:not([type])')) {
	                    /**added hidden type */
	                    $this.on('keydown.autoNumeric', function (e) {
	                        holder = getHolder($this);
	                        if (holder.settings.aDec === holder.settings.aSep) {
	                            $.error("autoNumeric will not function properly when the decimal character aDec: '" + holder.settings.aDec + "' and thousand separator aSep: '" + holder.settings.aSep + "' are the same character");
	                            return this;
	                        }
	                        if (holder.that.readOnly) {
	                            holder.processed = true;
	                            return true;
	                        }
	                        /** The below streamed code / comment allows the "enter" keydown to throw a change() event */
	                        /** if (e.keyCode === 13 && holder.inVal !== $this.val()){
	                            $this.change();
	                            holder.inVal = $this.val();
	                        }*/
	                        holder.init(e);
	                        holder.settings.oEvent = 'keydown';
	                        if (holder.skipAllways(e)) {
	                            holder.processed = true;
	                            return true;
	                        }
	                        if (holder.processAllways()) {
	                            holder.processed = true;
	                            holder.formatQuick();
	                            e.preventDefault();
	                            return false;
	                        }
	                        holder.formatted = false;
	                        return true;
	                    });
	                    $this.on('keypress.autoNumeric', function (e) {
	                        var holder = getHolder($this),
	                            processed = holder.processed;
	                        holder.init(e);
	                        holder.settings.oEvent = 'keypress';
	                        if (holder.skipAllways(e)) {
	                            return true;
	                        }
	                        if (processed) {
	                            e.preventDefault();
	                            return false;
	                        }
	                        if (holder.processAllways() || holder.processKeypress()) {
	                            holder.formatQuick();
	                            e.preventDefault();
	                            return false;
	                        }
	                        holder.formatted = false;
	                    });
	                    $this.on('keyup.autoNumeric', function (e) {
	                        var holder = getHolder($this);
	                        holder.init(e);
	                        holder.settings.oEvent = 'keyup';
	                        var skip = holder.skipAllways(e);
	                        holder.kdCode = 0;
	                        delete holder.valuePartsBeforePaste;
	                        if ($this[0].value === holder.settings.aSign) {
	                            /** added to properly place the caret when only the currency is present */
	                            if (holder.settings.pSign === 's') {
	                                setElementSelection(this, 0, 0);
	                            } else {
	                                setElementSelection(this, holder.settings.aSign.length, holder.settings.aSign.length);
	                            }
	                        }
	                        if (skip) {
	                            return true;
	                        }
	                        if (this.value === '') {
	                            return true;
	                        }
	                        if (!holder.formatted) {
	                            holder.formatQuick();
	                        }
	                    });
	                    $this.on('focusin.autoNumeric', function () {
	                        var holder = getHolder($this);
	                        holder.settingsClone.oEvent = 'focusin';
	                        if (holder.settingsClone.nBracket !== null) {
	                            var checkVal = $this.val();
	                            $this.val(negativeBracket(checkVal, holder.settingsClone.nBracket, holder.settingsClone.oEvent));
	                        }
	                        holder.inVal = $this.val();
	                        var onempty = checkEmpty(holder.inVal, holder.settingsClone, true);
	                        if (onempty !== null) {
	                            $this.val(onempty);
	                            if (holder.settings.pSign === 's') {
	                                setElementSelection(this, 0, 0);
	                            } else {
	                                setElementSelection(this, holder.settings.aSign.length, holder.settings.aSign.length);
	                            }
	                        }
	                    });
	                    $this.on('focusout.autoNumeric', function () {
	                        var holder = getHolder($this),
	                            settingsClone = holder.settingsClone,
	                            value = $this.val(),
	                            origValue = value;
	                        holder.settingsClone.oEvent = 'focusout';
	                        var strip_zero = ''; /** added to control leading zero */
	                        if (settingsClone.lZero === 'allow') {
	                            /** added to control leading zero */
	                            settingsClone.allowLeading = false;
	                            strip_zero = 'leading';
	                        }
	                        if (value !== '') {
	                            value = autoStrip(value, settingsClone, strip_zero);
	                            if (checkEmpty(value, settingsClone) === null && autoCheck(value, settingsClone, $this[0])) {
	                                value = fixNumber(value, settingsClone.aDec, settingsClone.aNeg);
	                                value = autoRound(value, settingsClone);
	                                value = presentNumber(value, settingsClone.aDec, settingsClone.aNeg);
	                            } else {
	                                value = '';
	                            }
	                        }
	                        var groupedValue = checkEmpty(value, settingsClone, false);
	                        if (groupedValue === null) {
	                            groupedValue = autoGroup(value, settingsClone);
	                        }
	                        if (groupedValue !== origValue) {
	                            $this.val(groupedValue);
	                        }
	                        if (groupedValue !== holder.inVal) {
	                            $this.change();
	                            delete holder.inVal;
	                        }
	                        if (settingsClone.nBracket !== null && $this.autoNumeric('get') < 0) {
	                            holder.settingsClone.oEvent = 'focusout';
	                            $this.val(negativeBracket($this.val(), settingsClone.nBracket, settingsClone.oEvent));
	                        }
	                    });
	                }
	            });
	        },
	        /** method to remove settings and stop autoNumeric() */
	        destroy: function destroy() {
	            return $(this).each(function () {
	                var $this = $(this);
	                $this.off('.autoNumeric');
	                $this.removeData('autoNumeric');
	            });
	        },
	        /** method to update settings - can call as many times */
	        update: function update(options) {
	            return $(this).each(function () {
	                var $this = autoGet($(this)),
	                    settings = $this.data('autoNumeric');
	                if ((typeof settings === 'undefined' ? 'undefined' : _typeof(settings)) !== 'object') {
	                    $.error("You must initialize autoNumeric('init', {options}) prior to calling the 'update' method");
	                    return this;
	                }
	                var strip = $this.autoNumeric('get');
	                settings = $.extend(settings, options);
	                getHolder($this, settings, true);
	                if (settings.aDec === settings.aSep) {
	                    $.error("autoNumeric will not function properly when the decimal character aDec: '" + settings.aDec + "' and thousand separator aSep: '" + settings.aSep + "' are the same character");
	                    return this;
	                }
	                $this.data('autoNumeric', settings);
	                if ($this.val() !== '' || $this.text() !== '') {
	                    return $this.autoNumeric('set', strip);
	                }
	                return;
	            });
	        },
	        /** returns a formatted strings for "input:text" fields Uses jQuery's .val() method*/
	        set: function set(valueIn) {
	            if (valueIn === null) {
	                return;
	            }
	            return $(this).each(function () {
	                var $this = autoGet($(this)),
	                    settings = $this.data('autoNumeric'),
	                    value = valueIn.toString(),
	                    testValue = valueIn.toString();
	                if ((typeof settings === 'undefined' ? 'undefined' : _typeof(settings)) !== 'object') {
	                    $.error("You must initialize autoNumeric('init', {options}) prior to calling the 'set' method");
	                    return this;
	                }
	                /** routine to handle page re-load from back button */
	                if (testValue !== $this.attr('value') && $this.prop('tagName').toLowerCase() === 'input' && settings.runOnce === false) {
	                    value = settings.nBracket !== null ? negativeBracket($this.val(), settings.nBracket, 'pageLoad') : value;
	                    value = autoStrip(value, settings);
	                }
	                /** allows locale decimal separator to be a comma */
	                if ((testValue === $this.attr('value') || testValue === $this.text()) && settings.runOnce === false) {
	                    value = value.replace(',', '.');
	                }
	                /** returns a empty string if the value being 'set' contains non-numeric characters and or more than decimal point (full stop) and will not be formatted */
	                if (!$.isNumeric(+value)) {
	                    return '';
	                }
	                value = checkValue(value, settings);
	                settings.oEvent = 'set';
	                value.toString();
	                if (value !== '') {
	                    value = autoRound(value, settings);
	                }
	                value = presentNumber(value, settings.aDec, settings.aNeg);
	                if (!autoCheck(value, settings)) {
	                    value = autoRound('', settings);
	                }
	                value = autoGroup(value, settings);
	                if ($this.is('input[type=text], input[type=hidden], input[type=tel], input:not([type])')) {
	                    /**added hidden type */
	                    return $this.val(value);
	                }
	                if ($.inArray($this.prop('tagName').toLowerCase(), settings.tagList) !== -1) {
	                    return $this.text(value);
	                }
	                $.error("The <" + $this.prop('tagName').toLowerCase() + "> is not supported by autoNumeric()");
	                return false;
	            });
	        },
	        /** method to get the unformatted value from a specific input field, returns a numeric value */
	        get: function get() {
	            var $this = autoGet($(this)),
	                settings = $this.data('autoNumeric');
	            if ((typeof settings === 'undefined' ? 'undefined' : _typeof(settings)) !== 'object') {
	                $.error("You must initialize autoNumeric('init', {options}) prior to calling the 'get' method");
	                return this;
	            }
	            settings.oEvent = 'get';
	            var getValue = '';
	            /** determine the element type then use .eq(0) selector to grab the value of the first element in selector */
	            if ($this.is('input[type=text], input[type=hidden], input[type=tel], input:not([type])')) {
	                /**added hidden type */
	                getValue = $this.eq(0).val();
	            } else if ($.inArray($this.prop('tagName').toLowerCase(), settings.tagList) !== -1) {
	                getValue = $this.eq(0).text();
	            } else {
	                $.error("The <" + $this.prop('tagName').toLowerCase() + "> is not supported by autoNumeric()");
	                return false;
	            }
	            if (getValue === '' && settings.wEmpty === 'empty' || getValue === settings.aSign && (settings.wEmpty === 'sign' || settings.wEmpty === 'empty')) {
	                return '';
	            }
	            if (settings.nBracket !== null && getValue !== '') {
	                getValue = negativeBracket(getValue, settings.nBracket, settings.oEvent);
	            }
	            if (settings.runOnce || settings.aForm === false) {
	                getValue = autoStrip(getValue, settings);
	            }
	            getValue = fixNumber(getValue, settings.aDec, settings.aNeg);
	            if (+getValue === 0 && settings.lZero !== 'keep') {
	                getValue = '0';
	            }
	            if (settings.lZero === 'keep') {
	                return getValue;
	            }
	            getValue = checkValue(getValue, settings);
	            return getValue; /** returned Numeric String */
	        },
	        /** method to get the unformatted value from multiple fields */
	        getString: function getString() {
	            var isAutoNumeric = false,
	                $this = autoGet($(this)),
	                str = $this.serialize(),
	                parts = str.split('&'),
	                formIndex = $('form').index($this),
	                i = 0;
	            for (i; i < parts.length; i += 1) {
	                var miniParts = parts[i].split('='),
	                    $field = $('form:eq(' + formIndex + ') input[name="' + decodeURIComponent(miniParts[0]) + '"]'),
	                    settings = $field.data('autoNumeric');
	                if ((typeof settings === 'undefined' ? 'undefined' : _typeof(settings)) === 'object') {
	                    if (miniParts[1] !== null) {
	                        miniParts[1] = $field.autoNumeric('get');
	                        parts[i] = miniParts.join('=');
	                        isAutoNumeric = true;
	                    }
	                }
	            }
	            if (isAutoNumeric === true) {
	                return parts.join('&');
	            }
	            return str;
	        },
	        /** method to get the unformatted value from multiple fields */
	        getArray: function getArray() {
	            var isAutoNumeric = false,
	                $this = autoGet($(this)),
	                formFields = $this.serializeArray(),
	                formIndex = $('form').index($this);
	            /*jslint unparam: true*/
	            $.each(formFields, function (i, field) {
	                var $field = $('form:eq(' + formIndex + ') input[name="' + decodeURIComponent(field.name) + '"]'),
	                    settings = $field.data('autoNumeric');
	                if ((typeof settings === 'undefined' ? 'undefined' : _typeof(settings)) === 'object') {
	                    if (field.value !== '') {
	                        field.value = $field.autoNumeric('get').toString();
	                    }
	                    isAutoNumeric = true;
	                }
	            });
	            /*jslint unparam: false*/
	            if (isAutoNumeric === true) {
	                return formFields;
	            }
	            return this;
	        },
	        /** returns the settings object for those who need to look under the hood */
	        getSettings: function getSettings() {
	            var $this = autoGet($(this));
	            return $this.eq(0).data('autoNumeric');
	        }
	    };
	    $.fn.autoNumeric = function (method) {
	        if (methods[method]) {
	            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
	        }
	        if ((typeof method === 'undefined' ? 'undefined' : _typeof(method)) === 'object' || !method) {
	            return methods.init.apply(this, arguments);
	        }
	        $.error('Method "' + method + '" is not supported by autoNumeric()');
	    };
	})(jQuery);

/***/ },

/***/ 58:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	var commonFun = __webpack_require__(1);
	var ValidatorForm = __webpack_require__(55);
	var $loginTipBox = $('#loginTip');
	var loginInForm = document.getElementById('loginInForm');
	var errorDom = $(loginInForm).find('.error-box');
	__webpack_require__(59);
	//刷新验证码
	$('#imageCaptcha').on('click', function () {
	    commonFun.refreshCaptcha(this, '/login/captcha');
	    loginInForm.captcha.value = '';
	});

	function popLoginTip() {
	    //判断是否登陆，如果没有登陆弹出登录框
	    $.when(commonFun.isUserLogin()).fail(function () {
	        console.log('未登陆');
	        layer.open({
	            type: 1,
	            title: false,
	            closeBtn: 0,
	            area: ['auto', 'auto'],
	            content: $('#loginTip')
	        });
	        $('.close-btn', $loginTipBox).on('click', function (event) {
	            event.preventDefault();
	            layer.closeAll();
	        });
	    });
	}

	//验证表单
	var validator = new ValidatorForm();
	validator.add(loginInForm.username, [{
	    strategy: 'isNonEmpty',
	    errorMsg: '用户名不能为空'
	}]);

	validator.add(loginInForm.password, [{
	    strategy: 'isNonEmpty',
	    errorMsg: '密码不能为空'
	}, {
	    strategy: 'checkPassword',
	    errorMsg: '密码为6位至20位，不能全是数字'
	}]);
	validator.add(loginInForm.captcha, [{
	    strategy: 'isNonEmpty',
	    errorMsg: '验证码不能为空'
	}]);

	var reInputs = $(loginInForm).find('input:text,input:password');

	Array.prototype.forEach.call(reInputs, function (el) {
	    globalFun.addEventHandler(el, 'blur', function () {
	        var errorMsg = validator.start(this);
	        if (errorMsg) {
	            errorDom.text(errorMsg);
	        } else {
	            errorDom.text('');
	        }
	    });
	});

	loginInForm.onsubmit = function (event) {
	    event.preventDefault();
	    var thisButton = $(event.target).find(':submit')[0];
	    var errorMsg = void 0;
	    for (var i = 0, len = reInputs.length; i < len; i++) {
	        errorMsg = validator.start(reInputs[i]);
	        if (errorMsg) {
	            errorDom.text(errorMsg);
	            return;
	        }
	    }
	    if (!errorMsg) {
	        globalFun.addClass(thisButton, 'loading');
	        //弹框登陆为ajax的form表单提交
	        var dataParam = $(loginInForm).serialize();
	        commonFun.useAjax({
	            url: '/login',
	            type: 'POST',
	            data: dataParam
	        }, function (data) {
	            if (data.status) {
	                window.location.reload();
	            } else {
	                commonFun.refreshCaptcha(loginInForm.imageCaptcha, '/login/captcha');
	                globalFun.removeClass(thisButton, 'loading');
	                errorDom.text(data.message);
	            }
	        });
	    }
	};

	module.exports = popLoginTip;

/***/ },

/***/ 59:
/***/ function(module, exports, __webpack_require__) {

	// style-loader: Adds some css to the DOM by adding a <style> tag

	// load the styles
	var content = __webpack_require__(60);
	if(typeof content === 'string') content = [[module.id, content, '']];
	// add the styles to the DOM
	var update = __webpack_require__(15)(content, {});
	if(content.locals) module.exports = content.locals;
	// Hot Module Replacement
	if(true) {
		// When the styles change, update the <style> tags
		if(!content.locals) {
			module.hot.accept(60, function() {
				var newContent = __webpack_require__(60);
				if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
				update(newContent);
			});
		}
		// When the module is disposed, remove the <style> tags
		module.hot.dispose(function() { update(); });
	}

/***/ },

/***/ 60:
/***/ function(module, exports, __webpack_require__) {

	exports = module.exports = __webpack_require__(4)();
	// imports


	// module
	exports.push([module.id, ".login-tip {\n  float: left;\n  width: 750px;\n  height: 410px;\n  display: none;\n  position: relative; }\n  @media (max-width: 700px) {\n    .login-tip {\n      width: 320px;\n      height: auto; } }\n  .login-tip .login-box {\n    float: left;\n    width: 290px;\n    font-size: 13px;\n    text-align: left;\n    margin: 0 70px; }\n    @media (max-width: 700px) {\n      .login-tip .login-box {\n        margin: 0 15px; } }\n    .login-tip .login-box h3 {\n      font-size: 20px;\n      color: #4c4c4c;\n      font-weight: normal;\n      line-height: 24px;\n      margin: 30px 0 10px 0; }\n    .login-tip .login-box label {\n      float: left;\n      width: 100%;\n      margin-top: 20px;\n      border: #dadada 1px solid;\n      box-sizing: border-box;\n      position: relative; }\n      .login-tip .login-box label.error {\n        position: absolute;\n        bottom: -23px;\n        left: 0;\n        margin-bottom: 0;\n        border: none;\n        width: 100%; }\n      .login-tip .login-box label input {\n        float: left;\n        width: 240px;\n        border: none;\n        line-height: 30px; }\n        .login-tip .login-box label input.captcha {\n          width: 120px; }\n      .login-tip .login-box label .image-captcha {\n        width: 120px;\n        overflow: hidden;\n        height: 43px;\n        cursor: pointer; }\n      .login-tip .login-box label .name {\n        float: left;\n        width: 30px;\n        height: 30px;\n        text-align: left;\n        margin: 5px 0 0 5px;\n        background: url(\"/images/logintip/result_20160901.png\") no-repeat; }\n        .login-tip .login-box label .name.user {\n          background-position: 2px 1px; }\n        .login-tip .login-box label .name.pass {\n          background-position: 5px -36px; }\n        .login-tip .login-box label .name.capt {\n          background-position: 3px -70px; }\n    .login-tip .login-box .forgot-password {\n      float: left;\n      width: 100%;\n      font-size: 12px;\n      margin-top: 10px; }\n      .login-tip .login-box .forgot-password .btn-normal {\n        width: 100%;\n        font-size: 20px;\n        margin-bottom: 10px;\n        line-height: 27px; }\n      .login-tip .login-box .forgot-password .fl a {\n        color: #e64d0a; }\n      .login-tip .login-box .forgot-password a {\n        font-size: 14px;\n        color: #545353; }\n        .login-tip .login-box .forgot-password a.register {\n          color: #e64d0a; }\n  .login-tip .code-item {\n    float: left;\n    width: 310px;\n    height: 305px;\n    margin-top: 75px;\n    background: url(\"/images/logintip/code-line.png\") no-repeat;\n    background-position: 0 0; }\n    @media (max-width: 700px) {\n      .login-tip .code-item {\n        display: none; } }\n    .login-tip .code-item p {\n      font-size: 14px;\n      color: #4c4c4c;\n      text-align: center;\n      margin-bottom: 0; }\n      .login-tip .code-item p.code-img {\n        margin: 15px 0 30px; }\n  .login-tip .close-btn {\n    position: absolute;\n    top: 10px;\n    right: 0;\n    width: 30px;\n    height: 30px;\n    cursor: pointer;\n    background: url(\"/images/logintip/result_20160901.png\") no-repeat;\n    background-position: 0 -108px; }\n", ""]);

	// exports


/***/ }

});