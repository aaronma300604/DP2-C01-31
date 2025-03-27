<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="administrator.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.domesticAirpots"/>
		</th>
		<td>
			<acme:print value="${domesticAirports}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.internationalAirports"/>
		</th>
		<td>
			<acme:print value="${internationalAirports}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.regionalAirports"/>
		</th>
		<td>
			<acme:print value="${regionalAirports}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.luxuryAirlines"/>
		</th>
		<td>
			<acme:print value="${luxuryAirlines}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.standardAirlines"/>
		</th>
		<td>
			<acme:print value="${standardAirlines}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.lowCostAirlines"/>
		</th>
		<td>
			<acme:print value="${lowCostAirlines}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.average-number-airlines-with-email-phone"/>
		</th>
		<td>
			<acme:print value="${ratioAirlinesWithEmailOrPhone}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.average-active-Aircrafts"/>
		</th>
		<td>
			<acme:print value="${ratioActiveAircrafts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.average-nonActive-Aircrafts"/>
		</th>
		<td>
			<acme:print value="${ratioNotActiveAircrafts}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.average-score-reviews-above-5"/>
		</th>
		<td>
			<acme:print value="${ratioRewiewsScoreAbove5}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.countReviews"/>
		</th>
		<td>
			<acme:print value="${countReviews}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.averageReviews"/>
		</th>
		<td>
			<acme:print value="${averageReviews}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.minimumReviews"/>
		</th>
		<td>
			<acme:print value="${minimumReviews}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.maximumReviews"/>
		</th>
		<td>
			<acme:print value="${maximumReviews}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.form.label.desviationReviews"/>
		</th>
		<td>
			<acme:print value="${deviationReviews}"/>
		</td>
	</tr>	
</table>
<acme:return/>

