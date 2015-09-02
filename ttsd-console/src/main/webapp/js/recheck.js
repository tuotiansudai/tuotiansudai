require(['jquery', 'bootstrap','template', 'fileinput', 'fileinput_locale_zh', 'jquery-ui', 'Validform_Datatype', 'Validform_v5.3.2', 'bootstrapDatetimepicker', 'moment-with-locales', 'bootstrapSelect'], function ($) {
    $(function (template) {
        $('#datetimepicker').datetimepicker();
        $('.selectpicker').selectpicker({
            style: 'btn-default',
            size: 4
        });

    })
})