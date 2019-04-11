module.exports = function(RED) {

    function SagofontoSchemaRegistryNode(config) {

	RED.nodes.createNode(this, config);
	
        var node = this;

		node.port = config.port;
		node.host = config.host;
		node.subject = config.subject;
        
        function createMagicByte(num) {
            arr = new Uint8Array([0,
                (num & 0xff000000) >> 24,
                (num & 0x00ff0000) >> 16,
                (num & 0x0000ff00) >> 8,
                (num & 0x000000ff)
            ]);
            return Buffer.from(arr);
        };

        node.on('input', function(msg) {
			
			if(node && node.schemaRegistry) {
				msg.schemaRegistry = node.schemaRegistry;
				node.send(msg);
			}
			else {
				node.status({fill:"green", shape:"ring", text:"initializing"});
				
				var http = require('http');
				
				var schemaregistryVersion = null;
				var schemaregistrySchema = null;

				var schemaregistryOptions = {
					port: node.port,
					hostname: node.host,
					path: '/subjects/' + node.subject + '/versions/',
					agent: false
				};

				http.get(schemaregistryOptions, function (res) {
					node.log(node.name + ': versions request returned { "status": ' + res.statusCode + ' "path": "' + schemaregistryOptions.path + '" }');
					res.on("data", function (schemaregistryVersionChunk) {
						var arrVersions = JSON.parse(schemaregistryVersionChunk);
						if (arrVersions && arrVersions.length > 0) {
							schemaregistryVersion = arrVersions[arrVersions.length - 1];
							if (schemaregistryVersion) {
								schemaregistryOptions.path += schemaregistryVersion;
								http.get(schemaregistryOptions, function (res) {

									var protocol = res.req.agent.protocol;
									
									res.on("data", function (schemaregistrySchemaChunk) {

										schemaregistrySchema = JSON.parse(String.fromCharCode.apply(null, schemaregistrySchemaChunk));
									
										node.schemaRegistry = {
											id: schemaregistrySchema.id,
											subject: schemaregistrySchema.subject,
											version: schemaregistrySchema.version,
                                            schema: schemaregistrySchema.schema,
                                            magicByte: createMagicByte(parseInt('' + schemaregistrySchema.id, 16))
										};
										
										msg.schemaRegistry = node.schemaRegistry;
										
										node.status({fill:"green", shape:"ring", text:"succes"});
										node.send(msg);
									});
								}).on('error', function (e) {
									node.status({fill:"red", shape:"ring", text:"failed (get schema)"});
									node.error(e);
								});
							}
						}
					});
				}).on('error', function (e) {
					node.status({fill:"red", shape:"ring", text:"failed (get versions)"});
					node.error(e);
				});
			}
		});
    }

    RED.nodes.registerType("sagofonto-schema-registry", SagofontoSchemaRegistryNode);
}