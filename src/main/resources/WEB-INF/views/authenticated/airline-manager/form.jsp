<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-integer code="authenticated.airline-manager.form.label.experience" path="experience"/>
	<acme:input-moment code="authenticated.airline-manager.form.label.birth" path="birth"/>
	<acme:input-url code="authenticated.airline-manager.form.label.link" path="link"/>
	<acme:input-select code="authenticated.airline-manager.form.label.airline" path="airline" choices="${airlines}"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.airline-manager.form.button.create" action="/authenticated/airline-manager/create"/>
	</jstl:if>

	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.airline-manager.form.button.update" action="/authenticated/airline-manager/update"/>
	</jstl:if>
</acme:form>