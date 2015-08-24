/**
 * Created by belen on 15/8/21.
 */
$(function () {
    var _html = '';
    var data = '';
    //初始化校验
    $(".jq-form").Validform();
    //初始化数据
    $.get(API_SELECT, function (data) {
        //data = data;
        _html = template('upload', data);
    });

    function initSelect(){
        var _selectAll = $('.jq-form select');
        var _selectOption = $('select option');
        _selectAll.each(function(m){
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


    // 渲染页面 slect ajax
    var ajaxGet = function (url, ele) {
        var url = url;
        var ele = ele;
        $.get(url, function (res) {
            var ret = template('select', res);
            ele.siblings('.select-box').children().remove();
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
                    if (pix == 'month') {
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

    //$('.selectpicker').

    // 循环上传图片分配对应位置
    var uploadFile = []; //存放上传资料
    var indexPic = function () {
        var _parent = $('.upload-box');
        var formGroup = _parent.find('.form-group');
        formGroup.each(function (index) {
            var str = [];
            var obj = {};
            var arr = formGroup.eq(index).find('.jq-src');
            obj.titleId = index;
            if (formGroup.eq(index).find('.file-preview-frame').index()) {
                arr.val('');
                obj.applyMetarialUrl = '';
                console.log(index + ':' + arr.val());
            } else {
                formGroup.eq(index).find('.file-preview-frame').each(function (i) {
                    var _img = formGroup.eq(index).find('.file-preview-frame').eq(i).find('img').attr('title');
                    str.push(_img);
                    arr.val(str);
                    obj.applyMetarialUrl = str;
                    console.log(index + ':' + arr.val())
                });
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


    //提交表单
    $('.jq-btn-form').click(function () {
        indexPic();
        var dataForm = JSON.stringify({
            "projectName": $('.jq-user').val(),
            "agentLoginName": $('#tags_1').val(),
            "loanLoginName": $('#tags').val(),
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
            "fundraisingStartTime": $('.jq-star-date').val() + ':00',//筹款启动时间类型为：yyyy-MM-dd HH:mm:ss
            "fundraisingEndTime": $('.jq-end-date').val() + ':00',//筹款截止时间类型为：yyyy-MM-dd HH:mm:ss
            "showOnHome": $('.jq-index').val(), //1选中
            "loanAmount": $('.jq-pay').val(),
            "loanTitles": uploadFile
        });
        console.log(dataForm)
        $.post(API_FORM, dataForm);
    })
});