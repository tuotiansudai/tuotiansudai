require(['jquery', 'bootstrap', 'Validform', 'Validform_Datatype', 'bootstrapSelect', 'jquery-ui', 'csrf','bootstrapDatetimepicker'], function ($) {
    $(function () {
        var $errorDom = $('.form-error'),
            $bannerForm = $('.banner-form'),
            $submitBtn = $('#btnSave'),
            $selectDom = $('.selectpicker'),
            $activatedTime = $('#datepickerBegin'),
            $deactivatedTime = $('#datepickerEnd'),
            boolFlag = false;

        //渲染select表单
        $selectDom.selectpicker();
        $('#datepickerBegin,#datepickerEnd').datetimepicker({
            format: 'YYYY-MM-DD HH:mm'
        });
        $activatedTime.on("dp.change", function (e) {
            $deactivatedTime.data("DateTimePicker").minDate(e.date);
        });

        $deactivatedTime.on("dp.change", function (e) {
            $activatedTime.data("DateTimePicker").maxDate(e.date);
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

        $('select.appUrl').change(function () {
            var appUrl = $(this).val();
            if (appUrl == '') {
                $('.jump-to-link').removeClass('app-push-link').val('');
            } else {
                $('.jump-to-link').addClass('app-push-link').val('');
            }
        }).trigger('change');

        $('.webImageUrl,.appImageUrl').on('change', function () {
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
                        if ($self.hasClass('webImageUrl')) {
                            $('.banner-webImageUrl').val(data.title);
                            $('.webImageUrlImage').html('');
                            $('.webImageUrlImage').append('<img style="width:100%" src="' + data.title + '" alt="大图">');
                        }
                        if ($self.hasClass('appImageUrl')) {
                            $('.banner-appImageUrl').val(data.title)
                            $('.appImageUrlImage').html('');
                            $('.appImageUrlImage').append('<img style="width:100%" src="' + data.title + '" alt="小图">');
                        }
                    }
                });
            }).fail(function (message) {
                if ($self.hasClass('webImageUrl')) {
                    showErrorMessage(message, $('.banner-webImageUrl', $bannerForm));
                }
                if ($self.hasClass('appImageUrl')) {
                    showErrorMessage(message, $('.banner-appImageUrl', $bannerForm));
                }
            });
        });

        $('body').on('click', '.form-error', function () {
            $submitBtn.removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });

        $(".banner-form").Validform({
            btnSubmit: '#btnSave',
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            ignoreHidden: true,
            tiptype: function (msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    boolFlag = false;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function (curform) {
                $errorDom.html('');
            },
            callback: function (form) {
                boolFlag = true;
                return false;
            }
        });

        $submitBtn.on('click', function (event) {
            event.preventDefault();
            var $self = $(this);
            if (!IsURL($("input[name='url']").val())) {
                showErrorMessage("链接网址格式不正确,请以http://或 https://开始");
                return false;
            }

            var appUrl = $('.appUrl').val();
            var jumpToLink = $('.jump-link-text').val();

            if (appUrl == '' && jumpToLink == '') {
                showErrorMessage('定位地址不能为空', $('.jump-link-text', curform));
                return false;
            }

            if (appUrl != '') {
                $('.jump-link-text').val('');
            }


            if ($("input[name='name']").val().length >= 50) {
                showErrorMessage("名称最多50个中文字符!");
                return false;
            }

            if ($("input[name='url']").val().length >= 100) {
                showErrorMessage("链接最多100个中文字符!");
                return false;
            }


            if ($("input[name='title']").val().length >= 50) {
                showErrorMessage("分享后标题最多50个中文字符!");
                return false;
            }

            if ($("input[name='content']").val().length >= 500) {
                showErrorMessage("分享后描述最多500个中文字符");
                return false;
            }

            if ($("input[name='activatedTime']").val() == '') {
                showErrorMessage("生效时间不能为空");
                return false;
            }
            if ($("input[name='deactivatedTime']").val() == '') {
                showErrorMessage("失效时间不能为空");
                return false;
            }

            if ($('.source:checked').length <= 0) {
                showErrorMessage("终端必须选择");
                return false;
            }

            var order = $('.order').val();
            if (isNaN(order) || parseInt(order) <= 0) {
                showErrorMessage("序号必须为正整数");
                return false;
            }

            if (boolFlag) {
                if (confirm("确认提交审核?")) {
                    var sharedUrl = $('.sharedUrl').val();
                    $('.sharedUrl').val(sharedUrl);
                    $self.attr('disabled', 'disabled');
                    var operate;
                    if ($('.jq-id').val() != null && $('.jq-id').val() != '') {
                        operate = 'edit';
                    } else {
                        operate = 'create';
                    }
                    $bannerForm[0].target = '';
                    $bannerForm[0].action = "/banner-manage/" + operate;
                    $bannerForm[0].submit();
                }

            }
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

        function appendSourceParameter(url) {
            var url_parts = url.split('?');
            if (url_parts.length > 1) {
                if (url_parts[1].length > 0) {
                    if (url_parts[1].indexOf('source=app') >= 0) {
                        return url;
                    } else {
                        return url + '&source=app';
                    }
                } else {
                    return url + 'source=app';
                }

            } else {
                return url + '?source=app';
            }
        }
    });

});