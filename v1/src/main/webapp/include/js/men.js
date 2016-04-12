/* index */


function sidemenu(id){
	var $obj = $(id), sideMenuIndex = -1;
	$obj.children("li").each(function(i){
		if(window.location.href.indexOf($(this).children("a").attr("href")) > -1){
			sideMenuIndex = i;
			if($(this).find("li").size() > 0){
				$(this).addClass("withsub");
			}
			$(this).children("a").eq(0).addClass("on");
			$(this).children("ul").show();
		}
	});
	$obj.find("li:has(li) li").each(function(i){
		if(window.location.href.indexOf($(this).children("a").attr("href")) > -1){
			sideMenuIndex = i;
			$(this).addClass("hover");
			$(this).parent().parent().addClass("withsub").children("a").eq(0).addClass("on");
			$(this).parent("ul").show();
		}
	});
	
}

(function (b) {
    b.fn.superfish = function (k) {
        var g = b.fn.superfish,
            j = g.c,
            f = b(['<span class="', j.arrowClass, '"> &#187;</span>'].join("")),
            i = function () {
                var c = b(this),
                    l = d(c);
                clearTimeout(l.sfTimer);
                c.showSuperfishUl().siblings().hideSuperfishUl()
            },
            e = function () {
                var c = b(this),
                    m = d(c),
                    l = g.op;
                clearTimeout(m.sfTimer);
                m.sfTimer = setTimeout(function () {
                        l.retainPath = (b.inArray(c[0], l.$path) > -1);
                        c.hideSuperfishUl();
                        if (l.$path.length && c.parents(["li.", l.hoverClass].join("")).length < 1) {
                            i.call(l.$path)
                        }
                    }, l.delay)
            },
            d = function (c) {
                var l = c.parents(["ul.", j.menuClass, ":first"].join(""))[0];
                g.op = g.o[l.serial];
                return l
            },
            h = function (c) {
                c.addClass(j.anchorClass).append(f.clone())
            };
        return this.each(function () {
                var c = this.serial = g.o.length;
                var m = b.extend({}, g.defaults, k);
                m.$path = b("li." + m.pathClass, this).slice(0, m.pathLevels).each(function () {
                    b(this).addClass([m.hoverClass, j.bcClass].join(" ")).filter("li:has(ul)").removeClass(m.pathClass)
                });
                g.o[c] = g.op = m;
                b("li:has(ul)", this)[(b.fn.hoverIntent && !m.disableHI) ? "hoverIntent" : "hover"](i, e).each(function () {
                    if (m.autoArrows) {
                        h(b(">a:first-child", this))
                    }
                }).not("." + j.bcClass).hideSuperfishUl();
                var l = b("a", this);
                l.each(function (n) {
                    var o = l.eq(n).parents("li");
                    l.eq(n).focus(function () {
                        i.call(o)
                    }).blur(function () {
                        e.call(o)
                    })
                });
                m.onInit.call(this)
            }).each(function () {
                var c = [j.menuClass];
                if (g.op.dropShadows && !(b.browser.msie && b.browser.version < 7)) {
                    c.push(j.shadowClass)
                }
                b(this).addClass(c.join(" "))
            })
    };
    var a = b.fn.superfish;
    a.o = [];
    a.op = {};
    a.IE7fix = function () {
        var c = a.op;
        if (b.browser.msie && b.browser.version > 6 && c.dropShadows && c.animation.opacity != undefined) {
            this.toggleClass(a.c.shadowClass + "-off")
        }
    };
    a.c = {
        bcClass: "sf-breadcrumb",
        menuClass: "sf-js-enabled",
        anchorClass: "sf-with-ul",
        arrowClass: "sf-sub-indicator",
        shadowClass: "sf-shadow"
    };
    a.defaults = {
        hoverClass: "sfHover",
        pathClass: "overideThisToUse",
        pathLevels: 1,
        delay: 800,
        animation: {
            opacity: "show"
        },
        speed: "normal",
        autoArrows: true,
        dropShadows: true,
        disableHI: false,
        onInit: function () {},
        onBeforeShow: function () {},
        onShow: function () {},
        onHide: function () {}
    };
    b.fn.extend({
        hideSuperfishUl: function () {
            var e = a.op,
                d = (e.retainPath === true) ? e.$path : "";
            e.retainPath = false;
            var c = b(["li.", e.hoverClass].join(""), this).add(this).not(d).removeClass(e.hoverClass).find(">ul").hide().css("visibility", "hidden");
            e.onHide.call(c);
            return this
        },
        showSuperfishUl: function () {
            var e = a.op,
                d = a.c.shadowClass + "-off",
                c = this.addClass(e.hoverClass).find(">ul:hidden").css("visibility", "visible");
            a.IE7fix.call(c);
            e.onBeforeShow.call(c);
            c.animate(e.animation, e.speed, function () {
                    a.IE7fix.call(c);
                    e.onShow.call(c)
                });
            return this
        }
    })
})(jQuery);

$(document).ready(function() {
	$("#men").superfish({
		delay : 500,
		animation : {
			opacity : 'show',
			height : 'show'
		},
		speed : 'fast',
		autoArrows : false,
		dropShadows : false
	});
	
	var men=document.getElementById("men");

	if(men !=null)
	{
	 var lis=men.getElementsByTagName("strong");
	 for(var i=0;i<lis.length;i++)
	 {
	        if(i==0)
	        {
	         lis[0].className="index";
	        }
	        if(window.location.href.indexOf(lis[i].parentNode.className) >-1)
	         {
	             lis[0].className="";   
	             lis[i].className="index";
	         }
	        else if(window.location.href.indexOf("/alzsMore/") >-1 )
	        { 
	          lis[0].className="";   
	          lis[i].className="index";
	       }
	 }
	}
});



