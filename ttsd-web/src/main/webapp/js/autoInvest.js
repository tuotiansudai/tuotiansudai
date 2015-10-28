/**
 * Created by CBJ on 2015/10/19.
 */
require(['jquery', 'csrf', 'autoNumeric','lodash','commonFun'], function ($) {
    $(function () {

        if($('#btnAuthority').length) {
            var $btnAuthority=$('#btnAuthority');
            $btnAuthority.click(function() {
                var content='<div cass="auto-invest"><button id="finishAuthor" class="btn btn-normal">已完成授权</button></div>';
                commonFun.popWindow('自动投标授权',content,{
                    width:'450px'
                });

                $('body').delegate('#finishAuthor','click',function() {
                    location.href='http://localhost:8080/investor/auto-invest/plan';
                });

            });
        }
        //switch button for plan
        if($('#planSwitchDom').length) {
            var $planSwitchDom=$('#planSwitchDom'),
                $btnSwitch=$('.switchBtn',$planSwitchDom),
                $radioLabel=$('label.radio',$btnSwitch),
                $projectLimit=$('.projectLimit',$planSwitchDom),
                $radio=$('input[type="radio"]',$planSwitchDom),
                //$checkbox=$('input[type="checkbox"]',$planSwitchDom),
                //$checkboxLabel=$('label.checkbox',$planSwitchDom),
                $saveInvestPlan=$('#saveInvestPlan'),
                $signPlanForm=$("#signPlanForm");
            //init radio and checkbox
            commonFun.initRadio($radio,$radioLabel);
           // commonFun.checkBoxInit($checkbox,$checkboxLabel);

            $radioLabel.click(function(index) {
                var $this=$(this),
                    value=$this.prev('input').val();
                if(value==1) {
                    $planSwitchDom.find('dl').show().siblings().show();
                }
                else {
                    $planSwitchDom.find('dl').first().show().siblings().hide();
                    //close event
                }

            });

            /* get Project duration */
            var getDurationObj=function() {
                var limitObj=[],sum;
                $projectLimit.find('span.active').each(function() {
                    var $this=$(this);
                    limitObj.push($this.attr('value'));
                });
                sum= _.sum(limitObj);
                return sum;
            }
            var checkOption=function() {
                var limitNum=getDurationObj(),
                    serialArr=$('input[type="number"]',$signPlanForm).serializeArray(),
                    //isChecked=$checkbox.is(':checked'),
                    valObj=_.map(serialArr, 'value');

                if(limitNum>0 && !_.isEmpty(valObj[0]) && !_.isEmpty(valObj[1]) && !_.isEmpty(valObj[2])) {
                    $saveInvestPlan.prop('disabled',false);
                }
                else {
                    $saveInvestPlan.prop('disabled',true);
                }

            }
            //select project limit
            $projectLimit.find('span').click(function() {
                var $this=$(this)
                $this.toggleClass('active');
                checkOption();
            });

            /* mouse leave input, trigger it*/
            $('input',$signPlanForm).bind('keyup',function() {
                checkOption();
            });

            $saveInvestPlan.click(function() {
                var val=$signPlanForm.serialize(),
                valObj=commonFun.parseURL('htt://test?'+val).params,
                    planKind=valObj.planKind,
                    investMin=valObj.investMin,
                    investMax=valObj.investMax,
                    ReservedAmount=valObj.ReservedAmount,
                    agreement=valObj.agreement,
                    duration=getDurationObj();

                var getParam={"status":planKind,
                    "min":investMin,
                    "max":investMax,
                    "price":ReservedAmount,
                    "duration":duration,
                    "agreement":agreement};
                $.ajax({
                    url: url,
                    data:getParam,
                    type: 'POST',
                    dataType: 'json'
                }).done(function (data) {
                    //var url=location.href.replace(/plan/,'plan-detail');
                    //location.href=url;
                });

            });
        }

        if($('#editSetting').length) {
            $('#editSetting').click(function() {
                var url=location.href.replace(/plan-detail/,'plan');
                location.href=url;
            });
        }


    });
});