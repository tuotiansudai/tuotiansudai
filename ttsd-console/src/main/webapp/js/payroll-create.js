require(['jquery', 'layerWrapper', 'template', 'csrf', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'Validform', 'Validform_Datatype'], function ($, layer) {
    $(function () {
        var $errorDom = $('.form-error'), //错误提示节点
            $payrollForm = $('.form-payroll'),
            boolFlag = false, //校验布尔变量值
            currentErrorObj = null;

        /**
         * @msg  {[string]} //文字信息
         * @obj  {[object]} //传入的dom节点
         * @return {[html]} //生成html
         */
        function showErrorMessage(msg, obj) {
            currentErrorObj = obj;
            var html = '';
            html += '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">';
            html += '<button type="button" class="close" data-dismiss="alert" aria-label="Close">';
            html += '<span aria-hidden="true">&times;</span>';
            html += '</button>';
            html += '<span class="txt">创建失败：' + msg + '</span>';
            html += '</div>';
            $errorDom.append(html);
        }

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
                    console.log(data.status)
                    if (data.status) {
                        console.log("totalAmount=" + data.totalAmount);
                        console.log("headCount=" + data.headCount);
                        console.log("payrolldetaillist=" + data.payrollDetailDtoList);
                        layer.msg('用户发放名单导入成功！');
                        var formData = new FormData();
                        var title = $('.payroll-title').val();
                        formData.append("title",title);
                        formData.append("totalAmount", data.totalAmount);
                        formData.append("headCount",data.headCount);
                        console.log("formDate=" + formData);
                        $.ajax({
                            url: '/payroll-manage/create',
                            type: 'POST',
                            data: formData,
                            dataType: 'JSON',
                            contentType: false,
                            processData: false
                        }).done(function (data) {
                            layer.msg('用户发放名单导入成功！');
                            window.location.href = "/payroll-manage/list";
                        }).fail(function(data){
                            console.log("fail")
                            window.location.href = "/payroll-manage/create";
                        });

                    }
                    else {
                        layer.open({
                            title: '失败',
                            content: data.message
                        });
                    }


                });
        });
    });
});