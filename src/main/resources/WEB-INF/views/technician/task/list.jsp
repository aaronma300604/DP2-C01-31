<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.task.list.label.type" path="type" width="33%"/>
	<acme:list-column code="technician.task.list.label.priority" path="priority" width="33%"/>
	<acme:list-column code="technician.task.list.label.estimatedDuration" path="estimatedDuration" width="33%"/>
	
	
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'&& $request.data.recordId != -1 }">
	<acme:button code="technician.involves.list.button.create" action="/technician/involves/create?recordId=${$request.data.recordId}"/>
</jstl:if>
<jstl:if test="${_command == 'list'&& $request.data.recordId == -1}">
	<acme:button code="technician.task.list.button.create" action="/technician/task/create"/>
</jstl:if>
