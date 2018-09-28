require('mWebStyle/account/loan_knowledge.scss');
let commonFun = require('publicJs/commonFun');
let tpl = require('art-template/dist/template');


$(function () {

    let $knowledgeList = $('#knowledgeList');
    let $knowledgeTitle = $('#knowledgeTitle');
    let bannerHeight = $('.knowledge-banner').height();
    let titleHeight = $('.title-section').height();
    let footerHeight = $('.knowledge-list').length ? $('.knowledge-list').height() : 0;
    let contentHeight = $('body').height() - bannerHeight - footerHeight - titleHeight;
    let titleBar="ALL";
    $('.knowledge-list-frame').height(contentHeight);

    let myScroll = new IScroll('#wrapperOut', {
        probeType: 2,
        mouseWheel: true,
        click: true
    });
    myScroll.on('scrollEnd', function () {
        //如果滑动到底部，则加载更多数据（距离最底部10px高度）
        if ((this.y - this.maxScrollY) <= 10) {
            getMore(true);

        }
    });

    let requestData = {"index": 1, "pageSize": 10};
    let isLastPage = false;

    let requestParams = {"index": 1, "pageSize": 10};
    if(titleBar){
        if(titleBar == "ALL"){
            requestParams = {"index": 1, "pageSize": 10}
        }else {
            requestParams = {"index": 1, "pageSize": 10,"subSection":titleBar};
        }
    }else {
        requestParams = {"index": 1, "pageSize": 10}
    }
    getMore()
//获取更多数据
    function getMore(flag) {
        $('#noData').hide();
        if (isLastPage) {
            $('#pullUp').hide();
            $('#noData').show();
            return false;

        }

        commonFun.useAjax(
            {
                url: '/knowledge/list',
                type: 'get',
                data: requestParams
            },
            function (data) {
                if (data.success) {
                    if (!data.data.hasNextPage) {
                        isLastPage = true;
                    }else {
                        isLastPage = true;
                    }
                    if (data.data.records.length) {
                        if (requestParams.index !== 1) {
                            $('#pullUp').show();
                        }
                        let knowledgeTpl = $('#knowledgeListTemplate').html();
                        let ListRender = _.template(knowledgeTpl);
                        let html = ListRender(data.data);
                        if(flag){
                            console.log('滚动加载滚动加载')
                            $knowledgeList.append(html);

                        }else {
                            console.log('点击加载点击加载')
                            $knowledgeList.html(html);

                        }

                        $knowledgeList.find('.date-time').each(function (key, option) {
                            var getTime = $(option).text();
                            $(option).text(getTime.substr(0, 10));
                        });
                        myScroll.refresh();

                    } else {
                        $knowledgeList.html('<div class="noContent">暂无内容</div>');
                        $('#noData').hide();
                        myScroll.refresh();
                    }
                }


            }
        )
        requestParams.index++;
    }

    $knowledgeTitle.find('span').on('click', function () {
        let _self = $(this);
        let params = _self.data('category');
        requestParams.index = 1;
        titleBar = params;
        if(titleBar){
            if(titleBar == "ALL"){
                requestParams = {"index": 1, "pageSize": 10}
            }else {
                requestParams = {"index": 1, "pageSize": 10,"subSection":titleBar};
            }
        }else {
            requestParams = {"index": 1, "pageSize": 10}
        }
        isLastPage = false;
        $('#pullUp').hide();
        getMore()
    })

    function isPassive() {
        var supportsPassiveOption = false;
        try {
            addEventListener("test", null, Object.defineProperty({}, 'passive', {
                get: function () {
                    supportsPassiveOption = true;
                }
            }));
        } catch (e) {
        }
        return supportsPassiveOption;
    }

    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, isPassive() ? {
        capture: false,
        passive: false
    } : false);
})