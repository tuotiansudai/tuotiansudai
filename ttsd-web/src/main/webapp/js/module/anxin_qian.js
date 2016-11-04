define(['jquery', 'layerWrapper', 'jquery.ajax.extension'], function($, layer) {

	(function() {

		$('.service-layer').on('click', function(event) {
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
		$('.privacy-layer').on('click', function(event) {
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
		$('.number-layer').on('click', function(event) {
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