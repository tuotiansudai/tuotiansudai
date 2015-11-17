<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>托天速贷APP下载</title>

    <script type="text/javascript">
        function is_weixin(){
            var ua = navigator.userAgent.toLowerCase();
            if(ua.match(/MicroMessenger/i)=="micromessenger") {
                return true;
            } else {
                return false;
            }
        }

        function jump() {
            var u = navigator.userAgent;
            if (!is_weixin()) {
                if (u.indexOf('Android') > -1) {
                    location.href = "/app/tuotiansudai.apk";
                } else if (u.indexOf('iPhone') > -1 || u.indexOf('iPad') > -1) {
                    location.href = "http://itunes.apple.com/us/app/id1039233966";
                }
            } else {
                document.getElementById("wxPic").style.display="block";
            }
        }
    </script>
</head>

  <body onload="jump()">
  <div id="wxPic" style="display:none">

    <p style="font-size: 15px;">请按照下面的图示操作，下载拓天速贷APP:</p>
    <img style="width: 100%;" src="${staticServer}/images/activity/wxDownload.png"/>
  </div>
  <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1254796373'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1254796373%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));</script>
  </body>
</html>
