/**
 * Created by CBJ on 2015/11/2.
 */
require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatetimepicker','jquery-ui'], function ($) {
    $(function () {

        $('#investDateBegin,#investDateEnd').datetimepicker({
            format: 'YYYY-MM-DD'
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

        $('.down-load').click(function(){
            var loginName = $('.jq-loginName').val();
            var startTime = $('.jq-startTime').val();
            var endTime = $('.jq-endTime').val();
            var operationType = $('.operationType').val();
            var businessType = $('.businessType').val();
            window.location.href = "/userFunds?loginName="+loginName+"&startTime="+startTime+"&endTime="+endTime+"&userBillOperationType="+operationType+"&userBillBusinessType="+businessType+"&export=csv";
        });
    });
})