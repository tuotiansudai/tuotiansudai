/*!
 * jquery.carousel.js
 */
;(function ($, window, undefined) {

    var Carousel
        , DEFALUT_OPTIONS
    ;

    /**
     * DEFAULT_OPTIONS
     */
    DEFAULT_OPTIONS = {
        speed: 300
        , easing: 'swing'
        , autoPlay: false
        , autoPlayTimeout: 2000
        , interval: 7000
        , selectorItemList: '.ui-carousel-itemList'
        , selectorItem: '.ui-carousel-item'
        , selectorPrev: '.ui-carousel-prev'
        , selectorNext: '.ui-carousel-next'
        , selectorPlay: '.ui-carousel-play'
        , playingClass: 'ui-carousel-playing'
        , stoppingClass: 'ui-carousel-stopping'
        , playReverse: false
        , playButtonText: 'Play'
        , stopButtonText: 'Stop'
        , cloneAppendClass: 'ui-carousel-cloneAppend'
        , clonePrependClass: 'ui-carousel-clonePrepend'
        , windowResizeSupport: true
        , useThrottlingEvent: true
        , throttlingInterval: 40
    };


    /**
     * Carousel
     */
    Carousel = function ($element, options) {
        this.options = $.extend({}, DEFAULT_OPTIONS, options);
        this.$element = $element;
        this.init();
    };


    /**
     * Carousel.prototype
     */
    Carousel.prototype = {

        /**
         * init
         */
        init: function () {
            var __this = this
                , o = __this.options
            ;

            __this._exploreElements();

            __this.LENGTH         = __this.$item.length;
            __this.ITEM_WIDTH     = __this.$item.outerWidth(true);
            __this.ITEMLIST_WIDTH = __this.ITEM_WIDTH * __this.ITEM_LENGTH;

            __this._index = 0;
            __this._timer = null;
            __this.$element.addClass(o.stoppingClass);

            __this.$element.css({ position: 'relative' });
            __this.$itemList.css({ position: 'absolute' });

            //$(window).on('load', function () {
            //	var pt = parseFloat(__this.$element.css('padding-top'))
            //	  , h = __this.$item.height()
            //	  ;

            //	__this.$element.css({ paddingTop: pt + h });
            //});


            __this.refresh();

            __this.$prev.on('click', function () {
                __this.toPrev();
            });

            __this.$next.on('click', function () {
                __this.toNext();
            });

            __this.$play.on('click', function () {
                var isPlaying = (__this.state === 'playing') ? true: false;

                if (isPlaying) {
                    __this.stop();
                } else {
                    __this.play();
                }
            });

            if (o.autoPlay) {
                setTimeout(function () {
                    __this.play();
                }, o.autoPlayTimeout);
            }

            if (o.windowResizeSupport) {

                if (o.useThrottlingEvent) {
                    $.throttlingResize({
                        interval: o.throttlingInterval
                    }).on('throttlingResize' + o.throttlingInterval, function () {
                        __this.refresh();
                    });
                } else {
                    $(window).on('resize', function () {
                        __this.refresh();
                    });
                }
            }

            return __this;
        }
        ,

        /**
         * _exploreElements
         */
        _exploreElements: function () {
            var __this = this
                , o = __this.options
            ;

            __this.$itemList = __this.$element.find(o.selectorItemList);
            __this.$item     = __this.$element.find(o.selectorItem);
            __this.$prev     = __this.$element.find(o.selectorPrev);
            __this.$next     = __this.$element.find(o.selectorNext);
            __this.$play     = __this.$element.find(o.selectorPlay);

            __this.$cloneAppend = $([]);
            __this.$clonePrepend = $([]);

            return __this;
        }
        ,

        /**
         * _setProperties
         */
        _setProperties: function () {
            var __this = this
                , o = __this.options
            ;

            __this.FRAME_WIDTH    = __this.$element.width();

            __this.TEST_WIDTH   = __this.FRAME_WIDTH + (__this.ITEM_WIDTH * 4);
            __this.TOTAL_LENGTH = Math.ceil(__this.TEST_WIDTH / __this.ITEM_WIDTH);
            __this.CLONE_LENGTH = __this.TOTAL_LENGTH - __this.LENGTH;

            __this.FRONT_POSITION = __this.FRAME_WIDTH / 2 - (__this.ITEM_WIDTH / 2) - ( __this.ITEM_WIDTH * (Math.ceil(__this.CLONE_LENGTH / 2)) );
            __this.BACK_POSITION  = __this.FRONT_POSITION - (__this.ITEM_WIDTH * (__this.LENGTH - 1));

            __this._left = __this.FRONT_POSITION - (__this.ITEM_WIDTH * __this._index);

            return __this;
        }
        ,

        /**
         * refresh
         */
        refresh: function () {
            var __this = this
                , o = __this.options
            ;

            __this._removeClone();
            __this._setProperties();

            __this._clone();

            __this.$itemList.css({
                width: (__this.TOTAL_LENGTH + 2) * __this.ITEM_WIDTH
                , left: __this._left
            });

            return __this;
        }
        ,

        /**
         * _clone
         */
        _clone: function () {
            var __this = this
                , o = __this.options
                , i, l, $target, $clone, target
            ;

            if (__this.CLONE_LENGTH > 0) {
                for (i = 0, l = Math.ceil(__this.CLONE_LENGTH / 2); i < l; i += 1) {
                    target = i % __this.LENGTH;
                    $target = __this.$item.eq(target);
                    $clone = $target.clone().addClass(o.cloneAppendClass);
                    __this.$itemList.append($clone);
                    __this.$cloneAppend = __this.$cloneAppend.add($clone);

                    target = __this.LENGTH - (i % __this.LENGTH) -1;
                    $target = __this.$item.eq(target);
                    $clone = $target.clone().addClass(o.clonePrependClass);
                    __this.$itemList.prepend($clone);
                    __this.$clonePrepend = __this.$clonePrepend.add($clone);
                }
            }

            return __this;
        }
        ,

        /**
         * _removeClone
         */
        _removeClone: function () {
            var __this = this;

            __this.$cloneAppend.remove();
            __this.$clonePrepend.remove();

            return __this;
        }
        ,

        /**
         * _animate
         */
        _animate: function () {
            var __this = this
                , o = __this.options
            ;

            __this.$element.data('carousel-animate', true)
            __this.$itemList.animate({
                left: __this._left
            }, {
                duration: o.speed
                , easing: 'linear'
                , queue: false
                , complete: function () {
                    if (__this._index < 0) {
                        __this._left = __this.BACK_POSITION;
                        __this._index = __this.LENGTH - 1;
                        __this.$itemList.css({
                            left: __this._left
                        });
                    }

                    if (__this._index >= __this.LENGTH) {
                        __this._left = __this.FRONT_POSITION;
                        __this._index = 0;
                        __this.$itemList.css({
                            left: __this._left
                        });
                    }
                    __this.$element.data('carousel-animate', false);
                }
            });

            return __this;
        }
        ,

        /**
         * _auto
         * @param {Boolean} reverse `true` を渡すと逆再生になる
         */
        _auto: function (reverse) {
            var __this = this
                , o = __this.options
                , move = (!reverse) ? __this.toNext : __this.toPrev
            ;

            move.call(__this);

            __this._timer = setTimeout(function () {
                __this._auto(reverse);
            }, o.interval || 40);

            return __this;
        }
        ,

        /**
         * play
         * @param {Boolean} reverse `true` を渡すと逆再生になる
         */
        play: function (reverse) {
            var __this = this
                , o = __this.options
                , reverse = reverse || o.playReverse
            ;

            __this.state = 'playing';
            __this.$element.trigger('carousel.play');
            __this.$element.removeClass(o.stopClass);
            __this.$element.addClass(o.playClass);
            __this.$play.text(o.stopButtonText);
            __this._auto(reverse);

            return __this;
        }
        ,

        /**
         * stop
         */
        stop: function () {
            var __this = this
                , o = __this.options
            ;

            if (__this._timer) {
                clearTimeout(__this._timer);
                __this._timer = null;
            }

            __this.state = 'stopping';
            __this.$element.trigger('carousel.stop');
            __this.$element.removeClass(o.playingClass);
            __this.$element.addClass(o.stoppingClass);
            __this.$play.text(o.playButtonText);

            return __this;
        }
        ,

        /**
         * toPrev
         */
        toPrev: function () {
            var __this = this;

            if (!__this.$element.data('carousel-animate')) {
                __this._index = __this._index - 1;
                __this._left = __this._left + __this.ITEM_WIDTH;

                __this._animate();
            }

            return __this;
        }
        ,

        /**
         * toNext
         */
        toNext: function () {
            var __this = this;

            if (!__this.$element.data('carousel-animate')) {
                __this._index = __this._index + 1;
                __this._left = __this._left - __this.ITEM_WIDTH;

                __this._animate();
            }

            return __this;
        }

    };


    /**
     * $.fn.carousel
     */
    $.fn.carousel = function (options) {
        return this.each(function () {
            var $this = $(this);
            $this.data('carousel', new Carousel($this, options));
        });
    };

})(jQuery, this);