/**
 * Created by CBJ on 2015/11/2.
 */
require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatetimepicker'], function ($) {
    $(function () {

        //$('#RewardDate .date,#investDate .date').datetimepicker({
        //    format: 'YYYY-MM-DD',
        //    autoclose: true
        //});
        $('form button[type="reset"]').click(function () {
            window.location.href = "/referrerManage";
        });
        $('#investDateBegin,#investDateEnd').datetimepicker({format: 'YYYY-MM-DD'});
        $('#investDateEnd').datetimepicker({
            useCurrent: false
        });
        $("#investDateBegin").on("dp.change", function (e) {
            $('#investDateEnd').data("DateTimePicker").minDate(e.date);
        });
        $("#investDateEnd").on("dp.change", function (e) {
            $('#investDateBegin').data("DateTimePicker").maxDate(e.date);
        });

        $('#RewardDateBegin,#RewardDateEnd').datetimepicker({format: 'YYYY-MM-DD'});
        $('#RewardDateEnd').datetimepicker({
            useCurrent: false
        });
        $("#RewardDateBegin").on("dp.change", function (e) {
            $('#RewardDateEnd').data("DateTimePicker").minDate(e.date);
        });
        $("#RewardDateEnd").on("dp.change", function (e) {
            $('#RewardDateBegin').data("DateTimePicker").maxDate(e.date);
        });

        $('.selectpicker').selectpicker();
        $('.search').on('click',function(){
            //var formData=$('form').serialize()+'&currentPageNo=1&pageSize=10';
            //window.location.href='/referrerManage?'+formData;

            var referrerLoginName = $('.referrerLoginName').val();
            var investLoginName = $('.investLoginName').val();
            var investStartTime = $('.investStartTime').val();
            var investEndTime = $('.investEndTime').val();
            var level = $('.level').val();
            var rewardStartTime = $('.rewardStartTime').val();
            var rewardEndTime = $('.rewardEndTime').val();
            var role = $('.role').val();

            window.location.href = '/referrerManage?referrerLoginName='+referrerLoginName+'&investLoginName='+investLoginName+'&investStartTime='+investStartTime+'&investEndTime='+investEndTime+'&level='+level+'&rewardStartTime='+rewardStartTime+'&rewardEndTime='+rewardEndTime+'&role='+role+'&currentPageNo=1&pageSize=10';
        });
    });
})