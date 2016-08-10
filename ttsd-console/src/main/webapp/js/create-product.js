require(['jquery', 'bootstrap', 'Validform', 'bootstrapDatetimepicker', 'Validform_Datatype', 'bootstrapSelect', 'jquery-ui'], function ($) {

    $('#startTime').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
    $('#endTime').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});

    var $orderNumber = $('.order-number');

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

    $('.imageUrlProduct').on('change', function () {
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
                    $('.form-imageUrl').val(data.title);
                    $('.imageUrlImage').html('<img style="width:100%" src="/' + data.title + '" >');
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
        tiptype:3,
        label:'.label',
        showAllError:true,
        beforeSubmit:function(curform) {
            //debugger
            curform[0].submit();
            //return false;
        }

    })



});