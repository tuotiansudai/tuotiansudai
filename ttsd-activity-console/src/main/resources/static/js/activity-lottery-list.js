require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

    $("select[name='prizeType']").change(function (){
        if($("select[name='prizeType']").val() == 'NATIONAL_PRIZE'){
            $("select[name='selectNational']").show();
            $("select[name='selectPrize']").hide();
        }else{
            $("select[name='selectNational']").hide();
            $("select[name='selectPrize']").show();
        }
    });

});
