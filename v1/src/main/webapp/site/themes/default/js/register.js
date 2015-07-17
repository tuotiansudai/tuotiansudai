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
        $('.mobileNumberClass').css({
            'pointer-events': 'auto',
            'border': '1px #d9d9d9 solid',
            'border-radius': '5',
            'background': '#c00;'
        })
    });


    function disabledMouseWheel() {
        if (document.addEventListener) {
            document.addEventListener('DOMMouseScroll', scrollFunc, false);
        }
        window.onmousewheel = document.onmousewheel = scrollFunc;//IE/Opera/Chrome
    }

    function scrollFunc(evt) {
        evt = evt || window.event;
        if (evt.preventDefault) {
            // Firefox
            evt.preventDefault();
            evt.stopPropagation();
        } else {
            // IE
            evt.cancelBubble = true;
            evt.returnValue = false;
        }
        return false;
    }

    disabledMouseWheel();

    timerCountB('captchaForm\\:sendAuthCodeBtn');
}



