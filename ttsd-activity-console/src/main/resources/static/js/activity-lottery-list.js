require(['jquery', 'underscore', 'jquery-ui', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $('.selectpicker').selectpicker();

    $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
    $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});

});
