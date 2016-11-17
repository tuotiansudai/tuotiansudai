require(['jquery', 'csrf', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment'], function ($) {
    $(function () {

        $('.selectpicker').selectpicker({
            style: 'btn-default',
            size: 8
        });

        $('#create').click(function(event) {
            if(!confirm("确定要执行创建合同吗？")) {
                return false;
            }
            $.ajax({
                url: '/project-manage/generate-contract',
                type: 'POST',
                dataType: 'json',
                data:$('#contractForm').serialize()
            })
            .done(function(res) {
                if (res.success) {
                    alert("成功创建合同!");
                }else{
                    alert(res.data.message);
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
            if(!confirm("确定要执行查询合同吗？")) {
                return false;
            }
            $.ajax({
                url: '/project-manage/query-contract',
                type: 'POST',
                dataType: 'json',
                data:$('#contractForm').serialize()
            })
                .done(function(res) {
                    if (res.success) {
                        alert("查询合同成功!");
                    }else{
                        alert(res.data.message);
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