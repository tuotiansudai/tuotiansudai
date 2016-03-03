require(['jquery', 'mustache', 'text!/tpl/point-bill-table.mustache', 'moment', 'pagination', 'daterangepicker'],
    function($, Mustache, pointBillListTemplate, moment, pagination) {
        $(function() {
            var $navBtn = $('.column-title .title-navli'),
                $signBtn = $('#signBtn'),
                $signTip = $('#signLayer'),
                $closeSign = $('#closeSign'),
                $taskBtn = $('#taskBtn'),
                $taskTip = $('#taskLayer'),
                $closeTask = $('#closeTask');
            //change model
            $navBtn.on('click', function(event) {
                event.preventDefault();
                var $self = $(this),
                    index = $self.index();
                $self.addClass('active').siblings().removeClass('active');
                $('.content-list .choi-beans-list:eq(' + index + ')').show().siblings().hide();
            });
            //show sign tip
            $signBtn.on('click', function(event) {
                event.preventDefault();
                $signTip.fadeIn('fast', function() {
                    $(this).find('.add-dou').animate({
                        'bottom': '50px',
                        'opacity': '0'
                    }, 800);
                });
            });
            //hide sign tip
            $closeSign.on('click', function(event) {
                event.preventDefault();
                $signTip.fadeOut('fast', function() {
                    $(this).find('.add-dou').css({
                        'bottom': '0',
                        'opacity': '1'
                    });
                });
            });
            //show task tip
            $taskBtn.on('click', function(event) {
                event.preventDefault();
                $taskTip.fadeIn('fast');
            });
            //hide task tip
            $closeTask.on('click', function(event) {
                event.preventDefault();
                $taskTip.fadeOut('fast');
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
                                    item.businessType = '任务奖励';
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
        });
    });