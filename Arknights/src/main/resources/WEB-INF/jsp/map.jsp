<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2024/1/11
  Time: 8:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html>

<head>
    <meta charset="utf-8">
    <title>echarts图形插件使用</title>
    <script src="../../static/js/echarts.js"></script>
</head>

<body>
<div id="main" style="height:800px;"></div>
<%--<script type="text/JavaScript" src="echarts.min.js"></script>--%>
<script type="text/JavaScript" src="zhejiang.js"></script>
<script type="text/javascript">
    var chart = echarts.init(document.getElementById('main'));
    chart.setOption({
        series: [{
            type: 'map',
            selectedMode: 'single',
            map: '浙江',
            itemStyle: {
                normal: {label: {show: true}},
                emphasis: {label: {show: true}}
            }
        }]
    });
</script>
</body>

</html>
