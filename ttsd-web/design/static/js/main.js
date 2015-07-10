/**
 * Created by zhaoshuai on 2015/5/7.
 */
require.config({
    baseUrl: "../js",//此处存放文件路径
    paths: {
        "jquery": "libs/jquery-1.10.1.min"//此处存放baseURL路径下要引入的JS文件
        //"Validform":"libs/Validform_v5.3.2_min"
    }
});
require(['jquery'], function ($) {
    //warning_mask浏览器版本过低提示:
    $(function () {
        function isIE() {
            if ($.browse) {
                return true;
            } else {
                return false;
            }
        };
        function IEVersion() {
            var rv = -1;
            if (navigator.appName == 'Microsoft Internet Explorer') {//IE浏览器
                var ua = navigator.userAgent;
                var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
                if (re.exec(ua) != null)
                    rv = parseFloat(RegExp.$1);
                return rv;
            }
        };
        var clientH_mask = $(window).height();
        if (IEVersion() < 9) {
            $(function () {
                $('.warning_mask').css('display', 'block').css('height', clientH_mask);
                $('.warning').css('display', 'block');
            });
        }
    });
    //nav--homepage
    $(function () {
        var timer;
        $("#nav_mouseover").hover(function () {
            clearTimeout(timer);
            timer = setTimeout(function () {
                $('.subnav').stop().slideDown(300);
            }, 0);
        }, function () {
            clearTimeout(timer);
            timer = setTimeout(function () {
                $('.subnav').stop().slideUp(300);
            }, 300)
        });
    });
    //mask/tip/倒计时--Email
    $(function () {
        $(function () {
            //tip
            var oTxt = $('#text1');
            var oTip = $('#tip');
            oTxt.focus(function () {
                oTip.css('display', 'none');
            });
            oTxt.blur(function () {
                if (oTxt.val() == '') {
                    oTip.css('display', 'block');
                }
            });
            oTip.on('click', function () {
                oTip.css('display', 'none');
                oTxt.focus();
            });
            //mask
            var clientH = $(window).height();
            $('#mask').css('height', clientH);
            $('#send,.confirm').on('click', function () {

                $('#mask').css('display', 'block');
                $('#email_yz,.bind_card').css('display', 'block');
                $('#toemail').html($('#text1').val());
            });
            $('#close ,.close2, #mask ,.now').on('click', function () {
                $('#mask,.bind_card').css('display', 'none');
                $('#email_yz,.bind_card').css('display', 'none');
            })
            //倒计时
            var count = 30;
            var countdown = setInterval(CountDown, 1000);

            function CountDown() {
                $('#align').html('1.没收到验证邮件？' + count + '<i>秒后重新发送</i>')
                if (count == 0) {
                    clearInterval(countdown);
                    $('#mask').css('display', 'none');
                    $('#email_yz').css('display', 'none');
                }
                count--;
            }
        });
    })
    //money--cash
    $(function () {
        $('#money').blur(function () {
            var Num = $('#money').val();
            if (!$.isNumeric(Num)) {
                $('.em_ts').css('display', 'none');
                $('#span_c').css('display', 'block');
                $('#span_b').css('display', 'none');
            } else {
                $('.em_ts').css('display', 'block');
                $('#span_c').css('display', 'none');
                if (Num < 50) {
                    $('.em_ts').css('display', 'none');
                    $('#span_b').css('display', 'block');
                } else {
                    $('.em_ts').css('display', 'block');
                    $('#span_b').css('display', 'none');
                }
            }

            if (Num === '') {
                $('.em_ts').css('display', 'block');
                $('#span_b').css('display', 'none');
                $('#span_c').css('display', 'none');
            }
            if (Num != '' && Num >= 50 && $.isNumeric(Num)) {
                $('.confirm').css('background', '#ff7a2a').css('pointer-events', 'auto');
            } else {
                $('.confirm').css('background', '#666666').css('pointer-events', 'none')
                //pointer-events:none：css3新属性，鼠标不在监听当前元素的事件。
            }
        });
    });
    //recharge input
    $(function () {
        $('.recharge_cz').on('blur', function () {
            if ($('.recharge_cz').val() === '') {
                $('.recharge_qr').css('background', '#666666');
            } else {
                $('.recharge_qr').css('background', '#ff7a2a');
            }
        });
    });
    //registered 验证：
    $(function () {
        //发送验证码：
        $('.send_vcode').on('click', function () {

           // alert('1');

        });
        //username
        function checkUserName(str) {
            var re = /^(?![^a-zA-Z]+$)(?!\D+$).{5,24}$/;
            if (re.test(str)) {
                $('.step_username em').css({'opacity': 1});
                $('.step_username b').css({'opacity': 0});
                return true;
            }else{
                $('.step_username em').css({'opacity': 0});
                $('.step_username b').css({'opacity': 1});
            }
            return true;
        };
        $('.username').on('input',function(){
            var usertxt = $(this).val();
            $(this).val(usertxt.replace(/^\s+|\s+$/g, ''));
        })
        $('.username').on('blur', function () {
            $('.step_username b').css({'lineHeight': '20px'});
            if ($(this).val() == '') {
                $('.step_username b').css({'opacity': 1, 'lineHeight': '40px'}).html('用户名不能为空！');
            }
            if( $(this).val().length < 5 || $(this).val().length > 25 ){
                $('.step_username em').css('opacity','0');
                $('.step_username b').css({'opacity': 1, 'lineHeight': '20px'}).html('用户名只允许包含字母和数字，长度为5-25位，请勿使用手机号！');
            } else {
                checkUserName($(this).val());
                $('.step_username b').html('用户名只允许包含字母和数字，长度为5-25位，请勿使用手机号！');
            }
        }).on('change',function(){
            checkUserName($(this).val());
        });
        //Mobil 验证

        function checkMobile(str) {
            var re = /^1[1-9]{10}$/;
            if ($('.phone').val().length < 11 && $('.phone').val().length > 0) {
                $('.step_one_phone em').css({'opacity': 0});
                $('.step_one_phone b').css({'opacity': 1,'display':'block'}).html('手机号不能小于11位!');
            } else if ($('.phone').val().length > 11) {
                $('.step_one_phone em').css({'opacity': 0});
                $('.step_one_phone b').css({'opacity': 1,'display':'block'}).html('手机号不能大于11位!');
            } else if (re.test(str)) {
                $('.step_one_phone em').css({'opacity': 0});
                $('.step_one_phone b').css({'opacity': 0});
                return true;
            }
            return false;
        };
        function mastNumber() {
            $(".phone").on('input', function () {
                if (isNaN($('.phone').val())) {
                    $('.step_one_phone b').css({'opacity': 1}).html('只能输入数字！');
                    $('.step_one_phone em').hide();
                    var tmptxt = $(this).val();
                    $(this).val(tmptxt.replace(/\D|^0/g, ''));
                } else {
                    checkMobile($('.phone').val());
                    $('.step_one_phone b').css({'opacity': 0});
                }
            })
        };
        mastNumber();
        $('.phone').on('blur', function () {
            checkMobile($('.phone').val());
            if (!$(this).val()) {
                $('.step_one_phone em').css({'opacity': 0});
                $('.step_one_phone  b').css({'opacity': 1});
            }
        }).on('input',function(){
            var phonetxt = $(this).val();
            $(this).val(phonetxt.replace(/^\s+|\s+$/g, ''));
        }).on('change', function () {
            $('.step_one_phone b').css({'opacity': 1}).html('手机号不能为空！');
            var phone_change = checkMobile($(this).val());
            if (phone_change) {
                $.ajax({
                    url: '/static/jsons/mobile.json?vcode_num=',
                    type: 'GET',
                    dataType: 'json',
                    beforeSend: function () {
                        $('.step_one_phone strong').show();
                    },
                    success: function (response) {
                        $('.step_one_phone strong').hide();
                        $('.step_one_phone em').css({'opacity': 0});
                        $('.step_one_phonee b').css({'opacity': 0});
                        if (response.status === 'success' && response.data.exist) {
                            $('.step_one_phone em').css({'opacity': 0});
                            $('.step_one_phone b').css({'opacity': 1}).html('手机号已存在！');
                            $('.step_one_phone strong').hide()
                        } else if (!response.data.exist) {
                            $('.step_one_phone em').css({'opacity': 1});
                            $('.step_one_phone b').hide();
                            $('.step_one_phone strong').hide();
                        }
                    },
                    error: function () {
                        if (status == 500) {
                            alert('服务器错误！');
                        }
                    }
                });
            }
        });
        //vcode验证码
        function verCode(str) {
            var re = /^\d{6}$/
            if ($('.vcode').val().length < 6 || $('.vcode').val().length > 6) {
                $('.step_two_verCode em').hide();
                $('.step_two_verCode b').css({'opacity': 1}).html('验证码格式错误！');
            } else if (re.test(str)) {
                $('.step_two_verCode em').css({'display': 'block', 'opacity': 1});
                $('.step_two_verCode b').hide();
                $('.step_two_verCode strong').hide();
                return true;
            }
            return false;
        }
        $('.vcode').on('blur', function () {
            if (!$(this).val()) {
                $('.step_two_verCode em').css({'opacity': 0});
                $('.step_two_verCode b').css({'opacity': 1});
            }
        }).on('change', function () {
            var vcode_change = verCode($(this).val());
            if (vcode_change) {
                $.ajax({
                    url: '/static/jsons/vcode.json?vcode_num=',
                    type: 'GET',
                    dataType: 'json',
                    beforeSend: function () {
                        $('.step_two_verCode strong').show();
                    },
                    success: function (response) {
                        $('.step_two_verCode strong').hide();
                        $('.step_two_verCode b').css({'opacity': 0});
                        if (response.status === 'success' && response.data.exist) {
                            $('.step_two_verCode em').css({'opacity': 1});
                            $('.step_two_verCode b').hide();
                            $('.step_two_verCode strong').hide()
                        }
                    },
                    error: function (status) {
                        if (status == 500) {
                            alert('服务器错误！');
                        }
                    }
                });
            }
        }).on('input',function(){
            var vcodetxt = $(this).val();
            $(this).val(vcodetxt.replace(/^\s+|\s+$/g, ''));
        });
        $('.vcode').keyup(function () {
            $('.step_two_verCode b').show();
            $('.step_two_verCode em').hide();
            var txt = $(this).val();
            $(this).val(txt.replace(/\D|^/g, ''));
        })
        //password验证
        function condition() {
            if ($('.password').val().length > 16) {
                $('.step_three_password b').html('密码不能大于16位').css({'opacity': 1});
                $('.step_three_password em').hide();
            }
            if ($('.password').val() == '') {
                $('.step_three_password b').css({'opacity': 1, 'display': 'block'});
                $('.step_three_password em').hide();
            } else if ($('.password').val().length < 6) {
                $('.step_three_password b').html('密码不能小于6位').css({'opacity': 1});
                $('.step_three_password em').hide();
            }
            if ($('.password').val().length >= 6 && $('.password').val().length <= 16) {
                var re = /^(([a-zA-Z]{3,})+([0-9]{3,}))|(([0-9]{3,})+([a-zA-Z]{3,})|(?=[\x21-\x7e]+))+$/;
                if (re.test($('.password').val())) {
                    $('.step_three_password em').css({'opacity': 1, 'display': 'block'});
                    $('.step_three_password b').css({'opacity': 0});
                } else {
                    $('.step_three_password em').css({'opacity': 0});
                    $('.step_three_password b').css({'opacity': 1}).html('验证码必须是数字和字母组合！');
                    return false;
                }
            }
        }
        $('.password').on('blur', function () {
            condition();
        }).on('change', function () {
            condition();
        }).on('input',function(){
            var passwordtxt = $(this).val();
            $(this).val(passwordtxt.replace(/^\s+|\s+$/g, ''));
        });
        //Email验证
        function checkEmail(str) {
            var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
            if (re.test(str)) {
                $('.step_four_email em').css({'opacity': 1, 'display': 'block'});
                $('.step_four_email b').hide();
                return true;
            } else if ($('.email').val() == '') {
                $('.step_four_email em').css({'opacity': 0});
                $('.step_four_email b').css({'opacity': 1});
            } else {
                $('.step_four_email em').css({'opacity': 0});
                $('.step_four_email b').css({'opacity': 1, 'display': 'block'}).html('请检查您的邮箱格式!');
            }
            return false;
        };
        $('.email').on('blur', function () {
            if (!$(this).val()) {
                $('.step_four_email em').css({'opacity': 0});
                $('.step_four_email b').css({'opacity': 1});
            }
        }).on('input',function(){
            var emailtxt = $(this).val();
            $(this).val(emailtxt.replace(/^\s+|\s+$/g, ''));
        }).on('change', function () {
            var validFormat = checkEmail($(this).val());
            if (validFormat) {
                $.ajax({
                    url: '/static/jsons/mobile.json?vcode_num=',
                    type: 'GET',
                    dataType: 'json',
                    beforeSend: function () {
                        //远程调用开始...
                        $('.step_four_email strong').show();
                    },
                    success: function (response) {
                        $('.step_four_email strong').hide();
                        $('.step_four_email em').hide();
                        $('.step_four_email b').css({'opacity': 0});
                        if (response.status === 'success' && response.data.exist) {
                            $('.step_four_email em').css({'opacity': 0});
                            $('.step_four_email b').html('邮箱已经存在!').css({'opacity': 1, 'display': 'block'});
                            $('.step_four_email strong').hide()
                        } else if (!response.data.exist) {
                            $('.step_four_email em').show();
                            $('.step_four_email b').hide();
                        }
                    },
                    error: function (status) {
                        if (status == 500) {
                            alert('服务器错误！');
                        }
                    }
                })
            }
        });
        //IDcard验证
        function checkId(str) {
            var reg = /^[^a-zA-Z]\d{14,17}\w+$/;
            if (reg.test(str)) {
                $('.vcard em').show();
            }
        }
        $('.idcard').on('blur', function () {
            if ($('.idcard').val().length < 15) {
                $('.vcard b').html('身份证号不能小于15位！').show();
                $('.vcard em').css({'opacity': 0});
            } else if ($('.idcard').val().length > 18) {
                $('.vcard b').show().html('身份证号不能大于18位！');
                $('.vcard em').css('opacity', 0);
            } else if ($('.idcard').val().length == 15) {
                $('.vcard em').css({'opacity': 1});
                $('.vcard b').hide();
            } else {
                $('.vcard em').css({'opacity': 1});
                $('.vcard b').hide();
            }
            checkId($('.idcard').val());
        }).on('input',function(){
            var idcardtxt = $(this).val();
            $(this).val(idcardtxt.replace(/^\s+|\s+$/g, ''));
        });
        //推荐用户：
        $('.user').on('change', function () {
            if (!$(this).val()) {
                $('.step_five_user em').css({'opacity': 0});
                $('.step_five_user b').css({'opacity': 1});
            }
        }).on('input',function(){
            var usertxt = $(this).val();
            $(this).val(usertxt.replace(/^\s+|\s+$/g, ''));
        }).on('change', function () {
            $.ajax({
                url: '/static/jsons/mobile.json',
                type: 'GET',
                datatype: 'json',
                timeout: 500,
                beforeSend: function () {
                    $('.step_five_user strong').show();
                },
                success: function (response) {
                    $('.step_five_user strong').hide();
                    if (response.status && response.data.exist) {
                        $('.step_five_user em').css({'opacity': 1, 'display': 'block'});
                        $('.step_five_user b').show();
                        $('.step_five_user strong').hide();
                    } else {
                        $('.step_five_user em').css({'opacity': 0});
                        $('.step_five_user b').html('此用户不存在！').css({'opacity': 1, 'display': 'block'});
                        $('.step_five_user strong').hide();
                    }
                    if ($('.user').val() == '') {
                        $('.step_five_user em').css({'opacity': 0});
                        $('.step_five_user b').css({'opacity': 0});
                        $('.step_five_user strong').hide();
                    }
                },
                error: function (status) {
                    if (status == 500) {
                        alert('服务器错误！');
                    }
                },
                timeout: 500
            });
        });
        //Next step
        $('.registered div').eq(0).show();
        $('.next_reg').css({'background':'#333','color':'#fff','pointer-events':'none'});
        $('.reg_one').find('a').css({'background':'url("../images/register/reg_one_active.png") no-repeat','color':'#fff'});
//        $(document).on('click',function(){
//            if(checkEmail($('.email').val())==true && condition && verCode($('.vcode').val()) && checkMobile($('.phone').val()) &&checkUserName($('.username').val())){
//                alert('1');
//                $('.next_reg').css({'background':'#fee2cb','color':'#f68e3a','pointer-events':'auto'});
//            }
//        });

        var iNow = 0;
        $('.next_reg').on('click', function () {
            if ($('.check_input').prop('checked')) {
                iNow++;
                if (iNow == $('.registered div').length - 1) {
                    iNow = 0;
                }
                $('.registered div').eq(0).hide();
                $('.registered div').eq(1).show();
                $('.reg_one').find('a').css({'background':'url("../images/register/reg_one.png") no-repeat','color':'#fff'});
                $('.reg_two').find('a').css({'background':'url("../images/register/reg_two_active.png") no-repeat','color':'#fff'});
            } else {
                $('.next_re').css('pointer-events', 'none');
                alert('必须同意服务协议才可以继续！');
                return false;
            }
        });

        $(function () {
            var clientH_reg_mask = $(window).height();
            $('.reg_show').on('click', function () {
                $('.reg_mask').css('display', 'block').css('height', clientH_reg_mask);
                $('.areen').show();
            });
            $('.reg_mask').on('click', function () {
                $(this).hide();
                $('.areen').hide();
            });
        });
    })

    //pcenter页面：
    $(function () {
        var pcenter_height = $(window).height();
        $('.pcenter_mask').css('height', pcenter_height);
        $(function () {
            $('.modify_pcenter_a').on('click', function () {
                $('.pcenter_mask').show();
                $('.change_mobile').show();
            });
            $('.binding_pcenter').on('click', function () {
                $('.pcenter_mask').show();
                $('.change_email').show();
            });
            $('.modify_pcenter_b').on('click', function () {
                $('.pcenter_mask').show();
                $('.change_content').show();
            });
            $('.modify_pcenter_c').on('click', function () {
                $('.pcenter_mask').show();
                $('.change_password').show();
            })
            $('.pcenter_mask, .pcenter_close').on('click', function () {
                $('.pcenter_mask').hide();
                $('.change_mobile').hide();
                $('.change_email').hide();
                $('.change_content').hide();
                $('.change_password').hide();
            })
        });
    });
});