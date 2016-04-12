require(['jquery', 'bootstrap','bootstrapDatetimepicker'], function ($) {
    $('#datepickerBegin,#datepickerEnd').datetimepicker({
        format: 'YYYY-MM-DD'
    });
    $("#datepickerBegin").on("dp.change", function (e) {
        $('#datepickerEnd').data("DateTimePicker").minDate(e.date);
    });
    $("#datepickerEnd").on("dp.change", function (e) {
        $('#datepickerBegin').data("DateTimePicker").maxDate(e.date);
    });

    var tooltip = $('.add-tooltip');
    if (tooltip.length){
        tooltip.tooltip();
    }

    $(".search").on("click",function(){
        var status = $(".status").val();
        if ($(".loanId").val() != "" && !$(".loanId").val().match("^[0-9]*$")) {
            $(".loanId").val('0');
        }
        var formData=$("#formLoanList").serialize(),
            allData=formData+'&index=1&pageSize=10';
        window.location.href = "/project-manage/loan-list?"+allData;

    });

});

