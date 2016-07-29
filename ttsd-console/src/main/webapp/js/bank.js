require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf','autoNumeric'], function ($) {

    $(function () {

        var $errorDom = $('.form-error'),
            $bankForm = $('.bank-form'),
            $submitBtn = $('#btnSave'),
            $backBtn = $('#btnBack'),
            boolFlag = false;

        $backBtn.on('click',function(){
            window.location.href = '/finance-manage/bank-list';
        });

        //金额保留小数点后2位
        var rep = /^\d+$/;
        var rep_point = /^([0-9]+\.[0-9]{2})[0-9]*$/;
        var rep_point1 = /^[0-9]+\.[0-9]$/;
        $('.singleAmount').blur(function () {
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

        $('.singleDayAmount').blur(function () {
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

        function showErrorMessage(msg, obj) {
            currentErrorObj = obj;
            var html = '';
            html += '<div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">';
            html += '<button type="button" class="close" data-dismiss="alert" aria-label="Close">';
            html += '<span aria-hidden="true">&times;</span>';
            html += '</button>';
            html += '<span class="txt">发布失败：' + msg + '</span>';
            html += '</div>';
            $errorDom.append(html);
        }

        $(".bank-form").Validform({
            btnSubmit: '#btnSave',
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            ignoreHidden:true,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    boolFlag = false;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function(curform) {
                $errorDom.html('');
            },
            callback: function(form) {
                boolFlag = true;
                return false;
            }
        });

        $submitBtn.on('click', function(event) {
            event.preventDefault();
            var $self = $(this);
            var singleAmount = $("#singleAmount").val();
            var singleDayAmount = $("#singleDayAmount").val();

            if (parseFloat(singleAmount) > parseFloat(singleDayAmount)) {
                showErrorMessage('单笔交易金额不能大于单日交易金额');
                return false;
            }
            if (boolFlag) {
                if (confirm("确认发布吗?")) {
                    $self.attr('disabled', 'disabled');
                    $bankForm[0].target='';
                    $bankForm[0].action = "/finance-manage/edit";
                    $bankForm[0].submit();
                }
            }
        });

    });

});