require('webStyle/investment/day_turn_out.scss');

let $formOut=$('#formOut'),
	$turnOut=$('#turnOut'),
	$submitBtn=$formOut.find('.submit-item'),
	$infoTip=$formOut.find('.info-item');

$turnOut.on('focusout', function(event) {
	event.preventDefault();
	let $self=$(this),
		selfVal=parseFloat($self.val()),
		limit=parseFloat($self.attr('data-limit')),
		Exp=/^\+?[1-9][0-9]*$/;
	if(Exp.test($(this).val())){
		if(selfVal<=limit && selfVal>0){
			$submitBtn.prop('disabled',false);
			$self.removeClass('error');
			$infoTip.hide();
		}else{
			$submitBtn.prop('disabled',true);
			$self.addClass('error');
			$infoTip.show();
		}
	}else{
		$(this).val('');
	}
});