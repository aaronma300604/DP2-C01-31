<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="airline-manager.leg.list.label.flightNumber" path="flightNumber" sortable="false" width="30%"/>
	<acme:list-column code="airline-manager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="10%"/>
	<acme:list-column code="airline-manager.leg.list.label.scheduledArrival" path="scheduledArrival" sortable="false" width="20%"/>
	<acme:list-column code="airline-manager.leg.list.label.origin" path="origin" sortable="false" width="20%"/>
	<acme:list-column code="airline-manager.leg.list.label.destination" path="destination" sortable="false" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>