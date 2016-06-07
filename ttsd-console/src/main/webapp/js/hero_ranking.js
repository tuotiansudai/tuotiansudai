require(['jquery', 'bootstrap','Validform','Validform_Datatype', 'bootstrapDatetimepicker','jquery-ui','csrf'], function ($) {
    $(function() {

        $('#datepicker').datetimepicker({
            format: 'YYYY-MM-DD'
        });

        $('#datepicker').on('dp.change', function(){
            location.href = '/activity-manage/hero-ranking?tradingTime=' + $('#tradingTime').val();
        });

        $('.invest').on('click', function(){
            if (!$(this).hasClass('active')) {
                $(this).addClass('active');
            }
            $('.referrer').removeClass('active');
            $('.invest-ranking').show();
            $('.referrer-ranking').hide();
        });

        $('.referrer').on('click', function(){
            if (!$(this).hasClass('active')) {
                $(this).addClass('active');
            }
            $('.invest').removeClass('active');
            $('.referrer-ranking').show();
            $('.invest-ranking').hide();
        });

    });
});