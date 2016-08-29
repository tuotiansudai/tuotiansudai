require(['jquery', 'Validform', 'bootstrap','jquery-ui', 'csrf'], function ($, _) {
    var $travelForm = $('.travel-form'),
        $submitBtn = $('.travel-confirm'), //提交按钮
        boolFlag = false, //校验布尔变量值
        currentErrorObj = null,
        $errorDom = $('.form-error'); //错误提示节点

    var _URL = window.URL || window.webkitURL;

    $('.travelImage,.travelIntroduceImage').on('change', function () {
        var $self = $(this),
            imageWidth,
            imageHeight;
        var file = $self.find('input').get(0).files[0];
        var formData = new FormData();
        formData.append('upfile', file);
        imageWidth = $self.find('input').attr("imageWidth");
        imageHeight = $self.find('input').attr("imageHeight");
        checkImage(file, imageWidth, imageHeight).done(function () {
            var formData = new FormData();
            formData.append('upfile', file);
            $.ajax({
                url: '/ueditor?action=uploadimage',
                type: 'POST',
                data: formData,
                dataType: 'JSON',
                contentType: false,
                processData: false
            }).done(function (data) {
                if (data.state) {
                    if ($self.hasClass('travelImage')) {
                        $('.travelImageUrl').val(data.title);
                        $('.image').html('');
                        $('.image').append('<img style="width:100%" src="/' + data.title + '" alt="缩略图">');

                    }
                    if ($self.hasClass('travelIntroduceImage')) {
                        $('.travelIntroduceUrl').val(data.title);
                        $('.imageIntroduce').html('');
                        $('.imageIntroduce').append('<img style="width:100%" src="/' + data.title + '" alt="缩略图">');

                    }

                }
            });
        }).fail(function (message) {
            if ($self.hasClass('travelImage')) {
                $('.image').html('');
                $('.travelImageUrl').val('');
                showErrorMessage(message, $('.travelImageUrl', $travelForm));
            }
            if ($self.hasClass('travelIntroduceImage')) {
                $('.imageIntroduce').html('');
                $('.travelIntroduceUrl').val('');
                showErrorMessage(message, $('.travelIntroduceUrl', $travelForm));
            }

        });

    });

    var checkImage = function (file, width, height) {
        var defer = $.Deferred(),
            img = new Image();
        img.src = _URL.createObjectURL(file);
        img.onload = function () {
            if (this.width != width && width != undefined) {
                defer.reject('图片宽度应为' + width);
                return;

            }
            if (this.height != height && height != undefined) {
                defer.reject('图片高度应为' + height);
                return;
            }
            defer.resolve(file);
        }
        return defer.promise();

    };
    //表单校验初始化参数
    $travelForm.Validform({
        btnSubmit: '.travel-confirm',
        tipSweep: true, //表单提交时触发显示
        focusOnError: false,
        ignoreHidden: true,
        tiptype: function (msg, o, cssctl) {
            if (o.type == 3) {
                var msg = o.obj.attr('errormsg') || msg;
                showErrorMessage(msg, o.obj);
            }
        },
        beforeCheck: function ($travelForm) {
            $errorDom.html('');
        },
        callback: function ($travelForm) {
            boolFlag = true;
            return false;
        }
    });
    $submitBtn.on('click', function(event) {
        event.preventDefault();
        var $self = $(this);
        if (boolFlag) {
            $self.attr('disabled', 'disabled');
            $travelForm[0].submit();
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

    //关闭警告提示
    $('body').on('click', '.form-error', function () {
        if (!!currentErrorObj) {
            currentErrorObj.focus();
        }
    });

    $('.travel-cancel').on('click',function(event){
        event.preventDefault();
        location.href = '/activity-console/activity-manage/travel/user-travel-list';
    });
});
