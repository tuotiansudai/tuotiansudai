define(['jquery', 'jquery.validate', 'jquery.validate.extension','drag'], function ($) {
    var $closeBtn=$('.count-form .close-count'),
        $countForm=$('.count-form'),
        $calBtn=$('.cal-btn');

    $(window).scrollTop()>$(window).scrollTop()>($(window).height()/2)?$('.back-top').fadeIn('fast'):$('.back-top').fadeOut('fast');
    $(window).scroll(function() {
        if($(window).scrollTop()>($(window).height()/2)){
            $('.back-top').fadeIn('fast');
        }else{
            $('.back-top').fadeOut('fast');
        }
    });
    $('.back-top .nav-text').on('click', function(event) {//back top
        event.preventDefault();
        $('body,html').animate({scrollTop:0},'fast');
    });

    var $validator = $("#countForm").validate({
        debug:true,
        rules: {
            money: {
                required: true,
                number: true
            },
            month: {
                required: true,
                number: true
            },
            bite: {
                required: true,
                number: true
            }
        },
        messages: {
            money: {
                required: '请输入投资金额！',
                number: '请输入有效的数字！'
            },
            month: {
                required: '请输入投资时长！',
                number: '请输入有效的数字！'
            },
            bite: {
                required: '请输入年化利率！',
                number: '请输入有效的数字！'
            }
        },
        submitHandler: function(form) {
            var moneyNum=Math.round($('#moneyNum').val()),
                monthNum=Math.round($('#monthNum').val()),
                biteNum=Math.round($('#biteNum').val())/100,
                $resultNum=$('#resultNum'),
                resultNum=moneyNum+moneyNum*monthNum*biteNum*0.9;
            $resultNum.text(resultNum.toFixed(2));
        },
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent());
        }
    });
    //close calculator
    $closeBtn.on('click', function(event) {
        event.preventDefault();
        var $self=$(this),
            $navList=$('.fix-nav-list li');
        $countForm.hide();
        $navList.removeClass('active');
    });
    //calculator show
    $calBtn.on('click', function(event) {
        event.preventDefault();
        $(this).addClass('active');
        $countForm.show();
    });
    //reset form
    $("#resetBtn").on('click', function(event) {
        event.preventDefault();
        $countForm.find('.int-text').val('');
        $('#resultNum').text('0');
    });
    //calculator drag
    $countForm.dragging({
        move : 'both',
        randomPosition : false,
        hander: '.hander'
    });
});








