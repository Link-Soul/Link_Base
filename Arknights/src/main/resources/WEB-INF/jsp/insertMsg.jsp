<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2024/1/12
  Time: 14:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>信息插入</title>
    <script src="../../static/js/jquery-3.7.1.js"></script>
</head>
<body>
<div>卡池名称
    <input id="poolName" name="poolName">
</div>
<div>up角色1
    <input id="up1" name="up1">
</div>
<div>up角色2
    <input id="up2" name="up2">
</div>
<div>开始时间
    <input id="startTime" type="datetime-local" name="startTime">
</div>
<div>结束时间
    <input id="stopTime" type="datetime-local" name="stopTime">
</div>

<button onclick="submit()" type="button">提交</button>
</body>
<script type="text/javascript">
    let form = {}

    function $doc(id) {
        return document.getElementById(id)
    }

    submit = function () {
        form.poolName = $doc("poolName").value
        form.up1 = $doc("up1").value
        form.up2 = $doc("up2").value
        form.startTime = new Date($doc("startTime").value).getTime()
        form.stopTime = new Date($doc("stopTime").value).getTime()
        console.log(form);
        $.post({
            url: "/insertPoolMsg",
            dataType: "text",
            data: form,
            success: function () {

            },
            error: function (xhr, status, error) {
                console.log(error)
            }
        })
    }

</script>
</html>
