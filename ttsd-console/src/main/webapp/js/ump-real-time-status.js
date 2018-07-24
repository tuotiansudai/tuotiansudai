require(['jquery', 'bootstrapSelect', 'jquery-ui', 'bootstrapDatetimepicker'], function ($) {

    $('#businessType').selectpicker();
    $('#merDate').datetimepicker({
        format: 'YYYY-MM-DD',
        maxDate: 'now',
        ignoreReadonly: true
    });

    $('.btnSearch').click(function(){
        var type = $('select[name="type"]').val();

        var queryParam = "type=" + type;

        if (type === 'user') {
            queryParam += '&' + "mobile=" +  $('input[name="login-name"]').val();
        }

        if (type === 'loan') {
            queryParam += '&' + "loanId=" +  $('input[name="loan-id"]').val();
        }

        if (type === 'transfer') {
            queryParam += '&' + "orderId=" +  $('input[name="order-id"]').val();
            queryParam += '&' + "merDate=" +  $('input[name="mer-date"]').val();
            queryParam += '&' + "businessType=" +  $('select[name="business-type"]').val();
        }

        location.href = "?" + queryParam;
        return false;
    });

    $('select[name="type"]').selectpicker().change(function() {
        var self = $(this);
        var selected = self.val();

        $("div.login-name").hide();
        $("div.loan").hide();
        $("div.transfer").hide();

        if (selected === 'user') {
            $("div.login-name").show();
        }

        if (selected === 'loan') {
            $("div.loan").show();
        }

        if (selected === 'transfer') {
            $("div.transfer").show();
        }
    });

});