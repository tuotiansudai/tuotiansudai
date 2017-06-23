require('webStyle/investment/day_turn_in.scss');


let $dayLoanDetailContent=$('#dayLoanDetailContent');

$dayLoanDetailContent.find('.intro-list dt').on('click',  function(event) {
    event.preventDefault();
    let $self=$(this),
        parent=$self.parent('.intro-list');
    parent.hasClass('active')?parent.removeClass('active'):parent.addClass('active');
});