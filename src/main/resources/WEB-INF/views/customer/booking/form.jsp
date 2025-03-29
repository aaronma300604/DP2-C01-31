<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="customer.booking.form.locatorCode" path="locatorCode"/>
	<acme:input-moment code="customer.booking.form.purchaseMoment" path="purchaseMoment"/>
	<acme:input-select code="customer.booking.form.travelClass" path="travelClass" choices="${travelClass}"/>	
	<acme:input-money code="customer.booking.form.price" path="price"/>
	<acme:input-textbox code="customer.booking.form.lastCreditCardNibble" path="lastCreditCardNibble"/>
	<acme:input-select code="customer.booking.form.flight" path="flight" choices="${flight}"/>	

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
			<acme:submit code="employer.job.form.button.update" action="/employer/job/update"/>
			<acme:submit code="employer.job.form.button.delete" action="/employer/job/delete"/>
			<acme:submit code="employer.job.form.button.publish" action="/employer/job/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>