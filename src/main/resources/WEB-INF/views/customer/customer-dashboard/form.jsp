<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textarea code="customer.customer-dashboard.form.label.lastFiveDestinations" path="lastFiveDestinations"/>
	<acme:input-double code="customer.customer-dashboard.form.label.moneySpent" path="moneySpent"/>
	<acme:input-textbox code="customer.customer-dashboard.form.label.bookingsByEconomy" path="bookingsByEconomy"/>
	<acme:input-textbox code="customer.customer-dashboard.form.label.bookingsByBusiness" path="bookingsByBusiness"/>
	<acme:input-textarea code="customer.customer-dashboard.form.label.costBooking" path="costBooking"/>
	<acme:input-textarea code="customer.customer-dashboard.form.label.passengerBooking" path="passengerBooking"/>
</acme:form>