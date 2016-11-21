require(['jquery', 'bootstrap','Validform','Validform_Datatype', 'bootstrapDatetimepicker','jquery-ui','csrf'], function ($) {
    $(function() {

        $('#datepicker').datetimepicker({
            format: 'YYYY-MM-DD'
        });

        $('#datepicker').on('dp.change', function(){
            location.href = '/activity-console/activity-manage/hero-ranking?tradingTime=' + $('#tradingTime').val();
        });

        $('.invest').on('click', function(){
            if (!$(this).hasClass('active')) {
                $(this).addClass('active');
            }
            $('.select-date').show();
            $('.referrer').removeClass('active');
            $('.upload').removeClass('active');
            $('.invest-ranking').show();
            $('.referrer-ranking').hide();
            $('.upload-image').hide();
            $('.avg-invest').show();
        });

        $('.referrer').on('click', function(){
            if (!$(this).hasClass('active')) {
                $(this).addClass('active');
            }
            $('.select-date').show();
            $('.invest').removeClass('active');
            $('.upload').removeClass('active');
            $('.referrer-ranking').show();
            $('.invest-ranking').hide();
            $('.upload-image').hide();
            $('.avg-invest').hide();
        });

        $('.upload').on('click', function(){
            if (!$(this).hasClass('active')) {
                $(this).addClass('active');
            }
            $('.invest').removeClass('active');
            $('.referrer').removeClass('active');
            $('.upload-image').show();
            $('.select-date').hide();
            $('.referrer-ranking').hide();
            $('.invest-ranking').hide();
        });

        var boolFlag = false, //校验布尔变量值
            $errorDom = $('.form-error'), //错误提示节点
            $prizeForm = $('.prize-form');
        var _URL = window.URL || window.webkitURL;
        $('.prize-image').on('change',function(){
            var $self = $(this),
                file = $self.find('input').get(0).files[0];
                checkImage(file).done(function(){
                    var formData = new FormData();
                    formData.append('upfile',file);
                    $.ajax({
                        url:'/ueditor?action=uploadimage',
                        type:'POST',
                        data:formData,
                        dataType:'JSON',
                        contentType: false,
                        processData: false
                    })
                        .done(function(data){
                            if(data.state){
                                $('.image-url').val(data.title);
                                $('.thumbImage').html('');
                                $('.thumbImage').append('<img style="width:100%" src="' + data.title + '" alt="神秘大奖缩略图">');
                            }
                        });
                }).fail(function(message){
                    showErrorMessage(message, $('.image-url', $prizeForm));
                });

        });

        var checkImage = function(file){
            var defer = $.Deferred(),
                img = new Image();
                img.src = _URL.createObjectURL(file);
                img.onload = function(){
                    if(this.width > 240){
                        defer.reject('图片宽应为240px!');
                        return;

                    }
                    if(this.height > 310){
                        defer.reject('图片高应为310px!');
                        return;
                    }
                    defer.resolve(file);
                }
            return defer.promise();

        };

        //表单校验初始化参数
        $(".prize-form").Validform({
            btnSubmit: '.tomorrow-prize-save,.today-prize-save',
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            ignoreHidden:true,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function(curform) {
                $errorDom.html('');

            },
            callback: function(form) {
                boolFlag = true;
                return false;
            }
        });

        //关闭警告提示
        $('body').on('click', '.form-error', function () {
            $('.tomorrow-prize-save,.today-prize-save').removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });
        //提交表单
        $('.tomorrow-prize-save,.today-prize-save').on('click', function(event) {
            event.preventDefault();
            var $self = $(this),
                url = "/activity-console/activity-manage/upload-image";
            if($self.hasClass("tomorrow-prize-save")){
                url = url + "?today=false";
            }
            if($self.hasClass("today-prize-save")){
                url = url + "?today=true";
            }
            if (boolFlag) {
                if (confirm("确认提交更新?")) {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        dataType: 'json',
                        contentType: 'application/json; charset=UTF-8',
                        data: JSON.stringify({"prizeName":$('.prize-name').val(),"imageUrl":$('.image-url').val()})
                    }).done(function (data) {
                        boolFlag = false;
                        $('.prize-name').val('');
                        $('.image-url').val('');
                        $('.thumbImage').html('');
                        if($self.hasClass("tomorrow-prize-save")){

                            $('.tomorrowThumbImage').html('');
                            $('.tomorrowThumbImage').append('<img style="width:100%" src="' + data.imageUrl + '" alt="神秘大奖缩略图">');
                        }
                        if($self.hasClass("today-prize-save")){
                            $('.todayThumbImage').html('');
                            $('.todayThumbImage').append('<img style="width:100%" src="' + data.imageUrl + '" alt="神秘大奖缩略图">');
                        }

                    })
                }

            }
        });
        /**
         * @msg  {[string]} //文字信息
         * @obj  {[object]} //传入的dom节点
         * @return {[html]} //生成html
         */
        function showErrorMessage(msg, obj) {
            currentErrorObj = obj;
            var html = '';
            html += '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">';
            html += '<button type="button" class="close" data-dismiss="alert" aria-label="Close">';
            html += '<span aria-hidden="true">&times;</span>';
            html += '</button>';
            html += '<span class="txt">创建失败：' + msg + '</span>';
            html += '</div>';
            $errorDom.append(html);
        }
    });
});
