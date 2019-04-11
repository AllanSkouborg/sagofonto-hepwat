<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
  <title>Apache Tomcat WebSocket Examples: Echo</title>
  <style type="text/css">

    #connect-container {
      float: left;
      width: 100%
    }

    #connect-container div {
      padding: 5px;
    }

    #console-container {
      float: left;
      height: 600px;
      width: 100%
    }

    #console {
      border: 1px solid #CCCCCC;
      border-right-color: #999999;
      border-bottom-color: #999999;
      height: 100%;
      overflow-y: scroll;
      padding: 5px;
      width: 100%
    }

    #console p {
      padding: 0;
      margin: 0;
    }

  </style>

  <script type="application/javascript">
      var ws = null;

      function setConnected(connected) {
          document.getElementById('connect').disabled = connected;
          document.getElementById('disconnect').disabled = !connected;
//          document.getElementById('echo').disabled = !connected;
      }

      function connect() {
 //         alert('connect');
          var target = document.getElementById('target').value;

          //init subscription text

          var subscriptiondataDiv = document.getElementById("subscriptiondata");
          var subscription1 = {hepwatDeviceId: 147850, calctype: 0, aggtype: 0, datatype: 0};
          var subscription2 = {hepwatDeviceId: 147850, calctype: 0, aggtype: 0, datatype: 1};
          //var subscription2 = {hepwatDeviceId: 132385, calctype: 0, aggtype: 0, datatype: 0};

          var subscriptions = [subscription1, subscription2];
          var subscriptionMessage = {type:2, subscriptions:subscriptions};
          var subscriptionJson = JSON.stringify(subscriptionMessage, null, 2);

          var textNode = document.createTextNode(subscriptionJson.toString());
          var textarea = document.createElement("TEXTAREA");
          textarea.setAttribute("id", "subscriptiondatatext")
          textarea.setAttribute("rows","20");
          textarea.setAttribute("cols", "100");
          textarea.appendChild(textNode);
          subscriptiondataDiv.appendChild(textarea);

          if (target == '') {
              alert('Please select server side connection implementation.');
              return;
          }

          if ('WebSocket' in window) {
              ws = new WebSocket(target);
          } else if ('MozWebSocket' in window) {
              ws = new MozWebSocket(target);
          } else {
              alert('WebSocket is not supported by this browser.');
              return;
          }

          ws.onopen = function (event) {
              setConnected(true);

              log(':onopen> ' + event.data);
          };

          ws.onmessage = function (event) {
              log(':onmessage> ' + event.data);
          };

          ws.onclose = function (event) {
              setConnected(false);
              log(':onclose> ' + event.data);
              log('Info: WebSocket connection closed, Code: ' + event.code + (event.reason == "" ? "" : ", Reason: " + event.reason));
          };
      }

      function disconnect() {
          alert('disconnect');
          if (ws != null) {
              ws.close();
              ws = null;
          }
          setConnected(false);
      }
      function JoinMessage(name) {
          this.type = 1;
          this.name = name;
      }
      function join() {
//          alert('join');

            if (ws) {
  //              alert(JSON.stringify(new JoinMessage('Margit')));
                ws.send(JSON.stringify(new JoinMessage('Margit')));
            }

      }

      function subscribe() {
          //alert('subscribe');
/*

          //try 1
          var subscriptionContent = document.getElementById('subscriptiondata');

          var subscriptiondata = subscriptionContent.firstElementChild.innerHTML;
//          alert (subscriptiondata);
          var subscriptionMessage = JSON.parse(subscriptiondata);
//          alert("subscriptiondata parsed");

*/
          ////try 2

          var subscriptionContent = document.getElementById('subscriptiondatatext');
          //if (subscriptionContent != null)
          //    alert("not null")
          var subscriptiondata = subscriptionContent.textContent;
          //alert (subscriptiondata);
          var subscriptionMessage = JSON.parse(subscriptiondata);

/*          //// static Message
          var subscription1 = {hepwatDeviceId: 1003, calctype: 0, aggtype: 0};
          var subscription2 = {hepwatDeviceId: 1002, calctype: 0, aggtype: 1};
          var subscriptions = [subscription1, subscription2];
          var subscriptionMessage = {type:2, subscriptions:subscriptions};
          //
*/
          if (ws) {
//              alert('going to send subscribtions');
              alert(JSON.stringify(subscriptionMessage));
              ws.send(JSON.stringify(subscriptionMessage));
          }

      }

      function log(message) {
          var console = document.getElementById('console');
          var p = document.createElement('p');
          p.style.wordWrap = 'break-word';
          p.appendChild(document.createTextNode(message));
          console.appendChild(p);
          while (console.childNodes.length > 25) {
              console.removeChild(console.firstChild);
          }
          console.scrollTop = console.scrollHeight;
      }
  </script>

</head>
<body>
<div>
  <div id="connect-container">
    <div>
      <%--<input id="target" type="text" size="40" style="width: 350px" value="ws://localhost:8080/kafka/consumer?topics=igss_local_test_3&topicType=IGSS" />--%>
      <input id="target" type="text" size="40" style="width: 350px" value="ws://localhost:8080/data" />
    </div>
    <div id="subscriptiondata">
      <%--<<input id="subscription" type="text" style="width:400pt;height: 100pt"  value='[\n{"hepwatDeviceId":1003,"calctype":0, "aggtype":0 }\n]' /> --%>
  <%--<textarea rows="4" cols="50" readonly="false" >
  </textarea>--%>
    </div>

<div>
<button id="connect" onclick="connect();">Connect</button>
<button id="join" onclick="join();">Join</button>
<button id="startsubscription" onclick="subscribe();">Start subscription</button>
<button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
</div>
</div>
<div id="console-container">
<div id="console" />
</div>
</div>
</body>
</html>