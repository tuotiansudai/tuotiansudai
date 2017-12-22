require(['jquery', 'bootstrap', 'Validform', 'bootstrapDatetimepicker', 'Validform_Datatype', 'bootstrapSelect', 'jquery-ui'], function ($) {

    var $selectDom = $('.selectpicker'),
        $dateStart = $('#startTime'),
        $dateEnd = $('#endTime'),
        $orderNumber = $('.order-number'),
        $submitBtn = $('#btnSave'),
        $errorDom = $('.form-error'),
        boolFlag = false, //校验布尔变量值
        $productForm = $('.form-list');

    $selectDom.selectpicker();

    $dateStart.datetimepicker({
        format: 'YYYY-MM-DD'
    }).on('dp.change', function (e) {
        $dateEnd.data("DateTimePicker").minDate(e.date);
    });

    $dateEnd.datetimepicker({
        format: 'YYYY-MM-DD'
    });

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

    var _URL = window.URL || window.webkitURL;

    var checkImage = function (file, width, height) {
        var defer = $.Deferred(),
            img = new Image();
        img.src = _URL.createObjectURL(file);
        img.onload = function () {
            if (width && this.width != width) {
                defer.reject('图片宽应为' + width);
                return;
            }
            if (height && this.height != height) {
                defer.reject('图片长应为' + height);
                return;
            }
            defer.resolve(file);
        }
        return defer.promise();
    };

    $('.imageUrlProduct,.appUrlProduct,.webUrlProduct').on('change', function () {
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
                    if ($self.hasClass("imageUrlProduct")) {
                        $('.form-imageUrl').val(data.title);
                        $('.imageUrlImage').html('<img style="width:100%" src="' + data.title + '" >');
                    }
                    if ($self.hasClass("appUrlProduct")) {
                        $('.appPictureUrl').val(data.title);
                        $('.appThumbnail').html('<img style="width:100%" src="' + data.title + '" >');
                    }
                    if ($self.hasClass("webUrlProduct")) {
                        $('.webPictureUrl').val(data.title);
                        $('.webThumbnail').html('<img style="width:100%" src="' + data.title + '" >');
                    }

                }
            });
        }).fail(function (message) {
            showErrorMessage(message, $('.form-imageUrl', $('.form-list')));
        });
    });

    var orderType = $orderNumber.data('type').toUpperCase();

    switch (orderType) {
        case 'VIRTUAL':
            $orderNumber.val('1');
            break;
        case 'PHYSICAL':
            $orderNumber.val('2');
            break;
    }

    $(".form-list").Validform({
        btnSubmit: '#btnSave',
        tipSweep: true,
        focusOnError: false,
        ignoreHidden: true,
        tiptype: function (msg, o, cssctl) {
            if (o.type == 3) {
                var msg = o.obj.attr('errormsg') || msg;
                showErrorMessage(msg, o.obj);
            }
        },
        callback: function (form) {
            boolFlag = true;
            return false;
        }
    });

    $('body').on('click', '.form-error', function () {
        $submitBtn.removeAttr('disabled');
        if (!!currentErrorObj) {
            currentErrorObj.focus();
        }
    });

    $submitBtn.on('click', function (event) {
        event.preventDefault();
        var $self = $(this);
        if (boolFlag) {
            $self.attr('disabled', 'disabled');
            $productForm[0].submit();
        }
    });
});