---
layout: post
title: jQuery QRcode
category: 资源
tags: jQuery QRcode
keywords: jQuery QRcode
description: 
---
## 引用jquery,jquery-qrcode
```markdown
<script type="text/javascript" src="../../scripts/jquery-1.10.1.min.js"></script>
<script src="../../scripts/jquery.qrcode.min.js" type="text/javascript"></script>
```
## 定义div
```markdown
<div id="code"></div>
```
## 调用
```markdown
function showQRCodeDiv() {
    var row = mastergrid.getRowByUID(row_uid);
    $("#code").text("").qrcode({
        render: "canvas", //table/canvas方式
        width: 228, //宽度
        height:228, //高度
        text: utf16to8(‘测试’) //二维码
    });
}

function utf16to8(str) {
    var out, i, len, c;
    out = "";
    len = str.length;
    for(i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
            out += str.charAt(i);
        } else if (c > 0x07FF) {
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
            out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
        } else {
            out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
        }
    }
    return out;
}

function saveQRCodeDiv() {
    var myCanvas = $("canvas")[0];
    var image = myCanvas.toDataURL("image/png").replace("image/png", "image/octet-stream");

    var link = document.createElement('a');
    link.href = image;
    var filename = mini.get("bqcode_2").getValue() + '.png';
    link.download = filename;
    var event = document.createEvent('MouseEvents');
    event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
    link.dispatchEvent(event);
}
```