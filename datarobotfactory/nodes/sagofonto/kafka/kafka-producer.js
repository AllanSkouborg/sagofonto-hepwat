module.exports = function(RED) {

    function kafkaProducerNode(config) {
        
        RED.nodes.createNode(this, config);
        
        var node = this;
        var kafka = require('kafka-node');
        var retry = require('retry');

        var kafka_producer = null;
        node.producer_ready = false;
        node.producer_failed = false;
        
        function getKafkaProducer(kafkaHost, connectTimeout, requestTimeout, requireAcks, ackTimeoutMs, partitionerType) {
            
            if (kafka_producer) return kafka_producer;

            var operation = retry.operation({
                retries: 5,
                factor: 3,
                minTimeout: 1 * 1000,
                maxTimeout: 60 * 1000,
                randomize: true,
            });
           
            var kafkaOptions = { kafkaHost: kafkaHost, autoConnect: true, connectTimeout: connectTimeout, requestTimeout : requestTimeout, idleConnection: 10000, connectRetryOptions: operation};
   
            const client = new kafka.KafkaClient(kafkaOptions);
            const producerOptions = { requireAcks: requireAcks, ackTimeoutMs: ackTimeoutMs, partitionerType: partitionerType };
            
            kafka_producer = new kafka.Producer(client, producerOptions);
                        
            kafka_producer.once('ready', function () {
                node.producer_ready = true;
                node.producer_failed = false;
                node.status({ fill: "green", shape: "ring", text: "ready" });
            });
            
            kafka_producer.once('error', function (err) {
                node.producer_ready = false;
                node.producer_failed = true;
                node.status({ fill: "red", shape: "ring", text: 'error, see console' });
                node.error("Error:", err);
            });
            
            client.once('connect', function () {
                node.status({ fill: "green", shape: "ring", text: "connected" });
            });
            
            client.once('error', function (err) {
                node.producer_ready = false;
                node.producer_failed = true;
                node.status({ fill: "red", shape: "ring", text: 'error, see console' });
                node.error("Kafka Client Error " + err);
            });

            return kafka_producer;
        }

        try {
            
            node.kafkaHost = config.kafkaHost;
            node.connectTimeout = config.connectTimeout; 
            node.requestTimeout = config.requestTimeout;
            node.requireAcks = config.requireAcks;
            node.ackTimeoutMs = config.ackTimeoutMs;
            node.partitionerType = config.partitionerType;
            node.topicName = config.topicName;
            node.compressionOption = config.compressionOption;
            node.producer = getKafkaProducer(node.kafkaHost, node.connectTimeout, node.requestTimeout, node.requireAcks, node.ackTimeoutMs, node.partitionerType);

			this.on('input', function(msg) {
                
                if (node.producer_ready && !node.producer_failed) {
                    var data = msg.payload;
                    const payloads = [{
                        topic: node.topicName,
                        messages: JSON.stringify(data, function (key, value) {
                            if (typeof value === "function") {
                                return undefined;
                            }
                            return value;
                        }),
                        key: msg.key,
                        attributes: node.compressionOption,
                        timestamp: Date.now()
                    }];

                    node.producer.send(payloads, function (err, data) {
                        if (err) {
							node.log("Kafka transaction failed, will try again in 30 seconds...");
							setTimeout(function () {
								node.producer.send(payloads, function (err, data) {
									if (err) {
										node.status({fill: "red", shape: "ring", text: err.message});
										node.error("Kafka producer Error " + err);
									}
									else {
										node.status({fill: "green", shape: "ring", text: "producing"});
										node.log(JSON.stringify(msg, function (key, value) {
											if (typeof value === "function") {
												return undefined;
											}
											return value;
										}));
										node.send(msg);
									}
								});
							}, 30000);
                        }
                        else {
                            node.status({fill: "green", shape: "ring", text: "producing"});
                            node.log(JSON.stringify(msg, function (key, value) {
                                if (typeof value === "function") {
                                    return undefined;
                                }
                                return value;
                            }));
                            node.send(msg);
                        }
                    });
                }
                else {
                    node.status({ fill: "red", shape: "ring", text: "not ready" });
                }
            });

		} catch (err) {
            node.status({ fill: "red", shape: "ring", text: 'error, see console' });
			node.error("Catched Error:", err);
		}
	}
	RED.nodes.registerType("sagofonto-kafka-producer", kafkaProducerNode);
}