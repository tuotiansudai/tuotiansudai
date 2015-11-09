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
        var loanId;
        if ($(".loanId").val() == "") {
            loanId = 0;
        } else {
            loanId = $(".loanId").val();
        }
        var formData=$("#formLoanList").serialize(),
            allData=formData+'&currentPageNo=1&pageSize=10';
        window.location.href = "/loanList/console?"+allData;

    });

});

