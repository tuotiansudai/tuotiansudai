require(['jquery', 'underscore', 'pagination', 'mustache', 'text!tpl/loan-invest-list.mustache', 'layerWrapper', 'fancybox'], function($, _, pagination, Mustache, investListTemplate, layer) {

	$.fn.carousel = function() {
		return this.each(function() {
			var $ele = $(this);
			var $leftBtn = $ele.find('.left-button');
			var $rightBtn = $ele.find('.right-button');
			var $scrollEle = $ele.find('.scroll-content > .row');
			var $col = $scrollEle.find('.col');
			var len = $col.length;
			var record = 0;
			$leftBtn.on('click', function() {
				record++;
				if (record > len - 4) {
					record = len - 4;
				}
				$scrollEle.stop().animate({
					marginLeft: (-200 - 10) * record
				});
			});
			$rightBtn.on('click', function() {
				record--;
				if (record < 0) {
					record = 0;
				}
				$scrollEle.stop().animate({
					marginLeft: (-200 - 10) * record
				});
			});
		});
	};

	$.fn.tab = function() {
		return this.each(function() {
			var $ele = $(this);
			var $tabHead = $ele.find('.title-block .item');
			var $content = $ele.children('.content');
			$tabHead.each(function(index) {
				$(this).on('click', _.partial(function(event, index) {
					$tabHead.eq(index).addClass('active').siblings().removeClass('active');
					$content.hide().eq(index).show();
				}, _, index))
			});
		});
	};

	$.fn.dropDown = function(options) {
		return this.each(function() {
			var $ele = $(this);
			var $options = $ele.find('.dropdown-list');
			var $item = $options.find('.item');
			var $main = $ele.find('.dropdown-main');
			var $text = $main.find('.text');
			var isShow = false;
			$main.on('click', function(event) {
				event.stopPropagation();
				if ($main.hasClass('disabled')) {
					return false;
				}
				if (isShow) {
					$options.hide();
				} else {
					$options.show();
				}
				isShow = !isShow;
			});
			$(document).on('click', function() {
				$options.hide();
				isShow = false;
			});
			$item.on('click', function(event) {
				event.stopPropagation();
				var $this = $(this);
				if ($this.hasClass('disabled')) {
					return false;
				}
				$this.addClass('active').find('input').prop('checked', true).end().siblings().removeClass('active').find('input').prop('checked', false);
				$options.hide();
				isShow = false;
				$text.text($this.data('text'));
				options && options.onChange && options.onChange($this.data('id'));
			});
			$ele.on('setOption', function(event, id) {
				$item.filter('[data-id="' + id + '"]').trigger('click');
			});
		});
	};

    var loadInvestData = function() {
    	var paginationElement = $('#invests-list-pagination');
        paginationElement.loadPagination({
            index: 1
        }, function(data) {
            if (data.status) {
                data.achievementClass = function () {
                    return function (text, render) {
                        var classMapping = { 'FIRST_INVEST': 'first-icon', 'MAX_AMOUNT': 'max-icon', 'LAST_INVEST': 'last-icon' };
                        return "<i class='" + classMapping[render(text)] + "'></i>";
                    }
                };
                var html = Mustache.render(investListTemplate, data);
                $('.loan-list-con table').html(html);
            }
        });
    };

    var extraRateFn = function() {
    	var $extraRate = $('#extra-rate');
    	if (!$extraRate.length) {
    	    return false;
    	}

    	var utils = {
    	    getSize: function(element, type) {
    	        return element[type]();
    	    },
    	    getOffset: function(element) {
    	        return element.offset();
    	    },
    	    removeElement: function(element) {
    	        if (element.length) {
    	            element.remove();
    	        }
    	    },
    	    getRelativeRate: function(arr, num) {
    	        var index =  _.findLastIndex(__extraRate, function(value) {
    	            if (num >= value.minInvestAmount && (value.maxInvestAmount > num || value.maxInvestAmount === 0)) {
    	                return true;
    	            }
    	        });
    	        return index !== -1 ? arr[index].rate : '';
    	    },
    	    replace: function(str) {
    	        return str.replace(/,/g, '');
    	    }
    	};

    	var tplFn = _.compose(_.template, function() {
    	    return $('#extra-rate-popup-tpl').html();
    	})();
    	var getOffset = _.partial(utils.getOffset, $extraRate);
    	var getSize = _.partial(utils.getSize, $extraRate);
    	var extraRateWidth = getSize('width');
    	var extraRateHeight = getSize('height');
    	var css = _.compose(_.partial(function(offset, extraRateHeight) {
    	    return {
    	        left: offset.left,
    	        top: offset.top + extraRateHeight - 5
    	    }
    	}, _, extraRateHeight), getOffset);
    	var createPopup = _.partial(function(tpl, css) {
    	    return $(tpl).css(css).appendTo('body');
    	}, _, css());
    	var showPopup = _.compose(createPopup, tplFn);
    	var removePopup = _.partial(_.compose(utils.removeElement, $), '#extra-rate-popup');

    	$extraRate.find('.fa').on({
    	    mouseover: _.partial(showPopup, {__extraRate: __extraRate}),
    	    mouseout: function() {
    	        removePopup();
    	    }
    	});

    	var getRelativeRate = _.partial(utils.getRelativeRate, __extraRate);
    	var changeHTML = function() {
    	    var $element = $('.data-extra-rate');
    	    return function(rate) {
    	        $element.html(rate);
    	    }
    	}();
    	var addSign = function(rate) {
    	    if (!rate) {
    	        return ''
    	    }
    	    return '+' + rate;
    	};

    	$('#invest-input').on('change', _.compose(changeHTML, addSign, getRelativeRate, parseInt, utils.replace, function() {
    	        return $(this).val()
    	    })).trigger('change');
    };

    var calInterest = function() {
    	var loanId = $('#invest-input').data('loan-id');
    	var $couponExpectedInterest = $(".experience-income");
    	var $btnLookOther = $('#investSubmit');

    	var selectMaxBenefitCoupon = function(investAmount) {
    		var defer = $.Deferred();
    		$.ajax({
    		    url: '/loan/' + loanId + '/amount/' + investAmount + "/max-benefit-user-coupon",
    		    type: 'get',
    		    dataType: 'json',
    		    contentType: 'application/json; charset=UTF-8'
    		}).done(function(maxBenefitUserCouponId) {
    		    if (!isNaN(parseInt(maxBenefitUserCouponId))) {
    		    	$('#ttsd-dropdown').trigger('setOption', maxBenefitUserCouponId);
    		    	defer.resolve(maxBenefitUserCouponId, investAmount);
    		    } else {
    		        $('#ttsd-dropdown').trigger('setOption', 'no');
    		        defer.reject();
    		    }
    		});
    		return defer.promise();
    	};

    	var calExpectedCouponInterest = function(maxBenefitUserCouponId, investAmount) {
    		var queryParams = [];

    		$.each($('input[type="hidden"][name="userCouponIds"]'), function(index, item) {
    		    queryParams.push({
    		        'name': 'couponIds',
    		        'value': $(item).data("coupon-id")
    		    })
    		});

    		queryParams.push({
    		    'name': 'couponIds',
    		    'value': maxBenefitUserCouponId
    		});

    		$.ajax({
    		    url: '/calculate-expected-coupon-interest/loan/' + loanId + '/amount/' + investAmount,
    		    data: $.param(queryParams),
    		    type: 'get',
    		    dataType: 'json',
    		    contentType: 'application/json; charset=UTF-8'
    		}).done(function(amount) {
    		    $couponExpectedInterest.text("+" + amount);
    		    $btnLookOther.prop('disabled', false);
    		});
    	};
    	var cleanExpectedCouponInterest = function() {
    		$couponExpectedInterest.text('');
    	};

    	$('#invest-input').on('input', _.debounce(function(event) {
    		var investAmount = $(this).val();

    		$.ajax({
    		    url: '/calculate-expected-interest/loan/' + loanId + '/amount/' + investAmount,
    		    type: 'get',
    		    dataType: 'json',
    		    contentType: 'application/json; charset=UTF-8'
    		}).done(function(amount) {
    		    $('.principal-income').text(amount);
    		});

    		selectMaxBenefitCoupon(investAmount).then(calExpectedCouponInterest, cleanExpectedCouponInterest);
    	}, 300));
    };

    var investSubmit = function() {
    	var $investSubmitBtn = $('#invest-submit-btn');
    	var isInvestor = 'INVESTOR' === $investSubmitBtn.data('user-role');
    	var noPasswordRemind = $investSubmitBtn.data('no-password-remind');
        var noPasswordInvest = $investSubmitBtn.data('no-password-invest');
    	$investSubmitBtn.on('click', function(event) {
    	    event.preventDefault();
    	    if (isInvestor) {
    	        noPasswordRemind || noPasswordInvest ? investSubmit() : markNoPasswordRemind();
    	        return;
    	    }
    	    location.href = '/login?redirect=' + encodeURIComponent(location.href);
    	});
    };

    function markNoPasswordRemind(){
        if (!noPasswordRemind) {
            $.ajax({
                url: '/no-password-invest/mark-remind',
                type: 'POST',
                dataType: 'json'
            })
            .done(function () {
                noPasswordRemind = true;
            });
        }
        layer.open({
            type: 1,
            closeBtn: 0,
            skin: 'demo-class',
            title: '免密投资',
            shadeClose: false,
            btn: autoInvestOn ? ['继续投资', '开启免密投资'] : ['继续投资', '前往联动优势授权'],
            area: ['500px', '160px'],
            content: '<p class="pad-m-tb tc">推荐您开通免密投资功能，简化投资过程，理财快人一步！</p>',
            btn1: function () {
                cnzzPush.trackClick("标的详情页", "马上投资弹框", "继续投资B");
                investSubmit();
                layer.closeAll();
            },
            btn2: function () {
                cnzzPush.trackClick("标的详情页", "马上投资弹框", autoInvestOn ? "开启免密投资" : "前往联动优势授权");
                if (autoInvestOn) {
                    $.ajax({
                            url: '/no-password-invest/enabled',
                            type: 'POST',
                            dataType: 'json'
                        })
                        .done(function () {
                            noPasswordInvest = true;
                            layer.closeAll();
                            layer.msg('开启成功！', function () {
                                investSubmit();
                            });
                        })
                        .fail(function () {
                            layer.closeAll();
                            layer.msg('开启失败，请重试！');
                        })
                } else {
                    showAuthorizeAgreementOptions();
                    $authorizeAgreement.submit();
                }
            }
        });
    }

    $(function() {
		$('[scroll-carousel]').carousel().find('.col').fancybox({
	        'titlePosition' : 'over',
	        'cyclic'        : false,
	        'showCloseButton':true,
	        'showNavArrows' : true,
	        'titleFormat'   : function(title, currentArray, currentIndex, currentOpts) {
	            return 'sdfsdf';
	        }
	    });
	    $('[data-tab]').tab();
	    $('#ttsd-dropdown').dropDown();
    	loadInvestData();
    	extraRateFn();
		calInterest();
		investSubmit();

    });
});