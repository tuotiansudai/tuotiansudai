require(['jquery', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker', 'jquery-ui'], function ($) {
    $(function () {

        $('#investDateBegin,#investDateEnd').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss'
        });
        $('#investDateEnd').datetimepicker({
            useCurrent: false
        });
        $("#investDateBegin").on("dp.change", function (e) {
            $('#investDateEnd').data("DateTimePicker").minDate(e.date);
        });
        $("#investDateEnd").on("dp.change", function (e) {
            $('#investDateBegin').data("DateTimePicker").maxDate(e.date);
        });
        $('.selectpicker').selectpicker();

        //自动完成提示
        var autoValue = '';
        $(".jq-loginName").autocomplete({
            source: function (query, process) {
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });

        $(".jq-loginName").blur(function () {
            for (var i = 0; i < autoValue.length; i++) {
                if ($(this).val() == autoValue[i]) {
                    $(this).removeClass('Validform_error');
                    return false;
                } else {
                    $(this).addClass('Validform_error');
                }
            }
        });

        $('form button[type="reset"]').click(function () {
            window.location.href = "/finance-manage/user-funds";
        });

        $('.search').click(function () {
            var mobile = $('.jq-loginName').val();
            var startTime = $('.jq-startTime').val();
            var endTime = $('.jq-endTime').val();
            var operationType = $('.operationType').val();
            var businessType = $('.businessType').val();
            //增加新的参数
            var role=$("input[name='role']:checked").val();
            var operationTypeUMP=$('.operationTypeUMP').val();
            var businessTypeUMP= $('.businessTypeUMP').val();
            window.location.href = "/finance-manage/user-funds?role="+role+"&businessTypeUMP="+businessTypeUMP+"&operationTypeUMP="+operationTypeUMP+"&mobile=" + mobile + "&startTime=" + startTime + "&endTime=" + endTime + "&userBillOperationType=" + operationType + "&userBillBusinessType=" + businessType + "&index=1&pageSize=10";
        });

        $('.down-load').click(function () {
            location.href = "/export/user-funds?" + $('form').serialize();
        });
        $("input[name='role']").click(function () {
            var self = $(this);
            if (self.val() == 'INVESTOR') {
                $('.operationTypeDiv').addClass('hidden');
                $('.businessTypeDiv').addClass('hidden');
                $('.operationTypeUMPDiv').removeClass('hidden');
                $('.businessTypeUMPDiv').removeClass('hidden');
            } else {
                $('.operationTypeDiv').removeClass('hidden');
                $('.businessTypeDiv').removeClass('hidden');
                $('.operationTypeUMPDiv').addClass('hidden');
                $('.businessTypeUMPDiv').addClass('hidden');
            }
        });
    });
})