$(function () {
    $('#datepicker1').datepicker({
        format:'yyyy-mm-dd',
        autoclose:true
    });
    $('#datepicker2').datepicker({
        format:'yyyy-mm-dd',
        autoclose:true
    });
    var tooltip = $('.add-tooltip');
    if (tooltip.length){
        tooltip.tooltip();
    }
});

$(function () {
    $(".search").on("click",function(){
        var status = $(".status").val();
        var loanId;
        if ($(".loanId").val() == "") {
            loanId = 0;
        } else {
            loanId = $(".loanId").val();
        }
        var loanName = $(".loanName").val();
        var startTime = $('#datepicker1').find("input").val();
        var endTime = $('#datepicker2').find("input").val();
        window.location.href = "/loanList/console?status="+status+"&loanId="+loanId+"&startTime="+startTime+"&endTime="+endTime+"&currentPageNo=1&loanName="+loanName+"&pageSize=10";
    });
});