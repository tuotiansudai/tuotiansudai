define(['jquery'], function ($) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $(document).ajaxError(function (event, jqXHR, ajaxSettings, thrownError) {
        if (jqXHR.status == 403) {
            var directURL = "/login";
            if (jqXHR.responseText) {
                directURL += "?redirect=" + jqXHR.responseText;
            }
            window.location.href = directURL;
        }
    });
});