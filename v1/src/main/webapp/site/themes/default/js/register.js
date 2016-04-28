function inputMobileNumber() {
    var oMask = $('.mask_phone_modify');
    var oMain = $('.main_phone_modify');
    var clientH = $(window).height();
    $('.mask_phone_modify').css('height', clientH);
    oMask.css('display', 'block');
    oMain.css('display', 'block');
    var confirmMobileNumber = $('#form\\:confirmMobileNumber').val();
    $('#captchaForm\\:mobileNumber').val(confirmMobileNumber);

    $('.changeNumber').on('click', function () {
        $('.mobileNumberClass').removeClass("mobileNumberClass").addClass("mobileNumberReadOnly");
    });


    disabledMouseWheel();

    timerCountB('captchaForm\\:sendAuthCodeBtn');
}

function closeSendSmsDialog() {
    $('.mask_phone_modify').css('display', 'none');
    $('.main_phone_modify').css('display', 'none');
    $('#form\\:imageCaptcha').val('');
    $('.imageCaptchaClass').click();
    var mobileNumber = $('#captchaForm\\:mobileNumber').val();
    $('#form\\:confirmMobileNumber').val(mobileNumber);
    $('#captchaForm\\:captcha').val('');
    $('.mobileNumberReadOnly').removeClass("mobileNumberReadOnly").addClass("mobileNumberClass");
    addMouseWheel();
}

function disabledMouseWheel() {
    if (document.addEventListener) {
        document.addEventListener('DOMMouseScroll', scrollFunc, false);
    }
    window.onmousewheel = document.onmousewheel = scrollFunc;
}

function addMouseWheel() {
    if (document.addEventListener) {
        document.addEventListener('DOMMouseScroll', scrollFunc, false);
    }
    window.onmousewheel = document.onmousewheel = null;
    document.removeEventListener('DOMMouseScroll', scrollFunc, false)
}

function scrollFunc(evt) {
    evt = evt || window.event;
    if (evt.preventDefault) {
        evt.preventDefault();
        evt.stopPropagation();
    } else {
        evt.cancelBubble = true;
    }
    return false;
}



