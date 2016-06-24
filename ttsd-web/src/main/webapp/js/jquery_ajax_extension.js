define(['jquery'], function ($) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $(document).ajaxError(function (event, jqXHR, ajaxSettings, thrownError) {
        if (jqXHR.status == 403) {
            if (jqXHR.responseText) {
                var responseJson = JSON.parse(jqXHR.responseText);
                var data = responseJson.data;
                window.location.href = data.directUrl + (data.refererUrl ? "?redirect=" + data.refererUrl : '');
            }
        }
    });
});