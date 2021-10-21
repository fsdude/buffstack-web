<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<h2>asdasdadas World!</h2>

<table class="list">
    <c:forEach items="${pessoas}" var="p">
        <tr>
            <td>${p.nome}
        </tr>
    </c:forEach>
</table>

</body>
</html>
