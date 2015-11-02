/**
 * Created by CBJ on 2015/11/2.
 */
require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatePicker'], function ($) {
    $(function () {
        $('#datetimepicker').datetimepicker({format: 'YYYY-MM-DD HH:mm:ss'});
        $('.selectpicker').selectpicker({
            style: 'btn-default',
            size: 4
        });
    })
})
