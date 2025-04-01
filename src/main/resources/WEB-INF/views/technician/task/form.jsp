<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="technician.task.form.label.type" path="type" choices="${types}"/>
	<acme:input-textbox code="technician.task.form.label.description" path="description"/>	
	<acme:input-integer code="technician.task.form.label.priority" path="priority"/>	
	<acme:input-integer code="technician.task.form.label.estimatedDuration" path="estimatedDuration"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish|delete') && draftMode == true}">
			<acme:input-checkbox code="technician.task.form.label.confirmation" path="confirmation"/>
			<acme:submit code="technician.task.form.button.update" action="/technician/task/update"/>
			<acme:submit code="technician.task.form.button.publish" action="/technician/task/publish"/>
			<acme:submit code="technician.task.form.button.delete" action="/technician/task/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="technician.task.form.label.confirmation" path="confirmation"/>
			<acme:submit code="technician.task.form.button.create" action="/technician/task/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>