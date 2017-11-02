require(['jquery', 'layerWrapper', 'csrf', 'bootstrap', 'jquery-ui','Validform'], function ($, layer) {
    $(function () {
        var $errorDom = $('.form-error'), //错误提示节点
            $payrollForm = $('.form-payroll'),
            boolFlag = false, //校验布尔变量值
            $submitBtn = $('#btnSave'),
            currentErrorObj = null;

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
                url: '/finance-manage/payroll-manage/import-csv',
                type: 'POST',
                data: formData,
                dataType: 'JSON',
                contentType: false,
                processData: false
            })
                .done(function (data) {
                    if (data.status) {
                        $('.payroll-totalAmount').val(data.totalAmount);
                        $('.payroll-headCount').val(data.headCount);
                        $('.payroll-uuid').val(data.uuid);
                        var id =  $('.payroll-id').val();
                        if(id != ""){
                            $('#old-payroll-details').hide();
                        }
                        var table = "<table class='table table-bordered'><tr><td>用户姓名</td><td>用户手机号</td><td>发放金额（元）</td></tr>";
                        var payrolldetailData = data.payrollDetailModelList;
                        $.each(payrolldetailData, function (i, n) {
                            table += "<tr><td>" + n.userName + "</td><td>" + n.mobile + "</td><td>" + (n.amount/100).toFixed(2) + "</td></tr>";
                        });
                        table += "</table>"
                        $('#payroll-details').append(table);
                        boolFlag = true;
                    }
                    else {
                        layer.open({
                            title: '提示',
                            content: data.message,
                            btn: ['确定']

                        });
                        boolFlag = false;
                    }

                });
        });


        $payrollForm.Validform({
            btnSubmit: '#btnSave',
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            ignoreHidden:true,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function(curform) {
                var title = $('.payroll-title', curform).val();
                var filename = $('#file-in', curform).val();
                if (title == '') {
                    showErrorMessage('标题不能为空', $('.payroll-title', curform));
                    return false;
                }
                if($('.payroll-id',curform).val() == undefined && filename == ''){
                    showErrorMessage('请上传发放名单', $('#file-in', curform));
                    return false;
                }
                if(!boolFlag){
                    return false;
                }
            },
            callback: function(form) {
                boolFlag = true;
                return false;
            }
        });

        $('body').on('click', '.form-error', function () {
            $submitBtn.removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });

        $submitBtn.on('click', function(event) {
            event.preventDefault();
            var $self = $(this);
            if (boolFlag) {
                $self.attr('disabled', 'disabled');
                $payrollForm[0].submit();
            }
        });



    });
});