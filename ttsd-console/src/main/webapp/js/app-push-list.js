require(['jquery','bootstrap', 'bootstrapDatetimepicker','csrf'], function($) {
    $(function() {
        $('.send-push-link').click(function(){
            if (!confirm("确认要推送吗?")) {
                return false;
            }
        });
    });
});