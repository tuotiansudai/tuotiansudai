require(['jquery', 'layer', 'jquery.validate', 'jquery.validate.extension', 'jquery.form'], function ($, layer) {
    $(function () {
        var $InfoBox = $('#personInfoBox'),
            $setEmail = $('.setEmail', $InfoBox),
            $setPass = $('.setPass', $InfoBox),
            $btnChangeEmail = $('#btnChangeEmail'),
            $changeEmailDOM = $('#changeEmailDOM'),
            $EmailForm = $('form', $changeEmailDOM),

            $btnChangePass = $('#btnChangePass'),
            $changePassDOM = $('#changePassDOM'),

            $passwordForm = $('form', $changePassDOM);

        $setEmail.on('click', function () {
            layer.open({
                type: 1,
                title: '绑定邮箱',
                area: ['500px', '230px'],
                shadeClose: true,
                content: $changeEmailDOM
            });
        });

        $EmailForm.validate({
            rules: {
                email: {
                    required: true,
                    email: true
                }
            },
            messages: {
                email: "请输入有效的邮箱地址"
            },
            submitHandler: function () {
                //var index = layer.open();
                //layer.close(index);
                layer.closeAll();
                layer.open({
                    type: 1,
                    title: '验证邮箱',
                    area: ['500px', '220px'],
                    shadeClose: true,
                    content: $('#CESuccess'),
                    btn: ['返回'],
                    yes: function (index, layero) {
                        layer.close(index);
                    }
                });

            }
        });

        $setPass.on('click', function () {
            layer.open({
                type: 1,
                title: '修改密码',
                area: ['500px', '300px'],
                shadeClose: true,
                content: $changePassDOM,
                cancel: function () {
                    $passwordForm.validate().resetForm();
                }
            });
        });

        $passwordForm.validate({
            focusCleanup: true,
            focusInvalid: false,
            errorClass: 'fa fa-times-circle error',
            validClass: 'fa fa-check-circle valid',
            onkeyup: function (element, event) {
                var excludedKeys = [16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225];

                if ((event.which !== 9 || this.elementValue(element) !== "") && $.inArray(event.keyCode, excludedKeys) === -1) {
                    this.element(element);
                }
            },
            onfocusout: function (element) {
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
                    },
                    success: function (response) {
                        var data = response.data;
                        if (data.status) {

                        }
                    },
                    error: function () {
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
                    regex: '^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$',
                    isNotExist: "/personal-info/password/{0}/is-exist"
                },
                newPassword: {
                    required: true,
                    rangelength: [6, 20],
                    regex: '^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$'
                },
                newPasswordConfirm: {
                    required: true,
                    rangelength: [6, 20],
                    regex: '^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$',
                    equalTo: "input[name='newPassword']"
                }
            },
            messages: {
                originalPassword: {
                    required: "请输入原密码",
                    regex: "原密码不正确",
                    rangelength: "原密码不正确",
                    isNotExist: "原密码不正确"
                },
                newPassword: {
                    required: "请输入新密码",
                    regex: "只能字母和数字组合",
                    rangelength: "长度6至20位"
                },
                newPasswordConfirm: {
                    required: "请输入新密码",
                    regex: "只能字母和数字组合",
                    rangelength: "长度6至20位",
                    equalTo: "密码不一致"
                }
            }
        });
    });
});
