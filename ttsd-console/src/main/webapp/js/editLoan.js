window.UEDITOR_HOME_URL = '/js/libs/ueditor/';
require(['jquery', 'template', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'fileinput', 'fileinput_locale_zh', 'Validform', 'Validform_Datatype', 'csrf'], function ($, template) {
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
                    dropZoneTitle: '选择图片文件到这里 &hellip;',
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

        //根据项目名称改变抵押物详情
        $('.jq-name').change(function () {
            var house_html =
                '<div class="form-group"><label class="col-sm-2 control-label">抵押物所在地: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-location" datatype="*" autocomplete="off" placeholder="" errormsg="抵押物所在地不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">借款抵押物估值: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-estimate-amount" datatype="*" autocomplete="off" placeholder="" errormsg="借款抵押物估值不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">抵押物借款金额: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-loan-amount" datatype="*" autocomplete="off" placeholder="" errormsg="抵押物借款金额不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">不动产登记证明:编号: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-estate-register-id" datatype="*" autocomplete="off" placeholder="" errormsg="不动产登记证明不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">房本编号: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-property-card-id" datatype="*" autocomplete="off" placeholder="" errormsg="房本编号不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">借款抵押房产面积: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-square" datatype="*" autocomplete="off" placeholder="" errormsg="借款抵押房产面积不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">公证书: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-authentic-act" datatype="*" autocomplete="off" placeholder="" errormsg="公证书不能为空"></div></div>';
            var vehicle_html =
                '<div class="form-group"><label class="col-sm-2 control-label">抵押物所在地: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-location" datatype="*" autocomplete="off" placeholder="" errormsg="抵押物所在地不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">借款抵押物估值: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-estimate-amount" datatype="*" autocomplete="off" placeholder="" errormsg="借款抵押物估值不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">抵押物借款金额: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-loan-amount" datatype="*" autocomplete="off" placeholder="" errormsg="抵押物借款金额不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">借款抵押车辆型号: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-model" datatype="*" autocomplete="off" placeholder="" errormsg="抵押车辆型号不能为空"></div></div>' +
                '<div class="form-group"><label class="col-sm-2 control-label">借款抵押车辆品牌: </label><div class="col-sm-4"><input type="text" class="form-control ui-autocomplete-input jq-pledge-brand" datatype="*" autocomplete="off" placeholder="" errormsg="抵押车辆品牌不能为空"></div></div>';
            var dom = document.getElementById("pledge-details");
            var value = $('.jq-name').val();
            if ("房产抵押借款" == value) {
                dom.innerHTML = house_html;
            } else if ("车辆抵押借款" == value) {
                dom.innerHTML = vehicle_html;
            }
        });

        //添加申请材料
        $('.btn-upload').click(function () {
            $('.upload-box').append(_html);
            $(".file-loading").fileinput({
                language: "zh",
                uploadUrl: "/upload",
                showUpload: true,
                dropZoneTitle: '选择图片文件到这里 &hellip;',
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
                    if (_hidden.hasClass('jq-duration')) {
                        if (_options.eq(i).attr('value') && _options.eq(i).attr('value') != '') {
                            $('.jq-timer').val(_options.eq(i).data('period'));
                            $('.jq-duration').val(_options.eq(i).data('duration'));
                            $('.jq-product-line').val(_options.eq(i).data('product-line'));
                            $('.jq-product-line').change();
                        } else {
                            $('.jq-timer').val('');
                            $('.jq-duration').val('');
                            $('.jq-product-line').val('');
                            $('.jq-product-line').change();
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

                var projectName = $('.jq-name', curform).val();
                if (projectName == '') {
                    showErrorMessage('请选择借款项目名称', $('.jq-name', curform));
                    return false;
                }

                var duration = $('.jq-duration', curform).val();
                if (duration == '') {
                    showErrorMessage('请选择借款期限', $('.jq-duration', curform));
                    return false;
                }

                var loanAmount = parseFloat($('.jq-pay', curform).val());
                if(loanAmount <= 0){
                    showErrorMessage('预计出借金额应大于0',$('.jq-pay',curform));
                    return false;
                }
                var increasingPay = parseFloat($('.jq-add-pay', curform).val());
                if (increasingPay <= 0) {
                    showErrorMessage('投资递增金额应大于0', $('.jq-add-pay', curform));
                    return false;
                }
                var minPay = parseFloat($('.jq-min-pay',curform).val());
                if(minPay <= 0){
                    showErrorMessage('最小投资金额应大于0',$('.jq-min-pay',curform));
                    return false;
                }
                var maxPay = parseFloat($('.jq-max-pay',curform).val());
                if(minPay > maxPay){
                    showErrorMessage('最小投资金额不得大于最大投资金额',$('.jq-min-pay',curform));
                    return false;
                }
                if(loanAmount < maxPay){
                    showErrorMessage('最大投资金额不得大于预计出借金额',$('.jq-max-pay',curform));
                    return false;
                }
                if ($('#extra:checked').length > 0) {
                    if ($('.extraSource:checked').length <= 0) {
                        showErrorMessage("投资奖励渠道必须选择");
                        return false;
                    }
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
            var htm = '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <span class="txt">' + msg + '</span></div>';
            $('.form-error').append(htm);
        }

        //关闭警告提示
        $('body').on('click','.form-error',function(){
            $('.jq-btn-form').removeAttr('disabled');
            if(!!currentErrorObj){currentErrorObj.focus();}
        });

        function getExtraRateIds() {
            var extraRateIds = [];
            $('.extra-rate-id').each(function () {
                extraRateIds.push($(this).val());
            });
            return extraRateIds;
        }

        function getExtraSource() {
            var extraSource = [];
            $('#extraSource .extraSource').each(function () {
                $(this).prop('checked')==true?extraSource.push($(this).val()):false;
            });
            return extraSource.join(",");
        }


        //提交表单
        $('.jq-btn-form').click(function () {
            //$(".jq-form").Validform({
            //    tiptype: 0,
            //});
            if (!confirm("确认要执行此操作吗?")) {
                return;
            }
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
                var value = $('.jq-name').val();
                var url = API_FORM + operate;
                if ("房产抵押借款" == value) {
                    var dataDto = {
                        "id": $('.jq-loanId').val(),
                        "loanStatus": $('.jq-status').val(),
                        "projectName": $('.jq-name').val(),
                        "pledgeType": $('.jq-pledge-type').val(),
                        "agentLoginName": $('.jq-agent').val(),
                        "loanerLoginName": "",
                        "loanerIdentityNumber": $('.jq-loaner-identity-number').val(),
                        "loanerUserName": $('.jq-loaner-user-name').val(),
                        "type": $('.jq-mark-type').val(),
                        "periods": $('.jq-timer').val(),
                        "duration": $('.jq-duration').val(),
                        "minInvestAmount": $('.jq-min-pay').val(),
                        "maxInvestAmount": $('.jq-max-pay').val(),
                        "investIncreasingAmount": $('.jq-add-pay').val(),
                        "activityType": $('.jq-impact-type').val(),
                        "productType": $('.jq-product-line').val(),
                        "activityRate": $('.jq-percent').val(),
                        "contractId": $('.jq-pact').val(),
                        "basicRate": $('.jq-base-percent').val(),
                        "fundraisingStartTime": new Date(Date.parse(startTime.replace(/-/g, "/"))),
                        "fundraisingEndTime": new Date(Date.parse(endTime.replace(/-/g, "/"))),
                        "showOnHome": showOnHome,
                        "loanAmount": $('.jq-pay').val(),
                        "loanTitles": uploadFile,
                        "extraRateIds": getExtraRateIds(),
                        "extraSource": getExtraSource(),

                        "declaration": $('.jq-loan-declaration').val(),

                        "loanerLoginName": "",
                        "loanerUserName": $('.jq-loaner-user-name').val(),
                        "loanerGender": $('.jq-loaner-gender').val(),
                        "loanerAge": $('.jq-loaner-age').val(),
                        "loanerIdentifyNumber": $('.jq-loaner-identity-number').val(),
                        "loanerMarriage": $('.jq-loaner-marriage').val(),
                        "loanerRegion": $('.jq-loaner-region').val(),
                        "loanerIncome": $('.jq-loaner-income').val(),
                        "loanerEmploymentStatus": $('.jq-loaner-employment').val(),

                        "pledgeLocation": $('.jq-pledge-location').val(),
                        "estimateAmount": $('.jq-pledge-estimate-amount').val(),
                        "pledgeLoanAmount": $('.jq-pledge-loan-amount').val(),
                        "square": $('.jq-pledge-square').val(),
                        "propertyCardId": $('.jq-pledge-property-card-id').val(),
                        "estateRegisterId": $('.jq-pledge-estate-register-id').val(),
                        "authenticAct": $('.jq-pledge-authentic-act').val()
                    };
                    if ('save' == operate) {
                        url += "-house";
                    }
                } else if ("车辆抵押借款" == value) {
                    var dataDto = {
                        "id": $('.jq-loanId').val(),
                        "loanStatus": $('.jq-status').val(),
                        "projectName": $('.jq-name').val(),
                        "pledgeType": $('.jq-pledge-type').val(),
                        "agentLoginName": $('.jq-agent').val(),
                        "loanerLoginName": $('.jq-loaner-login-name').val(),
                        "loanerIdentityNumber": $('.jq-loaner-identity-number').val(),
                        "loanerUserName": $('.jq-loaner-user-name').val(),
                        "type": $('.jq-mark-type').val(),
                        "periods": $('.jq-timer').val(),
                        "duration": $('.jq-duration').val(),
                        "minInvestAmount": $('.jq-min-pay').val(),
                        "maxInvestAmount": $('.jq-max-pay').val(),
                        "investIncreasingAmount": $('.jq-add-pay').val(),
                        "activityType": $('.jq-impact-type').val(),
                        "productType": $('.jq-product-line').val(),
                        "activityRate": $('.jq-percent').val(),
                        "contractId": $('.jq-pact').val(),
                        "basicRate": $('.jq-base-percent').val(),
                        "fundraisingStartTime": new Date(Date.parse(startTime.replace(/-/g, "/"))),
                        "fundraisingEndTime": new Date(Date.parse(endTime.replace(/-/g, "/"))),
                        "showOnHome": showOnHome,
                        "loanAmount": $('.jq-pay').val(),
                        "loanTitles": uploadFile,
                        "extraRateIds": getExtraRateIds(),
                        "extraSource": getExtraSource(),

                        "declaration": $('.jq-loan-declaration').val(),

                        "loanerUserName": $('.jq-loaner-user-name').val(),
                        "loanerGender": $('.jq-loaner-gender').val(),
                        "loanerAge": $('.jq-loaner-age').val(),
                        "loanerIdentifyNumber": $('.jq-loaner-identity-number').val(),
                        "loanerMarriage": $('.jq-loaner-marriage').val(),
                        "loanerRegion": $('.jq-loaner-region').val(),
                        "loanerIncome": $('.jq-loaner-income').val(),
                        "loanerEmploymentStatus": $('.jq-loaner-employment').val(),

                        "pledgeLocation": $('.jq-pledge-location').val(),
                        "estimateAmount": $('.jq-pledge-estimate-amount').val(),
                        "pledgeLoanAmount": $('.jq-pledge-loan-amount').val(),
                        "brand": $('.jq-pledge-brand').val(),
                        "model": $('.jq-pledge-model').val()
                    };
                    if ('save' == operate) {
                        url += '-vehicle';
                    }
                }
                var dataForm = JSON.stringify(dataDto);
                $.ajax({
                    url: url,
                    type: 'POST',
                    dataType: 'json',
                    data: dataForm,
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (res) {
                    if(res.data.status){
                        formFlag =true;
                        location.href='/project-manage/loan-list';
                    }else{
                        formFlag =false;
                        var msg = res.data.message || '服务端校验失败';
                        showErrorMessage(msg);
                    }
                }).fail(function () {
                    console.log("error");
                    $('.jq-btn-form').removeAttr('disabled');
                }).always(function () {
                    console.log("complete");
                });
            }
        });

        function uncheckedExtraRate() {
            $('.extra-rate').addClass('hidden');
            $('.extra-source').addClass('hidden');
            $('.extra-rate-rule').html('');
            $('.extra-rate-id').remove();
            $('#extra').prop('checked', false);
            $('.extraSource').each(function() {
                this.checked = false;
            });
        }

        function checkedExtraRate() {
            $('.form-error').html('');
            var $loanName = $('.jq-name');
            var $productType = $('.jq-product-line');
            if ($loanName.val() == '') {
                showErrorMessage('项目名称未选择，不能操作此选项');
                $('#extra').prop('checked', false);
            } else if ($productType.val() == '' || $productType.val() == '_30') {
                showErrorMessage('借款期限未选择或选择为30天，不能操作此选项');
                $('#extra').prop('checked', false);
            } else {
                $.ajax({
                    url: '/project-manage/loan/extra-rate-rule?loanName=' + $loanName.val() + '&productType=' + $productType.val(),
                    type: 'GET',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                })
                    .done(function (res) {
                        if (res.data.status) {
                            $('.extra-rate-id').remove();
                            $('.extra-rate').removeClass('hidden');
                            $('.extra-source').removeClass('hidden');
                            var $extraRateRule = $('.extra-rate-rule');
                            $extraRateRule.html('');
                            var extraLoanRateRuleModels = res.data.extraLoanRateRuleModels;
                            console.log(extraLoanRateRuleModels)
                            for (var i = 0; i < extraLoanRateRuleModels.length; i++) {
                                var minInvestAmount = extraLoanRateRuleModels[i].minInvestAmount / 100;
                                var maxInvestAmount;
                                if (extraLoanRateRuleModels[i].maxInvestAmount > 0) {
                                    maxInvestAmount = '<' + extraLoanRateRuleModels[i].maxInvestAmount / 100;
                                } else {
                                    maxInvestAmount = '';
                                }
                                $extraRateRule.append('<tr><td> ' + minInvestAmount + '≤投资额' + maxInvestAmount + '</td><td>' + parseFloat(extraLoanRateRuleModels[i].rate * 100) + '</td></tr>');
                                $('.extra-rate').append('<input type="hidden" class="extra-rate-id" value="' + extraLoanRateRuleModels[i].id + '">');
                            }
                        } else {
                            showErrorMessage('服务端校验失败');
                        }
                    })
                    .fail(function () {
                        showErrorMessage('服务端操作失败');
                    });
            }
        }

        $('#extra').on('click', function () {
            if (!$(this).is(':checked')) {
                uncheckedExtraRate();
            } else {
                checkedExtraRate();
            }
        });

        $('.jq-name').on('change', function () {
            var $productType = $('.jq-product-line');
            if ($(this).val() == '') {
                uncheckedExtraRate();
            }
            if ($productType.val() != '' && $productType.val() != '_30' && $('#extra').is(':checked')) {
                checkedExtraRate();
            }
        });

        $('.jq-product-line').on('change', function () {
            if ($(this).val() == '' || $(this).val() == '_30') {
                uncheckedExtraRate();
            }
            if ($('.jq-name').val() != '' && $('#extra').is(':checked')) {
                checkedExtraRate();
            }
        });

        $('.jq-btn-refuse').click(function (event) {

            if (!confirm("确认要执行此操作吗?")) {
                return;
            }

            event.preventDefault();
            var $self=$(this);
            $.ajax({
                url: '/refuse?taskId=PROJECT-'+$(this).attr('data-loanId'),
                type: 'GET',
                dataType: 'json',
                data: {}
            }).done(function(res) {
                if(res.data.status){
                    formFlag =true;
                    location.href='/project-manage/loan-list';
                }else{
                    formFlag =false;
                    var msg = res.data.message || '服务端校验失败';
                    showErrorMessage(msg);
                }
            }).fail(function() {
                console.log("error");
                $('.jq-btn-refuse').removeAttr('disabled');
            });
        });

    });
});
