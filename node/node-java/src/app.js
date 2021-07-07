"use strict";
process.env.JAVA_HOME = '/usr/sap/TenantConfigurator/bin/jre';
const fs = require("fs");
const java = require("java");
const path = require('path');
const libDir = path.resolve(__dirname, '../java-lib');
const dependencies = fs.readdirSync(libDir);
dependencies.forEach(function(dependency){
    console.log(path.resolve(libDir, `./${dependency}`));
    java.classpath.push(path.resolve(libDir, `./${dependency}`));
});

let encryptStr = `AgAAE3RlbmFudGNvbmZpZ3VyYXRpb24AAAAQfgeli9LIhrcnRXhEr08WKQAAAJBZDVv3QNgc1py7GxhZLy2CxPxiFVG+7Pv2y6l7KxXzjQf5lwGAHcM25VKhLTltAS7fam2d0+Qmkq2vFolcM1/LqI7SnOT2g19SIrbgMR2DymSYyLCHFvhhsWhG2832pC1YsCiuXKQ1O8al/zf2Gb3DytFm/awgpOWLAYDGXFFitIvJVohzOsdOClVKQPDzHk3t2znCs8yG3O7GgIOh4HzOHzpK5z+yjb0=`;
var EncryptionTool = java.import('com.sap.encryption.util.EncryptionTool');
let decryptStr = new EncryptionTool("tenantconfiguration").decryptionSync(encryptStr);
console.log(`value: ${decryptStr}`);

// exports.getJavaInstance = function() {
//     return java;
// }