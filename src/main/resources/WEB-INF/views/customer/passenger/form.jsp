<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="customer.passenger.form.name" path="name"/>
	<acme:input-textbox code="customer.passenger.form.email" path="email"/>
	<acme:input-textbox code="customer.passenger.form.passportNumber" path="passportNumber"/>	
	<acme:input-moment code="customer.passenger.form.dateOfBirth" path="dateOfBirth"/>
	<acme:input-textbox code="customer.passenger.form.specialNeeds" path="specialNeeds"/>
	
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode }"  >		
			<acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update"/>
			<acme:submit code="customer.passenger.form.button.publish" action="/customer/passenger/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.passenger.form.button.create" action="/customer/passenger/create"/>
		</jstl:when>		
	</jstl:choose>
	
</acme:form>