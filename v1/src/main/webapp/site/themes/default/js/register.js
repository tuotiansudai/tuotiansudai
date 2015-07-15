$(function(){
	var oMask=$('.mask_phone_modify');
	var oMain=$('.main_phone_modify');
	var oClose=$('.close');
	var oLoginBtn=$('.login_btn_next_step');
	var clientH = $(window).height();
	$('.mask_phone_modify').css('height', clientH);
	oLoginBtn.on('click',function(){
		var imageCaptcha = $('#form\\:image_captcha').val();
		var mobileNumber = $('#form\\:mobileNumber').val();
		var username = $('#form\\:username').val();
		var pass = $('#form\\:pass').val();
		var repass = $('#form\\:repass').val();

		if(username === ''){
			alert('-----1');
			return false;
		}
		if(pass === ''){
			return false;
		}
		if(repass === ''){
			return false;
		}
		if(imageCaptcha === ''){
			return false;
		}
		if(mobileNumber === ''){
			return false;
		}

		$.ajax({
			url:'/verifyCaptchaServlet',
			type:'POST',
			data:{image_captcha:imageCaptcha,mobileNumber:mobileNumber},
			dataType:'json',
			success:function(data){
				if(data[0].message == 'sendSmsError'){
					alert('短信验证码发送失败，请您核实手机号码!');
				}else if(data[0].message == 'verifyCaptchaError'){
					alert("验证码输入错误,请您核实输入验证码或者点击图片重新获取验证码!");
				}else if(data[0].message == 'error'){
					alert('验证码不正确,请重新获取验证码!');
				}else{
					oMask.css('display','block');
					oMain.css('display','block');
					$('.phoneNumber').html(mobileNumber);
					timerCountB('form\\:sendAuthCodeBtn');

				}
			},
			error:function(){
				alert('验证码验证失败，请重新获取验证码!');
			}

		});
	});
	oClose.on('click',function(){
		oMask.css('display','none');
		oMain.css('display','none');
		$('.imageCaptchaClass').click();
		$('#form\\:image_captcha').val("");

	});
	$('.changeNumber').on('click',function(){
		oMask.css('display','none');
		oMain.css('display','none');
		$('.imageCaptchaClass').click();
		$('#form\\:image_captcha').val("");

	});


});