require(['jquery', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment'], function ($) {
    $(function () {

        $('.selectpicker').selectpicker({
            style: 'btn-default',
            size: 8
        });

        $('#create').click(function(event) {
            if(!confirm("确定要创建合同吗？")) {
                return false;
            }
            $.ajax({
                url: '/project-manage/generate-contract',
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

        $('#find').click(function(event) {
            if(!confirm("确定要创建合同吗？")) {
                return false;
            }
            $.ajax({
                url: '/project-manage/generate-contract',
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