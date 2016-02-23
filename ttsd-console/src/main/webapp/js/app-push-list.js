require(['jquery','bootstrap', 'bootstrapDatetimepicker','bootstrapSelect','csrf'], function($) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
    $(function() {
        //$('.send-push-link').click(function(event){
        //    event.preventDefault();
        //    if (!confirm("确认要推送吗?")) {
        //        return false;
        //    }
        //});

    });
});