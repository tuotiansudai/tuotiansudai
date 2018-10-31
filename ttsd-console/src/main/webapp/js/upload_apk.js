window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'bootstrap','jquery-ui','csrf'], function ($) {
    $(function () {
        //提交表单
        $('.version-update').on('click', function (event) {
            var $this = $(this);
            if (confirm('确定已经上传了apk包?')) {
                $this.attr("disabled", true);
                $.ajax({
                    url: '/app/upload/version-json',
                    type: 'POST',
                    data: new FormData($('#upload-version-form')[0]),
                    dataType: 'JSON',
                    contentType: false,
                    processData: false
                })
                    .done(function (data) {
                        if (data.status){
                            alert("上传version.json成功");
                        }else {
                            alert(data.message);
                        }
                        $this.attr("disabled", false);
                    })
                    .fail(function (message) {
                        alert("上传version.json失败");
                        $this.attr("disabled", false);
                    });
            }
        });

        $('.version-view').on('click', function(event){
            $.ajax({
                url: '/app/look/version-json',
                type: 'GET'
            })
                .done(function (data) {
                    if (data.status){
                        alert(data.message);
                    }else {
                        alert("查看失败");
                    }
                })
                .fail(function (message) {
                    alert("查看失败");
                });
        });
    });
});