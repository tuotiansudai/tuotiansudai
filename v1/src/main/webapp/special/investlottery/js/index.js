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
            top--;
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
    });

    var roll = function(btn, data){
        var _index = $(this).closest('.draw-box').find('li.current').index();
        btn.attr('disabled', true);

        var res = data.investLotteryPrizeType;
        var i = 0;
        var timer = setInterval(function () {
            i++;
            if (_index >= 11) {
                _index = 0;
            } else {
                _index++;
            }
            flag = false;

            btn.closest('.draw-box').find('li').removeClass('current');
            btn.closest('.draw-box').find('li').eq(_index).addClass('current');

            if(i<=20){return;} //确保转够一圈

            if (   (res == 'A' && _index == 2)
                || (res == 'B' && _index == 11)
                || (res == 'C' && (_index == 5 || _index == 9))
                || (res == 'D' && (_index == 1 || _index == 7))
                || (res == 'E' && (_index == 3 || _index == 10))
                || (res == 'F' && (_index == 4 || _index == 8))
            ) {
                // 实物奖品
                $('.bg-1 .title span').text(data.prizeDesc);
                $('.bg-1 .txtv span').text(data.prizeDesc);
                $('.bg-1').show();
                $('.bg-2').hide();
                flag = true;
                btn.removeAttr('disabled');
                clearInterval(timer);
            } else if (res == 'G' && (_index == 0 || _index == 6)) {
                // 现金奖品
                $('.bg-2 .title span').text(data.prizeDesc);
                $('.bg-2 .txtv span').text(data.prizeDesc.replace(/[^0-9.]/ig, ""));
                $('.bg-1').hide();
                $('.bg-2').show();
                flag = true;
                btn.removeAttr('disabled');
                clearInterval(timer);
            }
        }, 100);
    };

    $('.btn-draw').click(function (e) {
        var _this = $(this);
        var _tab = $('.tab .news');
        if ($('#currentLoginName').val() == '') {
            alert('请先登录');
            return false;
        } else {

            var url, num_obj;
            if (_tab.eq(0).hasClass('current')) {
                url = '/special/invest-lottery/novice';//新手
                num_obj = $('#num');
            }else {
                url = '/special/invest-lottery/normal';//普通
                num_obj = $('#num_1');
            }

            var NUM = num_obj.text();

            $.ajax({
                url: url,
                type: 'GET',
                success: function (data) {
                    num_obj.text(data.remainingTimes);
                    if (NUM > 0) {
                        roll(_this, data);
                    }else{
                        alert('暂时没有可用的抽奖机会');
                    }
                }
            })
        }

    });

    $('.close').click(function () {
        $(this).closest('.box').hide();
    })
})
