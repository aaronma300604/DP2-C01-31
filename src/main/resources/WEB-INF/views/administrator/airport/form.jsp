<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.airport.form.label.name" path="name"/>
	<acme:input-textbox code="administrator.airport.form.label.iata" path="iata"/>	
	<acme:input-select code="administrator.airport.form.label.operationalScope" path="operationalScope" choices="${operationalScopes}"/>	
	<acme:input-textbox code="administrator.airport.form.label.city" path="city"/>
	<acme:input-textbox code="administrator.airport.form.label.country" path="country"/>
	<acme:input-url code="administrator.airport.form.label.website" path="website"/>
	<acme:input-email code="administrator.airport.form.label.email" path="email"/>
	<acme:input-textbox code="administrator.airport.form.label.phone" path="phone" placeholder="administrator.airport.form.text.phone"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:input-checkbox code="administrator.airport.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airport.form.button.update" action="/administrator/airport/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="administrator.airport.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airport.form.button.create" action="/administrator/airport/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>