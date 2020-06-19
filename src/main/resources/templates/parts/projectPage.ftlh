<#import "navbarForm.ftlh" as navbar>
<#import "common.ftlh" as common>

<#macro head title>
    <@common.head title = title>
        <!-- Required meta tags. Сount pixel density for normal display on various screens-->
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <#nested>
    </@common.head>
</#macro>

<#macro body navBarTitle>
     <@common.body>
        <@navbar.navbarForm title = navBarTitle/>
        <#nested>
    </@common.body>
</#macro>

<#assign isKnown = Session.SPRING_SECURITY_CONTEXT??>
<#if isKnown>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    userName = user.getUsername()
    isAdmin = user.isAdmin()
    userId = user.getId()
    >
<#else>
    <#assign
    userName = "unknown"
    isAdmin = false
    userId = -1
    >
</#if>





