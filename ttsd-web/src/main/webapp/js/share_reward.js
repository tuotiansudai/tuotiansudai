require(['jquery','qrcode'], function($) {
    $(function() {
    	if($("#rewardCode").length > 0){
	    	var str = toUtf8($("#rewardCode").attr('data-recommend'));
			$("#code").qrcode({
				render: "table",
				width: 90,
				height:90,
				text: str
			});
	    	function toUtf8(str) {   
			    var out, i, len, c;   
			    out = "";   
			    len = str.length;   
			    for(i = 0; i < len; i++) {   
			    	c = str.charCodeAt(i);   
			    	if ((c >= 0x0001) && (c <= 0x007F)) {   
			        	out += str.charAt(i);   
			    	} else if (c > 0x07FF) {   
			        	out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));   
			        	out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));   
			        	out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));   
			    	} else {   
			        	out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));   
			        	out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));   
			    	}   
			    }   
			    return out;   
			} 
    	}
	
    });
});