<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
		
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete') && bookingDraftMode == false}"   >	
		<acme:input-select code="customer.passenger.list.passportNumber" path="passenger" choices="${passengerChoices}" readonly="true"/>	
		<acme:input-select code="customer.booking.list.locatorCode" path="booking" choices="${bookingChoices}" readonly="true"/>	
		</jstl:when> 	
		<jstl:when test="${acme:anyOf(_command, 'show|delete') && bookingDraftMode == true}"   >	
		<acme:input-select code="customer.passenger.list.passportNumber" path="passenger" choices="${passengerChoices}" readonly="true"/>	
		<acme:input-select code="customer.booking.list.locatorCode" path="booking" choices="${bookingChoices}" readonly="true"/>	
			<acme:submit code="customer.passenger-booking.form.delete" action="/customer/passenger-booking/delete"/>
		</jstl:when> 	
		<jstl:when test="${_command == 'create'}">
		<acme:input-select code="customer.passenger.list.passportNumber" path="passenger" choices="${passengerChoices}"/>	
		<acme:input-select code="customer.booking.list.locatorCode" path="booking" choices="${bookingChoices}"/>	
			<acme:submit code="customer.passenger-booking.form.button.create" action="/customer/passenger-booking/create"/>
		</jstl:when>	
	</jstl:choose>
	
</acme:form>