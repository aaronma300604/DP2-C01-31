<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.assignments.list.label.flightNumber" path="leg.flightNumber" width="10%"/>
	<acme:list-column code="flight-crew-member.assignments.list.label.moment" path="moment" width="10%"/>
	<acme:list-column code="flight-crew-member.assignments.list.label.status" path="currentStatus" width="10%"/>
	<acme:list-column code="flight-crew-member.assignments.list.label.scheduledArrival" path="leg.scheduledArrival" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:choose>
	<jstl:when test="${_command == 'listUL'}">
		<acme:button code="flight-crew-member.assignments.list.button.create" action="/flight-crew-member/flight-assignment/create"/>
	</jstl:when>
</jstl:choose>
