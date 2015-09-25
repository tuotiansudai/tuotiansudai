/**
 * Created by belen on 15/8/21.
 */
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

    });

    function initSelect() {
        var _selectAll = $('.jq-form select');
        var _selectOption = $('select option');
        _selectAll.each(function (m) {
            var _optionTxt = $(this).find('option').eq(0).attr('value');
            $(this).siblings('[type="hidden"]').val(_optionTxt);
        });
    };
    initSelect();

    $('#datetimepicker6').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
    $('#datetimepicker7').datetimepicker({format: 'YYYY-MM-DD HH:mm'});
    $('.selectpicker').selectpicker({
        style: 'btn-default',
        size: 8
    });
    //添加申请材料
    $('.btn-upload').click(function () {
        $('.upload-box').append(_html);
        $(".file-loading").fileinput({
            language: "zh",
            uploadUrl: "/",
            showUpload: false,
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
        var txt = _this.siblings('.files-input').val();
        $.ajax({
            url: API_POST_TITLE,
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({title: txt}),
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
                obj.applyMetarialUrl = '';
            } else {
                formGroup.eq(index).find('.file-preview-frame').each(function (i) {
                    var _img = formGroup.eq(index).find('.file-preview-frame').eq(i).find('img').attr('title');
                    str += _img + ',';
                    _url = str.substring(0, str.lastIndexOf(','));

                });
                obj.applyMetarialUrl = _url;
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
    $("#tags,#tags_1").autocomplete({
        source: function (query, process) {
            //var matchCount = this.options.items;//返回结果集最大数量
            $.get(api_url+'/'+query.term, function (respData) {
                autoValue = respData;
                return process(respData);
            });
        }
    });
    $("#tags,#tags_1").blur(function () {
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
        tiptype: 0,
        callback:function(form){
            formFlag = true;
            return false;
        }
    });
    //关闭警告提示
    $('body').on('click','[aria-hidden="true"]',function(){
        $('.jq-btn-form').removeAttr('disabled');
    });
    //提交表单
    $('.jq-btn-form').click(function () {
        //$(".jq-form").Validform({
        //    tiptype: 0,
        //});
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
                "projectName": $('.jq-user').val(),
                "agentLoginName": $('#tags_1').val(),
                "loanerLoginName": $('#tags').val(),
                "type": $('.jq-mark-type').val(),
                "periods": $('.jq-timer').val(),
                "descriptionText": getContentTxt(),
                "descriptionHtml": getContent(),
                "investFeeRate": $('.jq-fee').val(),
                "minInvestAmount": $('.jq-min-pay').val(),
                "maxInvestAmount": $('.jq-max-pay').val(),
                "investIncreasingAmount": $('.jq-add-pay').val(),
                "activityType": $('.jq-impact-type').val(),
                "activityRate": $('.jq-percent').val(),
                "contractId": $('.jq-pact').val(),
                "basicRate": $('.jq-base-percent').val(),
                "fundraisingStartTime": new Date(Date.parse(startTime.replace(/-/g, "/"))),
                "fundraisingEndTime": new Date(Date.parse(endTime.replace(/-/g, "/"))),
                "showOnHome": showOnHome,
                "loanAmount": $('.jq-pay').val(),
                "loanTitles": uploadFile,
            });
            $.ajax({
                url: API_FORM,
                type: 'POST',
                dataType: 'json',
                data: dataForm,
                contentType: 'application/json; charset=UTF-8',
            })
                .done(function (res) {
                    if(res.data.status){
                        formFlag =true;
                        var htm = '<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <span class="txt">亲，恭喜您建标成功！</span></div>';
                        $('.form-error').append(htm);
                    }else{
                        formFlag =false;
                        var htm = '<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <span class="txt">亲，建标失败啦！</span></div>';
                        $('.form-error .txt').text('亲，建标失败啦！')
                        $('.form-error').append(htm);

                    }
                })
                .fail(function () {
                    console.log("error");
                })
                .always(function () {
                    console.log("complete");
                });
        }
    });
});