require(['jquery', 'bootstrapSelect', 'jquery-ui', 'bootstrapDatetimepicker'], function ($) {

    $('.btnSearch').click(function(){
        var type = $('select[name="type"]').val();

        var queryParam = "type=" + type;

        if (type === 'user') {
            queryParam += '&' + "loginName=" +  $('input[name="login-name"]').val();
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
            $('#merDate').datetimepicker({format: 'YYYY-MM-DD', maxDate: 'now'});
            $('#businessType').selectpicker();
            $("div.transfer").show();
        }
    });

    $('#loginName').autocomplete({
        minLength: 0,
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user-manage/user/' + query.term + '/search', function (respData) {
                return process(respData);
            });
        },
        change: function (event, ui) {
            if (!ui.item) {
                this.value = '';
            }
        }
    });
});