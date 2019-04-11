
/* 
 * ~\.node-red\node_modules\node-red-contrib-opcua\opcua\102-opcuaclient.js 
 * Linje 651, tilføj linjen
 *	
 * if(!dataValue || !dataValue.value) return;
 * 
 * gem fil og genstart node-red
 * */

module.exports = function (RED) {
    
    function opcuaNodeCrawlerNode(config) {
        
        RED.nodes.createNode(this, config);
        
        var node = this;
        var opcua = require("node-opcua");
        var crawler = require("node-opcua-client-crawler");
        var DataType = require("node-opcua-variant").DataType;
        var EUInformation = require("node-opcua-data-access")
        var AttributeIds = opcua.AttributeIds;
				
        try {
            
            node.opcuaServerEndpoint = config.opcuaServerEndpoint;
            node.rootTopic = config.rootTopic;
            
            this.on('input', function (msg) {
                
                var client = new opcua.OPCUAClient();
                var session = null;
                
                if (msg.opcuaServerEndpoint) {
                    node.opcuaServerEndpoint = msg.opcuaServerEndpoint;
                } else if (msg.payload && msg.payload.opcuaServerEndpoint) {
                    node.opcuaServerEndpoint = msg.payload.opcuaServerEndpoint;
                } else if (msg.payload && msg.payload.dataSource && msg.payload.dataSource.url) {
                    node.opcuaServerEndpoint = msg.payload.dataSource.url;
                }
                
                if (msg.rootTopic) {
                    node.rootTopic = msg.rootTopic;
                } else if (msg.payload && msg.payload.rootTopic) {
                    node.rootTopic = msg.payload.rootTopic;
                } else if (msg.payload && msg.payload.dataSource && msg.payload.dataSource.rootTopic) {
                    node.rootTopic = msg.payload.dataSource.rootTopic;
                }

                client.connect(node.opcuaServerEndpoint, function (err) {
                    
                    if (err) {
                        node.status({ fill: "red", shape: "ring", text: 'error, cannot connect' });
                        node.error("Error : trying to connect ... ", err);
                    } 
                    else {
                       
                        client.createSession(function (err, ses) {
                            if (err) {
                                node.status({ fill: "red", shape: "ring", text: 'error, cannot create session' });
                                node.error("Error : trying to create session  ... ", err);
                                client.disconnect(function (err) { // disconnect
                                    if (err) {
                                        node.error("Error : trying to disconnect:", err);
                                    }
                                });
                            } 
                            else { // session available
                                
                                node.status({ fill: "green", shape: "ring", text: 'session active' });

                                session = ses;
                                var activeSessionRequests = [];
                                
                                activeSessionRequests.push("NodeCrawler");
                                var nodeCrawler = new crawler.NodeCrawler(session);

                                nodeCrawler.read(node.rootTopic, function (err, nodeTree) {
                                    
                                    function closeSession(requestName) {
                                        
                                        for (var i = activeSessionRequests.length - 1; i >= 0; i--) {
                                            if (activeSessionRequests[i] === requestName) {
                                                activeSessionRequests.splice(i, 1);
                                            }
                                        }
                                        
                                        if (activeSessionRequests.length == 0) {
                                            session.close(function (err) { // close session
                                                if (err) {
                                                    node.error("Error : trying to close session:", err);
                                                } else {
                                                    node.status({ fill: "yellow", shape: "ring", text: 'session closed' });
                                                }

                                                client.disconnect(function (err) { // disconnect
                                                    if (err) {
                                                        node.error("Error : trying to disconnect:", err);
                                                    } else {
                                                        node.status({ fill: "yellow", shape: "ring", text: 'clients disconnected' });
                                                    }
                                                });
                                            });
                                            
                                            if (msg.payload) {
                                                msg.payload.nodeTree = nodeTree;
                                            } else {
                                                msg.payload = { nodeTree: nodeTree };
                                            }
                                            
                                            node.send(msg);
                                        }
                                    }

                                    function findBasicDataType(dataTypeId, nodeIndex, callback) {
                                        
                                        if (!dataTypeId.value) {
                                            var dtArr = dataTypeId.split('=');
                                            var dt = dtArr[dtArr.length - 1];
                                            dataTypeId = {
                                                value: dt
                                            };
                                        }

                                        if (dataTypeId.value <= 25) {
                                            // well-known DataType
                                            var dataType = DataType.get(dataTypeId.value);
                                            callback(null, dataType, nodeIndex);
                                        } else {
                                            callback(null, 'complex', nodeIndex);
                                        }
                                    }

                                    function getUnit(dv) {
                                        var unit;
                                        if (dv) {
                                            dv = dv.split(/\r\n/);
                                            var i = 0;
                                            if (dv.length > 1) {
                                                for (i = 0; i < dv.length; i++) {
                                                    if (dv[i].indexOf("displayName") !== -1) {
                                                        dv = dv[i].split(/\x1B/);
                                                        for (i = 0; i < dv.length; i++) {
                                                            if (dv[i].indexOf("text") !== -1) {
                                                                dv = dv[i].split("=");
                                                                dv = dv[dv.length - 1];
                                                                unit = dv;
                                                                return unit;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                dv = dv[0].split("displayName");
                                                for (i = 0; i < dv.length; i++){
                                                    if (dv[i].indexOf("description") !== -1){
                                                        dv = dv[i].split("description");
                                                        for (i = 0; i < dv.length - 1; i++){
                                                            if (dv[i].indexOf("text") !== -1){
                                                                dv = dv[i].split("=");
                                                                dv = dv[dv.length - 1];
                                                                unit = dv.trim();
                                                                return unit;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        return null;
                                    }

                                    if (err) {
                                        node.error("Error: trying to read with NodeCrawler ... ", err);
                                        node.status({ fill: "red", shape: "ring", text: 'Error traversing node tree' });
                                        closeSession("NodeCrawler");
                                    } else {
                                        
                                        var max_age = 0;
                                        
                                        var nodes_to_read = [];
                                        var nodes_to_queryDataType = [];
                                        
                                        if (nodeTree && nodeTree.organizes) {
                                            
                                            var objectNodeIdx = 0;
                                            for (objectNodeIdx = 0; objectNodeIdx < nodeTree.organizes.length; objectNodeIdx++) {
                                                
                                                var objectNode = nodeTree.organizes[objectNodeIdx];
                                                if (objectNode && objectNode.nodeId) {
                                                    
                                                    objectNode.nodeId = decodeURI(objectNode.nodeId);
                                                    objectNode.browseName = decodeURI(objectNode.browseName);
                                                    
                                                    if (objectNode.dataType) {
                                                        nodes_to_queryDataType.push({
                                                            nodeId: objectNode.nodeId, 
                                                            dataType: objectNode.dataType,
                                                            objectNodeIdx: objectNodeIdx
                                                        });
                                                    }

                                                    nodes_to_read.push({ nodeId: objectNode.nodeId, attributeId: opcua.AttributeIds.Description, attributeName: "description", objectNodeIdx: objectNodeIdx });
                                                    nodes_to_read.push({ nodeId: objectNode.nodeId, attributeId: opcua.AttributeIds.DisplayName, attributeName: "displayName", objectNodeIdx: objectNodeIdx });

                                                    if (objectNode.hasComponent) {
                                                        
                                                        var objectNodeComponentIdx = 0;
                                                        for (objectNodeComponentIdx = 0; objectNodeComponentIdx < objectNode.hasComponent.length; objectNodeComponentIdx++) {
                                                            
                                                            var objectValueNode = objectNode.hasComponent[objectNodeComponentIdx];
                                                            if (objectValueNode && objectValueNode.nodeId) {
                                                                
                                                                objectValueNode.nodeId = decodeURI(objectValueNode.nodeId);
                                                                objectValueNode.browseName = decodeURI(objectValueNode.browseName);
                                                                
                                                                if (objectValueNode.dataType) {
                                                                    nodes_to_queryDataType.push({
                                                                        nodeId: objectValueNode.nodeId, 
                                                                        dataType: objectValueNode.dataType,
                                                                        objectNodeIdx: objectNodeIdx, 
                                                                        objectNodeComponentIdx: objectNodeComponentIdx
                                                                    });
                                                                }

                                                                nodes_to_read.push({ nodeId: objectValueNode.nodeId, attributeId: opcua.AttributeIds.Description, attributeName: "description", objectNodeIdx: objectNodeIdx, objectNodeComponentIdx: objectNodeComponentIdx });
                                                                nodes_to_read.push({ nodeId: objectValueNode.nodeId, attributeId: opcua.AttributeIds.DisplayName, attributeName: "displayName", objectNodeIdx: objectNodeIdx, objectNodeComponentIdx: objectNodeComponentIdx });

                                                                if (objectValueNode.hasProperty) {
                                                                    
                                                                    var objectNodePropertyIdx = 0;
                                                                    for (objectNodePropertyIdx = 0; objectNodePropertyIdx < objectValueNode.hasProperty.length; objectNodePropertyIdx++) {
                                                                        
                                                                        var objectValueNodeValue = objectValueNode.hasProperty[objectNodePropertyIdx];
                                                                        if (objectValueNodeValue && objectValueNodeValue.nodeId) {

                                                                            objectValueNodeValue.nodeId = decodeURI(objectValueNodeValue.nodeId);
                                                                            objectValueNodeValue.browseName = decodeURI(objectValueNodeValue.browseName);

                                                                            if (objectValueNodeValue.browseName == "EngineeringUnits") {
                                                                                objectValueNode.measureUnit = getUnit(objectValueNodeValue.dataValue);
                                                                            }
                                                                            if (objectValueNodeValue.dataType != null) {
                                                                                nodes_to_queryDataType.push({
                                                                                    nodeId: objectValueNodeValue.nodeId, 
                                                                                    dataType: objectValueNodeValue.dataType,
                                                                                    objectNodeIdx: objectNodeIdx, 
                                                                                    objectNodeComponentIdx: objectNodeComponentIdx,
                                                                                    objectNodePropertyIdx: objectNodePropertyIdx
                                                                                });
                                                                            }

                                                                            nodes_to_read.push({ nodeId: objectValueNodeValue.nodeId, attributeId: opcua.AttributeIds.DisplayName, attributeName: "displayName", objectNodeIdx: objectNodeIdx, objectNodeComponentIdx: objectNodeComponentIdx, objectNodePropertyIdx: objectNodePropertyIdx });
                                                                            nodes_to_read.push({ nodeId: objectValueNodeValue.nodeId, attributeId: opcua.AttributeIds.Value, attributeName: "value", objectNodeIdx: objectNodeIdx, objectNodeComponentIdx: objectNodeComponentIdx, objectNodePropertyIdx: objectNodePropertyIdx });
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        
                                        activeSessionRequests.push("ReadNodes");
                                        
                                        session.read(nodes_to_read, max_age, function (err, nodes_to_read, dataValues) {
                                            if (!err) {
                                                var dv = 0;
                                                for (dv = 0; dv < dataValues.length; dv++) {
                                                    if (dataValues[dv]) {
                                                        if (dataValues[dv].value && dataValues[dv].value.value) {
                                                            if (nodes_to_read[dv].objectNodePropertyIdx != null) {
                                                                nodeTree.organizes[nodes_to_read[dv].objectNodeIdx].hasComponent[nodes_to_read[dv].objectNodeComponentIdx].hasProperty[nodes_to_read[dv].objectNodePropertyIdx][nodes_to_read[dv].attributeName] = dataValues[dv].value.value.text;
                                                            }
                                                            else if (nodes_to_read[dv].objectNodeComponentIdx != null) {
                                                                nodeTree.organizes[nodes_to_read[dv].objectNodeIdx].hasComponent[nodes_to_read[dv].objectNodeComponentIdx][nodes_to_read[dv].attributeName] = dataValues[dv].value.value.text;
                                                            }
                                                            else {
                                                                nodeTree.organizes[nodes_to_read[dv].objectNodeIdx][nodes_to_read[dv].attributeName] = dataValues[dv].value.value.text;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                node.error(err);
                                            }
                                            
                                            closeSession("ReadNodes");
                                        });
                                        
                                        var dt = 0;
                                        for (dt = 0; dt < nodes_to_queryDataType.length; dt++) {
                                            activeSessionRequests.push("QueryDataType_" + dt);
                                            findBasicDataType(nodes_to_queryDataType[dt].dataType, dt, function (err, dataType, nodeIndex) {
                                                if (!err) {
                                                    var node = nodes_to_queryDataType[nodeIndex];
                                                    if (node.objectNodePropertyIdx != null) {
                                                        nodeTree.organizes[node.objectNodeIdx].hasComponent[node.objectNodeComponentIdx].hasProperty[node.objectNodePropertyIdx].dataType = dataType;
                                                    }
                                                    else if (node.objectNodeComponentIdx != null) {
                                                        nodeTree.organizes[node.objectNodeIdx].hasComponent[node.objectNodeComponentIdx].dataType = dataType;
                                                    }
                                                    else {
                                                        nodeTree.organizes[node.objectNodeIdx].dataType = dataType;
                                                    }
                                                }
                                                closeSession("QueryDataType_" + nodeIndex);
                                            });
                                        }
                                        closeSession("NodeCrawler");
                                    }
                                });
                            }
                        });
                    }
                });
            });

        } catch (err) {
            node.status({ fill: "red", shape: "ring", text: 'error, see log' });
            node.error("Error:", err);
        }
    }
    
    RED.nodes.registerType("sagofonto-opcua-nodeCrawler", opcuaNodeCrawlerNode);
}