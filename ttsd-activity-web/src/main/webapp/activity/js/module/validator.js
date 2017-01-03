define(['jquery','commonFun'], function ($,commonFun) {

    /*******策略对象********/
    var strategies = {
        isNonEmpty: function(errorMsg) {
            if (this.value === '') {
                defineCls.addClass(this,'error');
                return errorMsg;
            }
            else {
                defineCls.removeClass(this,'error');
            }
        },
        minLength: function(errorMsg,length) {
            if (this.value.length < Number(length)) {
                defineCls.addClass(this,'error');
                return errorMsg;
            }
            else {
                defineCls.removeClass(this,'error');
            }
        },
        maxLength: function(errorMsg,length) {
            if (this.value.length > Number(length)) {
                defineCls.addClass(this,'error');
                return errorMsg;
            }
            else {
                defineCls.removeClass(this,'error');
            }
        },
        isNumber:function(errorMsg,length) {
            if(length) {
                var reg=new RegExp('^\\d{'+length+'}$','g');
                if(reg.test(this.value)) {
                    defineCls.removeClass(this,'error');
                }
                else {
                    defineCls.addClass(this,'error');
                    return errorMsg;
                }
            }
        },
        isChinese:function (errorMsg) {
            if(/^[\u4E00-\u9FA5]+$/.test(this.value)) {
                defineCls.removeClass(this,'error');
            }
            else {
                defineCls.addClass(this,'error');
                return errorMsg;
            }

        },
        equalLength:function(errorMsg,length) {
            if (this.value.length!=Number(length)) {
                defineCls.addClass(this,'error');
                return errorMsg;
            }
            else {
                defineCls.removeClass(this,'error');
            }
        },
        checkPassword:function(errorMsg) {
            var regBool=/^(?=.*[^\d])(.{6,20})$/.test(this.value);
            if (!regBool) {
                defineCls.addClass(this,'error');
                return errorMsg;
            }
            else {
                defineCls.removeClass(this,'error');
            }
        },
        isMobile: function(errorMsg) {
            if (!/(^1[0-9]{10}$)/.test(this.value)) {
                defineCls.addClass(this,'error');
                return errorMsg;
            }
            else {
                defineCls.removeClass(this,'error');
            }
        },
        identityValid:function(errorMsg) {
            //验证身份证号
            var cardValid=commonFun.IdentityCodeValid(this.value);
            if(!cardValid) {
                defineCls.addClass(this,'error');
                return errorMsg;
            }
            else {
                defineCls.removeClass(this,'error');
            }
        },
        ageValid:function(errorMsg) {
            //验证年龄是否满18
            var cardValid=commonFun.IdentityCodeValid(this.value);
            if(cardValid) {
                var ageValid=commonFun.checkedAge(this.value);
                if(!ageValid) {
                    defineCls.addClass(this,'error');
                    return errorMsg;
                }
                else {
                    defineCls.removeClass(this,'error');
                }
            }
        },
        isCardExist:function(errorMsg) {
            var getResult='',
                that=this;
            if(this.value.length!=18) {
                return;
            }
            commonFun.useAjax({
                type:'GET',
                async: false,
                url:'/authentication/identityNumber/'+this.value+'/is-exist'
            },function(response) {
                if(response.data.status) {
                    // 身份证已存在
                    getResult=errorMsg;
                    defineCls.addClass(that,'error');
                }
                else {
                    getResult='';
                    defineCls.removeClass(that,'error');
                }
            });
            return getResult;
        },
        isMobileExist:function(errorMsg) {
            var getResult='',
                that=this;
            useAjax({
                type:'GET',
                async: false,
                url:'/register/mobile/'+this.value+'/is-register'
            },function(response) {
                if(response.data.status) {
                    // 如果为true说明手机已存在或已注册
                    getResult=errorMsg;
                    defineCls.addClass(that,'error');
                }
                else {
                    getResult='';
                    defineCls.removeClass(that,'error');
                }
            });
            return getResult;
        },
        isMobileExistRetrieve:function(errorMsg) {
            var getResult='',
                that=this;
            useAjax({
                type:'GET',
                async: false,
                url:'/mobile-retrieve-password/mobile/'+this.value+'/is-exist'
            },function(response) {
                if(!response.data.status) {
                    // 如果为true说明手机已存在或已注册
                    getResult=errorMsg;
                    defineCls.addClass(that,'error');
                }
                else {
                    getResult='';
                    defineCls.removeClass(that,'error');
                }
            });
            return getResult;
        }

    };
// *****Validator验证类*******

     function ValidatorForm(cache,checkOption) {
         this.cache = [];
         this.checkOption = {};
         this.newStrategy=function(name,callback) {
             strategies[name]=function(data) {
                 callback && callback(data);
             }
         }
         this.add=function(dom, rules) {
             var self = this;
             self.checkOption[dom.name]=[];
             for (var i = 0, rule; rule = rules[i++];) {
                 self.checkOption[dom.name].push(rule);
             }

             self.cache.push(function(thisDom) {
                 var domName=thisDom.name;
                 var domOption=self.checkOption[domName];
                 var len=domOption.length,
                     getErrorMsg;

                 for(var j=0;j<len;j++) {
                     var strategy=domOption[j].strategy.split(':').shift();
                     var errorMsg=domOption[j].errorMsg;
                     var optionParams=[];
                     optionParams.push(errorMsg);
                     var secondParam=domOption[j].strategy.split(':')[1];
                     if(secondParam) {
                         optionParams.push(secondParam);
                     }
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

});
