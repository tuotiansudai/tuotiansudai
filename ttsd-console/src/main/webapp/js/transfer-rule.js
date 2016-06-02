require(['jquery', 'csrf', 'jquery-ui', 'bootstrap'], function ($) {
    $('.btn-submit').click(function () {
        $('.web-error-message').hide();
        $('#confirm-modal').modal('show');
        return false;
    });

    $('#confirm-modal').find('.btn-submit').click(function () {
        $("form").submit();
    });

    $('.btn-refuse').click(function () {
        $.ajax({
            url: '/refuse?taskId=TRANSFER_RULE',
            type: 'GET'
        })
        .done(function() {
            window.location="/transfer-manage/transfer-rule";
        })
    });

    $('input[type="reset"]').click(function () {
        location.reload();
        return false;
    });
});