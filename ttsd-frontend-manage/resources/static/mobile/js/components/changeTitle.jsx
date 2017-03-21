function changeTitle(title){
    let body = document.getElementsByTagName('body')[0];
    document.title = title;
    let iframe = document.createElement("iframe");
    iframe.style.display = 'none';
    iframe.setAttribute("src", "");
    let fn = function() {
        setTimeout(function() {
            iframe.removeEventListener('load', fn);
            document.body.removeChild(iframe);
        }, 0);
    };
    iframe.addEventListener('load', fn);
    document.body.appendChild(iframe);
}

export default changeTitle;
