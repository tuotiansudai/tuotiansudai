/**
 * Created by CBJ on 2015/11/2.
 */
require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatetimepicker'], function ($) {
    $(function () {

        //$('#RewardDate .date,#investDate .date').datetimepicker({
        //    format: 'YYYY-MM-DD',
        //    autoclose: true
        //});

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
        $("#investDateEnd").on("dp.change", function (e) {
            $('#RewardDateEnd').data("DateTimePicker").maxDate(e.date);
        });

        $('.selectpicker').selectpicker();
    });
})