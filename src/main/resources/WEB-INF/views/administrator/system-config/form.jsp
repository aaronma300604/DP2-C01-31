<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="administrator.sysconf.form.label.currency" path="currency"/>
	<acme:input-checkbox code="administrator.sysconf.form.label.systemCurrency" path="systemCurrency"/>
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="administrator.sysconf.form.button.update" action="/administrator/system-config/update"/>
			<acme:submit code="administrator.sysconf.form.button.delete" action="/administrator/system-config/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.sysconf.form.button.create" action="/administrator/system-config/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>