<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="keywords" content="枨曦金融网，投资理财，P2P理财，互联网金融，投资理财，理财，网络贷款，个人贷款，小额贷款，网络投融资平台, 网络理财, 固定收益, 100%本息保障"/>
    <meta name="description" content="枨曦金融网-专业的互联网金融平台！预期年化收益可高达13%，第三方资金托管，屡获大奖。"/>
    <title>枨曦金融网-专业的互联网金融公司</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/center.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/fund-guanli.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/security.css"/>
    <script type="text/javascript" language="javascript"
            src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" language="javascript"
            src="${pageContext.request.contextPath}/js/trafficStatistics.js"></script>

</head>

<body>
<!--页头start-->
<div id="header">
    <jsp:include page="commons/header.jsp"/>
</div>
<!--页头end-->

<!-- 二级导航栏start -->
<jsp:include page="commons/subNav.jsp"/>
<!-- 二级导航栏end -->

<!--页中start-->
<div class="mainBox">
    <div class="homeWap">
        <div class="fund-guanli clearfix">
            <div class="left-nav">
                <jsp:include page="commons/leftNav.jsp"/>
            </div>

            <div class="right-body">
                <div class="leftTitle"><span class="on">微信支付</span></div>
                <span hidden="true" id="out_trade_no">${rechargeNo}</span>
                <div class="unrecognized" style="display:block;" id="unrecognized1">
                    <h3>充值订单号：${rechargeNo} &nbsp;&nbsp; 充值金额：${rechargeMoney} &nbsp;&nbsp;充值时间:${rechargeTime}</h3>
                    <img src="${pageContext.request.contextPath}/loan/generateQRCode?rechargeNo=${rechargeNo}&rechargeMoney=${rechargeMoney}&d=" + new Date().getTime()/>
                </div>

            </div>
        </div>
    </div>
</div>
<!--页中end-->

<!--页脚start-->
<jsp:include page="commons/footer.jsp"/>
<!--页脚end-->

<script type="text/javascript">
    // 查询是否支付成功
    function checkPayResult() {
        // 获取订单号
        var out_trade_no = document.getElementById("out_trade_no").innerText;
        if (out_trade_no == null || out_trade_no == "") {
            return;
        }
        $.ajax({
            url: "wxPayIsSuccess",
            data: "out_trade_no=" + out_trade_no,
            type: "get",
            success: function (jsonObject) {
                if (jsonObject.result == "success") {
                    location.href = "${pageContext.request.contextPath}/loan/myRecharge";
                }
            }
        });

        /*$.get("/loan/wxPayIsSuccess", function(data) {
//              debugger;
            console.log(data);
            if (data) {
                window.location.href = "/loan/myRecharge";
            }
        });*/
    }
    $(function() {
        // 每个3秒调用后台方法，查看订单是否已经支付成功
        window.setInterval("checkPayResult()", 3000);
    });
</script>
</body>

</html>