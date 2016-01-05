require(['jquery', 'template', 'csrf','bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'Validform', 'Validform_Datatype'], function($) {
    $(function() {
        var $selectDom = $('.selectpicker'), //select表单
            $dateStart = $('#startTime'), //开始时间
            $dateEnd = $('#endTime'), //结束时间
            $errorDom = $('.form-error'), //错误提示节点
            $submitBtn = $('#btnSave'), //提交按钮
            $couponForm = $('.form-list'),
            boolFlag = false, //校验布尔变量值
            currentErrorObj = null;


        //渲染select表单
        $selectDom.selectpicker();



        //起始时间绑定插件
        $dateStart.datetimepicker({
            format: 'YYYY-MM-DD HH:mm'
        }).on('dp.change', function(e) {
            $dateEnd.data("DateTimePicker").minDate(e.date);
        });

        //结束时间绑定插件
        $dateEnd.datetimepicker({
            format: 'YYYY-MM-DD HH:mm'
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

        // 充值金额保留小数点后2位
        var rep = /^\d+$/;
        var rep_point = /^([0-9]+\.[0-9]{2})[0-9]*$/;
        var rep_point1 = /^[0-9]+\.[0-9]$/;
        $('.coupon-number').blur(function () {
            var _this = $(this),
                text = _this.val(),
                num = text.replace(rep_point, "$1");
            if (rep.test(text)) {
                _this.val(text + '.00').removeClass('Validform_error');
            } else if (rep_point.test(text)) {
                _this.val(num).removeClass('Validform_error');
            } else if (rep_point1.test(text)) {
                _this.val(text + '0').removeClass('Validform_error');
            } else {
                _this.val('').addClass('Validform_error');
            }
        });

        var rep_point2 = /^[0-9]+\.[0-9]*$/;

        $('.give-number,.coupon-deadline').blur(function () {
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

        $('.couponType').change(function(){
            var couponType = this.value;
            if(couponType == "NEWBIE_COUPON"){
                $('.newbie-coupon').show();
                $('.invest-coupon').hide();
                $('.userGroup').addClass("invest-coupon-total_count");
                $('.give-number').removeAttr("invest-coupon-total_count");
                $('.give-number').removeAttr("readonly");
            }else{
                $('.give-number').addClass("invest-coupon-total_count");
                $('.give-number').attr("readonly",true);
                $('.userGroup').removeAttr("invest-coupon-total_count");
                $('.newbie-coupon').hide();
                $('.invest-coupon').show();
            }
        }).trigger('change');

        $('.userGroup').change(function(){
            var userGroup = this.value;
            var couponType = $('.couponType').val();
            if(couponType == "INVEST_COUPON"){
                $.get('/activity-manage/get/'+userGroup,function(data){
                    $('.give-number').val(data);
                })
            }

        });

    });
});