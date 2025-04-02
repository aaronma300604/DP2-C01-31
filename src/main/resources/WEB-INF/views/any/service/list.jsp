<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.service.list.label.name" path="name" width="50%"/>
	<acme:list-column code="any.service.list.label.avgDwellTime" path="avgDwellTime" width="50%"/>
	<acme:list-payload path="payload"/>
</acme:list>
