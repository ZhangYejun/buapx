<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:directive.include file="../../default/ui/includes/top.jsp" />
<script type="text/javascript" src="./js/buapxProtocol.js"></script>
<%-- --%> 
   <script type="text/javascript">
        function plugin0()
        {
            return document.getElementById('plugin0');
        }
        plugin = plugin0;
        function addEvent(obj, name, func)
        {
            if (obj.attachEvent) {
                obj.attachEvent("on"+name, func);
            } else {
                obj.addEventListener(name, func, false); 
            }
        }
        
        function load()
        {
            addEvent(plugin(), 'test', function(){
                alert("Received a test event from the plugin.")
            });
        }
        function pluginLoaded() {
            alert("Plugin loaded!");
        }
        
        function addTestEvent()
        {
            addEvent(plugin(), 'echo', function(txt,count){
                alert(txt+count);
            });
        }
        
        function testEvent()
        {
            plugin().testEvent();
        }
        
        function pluginValid()
        {
            if(plugin().valid){
                alert(plugin().echo("This plugin seems to be working!"));
            } else {
                alert("Plugin is not working :(");
            }
        }
        </script> 
        <body onload="load()">
<object id="plugin0" classid="clsid:98FEF955-00B3-41F8-8B00-F3ED2BBA5CA9" <param name="onload" value="pluginLoaded" />
</object><br />

<script>
plugin().LoginByTicket('${ticket}');
</script>

<jsp:directive.include file="../../default/ui/includes/bottom.jsp" />