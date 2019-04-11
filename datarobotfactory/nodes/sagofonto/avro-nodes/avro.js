/*
Licensed to Cloudera, Inc. under one
or more contributor license agreements. See the NOTICE file
distributed with this work for additional information
regarding copyright ownership. Cloudera, Inc. licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
module.exports = function(RED) {
	var avro = require('avro-js');

	/*
	 * Avro Converter Node
	 *
	 * Converts JSON to Avro and Avro to JSON.
	 */
	function avroNode(config) {
		RED.nodes.createNode(this, config);

		var node = this;

		function getPropByString(obj, propString) {
		    if (!propString)
		        return obj;

		    var prop, props = propString.split('.');

		    for (var i = 0, iLen = props.length - 1; i < iLen; i++) {
		        prop = props[i];

		        var candidate = obj[prop];
		        if (candidate !== undefined) {
		            obj = candidate;
		        } else {
		            break;
		        }
		    }

		    return obj[props[i]];
		}

		function setPropByString(obj, propString, val) {
		    if (!propString)
		        return obj;

		    var prop, props = propString.split('.');

		    for (var i = 0, iLen = props.length - 1; i < iLen; i++) {
		        prop = props[i];

		        var candidate = obj[prop];
		        if (candidate !== undefined) {
		            obj = candidate;
		        } else {
		            break;
		        }
		    }
		    obj[props[i]] = val;
		}

		var schema = null;
		var type = null;

		if (config.schemaSelector == 'literal') {
			schema = config.schemaLiteral;
			type = avro.parse(schema);
		} else if (config.schemaSelector == 'file') {
			schema = config.schemaFile;
			type = avro.parse(schema);
		} else if (config.schemaSelector == 'property') {
			schema = 'fromInputProperty';
		} else {
			node.error("Missing schema selector. Must be \"literal\", \"file\" or \"property\".");
		}

		if (!schema) {
			node.error("No Avro schema provided.");
		}
        
        node.useMagicByte = (config.useMagicByte && config.useMagicByte == true);

		try {
			this.on('input', function(msg) {
				
				if(type == null) {
					if(schema == 'fromInputProperty') {
						schema = msg.schemaRegistry.schema;
						type = avro.parse(schema);
					}
				}
				
				var val = getPropByString(msg, config.property);
				if (typeof(val) == 'undefined') {
					node.error("Property \"" + config.property + "\" is undefined.");
				}
				if (typeof(val) == 'string') {
					val = JSON.parse(msg[config.property]);
				}

				if (typeof(val) == 'object') {
                    if (Buffer.isBuffer(val)) {
                        if (node.useMagicByte) {
                            const _length = msg.schemaRegistry.magicByte.length;
                            val = val.slice(_length);
                        }
						val = type.fromBuffer(val);
						setPropByString(msg, config.property, val);
						node.send(msg);
					} else if (type.isValid(val)) {
                        val = type.toBuffer(val);
                        if (node.useMagicByte) {
                            const _length = val.length + msg.schemaRegistry.magicByte.length;
                            val = Buffer.concat([msg.schemaRegistry.magicByte, val], _length);
                        }
						setPropByString(msg, config.property, val);
						node.send(msg);
					} else {
						node.error(config.property + " object is not valid for the provided Avro schema: " + JSON.stringify(val));
					}
				} else {
					node.error(config.property + " is not an object, type is: " + typeof(val));
				}
			});
		} catch (e) {
			if (e instanceof SyntaxError) {
				node.error(config.property + " is neither a JavaScript Object nor a valid JSON string: " + e);
			} else {
				node.error(e);
			}
		}
	}
	
	RED.nodes.registerType("sagofonto-avro", avroNode);
}