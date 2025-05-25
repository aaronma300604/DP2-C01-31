<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="customer.customer-dashboard.form.label.lastFiveDestinations"/>
		</th>
		<td>
			<acme:print value="${lastFiveDestinations}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:print code="customer.customer-dashboard.form.label.moneySpent"/>
		</th>
		<td>
			<acme:print value="${moneySpent}"/>
		</td>
	</tr>
		<tr>
		<th scope="row">
			<acme:print code="customer.customer-dashboard.form.label.bookingsByEconomy"/>
		</th>
		<td>
			<acme:print value="${bookingsByEconomy}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.customer-dashboard.form.label.bookingsByBusiness"/>
		</th>
		<td>
			<acme:print value="${bookingsByBusiness}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.customer-dashboard.form.label.costBooking"/>
		</th>
		<td>
			<acme:print value="${costBooking}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="customer.customer-dashboard.form.label.passengerBooking"/>
		</th>
		<td>
			<acme:print value="${passengerBooking}"/>
		</td>
	
</table>
<acme:return/>