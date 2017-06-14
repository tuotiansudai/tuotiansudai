require(['jquery', 'bootstrap', 'Validform', 'Validform_Datatype', 'bootstrapDatetimepicker', 'jquery-ui', 'csrf'], function ($) {
    $(function () {

        $('#datepicker').datetimepicker({
            format: 'YYYY-MM-DD'
        });

        $('#datepicker').on('dp.change', function () {
            location.href = '/activity-console/activity-manage/newman-tyrant?tradingTime=' + $('#tradingTime').val();
        });

        $('.invest').on('click', function () {
            if (!$(this).hasClass('active')) {
                $(this).addClass('active');
            }
            $('.select-date').show();
            $('.upload').removeClass('active');
            $('.tyrant-ranking').show();
            $('.newman-ranking').show();
            $('.upload-image').hide();
            $('.avg-newman').show();
            $('.avg-tyrant').show();
        });


        $('.upload').on('click', function () {
            if (!$(this).hasClass('active')) {
                $(this).addClass('active');
            }
            $('.invest').removeClass('active');
            $('.upload-image').show();
            $('.select-date').hide();
            $('.tyrant-ranking').hide();
            $('.newman-ranking').hide();
            $('.avg-newman').hide();
            $('.avg-tyrant').hide();
        });

        var boolFlag = false, //校验布尔变量值
            $errorDom = $('.form-error'), //错误提示节点
            $prizeForm = $('.prize-form');
        var _URL = window.URL || window.webkitURL;
        $('.gold-prize-image,.silver-prize-image').on('change', function () {
            var $self = $(this),
                file = $self.find('input').get(0).files[0];
            checkImage(file).done(function () {
                var formData = new FormData();
                formData.append('upfile', file);
                $.ajax({
                    url: '/ueditor?action=uploadimage',
                    type: 'POST',
                    data: formData,
                    dataType: 'JSON',
                    contentType: false,
                    processData: false
                })
                    .done(function (data) {
                        if (data.state) {
                            if ($self.hasClass('gold-prize-image')) {
                                $('.gold-image-url').val(data.title);
                                $('.goldThumbImage').html('');
                                $('.goldThumbImage').append('<img style="width:100%" src="' + data.title + '" alt="金奖缩略图">');
                            }
                            if ($self.hasClass('silver-prize-image')) {
                                $('.silver-image-url').val(data.title);
                                $('.silverThumbImage').html('');
                                $('.silverThumbImage').append('<img style="width:100%" src="' + data.title + '" alt="银奖缩略图">');
                            }
                        }
                    });
            }).fail(function (message) {
                if ($self.hasClass('gold-prize-image')) {
                    showErrorMessage(message, $('.gold-image-url', $prizeForm));
                }
                if ($self.hasClass('silver-prize-image')) {
                    showErrorMessage(message, $('.silver-image-url', $prizeForm));
                }
            });

        });

        var checkImage = function (file) {
            var defer = $.Deferred(),
                img = new Image();
            img.src = _URL.createObjectURL(file);
            img.onload = function () {
                if (this.width > 370) {
                    defer.reject('图片宽应为370px!');
                    return;

                }
                if (this.height > 200) {
                    defer.reject('图片高应为200px!');
                    return;
                }
                defer.resolve(file);
            }
            return defer.promise();

        };


        //关闭警告提示
        $('body').on('click', '.form-error', function () {
            $('.tomorrow-prize-save,.today-prize-save').removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });
        //提交表单
        $('.tomorrow-prize-save,.today-prize-save').on('click', function (event) {
            event.preventDefault();
            var $self = $(this),
                url = "/activity-console/activity-manage/newman-tyrant/upload-image";
            if ($self.hasClass("tomorrow-prize-save")) {
                url = url + "?today=false";
            }
            if ($self.hasClass("today-prize-save")) {
                url = url + "?today=true";
            }
            // if (boolFlag) {
            if (confirm("确认提交更新?")) {
                $.ajax({
                    url: url,
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8',
                    data: JSON.stringify({"goldPrizeName": $('.gold-prize-name').val(), "goldImageUrl": $('.gold-image-url').val(),
                        "silverPrizeName":$('.silver-prize-name').val(),"silverImageUrl":$('.silver-image-url').val()})
                }).done(function (data) {
                    boolFlag = false;
                    $prizeForm[0].reset();
                    $('.goldThumbImage').html('');
                    $('.silverThumbImage').html('');
                    if ($self.hasClass("tomorrow-prize-save")) {

                        $('.tomorrowThumbImage').html('');
                        $('.tomorrowThumbImage').append('<img src="' + data.goldImageUrl + '" alt="金奖缩略图">');
                        $('.tomorrowThumbImage').append('<img src="' + data.silverImageUrl + '" alt="银奖缩略图">');
                    }
                    if ($self.hasClass("today-prize-save")) {
                        $('.todayThumbImage').html('');
                        $('.todayThumbImage').append('<img src="' + data.goldImageUrl + '" alt="金奖缩略图">');
                        $('.todayThumbImage').append('<img src="' + data.silverImageUrl + '" alt="银奖大奖缩略图">');
                    }

                })
            }

            // }
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
