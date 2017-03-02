let commonFun=require('publicJs/commonFun');

function createElement(element,errorMsg) {
    if(element && element.nextElementSibling) {
        element.nextElementSibling.innerHTML=errorMsg;
        return;
    }
    var span=document.createElement("span");
    span.className="error";
    span.innerHTML=errorMsg;
    element && element.parentElement.appendChild(span);
}
function removeElement(element) {
    (element && element.nextElementSibling && globalFun.hasClass(element.nextElementSibling,'error')) && element.parentElement.removeChild(element.nextElementSibling);
}
/*******策略对象********/

var isHaveError ={
    yes(errorMsg,showErrorAfter) {
        globalFun.addClass(this,'error');
        showErrorAfter && createElement(this,errorMsg);
    },
    no(showErrorAfter) {
        globalFun.removeClass(this,'error');
        globalFun.addClass(this,'valid');
        showErrorAfter && removeElement(this);
    }
};

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
            that=this,
            _arguments=arguments;
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
                isHaveError.yes.apply(that,_arguments);
            }
            else {
                getResult='';
                isHaveError.no.apply(that,_arguments);
            }
        });
        return getResult;
    },
    isMobileExist:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this,
            _arguments=arguments;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:'/register/user/mobile/'+this.value+'/is-exist'
        },function(response) {
            if(response.data.status) {
                // 如果为true说明手机已存在或已注册
                getResult=errorMsg;
                isHaveError.yes.apply(that,_arguments);
            }
            else {
                getResult='';
                isHaveError.no.apply(that,_arguments);
            }
        });
        return getResult;
    },
    isEmailExist:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this,
            _arguments=arguments;
        commonFun.useAjax({
            type:'GET',
            async: false,
            url:'/personal-info/email/'+this.value+'/is-exist'
        },function(response) {
            if(response.data.status) {
                // 邮箱已存在
                getResult=errorMsg;
                isHaveError.yes.apply(that,_arguments);
            }
            else {
                getResult='';
                isHaveError.no.apply(that,_arguments);
            }
        });
        return getResult;
    },
    isCaptchaVerify:function(errorMsg,showErrorAfter) {
        var getResult='',
            that=this,
            _arguments=arguments;
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
            if(response.data.status) {
                // 如果为true说明验证码正确
                getResult='';
                isHaveError.no.apply(that,_arguments);
            }
            else {
                getResult=errorMsg;
                isHaveError.yes.apply(that,_arguments);
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

exports.ValidatorForm = ValidatorForm;
exports.isHaveError = isHaveError;


