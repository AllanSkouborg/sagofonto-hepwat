module.exports = function (RED) {
  'use strict';
  var ftp = require('ftp');
  var fs = require('fs');

  function FtpNode(n) {
    RED.nodes.createNode(this, n);
    var node = this;
    var credentials = RED.nodes.getCredentials(n.id);
    this.options = {
      'host': n.host || 'localhost',
      'port': n.port || 21,
      'secure': n.secure || false,
      'secureOptions': n.secureOptions,
      'user': n.user || 'anonymous',
      'password': credentials.password || 'anonymous@',
      'connTimeout': n.connTimeout || 10000,
      'pasvTimeout': n.pasvTimeout || 10000,
      'keepalive': n.keepalive || 10000
    };
  }

  RED.nodes.registerType('sagofonto_ftp', FtpNode, {
    credentials: {
      password: {type: 'password'}
    }
  });

  function FtpInNode(n) {
    RED.nodes.createNode(this, n);
    this.sagofonto_ftp = n.sagofonto_ftp;
    this.operation = n.operation;
    this.filename = n.filename;
    this.localFilename = n.localFilename;
	this.serverPath = n.serverPath;
    this.ftpConfig = RED.nodes.getNode(this.sagofonto_ftp);

    if (this.ftpConfig) {
      var node = this;
      node.on('input', function (msg) {
        var conn = new ftp();
        var filename = node.filename || msg.filename || '';
        var localFilename = node.localFilename || msg.localFilename || '';
		var serverPath = node.serverPath || msg.serverPath || '';
        this.sendMsg = function (err, result) {
          if (err) {
            node.error(err.toString());
            node.status({ fill: 'red', shape: 'ring', text: 'failed' });
          }
          node.status({});
          if (node.operation == 'get') {
            result.once('close', function() { conn.end(); });
            result.pipe(fs.createWriteStream(localFilename));
            msg.payload = 'Get operation successful. ' + localFilename;
          } else if (node.operation == 'put') {
            conn.end();
            msg.payload = 'Put operation successful.';
          } else {
            conn.end();
            msg.payload = result;
          }
          msg.filename = filename;
          msg.localFilename = localFilename;
		  msg.serverPath = serverPath;
          node.send(msg);
        };
        conn.on('ready', function () {
          switch (node.operation) {
            case 'list':
              conn.list(serverPath, node.sendMsg);
              break;
            case 'get':
              conn.get(filename, node.sendMsg);
              break;
            case 'put':
              conn.put(localFilename, filename, node.sendMsg);
              break;
            case 'delete':
              conn.delete(filename, node.sendMsg);
			case 'cwd':
              conn.cwd(serverPath, node.sendMsg);
              break;
          }
        });
        conn.connect(node.ftpConfig.options);
      });
    } else {
      this.error('missing ftp configuration');
    }
  }
  RED.nodes.registerType('sagofonto_ftp in', FtpInNode);
}
