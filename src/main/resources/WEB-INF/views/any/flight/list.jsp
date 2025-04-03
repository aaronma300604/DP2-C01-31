<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.flight.list.label.tag" path="tag" width="10%"/>
	<acme:list-column code="any.flight.list.label.cost" path="cost" width="20%"/>
	<acme:list-column code="any.flight.list.label.scheduledDeparture" path="scheduledDeparture" width="15%"/>
	<acme:list-column code="any.flight.list.label.scheduledArrival" path="scheduledArrival" width="15%"/>
	<acme:list-column code="any.flight.list.label.origin" path="origin" width="20%"/>
	<acme:list-column code="any.flight.list.label.destination" path="destination" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

