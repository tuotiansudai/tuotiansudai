window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'template', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'fileinput', 'fileinput_locale_zh', 'Validform', 'Validform_Datatype','ueditor', 'csrf'], function ($, template) {
    $(function () {
        var _html = '';
        var data = '';
        //初始化校验
        //$(".jq-form").Validform({
        //    tiptype: 0,
        //});
        //初始化数据
        $.get(API_SELECT, function (data) {
            //data = data;
            var dataTPL = {_data:data};
            _html = template('upload', dataTPL);
            for(var i in rereq){
                var uploadbox = $('.upload-box').append(_html);
                $(".file-loading").fileinput({
                    language: "zh",
                    uploadUrl: "/upload",
                    showUpload: true,
                    initialPreviewShowDelete:true,
                    allowedFileExtensions: ["jpg", "png", "gif", "jpeg"],
                    initialPreview:rereq[i]
                });
                $('.selectpicker').selectpicker({
                    style: 'btn-default',
                    size: 8
                });

                $('.rereq',uploadbox.children().last()).selectpicker('val', i);
                $('select.rereq').last().parent().find('input.jq-txt').val(i);
            }
        });

        $('#datetimepicker6').datetimepicker({format: 'YYYY-MM-DD HH:mm', minDate: new Date(Date.parse('2015/1/1'))});
        $('#datetimepicker7').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
        $('.selectpicker').selectpicker({
            style: 'btn-default',
            size: 8
        });
        var dpicker7 = $('#datetimepicker7').data("DateTimePicker");
        $('#datetimepicker6').on('dp.change',function(e){
            dpicker7.minDate(e.date);
        });

        //添加申请材料
        $('.btn-upload').click(function () {
            $('.upload-box').append(_html);
            $(".file-loading").fileinput({
                language: "zh",
                uploadUrl: "/upload",
                showUpload: true,
                initialPreviewShowDelete:true,
                allowedFileExtensions: ["jpg", "png", "gif", "jpeg"]
            });
            $('.selectpicker').selectpicker({
                style: 'btn-default',
                size: 8
            });
        });

        //添加材料名称
        $('body').on('click', '.jq-add', function () {
            var _this = $(this);
            var obj = _this.parent().find('.error');
            if (obj.length) {
                obj.remove();
            }
            var txt = _this.siblings('.files-input').val().replace(/\s+/g,"");
            if (!txt) {
                _this.parent().append('<i class="error">材料名称不能为空！</i>');
                return;
            }
            var duplicate = _this.siblings('.select-box').find('select.selectpicker option').filter(function(key,option){
                return $(option).text() == txt;
            });

            if (duplicate.length > 0) {
                _this.parent().append('<i class="error">材料名称已存在,不能重复添加！</i>');
                return;
            }
            $.ajax({
                url: API_POST_TITLE,
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify({title: txt})
            }).done(function (data) {
                //if (data.status) {
                //data = data;
                ajaxGet(API_SELECT, _this);
                //} else {

                //}
            })
        });

        // 删除添加资料
        $('body').on('click', '.jq-delete', function () {
            $(this).closest('.form-group').remove();
        });


        // 渲染页面 slect ajax
        var ajaxGet = function (url, ele) {
            var url = url;
            var ele = ele;
            $.get(url, function (res) {
                var res = {_data:res};
                var ret = template('select', res);
                ele.siblings('.select-box').children().not('[type="hidden"]').remove();
                ele.siblings('.select-box').append(ret);
                $('.selectpicker').selectpicker({
                    style: 'btn-default',
                    size: 8
                });
            });
        }

        //选择下拉框赋值给input
        $('body').on('click', '.dropdown-menu li', function () {
            var _this = $(this);
            var txt = _this.text();
            var bootstrapSelect = _this.closest('.bootstrap-select');
            var _select = bootstrapSelect.siblings('select');
            var _options = _select.find('option');
            var _hidden = bootstrapSelect.siblings('[type="hidden"]');
            var flag = false;
            if (_select.hasClass('jq-b-type')) {
                flag = true;
            } else {
                flag = false;
            }
            _options.each(function (i) {
                if ($.trim(_options.eq(i).text()) == $.trim(txt)) {
                    if (flag) {
                        var pix = _options.eq(i).attr('data-repaytimeunit');
                        var _pix = '';
                        if (pix == 'MONTH') {
                            _pix = "月";
                        } else {
                            _pix = "天";
                        }
                        var day = _options.eq(i).attr('data-repaytimeperiod');
                        $('.jq-day').text(day);
                        $('.jq-piex').text(_pix);
                    }
                    _hidden.val(_options.eq(i).attr('value'));
                    if (_hidden.hasClass('jq-product-type')) {
                        if (_options.eq(i).attr('value')) {
                            $('.jq-timer').val(_options.eq(i).data('period'));
                            $('.jq-base-percent').val(_options.eq(i).data('baserate'));
                        } else {
                            $('.jq-timer').val('');
                            $('.jq-base-percent').val('');
                        }
                    }
                }
            })
        });

        var uploadFile = []; //存放上传资料
        // 循环上传图片分配对应位置
        var indexPic = function () {
            uploadFile = [];
            var _url = '';
            var _parent = $('.upload-box');
            var formGroup = _parent.find('.form-group');
            formGroup.each(function (index) {
                var str = '';
                var obj = {};
                obj.titleId = formGroup.eq(index).find('.jq-txt').val();
                if (formGroup.eq(index).find('.file-preview-frame').index()) {
                    obj.applicationMaterialUrls = '';
                } else {
                    formGroup.eq(index).find('.file-preview-frame').each(function (i) {
                        var _img = formGroup.eq(index).find('.file-preview-frame').eq(i).find('img').attr('src');
                        str += _img + ',';
                        _url = str.substring(0, str.lastIndexOf(','));

                    });
                    obj.applicationMaterialUrls = _url;
                }
                uploadFile.push(obj);
            });
        };
        $('.jq-checkbox label').click(function () {
            if ($('.jq-index').prop('checked')) {
                $('.jq-index').val('1');
            } else {
                $('.jq-index').val('0');
            }
        });
        //自动完成提示
        var autoValue = '';
        $(".jq-agent").autocomplete({
            source: function (query, process) {
                $.get('/user-manage/account/' + query.term + '/search', function (respData) {
                    autoValue = respData;
                    return process(respData);
                });
            }
        });
        $(".jq-agent").blur(function () {
            for(var i = 0; i< autoValue.length; i++){
                if($(this).val()== autoValue[i]){
                    $(this).removeClass('Validform_error');
                    return false;
                }else{
                    $(this).addClass('Validform_error');
                }

            }

        });



        // 充值金额保留小数点后2位
        var rep = /^\d+$/;
        var rep_point = /^([0-9]+\.[0-9]{2})[0-9]*$/;
        var rep_point1 = /^[0-9]+\.[0-9]$/;
        $('.jq-money').blur(function () {
            var _this = $(this)
            var text = _this.val();
            var num = text.replace(rep_point, "$1");
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

        var formFlag =false;
        $(".jq-form").Validform({
            btnSubmit:'.jq-btn-form',
            tipSweep: true,
            focusOnError: false,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            //beforeSubmit
            beforeCheck: function(curform){
                $('.form-error').html('');
                var periods = parseInt($('.jq-timer',curform).val());
                if(periods <= 0){
                    showErrorMessage('借款期限最小为1',$('.jq-timer',curform));
                    return false;
                }
                var loanAmount = parseInt($('.jq-pay',curform).val());
                if(loanAmount <= 0){
                    showErrorMessage('预计出借金额应大于0',$('.jq-pay',curform));
                    return false;
                }
                var increasingPay = parseFloat($('.jq-add-pay', curform).val());
                if (increasingPay <= 0) {
                    showErrorMessage('投资递增金额应大于0', $('.jq-add-pay', curform));
                    return false;
                }
                var minPay = parseInt($('.jq-min-pay',curform).val());
                if(minPay <= 0){
                    showErrorMessage('最小投资金额应大于0',$('.jq-min-pay',curform));
                    return false;
                }
                var maxPay = parseInt($('.jq-max-pay',curform).val());
                if(minPay > maxPay){
                    showErrorMessage('最小投资金额不得大于最大投资金额',$('.jq-min-pay',curform));
                    return false;
                }
                if(loanAmount < maxPay){
                    showErrorMessage('最大投资金额不得大于预计出借金额',$('.jq-max-pay',curform));
                    return false;
                }
            },
            callback:function(form){
                formFlag = true;
                return false;
            }
        });

        //显示警告提示
        var currentErrorObj = null;
        function showErrorMessage(msg, obj){
            currentErrorObj = obj;
            var htm = '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <span class="txt">建标失败：'+msg+'</span></div>';
            $('.form-error').append(htm);
        }

        //关闭警告提示
        $('body').on('click','.form-error',function(){
            $('.jq-btn-form').removeAttr('disabled');
            if(!!currentErrorObj){currentErrorObj.focus();}
        });
        //提交表单
        $('.jq-btn-form').click(function () {
            //$(".jq-form").Validform({
            //    tiptype: 0,
            //});
            var operate = $(this).data("operate");
            if(formFlag) {
                $(this).attr('disabled','disabled');
                indexPic();
                var startTime = $('.jq-star-date').val();
                var endTime = $('.jq-end-date').val();
                var showOnHomeInputVal = $('.jq-index').val();
                var showOnHome = true;
                if (showOnHomeInputVal == '0') {
                    showOnHome = false;
                }
                var dataForm = JSON.stringify({
                    "id":$('.jq-loanId').val().replace(/\,/g,""),
                    "loanStatus":$('.jq-status').val(),
                    "projectName": $('.jq-user').val(),
                    "agentLoginName": $('.jq-agent').val(),
                    "loanerLoginName": $('.jq-loaner-login-name').val(),
                    "loanerUserName": $('.jq-loaner-user-name').val(),
                    "loanerIdentityNumber": $('.jq-loaner-identity-number').val(),
                    "type": $('.jq-mark-type').val(),
                    "periods": $('.jq-timer').val(),
                    "descriptionText": getContentTxt(),
                    "descriptionHtml": getContent(),
                    "investFeeRate": $('.jq-fee').val(),
                    "minInvestAmount": $('.jq-min-pay').val(),
                    "maxInvestAmount": $('.jq-max-pay').val(),
                    "investIncreasingAmount": $('.jq-add-pay').val(),
                    "activityType": $('.jq-impact-type').val(),
                    "productType": $('.jq-product-type').val(),
                    "activityRate": $('.jq-percent').val(),
                    "contractId": $('.jq-pact').val(),
                    "basicRate": $('.jq-base-percent').val(),
                    "fundraisingStartTime": new Date(Date.parse(startTime.replace(/-/g, "/"))),
                    "fundraisingEndTime": new Date(Date.parse(endTime.replace(/-/g, "/"))),
                    "showOnHome": showOnHome,
                    "loanAmount": $('.jq-pay').val(),
                    "loanTitles": uploadFile
                });
                $.ajax({
                    url: API_FORM+operate,
                    type: 'POST',
                    dataType: 'json',
                    data: dataForm,
                    contentType: 'application/json; charset=UTF-8'
                })
                    .done(function (res) {
                        if(res.data.status){
                            formFlag =true;
                            location.href='/project-manage/loan-list';
                        }else{
                            formFlag =false;
                            var msg = res.data.message || '服务端校验失败';
                            showErrorMessage(msg);
                        }
                    })
                    .fail(function () {
                        console.log("error");
                        $('.jq-btn-form').removeAttr('disabled');
                    })
                    .always(function () {
                        console.log("complete");
                    });
            }
        });
    });
});
