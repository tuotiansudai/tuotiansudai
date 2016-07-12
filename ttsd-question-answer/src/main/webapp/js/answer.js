//var style2 = require('../style/sass/answer.scss');

var $=require('jquery');

var $answerContainer=$('.answer-container');
var $answerButton=$('.answer-button',$answerContainer);
var $toAnswerBox=$('.to-answer-box',$answerContainer);
$answerButton.find('button').on('click',function(index) {
    $toAnswerBox.toggle();

});