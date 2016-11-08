window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'bootstrap', 'Validform', 'Validform_Datatype', 'bootstrapSelect', 'ueditor', 'jquery-ui', 'csrf'], function ($) {
    $(function () {
        var $selectDom = $('.selectpicker'); //select表单
        var $submitBtn = $('.message-save'); //提交按钮
        var $importBtn = $('.file-btn'); //导入用户按钮
        var $messageForm = $('.message-form');
        var $userGroups = $('.userGroups');
        var $importGroups = $('.importGroups');
        var $allGroups = $('.allGroups');
        //渲染select表单
        $selectDom.selectpicker();

        //收件人checkbox控制
        $allGroups.click(function () {
            for (var i = 0; i < $userGroups.length; ++i) {
                $userGroups.get(i).checked = $allGroups.get(0).checked;
            }
            if ($allGroups.get(0).checked) {
                $importGroups.get(0).checked = false;
            }
        });
        $userGroups.click(function () {
            var allChecked = true;
            for (var i = 0; i < $userGroups.length; ++i) {
                if ($userGroups.get(i).checked) {
                    $importGroups.get(0).checked = false;
                } else {
                    allChecked = false;
                }
            }
            $allGroups.get(0).checked = allChecked;
        });
        $importGroups.click(function () {
            if ($importGroups.get(0).checked) {
                for (var i = 0; i < $userGroups.length; ++i) {
                    $userGroups.get(i).checked = false;
                }
                $allGroups.get(0).checked = false;
            }
        });

        //导入用户按钮
        $importBtn.change(function () {
            if ($importGroups.get(0).checked) {
                var file = $(this).find('input').get(0).files[0];
                var formData = new FormData();
                var importUsersId = $('.importUsersId').val();
                formData.append('file', file);
                $.ajax({
                    url: '/message-manage/manual-message/import-users/' + importUsersId,
                    type: 'POST',
                    data: formData,
                    dataType: 'JSON',
                    contentType: false,
                    processData: false
                }).done(function (data) {
                    if (data.data.status) {
                        alert(data.data.message);
                        $('.importUsersId').val(data.data.message);
                    } else {
                        alert(data.data.message);
                    }
                });
            } else {
                alert("必须勾选\"导入用户名单\", 否则无法成功导入");
            }
        });

        function check() {
            if ($('.message-title').val().length > 40) {
                alert("标题超过40字");
                return false;
            }
            var selectChannel = false;
            for (var i = 0; i < $('.channel').length; ++i) {
                if ($('.channel').get(i).checked) {
                    selectChannel = true;
                    break;
                }
            }
            if (!selectChannel) {
                alert("没有选择渠道");
                return false;
            }
            var selectReceiver = false;
            if ($allGroups.get(0).checked) {
                selectReceiver = true;
            }
            if ($importGroups.get(0).checked) {
                selectReceiver = true;
            }
            for (var j = 0; j < $userGroups.length; ++j) {
                if ($userGroups.get(j).checked) {
                    selectReceiver = true;
                    break;
                }
            }
            if (!selectReceiver) {
                alert("没有选择收件人");
                return false;
            }
            return true;
        }

        //提交表单
        $submitBtn.on('click', function (event) {
            event.preventDefault();
            var boolFlag = check();
            var channelArr = [], placeArr = [], userGroupArr = [];
            userGroupArr.push($('#userGroup').find('input[type="checkbox"]:checked').val());

            $('#messageChannel').find('.channel:checked').each(function (index, el) {
                channelArr.push($(this).val());
            });

            if ($('#areaGroup').find('input[type="checkbox"]:checked').length > 0) {
                $('#areaGroup').find('input[type="checkbox"]:checked').each(function (index, el) {
                    placeArr.push($(this).val());
                });
            } else {
                // $('#areaGroup').find('input[type="checkbox"]').each(function (index, el) {
                //     placeArr.push($(this).val());
                // });
            }

            if (boolFlag) {
                if (confirm("确认提交审核?")) {
                    var dataDto = {
                        "id": $('.message-id').val(),
                        "title": $('.message-title').val(),
                        "template": getContent(),
                        "templateTxt": getContentTxt(),
                        "userGroups": userGroupArr,
                        "channels": channelArr,
                        "manualMessageType": $('.manualMessageType').val(),
                        "webUrl": $('.message-web-url').val(),
                        "appUrl": $('.message-app-url').val(),
                        "jpush": $('#extra').prop('checked') ? true : false,
                        "pushType": $('.message-pushType').val(),
                        "pushSource": $('.message-pushSource').val(),
                        "pushDistricts": placeArr
                    };
                    var dataForm = JSON.stringify(dataDto);
                    var importUsersId = $('.importUsersId').val();
                    var url = "/message-manage/manual-message/create?importUsersId=" + importUsersId;
                    $.ajax({
                        url: url,
                        type: 'POST',
                        dataType: 'json',
                        data: dataForm,
                        contentType: 'application/json; charset=UTF-8'
                    }).done(function (res) {
                        if (res.status) {
                            location.href = "/message-manage/manual-message-list";
                        }
                    });
                }
            }
        });
        $('#extra').prop('checked') == true ? $('.check-item').show() : $('.check-item').hide();
        $('#extra').on('change', function (event) {
            event.preventDefault();
            if ($(this).prop('checked') == true) {
                $('.check-item').show();
            } else {
                $('.check-item').hide();
            }
        });

        $('.area-list .push_object_choose').on('change', function (event) {
            event.preventDefault();
            if ($(this).attr('value') == 'all') {
                $('#areaGroup .app-push-link').hide();
            } else if ($(this).attr('value') == 'district') {
                $('#areaGroup .app-push-link').show();
            }
        });
    });
});