/**
 * Created by CBJ on 2015/11/2.
 */
require(['jquery', 'bootstrap','bootstrapSelect','bootstrapDatePicker'], function ($) {
    $(function () {
        $('#investDate .date').datepicker({
            format:'yyyy-mm-dd',
            autoclose:true
        });
        $('.selectpicker').selectpicker();
    });
})