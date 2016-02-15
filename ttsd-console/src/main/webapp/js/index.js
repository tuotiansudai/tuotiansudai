require(['jquery'], function ($) {
    $(function() {
    	$('.table .btn-danger').on('click',function(event) {
    		event.preventDefault();
    		$.ajax({
    			url: '/path/to/file',
    			type: 'POST',
    			dataType: 'json',
    			data: {param1: 'value1'},
    		})
    		.done(function() {
    			console.log("success");
    		})
    		.fail(function() {
    			console.log("error");
    		});
    		
    	});
    });
});
