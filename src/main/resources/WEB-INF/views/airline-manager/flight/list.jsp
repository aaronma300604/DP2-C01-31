<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="airline-manager.flight.list.label.tag" path="tag" width="20%"/>
	<acme:list-column code="airline-manager.flight.list.label.origin" path="origin" width="30%"/>
	<acme:list-column code="airline-manager.flight.list.label.destination" path="destination" width="30%"/>
	<acme:list-column code="airline-manager.flight.list.label.cost" path="cost" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="airline-manager.flight.list.button.create" action="/airline-manager/flight/create"/>
</jstl:if>		