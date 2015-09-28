/**
 * Created by belen on 15/8/27.
 */
$(function () {
    var flag = true;
    $('.tab span').click(function () {
        if (flag) {
            var index = $(this).index();
            $(this).addClass('current').siblings().removeClass('current');
            $('.con-box .box').eq(index - 1).show().siblings().hide();
        }

    });

    var _tbody = $('#table-lottery tbody');
    var top = 0;
    var isStop = false;
    $('#table-lottery tbody').append(_tbody.find('tr').clone());
    var tim = setInterval(function () {
        if(isStop){
            return false;
        }else{
            top--
            if (top == -parseInt(_tbody.height() / 2)) {
                top = 0;
                _tbody.css('top', 0);
            } else {
                _tbody.css('top', top);
            }
        }
    }, 50);

    $('#table-lottery tbody').hover(function(){
        isStop = true;
    },function(){
        isStop = false;
    })

    var _tab = $('.tab .news');
    $('.btn-draw').click(function (e) {
        var NUM = $('#num').text();
        var _this = $(this);
        var _index = $(this).closest('.draw-box').find('li.current').index();
        if ($('#currentLoginName').val() == '') {
            alert('请先登录');
            return false;
        } else {
            if (_tab.eq(0).hasClass('current')) {
                $.ajax({
                    url: '/special/invest-lottery/novice',//新手
                    type: 'GET',
                    success: function (data) {
                        $('#num').text(data.remainingTimes);

                        if (NUM > 0) {
                            var res = data.investLotteryPrizeType;
                            var i = 0;
                            var timer = setInterval(function () {
                                i++;
                                if (_index == 11) {
                                    _index = 0;
                                } else {
                                    _index++;
                                }
                                _this.attr('disabled', true)
                                flag = false;
                                _this.closest('.draw-box').find('li').removeClass('current');
                                _this.closest('.draw-box').find('li').eq(_index).addClass('current');
                                if (res == 'A' && i > 20) {
                                    if (_index == 2) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }
                                } else if (res == 'B' && i > 20) {
                                    if (_index ==11) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }
                                } else if (res == 'C' && i > 20) {
                                    if (_index == 5 || _index == 9) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                } else if (res == 'D' && i > 20) {
                                    if (_index == 1 || _index == 7) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                } else if (res == 'E' && i > 20) {
                                    if (_index == 3 || _index == 10) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                } else if (res == 'F' && i > 20) {
                                    if (_index == 4 || _index == 8) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                } else if (res == 'G' && i > 20) {
                                    if (_index == 0 || _index == 6) {
                                        $('.bg-2 .title span').text(data.prizeDesc);
                                        $('.bg-2 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').hide();
                                        $('.bg-2').show();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                }

                            }, 100);
                        }else{
                            alert('暂时没有可用的抽奖机会');
                        }
                    }
                })
            } else if(_tab.eq(1).hasClass('current')){
                var NUM_1 =  $('#num_1').text();
                $.ajax({
                    url: '/special/invest-lottery/normal',//普通
                    type: 'GET',
                    success: function (data) {
                        $('#num_1').text(data.remainingTimes);
                        if (NUM_1 > 0) {
                            var res = data.investLotteryPrizeType;
                            var i = 0;
                            var timer = setInterval(function () {
                                i++;
                                if (_index == 11) {
                                    _index = 0;
                                } else {
                                    _index++;
                                }
                                _this.attr('disabled', true)
                                flag = false;
                                _this.closest('.draw-box').find('li').removeClass('current');
                                _this.closest('.draw-box').find('li').eq(_index).addClass('current');
                                if (res == 'A' && i > 20) {
                                    if (_index == 2) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }
                                } else if (res == 'B' && i > 20) {
                                    if (_index ==11) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }
                                } else if (res == 'C' && i > 20) {
                                    if (_index == 5 || _index == 9) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                } else if (res == 'D' && i > 20) {
                                    if (_index == 1 || _index == 7) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                } else if (res == 'E' && i > 20) {
                                    if (_index == 3 || _index == 10) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                } else if (res == 'F' && i > 20) {
                                    if (_index == 4 || _index == 8) {
                                        $('.bg-1 .title span').text(data.prizeDesc);
                                        $('.bg-1 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').show();
                                        $('.bg-2').hide();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                } else if (res == 'G' && i > 20) {
                                    if (_index == 0 || _index == 6) {
                                        $('.bg-2 .title span').text(data.prizeDesc);
                                        $('.bg-2 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                                        $('.bg-1').hide();
                                        $('.bg-2').show();
                                        flag = true;
                                        _this.removeAttr('disabled');
                                        clearInterval(timer);
                                    }

                                }

                            }, 100);
                        }else{
                            alert('暂时没有可用的抽奖机会');
                        }
                    }
                });
            }

        }

    });

    $('.close').click(function () {
        $(this).closest('.box').hide();
    })
})
