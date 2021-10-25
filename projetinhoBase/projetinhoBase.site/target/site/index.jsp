<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Página inicial</title>
</head>
<body>
<s:form action="salvarPessoa">
    <s:textfield name="pessoaBean.id" label="ID"/>
    <s:textfield name="pessoaBean.nome" label="Nome"/>
    <s:textfield name="pessoaBean.cpf" label="CPF"/>
    <s:submit/>
</s:form>

<s:if test="!getPessoaList().isEmpty()">
    <div class="content">
        <table class="userTable" cellpadding="5px">
            <tr class="even">
                <th>ID</th>
                <th>Nome</th>
                <th>CPF</th>
            </tr>
            <s:iterator value="pessoaList" status="pessoaStatus">
                <tr class="<s:if test="#pessoaStatus.odd == true ">odd</s:if> <s:else>even</s:else>">
                    <td><s:property value="id"/></td>
                    <td><s:property value="nome"/></td>
                    <td><s:property value="cpf"/></td>
<%--                    <td>--%>
<%--                        <s:url id="editURL" action="editUser">--%>
<%--                            <s:param name="id" value="%{id}"></s:param>--%>
<%--                        </s:url>--%>
<%--                        <s:a href="%{editURL}">Edit</s:a>--%>
<%--                    </td>--%>
<%--                    <td>--%>
<%--                        <s:url id="deleteURL" action="deleteUser">--%>
<%--                            <s:param name="id" value="%{id}"></s:param>--%>
<%--                        </s:url>--%>
<%--                        <s:a href="%{deleteURL}">Delete</s:a>--%>
<%--                    </td>--%>
                </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
</body>
</html>