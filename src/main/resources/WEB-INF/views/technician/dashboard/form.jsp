<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="technician.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.number-maintenance-status"/>
		</th>
		<td>
			<acme:print value="${airportsByScope}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.nearest-next-inspection"/>
		</th>
		<td>
			<acme:print value="${airlinesByType}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.most-tasks-aircarfts"/>
		</th>
		<td>
			<acme:print value="${ratioAirlinesWithEmailOrPhone}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.cost-statistics"/>
		</th>
		<td>
			<acme:print value="${ratioAirlinesWithEmailOrPhone}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.duration-statistics"/>
		</th>
		<td>
			<acme:print value="${ratioAirlinesWithEmailOrPhone}"/>
		</td>
	</tr>	
</table>

<h2>
	<acme:print code="technician.dashboard.form.title.application-statuses"/>
</h2>

<div>
	<canvas id="canvas"></canvas>
</div>
<acme:return/>