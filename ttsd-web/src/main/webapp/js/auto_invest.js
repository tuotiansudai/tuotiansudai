require(['jquery', 'jquery.ajax.extension', 'autoNumeric', 'lodash', 'commonFun', 'jquery.validate'], function ($) {
    $(function () {
        if ($('#btnAuthority').length) {
            var $btnAuthority = $('#btnAuthority');
            $btnAuthority.click(function () {
                var content = '<div cass="auto-invest"><button id="finishAuthor" class="btn btn-normal">已完成授权</button></div>';
                commonFun.popWindow('自动投标授权', content, {
                    width: '450px'
                });

                $('body').delegate('#finishAuthor', 'click', function () {
                    location.href = '/investor/auto-invest/plan';
                });
            });
        }
        //switch button for plan
        if ($('#planSwitchDom').length) {
            var $planSwitchDom = $('#planSwitchDom'),
                $btnSwitch = $('.switchBtn', $planSwitchDom),
                $radioLabel = $('label.radio', $btnSwitch),
                $projectLimit = $('.projectLimit', $planSwitchDom),
                $radio = $('input[type="radio"]', $planSwitchDom),
                //$checkbox=$('input[type="checkbox"]',$planSwitchDom),
                //$checkboxLabel=$('label.checkbox',$planSwitchDom),
                $saveInvestPlan = $('#saveInvestPlan'),
                $signPlanForm = $("#signPlanForm");
            //init radio and checkbox
            commonFun.initRadio($radio, $radioLabel);
            // commonFun.checkBoxInit($checkbox,$checkboxLabel);

            if($('#plan-close').is(":checked")){
                $planSwitchDom.find('dl').first().show().siblings().hide();
            }

            $('input.autoNumeric').autoNumeric('init');

            $projectLimit.each(function () {
                var $this = $(this);
                var selectedPeriods = parseInt($this.data('value'));
                if (!isNaN(selectedPeriods)) {
                    $this.find('span').each(function () {
                        var $item = $(this);
                        var period = parseInt($item.data('value'));
                        if ((period & selectedPeriods) == period) {
                            $item.addClass('active');
                        }
                    });
                }
            });

            $radioLabel.click(function (index) {
                var $this = $(this),
                    value = $this.prev('input').val();
                if (value == 1) {
                    $planSwitchDom.find('dl').show().siblings().show();
                }
                else {
                    $.ajax({
                        url: '/investor/auto-invest/turn-off',
                        type: 'POST',
                        dataType: 'json',
                        contentType: 'application/json; charset=UTF-8'
                    }).done(function (data) {
                        $planSwitchDom.find('dl').first().show().siblings().hide();
                    }).fail(function (data) {
                        console.log(data);
                    });
                }

            });

            /* get Project duration */
            var getDurationObj = function () {
                var limitObj = [], sum;
                $projectLimit.find('span.active').each(function () {
                    var $this = $(this);
                    limitObj.push($this.data('value'));
                });
                sum = _.sum(limitObj);
                return sum;
            };
            var checkOption = function () {
                var limitNum = getDurationObj(),
                    serialArr = $('input.autoNumeric', $signPlanForm).serializeArray(),
                    //isChecked=$checkbox.is(':checked'),
                    valObj = _.map(serialArr, 'value');

                if (limitNum > 0 && !_.isEmpty(valObj[0]) && !_.isEmpty(valObj[1]) && !_.isEmpty(valObj[2])) {
                    $saveInvestPlan.prop('disabled', false).addClass('btn-normal');
                }
                else {
                    $saveInvestPlan.prop('disabled', true).removeClass('btn-normal');
                }
            };
            //select project limit
            $projectLimit.find('span').click(function () {
                var $this = $(this)
                $this.toggleClass('active');
                checkOption();
            });

            $('#checkOption').click(function(){
                checkOption();
            });

            /* mouse leave input, trigger it*/
            $('input', $signPlanForm).bind('keyup', function () {
                checkOption();
            });

            checkOption();

            $saveInvestPlan.click(function () {
                var requestData = {
                    "minInvestAmount": $('input[name="minInvestAmount"]').autoNumeric('get'),
                    "maxInvestAmount": $('input[name="maxInvestAmount"]').autoNumeric('get'),
                    "retentionAmount": $('input[name="retentionAmount"]').autoNumeric('get'),
                    "autoInvestPeriods": getDurationObj()
                };
                if(parseInt(requestData.maxInvestAmount) < parseInt(requestData.minInvestAmount)){
                    alert("投资金额范围输入有误");
                    return false;
                }
                $.ajax({
                    url: '/investor/auto-invest/turn-on',
                    data: JSON.stringify(requestData),
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=UTF-8'
                }).done(function (data) {
                    if (data.data.status) {
                        location.href = 'plan-detail';
                    } else {
                        // fail
                    }
                }).fail(function (data) {
                    console.log(data);
                });

            });
        }

        if ($('#editSetting').length) {
            $('#editSetting').click(function () {
                var url = location.href.replace(/plan-detail/, 'plan');
                location.href = url;
            });
        }


    });
});