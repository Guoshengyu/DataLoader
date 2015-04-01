<%--
  Created by IntelliJ IDEA.
  User: Richard
  Date: 2015/03/21 
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="utf-8"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>CAUP DataLoader</title>
    <meta http-equiv="Content-Type" content="text/html" charset=utf-8">
    <script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
    <script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>


    <script src="<c:url value="/resources/ajax.js"/>"></script>

</head>
<body>

    <div>
        <h1>Data Loader Main Page</h1>
    </div>

    ${msg}
    <div>
        <form id = "file-upload-form"  method = "post" action = "file/upload" enctype="multipart/form-data">
            <input type="file" name="upload-file" id="upload-file"/>
            <button class="btn"  onclick="uploadFile()">上传</button>
        </form>
    </div>
    <div>
        <h2> Test Radio checkbox</h2>
        <table id="search-indicator">
        </table>
        <button class="btn"  onclick="generateSelectionResultJson()">Confirm</button>

    </div>
</body>
<script type="text/javascript">

    var indexList;
    var searchResultList;
    var indexCount;
    var selectionList;
    var selectionResultJson;

    function getIndexList() {
        $.get("getData/getIndicator", function (data, status) {
       //     indexList = JSON.parse(data);
            indexList = data;
            $("#test-indicator").html("hd中文fs");
            $.each(indexList.IndexList, function(index, item){
                $("#test-indicator").append(item.IndexName + "  " + item.Unit + "<br>");
            })
        });
    }

    function getSearchResultList(){
        $.get("searchIndex/getResult", function(data, status){
            searchResultList = data;
            $.each(searchResultList.IndexList, function(index, item){
                //Selection Tag Start
                $("#search-indicator").append("<tr> <td>"  + item.Region + "  " + item.DBIndex + " " + item.DBUnit +  "</td> <td><select id = \"search-result-selection-" + index + "\">");
                $.each(item.ybIndexList, function(index1, item1){
                    //Add selector options
                   var selectorObj = document.getElementById("search-result-selection-" + index);
                   selectorObj.options.add(new Option(item1.ybIndex + " | 单位：" + item1.ybUnit, index1));
                   selectorObj.options[0].selected = true;
                    if(index1 == item.ybIndexList.length - 1){
                        selectorObj.options.add(new Option("没有选项"));
                    }
                });
                //Selection Tag End
                $("#search-indicator").append("</select></td></tr>");
                indexCount = index + 1;
            })
        });
    }

    function getSelectionList(){
        var tempSelectionList = [];
        for(var i = 0; i != indexCount; i++){
            var selectorObj = document.getElementById("search-result-selection-" + i);
            var option = selectorObj.options[selectorObj.selectedIndex];
            if(option != null && option.text != "没有选项"){
                tempSelectionList.push({"YBIndex": option.text.substr(0, option.text.indexOf("|") - 1), "YBUnit": option.text.substr(option.text.indexOf("单位") + 3)});
            }else{
                tempSelectionList.push({"YBIndex": "NULL", "YBUnit": "NULL"});
            }
        }
        //console.log(JSON.stringify(tempSelectionList));
        selectionList = JSON.parse(JSON.stringify(tempSelectionList));
    }

    function postSelectionResult(obj){

        $.ajax({
            type: "POST",
            url: "generateResult/writeResult",
            contentType: "application/json; charset=utf-8",
            dataType: "application/json",
            data: JSON.stringify(obj),
            //data: JSON.stringify(selectionResultJson),
            success: function(jsonResult){
                console.log("sucess!");
            }
        });
    }

    function generateSelectionResultJson(){
        getSelectionList();
        var tempRetResultList = [];
        for(var i = 0; i != selectionList.length && i != searchResultList.length; ++i){
            tempRetResultList.push({"DBIndex":searchResultList.IndexList[i].DBIndex, "DBUnit":searchResultList.IndexList[i].DBUnit, "YBIndex": selectionList[i].YBIndex, "YBUnit":selectionList[i].YBUnit});
        }
        selectionResultJson = JSON.parse(JSON.stringify(tempRetResultList));
        console.log(JSON.stringify(selectionResultJson));
        postSelectionResult(selectionResultJson);
    }

    function uploadFile(){
        var oMyForm = new FormData();
        oMyForm.append("file", upload-file.files[0]);

        $.ajax({
            url: "file/upload",
            data: oMyForm,
            dataType: 'text',
            processData: false,
            contentType: false,
            type: 'POST',
            success: function(data){
                //  $('#result').html(data+ " uploaded by FormData!");
             //   $('#result').html(data);
                console.log(data);
            }
        });
    }
   // getSearchResultList();
    //getIndexList();

</script>
</html>
1