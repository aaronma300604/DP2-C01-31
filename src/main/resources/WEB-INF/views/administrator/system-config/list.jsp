<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.sysconf.list.label.currency" path="currency" width="100%"/>	
	
	<acme:list-payload path="payload"/>
	
</acme:list>
	<acme:button code="administrator.sysconf.list.button.create" action="/administrator/system-config/create"/>
