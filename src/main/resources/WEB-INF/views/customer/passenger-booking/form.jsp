<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="customer.passenger.list.passportNumber" path="passenger.passportNumber" choices="${passengerChoices}"/>	
	<acme:input-select code="customer.booking.list.locatorCode" path="booking.locatorCode" choices="${bookingChoices}"/>	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')  && draftMode == true}"  >		
			<acme:submit code="customer.passenger-booking.form.update" action="/customer/passenger-booking/update"/>
		</jstl:when> 	
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.passenger-booking.form.button.create" action="/customer/passenger-booking/create"/>
		</jstl:when>	
	</jstl:choose>
	
</acme:form>