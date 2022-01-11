<%--
  Created by IntelliJ IDEA.
  User: mou_lamine
  Date: 02/01/2022
  Time: 02:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.time.LocalDateTime" %><%
    response.setContentType("application/xml");
    LocalDateTime currentTime = LocalDateTime.now();
%><
<?xml version="1.0" encoding="UTF-8"?>
<t>
    <name><%= currentTime.getHour() %></name>
    <score><%= currentTime.getMinute()%></score>
</t>