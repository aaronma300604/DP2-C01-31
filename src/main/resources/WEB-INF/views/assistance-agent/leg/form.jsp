<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="assistance-agent.leg.form.label.fligthNumber" path="flightNumber"/>
	<acme:input-moment code="assistance-agent.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="assistance-agent.leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-double code="assistance-agent.leg.form.label.duration" path="duration"/>
	<acme:input-select code="assistance-agent.leg.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-select code="assistance-agent.leg.form.label.flight" path="flight" choices="${flights}"/>
	<acme:input-select code="assistance-agent.leg.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-select code="assistance-agent.leg.form.label.origin" path="origin" choices="${origins}"/>
	<acme:input-select code="assistance-agent.leg.form.label.destination" path="destination" choices="${destinations}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="assistance-agent.leg.form.button.update" action="/assistance-agent/leg/update"/>
			<acme:submit code="assistance-agent.leg.form.button.delete" action="/assistance-agent/leg/delete"/>
			<acme:submit code="assistance-agent.leg.form.button.publish" action="/assistance-agent/leg/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.leg.form.button.create" action="/assistance-agent/leg/create"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>