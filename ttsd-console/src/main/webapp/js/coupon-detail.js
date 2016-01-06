require(['jquery','bootstrap', 'bootstrapSelect','csrf'], function($) {
    $(function () {
        $('.selectpicker').selectpicker();

        $('.selectpicker').change(function(){
            var link = $(this).attr('data-link');
            if ($(this).val() != '') {
                link = link + '?isUsed=' + $(this).val();
            }
            window.location.href = link;
        });
    });

});