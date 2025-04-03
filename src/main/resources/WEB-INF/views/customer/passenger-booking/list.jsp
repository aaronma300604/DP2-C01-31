<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.passportNumber" path="passenger.passportNumber" width="25%"/>
	<acme:list-column code="customer.booking.list.locatorCode" path="booking.locatorCode" width="30%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="customer.passenger-booking.list.button.create" action="/customer/passenger-booking/create"/>
</jstl:if>		