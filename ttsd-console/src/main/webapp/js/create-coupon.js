require(['jquery', 'template', 'bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'Validform', 'Validform_Datatype'], function($) {
    $(function() {
        var $selectDom = $('.selectpicker'), //select表单
            $dateStart = $('#startTime'), //开始时间
            $dateEnd = $('#endTime'), //结束时间
            $errorDom = $('.form-error'), //错误提示节点
            $submitBtn = $('#btnSave'), //提交按钮
            boolFlag = false, //校验布尔变量值
            currentErrorObj = null;


        //渲染select表单
        $selectDom.selectpicker();

        //起始时间绑定插件
        $dateStart.datetimepicker({
            format: 'YYYY-MM-DD'
        }).on('dp.change', function(e) {
            $dateEnd.data("DateTimePicker").minDate(e.date);
        });

        //结束时间绑定插件
        $dateEnd.datetimepicker({
            format: 'YYYY-MM-DD'
        });

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

        //表单校验初始化参数
        $(".form-list").Validform({
            btnSubmit: '#btnSave',
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function(curform) {
                var periods = parseInt($('.coupon-number', curform).val());
                $errorDom.html('');
                if (periods <= 0) {
                    showErrorMessage('投资体验券金额最小为1', $('.coupon-number', curform));
                    return false;
                }
            },
            callback: function(form) {
                boolFlag = true;
                return false;
            }
        });
        //关闭警告提示
        $('body').on('click', '.form-error', function () {
            $submitBtn.removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });

        //提交表单
        $submitBtn.on('click', function(event) {
            event.preventDefault();
            var $self = $(this);
            if (boolFlag) {
                $self.attr('disabled', 'disabled');
                $.ajax({
                    url: API_FORM,//请求地址
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8',
                    data: {
                        "name": $('.coupon-name').val(),
                        "amount": $('.coupon-number').val(),
                        "startTime": $('.coupon-start').val(),
                        "endTime": $('.coupon-end').val(),
                        "totalCount": $('.coupon-total').val()
                    }
                })
                .done(function(res) {
                    console.log("success");
                    if (res.data.status) {
                        boolFlag = true;
                        location.href = '/project-manage/loan-list';
                    } else {
                        boolFlag = false;
                        var msg = res.data.message || '服务端校验失败';
                        showErrorMessage(msg);
                    }
                })
                .fail(function() {
                    console.log("error");
                    $self.removeAttr('disabled');
                })
                .always(function() {
                    console.log("complete");
                });
            }
        });

    });
});