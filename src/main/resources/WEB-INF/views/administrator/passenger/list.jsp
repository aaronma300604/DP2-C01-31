<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.passenger.list.passportNumber" path="passportNumber" width="25%"/>
	<acme:list-column code="administrator.passenger.list.dateOfBirth" path="dateOfBirth" width="25%"/>
	<acme:list-payload path="payload"/>
</acme:list>

