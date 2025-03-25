<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.moment" path="moment"/>
	<acme:input-select path="duty" code="flight-crew-member.flight-assignment.form.label.duty" choices="${duties}"  />
	<acme:input-select path="currentStatus" code="flight-crew-member.flight-assignment.form.label.status" choices="${statuses}"  />
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>
</acme:form>