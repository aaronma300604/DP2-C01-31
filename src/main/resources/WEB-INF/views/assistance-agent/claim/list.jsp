<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.claim.list.label.date" path="date" width="10%"/>
	<acme:list-column code="assistance-agent.claim.list.label.email" path="email" width="10%"/>
	<acme:list-column code="assistance-agent.claim.list.label.description" path="description" width="10%"/>
	<acme:list-column code="assistance-agent.claim.list.label.type" path="type" width="10%"/>
	<acme:list-column code="assistance-agent.claim.list.label.status" path="status" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="assistance-agent.claim.list.button.create" action="/assistance-agent/claim/create"/>
	