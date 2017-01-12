require(['jquery', 'moment','mustache', 'layerWrapper', 'text!/tpl/point-bill-table.mustache', 'pagination', 'daterangepicker', 'jquery.ajax.extension'],
    function($, moment, Mustache, layer, pointBillListTemplate, pagination) {
        $(function() {
            var $navBtn = $('.column-title .title-navli'),
                $signBtn = $('#signBtn'),
                $signTip = $('#signLayer'),
                $closeSign = $('#closeSign'),
                $taskBtn = $('#taskBtn'),
                $beanDetail=$('#beansDetail'),
                $beansNum=$('.beans-coupon .bean-use'),
                $taskStatusMenu=$('#taskStatusMenu'),
                $contentList=$('.content-list'),
                $taskStatus=$('.task-status',$contentList),
                $buttonMore=$('.button-more',$contentList);
            var $taskBox=$('#taskFrame').find('.task-box'),
                taskBoxLen=$taskBox.length;
            var disabledBtnLen=$taskBox.find('a.btn-normal[disabled="disabled"]').length;
            if(disabledBtnLen==taskBoxLen) {
                $taskBox.hide();
            }

            $('.notice-tip').on('click',function() {
                $taskBox.toggle();
                var $this=$(this);
                if($taskBox.is(':hidden')) {
                    $this.find('i').addClass('fa-chevron-down').removeClass('fa-chevron-up');
                }
                else {
                    $this.find('i').addClass('fa-chevron-up').removeClass('fa-chevron-down');
                }

            })
            //change model
            $navBtn.on('click', function (event) {
                event.preventDefault();
                var $self = $(this),
                    index = $self.index();
                if (index == 0) {
                    location.href = "/point";
                } else {
                    $self.addClass('active').siblings().removeClass('active');
                    $('.content-list .choi-beans-list:eq(' + index + ')').show().siblings().hide();
                }
            });

            //taskStatusMenu
            $taskStatusMenu.find('span').click(function(event) {
                event.preventDefault();
                var $this=$(this),
                    index=$this.index();
                console.log(index);
                $this.addClass('active').siblings('span').removeClass('active');
                $('.task-status').eq(index).show().siblings('.task-status').hide();
            });
            $taskStatus.find('.border-box').hide();
            $taskStatus.find('.border-box:lt(4)').show();

            $buttonMore.on('click',function(event) {
                event.preventDefault();
                var $this=$(this),
                    $parentBox=$this.parents('.task-status');
                $this.toggleClass('open');
                if($this.hasClass('open')) {
                    $parentBox.find('.border-box').show();
                    $this.find('span').text('收起');
                    $this.find('i').addClass('fa-chevron-circle-up').removeClass('fa-chevron-circle-down');
                }
                else {
                    $parentBox.find('.border-box').hide();
                    $parentBox.find('.border-box:lt(4)').show();
                    $this.find('span').text('点击查看更多任务')
                    $this.find('i').addClass('fa-chevron-circle-down').removeClass('fa-chevron-circle-up');
                }

            });

            //show sign tip
            $signBtn.on('click', function (event) {
                event.preventDefault();
                var _this = $(this),
                    $signText = $(".sign-text"),
                    $tomorrowText = $(".tomorrow-text"),
                    $addDou = $(".add-dou"),
                    $signBtn = $("#signBtn");

                $.ajax({
                    url: _this.data('url'),
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (response) {
                    if (response.data.status) {
                        $signText.html("签到成功，领取" + response.data.signInPoint + "积分！");
                        $tomorrowText.html("明日可领" + response.data.nextSignInPoint + "积分");
                        $signBtn.addClass("no-click").html("今日已签到");
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
                $navBtn.eq(1).trigger('click');

            });

            $beanDetail.on('click', function (event) {
                event.preventDefault();
                $('.column-title .title-navli:eq(3)').trigger('click');
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
                                    item.businessType = '积分兑换';
                                    break;
                                case 'LOTTERY':
                                    item.businessType = '抽奖';
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
                    beansNum=$self.attr('data-beans'),
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
                                        layer.open({
                                            title: '温馨提示',
                                            content: '兑换成功！您可前去我的宝藏中查看。',
                                            btn: ['去看看'],
                                            yes:function() {
                                                location.href = '/my-treasure';
                                            },
                                            end:function() {
                                                changeDatePicker();
                                                loadPointBillData();
                                                $beansNum.text(Math.round($beansNum.text())-Math.round(beansNum));
                                            }
                                        });
                                    } else if (!data.status && data.message == 'point insufficient') {
                                        layer.open({
                                            title: '温馨提示',
                                            content: '您的积分不足，赚取足够多的积分后再来兑换吧！',
                                            btn: ['赚取积分', '取消'],
                                            yes:function(index,layero){
                                                location.href='/point';
                                            }
                                        });
                                    } else if (!data.status && data.message == 'coupon exchangeable insufficient') {
                                        layer.msg('当前优惠券已兑完，请兑换其他优惠券',{icon: 5});

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