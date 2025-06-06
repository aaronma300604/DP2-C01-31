<%--
- menu.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
			<acme:menu-option code="master.menu.any" access="true">
			<acme:menu-suboption code="master.menu.any.review-list" action="/any/review/list"/>
			<acme:menu-suboption code="master.menu.any.service-list" action="/any/service/list"/>
			<acme:menu-suboption code="master.menu.any.flight-list" action="/any/flight/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-dani" action="https://www.informatica.us.es/"/>
     		<acme:menu-suboption code="master.menu.anonymous.favourite-link-estrella" action="https://pointerpointer.com"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-aaron" action="https://www.youtube.com"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-ivan" action="https://github.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-diego" action="https://www.linkedin.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-suboption code="master.menu.administrator.show-dashboard" action="/administrator/dashboard/show" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="feature.menu.administrator.list-airports" action="/administrator/airport/list"/>
			<acme:menu-suboption code="feature.menu.administrator.list-aircrafts" action="/administrator/aircraft/list"/>
			<acme:menu-suboption code="feature.menu.administrator.list-maintenance-records" action="/administrator/maintenance-record/list"/>
			<acme:menu-suboption code="feature.menu.administrator.list-airlines" action="/administrator/airline/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-system-config" action="/administrator/system-config/list"/>
			<acme:menu-suboption code="feature.menu.administrator.list-services" action="/administrator/service/list"/>
			<acme:menu-suboption code="feature.menu.administrator.list-booking" action="/administrator/booking/list"/>
			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link-dani" action="https://www.informatica.us.es/"/>
			<acme:menu-suboption code="master.menu.provider.favourite-link-estrella" action="https://pointerpointer.com"/>
			<acme:menu-suboption code="master.menu.provider.favourite-link-aaron" action="https://www.youtube.com"/>
			<acme:menu-suboption code="master.menu.provider.favourite-link-ivan" action="https://github.com/"/>
			<acme:menu-suboption code="master.menu.provider.favourite-link-diego" action="https://www.linkedin.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link-dani" action="https://www.informatica.us.es/"/>
			<acme:menu-suboption code="master.menu.consumer.favourite-link-estrella" action="https://pointerpointer.com"/>
			<acme:menu-suboption code="master.menu.consumer.favourite-link-aaron" action="https://www.youtube.com"/>
			<acme:menu-suboption code="master.menu.consumer.favourite-link-ivan" action="https://github.com/"/>
			<acme:menu-suboption code="master.menu.consumer.favourite-link-diego" action="https://www.linkedin.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.manager" access="hasRealm('AirlineManager')">
			<acme:menu-suboption code="master.menu.manager.list-my-flights" action="/airline-manager/flight/list"/>
			<acme:menu-suboption code="master.menu.manager.list-my-legs" action="/airline-manager/leg/list"/>
			<acme:menu-suboption code="master.menu.manager.show-dashboard" action="/airline-manager/dashboard/show"/>
		</acme:menu-option>
    
		
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.booking" action="/customer/booking/list"/>
			<acme:menu-suboption code="master.menu.customer.passenger" action="/customer/passenger/list"/>
			<acme:menu-suboption code="master.menu.customer.passenger-booking" action="/customer/passenger-booking/list"/>
			<acme:menu-suboption code="master.menu.customer.customer-dashboard" action="/customer/customer-dashboard/show"/>
			
    </acme:menu-option>
		
		<acme:menu-option code="master.menu.technician" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technician.list-my-records" action="/technician/maintenance-record/list?mine=true"/>
			<acme:menu-suboption code="master.menu.technician.list-availiable-records" action="/technician/maintenance-record/list?mine=false"/>
			<acme:menu-suboption code="master.menu.technician.list-my-tasks" action="/technician/task/list?mine=true&recordId=-1"/>
			<acme:menu-suboption code="master.menu.technician.list-available-tasks" action="/technician/task/list?mine=false&recordId=-1"/>		
			<acme:menu-suboption code="master.menu.technician.show-dashboard" action="/technician/dashboard/show" />
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.member" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.member.list-my-assignments" action="/flight-crew-member/flight-assignment/list"/>
			<acme:menu-suboption code="master.menu.member.list-my-assignmentsUL" action="/flight-crew-member/flight-assignment/listUL"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.agent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.agent.list-my-undergoing-claims" action="/assistance-agent/claim/list"/>
			<%---<acme:menu-suboption code="master.menu.agent.show-dashboard" action="/agent/dashboard/show" />--%>
		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-technician" action="/authenticated/technician/create" access="!hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.technician-profile" action="/authenticated/technician/update" access="hasRealm('Technician')"/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/airline-manager/create" access="!hasRealm('AirlineManager')"/>
			<acme:menu-suboption code="master.menu.user-account.manager-profile" action="/authenticated/airline-manager/update" access="hasRealm('AirlineManager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-customer" action="/authenticated/customer/create" access="!hasRealm('Customer')"/>
			<acme:menu-suboption code="master.menu.user-account.customer-profile" action="/authenticated/customer/update" access="hasRealm('Customer')"/>	
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

