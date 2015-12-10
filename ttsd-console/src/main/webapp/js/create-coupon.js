require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment'], function($) {
    var $selectDom=$('.selectpicker'),//select表单
        $dateStart=$('#startTime'),//开始时间
        $dateEnd=$('#endTime');//结束时间;
    //渲染select表单
    $selectDom.selectpicker();

    //起始时间绑定插件
    $dateStart.datetimepicker({
        format: 'YYYY-MM-DD'
    }).on('dp.change', function(e) {
        $dateEnd.data("DateTimePicker").minDate(e.date);
    });
    
    //结束时间绑定插件
    $dateEnd.datetimepicker({
        format: 'YYYY-MM-DD'
    });

    //提交事件
    $('form button[type="submit"]').click(function(event) {
        var queryParams = '';
        if ($('form input[name="loanId"]').val() != "" && !$('form input[name="loanId"]').val().match("^[0-9]*$")) {
            $('form input[name="loanId"]').val('0');
        }
        if ($('form input[name="loanId"]').val()) {
            queryParams += "loanId=" + $('form input[name="loanId"]').val() + "&";
        }
        if ($('form input[name="loginName"]').val().length > 0) {
            queryParams += "loginName=" + $('form input[name="loginName"]').val() + "&";
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
        location.href = "?" + queryParams;
        return false;
    });

    

});