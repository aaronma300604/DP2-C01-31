<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.tracking-log.list.label.last-update" path="lastUpdate" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.step" path="stepUndergoing" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.percentage" path="resolutionPercentage" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.resolution" path="resolution" width="10%"/>
	<acme:list-column code="assistance-agent.tracking-log.list.label.acceptance-status" path="accepted" width="10%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${creatable != false}">
	<acme:button code="assistance-agent.tracking-log.list.button.create" action="/assistance-agent/tracking-log/create?claimid=${param.claimid}"/>
</jstl:if>

	