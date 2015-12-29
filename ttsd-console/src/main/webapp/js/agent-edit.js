require(['jquery', 'bootstrap','Validform','Validform_Datatype','jquery-ui','csrf'], function ($) {
    $(function () {
        var currentErrorObj = null,
            $errorDom = $('.form-error'), //错误提示节点
            $submitBtn = $('.jq-save'), //提交按钮
            $resetBtn = $('.jq-reset'), //重置按钮
            $currentForm = $('.jq-form');
            var formFlag =false;

        $('.jq-loginName').autocomplete({
            minLength: 3,
            source: function (query, process) {
                //var matchCount = this.options.items;//返回结果集最大数量
                $.get('/user-manage/staff/' + query.term + '/search', function (respData) {
                    return process(respData);
                });
            },
            change: function (event, ui) {
                if (!ui.item) {
                    this.value = '';
                }
            }
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
        var rep_point = /^([0-9]+\.[0-9]{2})[0-9]*$/;
        var rep_point1 = /^[0-9]+\.[0-9]$/;
        $('.jq-rate').blur(function () {
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

        $('.jq-level').blur(function () {
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
        $(".jq-form").Validform({
            btnSubmit: $submitBtn,
            btnReset:$resetBtn,
            tipSweep: true, //表单提交时触发显示
            focusOnError: false,
            tiptype: function(msg, o, cssctl) {
                if (o.type == 3) {
                    var msg = o.obj.attr('errormsg') || msg;
                    showErrorMessage(msg, o.obj);
                }
            },
            beforeCheck: function(curform) {
                $errorDom.html('');
                var rate = parseFloat($('.jq-rate', curform).val());
                var level = parseInt($('.jq-level', curform).val())
                if (level <= 0) {
                    showErrorMessage('代理层级应大于0!', $('.jq-rate', curform));
                    return false;
                }
                if (rate <= 0) {
                    showErrorMessage('收益比例应大于0!', $('.jq-level', curform));
                    return false;
                }
            },
            callback: function(form) {
                formFlag = true;
                return false;
            }
        });

        $submitBtn.on('click', function(event) {
            event.preventDefault();
            var $self = $(this);
            if (formFlag) {
                $self.attr('disabled', 'disabled');
                $currentForm[0].submit();
            }
        });


        //关闭警告提示
        $('body').on('click', '.form-error', function () {
            $submitBtn.removeAttr('disabled');
            if (!!currentErrorObj) {
                currentErrorObj.focus();
            }
        });

    });
})