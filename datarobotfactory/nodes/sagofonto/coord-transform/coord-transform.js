module.exports = function(RED) {

    function SagofontoCoordTransformNode(config) {

	    RED.nodes.createNode(this, config);
	
        var node = this;
		var gdal = require('gdal');
		var util = require('util');
		
		try {
		
			node.sourceX = config.sourceX;		// default: 9.534475
			node.sourceY = config.sourceY;		// default: 55.529096
			node.sourceZ = config.sourceZ;		// default: 0.000000
			
			node.status({ fill: "green", shape: "ring", text: 'node ready to transform...' });
							
			node.on('input', function(msg) {
                var _sourceX = node.sourceX || msg.sourceX;
                var _sourceY = node.sourceY || msg.sourceY;
                var _sourceZ = node.sourceZ || msg.sourceZ;

				try{				
					// coordinate transformation
					var source = gdal.SpatialReference.fromEPSG (4326); 
					var target = gdal.SpatialReference.fromEPSG(25832);

					transform = new gdal.CoordinateTransformation ( source,  target );
					
					pt = transform.transformPoint(_sourceX, _sourceY, _sourceZ);
					
					msg.targetX = pt.x;
					msg.targetY = pt.y;
					msg.targetZ = pt.z;
					msg.payload = {x: pt.x, y: pt.y, z: pt.z};
                    node.send(msg);
				
				} catch (err){
                    node.error("Error: " + err.message);
                    node.status({ fill: "red", shape: "ring", text: 'error, see log' });
				}
			});
			
		} catch (err) {
			node.error("Error: " + err.message);
            node.status({ fill: "red", shape: "ring", text: 'unexpected error, see log' });
        }
    }

    RED.nodes.registerType("sagofonto-coord-transform", SagofontoCoordTransformNode);
}