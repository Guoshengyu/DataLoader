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
        <form id = "file-upload-form" method="post">
            <input type="file"/>
            <button class="btn" type="submit">上传数据文件</button>
        </form>
    </div>
    <div>
        <h2> Test Radio checkbox</h2>
        <div id="search-indicator">

        </div>
        <input type="radio" name="radiobutton" value="radiobutton" checked> 喜欢
        <input type="radio" name="radiobutton" value="radiobutton"> 不喜欢
        <input type="radio" name="radiobutton" value="radiobutton"> 无所谓<br>

        <h2>Test Index List</h2>
        <div id="test-indicator" >


        </div>
    </div>
</body>
<script type="text/javascript">

    var indexList;
    var searchResultList;

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
           // $("#search-indicator").html("hd中文fs");
            $.each(searchResultList.IndexList, function(index, item){
                $("#search-indicator").append(item.Region + "  " + item.DBIndex + " " + item.DBUnit +  "<br>");
                $.each(item.ybIndexList, function(index1, item1){
                    $("#search-indicator").append(item1.ybIndex + "  " + item1.ybUnit  +  " | ");
                });
                $("#search-indicator").append("<br>");
            })
        });
    }

    getSearchResultList();
    getIndexList();

</script>
</html>
