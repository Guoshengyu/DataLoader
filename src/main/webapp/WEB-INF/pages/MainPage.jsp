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

    <script src="http://code.jquery.com/jquery-2.1.3.js"></script>
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
        <h2>Test Index List</h2>
        <div id="test-indicator" >


        </div>
    </div>
</body>
<script type="text/javascript">

    var indexList;

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
    getIndexList();

</script>
</html>
