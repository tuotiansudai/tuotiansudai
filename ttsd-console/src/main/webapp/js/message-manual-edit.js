window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'underscore', 'bootstrap', 'Validform', 'Validform_Datatype', 'bootstrapSelect', 'bootstrapDatetimepicker', 'ueditor', 'jquery-ui', 'csrf'], function ($, _) {
    $(function () {
        var $selectDom = $('.selectpicker'); //select表单
        var $submitBtn = $('.message-save'); //提交按钮
        var $importBtn = $('.file-btn'); //导入用户按钮
        var $userGroup = $('input[name="userGroup"]');
        var importUsersFlag = $('#importUsersFlag');
        var pushSwitch = $('#push');
        var $validStartTime = $('#datepickerBegin');
        var $validEndTime = $('#datepickerEnd');
        //渲染select表单
        $selectDom.selectpicker();
        $validStartTime.datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
        $validEndTime.datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});

        pushSwitch.prop('checked') == true ? $('.push-check-item').show() : $('.push-check-item').hide();

        $userGroup.click(function () {
            var self = $(this);
            if (self.val() == 'IMPORT_USER') {
                $importBtn.removeClass('hidden');
            } else {
                $importBtn.addClass('hidden');
            }
        });

        //导入用户按钮
        $importBtn.change(function () {
            if ($('input[name="userGroup"]:checked').val() !== 'IMPORT_USER') {
                return false;
            }
            var file = $(this).find('input').get(0).files[0];
            var formData = new FormData();
            formData.append('file', file);
            $.ajax({
                url: '/message-manage/manual-message/import-users',
                type: 'POST',
                data: formData,
                dataType: 'JSON',
                contentType: false,
                processData: false
            }).done(function (data) {
                if (data.data.status) {
                    importUsersFlag.val(data.data.message);
                } else {
                    showErrorMessage(data.data.message);
                }
            });

        });

        var check = function () {
            if ($('input[name="id"]').val() === '' &&  $('input[name="userGroup"]:checked').val() === 'IMPORT_USER' && $('#importUsersFlag').val() === '') {
                showErrorMessage("请导入用户");
                return false;
            }

            if ($('.message-title').val().length <= 0 || $('.message-title').val().length > 40) {
                showErrorMessage("标题不能为空, 且不超过40字");
                return false;
            }

            if ($('input[name="channels"]:checked').length === 0) {
                showErrorMessage("请选择送达方式");
                return false;
            }

            var messageContent = getContentTxt();
            if (messageContent.length <= 0) {
                showErrorMessage("文本内容不能为空");
                return false;
            }

            if ($('#datepickerBegin').find('.form-control').val() == '') {
                showErrorMessage("消息发送开始时间不能为空");
                return false;
            }

            if ($('#datepickerEnd').find('.form-control').val() == '') {
                showErrorMessage("消息发送结束时间不能为空");
                return false;
            }

            return true;
        };

        //提交表单
        $submitBtn.on('click', function (event) {
            event.preventDefault();
            var channels = [];
            $('#messageChannel').find('.channel:checked').each(function (index, el) {
                channels.push($(this).val());
            });

            if (check()) {
                $('#confirm-modal').modal('show');
            }

            return false;

        });

        $('#confirm-modal').find('.btn-submit').click(function () {
            var channels = [];
            $('#messageChannel').find('.channel:checked').each(function (index, el) {
                channels.push($(this).val());
            });

            var dataDto = {
                "id": $('.message-id').val(),
                "title": $('.message-title').val(),
                "template": getContent(),
                "templateTxt": getContentTxt(),
                "userGroup": $('input[name="userGroup"]:checked').val(),
                "channels": _.map($('input[name="channels"]:checked'), function(item) {
                    return $(item).val();
                }),
                "importUsersFlag": $('#importUsersFlag').val(),
                "messageCategory": $('.messageCategory').val(),
                "webUrl": $('.message-web-url').val().length == 0 ? null : $('.message-web-url').val(),
                "appUrl": $('.message-app-url').val(),
                "validStartTime": $('#datepickerBegin').find('.form-control').val(),
                "validEndTime": $('#datepickerEnd').find('.form-control').val()
            };

            if ($('#push').prop('checked')) {
                dataDto.push = {
                    "id": "",
                    "pushType": $('.message-pushType').val(),
                    "pushSource": $('.message-pushSource').val(),
                    "content": $('.message-title').val()
                }
            }

            $.ajax({
                url: "/message-manage/manual-message",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(dataDto),
                contentType: 'application/json; charset=UTF-8'
            }).done(function (res) {
                if (res.data.status) {
                    location.href = "/message-manage/manual-message-list";
                } else {
                    alert("站内信创建失败!");
                }
            });
        });

        pushSwitch.click(function () {
            if ($(this).prop('checked') == true) {
                $('.push-check-item').show();
            } else {
                $('.push-check-item').hide();
            }
        });

        var showErrorMessage = function (message) {
            var errorModal = $('#error-modal');
            errorModal.find('.modal-body H5').html(message);
            errorModal.modal('show');
        }
    });
});