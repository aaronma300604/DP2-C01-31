<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.booking.form.locatorCode" path="locatorCode" readonly="true"/>
	<acme:input-moment code="administrator.booking.form.purchaseMoment" path="purchaseMoment" readonly="true" />
	<acme:input-select code="administrator.booking.form.travelClass" path="travelClass" choices="${travelClasses}"/>	
	<acme:input-money code="administrator.booking.form.price" path="price" readonly="true"/>
	<acme:input-textbox code="administrator.booking.form.lastCreditCardNibble" path="lastCreditCardNibble"/>
	<acme:input-select code="administrator.booking.form.flight" path="flight" choices="${flights}"/>	

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && !draftMode && existsAnyPassenger }">
		<acme:button code="administrator.booking.form.show.passengers" action="/administrator/passenger/list?bookingId=${bookingId}&isFromBooking=${isFromBooking}"/>
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode && existsAnyPassenger}">
			<acme:button code="administrator.booking.form.show.passengers" action="/administrator/passenger/list?bookingId=${bookingId}&isFromBooking=${isFromBooking }"/>
		
		</jstl:when>
			
	</jstl:choose>
</acme:form>