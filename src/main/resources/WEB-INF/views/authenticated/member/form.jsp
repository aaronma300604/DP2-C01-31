<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="authenticated.member.form.title"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="authenticated.member.form.label.last-five-destinations-assigned"/>
		</th>
		<td>
			<acme:print value=""/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="authenticated.member.form.label.legs-with-incidents"/>
		</th>
		<td>
			<acme:print value=""/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="authenticated.member.form.label.last-legs-crew-members"/>
		</th>
		<td>
			<acme:print value=""/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="authenticated.member.form.label.Assignments-by-status"/>
		</th>
		<td>
			<acme:print value=""/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="authenticated.member.form.label.deviation-statistics"/>
		</th>
		<td>
			<acme:print value=""/>
		</td>
	</tr>		
</table>

<h2>
	<acme:print code="authenticated.member.form.title.application-statuses"/>
</h2>

<div>
	<canvas id="canvas"></canvas>
</div>
<acme:return/>