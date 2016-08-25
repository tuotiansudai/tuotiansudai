require(['jquery', 'underscore', 'layerWrapper', 'commonFun', 'circle'], function ($, _, layer) {

    var $awardRecordsFrame = $('#awardRecordsFrame');
    var $slideBody = $('table tbody', $awardRecordsFrame);

    var $awardList = $('.award-list');
    var $awardCategory = $('.award-category', $awardRecordsFrame);
    var $winnerBox = $('.winner-box table tbody', $awardRecordsFrame);
    var $recordBox = $('.record-box table tbody', $awardRecordsFrame);

    var scrollTimer, scrollTimer2;
    var $swiperWrapper = $('.swiper-wrapper'),
        $swiperslide = $('.swiper-slide', $swiperWrapper);
    var browser = commonFun.browserRedirect();
    if (browser == 'mobile') {
        var isScrolling, startPos, endPos;
        var slider = {
            //判断设备是否支持touch事件
            touch: ('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch,
            slider: document.getElementById('sliderBox'),

            //事件
            events: {
                index: 0,     //显示元素的索引
                slider: this.slider,     //this为slider对象
                icons: document.getElementById('sliderBox'),
                icon: document.getElementById('sliderBox').getElementsByClassName('swiper-slide'),
                handleEvent: function (event) {
                    var self = this;     //this指events对象
                    if (event.type == 'touchstart') {
                        self.start(event);
                    } else if (event.type == 'touchmove') {
                        self.move(event);
                    } else if (event.type == 'touchend') {
                        self.end(event);
                    }
                },
                //滑动开始
                start: function (event) {
                    var touch = event.targetTouches[0];     //touches数组对象获得屏幕上所有的touch，取第一个touch
                    startPos = {x: touch.pageX, y: touch.pageY, time: +new Date};    //取第一个touch的坐标值
                    isScrolling = 0;   //这个参数判断是垂直滚动还是水平滚动
                    if (isScrolling == 0) {
                        this.icons.appendChild(this.icon[0]);
                    }
                    this.icons.addEventListener('touchmove', this, false);
                    this.icons.addEventListener('touchend', this, false);
                },
                //移动
                move: function (event) {
                    //当屏幕有多个touch或者页面被缩放过，就不执行move操作
                    if (event.targetTouches.length > 1 || event.scale && event.scale !== 1) return;
                    var touch = event.targetTouches[0];
                    endPos = {x: touch.pageX - startPos.x, y: touch.pageY - startPos.y};
                    isScrolling = Math.abs(endPos.x) < Math.abs(endPos.y) ? 1 : 0;    //isScrolling为1时，表示纵向滑动，0为横向滑动
                    if (isScrolling == 0) {
                        event.preventDefault();      //阻止触摸事件的默认行为，即阻止滚屏
                    }
                },
                //滑动释放
                end: function (event) {
                    var duration = +new Date - startPos.time;    //滑动的持续时间

                    if (isScrolling === 0) {    //当为水平滚动时
                        //this.icon[this.index].className = 'prize-box swiper-slide';
                        if (Number(duration) > 10) {
                            //判断是左移还是右移，当偏移量大于10时执行
                            if (endPos.x > 10) {
                                if (this.index !== 0) this.index -= 1;

                            } else if (endPos.x < -10) {
                                if (this.index !== this.icon.length - 1) this.index += 1;
                            }
                        }
                        console.log(this.index);
                        this.icon[0].className = 'prize-box swiper-slide first';
                        this.icon[1].className = 'prize-box swiper-slide active';
                        this.icon[2].className = 'prize-box swiper-slide last';
                        //this.icons.style.left = -this.index*600 + 'px';
                    }
                    //解绑事件
                    this.icons.removeEventListener('touchmove', this, false);
                    this.icons.removeEventListener('touchend', this, false);
                }
            },

            //初始化
            init: function () {
                var self = this;     //this指slider对象
                if (!!self.touch) self.slider.addEventListener('touchstart', self.events, false);    //addEventListener第二个参数可以传一个对象，会调用该对象的handleEvent属性
            }
        };

        slider.init();


    }

    function scrollAwardRecords(obj) {
        var lineHeight = obj.find("tr:first").height();
        obj.animate({
            "margin-top": -lineHeight + "px"
        }, 600, function () {
            obj.css({
                "margin-top": "0px"
            }).find("tr:first").appendTo(obj);
        })
    }

    if ($awardList.find('dd').length > 10) {
        $awardList.hover(function () {
            clearInterval(scrollTimer2);
        }, function () {
            scrollTimer2 = setInterval(function () {
                var lineHeight = $awardList.find("dd:first").height();
                $awardList.animate({
                    "margin-top": -lineHeight + "px"
                }, 600, function () {
                    $awardList.css({
                        "margin-top": "0px"
                    }).find("dd:first").appendTo($awardList);
                })
            }, 2000);
        }).trigger("mouseout");
    }

    //获奖名单
    if ($winnerBox.find('tr').length > 5) {
        $winnerBox.hover(function () {
            clearInterval(scrollTimer);
        }, function () {
            scrollTimer = setInterval(function () {
                scrollAwardRecords($winnerBox);
            }, 2000);
        }).trigger("mouseout");
    }

    //我的获奖记录
    if ($recordBox.find('tr').length > 5) {
        $recordBox.hover(function () {
            clearInterval(scrollTimer);
        }, function () {
            scrollTimer = setInterval(function () {
                scrollAwardRecords($recordBox);
            }, 2000);
        }).trigger("mouseout");
    }
    $awardCategory.find('li').on('click', function () {
        var $this = $(this),
            num = $this.index();
        $this.addClass('active').siblings('li').removeClass('active');
        $('.switchContent', $awardRecordsFrame).eq(num).show().siblings('.switchContent').hide();

    })
});