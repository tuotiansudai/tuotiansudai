define(['jquery', 'jquery.validate', 'jquery.validate.extension','drag'], function ($) {
    var $closeBtn=$('.count-form .close-count'),
        $countForm=$('.count-form'),
        $calBtn=$('.cal-btn');

    $(window).scrollTop()>($(window).height()/2)?$('.back-top').fadeIn('fast'):$('.back-top').fadeOut('fast');
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

    $("#countForm").validate({
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
                resultNum=moneyNum+moneyNum*monthNum*biteNum*30*0.9/365;
            $resultNum.text(resultNum.toFixed(2));
        },
        errorPlacement: function(error, element) {
            error.insertAfter(element.parent());
        }
    });
    //close calculator
    $closeBtn.on('click', function(event) {
        event.preventDefault();
        var $navList=$('.fix-nav-list li');
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

    //feedback click
    $('.type-list dt').on('click', function(event) {
        event.preventDefault();
        var $self=$(this),
            $list=$self.siblings('dd');
        $list.slideToggle('fast');
    });
    $('.type-list dd').on('click', function(event) {
        event.preventDefault();
        var $self=$(this),
            $parent=$self.parent('.type-list'),
            $dt=$parent.find('dt'),
            $dd=$parent.find('dd');
        $dt.text($self.text());
        $dd.hide();
    });

    $('.feed-close').on('click', function(event) {
        event.preventDefault();
        var $self=$(this),
            $showFeed=$('.fix-nav-list .show-feed'),
            $tipDom=$self.closest('.feedback-model');
        $tipDom.hide();
        $showFeed.removeClass('active');
    });

    $('.fix-nav-list .show-feed').on('click', function(event) {
        event.preventDefault();
        var $self=$(this),
            $feedBack=$('.feedback-container');
        $self.addClass('active');
        $feedBack.show();
    });
    //support placeholder
    function placeholder(nodes, pcolor) {
      if (nodes.length && !("placeholder" in document_createElement_x("input"))) {
        for (i = 0; i<nodes.length;i++){
          var self = nodes[i],
            placeholder = self.getAttribute('placeholder') || ''; self.onfocus = function() {
            if (self.value == placeholder) {
              self.value = '';
              self.style.color = "";
            }
          }
          self.onblur = function() {
            if (self.value == '') {
              self.value = placeholder;
              self.style.color = pcolor;
            }
          }
          self.value = placeholder; self.style.color = pcolor;
        }
      }
    }
});








