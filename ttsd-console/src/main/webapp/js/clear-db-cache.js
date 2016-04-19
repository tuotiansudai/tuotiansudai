require(['jquery', 'bootstrap','jquery-ui','csrf'], function ($) {
    $(function () {
        $('.btn').click(function(event) {
            if(!confirm("确定要清除数据库缓存吗？")) {
                return false;
            }
            $.ajax({
                url: '/security-log/clear-db-cache',
                type: 'POST',
                dataType: 'json'
            })
            .done(function(res) {
                if (res.success) {
                    alert("数据库缓存已经清除。");
                }
                console.log("success");
            })
            .fail(function() {
                console.log("error");
            })
            .always(function() {
                console.log("complete");
            });
        });
    });
})