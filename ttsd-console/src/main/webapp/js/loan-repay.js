require(['jquery', 'jquery-ui',
    'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect',
    'moment', 'moment-with-locales', 'Validform_v5.3.2','Validform_Datatype'], function ($) {
    $(function () {
        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
        var api_url = 'http://localhost:8082/loan/loaner';
        var autoValue = '';
        $("#loginName").autocomplete({

            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get(api_url+'/'+query.term, function (respData) {
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

            location.href="/loan-repay?loanId="
            +"&loginName="
            +"&beginTime="
            +"&endTime="
            +"&repayStatus="
            +"&index=1"
            +"&pageSize=10";
        });



        function pageinationView(e){
            var index = $(e.target).attr("pageIndex");
            var loanId =  $('#loanId').val();
            var loginName =  $('#loginName').val();
            var beginTime =  $('#beginTime').val();
            var endTime =  $('#endTime').val();
            var repayStatus = $('#repayStatus').val()
            var pageSize = 10;

            location.href="/loan-repay?loanId="+loanId
            +"&loginName="+loginName
            +"&beginTime="+beginTime
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
