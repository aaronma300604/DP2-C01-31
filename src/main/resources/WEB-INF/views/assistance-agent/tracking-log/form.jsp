<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    
    <jstl:choose>
        <jstl:when test="${draftMode == false}">
            <acme:input-moment code="assistance-agent.tracking-log.list.label.last-update" path="lastUpdate"/>
        </jstl:when>
    </jstl:choose>
    <acme:input-textbox code="assistance-agent.tracking-log.list.label.step" path="stepUndergoing"/>
    <acme:input-double code="assistance-agent.tracking-log.list.label.percentage" path="resolutionPercentage"/>
    <acme:input-textarea code="assistance-agent.tracking-log.list.label.resolution" path="resolution"/>
    <acme:input-select code="assistance-agent.tracking-log.list.label.acceptance-status" path="accepted" choices="${types}"/>
    
    
    <jstl:choose>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
        	<jstl:if test="${creatable == true}">
				<acme:submit code="assistance-agent.tracking-log.form.button.update" action="/assistance-agent/tracking-log/update"/>
			</jstl:if>
            <acme:submit code="assistance-agent.tracking-log.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
            <jstl:if test="${creatable == true}">
				<acme:submit code="assistance-agent.tracking-log.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
			</jstl:if>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="assistance-agent.tracking-log.form.button.create" action="/assistance-agent/tracking-log/create?claimid=${param.claimid}"/>
        </jstl:when>    
    </jstl:choose>
</acme:form>