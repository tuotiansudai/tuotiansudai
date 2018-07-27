require(['jquery', 'bootstrapSelect', 'jquery-ui', 'bootstrapDatetimepicker'], function ($) {

    $('#tradeType').selectpicker();
    $('#orderDate').datetimepicker({
        format: 'YYYY-MM-DD',
        maxDate: 'now',
        ignoreReadonly: true
    });

    $('.btnSearch').click(function(){
        var type = $('select[name="type"]').val();
        var role=$("input[name='role']:checked").val();
        if (type === 'user') {
            location.href = "/finance-manage/real-time-status/user?loginNameOrMobile=" + $('input[name="loginNameOrMobile"]').val()+"&role="+role;
        }

        if (type === 'loan') {
            location.href = "/finance-manage/real-time-status/loan?loanId=" + $('input[name="loanId"]').val()+"&role="+role;
        }

        if (type === 'trade') {
            location.href = "/finance-manage/real-time-status/trade?orderNo={orderNo}&orderDate={orderDate}&queryTradeType={queryTradeType}&role={role}"
                .replace("{orderNo}", $('input[name="orderNo"]').val())
                .replace("{orderDate}", $('input[name="orderDate"]').val())
                .replace("{queryTradeType}", $('select[name="queryTradeType"]').val())
                .replace("{role}", role);
        }
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

        if (selected === 'trade') {
            $("div.transfer").show();
        }
    });
});