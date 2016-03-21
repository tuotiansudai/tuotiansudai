require(['jquery', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'csrf'], function ($) {
    $(function () {

        $('.selectpicker').selectpicker();

        $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD'});
        $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD'});
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
                if (res.data.status) {
                    location.href = '/app-push-manage/manual-app-push-list';
                } else {
                    var msg = res.data.message || '服务端校验失败';
                    alert(msg);
                }
            }).fail(function () {
                console.log("error");
            }).always(function () {
                console.log("complete");
            });
        });

        $('.jpushReport').click(function (event) {
            event.preventDefault();
            var $self=$(this);
            $.ajax({
                url: "/app-push-manage/manual-app-push/" + $(this).attr('data-pushId') + "/refreshReport",
                type: 'GET',
                dataType: 'json'
            }).done(function (res) {
                if (res.data.status) {
                    $self.find('.iosReport').text(res.data.iosReceived);
                    $self.find('.androidReport').text(res.data.androidReceived);
                } else {
                    var msg = res.data.message || '服务器校验失败';
                    alert(msg);
                }
            }).fail(function () {
                console.log("error");
            }).always(function () {
                console.log("complete");
            })
        });
    });

});