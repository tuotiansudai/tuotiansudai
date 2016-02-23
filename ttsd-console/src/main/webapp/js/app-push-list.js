require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'csrf'], function ($) {
    $(function () {
        $('.pass').click(function (event) {
            event.preventDefault();
            if (!confirm("确定同意吗?")) {
                return false;
            }
            $.ajax({
                url: "/app-push-manage/manual-app-push/" + $(this).attr('data-pushId') + "/pass",
                type: 'GET',
                dataType: 'json',
                data: {}
            }).done(function (res) {
                if(res.data.status){
                    location.href='/app-push-manage/manual-app-push-list';
                }else{
                    var msg = res.data.message || '服务端校验失败';
                    alert(msg);
                }
            }).fail(function () {
                console.log("error");
            }).always(function () {
                console.log("complete");
            });
        });
    });

});