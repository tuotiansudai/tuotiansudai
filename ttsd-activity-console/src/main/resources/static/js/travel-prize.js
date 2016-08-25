require(['jquery', 'underscore', 'jquery-ui', 'csrf', 'bootstrap', 'bootstrapSelect', 'bootstrapDatetimepicker'], function ($, _) {
    $('#confirm-modal').find('.btn-submit').click(function () {
        $("form").submit();
    });

    $('.btn-submit').click(function () {
        $('#confirm-modal').modal('show');
        return false;
    });
});
