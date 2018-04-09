require(['jquery', 'underscore', 'template', 'mustache', 'text!/tpl/loaner-details.mustache', 'text!/tpl/loaner-enterprise-details.mustache', 'text!/tpl/pledge-house.mustache', 'text!/tpl/pledge-vehicle.mustache', 'text!/tpl/pledge-enterprise.mustache', 'text!/tpl/loan-extra-rate.mustache', 'text!/tpl/loan-title-template.template', 'text!/tpl/loan-title-select-template.template', 'text!/tpl/loaner-enterprise-info.mustache', 'text!/tpl/loaner-enterprise-factoring-info.mustache', 'jquery-ui', 'bootstrap', 'bootstrapDatetimepicker', 'bootstrapSelect', 'moment', 'fileinput', 'fileinput_locale_zh', 'Validform', 'Validform_Datatype', 'csrf'],
    function ($, _, template, Mustache, loanerDetailsTemplate, loanerEnterpriseDetailsTemplate, pledgeHouseTemplate, pledgeVehicleTemplate, pledgeEnterpriseTemplate, loanExtraRateTemplate, loanTitleTemplate, loanTitleSelectTemplate, loanerEnterpriseInfoTemplate, loanerEnterpriseFactoringInfoTemplate) {
        var loanParam = ['id', 'name', 'agent', 'productType', 'pledgeType', 'loanType', 'pledgeType', 'activityType',
            'loanAmount', 'baseRate', 'activityRate', 'originalDuration', 'minInvestAmount', 'maxInvestAmount', 'investIncreasingAmount',
            'fundraisingStartTime', 'fundraisingEndTime', 'deadline', 'contractId', 'status'];

        var loanDetailsParam = ['declaration', 'extraRateRuleIds', 'extraSource', 'activity', 'activityDesc', 'nonTransferable', 'disableCoupon', 'pushMessage', 'grantReward', 'rewardRate', 'estimates'];

        var loanerDetailsParam = ['userName', 'identityNumber', 'gender', 'age', 'marriage', 'region', 'income', 'employmentStatus', 'purpose'];

        var loanerEnterpriseDetailsParam = ['juristicPerson', 'shareholder', 'address', 'purpose'];

        var loanerEnterpriseInfoParam = ['companyName', 'address', 'purpose'];

        var loanerEnterpriseFactoringInfoParam = ['factoringCompanyName', 'factoringCompanyDesc'];

        var pledgeHouseParam = ['pledgeLocation', 'estimateAmount', 'pledgeLoanAmount', 'square', 'propertyCardId', 'propertyRightCertificateId', 'estateRegisterId', 'authenticAct'];

        var pledgeVehicleParam = ['pledgeLocation', 'estimateAmount', 'pledgeLoanAmount', 'brand', 'model'];

        var pledgeEnterpriseParam = ['pledgeLocation', 'estimateAmount', 'guarantee'];

        var arrayParam = ['extraRateIds', 'extraSource'];

        var arrayPledgeParam = ['pledgeHouse', 'pledgeVehicle', 'pledgeEnterprise'];

        var loanIdElement = $('input[name="id"]');
        var loanNameElement = $('select[name="name"]'); //标的名称Element
        var loanTypeElement = $('select[name="loanType"]'); //标的类型Element
        var productTypeElement = $('select[name="productType"]'); //标的产品类型Element
        var pledgeTypeElement = $('input[name="pledgeType"]'); //标的抵押类型Element
        var extraElement = $('#extra'); //加息开关
        var extraRuleElement = $('.extra-rate'); //加息开关
        var extraSourceElement = $('.extra-source'); //extraSource
        var $formSubmitBtn = $('.form-submit-btn');
        var $currentFormSubmitBtn;
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
                sectionThreeElement.html("<div class='house-pledge'><h3><span class='house-title'>抵押物信息</span> <button type='button' class='jq-add-house-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>" + Mustache.render(pledgeHouseTemplate) + '</div>');
            }

            if ('车辆抵押借款' === loanName) {
                pledgeTypeElement.val("VEHICLE");
                sectionTwoElement.html(Mustache.render(loanerDetailsTemplate));
                sectionThreeElement.html("<div class='vehicle-pledge'><h3><span class='vehicle-title'>抵押物信息</span> <button type='button' class='jq-add-vehicle-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>" + Mustache.render(pledgeVehicleTemplate) + '</div>');
            }

            if ('经营性借款' === loanName && $('#projectName option:selected').attr('data-pledgetype') === 'ENTERPRISE_CREDIT') {
                pledgeTypeElement.val("ENTERPRISE_CREDIT");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseDetailsTemplate));
                sectionThreeElement.html(Mustache.render(''));
            }

            if ('经营性借款' === loanName && $('#projectName option:selected').attr('data-pledgetype') === 'ENTERPRISE_PLEDGE') {
                pledgeTypeElement.val("ENTERPRISE_PLEDGE");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseDetailsTemplate));
                sectionThreeElement.html("<div class='enterprise-pledge'><h3><span class='enterprise-title'>抵押物信息</span> <button type='button' class='jq-add-enterprise-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>" + Mustache.render(pledgeEnterpriseTemplate) + '</div>');
            }

            if ('经营性借款' === loanName && $('#projectName option:selected').attr('data-pledgetype') === 'ENTERPRISE_FACTORING') {
                pledgeTypeElement.val("ENTERPRISE_FACTORING");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseInfoTemplate));
                sectionThreeElement.html(Mustache.render(loanerEnterpriseFactoringInfoTemplate));
            }

            if ('经营性借款' === loanName && $('#projectName option:selected').attr('data-pledgetype') === 'ENTERPRISE_BILL') {
                pledgeTypeElement.val("ENTERPRISE_BILL");
                sectionTwoElement.html(Mustache.render(loanerEnterpriseInfoTemplate));
                sectionThreeElement.html(Mustache.render(''));
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
            format: 'YYYY-MM-DD HH:mm'
        });
        fundraisingEndTimeElement.datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            useCurrent: false
        });
        fundraisingStartTimeElement.on("dp.change", function (e) {
            fundraisingEndTimeElement.data("DateTimePicker").minDate(e.date);
        });
        fundraisingEndTimeElement.on("dp.change", function (e) {
            fundraisingStartTimeElement.data("DateTimePicker").maxDate(e.date);
        });

        var deadlineElement = $('#deadline');
        deadlineElement.datetimepicker({
            format: 'YYYY-MM-DD'
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

        //添加房产抵押物信息
        $('body').on('click', '.jq-add-house-pledge', function () {
            if($('.house-pledge').length > 3){
                $('#pledge-modal').modal('show');
            }else{
                sectionThreeElement.append("<div class='house-pledge'><h3><span class='house-title'>抵押物信息</span> <button type='button' class='jq-add-house-pledge btn btn-info' style='margin-left: 10px;'>+</button> <button type='button' class='jq-del-house-pledge btn btn-info'>-</button></h3>" + Mustache.render(pledgeHouseTemplate) + '</div>');
                $("#section-three h3 span").each(function (i) {
                    $(this).text("抵押物信息" + (i + 1));
                });
            }
        });

        // 删除房产抵押物信息
        $('body').on('click', '.jq-del-house-pledge', function () {
            $(this).closest('.house-pledge').remove();

            if ($("#section-three h3 span").length > 1) {
                $("#section-three h3 span").each(function (i) {
                    $(this).text("抵押物信息" + (i + 1));
                });
            } else {
                $(this).text("抵押物信息");
            }

            if ($("#section-three h3 span").text() == "抵押物信息1") {
                $("#section-three h3 span").text("抵押物信息");
            }
        });

        //添加车辆抵押物信息
        $('body').on('click', '.jq-add-vehicle-pledge', function () {
            if($('.vehicle-pledge').length > 3){
                $('#pledge-modal').modal('show');
            }else {
                sectionThreeElement.append("<div class='vehicle-pledge'><h3><span class='vehicle-title'>抵押物信息</span> <button type='button' class='jq-add-vehicle-pledge btn btn-info' style='margin-left: 10px;'>+</button> <button type='button' class='jq-del-vehicle-pledge btn btn-info'>-</button></h3>" + Mustache.render(pledgeVehicleTemplate) + '</div>');
                $("#section-three h3 span").each(function (i) {
                    $(this).text("抵押物信息" + (i + 1));
                });
            }
        });

        // 删除车辆抵押物信息
        $('body').on('click', '.jq-del-vehicle-pledge', function () {
            $(this).closest('.vehicle-pledge').remove();

            if ($("#section-three h3 span").length > 1) {
                $("#section-three h3 span").each(function (i) {
                    $(this).text("抵押物信息" + (i + 1));
                });
            } else {
                $(this).text("抵押物信息");
            }

            if ($("#section-three h3 span").text() == "抵押物信息1") {
                $("#section-three h3 span").text("抵押物信息");
            }
        });


        //添加税易经营性借款抵押物信息
        $('body').on('click', '.jq-add-enterprise-pledge', function () {
            if($('.enterprise-pledge').length > 3){
                $('#pledge-modal').modal('show');
            }else {
                sectionThreeElement.append("<div class='enterprise-pledge'><h3><span class='enterprise-title'>抵押物信息</span> <button type='button' class='jq-add-enterprise-pledge btn btn-info' style='margin-left: 10px;'>+</button> <button type='button' class='jq-del-enterprise-pledge btn btn-info'>-</button></h3>" + Mustache.render(pledgeEnterpriseTemplate) + '</div>');
                $("#section-three h3 span").each(function (i) {
                    $(this).text("抵押物信息" + (i + 1));
                });
            }

        });

        // 删除税易经营性借款抵押物信息
        $('body').on('click', '.jq-del-enterprise-pledge', function () {
            $(this).closest('.enterprise-pledge').remove();

            if ($("#section-three h3 span").length > 1) {
                $("#section-three h3 span").each(function (i) {
                    $(this).text("抵押物信息" + (i + 1));
                });
            } else {
                $(this).text("抵押物信息");
            }

            if ($("#section-three h3 span").text() == "抵押物信息1") {
                $("#section-three h3 span").text("抵押物信息");
            }
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
        var initUploadImagesData = function () {
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

        $('input[name="grantReward"]').click(function () {
            $('input[name="rewardRate"]').prop('disabled', !$(this).prop("checked"));
        });

        var loanAgentElement = $('input[name="agent"]');
        loanAgentElement.autocomplete({
            source: function (query, process) {
                $.get('/user-manage/account/' + query.term + '/search', function (respData) {
                    return process(respData);
                });
            }
        });


        $('input.amount,input.rate').blur(function () {
            var self = $(this);
            var value = self.val().trim();
            if (!$.isNumeric(value)) {
                self.val('');
                return false;
            }

            var nums = value.split('.');
            var integer = nums[0] === '' ? 0 : parseInt(nums[0]);
            var fraction = nums.length > 1 && nums[1] !== '' ? nums[1].substr(0, 2) : '0';
            self.val(integer + '.' + fraction);

        });

        var fromValid = true;
        $("form").Validform({
            btnSubmit: '.form-submit-btn',
            tipSweep: true,
            focusOnError: false,
            tiptype: function (msg, o, cssctl) {
                //msg：提示信息;
                //o:{obj:*,type:*,curform:*},
                //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单时o.obj为该表单对象），
                //type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态,
                //curform为当前form对象;
                //cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
                var $ele = $(o.obj);
                var invalid = o.type === 3;
                if (invalid) { // 3校验失败
                    var errorMessage = $ele.attr('errormsg') || "请检查输入是否正确";
                    showErrorMessage(errorMessage, $ele);
                }
            },
            beforeCheck: function (form) {
                clearErrorMessage();
            },
            beforeSubmit: function (curform) {
                if (!loanTypeElement.val()) {
                    showErrorMessage('请选择标的类型', loanTypeElement);
                    return false;
                }

                if (!productTypeElement.val()) {
                    showErrorMessage('请选择借款期限', productTypeElement);
                    return false;
                }

                if (parseFloat($('input[name="baseRate"]')) === 0) {
                    showErrorMessage('基本利率不能为0', $('input[name="baseRate"]'));
                    return false;
                }

                var activityDesc = $('input[name="activityDesc"]');
                if ($('input[name="activity"]').prop('checked') && activityDesc.val().trim() === '') {
                    showErrorMessage('标的所属活动必须填写', activityDesc);
                    return false;
                }

                var $loanAmount = $('input[name="loanAmount"]');
                var $minInvestAmount = $('input[name="minInvestAmount"]');
                var $maxInvestAmount = $('input[name="maxInvestAmount"]');
                var $investIncreasingAmount = $('input[name="investIncreasingAmount"]');

                if (parseFloat($minInvestAmount.val()) > parseFloat($maxInvestAmount.val())) {
                    showErrorMessage('最小投资金额不得大于最大投资金额', $minInvestAmount);
                    return false;
                }

                if (parseFloat($loanAmount.val()) < parseFloat($maxInvestAmount.val())) {
                    showErrorMessage('最大投资金额不得大于预计出借金额', $maxInvestAmount);
                    return false;
                }

                if (parseFloat($maxInvestAmount.val()) < parseFloat($investIncreasingAmount.val())) {
                    showErrorMessage('投资递增金额不得大于最大投资金额', $investIncreasingAmount);
                    return false;
                }

                if (parseFloat($loanAmount.val()) < parseFloat($investIncreasingAmount.val())) {
                    showErrorMessage('投资递增金额不得大于预计出借金额', $investIncreasingAmount);
                    return false;
                }

                if (extraElement.length > 0 && extraElement.is(':checked')) {
                    if ($('input[name="extraSource"]:checked').length <= 0) {
                        showErrorMessage("投资奖励渠道必须选择", $('input[name="extraSource"]'));
                        return false;
                    }
                }
            },
            callback: function (data) {
                return false;
            }
        });

        var showErrorMessage = function (message, $ele) {
            fromValid = false;
            var $error = $('<span class="alert alert-danger" role="alert"></span>');
            $error.html(message);
            $ele.parents('.form-group').addClass("has-error").append($error);
            $ele.focus();
        };

        var clearErrorMessage = function () {
            fromValid = true;
            $('.alert-danger').remove();
            $('.form-group').removeClass('has-error');
        };

        $('#confirm-modal').find('.btn-submit').click(function () {
            $formSubmitBtn.attr('disabled', 'disabled');
            initUploadImagesData();

            var value = loanNameElement.val();
            var url = $currentFormSubmitBtn.data("url");
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
            if ("经营性借款" == value && $('#projectName option:selected').attr('data-pledgetype') == 'ENTERPRISE_CREDIT') {
                requestData = generateRequestParams({
                    'loan': loanParam,
                    'loanDetails': loanDetailsParam,
                    'loanerEnterpriseDetails': loanerEnterpriseDetailsParam
                });
            }
            if ("经营性借款" == value && $('#projectName option:selected').attr('data-pledgetype') == 'ENTERPRISE_PLEDGE') {
                requestData = generateRequestParams({
                    'loan': loanParam,
                    'loanDetails': loanDetailsParam,
                    'loanerEnterpriseDetails': loanerEnterpriseDetailsParam,
                    'pledgeEnterprise': pledgeEnterpriseParam
                });
            }
            if ("经营性借款" == value && $('#projectName option:selected').attr('data-pledgetype') == 'ENTERPRISE_FACTORING') {
                requestData = generateRequestParams({
                    'loan': loanParam,
                    'loanDetails': loanDetailsParam,
                    'loanerEnterpriseInfo': loanerEnterpriseInfoParam + loanerEnterpriseFactoringInfoParam
                });
            }
            if ("经营性借款" == value && $('#projectName option:selected').attr('data-pledgetype') == 'ENTERPRISE_BILL') {
                requestData = generateRequestParams({
                    'loan': loanParam,
                    'loanDetails': loanDetailsParam,
                    'loanerEnterpriseInfo': loanerEnterpriseInfoParam
                });
            }

            $.ajax(
                {
                    url: url,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(requestData),
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (res) {
                $currentFormSubmitBtn.removeAttr('disabled');
                $('#confirm-modal').modal('hide');
                if (res.data.status) {
                    fromValid = true;
                    location.href = '/project-manage/loan-list';
                } else {
                    fromValid = false;
                    var msg = res.data.message || '服务端校验失败';
                    showErrorMessage(msg, $currentFormSubmitBtn);
                }
            }).fail(function () {
                $currentFormSubmitBtn.removeAttr('disabled');
                $('#confirm-modal').modal('hide');
            })
        });

        //提交表单
        $formSubmitBtn.click(function () {
            $currentFormSubmitBtn = $(this);
            if (fromValid) {
                $('#confirm-modal').modal('show');
            }

            return false;
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
            clearErrorMessage();

            if (['_90', '_180', '_360'].indexOf(productTypeElement.val()) === -1) {
                showErrorMessage('借款期限未选择或选择为30天，不能操作此选项', extraElement);
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
                        showErrorMessage('服务端校验失败', extraElement);
                    }
                }).fail(function () {
                showErrorMessage('服务端操作失败', extraElement);
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
            var arrPledgeInfo = [];
            var inputElements = $('form input[type="text"],input[type="hidden"],input[type="checkbox"]:checked,input[type="radio"]:checked,select,textarea');
            $.each(requestParams, function (attr, param) {
                requestData[attr] = {};
                inputElements.each(function (index, element) {
                    var $element = $(element);
                    var elementName = $element.prop('name');
                    var elementValue = $element.val();
                    if (arrayPledgeParam.indexOf(attr) !== -1) {
                        if (param.indexOf(elementName) !== -1) {
                            arrPledgeInfo.push(attr + "_" + elementName + ":" + elementValue);
                        }
                    }
                    else {
                        if (param.indexOf(elementName) !== -1) {
                            if (requestData[attr][elementName]) {
                                $.isArray(requestData[attr][elementName]) ? requestData[attr][elementName].push(elementValue) : requestData[attr][elementName] = [requestData[attr][elementName], elementValue];
                            } else {
                                requestData[attr][elementName] = arrayParam.indexOf(elementName) !== -1 ? [elementValue] : elementValue;
                            }
                        }
                    }
                });
            });
            requestData['loan']['loanTitles'] = uploadFile;

            if (arrPledgeInfo.length > 0) {
                var pledgeStr = arrPledgeInfo[0].substring(0, arrPledgeInfo[0].indexOf("_"));
                requestData[pledgeStr] = getArray(arrPledgeInfo, pledgeStr);
            }
            console.log(requestData)
            return requestData
        };

        var getArray = function (arrPledgeInfo, pledgeType) {
            var arrPledge = [];
            var obj1 = {};
            var obj2 = {};
            var obj3 = {};
            var obj4 = {};
            if (pledgeType == "pledgeHouse") {
                var pledgeHouseParamLength = pledgeHouseParam.length-1
                for (i in arrPledgeInfo) {

                    if (i < pledgeHouseParamLength) {
                        obj1[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    } else if (i >= pledgeHouseParamLength && i < pledgeHouseParamLength * 2) {
                        obj2[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                    else if (i >= pledgeHouseParamLength * 2 && i < pledgeHouseParamLength * 3) {
                        obj3[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                    else if (i >= pledgeHouseParamLength * 3 && i < pledgeHouseParamLength * 4) {
                        obj4[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                }
            }
            if (pledgeType == "pledgeVehicle") {
                for (i in arrPledgeInfo) {
                    if (i < pledgeVehicleParam.length) {
                        obj1[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    } else if (i >= pledgeVehicleParam.length && i < pledgeVehicleParam.length * 2) {
                        obj2[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                    else if (i >= pledgeVehicleParam.length * 2 && i < pledgeVehicleParam.length * 3) {
                        obj3[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                    else if (i >= pledgeVehicleParam.length * 3 && i < pledgeVehicleParam.length * 4) {
                        obj4[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                }
            }
            if (pledgeType == "pledgeEnterprise") {
                for (i in arrPledgeInfo) {
                    if (i < pledgeEnterpriseParam.length) {
                        obj1[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    } else if (i >= pledgeEnterpriseParam.length && i < pledgeEnterpriseParam.length * 2) {
                        obj2[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                    else if (i >= pledgeEnterpriseParam.length * 2 && i < pledgeEnterpriseParam.length * 3) {
                        obj3[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                    else if (i >= pledgeEnterpriseParam.length * 3 && i < pledgeEnterpriseParam.length * 4) {
                        obj4[arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf("_") + 1, arrPledgeInfo[i].indexOf(":"))] = arrPledgeInfo[i].substring(arrPledgeInfo[i].indexOf(":")+1, arrPledgeInfo[i].length);
                    }
                }
            }
            arrPledge.push(obj1);
            if (!isEmptyObject(obj2)) arrPledge.push(obj2);
            if (!isEmptyObject(obj3)) arrPledge.push(obj3);
            if (!isEmptyObject(obj4)) arrPledge.push(obj4);
            return arrPledge;
        };

        var isEmptyObject = function isEmptyObject(obj) {
            for (var key in obj) {
                return false;
            }
            return true;
        }
    });