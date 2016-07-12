require(['jquery', 'underscore', 'pagination', 'mustache', 'text!tpl/loan-invest-list.mustache', 'fancybox'], function($, _, pagination, Mustache, investListTemplate) {
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

	$.fn.dropDown = function() {
		return this.each(function() {
			var $ele = $(this);
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
    	loadInvestData();
    });
});