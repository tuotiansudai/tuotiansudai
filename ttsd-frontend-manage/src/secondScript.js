  function update() {
                 var now = new Date();
                 var beginTime = now.getTime();
 
                 var templateObj = [];
                 var reg = //{[A-Za-z]*/}/;
                 var para = reg.exec(template);
                 var tempHtml = template;
                 while (para && para.length > 0) {
                     var len = para.index;
                     var temp = {};
                     temp.html = tempHtml.substr(0, len);
                     temp.field = para[0].substr(1, para[0].length - 2); ;
                     templateObj.push(temp);
                     tempHtml = tempHtml.substr(len + para[0].length);
                     para = reg.exec(tempHtml);
                 }
                 var end = {};
                 end.html = tempHtml;
                 templateObj.push(end);
 
                 var html = "";
                 $.each(data, function (index, dataItem) {
                     var tempHtm = "";
                    $.each(templateObj, function (i, item) {
                         if (item.field) {
                             tempHtm = tempHtm + item.html + dataItem[item.field];
                         } else {
                             tempHtm = tempHtm + item.html;
                         }
                     });
                     html += tempHtm;
                 });
                 wl2.append(html);
             }
             update();