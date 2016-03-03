require(['jquery', 'moment', 'pagination', 'mustache'],
    function($, moment, pagination, Mustache) {
        $(function() {

            var $navBtn=$('.column-title .title-navli'),
                $signBtn=$('#signBtn'),
                $signTip=$('#signLayer'),
                $closeSign=$('#closeSign'),
                $taskBtn=$('#taskBtn'),
                $taskTip=$('#taskLayer'),
                $beansOperation = $(".beans-operat"),
                btnSignIn = $('.btn-sign',$beansOperation),
                $closeTask=$('#closeTask');
            //change model
            $navBtn.on('click', function(event) {
                event.preventDefault();
                var $self=$(this),
                    index=$self.index();
                $self.addClass('active').siblings().removeClass('active');
                $('.content-list .choi-beans-list:eq('+index+')').show().siblings().hide();
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

            btnSignIn.click(function(){
                var _this = $(this),
                    signText = $(".sign-text");
                    tomorrowText = $(".tomorrow-text");

                $.ajax({
                    url: _this.data('url'),
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function(response){
                    if(response.data.status){
                        signText.html("签到成功，领取"+ response.data.signInPoint+"5财豆！");
                        tomorrowText.html("明日可领"+response.data.nextSignInPoint+"财豆");
                    }
                })
            });


        });
    });