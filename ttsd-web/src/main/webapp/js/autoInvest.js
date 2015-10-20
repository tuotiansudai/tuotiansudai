/**
 * Created by CBJ on 2015/10/19.
 */
require(['jquery', 'csrf', 'autoNumeric','commonFun'], function ($) {
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
                $checkbox=$('input[type="checkbox"]',$planSwitchDom),
                $checkboxLabel=$('label.checkbox',$planSwitchDom);
            //init radio and checkbox
            commonFun.initRadio($radio,$radioLabel);
            commonFun.checkBoxInit($checkbox,$checkboxLabel);

            $radioLabel.click(function(index) {
                var $this=$(this),
                    value=$this.prev('input').val();
                if(value==1) {
                    $planSwitchDom.find('dl').show().siblings().show();
                }
                else {
                    $planSwitchDom.find('dl').first().show().siblings().hide();
                }

            });
            //select project limit
            $projectLimit.find('span').click(function() {
                var $this=$(this);
                $this.toggleClass('active');
            });
        }

    });
});