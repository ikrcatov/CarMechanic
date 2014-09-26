<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:url value="/resources/css/table.css" var="tableCssUrl" />
<link rel="stylesheet" type="text/css" media="screen" href="${tableCssUrl}" />
  
<c:url value="/resources/css/base.css" var="baseCssUrl" />
<link rel="stylesheet" type="text/css" media="screen" href="${baseCssUrl}" />
  
<c:url value="/resources/css/skeleton.css" var="skeletonCssUrl" />
<link rel="stylesheet" type="text/css" media="screen" href="${skeletonCssUrl}" />

<c:url value="/resources/css/layout.css" var="layoutCssUrl" />
<link rel="stylesheet" type="text/css" media="screen" href="${layoutCssUrl}" />
  
<c:url value="/resources/css/toastr.css" var="toastrCssUrl" />
<link rel="stylesheet" type="text/css" media="screen" href="${toastrCssUrl}" />
  
<c:url value="/resources/css/toastr.min.css" var="toastrMinCssUrl" />
<link rel="stylesheet" type="text/css" media="screen" href="${toastrMinCssUrl}" />
  
<c:url value="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery.ui.all.css" var="jquery2" />
<link rel="stylesheet" type="text/css" media="screen" href="${jquery2}" />