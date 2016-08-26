require(['jquery', 'Validform', 'bootstrap','jquery-ui', 'csrf'], function ($, _) {
    var $luxuryForm = $('.luxury-form'),
        $submitBtn = $('.luxury-confirm'), //提交按钮
        boolFlag = false, //校验布尔变量值
        currentErrorObj = null,
        $errorDom = $('.form-error'); //错误提示节点

    var _URL = window.URL || window.webkitURL;

    $('.luxuryImage,.luxuryIntroduceImage').on('change', function () {
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
                    if ($self.hasClass('luxuryImage')) {
                        $('.luxuryImageUrl').val(data.title);
                        $('.image').html('');
                        $('.image').append('<img style="width:100%" src="/' + data.title + '" alt="缩略图">');

                    }
                    if ($self.hasClass('luxuryIntroduceImage')) {
                        $('.luxuryIntroduceUrl').val(data.title);
                        $('.imageIntroduce').html('');
                        $('.imageIntroduce').append('<img style="width:100%" src="/' + data.title + '" alt="缩略图">');

                    }

                }
            });
        }).fail(function (message) {
            if ($self.hasClass('luxuryImage')) {
                $('.image').html('');
                $('.luxuryImageUrl').val('');
                showErrorMessage(message, $('.luxuryImageUrl', $luxuryForm));
            }
            if ($self.hasClass('luxuryIntroduceImage')) {
                $('.imageIntroduce').html('');
                $('.luxuryIntroduceUrl').val('');
                showErrorMessage(message, $('.luxuryIntroduceUrl', $luxuryForm));
            }

        });

    });

    var checkImage = function (file, width, height) {
        var defer = $.Deferred(),
            img = new Image();
        img.src = _URL.createObjectURL(file);
        img.onload = function () {
            if (this.width != width) {
                defer.reject('图片长宽应为' + width);
                return;

            }
            if (this.height != height) {
                defer.reject('图片长宽应为' + height);
                return;
            }
            defer.resolve(file);
        }
        return defer.promise();

    };
    //表单校验初始化参数
    $luxuryForm.Validform({
        btnSubmit: '.luxury-confirm',
        tipSweep: true, //表单提交时触发显示
        focusOnError: false,
        ignoreHidden: true,
        tiptype: function (msg, o, cssctl) {
            if (o.type == 3) {
                var msg = o.obj.attr('errormsg') || msg;
                showErrorMessage(msg, o.obj);
            }
        },
        beforeCheck: function ($luxuryForm) {
            $errorDom.html('');
        },
        callback: function ($luxuryForm) {
            boolFlag = true;
            return false;
        }
    });
    $submitBtn.on('click', function(event) {
        event.preventDefault();
        var $self = $(this);
        if (boolFlag) {
            $self.attr('disabled', 'disabled');
            $luxuryForm[0].submit();
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
    $('.luxury-cancel').on('click',function(event){
        event.preventDefault();
        location.href = '/activity-console/activity-manage/luxury/user-luxury-list';
    });
});
