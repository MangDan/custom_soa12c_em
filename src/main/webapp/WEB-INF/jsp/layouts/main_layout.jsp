<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
  <head>
    <title><tiles:getAsString name="title"/></title>
  </head>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <table width="100%">
      <tr>
        <td>
          <tiles:insertAttribute name="header" />
        </td>
      </tr>
      <tr>
        <td>
          <tiles:insertAttribute name="body" />
        </td>
      </tr>
      <tr>
        <td>
          <tiles:insertAttribute name="footer" />
        </td>
      </tr>
    </table>
  </body>
</html>