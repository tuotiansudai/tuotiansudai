require(['jquery', 'jquery-ui',
    'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect',
    'moment'], function ($) {
    $(function () {
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

        $('.selectpicker').selectpicker({
            style: 'btn-default',
            size: 8
        });
        var autoValue = '';
        $("#loginName").autocomplete({
            source: function (query, process) {
                $.get('/user/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });
        $("#loginName").blur(function () {
            for(var i = 0; i< autoValue.length; i++){
                if($(this).val()== autoValue[i]){
                    $(this).removeClass('Validform_error');
                    return false;
                }else{
                    $(this).addClass('Validform_error');
                }

            }

        });
        $("#btnRepayReset").click(function(){

            location.href="/project-manage/loan-repay";
        });



        function pageinationView(e){
            var index = $(e.target).attr("pageIndex");

            if ($('#loanId').val() != "" && !$('#loanId').val().match("^[0-9]*$")) {
                $('#loanId').val('0');
            }

            var loanId =  $('#loanId').val();
            var loginName =  $('#loginName').val();
            var startTime =  $('#startTime').val();
            var endTime =  $('#endTime').val();
            var repayStatus = $('#repayStatus').val()
            var pageSize = 10;

            location.href="/project-manage/loan-repay?loanId="+loanId
            +"&loginName="+loginName
            +"&startTime="+startTime
            +"&endTime="+endTime
            +"&repayStatus="+repayStatus
            +"&index="+index
            +"&pageSize="+pageSize;
        }
        $('#btnRepayQuery').click(pageinationView);
        $('.Previous').click(pageinationView);
        $('.Next').click(pageinationView);


    });
});
