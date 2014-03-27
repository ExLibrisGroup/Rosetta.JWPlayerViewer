<%@ page isErrorPage="true" import="java.io.*" contentType="text/html"%>
<html>
<body>
	<br/> StackTrace:
	<%
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		out.println(stringWriter);
		printWriter.close();
		stringWriter.close();
	%>
</body>
</html>
