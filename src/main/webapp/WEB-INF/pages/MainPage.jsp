<%--
  Created by IntelliJ IDEA.
  User: Richard
  Date: 2015/03/21 
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>CAUP DataLoader</title>
    <script type="text/javascript">
        $
    </script>
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
        <label id="test-indicator">

        </label>
    </div>
</body>
</html>
