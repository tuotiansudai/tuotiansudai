define(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function($, layer) {

	(function() {

		$('#serviceLayer').on('click', function(event) {
			event.preventDefault();
			layer.open({
				type: 1,
				title: '安心签服务协议',
				area: ['950px', '600px'],
				shadeClose: true,
				move: false,
				scrollbar: true,
				skin: 'register-skin',
				content: $('#serviceBox')
			});
		});
		$('#privacyLayer').on('click', function(event) {
			event.preventDefault();
			layer.open({
				type: 1,
				title: '隐私条款',
				area: ['950px', '600px'],
				shadeClose: true,
				move: false,
				scrollbar: true,
				skin: 'register-skin',
				content: $('#privacyBox')
			});
		});
		$('#numberLayer').on('click', function(event) {
			event.preventDefault();
			layer.open({
				type: 1,
				title: 'CFCA数字证书服务协议',
				area: ['950px', '600px'],
				shadeClose: true,
				move: false,
				scrollbar: true,
				skin: 'register-skin',
				content: $('#numberBox')
			});
		});

	})();

});