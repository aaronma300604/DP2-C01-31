<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.logs.list.label.moment" path="moment" width="10%"/>
	<acme:list-column code="flight-crew-member.logs.list.label.incident" path="incident" width="10%"/>
	<acme:list-column code="flight-crew-member.logs.list.label.severityLevel" path="severityLevel" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="flight-crew-member.logs.list.button.create" action="/flight-crew-member/activity-log/create"/>