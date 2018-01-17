require('mWebStyle/about_assurance.scss');

let $assurance = $('#assurance'),
    $iconAssurance = $('#iconAssurance',$assurance);

$iconAssurance.on('click',function () {
    location.href='/m'
})