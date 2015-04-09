<%--
  Created by IntelliJ IDEA.
  User: Richard
  Date: 2015/03/21 
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>CAUP DataLoader</title>
    <meta http-equiv="Content-Type" content="text/html" charset=utf-8">
    <script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
    <script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>


    <script src="<c:url value="/resources/ajax.js"/>"></script>
    <script src="<c:url value="/resources/ajaxfileupload.js"/>"></script>

</head>
<body>

<div  style="text-align: center">
    <h1>CAUP Data Loader</h1>
    <h3>Demo 0.4</h3>
</div>

<div style="text-align: center">
    <%--<form id = "file-upload-form"   enctype="multipart/form-data">--%>
    <input type="file" name="myFiles" id="upload-file"/>
    <button class="btn" onclick="ajaxFileUpload()">Upload</button>
    <br>
    <%--</form>--%>
    <label id="process-condition" style="font-family: Georgia"></label>
</div>
<div style="text-align: center">
    <table id="search-indicator" border="1" style="font-family: Georgia; font-size: 16px">
    </table>
    <button class="btn" onclick="generateSelectionResultJson()">Confirm & Download</button>
    <%--<button class="btn" onclick="downloadFile()">Download</button>--%>

</div>

</body>
<script type="text/javascript">
    var indexList;
    var searchResultList;
    var indexCount;
    var selectionList;
    var selectionResultJson;
    var fileName;
    var downloadFilePath;

    function getSearchResultList() {

        $("#process-condition").html("Processing..");
        var startTime = new Date().getTime();
        $.get("searchIndex/getResult?fileName=" + fileName, function (data, status) {
            if (data == "fail"){
                alert("FFF");
                return;
            }

            searchResultList = data;
            $("#search-indicator").html("");
            $.each(searchResultList.IndexList, function (index, item) {
                //Selection Tag Start
                $("#search-indicator").append("<tr> <td>" + (index+1) + " " + item.Region + "  " + item.DBIndex + " " + item.DBUnit + "</td> <td><select id = \"search-result-selection-" + index + "\">");
                $.each(item.ybIndexList, function (index1, item1) {
                    //Add selector options
                    var selectorObj = document.getElementById("search-result-selection-" + index);
                    selectorObj.options.add(new Option(item1.ybIndex + " | 单位：" + item1.ybUnit, index1));
                    selectorObj.options[0].selected = true;
                    if (index1 == item.ybIndexList.length - 1) {
                        selectorObj.options.add(new Option("没有选项"));
                    }
                });
                //Selection Tag End
                $("#search-indicator").append("</select></td></tr>");
                indexCount = index + 1;
           //     var processNumber = Math.floor((searchResultList.IndexList.length - 1) / 10 );
                var interval = new Date().getTime() - startTime;
                $("#process-condition").html(interval /1000 + " seconds for " + indexCount + " indexes");


            })

        });

    }

    function getSelectionList() {
        var tempSelectionList = [];
        for (var i = 0; i != indexCount; i++) {
            var selectorObj = document.getElementById("search-result-selection-" + i);
            var option = selectorObj.options[selectorObj.selectedIndex];
            if (option != null && option.text != "没有选项") {
                tempSelectionList.push({"YBIndex": option.text.substr(0, option.text.indexOf("|") - 1), "YBUnit": option.text.substr(option.text.indexOf("单位") + 3)});
            } else {
                tempSelectionList.push({"YBIndex": "NULL", "YBUnit": "NULL"});
            }
        }
        //console.log(JSON.stringify(tempSelectionList));
        selectionList = JSON.parse(JSON.stringify(tempSelectionList));
    }

    function postSelectionResult(obj) {

        $.ajax({
            type: "POST",
            url: "generateResult/writeResult?fileName=" + fileName,
            contentType: "application/json; charset=utf-8",
            dataType: "application/json",
            data: JSON.stringify(obj),
            //data: JSON.stringify(selectionResultJson),
            success: function (data) {
                console.log("1111");
                downloadFilePath = data.name;
                downloadFile();
            },
            error:function() {
                console.log("4444");
               // alert(arguments[1]);
                downloadFile();
            }
        });
    }

    function generateSelectionResultJson() {
        getSelectionList();
        var tempRetResultList = [];
        for (var i = 0; i != selectionList.length && i != searchResultList.length; ++i) {
            tempRetResultList.push({"DBIndex": searchResultList.IndexList[i].DBIndex, "DBUnit": searchResultList.IndexList[i].DBUnit, "YBIndex": selectionList[i].YBIndex, "YBUnit": selectionList[i].YBUnit});
        }
        selectionResultJson = JSON.parse(JSON.stringify(tempRetResultList));
        console.log(JSON.stringify(selectionResultJson));
        postSelectionResult(selectionResultJson);

      //   downloadFile();
    }

    function ajaxFileUpload() {
        $("#search-indicator").html("");
        $.ajaxFileUpload({
            //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
            url: "file/upload",
            fileElementId: "upload-file",           //文件选择框的id属性
            dataType: 'text',                       //服务器返回的格式,可以是json或xml等
            success: function (data, status) {        //服务器响应成功时的处理函数
                data = data.replace("<PRE>", '');  //ajaxFileUpload会对服务器响应回来的text内容加上<pre>text</pre>前后缀
                data = data.replace("</PRE>", '');
                data = data.replace("<pre>", '');
                data = data.replace("</pre>", ''); //本例中设定上传文件完毕后,服务端会返回给前台[0`filepath]
                console.log(data);
                fileName = data;
                getSearchResultList();
            },
            error: function (data, status, e) { //服务器响应失败时的处理函数
                alert(data);
            }
        });
    }

    function downloadFile() {
        window.location.href = 'file/download?fileName='
                + fileName;
    }

</script>
</html>
