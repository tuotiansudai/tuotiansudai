require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'csrf'], function ($) {

    $(function(){
        $('.selectpicker').selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
        $('form button[type="reset"]').click(function () {
            location.href = "invests";
        });
        $('form button[type="submit"]').click(function (event) {
            var queryParams = '';
            if ($('form input[name="loanId"]').val() != "" && !$('form input[name="loanId"]').val().match("^[0-9]*$")) {
                $('form input[name="loanId"]').val('0');
            }
            if ($('form input[name="loanId"]').val()) {
                queryParams += "loanId=" + $('form input[name="loanId"]').val() + "&";
            }
            if ($('form input[name="mobile"]').val().length > 0) {
                queryParams += "mobile=" + $('form input[name="mobile"]').val() + "&";
            }
            if ($('form input[name="startTime"]').val()) {
                queryParams += "startTime=" + $('form input[name="startTime"]').val() + "&";
            }
            if ($('form input[name="endTime"]').val()) {
                queryParams += "endTime=" + $('form input[name="endTime"]').val() + "&";
            }
            if ($('form select[name="investStatus"]').val()) {
                queryParams += "investStatus=" + $('form select[name="investStatus"]').val() + "&";
            }
            if ($('form select[name="source"]').val()) {
                queryParams += "source=" + $('form select[name="source"]').val() + "&";
            }
            if ($('form select[name="channel"]').val()) {
                queryParams += "channel=" + $('form select[name="channel"]').val() + "&";
            }
            if ($('form select[name="role"]').val()) {
                queryParams += "role=" + $('form select[name="role"]').val() + "&";
            }
            if ($('form select[name="usedPreferenceType"].val()')) {
                queryParams += "usedPreferenceType=" + $('form select[name="usedPreferenceType"]').val() + "&";
            }
            location.href = "?" + queryParams;
            return false;
        });

        //自动完成提示
        var autoValue = '';
        $("#tags").autocomplete({
            source: function (query, process) {
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });
        $("#tags").blur(function () {
            for (var i = 0; i < autoValue.length; i++) {
                if ($(this).val() == autoValue[i]) {
                    $(this).removeClass('Validform_error');
                    return false;
                } else {
                    $(this).addClass('Validform_error');
                }

            }

        });

        $('.down-load').on('click',function () {
            location.href = "/export/invests?" + $('form').serialize();
        });

        $('.updateTransferStatus').on('click', function () {
            var investId = $(this).data('investid');

            if (confirm($(this).data('warmprompt'))) {
                $.ajax({
                    url: '/finance-manage/update/invest/'+ investId +'/transfer-status',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                })
                    .done(function (res) {
                        if (res.status){
                            location.href='/finance-manage/invests';
                        }else {
                            alert("修改失败");
                        }
                    })
                    .fail(function() {
                        alert("修改失败");
                    })
            }
        });
    })

});
