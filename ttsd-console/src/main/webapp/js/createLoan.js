require(['jquery', 'template', 'mustache', 'text!/tpl/loaner-details.mustache', 'text!/tpl/loaner-enterprise-details.mustache', 'text!/tpl/pledge-house.mustache', 'text!/tpl/pledge-vehicle.mustache', 'text!/tpl/pledge-enterprise.mustache', 'text!/tpl/loan-extra-rate.mustache', 'text!/tpl/loan-title-template.template', 'text!/tpl/loan-title-select-template.template', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'fileinput', 'fileinput_locale_zh', 'Validform', 'Validform_Datatype', 'csrf'],
    function ($, template, Mustache, loanerDetailsTemplate, loanerEnterpriseDetailsTemplate, pledgeHouseTemplate, pledgeVehicleTemplate, pledgeEnterpriseTemplate, loanExtraRateTemplate, loanTitleTemplate, loanTitleSelectTemplate) {
        var loanParam = ['id', 'name', 'agent', 'productType', 'pledgeType', 'loanType', 'pledgeType', 'activityType',
            'loanAmount', 'baseRate', 'activityRate', 'minInvestAmount', 'maxInvestAmount', 'investIncreasingAmount',
            'fundraisingStartTime', 'fundraisingEndTime', 'contractId', 'status'];

        var loanDetailsParam = ['declaration', 'extraRateRuleIds', 'extraSource', 'activity', 'activityDesc'];

        var loanerDetailsParam = ['userName', 'identityNumber', 'gender', 'age', 'marriage', 'region', 'income', 'employmentStatus'];

        var loanerEnterpriseDetailsParam = ['juristicPerson', 'shareholder', 'address', 'purpose'];

        var pledgeHouseParam = ['pledgeLocation', 'estimateAmount', 'pledgeLoanAmount', 'square', 'propertyCardId', 'estateRegisterId', 'authenticAct'];

        var pledgeVehicleParam = ['pledgeLocation', 'estimateAmount', 'pledgeLoanAmount', 'brand', 'model'];

        var pledgeEnterpriseParam = ['pledgeLocation', 'estimateAmount', 'guarantee'];

        var arrayParam = ['extraRateIds', 'extraSource'];

        var loanIdElement = $('input[name="id"]');
        var loanNameElement = $('select[name="name"]'); //标的名称Element
        var loanTypeElement = $('select[name="loanType"]'); //标的类型Element
        var productTypeElement = $('select[name="productType"]'); //标的产品类型Element
        var pledgeTypeElement = $('input[name="pledgeType"]'); //标的抵押类型Element
        var extraElement = $('#extra'); //加息开关
        var extraRuleElement = $('.extra-rate'); //加息开关
        var extraSourceElement = $('.extra-source'); //加息开关

        var formErrorElement = $('.form-error');

        var sectionOneElement = $('#section-one'); //项目信息Section
        var sectionTwoElement = $('#section-two'); //房产车辆抵押借款人信息Section 或 税易经营性借款企业借款人信息Section
        var sectionThreeElement = $('#section-three'); //抵押物信息信息Section

        var loanTitleTemplateHtml;



        //修改section
        var changeSection = function () {
            var loanName = loanNameElement.val();
            if ('房产抵押借款' === loanName) {
                pledgeTypeElement.val("HOUSE");
                sectionTwoElement.html(Mustache.render(loanerDetailsTemplate));
                sectionThreeElement.html(Mustache.render(pledgeHouseTemplate));
            }

            if ('车辆抵押借款' === loanName) {
                pledgeTypeElement.val("VEHICLE");
                sectionTwoElement.html(Mustache.render(loanerDetailsTemplate));
                sectionThreeElement.html(Mustache.render(pledgeVehicleTemplate));
            }

            if ('税易经营性借款' === loanName) {
                pledgeTypeElement.val("ENTERPRISE");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseDetailsTemplate));
                sectionThreeElement.html(Mustache.render(pledgeEnterpriseTemplate));
            }

            $('.selectpicker').selectpicker();
        };

        if (!loanIdElement.val()) {
            changeSection();
        }

        //根据项目名称改变section
        loanNameElement.change(function () {
            changeSection();
            if (['_90', '_180', '_360'].indexOf(productTypeElement.val()) !== -1 && extraElement.is(':checked')) {
                checkedExtraRate();
            }
        });

        $('.selectpicker').selectpicker();

        var fundraisingStartTimeElement = $('#fundraisingStartTime');
        var fundraisingEndTimeElement = $('#fundraisingEndTime');
        fundraisingStartTimeElement.datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            minDate: new Date()
        });
        fundraisingEndTimeElement.datetimepicker({
            format: 'YYYY-MM-DD HH:mm'
        });
        fundraisingStartTimeElement.on('dp.change', function (e) {
            fundraisingEndTimeElement.data("DateTimePicker").minDate(e.date);
        });

        //初始化数据
        $.get("/project-manage/loan/titles", function (data) {
            loanTitleTemplateHtml = template.compile(loanTitleTemplate)({_data: data});

            if (loanIdElement.val()) {
                $.each(rereq, function (index, item) {
                    var uploadBox = $('.upload-box').append(loanTitleTemplateHtml);
                    $(".file-loading").fileinput({
                        language: "zh",
                        uploadUrl: "/upload",
                        showUpload: true,
                        dropZoneTitle: '选择图片文件到这里 &hellip;',
                        initialPreviewShowDelete: true,
                        allowedFileExtensions: ["jpg", "png", "gif", "jpeg"],
                        initialPreview: item
                    });
                    $('.selectpicker').selectpicker({
                        style: 'btn-default',
                        size: 8
                    });

                    $('.loan-title-selector', uploadBox.children().last()).selectpicker('val', index);
                    $('select.loan-title-selector').last().parent().find('input.jq-txt').val(index);
                });
            }
        });

        //添加申请材料
        $('.btn-upload').click(function () {
            $('.upload-box').append(loanTitleTemplateHtml);
            $(".upload").fileinput({
                language: "zh",
                uploadUrl: "/upload",
                showUpload: true,
                dropZoneTitle: '选择图片文件到这里 &hellip;',
                initialPreviewShowDelete: true,
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
            var txt = _this.siblings('.files-input').val().replace(/\s+/g, "");
            if (!txt) {
                _this.parent().append('<i class="error">材料名称不能为空！</i>');
                return;
            }
            var duplicate = _this.siblings('.select-box').find('select.selectpicker option').filter(function (key, option) {
                return $(option).text() == txt;
            });

            if (duplicate.length > 0) {
                _this.parent().append('<i class="error">材料名称已存在,不能重复添加！</i>');
                return;
            }

            $.ajax({
                url: "/project-manage/loan/title",
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
                data: JSON.stringify({title: txt})
            }).done(function (data) {
                var ele = _this;
                $.get("/project-manage/loan/titles", function (res) {
                    ele.siblings('.select-box').children().not('[type="hidden"]').remove();
                    ele.siblings('.select-box').append(template.compile(loanTitleSelectTemplate)({_data: res}));
                    $('.selectpicker').selectpicker({
                        style: 'btn-default',
                        size: 8
                    });
                });
            })
        });

        // 删除添加资料
        $('body').on('click', '.jq-delete', function () {
            $(this).closest('.form-group').remove();
        });


        $('body').on('click', '.loan-title .dropdown-menu li', function () {
            var _this = $(this);
            var bootstrapSelect = _this.closest('.bootstrap-select');
            var _select = bootstrapSelect.siblings('select');
            var _options = _select.find('option');
            var _hidden = bootstrapSelect.siblings('[type="hidden"]');
            _options.each(function (i) {
                if ($.trim(_options.eq(i).text()) == $.trim(_this.text())) {
                    _hidden.val(_options.eq(i).attr('value'));
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

        $('input[name="activity"]').click(function () {
            $('input[name="activityDesc"]').prop('disabled', !$(this).prop("checked"));
        });

        var loanAgentElement = $('input[name="agent"]');
        loanAgentElement.autocomplete({
            source: function (query, process) {
                $.get('/user-manage/account/' + query.term + '/search', function (respData) {
                    return process(respData);
                });
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

        var formFlag = false;
        $(".jq-form").Validform({
            btnSubmit: '.jq-btn-form',
            tipSweep: true,
            focusOnError: false,
            tiptype: function (msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            //beforeSubmit
            beforeCheck: function (curform) {
                $('.form-error').html('');

                if (!loanTypeElement.val()) {
                    showErrorMessage('请选择标的类型', loanTypeElement);
                    return false;
                }

                if (!productTypeElement.val()) {
                    showErrorMessage('请选择借款期限', productTypeElement);
                    return false;
                }

                var activityDesc = $('.jq-activity-desc', curform).val();
                if ($('.jq-activity').prop('checked') && activityDesc.trim() == "") {
                    showErrorMessage('您选择了活动专享,标的所属活动必须填写', $('.jq-activity-desc', curform));
                    $('.jq-activity-desc').prop('disabled', false);
                    return false;
                }

                var duration = $('.jq-duration', curform).val();
                if (duration == '') {
                    showErrorMessage('请选择借款期限', $('.jq-duration', curform));
                    return false;
                }

                var loanAmount = parseFloat($('.jq-pay', curform).val());
                if (loanAmount <= 0) {
                    showErrorMessage('预计出借金额应大于0', $('.jq-pay', curform));
                    return false;
                }
                var increasingPay = parseFloat($('.jq-add-pay', curform).val());
                if (increasingPay <= 0) {
                    showErrorMessage('投资递增金额应大于0', $('.jq-add-pay', curform));
                    return false;
                }
                var minPay = parseFloat($('.jq-min-pay', curform).val());
                if (minPay <= 0) {
                    showErrorMessage('最小投资金额应大于0', $('.jq-min-pay', curform));
                    return false;
                }
                var maxPay = parseFloat($('.jq-max-pay', curform).val());
                if (minPay > maxPay) {
                    showErrorMessage('最小投资金额不得大于最大投资金额', $('.jq-min-pay', curform));
                    return false;
                }
                if (loanAmount < maxPay) {
                    showErrorMessage('最大投资金额不得大于预计出借金额', $('.jq-max-pay', curform));
                    return false;
                }
                if (extraElement.length > 0 && extraElement.is(':checked')) {
                    if ($('input[name="extraSource"]:checked').length <= 0) {
                        showErrorMessage("投资奖励渠道必须选择");
                        return false;
                    }
                }
            },
            callback: function (form) {
                formFlag = true;
                return false;
            }
        });

        //显示警告提示
        var currentErrorObj = null;

        function showErrorMessage(msg, obj) {
            currentErrorObj = obj;
            var htm = '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <span class="txt">建标失败：' + msg + '</span></div>';
            $('.form-error').append(htm);
        }

        //关闭警告提示
        $('.form-error').on('click', function () {
            $('.jq-btn-form').removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });

        //提交表单
        $('.jq-btn-form').click(function () {
            if (!confirm("确认要执行此操作吗?")) {
                return;
            }
            if (formFlag) {
                $(this).attr('disabled', 'disabled');
                indexPic();

                var value = loanNameElement.val();
                var url = $(this).data("url");
                var requestData = {};

                if ("房产抵押借款" == value) {
                    requestData = generateRequestParams({
                        'loan': loanParam,
                        'loanDetails': loanDetailsParam,
                        'loanerDetails': loanerDetailsParam,
                        'pledgeHouse': pledgeHouseParam
                    });
                }
                if ("车辆抵押借款" == value) {
                    requestData = generateRequestParams({
                        'loan': loanParam,
                        'loanDetails': loanDetailsParam,
                        'loanerDetails': loanerDetailsParam,
                        'pledgeVehicle': pledgeVehicleParam
                    });
                }
                if ("税易经营性借款" == value) {
                    requestData = generateRequestParams({
                        'loan': loanParam,
                        'loanDetails': loanDetailsParam,
                        'loanerEnterpriseDetails': loanerEnterpriseDetailsParam,
                        'pledgeEnterprise': pledgeEnterpriseParam
                    });
                }
                $.ajax({
                        url: url,
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(requestData),
                        contentType: 'application/json; charset=UTF-8'
                    })
                    .done(function (res) {
                        if (res.data.status) {
                            formFlag = true;
                            location.href = '/project-manage/loan-list';
                        } else {
                            formFlag = false;
                            var msg = res.data.message || '服务端校验失败';
                            showErrorMessage(msg);
                        }
                    })
                    .fail(function () {
                        $('.jq-btn-form').removeAttr('disabled');
                    })
            }
        });

        var uncheckedExtraRate = function () {
            extraRuleElement.addClass('hidden');
            extraRuleElement.html('');

            extraElement.prop('checked', false);
            extraSourceElement.addClass('hidden');
            extraSourceElement.find("input[name='extraSource']").each(function (index, element) {
                $(element).prop('checked', false);
            });
        };

        var checkedExtraRate = function () {
            formErrorElement.html('');
            if (pledgeTypeElement.val() === 'ENTERPRISE') {
                showErrorMessage('项目不支持');
                extraElement.prop('checked', false);
                return;
            }
            if (['_90', '_180', '_360'].indexOf(productTypeElement.val()) === -1) {
                showErrorMessage('借款期限未选择或选择为30天，不能操作此选项');
                extraElement.prop('checked', false);
                return;
            }
            $.ajax({
                    url: '/project-manage/loan/extra-rate-rule?loanName=' + loanNameElement.val() + '&productType=' + productTypeElement.val(),
                    type: 'GET',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                })
                .done(function (res) {
                    if (res.data.status) {
                        var templateDate = [];
                        $.each(res.data.extraLoanRateRuleModels, function (index, rule) {
                            var minInvestAmount = rule.minInvestAmount / 100;
                            var maxInvestAmount = rule.maxInvestAmount > 0 ? rule.maxInvestAmount / 100 : undefined;
                            templateDate.push({
                                'ruleId': rule.id,
                                'minInvestAmount': minInvestAmount,
                                'maxInvestAmount': maxInvestAmount,
                                'ruleRate': rule.rate * 100
                            });
                        });
                        extraRuleElement.html(Mustache.render(loanExtraRateTemplate, {'rules': templateDate}));
                        extraRuleElement.removeClass('hidden');
                        extraSourceElement.removeClass('hidden');
                    } else {
                        showErrorMessage('服务端校验失败');
                    }
                })
                .fail(function () {
                    showErrorMessage('服务端操作失败');
                });
        };

        extraElement.on('click', function () {
            extraElement.is(':checked') ? checkedExtraRate() : uncheckedExtraRate()
        });

        productTypeElement.on('change', function () {
            if (pledgeTypeElement.val() === 'ENTERPRISE' || ['_90', '_180', '_360'].indexOf(productTypeElement.val()) === -1) {
                uncheckedExtraRate();
            }
            if (['_90', '_180', '_360'].indexOf(productTypeElement.val()) !== -1 && extraElement.is(':checked')) {
                checkedExtraRate();
            }
        });

        var generateRequestParams = function (requestParams) {
            var requestData = {};
            var inputElements = $('form input[type="text"],input[type="hidden"],input[type="checkbox"]:checked,select,textarea');
            $.each(requestParams, function (attr, param) {
                requestData[attr] = {};
                inputElements.each(function (index, element) {
                    var $element = $(element);
                    var elementName = $element.prop('name');
                    var elementValue = $element.val();
                    if (param.indexOf(elementName) !== -1) {
                        if (requestData[attr][elementName]) {
                            $.isArray(requestData[attr][elementName]) ? requestData[attr][elementName].push(elementValue) : requestData[attr][elementName] = [requestData[attr][elementName], elementValue];
                        } else {
                            requestData[attr][elementName] = arrayParam.indexOf(elementName) !== -1 ? [elementValue] : elementValue;
                        }
                    }
                });
            });
            requestData['loan']['loanTitles'] = uploadFile;
            return requestData
        }
    });