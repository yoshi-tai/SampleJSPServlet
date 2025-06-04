<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Jsp/Servlet必須課題2</title>
<style>
.margin{margin-left: 100px;}
.color{background-color:white;}
</style>
</head>
<body>
	<h2>商品登録画面</h2>
	<form action="newProduct" method="post">
		<table>
    		<tr>
    			<td>商品ID</td>
    			
    			<td>
  					<input type="text" name="ID" value="${IDKEY}"
         				<c:if test="${disableIDKEY}">disabled</c:if>>
  				</td>

    		</tr>
    		<tr>
    			<td>商品コード</td>
    			<td><input type= "text" name="CODE" value="${CODEKEY}">
    				<c:if test="${not empty errorMessage1}">
    					<span style="font-size: 8px;">"${errorMessage1}"</span>
    				</c:if>
   				 </td>
    		</tr>
    		<tr>
    			<td>商品名</td>
    			<td><input type= "text" name="NAME" value="${NAMEKEY}"></td>
    		</tr>
    		<tr>
    			<td>価格</td>
    			<td><input type= "text" name="PRICE" value="${PRICEKEY}">
    				<c:if test="${not empty errorMessage2}">
    					<span style="font-size: 8px;">"${errorMessage2}"</span>
    				</c:if>
    			</td>
    		</tr>
    		<tr>
    			<td>カテゴリー</td>
    			<td>
    				<select name="CATEGORYID">
 <c:forEach var="list" items="${productList}">
    <option value="${list.CATEGORYID}" 
        <c:if test="${list.CATEGORYID == CATEGORYID}">selected</c:if>>
        ${list.CATEGORYNAME}
    </option>
</c:forEach>

    				</select>
    			</td>
    		</tr>
    	</table>
    	<input type="submit" name="botan" value="登録" class="margin" />
    	<input type="hidden" name="taiga" value="taiga"/>
    	<input type="submit" name="botan" value="検索" class="color" />
    	<input type="submit" name="botan" value="更新" class="color" />
    	<input type="submit" name="botan" value="削除" class="color" />
    </form>
	<p>${MSGKEY}</p>
</body>
</html>