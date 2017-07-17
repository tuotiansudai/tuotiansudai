require('webStyle/investment/loan_detail_day.scss');


let $dayLoanDetailContent=$('#dayLoanDetailContent');

$dayLoanDetailContent.find('.intro-list dt').on('click',  function(event) {
    event.preventDefault();
    let $self=$(this),
        parent=$self.parent('.intro-list');
    parent.hasClass('active')?parent.removeClass('active'):parent.addClass('active');
});