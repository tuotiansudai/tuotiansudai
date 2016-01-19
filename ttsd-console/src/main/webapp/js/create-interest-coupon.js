require(['jquery', 'template', 'csrf','bootstrap', 'jquery-ui', 'bootstrapSelect', 'moment', 'Validform', 'Validform_Datatype'], function($) {
    $(function() {
        var $selectDom = $('.selectpicker'), //select表单
            $errorDom = $('.form-error'), //错误提示节点
            $submitBtn = $('#btnSave'), //提交按钮
            $couponForm = $('.form-list'),
            boolFlag = false, //校验布尔变量值
            currentErrorObj = null;


        //渲染select表单
        $selectDom.selectpicker();

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

        // 充值金额保留小数点后2位
        var rep = /^\d+$/;
        var rep_point2 = /^[0-9]+\.[0-9]*$/;

        $('.give-number,.coupon-deadline,.coupon-number').blur(function () {
            var _this = $(this),
                text = _this.val(),
                num = text.replace(rep, "$1");
            if (rep.test(text)) {
                _this.val(text).removeClass('Validform_error');
            }else if (rep_point2.test(text)) {
                _this.val(parseInt(text)).removeClass('Validform_error');
            }else {
                _this.val('').addClass('Validform_error');
            }
        });
        //表单校验初始化参数
        $(".form-list").Validform({
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
                var couponRate = parseFloat($('.coupon-rate').val());
                if (couponRate <= 0) {
                    showErrorMessage('加息券利率需要大于0', $('.coupon-rate', curform));
                    return false;
                }
                var periods = parseInt($('.coupon-number', curform).val());
                $errorDom.html('');
                if (periods <= 0) {
                    showErrorMessage('金额最小为1', $('.coupon-number', curform));
                    return false;
                }
                var fivenumber = parseInt($('.give-number', curform).val());
                if (fivenumber <= 0) {
                    showErrorMessage('发放数量最小为1', $('.give-number', curform));
                    return false;
                }
                var len= $('input[name="productTypes"]').filter(function(key,option) {
                    return $(option).is(':checked');
                }).length;

                if(len <= 0){
                    showErrorMessage('请选择可投资标的', $('.productType', curform));
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
                $couponForm[0].submit();
            }
        });

        function iniForm(){
            $errorDom.html('');
            $('.coupon-number').val('');
            $('.coupon-deadline').val('');
            $('.give-number').val('');
            $('.invest-quota').val('');
            $('.smsAlert').eq(0).prop('checked',true)
            $('.productType').prop('checked',false).eq(0).prop('checked',true)
        }

        $('.userGroup').change(function(){
            var userGroup = this.value;
            var $fileBtn = $('.file-btn');
            if(userGroup != "IMPORT_USER_STAFF"){
                $fileBtn.hide();
                $.get('/activity-manage/coupon/user-group/'+userGroup+'/estimate',function(data){
                    $('.give-number').val(data);
                })
            } else {
                $fileBtn.show();
                $('.give-number').val('');
            }
        });

        $('.file-btn').on('change',function(){
            var file = $(this).find('input').get(0).files[0];
            var formData = new FormData();
            formData.append('file',file);
            $.ajax({
                url:'/activity-manage/import-excel',
                type:'POST',
                data:formData,
                dataType:'JSON',
                contentType: false,
                processData: false
            })
            .done(function(data){
                $('#import-file').val(data[0]);
                $('.give-number').val(data[1]);
            });
        });

    });
});