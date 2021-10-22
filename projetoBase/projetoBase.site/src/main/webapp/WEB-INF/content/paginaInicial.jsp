<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<h2>Lista de pessoas no bd!</h2>

<%--<s:form action="" method="post">--%>
<%--    <s:textfield label="Nome" name="nome"></s:textfield>--%>
<%--    <s:textfield label="CPF" name="cpf"></s:textfield>--%>
<%--</s:form>--%>


<table class="list">
    <c:forEach items="${pessoas}" var="p">
        <tr>
            <td>${p.id} ${p.nome} ${p.cpf}
                <s:form namespace="/" action="" method="get">
                    <input type="hidden" name="pessoaId" value="${p.id}"/>
                    <input type="submit" name="action" value="delete">
                </s:form>
        </tr>
    </c:forEach>
</table>

</body>
</html>
