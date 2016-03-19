
<!-- 
Anup Rokkam Pratap: 80529276
Yuvaraj Merve Basavaraj: 91605284

 -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
blockquote { 
    display: block;
    margin-top: 1em;
    margin-bottom: 1em;
    width:650px
}
</style>

<title>Search Results</title>
</head>
<body>

<h1><a href="index.jsp"> Search ICS</a></h1>
<form name="frm" method="get" action="search">
	<input class="textbar" type="text" name="query" value="${query}">
	
	<input type="submit" value="Search">
	</form>
	<h4> Top five results for : <b> ${query} </b> </h4>
<div style="vertical-align:middle">
<c:forEach items="${searchresult}" var="result">
		 <blockquote>
       	 <div style="font-size:120%"><a href="${result.url}"><b>${result.name}</b></a></div>
       	 <div style="color:green">${result.url}</div>
       	<div>${result.snippet}...</div>
       	 <br>
       	 </blockquote>
         <%-- <c:out value="${result.name}"/> --%> 
          <br>
      </c:forEach>
      </div>
</body>
</html>