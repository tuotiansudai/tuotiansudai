require(['jquery', 'layerWrapper', 'csrf', 'bootstrap', 'jquery-ui'], function ($, layer) {
    $(function () {
        var $errorDom = $('.form-error'), //错误提示节点
            $payrollForm = $('.form-payroll'),
            boolFlag = false, //校验布尔变量值
            currentErrorObj = null;

        // /**
        //  * @msg  {[string]} //文字信息
        //  * @obj  {[object]} //传入的dom节点
        //  * @return {[html]} //生成html
        //  */
        // function showErrorMessage(msg, obj) {
        //     currentErrorObj = obj;
        //     var html = '';
        //     html += '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">';
        //     html += '<button type="button" class="close" data-dismiss="alert" aria-label="Close">';
        //     html += '<span aria-hidden="true">&times;</span>';
        //     html += '</button>';
        //     html += '<span class="txt">创建失败：' + msg + '</span>';
        //     html += '</div>';
        //     $errorDom.append(html);
        // }

        $('.file-btn').on('change', function () {
            var file = $(this).find('input').get(0).files[0];
            var formData = new FormData();
            formData.append('file', file);
            $.ajax({
                url: '/payroll-manage/import-csv',
                type: 'POST',
                data: formData,
                dataType: 'JSON',
                contentType: false,
                processData: false
            })
                .done(function (data) {
                    if (data.status) {
                        consoel.log(data.totalAmount)
                        consoel.log(data.totalAmount)
                    }
                    else {
                        layer.open({
                            title: '提示',
                            content: data.message,
                            btn:['确定'],
                        });
                        //window.location.reload();
                        $('.payroll-title').val(title);
                    }

                });
        });
    });
});