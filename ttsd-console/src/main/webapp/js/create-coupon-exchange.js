require(['jquery', 'template', 'csrf','bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'Validform', 'Validform_Datatype'], function($) {
    $(function() {
        var $selectDom = $('.selectpicker'),
            $dateStart = $('#startTime'),
            $dateEnd = $('#endTime'),
            $errorDom = $('.form-error'),
            $submitBtn = $('#btnSave'),
            boolFlag = false, //校验布尔变量值
            $couponForm = $('.form-list');

        $selectDom.selectpicker();

        $dateStart.datetimepicker({
            format: 'YYYY-MM-DD'
        }).on('dp.change', function(e) {
            $dateEnd.data("DateTimePicker").minDate(e.date);
        });

        $dateEnd.datetimepicker({
            format: 'YYYY-MM-DD'
        });

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

        $('.give-number,.exchange-point').blur(function () {
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
                var couponType = $('.couponType', curform).val();
                if(couponType == 'INTEREST_COUPON' || couponType == '加息券'){
                    var rep_point1 = /^(\d+\.\d{1,1}|\d+)$/;
                    var couponRate = parseFloat($('.coupon-rate').val());
                    if (couponRate <= 0) {
                        showErrorMessage('加息券利率需要大于0', $('.coupon-rate', curform));
                        return false;
                    }
                    if (!rep_point1.test(couponRate)) {
                        showErrorMessage('加息券利息需要大于等于0.1且只能保留1位小数', $('.coupon-rate', curform));
                        return false;
                    }
                }

                var fivenumber = parseInt($('.give-number', curform).val());
                if (fivenumber <= 0) {
                    showErrorMessage('最小为1', $('.give-number', curform));
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