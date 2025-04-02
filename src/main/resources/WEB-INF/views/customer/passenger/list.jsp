<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.passportNumber" path="passportNumber" width="25%"/>
	<acme:list-column code="customer.passenger.list.dateOfBirth" path="dateOfBirth" width="25%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list' && empty isFromBooking || isFromBooking == false }">
	<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
</jstl:if>	
	
<p>Booking ID: ${isFromBooking}</p>