<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--

--%>

<%@ page errorPage="error.jsp" %>
<%@ page import="datasource.*" %>
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="net.sf.jasperreports.engine.util.*" %>
<%@ page import="net.sf.jasperreports.engine.export.*" %>
<%@ page import="net.sf.jasperreports.j2ee.servlets.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="servlets.ReportConstants" %>

<%
	String reportFileName = application.getRealPath("/reports/"+ReportConstants.REPORT_NAME+".jasper");
	File reportFile = new File(reportFileName);
    if (!reportFile.exists())
		throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");

	Map parameters = new HashMap();
	parameters.put("ReportTitle", "Address Report");
	parameters.put("BaseDir", reportFile.getParentFile());
	parameters.put("cargoowner", "刘大壮");
				
	JasperPrint jasperPrint = 
		JasperFillManager.fillReport(
			reportFileName, 
			parameters, 
			new WebappDataSource()
			);
				
	session.setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
%>

<html>
<head>
<title>JasperReports - Web Application Sample</title>
<link rel="stylesheet" type="text/css" href="../stylesheet.css" title="Style">
</head>

<body bgcolor="white">

<span class="bold">The compiled report design was successfully filled with data.</span>

</body>
</html>

