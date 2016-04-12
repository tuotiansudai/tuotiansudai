/**
 * Created by zhaoshuai on 2015/7/27.
 */
$(function () {
    var aBtn = $('.btn a');
    var aDiv = $('.wrap-tab');

    for (var i = 0; i < aBtn.length; i++) {
        aBtn[i].index = i;
        aBtn[i].onclick = function () {
            for (var i = 0; i < aBtn.length; i++) {
                aBtn[i].className = '';
                aDiv[i].style.display = 'none';
            }
            this.className = 'wrap-active';
            aDiv[this.index].style.display = 'block';
        }
    }
});
