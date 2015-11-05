/**
 * Created by CBJ on 2015/11/2.
 */
require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatetimepicker'], function ($) {
    $(function () {

        $('#investDate .date').datetimepicker({
            format: 'YYYY-MM-DD'
        });
        $('.selectpicker').selectpicker();

        $('form button[type="reset"]').click(function () {
            window.location.href = "/userFunds";
        });

        $('.search').click(function() {
            var loginName = $('.jq-loginName').val();
            var startTime = $('.jq-startTime').val();
            var endTime = $('.jq-endTime').val();
            var operationType = $('.operationType').val();
            var businessType = $('.businessType').val();
            window.location.href = "/userFunds?loginName="+loginName+"&startTime="+startTime+"&endTime="+endTime+"&userBillOperationType="+operationType+"&userBillBusinessType="+businessType+"&currentPageNo=1&pageSize=10";
        });
    });
})