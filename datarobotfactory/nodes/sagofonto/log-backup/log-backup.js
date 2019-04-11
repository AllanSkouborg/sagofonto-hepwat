module.exports = function(RED) {

    function SagofontoLogBackupNode(config) {

	    RED.nodes.createNode(this, config);
	
        var node = this;
		var fs = require('fs');
		
		try {
		
			node.sourcePath = config.sourcePath;			// default: /usr/src/node-red/
			node.sourceFilename = config.sourceFilename;	// default: hepwat.log
			node.destPath = config.destPath;				// default: /usr/src/node-red/logs/
			
			node.status({ fill: "green", shape: "ring", text: 'node ready to backup...' });
							
			node.on('input', function(msg) {

				filepath = node.sourcePath + node.sourceFilename;
				try{
                    fs.accessSync(filepath, fs.F_OK);
                    var timeStamp = new Date().getTime();
                    var fileName = node.sourceFilename.split('.');
                    var destFilename = fileName[0] + '_' + timeStamp + '.' + fileName[1];

                    fs.createReadStream(node.sourcePath + node.sourceFilename).pipe(fs.createWriteStream(node.destPath + destFilename));

                    node.log("Backup of " + filepath + " created at " + node.destPath + destFilename);
                    node.send(msg);
				
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

    RED.nodes.registerType("sagofonto-log-backup", SagofontoLogBackupNode);
}