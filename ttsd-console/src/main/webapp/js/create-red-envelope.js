require(['jquery', 'layerWrapper', 'template', 'csrf','bootstrap', 'bootstrapDatetimepicker', 'jquery-ui', 'bootstrapSelect', 'moment', 'Validform', 'Validform_Datatype'], function($, layer) {
    $(function() {
        var $selectDom = $('.selectpicker'), //select表单
            $dateStart = $('#startTime'), //开始时间
            $dateEnd = $('#endTime'), //结束时间
            $errorDom = $('.form-error'), //错误提示节点
            $submitBtn = $('#btnSave'), //提交按钮
            $businessType = $('#businessType'),  // 业务类型
            $couponForm = $('.form-list'),
            boolFlag = false, //校验布尔变量值
            currentErrorObj = null;

        // 业务类型切换
        $businessType.on('change', function() {
            if ($businessType.val() == '0') {
                window.location.href ='/activity-manage/coupon';
            }else if($businessType.val() == '1'){
                window.location.href ='/activity-manage/interest-coupon';
            }else if($businessType.val() == '2'){
                window.location.href ='/activity-manage/red-envelope';
            }else if($businessType.val() == '3'){
                window.location.href ='/activity-manage/birthday-coupon';
            }
        });

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

        // 充值金额保留小数点后2位
        var rep = /^\d+$/;
        var rep_point2 = /^[0-9]+\.[0-9]*$/;

        $('.coupon-number, .give-number').blur(function () {
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
                    showErrorMessage('红包金额最小为1', $('.coupon-number', curform));
                    return false;
                }
                var deadline = parseInt($('.coupon-deadline', curform).val());
                if (deadline <= 0) {
                    showErrorMessage('优惠券有效天数必须大于0', $('.coupon-deadline', curform));
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
            $('.coupon-start').val('');
            $('.coupon-end').val('');
            $('.invest-quota').val('');
            $('.give-number').val('');
            $('.productType').prop('checked',false).eq(0).prop('checked',true);
        }

        $('.userGroup').change(function(){
            $('.coupon-table').hide();
            $('.name-tr').remove();
            $('.coupon-agent-channel').children().remove();
            $('.coupon-deposit').hide();
            $('.file-btn').find('input').val('');
            $('.file-btn').hide();
            var userGroup = this.value;
            if(userGroup != "IMPORT_USER" && userGroup != 'AGENT' && userGroup != 'CHANNEL' && userGroup != 'EXCHANGER_CODE' && userGroup != 'NEW_REGISTERED_USER' && userGroup == 'FIRST_INVEST_ACHIEVEMENT' && userGroup == 'MAX_AMOUNT_ACHIEVEMENT' && userGroup == 'LAST_INVEST_ACHIEVEMENT'){
                $.get('/activity-manage/coupon/user-group/' + userGroup + '/estimate', function (data) {
                    $('.give-number').val(data);
                })
            } else if (userGroup == "EXCHANGER_CODE") {
                    $('.file-btn').find('input').val('');
                    $('.give-number').val('').prop('readonly', false);
            } else if (userGroup == "NEW_REGISTERED_USER") {
                    $('.file-btn').find('input').val('');
                    $('.give-number').val('').prop('readonly', false);
            } else if (userGroup == "AGENT") {
                $.get('/user-manage/user/agents', function(data) {
                    if (data.length > 0 ) {
                        $('.coupon-deposit').show();
                    }
                    for (var i=0; i < data.length; i++) {
                        $('.coupon-agent-channel').append('<label><input type="checkbox" class="agent" name="agents" value="'+data[i]+'">'+data[i]+'</label>');
                    }
                })
                $('.give-number').val('0');
            } else if (userGroup == "CHANNEL") {
                $.get('/user-manage/user/channels', function(data) {
                    if (data.length > 0) {
                        $('.coupon-deposit').show();
                    }
                    for (var i=0; i < data.length; i++) {
                        $('.coupon-agent-channel').append('<label><input type="checkbox" class="channel" name="channels" value="'+data[i]+'">'+data[i]+'</label>');
                    }
                })
                $('.give-number').val('0').prop('readonly', false);
            } else if (userGroup == "FIRST_INVEST_ACHIEVEMENT" || userGroup == "MAX_AMOUNT_ACHIEVEMENT" || userGroup == "LAST_INVEST_ACHIEVEMENT") {
                $('.give-number').val('').prop('readonly', false);
            } else if(userGroup == "MEMBERSHIP_V0" || userGroup == "MEMBERSHIP_V1" || userGroup == "MEMBERSHIP_V2"
                || userGroup == "MEMBERSHIP_V3" || userGroup == "MEMBERSHIP_V4" || userGroup == "MEMBERSHIP_V5"){
                $.get('/user-manage/userMembership/count?userGroup='+userGroup,function(data) {
                    $('.give-number').val(parseInt(data)).prop('readonly', true);
                })
            } else if(userGroup == "INVESTED_USER" || userGroup == "REGISTERED_NOT_INVESTED_USER" || userGroup == "STAFF"
            || userGroup == "STAFF_RECOMMEND_LEVEL_ONE" || userGroup == "NOT_ACCOUNT_NOT_INVESTED_USER" || userGroup == "ALL_USER") {
                $.get('/activity-manage/coupon/user-group/' + userGroup + '/estimate', function (data) {
                    $('.give-number').val(data);
                })
            }
            else{
                $('#file-in').trigger('click');
                $('.file-btn').show();
            }
        });

        $('.coupon-agent-channel').on('click','.agent', function() {
            var num = $("input.agent:checkbox:checked").length;
            $('.give-number').val(num);
        });

        $('.coupon-agent-channel').on('click','.channel', function() {
            var num = 0;
            $('.channel:checked').each(function(index,item) {
                $.get('/user-manage/user/'+$(item).val()+'/channel',function(data) {
                    num += parseInt(data);
                    $('.give-number').val(num);
                })
            });
            if($('.channel:checked').length==0) {
                $('.give-number').val('0').prop('readonly', false);
            }
        });

        $('.file-btn').on('change',function(){
            $('.coupon-table').hide();
            $('.name-tr').remove();
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
                if (data.status) {
                    $('#import-file').val(data.fileUuid);
                    $('.give-number').val(data.totalCount);
                    $('.coupon-table').show();
                    var names = data.successLoginNames;
                    for (var i = 0; i < names.length; i++) {
                        $('.table-bordered').append('<tr class="name-tr"><td>'+parseInt(i+1)+'</td><td>'+names[i]+'</td></tr>');
                    }
                } else {
                    $('.give-number').val('0');
                }
                layer.msg(data.message);
            });
        });
    });
});