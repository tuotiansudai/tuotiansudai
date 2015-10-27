require(['jquery', 'pagination', 'mustache', 'text!/tpl/loan-invest-list.mustache', 'csrf'], function ($, pagination, Mustache, investListTemplate) {
    $(function () {

        var paginationElement = $('.pagination');

        var loadLoanData = function (currentPage) {
            var requestData = {index: currentPage || 1};

            paginationElement.loadPagination(requestData, function (data) {
                if (data.status) {
                    var html = Mustache.render(investListTemplate, data);
                    $('.loan-list-con table').html(html);
                }

            });
        };

        //pageCount：总页数
        //current：当前页
        //初始化标的比例（进度条）
        var java_point = 15; //后台传递数据
        if (java_point <= 50) {
            $('.chart-box .rount').css('webkitTransform', "rotate(" + 3.6 * java_point + "deg)");
            $('.chart-box .rount2').hide();
        } else {
            $('.chart-box .rount').css('webkitTransform', "rotate(180deg)");
            $('.chart-box .rount2').show();
            $('.chart-box .rount2').css('webkitTransform', "rotate(" + 3.6 * (java_point - 50) + "deg)");
        }

        // tab select
        var tabs = $('.nav li');
        tabs.click(function () {
            var self = $(this);
            tabs.removeClass('active');
            self.addClass('active');
            var index = self.index();
            if (index === 1) {
                loadLoanData();
            }
            $('.loan-list .loan-list-con').removeClass('active').eq(index).addClass('active');
        });

        $('.img-list li').click(function () {
            var _imgSrc = $(this).find('img').attr('src');
            $('.content img').attr('src', _imgSrc);
            $('.layer-box').show();
            return false;
        });

        $('.layer-wrapper').click(function () {
            $('.layer-box').hide();
        });

        function timer(intDiff) {
            window.setInterval(function () {
                var day = 0,
                    hour = 0,
                    minute = 0,
                    second = 0;//时间默认值
                if (intDiff > 0) {
                    // day = Math.floor(intDiff / (60 * 60 * 24));
                    // hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
                    minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
                    second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
                } else {
                    $('.btn-pay').removeClass('grey').removeAttr('disabled').html('可投资');
                }
                if (minute <= 9) minute = '0' + minute;
                if (second <= 9) second = '0' + second;
                // $('#day_show').html(day+"天");
                // $('#hour_show').html('<s id="h"></s>'+hour+'时');
                $('#minute_show').html('<s></s>' + minute + '分');
                $('#second_show').html('<s></s>' + second + '秒');
                intDiff--;
            }, 1000);
        }

        if ($('#loanStatus').val() == 'PREHEAT') {
            timer(intDiff);
        }
    });
});