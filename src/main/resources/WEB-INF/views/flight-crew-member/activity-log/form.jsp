<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="flight-crew-member.logs.list.label.moment" path="moment"  readonly ="true"/>
	<acme:input-textbox code="flight-crew-member.logs.list.label.incident" path="incident"/>
	<acme:input-textbox code="flight-crew-member.logs.list.label.description" path="description"/>
	<acme:input-integer code="flight-crew-member.logs.list.label.severityLevel" path="severityLevel"/>
	<acme:input-select code="flight-crew-member.logs.list.label.assignments" path="assignment" choices="${assignmentChoices}"/>
	
	<jstl:choose>
	
	<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">	
			<acme:submit code="flight-crew-member.activity-log.form.button.update" action="/flight-crew-member/activity-log/update"/>
			<acme:submit code="flight-crew-member.activity-log.form.button.delete" action="/flight-crew-member/activity-log/delete"/>
			<acme:submit code="flight-crew-member.activity-log.form.button.publish" action="/flight-crew-member/activity-log/publish"/>
	</jstl:when>
	
	<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.activity-log.form.button.create" action="/flight-crew-member/activity-log/create"/>
	</jstl:when>
	</jstl:choose>
	
</acme:form>