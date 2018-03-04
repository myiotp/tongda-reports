<%--
--%>

<%@ page isErrorPage="true" %>
<%@ page import="java.io.*" %>

<html>
<head>
<title>
JasperReports - Web Application Sample
</title>
<link rel="stylesheet" type="text/css" href="../stylesheet.css" title="Style">
</head>

<body bgcolor="white">
<span class="bnew">JasperReports encountered this error :</span>
<pre>
<% exception.printStackTrace(new PrintWriter(out)); %>
</pre>
</body>
</html>
