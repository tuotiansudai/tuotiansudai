(function(window){
	var timer = function(id,config){
		var a = function(){
			this.config = config || {};
			this.dom = $("#"+id);
			this.count = this.config.count || 10;
			this.animate = this.config.animate || true;
			this.initText = this.config.initText || this.dom.val() || this.dom.html();
			this.initTextBefore = this.config.initTextBefore || this.initText+" ";
			this.initTextAfter = this.config.initTextAfter || "S";
			this.initHref = this.dom.attr("href");
			this.isInput = this.dom.is("input");
			this.isA = this.dom.is("a");
//			this.dom.click(this.begin.bind(this));
		};
		a.prototype = {
			begin:function(){
				if(this.isA && this.dom.attr("disabled")=="disabled"){
					//this.dom.attr("href","javascript:void(0)");
					//this.dom.removeAttr("href");
					return false;
				}
				this.dom.attr("disabled","disabled");
				this.count++;
				this.next();
				this.interval = setInterval(this.next.bind(this),1000);
			},
			next:function(){
				this.count--;
				this.setVal(this.initTextBefore+this.count+this.initTextAfter);
				if(this.count == 0){
					this.dom.removeAttr("disabled");
					this.setVal(this.initText);
					if(this.isA){
						this.dom.attr("href", this.initHref);
					}
					clearInterval(this.interval);
					this.count = this.config.count || 10;
				}
			},
			setVal:function(val){
				if(this.isInput){
					this.dom.val(val);
				}else if(this.isA){
					this.dom.html(val);
				}
			}
		};
		return new a(id,config);
	}
	window.timer = timer;
})(window);