//HTML source code
const char INDEX_HTML[] = R"rawliteral(
<!DOCTYPE html>
<html>
<head>
<meta name = "viewport" content = "width = device-width, initial-scale = 1.0, maximum-scale = 1.0, user-scalable=0">
<title>LEDs con WebSockets</title>
<style>
body {
  font-family: Arial, Helvetica, Sans-Serif; 
  Color: #000000; 
  text-align: center;
}

button:hover{
  transform:scale(1.5);
}
#ledTwoStatus{
  margin:10px;
}
</style>
<script>
var websock;
function start() {
  websock = new WebSocket('ws://' + window.location.hostname + ':81/');
  websock.onopen = function(evt) { console.log('websock open'); };
  websock.onclose = function(evt) { console.log('websock close'); };
  websock.onerror = function(evt) { console.log(evt); };
}
function buttonclick(e) {
  websock.send(e.id);
}
</script>
</head>
<body onload="javascript:start();">
<h1>LEDs con WebSockets</h1>
<div id="ledOneStatus"><b>LED1</b></div>
<button id="ledOneOn"  type="button" onclick="buttonclick(this);">On</button> 
<button id="ledOneOff" type="button" onclick="buttonclick(this);">Off</button>
<div id="ledTwoStatus"><b>LED2</b></div>
<button id="ledTwoOn"  type="button" onclick="buttonclick(this);">On</button> 
<button id="ledTwoOff" type="button" onclick="buttonclick(this);">Off</button>
</body>
</html>
)rawliteral";