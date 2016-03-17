require(['jquery', 'template', 'csrf','bootstrap', 'jquery-ui', 'bootstrapSelect', 'moment', 'Validform', 'Validform_Datatype'], function($) {
    $(function() {
        var $selectDom = $('.selectpicker'),
            $errorDom = $('.form-error'),
            $submitBtn = $('#btnSave'),
            $couponForm = $('.form-list');

        $selectDom.selectpicker();

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

        var rep = /^\d+$/;
        var rep_point2 = /^[0-9]+\.[0-9]*$/;

        $('.give-number,.coupon-deadline,.coupon-number,.exchange-point').blur(function () {
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
                var periods = parseInt($('.coupon-number', curform).val());
                $errorDom.html('');
                if (periods <= 0) {
                    showErrorMessage('投资体验券金额最小为1', $('.coupon-number', curform));
                    return false;
                }
                var fivenumber = parseInt($('.give-number', curform).val());
                if (fivenumber <= 0) {
                    showErrorMessage('最小为1', $('.coupon-number', curform));
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
                $couponForm[0].submit();
            }
        });

        function iniForm(){
            $errorDom.html('');
            $('.coupon-number').val('');
            $('.coupon-deadline').val('');
            $('.give-number').val('');
            $('.invest-quota').val('');
        }

        $('.couponType').change(function(){
            var couponType = this.value;
            iniForm();
            if(couponType == "INVEST_COUPON"){
                $('.interest-coupon').hide();
                $('.invest-coupon').show();
            }else{
                $('.interest-coupon').show();
                $('.invest-coupon').hide();
            }
        });

    });
});