require(['jquery', 'csrf', 'jquery-ui', 'bootstrapSelect', 'bootstrap', 'layer', 'layer-extend', 'layerWrapper', 'bootstrapDatetimepicker'], function ($) {
    $('#pointBusinessType').selectpicker();
    $('#channel').selectpicker();
    $('#startTime , #endTime').datetimepicker({format: 'YYYY-MM-DD'});
    $('.point-consume').click(function (e) {
        e.preventDefault();
        location.href = "/export/point-consume?" + $(".query-build").serialize();
    });
});