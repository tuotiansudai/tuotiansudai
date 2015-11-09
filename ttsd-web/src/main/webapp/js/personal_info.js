require(['jquery', 'layer', 'jquery.validate', 'jquery.validate.extension', 'jquery.form'], function ($, layer) {
    $(function () {
        var $InfoBox = $('#personInfoBox'),
            $changeEmailLayer = $('.setEmail', $InfoBox),
            $changePasswordLayer = $('.setPass', $InfoBox),
            $changeEmailDOM = $('#changeEmailDOM'),
            $changePassDOM = $('#changePassDOM'),
            $EmailForm = $('form', $changeEmailDOM),
            $passwordForm = $('form', $changePassDOM);

        $changeEmailLayer.on('click', function () {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '绑定邮箱',
                area: ['490px', '220px'],
                shadeClose: true,
                content: $changeEmailDOM,
                cancel: function () {
                    $passwordForm.validate().resetForm();
                }
            });
        });

        $EmailForm.validate({
            success: 'form-valid',
            focusInvalid: false,
            errorClass: 'form-error',
            rules: {
                email: {
                    required: true,
                    email: true,
                    isExist: "/personal-info/email/{0}/is-exist"
                }
            },
            onkeyup: function (element, event) {
                var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];
                if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                    this.element(element);
                }
            },
            onFocusOut: function (element) {
                if (!this.checkable(element) && !this.optional(element)) {
                    this.element(element);
                }
            },
            messages: {
                email: {
                    required: "请输入有效邮箱",
                    email: "请输入有效邮箱",
                    isExist: "邮箱已存在"
                }
            },
            submitHandler: function (form) {
                var self = this;
                $(form).ajaxSubmit({
                    dataType: 'json',
                    beforeSubmit: function (arr, $form, options) {
                        $('.change-email-success .email').html($('input[name="email"]').val());
                        self.resetForm();
                        layer.closeAll();
                    },
                    success: function (response) {
                        var data = response.data;
                        if (data.status) {
                            layer.open({
                                type: 1,
                                title: '验证邮箱',
                                area: ['500px', '220px'],
                                shadeClose: true,
                                content: $('#change-email-success'),
                                btn: ['返回'],
                                yes: function (index, layero) {
                                    layer.close(index);
                                }
                            });
                        } else {
                            layer.msg('邮箱绑定失败，请重试！', {type: 1, time: 2000});
                        }
                    },
                    error: function () {
                        layer.msg('邮箱绑定失败，请重试！', {type: 1, time: 2000});
                    },
                    complete: function () {
                    }
                });
                return false;
            }
        });

        $changePasswordLayer.on('click', function () {
            layer.open({
                type: 1,
                move: false,
                offset: "200px",
                title: '修改密码',
                area: ['500px', '300px'],
                shadeClose: false,
                content: $changePassDOM,
                cancel: function () {
                    $passwordForm.validate().resetForm();
                }
            });
        });

        $passwordForm.validate({
            focusCleanup: true,
            success: 'form-valid',
            focusInvalid: false,
            errorClass: 'form-error',
            onkeyup: function (element, event) {
                var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];
                if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                    this.element(element);
                }
            },
            onFocusOut: function (element) {
                if (!this.checkable(element) && !this.optional(element)) {
                    this.element(element);
                }
            },
            submitHandler: function (form) {
                var self = this;
                $(form).ajaxSubmit({
                    dataType: 'json',
                    beforeSubmit: function (arr, $form, options) {
                        self.resetForm();
                        layer.closeAll();
                    },
                    success: function (response) {
                        var data = response.data;
                        if (data.status) {
                            layer.msg('密码修改成功，请重新登录！', {type: 1, time: 2000}, function(){
                                $('.header .logout-form').submit();
                            });
                        } else {
                            layer.msg('密码修改失败，请重试！', {type: 1, time: 2000});
                        }
                    },
                    error: function () {
                        layer.msg('密码修改失败，请重试！', {type: 1, time: 2000});
                    },
                    complete: function () {
                    }
                });
                return false;
            },
            rules: {
                originalPassword: {
                    required: true,
                    rangelength: [6, 20],
                    regex: "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$",
                    isNotExist: "/personal-info/password/{0}/is-exist"
                },
                newPassword: {
                    required: true,
                    rangelength: [6, 20],
                    regex: "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$"
                },
                newPasswordConfirm: {
                    required: true,
                    rangelength: [6, 20],
                    regex: "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$",
                    equalTo: "input[name='newPassword']"
                }
            },
            messages: {
                originalPassword: {
                    required: "请输入原密码",
                    rangelength: "长度6~20位",
                    regex: "字母和数字组合",
                    isNotExist: "原密码不正确"
                },
                newPassword: {
                    required: "请输入新密码",
                    rangelength: "长度6~20位",
                    regex: "字母和数字组合"
                },
                newPasswordConfirm: {
                    required: "请输入新密码",
                    rangelength: "长度6~20位",
                    regex: "字母和数字组合",
                    equalTo: "密码不一致"
                }
            }
        });
    });
});
