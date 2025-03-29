<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-moment code="technician.maintenance-record.form.label.date" path="date"/>
	<acme:input-select code="technician.maintenance-record.form.label.maintenanceStatus" path="maintenanceStatus" choices="${maintenanceStatuses}"/>	
	<acme:input-moment code="technician.maintenance-record.form.label.nextInspection" path="nextInspection"/>	
	<acme:input-money code="technician.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textbox code="technician.maintenance-record.form.label.notes" path="notes"/>
	<acme:input-select code="technician.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show'  && draftMode == false}">
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task/list?recordId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
			<acme:input-checkbox code="technician.maintenance-record.form.label.confirmation" path="confirmation"/>
			<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update"/>
			<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish"/>
			<acme:button code="technician.involves.list.button.create" action="/technician/involves/create?recordId=${id}"/>
			<acme:button code="technician.maintenance-record.form.button.tasks" action="/technician/task/list?recordId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="technician.maintenance-record.form.label.confirmation" path="confirmation"/>
			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>