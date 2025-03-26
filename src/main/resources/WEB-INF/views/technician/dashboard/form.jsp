<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="technician.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.average-number-jobs-employer"/>
		</th>
		<td>
			<acme:print value="${airportsByScope}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.average-number-applications-worker"/>
		</th>
		<td>
			<acme:print value="${airlinesByType}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.average-number-applications-employer"/>
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