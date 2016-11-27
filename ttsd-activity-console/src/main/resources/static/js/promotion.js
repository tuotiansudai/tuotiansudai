require(['jquery', 'Validform', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'csrf'], function ($, _) {
    var $promotionForm = $('.promotion-form'),
        $selectDom = $('.selectpicker'), //select表单
        $dateStart = $('#startTime'), //开始时间
        $dateEnd = $('#endTime'), //结束时间
        $submitBtn = $('.promotion-confirm'), //提交按钮
        boolFlag = false, //校验布尔变量值
        currentErrorObj = null,
        $errorDom = $('.form-error'); //错误提示节点

    //渲染select表单
    $selectDom.selectpicker();

    //起始时间绑定插件
    $dateStart.datetimepicker({
        format: 'YYYY-MM-DD'
    }).on('dp.change', function(e) {
        $dateEnd.data("DateTimePicker").minDate(e.date);
    });

    //结束时间绑定插件
    $dateEnd.datetimepicker({
        format: 'YYYY-MM-DD'
    });


    var _URL = window.URL || window.webkitURL;

    $('.promotionImageUrl').on('change', function () {
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
                    if ($self.hasClass('promotionImageUrl')) {
                        $('.promotionImageUrl').val(data.title);
                        $('.imageUrl').html('');
                        $('.imageUrl').append('<img style="width:100%" src="/' + data.title + '" alt="缩略图">');
                    }
                }
            });
        }).fail(function (message) {
            if ($self.hasClass('promotionImageUrl')) {
                $('.imageUrl').html('');
                $('.promotionImageUrl').val('');
                showErrorMessage(message, $('.promotionImageUrl', $promotionForm));
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

    $('select.linkUrl').change(function () {
        var linkUrl = $(this).val();
        if (linkUrl == '') {
            $('.jump-to-link').removeClass('app-push-link').val('');
        } else {
            $('.jump-to-link').addClass('app-push-link').val('');
        }
    }).trigger('change');

    $('.jump-link-text').on('focusout',function(e){
        e.preventDefault();
        $('.linkUrl').find('option:contains("其他")').val($(this).val()).trigger('click');
    });

    //表单校验初始化参数
    $promotionForm.Validform({
        btnSubmit: '.promotion-confirm',
        tipSweep: true, //表单提交时触发显示
        focusOnError: false,
        ignoreHidden: true,
        tiptype: function (msg, o, cssctl) {
            if (o.type == 3) {
                var msg = o.obj.attr('errormsg') || msg;
                showErrorMessage(msg, o.obj);
            }
        },
        beforeCheck: function ($promotionForm) {




            $errorDom.html('');
        },
        callback: function ($promotionForm) {
            boolFlag = true;
            return false;
        }
    });
    $submitBtn.on('click', function(event) {
        event.preventDefault();
        var $self = $(this);

        var linkUrl = $('.linkUrl').val();
        var jumpToLink = $('.jump-link-text').val();

        if (linkUrl == '' && jumpToLink == '') {
            showErrorMessage('定位地址不能为空', $('.jump-link-text', curform));
            return false;
        }

        if(linkUrl != ''){
            $('.jump-link-text').val('');
        }

        if ($("input[name='name']").val().length >= 50) {
            showErrorMessage("名称最多50个中文字符!");
            return false;
        }

        var seq = $('.seq').val();
        if (isNaN(seq) || parseInt(seq) <= 0) {
            showErrorMessage("序号必须为正整数");
            return false;
        }

        if (boolFlag) {
            if (confirm("确认创建吗?")) {
                $self.attr('disabled', 'disabled');
                var operate;
                if ($('.jq-id').val() != null && $('.jq-id').val() != '') {
                    operate = 'edit';
                } else {
                    operate = 'create';
                }
                $promotionForm[0].target = '';
                $promotionForm[0].action = "/activity-console/activity-manage/promotion/" + operate;
                $promotionForm[0].submit();
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

    //关闭警告提示
    $('body').on('click', '.form-error', function () {
        if (!!currentErrorObj) {
            currentErrorObj.focus();
        }
    });

    $('.promotion-cancel').on('click',function(event){
        event.preventDefault();
        location.href = '/activity-console/activity-manage/promotion/promotion-list';
    });

    //验证网址的合法性
    function IsURL(str_url) {
        str_url = str_url.match(/^(https|http)?:\/\/.+/);
        if (str_url == null) {
            return false;
        } else {
            return true;
        }
    }

});