/**
 * Created by belen on 15/8/27.
 */
$(function () {
    $('.tab span').click(function () {
        var index = $(this).index();
        $(this).addClass('current').siblings().removeClass('current');
        $('.con-box .box').eq(index).show().siblings().hide();
    });

    $(".table-box tbody").scrollable({
        width: 250,
        height: 250,
        direction: "top",
        duration: 100,
        scrollCount: 0,
        listSelector:'tbody'
    })


    $('.btn-draw').click(function () {
        var _this = $(this);
        var _index = $(this).closest('.draw-box').find('li.current').index();
        setInterval(function () {
            if (_index == 11) {
                _index = 0;
            }else{
                _index++;
            }
            _this.closest('.draw-box').find('li').removeClass('current');
            _this.closest('.draw-box').find('li').eq(_index).addClass('current');
        }, 50)
    });
})
