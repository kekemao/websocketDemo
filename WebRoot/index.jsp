<%@ page language="java" contentType="text/html; charset=gb2312" pageEncoding="gb2312"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>websocket实例</title>
<link type="text/css"  rel="stylesheet" href="css/style.css" />
 <script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
</head>
<body>
<script type="text/javascript">

	var ws = null;
	$(function(){
	startWebSocket();
	});
	var startWebSocket=function () {	
		if('WebSocket' in window){		
			try{
				ws = new WebSocket("ws://localhost:80/testWebsocket1/servlet/MyWebSocketServlet");
			}catch(e){
				alert("1");
			}
		}else if ('MozWebSocket' in window){
			    ws = new MozWebSocket("ws://localhost:80/testWebsocket1/servlet/MyWebSocketServlet");
		}else{
			alert("not support");   
		}   
	    ws.onopen = function(evt) {};  
		ws.onmessage = function(evt) {	
		 handle(evt.data);
		};   
		ws.onclose = function(evt) {   
			alert("服务中断！！!");   
		};   
	}
	
    var code="to";
     var flag="query_one";
    function msg(){
		var val=$("#file_nm").val();
		code = code.split("to")[1]+"to"+val;
		$("#log_name").text(m2);
		flag="query_one";
		ws.send(code);	
    }
       function msg2(){
		var val=$("#m2").val()+".trc";
		code = code.split("to")[1]+"to"+val;
		flag="query_one";
		ws.send(code);	
		
    }		
   
	var sendMsg=function(id) {
		if (id.indexOf("%") > -1){
		flag="query_more";
		// document.getElementById(id).disabled="true";
		  ws.send(id);	  
		} 
	}
			
	var handle=function(msg){
		 var num1=msg.indexOf('[');
		 var num2=msg.indexOf(']');
		 var m1=msg.substring(0,num1+1).toString();
		 var m2=msg.substring(num1+1,num2).toString();
		 var m3=msg.substring(num2).toString();
		 
		 if(flag==="query_more"){
		    $("#datas").text("");
			var p = document.createElement("p");
		    $("#m2").val(m2);
		    
			p.innerHTML += m1+'<a href="#" onClick="msg2()">'+m2+'</a>'+m3;
			$("#log_name").text(m2);
			flag="auto_add";
			$("#datas").prepend(p); 
			
		}else if(flag==="query_one"){
			$("#datas").text("");
			var p = document.createElement("p");
			$("#m2").val(m2);
			
			p.innerHTML += m1+'<a href="#" onClick="msg2()">'+m2+'</a>'+m3;
			$("#log_name").text(m2);
			flag="auto_add";
			$("#datas").append(p);
		
		} else if(flag==="auto_add"){
			var p = document.createElement("p");
			$("#m2").val(m2);
			
			p.innerHTML += m1+'<a href="#" onClick="msg2()">'+m2+'</a>'+m3;
			$("#log_name").text(m2);
			$("#datas").append(p);
			window.scrollTop=document.body.scrollHeight;
		} 		  
	}
	var Submit=function(){
	    var lintSum = $("#lineNum").val();
		sendMsg("%%"+lintSum);
	}
</script>
  <div class="top">   当前日志名：<span class="inp wid" id="log_name"></span><span><strong class="spanset">ICS日志</strong></span>
                               
            &nbsp; &nbsp;请输入实例名(例如：PAY)：
            <input class="inp wid" type="hidden"  id="m2"></input>
			<select class="inp wid"   id="file_nm">
				<option value="TXNPLT_PAY">PAY</option>
				<option value="WEB">WEB</option>
			</select>
			<input class="inp" type="button" value="确定"  onClick="msg()"></input></div>
  	<div>
	    <div  >
	    	
	    	<div id="info" class="infoPanel">
	    	<div class="huoQuNum">	    	
	    		<span class="fr-pad"> <button id="Submit" onclick="Submit();">确定</button></span>
	    		<span class="fr-pad">前<input class="lineNum" id="lineNum" name="lineNum"/>行</span> 
		    	<span class="fr-pad"><button id="%%20" onClick="sendMsg(this.id)">前20条</button></span>
		    	<span class="fr-pad"><button id="%%10" onClick="sendMsg(this.id)">前10条</button></span>
		    	<span class="fr-pad"><button id="%%5" onClick="sendMsg(this.id)">前5条</button></span>		    	
	    	</div>
	    	  <div id="datas">
	    	  </div>
	    	</div>
	    </div>
    </div>
</body>  
</html>