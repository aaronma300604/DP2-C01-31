<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="any.review.form.label.name" path="reviewerName"/>
	<acme:input-textbox code="any.review.form.label.subject" path="subject"/>	
	<acme:input-textbox code="any.review.form.label.text" path="text"/>	
	<acme:input-double code="any.review.form.label.score" path="score"/>
	<acme:input-checkbox code="any.review.form.label.recommended" path="recommended"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:input-checkbox code="any.review.form.label.confirmation" path="confirmation"/>
		<acme:submit code="any.review.form.button.create" action="/any/review/create"/>
	</jstl:if>

	
</acme:form>