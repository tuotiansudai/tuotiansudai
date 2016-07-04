/**
 * Created by CBJ on 2015/11/2.
 */
require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatetimepicker', 'jquery-ui'], function ($) {
    $(function () {

        //$('#RewardDate .date,#investDate .date').datetimepicker({
        //    format: 'YYYY-MM-DD',
        //    autoclose: true
        //});
        $('form button[type="reset"]').click(function () {
            window.location.href = "/user-manage/referrer";
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

        //自动完成提示
        var autoValue = '';
        $(".referrerMobile, .investMobile").autocomplete({
            source: function (query, process) {
                $.get('/user-manage/mobile/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });
        $(".referrerMobile, .investMobile").blur(function () {
            for (var i = 0; i < autoValue.length; i++) {
                if ($(this).val() == autoValue[i]) {
                    $(this).removeClass('Validform_error');
                    return false;
                } else {
                    $(this).addClass('Validform_error');
                }
            }
        });

        $('.search').on('click',function(){
            //var formData=$('form').serialize()+'&currentPageNo=1&pageSize=10';
            //window.location.href='/referrerManage?'+formData;

            var referrerMobile = $('.referrerMobile').val();
            var investMobile = $('.investMobile').val();
            var investStartTime = $('.investStartTime').val();
            var investEndTime = $('.investEndTime').val();

            if ($('.level').val() != "" && !$('.level').val().match("^[0-9]*$")) {
                $('.level').val('');
            }

            var level = $('.level').val();
            var rewardStartTime = $('.rewardStartTime').val();
            var rewardEndTime = $('.rewardEndTime').val();
            var role = $('.role').val();
            var source = $('.source').val();
            window.location.href = '/user-manage/referrer?referrerMobile=' + referrerMobile + '&investMobile=' + investMobile + '&investStartTime=' + investStartTime + '&investEndTime=' + investEndTime + '&level=' + level + '&rewardStartTime=' + rewardStartTime + '&rewardEndTime=' + rewardEndTime + '&role=' + role + '&source=' + source + '&index=1&pageSize=10';
        });

        $('.down-load').on('click',function(){
            var referrerMobile = $('.referrerMobile').val();
            var investMobile = $('.investMobile').val();
            var investStartTime = $('.investStartTime').val();
            var investEndTime = $('.investEndTime').val();
            var level = $('.level').val();
            var rewardStartTime = $('.rewardStartTime').val();
            var rewardEndTime = $('.rewardEndTime').val();
            var role = $('.role').val();
            var source = $('.source').val();
            window.location.href = '/user-manage/referrer?referrerMobile=' + referrerMobile + '&investMobile=' + investMobile + '&investStartTime=' + investStartTime + '&investEndTime=' + investEndTime + '&level=' + level + '&rewardStartTime=' + rewardStartTime + '&rewardEndTime=' + rewardEndTime + '&role=' + role + '&source=' + source + '&export=csv';
        });
    });
})