require(['jquery', 'bootstrap', 'Validform', 'Validform_Datatype', 'bootstrapSelect', 'bootstrapDatetimepicker', 'jquery-ui', 'csrf'], function ($) {
    $(function () {
        var $selectDom = $('.selectpicker'); //select表单
        var $submitBtn = $('.submit-btn'); //提交按钮
        var $importBtn = $('.file-btn'); //导入用户按钮
        //渲染select表单
        $selectDom.selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

        //导入用户按钮
        $importBtn.click(function () {
            if ("IMPORT_USER" == $('.jq-userGroup').val()) {
                var file = $(this).find('input').get(0).files[0];
                var formData = new FormData();
                var importUsersId = $('.importUsersId').val();
                formData.append('file', file);
                $.ajax({
                    url: '/membership-manage/give/import-users/' + importUsersId,
                    type: 'POST',
                    data: formData,
                    dataType: 'JSON',
                    contentType: false,
                    processData: false
                }).done(function (data) {
                    if (data.data.status) {
                        alert(data.data.message);
                        $('#importUsersId').val(data.data.message);
                    } else {
                        alert(data.data.message);
                    }
                });
            } else {
                alert("只有\"导入用户\"才能导入");
                return;
            }
        });

        function checkFormat() {
            return true;
        }

        //提交表单
        $submitBtn.on('click', function () {
            if (!confirm("确认要执行此操作吗?")) {
                return;
            }
            if (checkFormat()) {
                var membershipGiveId = document.getElementById('membershipGiveId').value;
                var userGroup = $('.jq-userGroup').val();
                var membershipLevel = $('.jq-membershipLevel').val();
                var startTime = $('.jq-start-date').val();
                var endTime = $('.jq-end-date').val();
                var validPeriod = $('.jq-valid-period').val();
                var smsNotify = $('.jq-smsNotify').get(0).checked;

                var importUsersId = document.getElementById('importUsersId').value;

                var url = "/membership-manage/give/edit?importUsersId=" + importUsersId;
                if ("IMPORT_USER" == userGroup) {
                    var dataDto = {
                        "id": membershipGiveId,
                        "membershipLevel": membershipLevel,
                        "validPeriod": validPeriod,
                        "receiveStartTime": null,
                        "receiveEndTime": null,
                        "userGroup": userGroup,
                        "smsNotify": smsNotify,
                        "valid": false
                    };
                } else if ("NEW_REGISTERED_USER" == userGroup) {
                    var dataDto = {
                        "id": membershipGiveId,
                        "membershipLevel": membershipLevel,
                        "validPeriod": validPeriod,
                        "receiveStartTime": startTime,
                        "receiveEndTime": endTime,
                        "userGroup": userGroup,
                        "smsNotify": smsNotify,
                        "valid": false
                    };
                }
                var dataForm = JSON.stringify(dataDto);
                $.ajax({
                    url: url,
                    type: 'POST',
                    dataType: 'json',
                    data: dataForm,
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (res) {
                    if (res.data.status) {
                        location.href = '/membership-manage/give/list';
                    } else {
                        alert(res.data.message);
                    }
                });
            }
        });
    });
});