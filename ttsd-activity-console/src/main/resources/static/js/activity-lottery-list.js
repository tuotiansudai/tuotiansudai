require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

    $("select[name='prizeType']").change(function (){
        if($("select[name='prizeType']").val() == 'NATIONAL_PRIZE'){
            $("#nationalDiv").show();
            $("#autumnPrizeDiv").hide();
        }else{
            $("select[name='nationalDiv']").hide();
            $("select[name='autumnPrizeDiv']").show();
        }
    });

});
