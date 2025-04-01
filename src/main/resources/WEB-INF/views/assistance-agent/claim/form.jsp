<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="assistance-agent.claim.form.label.date" path="date"/>
	<acme:input-email code="assistance-agent.claim.form.label.email" path="email"/>
	<acme:input-textarea code="assistance-agent.claim.flight.form.label.description" path="description"/>
	<acme:input-textbox code="assistance-agent.claim.flight.form.label.status" path="status"/>
	<acme:input-select code="assistance-agent.claim.flight.form.label.type" path="type" choices="${types}"/>
	
	<jstl:choose>
        <jstl:when test="${_command == 'show'}">
            <acme:button code="assistance-agent.claim.formlabel.leg" action="/assistance-agent/leg/show?id=${legId}"/>
        </jstl:when>
    </jstl:choose>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
			<acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>