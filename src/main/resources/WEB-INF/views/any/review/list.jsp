<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.review.list.label.review" path="text" width="100%"/>
	
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="any.review.list.button.create" action="/any/review/create"/>

