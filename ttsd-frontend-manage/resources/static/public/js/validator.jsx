let commonFun=require('publicJs/commonFun');

/*******策略对象********/
var isHaveError ={
    yes(errorMsg,showErrorAfter) {
        globalFun.addClass(this,'error');
        showErrorAfter && commonFun.createElement(this,errorMsg);
    },
    no(showErrorAfter) {
        globalFun.removeClass(this,'error');
        globalFun.addClass(this,'valid');
        showErrorAfter && commonFun.removeElement(this);
    }
}
var strategies = {
    isNonEmpty: function(errorMsg,showErrorAfter) {
        if (this.value === '') {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    minValue: function(errorMsg,value,showErrorAfter) {
        if (Number(this.value) < Number(value)) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    maxValue: function(errorMsg,value,showErrorAfter) {
        if (Number(this.value) > Number(value)) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    minLength: function(errorMsg,length,showErrorAfter) {
        if (this.value.length < Number(length)) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    maxLength: function(errorMsg,length,showErrorAfter) {
        if (this.value.length > Number(length)) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    isNumber:function(errorMsg,length,showErrorAfter) {
        if(length) {
            var reg=new RegExp('^\\d{'+length+'}$','g');
            if(reg.test(this.value)) {
                isHaveError.no.apply(this,arguments);
            }
            else {
                isHaveError.yes.apply(this,arguments);
                return errorMsg;
            }
        }
        else {
            //判断是非为数字，无需固定判断长度
           if(/^\d+[.]{0,1}\d*$/.test(this.value)) {
               isHaveError.no.apply(this,arguments);
           }
           else {
               isHaveError.yes.apply(this,arguments);
               return errorMsg;
           }

        }
    },
    isChinese:function (errorMsg,showErrorAfter) {
        if(/^[\u4E00-\u9FA5]+$/.test(this.value)) {
            isHaveError.no.apply(this,arguments);
        }
        else {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
    },
    equalTo:function (errorMsg,dom,showErrorAfter) {
        let oldVal=$(dom).val();
        if(oldVal!=this.value) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    equalLength:function(errorMsg,length,showErrorAfter) {
        if (this.value.length!=Number(length)) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    checkPassword:function(errorMsg,showErrorAfter) {
        var regBool=/^(?=.*[^\d])(.{6,20})$/.test(this.value);
        if (!regBool) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    isMobile: function(errorMsg,showErrorAfter) {
        //只验证手机号不验证是非为空
        if(this.value=='') {
            return '';
        }
        if (!/(^1[0-9]{10}$)/.test(this.value)) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    isEmail:function(errorMsg,showErrorAfter) {
        //只验证邮箱不验证是非为空
        if(this.value=='') {
            return '';
        }
        if(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(this.value)) {
            isHaveError.no.apply(this,arguments);
        } else {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
    },
    identityValid:function(errorMsg,showErrorAfter) {
        //验证身份证号
        var cardValid=commonFun.IdentityCodeValid(this.value);
        if(!cardValid) {
            isHaveError.yes.apply(this,arguments);
            return errorMsg;
        }
        else {
            isHaveError.no.apply(this,arguments);
        }
    },
    ageValid:function(errorMsg,showErrorAfter) {
        //验证年龄是否满18
        var cardValid=commonFun.IdentityCodeValid(this.value);
        if(cardValid) {
            var ageValid=commonFun.checkedAge(this.value);
            if(!ageValid) {
                isHaveError.yes.apply(this,arguments);
                return errorMsg;
            }
            else {
                isHaveError.no.apply(this,arguments);
            }
        }
    },
    //身份证号是否存在
    isCardExist:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this;
        if(this.value.length!=18) {
            return;
        }
        commonFun.useAjax({
            type:'GET',
            async: false,
            url: '/register/account/identity-number/'+this.value+'/is-exist'
        },function(response) {
            if(response.data.status) {
                //身份证号已存在
                getResult=errorMsg;
                isHaveError.yes.apply(that,arguments);
            }
            else {
                getResult='';
                isHaveError.no.apply(that,arguments);
            }
        });
        return getResult;
    },
    isMobileExist:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:'/register/user/mobile/'+this.value+'/is-exist'
        },function(response) {
            if(response.data.status) {
                // 如果为true说明手机已存在或已注册
                getResult=errorMsg;
                isHaveError.yes.apply(that,arguments);
            }
            else {
                getResult='';
                isHaveError.no.apply(that,arguments);
            }
        });
        return getResult;
    },
    isEmailExist:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:'/personal-info/email/'+this.value+'/is-exist'
        },function(response) {
            if(response.data.status) {
                // 邮箱已存在
                getResult=errorMsg;
                isHaveError.yes.apply(that,arguments);
            }
            else {
                getResult='';
                isHaveError.no.apply(that,arguments);
            }
        });
        return getResult;
    },
    //修改密码的时候验证原密码是否存在
    isNotExistPassword:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:'/personal-info/password/'+this.value+'/is-exist'
        },function(response) {
            if(response.data.status) {
                // 如果为true说明密码存在有效
                getResult='';
                isHaveError.no.apply(that,arguments);
            }
            else {
                getResult=errorMsg;
                isHaveError.yes.apply(that,arguments);
            }
            return getResult;
        });
    },
    isMobileRetrieveExist1:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:'/mobile-retrieve-password/mobile/'+this.value+'/is-exist?random=' + new Date().getTime()
        },function(response) {
            if(response.data.status) {
                // 如果为true说明手机已存在
                getResult='';
                isHaveError.no.apply(that,arguments);
            }
            else {
                getResult=errorMsg;
                isHaveError.yes.apply(that,arguments);
            }
        });
        return getResult;
    },
    isCaptchaVerify:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this;
        let retrieveForm=globalFun.$('#retrieveForm');
        var _phone = retrieveForm.mobile.value,
            _captcha=retrieveForm.captcha.value;
        //先判断手机号格式是否正确
        if(globalFun.hasClass(retrieveForm.mobile,'error')) {
            return;
        }
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:`/mobile-retrieve-password/mobile/${_phone}/captcha/${_captcha}/verify?random=` + new Date().getTime()
        },function(response) {
            if(!response.data.status) {
                // 如果为true说明验证码不正确
                getResult='';
                isHaveError.no.apply(that,arguments);
            }
            else {
                getResult=errorMsg;
                isHaveError.yes.apply(that,arguments);
            }
        });
        return getResult;
    },
    //免密投资验证图形码
    isNoPasswordCaptchaVerify:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this;
        let turnOffForm=globalFun.$('#turnOffNoPasswordInvestForm');
        var _phone = turnOffForm.mobile.value,
            _captcha=turnOffForm.captcha.value;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:`/no-password-invest/mobile/${_phone}/captcha/${_captcha}/verify`
        },function(response) {
            if(!response.data.status) {
                // 如果为true说明验证码不正确
                getResult='';
                isHaveError.no.apply(that,arguments);
            }
            else {
                getResult=errorMsg;
                isHaveError.yes.apply(that,arguments);
            }
        });
        return getResult;
    },

    //推荐人是非存在
    isReferrerExist:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this;
        //只验证推荐人是否存在，不验证是否为空
        if(this.value=='') {
            getResult='';
            isHaveError.no.apply(that,arguments);
            return '';
        }
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:'/register/user/referrer/'+this.value+'/is-exist'
        },function(response) {
            if(response.data.status) {
                // 如果为true说明推荐人存在
                getResult='';
                isHaveError.no.apply(that,arguments);
            }
            else {
                getResult=errorMsg;
                isHaveError.yes.apply(that,arguments);
            }
        });
        return getResult;
    }

};
// *****Validator验证类*******

function ValidatorForm(cache,checkOption) {
     this.cache = [];
     this.checkOption = {};
     this.newStrategy=function(dom,name,callback) {
         strategies[name]=function(errorMsg,showErrorAfter) {
             if(callback) {
                 return callback.apply(dom,arguments);
             }
         }
     }
     this.add=function(dom, rules,errorAfter) {
         var self = this;
         self.checkOption[dom.name]=[];
         for (var i = 0, rule; rule = rules[i++];) {
             self.checkOption[dom.name].push(rule);
         }

         self.cache.push(function(thisDom) {
             var domName=thisDom.name;
             var domOption=self.checkOption[domName];
             if(!domOption) {
                return;
             }
             var len=domOption.length,
                 getErrorMsg;

             for(var j=0;j<len;j++) {
                 var strategy=domOption[j].strategy.split(':').shift();
                 var errorMsg=domOption[j].errorMsg;
                 var optionParams=[];
                 optionParams.push(errorMsg);
                 var secondParam=domOption[j].strategy.split(':')[1];

                 secondParam && optionParams.push(secondParam);
                 errorAfter && optionParams.push(errorAfter);
                 getErrorMsg=strategies[strategy].apply(thisDom,optionParams);

                 if(getErrorMsg) {
                     break;//跳出for循环
                 }
             }
             return getErrorMsg;
         });
     }
     this.start=function(dom) {
         var validatorFunc = this.cache[0];
         var errorMsg=validatorFunc(dom);
         if (errorMsg) {
             return errorMsg;
         }
     }
}

module.exports=ValidatorForm;


