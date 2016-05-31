require(['jquery', 'mustache', 'text!/tpl/message-list.mustache', 'pagination', 'layerWrapper', 'jquery.ajax.extension'], function($, Mustache, messageList, pagination, layer) {
	$(function() {
		var activeIndex = $('.filters-list li.active').index(),
			$paginationElement = $('.pagination');

		function loadLoanData(currentPage) { //template data to page and generate pagenumber
			var requestData = {
				status: '',
				index: currentPage || 1
			};
			$paginationElement.loadPagination(requestData, function(data) {

			});
			var html = Mustache.render(messageList);
			$('.list-container .global-message-list.active').html(html);
		};
		loadLoanData();

	});
});
