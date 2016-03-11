require(['jquery', 'moment','mustache', 'layerWrapper', 'text!/tpl/point-bill-table.mustache', 'pagination', 'daterangepicker', 'csrf'],
    function($, moment, Mustache, layer, pointBillListTemplate, pagination) {
        $(function() {
            var $navBtn = $('.column-title .title-navli'),
                $signBtn = $('#signBtn'),
                $signTip = $('#signLayer'),
                $closeSign = $('#closeSign'),
                $taskBtn = $('#taskBtn'),
                $taskTip = $('#taskLayer'),
                $closeTask = $('#closeTask'),
                $beanDetail=$('#beansDetail');
            //change model
            $navBtn.on('click', function (event) {
                event.preventDefault();
                var $self = $(this),
                    index = $self.index();
                $self.addClass('active').siblings().removeClass('active');
                $('.content-list .choi-beans-list:eq(' + index + ')').show().siblings().hide();
            });
            //show sign tip
            $signBtn.on('click', function (event) {
                event.preventDefault();
                var _this = $(this),
                    $signText = $(".sign-text");
                    $tomorrowText = $(".tomorrow-text");
                    $addDou = $(".add-dou");

                $.ajax({
                    url: _this.data('url'),
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (response) {
                    if (response.data.status) {
                        $signText.html("签到成功，领取" + response.data.signInPoint + "财豆！");
                        $tomorrowText.html("明日可领" + response.data.nextSignInPoint + "财豆");
                        $addDou.html("+" + response.data.signInPoint);
                        $signTip.fadeIn('fast', function () {
                            $(this).find('.add-dou').animate({
                                'bottom': '50px',
                                'opacity': '0'
                            }, 800);
                        });
                    }
                })
            });
            //hide sign tip
            $closeSign.on('click', function (event) {
                event.preventDefault();
                location.href = "/point";
            });
            //show task tip
            $taskBtn.on('click', function (event) {
                event.preventDefault();
                $taskTip.fadeIn('fast');
            });
            //hide task tip
            $closeTask.on('click', function (event) {
                event.preventDefault();
                $taskTip.fadeOut('fast');
            });
            $beanDetail.on('click', function (event) {
                event.preventDefault();
                $('.column-title .title-navli:eq(2)').trigger('click');
            });

            var today = moment().format('YYYY-MM-DD'), // 今天
                week = moment().subtract(1, 'week').format('YYYY-MM-DD'),
                month = moment().subtract(1, 'month').format('YYYY-MM-DD'),
                sixMonths = moment().subtract(6, 'month').format('YYYY-MM-DD'),
                $dateFilter = $('.date-filter'),
                $statusFilter = $('.status-filter');


            var dataPickerElement = $('#date-picker'),
                paginationElement = $('.pagination');

            dataPickerElement.dateRangePicker({
                separator: ' ~ '
            });

            var changeDatePicker = function() {
                var duration = $(".date-filter .select-item.current").data('day');
                switch (duration) {
                    case 1:
                        dataPickerElement.val(today + '~' + today);
                        break;
                    case 7:
                        dataPickerElement.val(week + '~' + today);
                        break;
                    case 30:
                        dataPickerElement.val(month + '~' + today);
                        break;
                    case 180:
                        dataPickerElement.val(sixMonths + '~' + today);
                        break;
                    default:
                        dataPickerElement.val('');
                }
            };

            var loadPointBillData = function(currentPage) {
                var dates = dataPickerElement.val().split('~');
                var startTime = $.trim(dates[0]) || '';
                var endTime = $.trim(dates[1]) || '';
                var businessType = $('.status-filter .select-item.current').data('status');

                var requestData = {
                    startTime: startTime,
                    endTime: endTime,
                    businessType: businessType,
                    index: currentPage || 1
                };

                paginationElement.loadPagination(requestData, function(data) {
                    if (data.status) {
                        _.each(data.records, function(item) {
                            switch (item.businessType) {
                                case 'SIGN_IN':
                                    item.businessType = '签到奖励';
                                    break;
                                case 'TASK':
                                    item.businessType = '任务奖励';
                                    break;
                                case 'INVEST':
                                    item.businessType = '投资奖励';
                                    break;
                                case 'EXCHANGE':
                                    item.businessType = '财豆兑换';
                                    break;
                            }
                        });
                    }
                    var html = Mustache.render(pointBillListTemplate, data);
                    $('.point-bill-list').html(html);
                });
            };

            changeDatePicker();
            loadPointBillData();

            $dateFilter.find(".select-item").click(function() {
                $(this).addClass("current").siblings(".select-item").removeClass("current");
                changeDatePicker();
                loadPointBillData();
            });
            $statusFilter.find(".select-item").click(function() {
                $(this).addClass("current").siblings(".select-item").removeClass("current");
                loadPointBillData();
            });

            //define calendar
            $('.apply-btn').on('click', function(event) {
                event.preventDefault();
                loadPointBillData();
                $(".date-filter .select-item").removeClass("current");
            });

            $('.reedom-now').on('click', function(event) {
                event.preventDefault();
                var $self=$(this),
                    dataId=$self.attr('data-id'),
                    couponName=$self.attr('data-bite');
                if (!$(this).hasClass('no-click')) {
                    layer.open({
                        title: '温馨提示',
                        content: '确认兑换'+couponName+'？',
                        btn: ['确定', '取消'],
                        yes:function(index,layero){
                            layer.close(index);
                            $.ajax({
                                url: '/point/'+dataId+'/exchange',
                                type: 'POST',
                                dataType: 'json'
                            })
                                .done(function(data) {
                                    if(data.status) {
                                        layer.alert('兑换成功！', {title: '温馨提示'}, function() {
                                            location.href='/point';
                                        });
                                    } else if (!data.status && data.message == 'point insufficient') {
                                        layer.open({
                                            title: '温馨提示',
                                            content: '您的财豆不足，赚取足够多的财豆后再来兑换吧！',
                                            btn: ['赚取财豆', '取消'],
                                            yes:function(index,layero){
                                                location.href='/point';
                                            }
                                        });
                                    } else {
                                        layer.alert('兑换失败，请重试！',{title:'温馨提示'});
                                    }
                                })
                                .fail(function() {
                                    layer.alert('兑换失败，请重试！',{title:'温馨提示'});
                                });
                        }
                    });
                }
            });


        });
    });