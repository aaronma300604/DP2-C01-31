<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.service.list.label.name" path="name" width="50%"/>
	<acme:list-column code="administrator.service.list.label.avgDwellTime" path="avgDwellTime" width="50%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="administrator.service.list.button.create" action="/administrator/service/create"/>
</jstl:if>
