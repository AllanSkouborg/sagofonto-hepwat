module.exports = function(RED) {

    function SagofontoSigfoxTokenNode(config) {

	    RED.nodes.createNode(this, config);
	
        var node = this;
		var request = require("request");
        var _client_id;
        var _client_secret;
        var _body;
		var options;

		try {
		
			node.clientId = config.clientId;
			node.clientSecret = config.clientSecret;
			
			_client_id = node.clientId;
			_client_secret = node.clientSecret;
						
			node.status({ fill: "green", shape: "ring", text: 'ready to get sigfox token...' });
							
			node.on('input', function(msg) {

			    _body = '{"client_id": "' + _client_id +
					'","client_secret": "' + _client_secret +
					'","audience":"https://humm-box.com/m2m","grant_type":"client_credentials"}';

				options = { method: 'POST',
					url: 'https://humm-server.eu.auth0.com/oauth/token',
					headers: { 'content-type': 'application/json' },
					body: _body
				};
				
				try{
                    request(options, function (error, response, body) {
						if (error) throw new Error(error);

						var tokenObject = JSON.parse(body);
						msg.payload = tokenObject.access_token;
						node.send(msg);
                        node.log("Obtained a new Sigfox bearer token...");
					});

				} catch (err){
                    node.error("Error: " + err.message);
                    node.status({ fill: "red", shape: "ring", text: 'access error, see log' });
				}
			});
			
		} catch (err) {
			node.error("Error: " + err.message);
            node.status({ fill: "red", shape: "ring", text: 'unexpected error, see log' });
        }
    }

    RED.nodes.registerType("sagofonto-sigfox-token", SagofontoSigfoxTokenNode);
}