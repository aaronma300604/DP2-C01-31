<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="airline-manager.flight.form.label.tag" path="tag"/>
	<acme:input-checkbox code="airline-manager.flight.form.label.selfTransfer" path="selfTransfer"/>
	<acme:input-money code="airline-manager.flight.form.label.cost" path="cost"/>
	<acme:input-textarea code="airline-manager.flight.form.label.description" path="description"/>
	<acme:input-select code="airline-manager.flight.form.label.airline" path="airline" choices="${airlines}"/>
	<acme:input-moment code="airline-manager.flight.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
	<acme:input-moment code="airline-manager.flight.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
	<acme:input-integer code="airline-manager.flight.form.label.layovers" readonly="true" path="layovers"/>
	<acme:input-textbox code="airline-manager.flight.form.label.origin" path="origin" readonly="true"/>
	<acme:input-textbox code="airline-manager.flight.form.label.destination" path="destination" readonly="true"/>
</acme:form>