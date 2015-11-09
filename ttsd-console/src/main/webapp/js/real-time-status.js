require(['jquery', 'bootstrapSelect', 'jquery-ui'], function ($) {

    $('.btnSearch').click(function(){
        var type = $('select[name="type"]').val();

        var queryParam = "type=" + type;

        if (type === 'user') {
            queryParam += '&' + "loginName=" +  $('input[name="loginName"]').val();
        }

        if (type === 'loan') {
            queryParam += '&' + "loanId=" +  $('input[name="loanId"]').val();
        }

        location.href = "?" + queryParam;
        return false;
    });

    $('select[name="type"]').selectpicker().change(function() {
        var self = $(this)
        var selected = self.val();

        $("div.login-name").hide();
        $("div.loan").hide();

        if (selected === 'user') {
            $("div.login-name").show();
        }

        if (selected === 'loan') {
            $("div.loan").show();
        }
    });

    $('#loginName').autocomplete({
        minLength: 0,
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get('/user/' + query.term + '/search', function (respData) {
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