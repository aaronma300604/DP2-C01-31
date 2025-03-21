<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airline-manager.leg.form.label.fligthNumber" path="flightNumber"/>
	<acme:input-moment code="airline-manager.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="airline-manager.leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-integer code="airline-manager.leg.form.label.duration" path="duration"/>
	<acme:input-select code="airline-manager.leg.form.label.status" path="status" choices="${statuses}" readonly="${acme:anyOf(status, 'ON_TIME|DELAYED|CANCELLED|LANDED')}"/>
	<acme:input-select code="airline-manager.leg.form.label.flight" path="flight" choices="${flights}"/>
	<acme:input-select code="airline-manager.leg.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-select code="airline-manager.leg.form.label.origin" path="origin" choices="${origins}"/>
	<acme:input-select code="airline-manager.leg.form.label.destination" path="destination" choices="${destinations}"/>
</acme:form>