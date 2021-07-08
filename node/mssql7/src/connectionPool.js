const mssql = require('mssql')
const { Connection } = require('./api');

class MSSQLConnectionPool{
    constructor(config){
        this.pool = new mssql.ConnectionPool(config);
        this.connected = false;
        this.connecting = false;
        this.connectingPromise = new Promise((resolve, reject) => {
            this.resolve = resolve;
            this.reject = reject;
        });
    }

    async getConnection(){
        if(!this.connected){
            if(!this.connecting){
                this.connecting = true;
                console.log('connecting to database ...');
                await this.pool.connect();
                this.resolve();
                this.connecting = false;
                this.connected = true;
            }else{
                console.log('waiting for connecting ...');
                await this.connectingPromise;
            }
        }
        return new MSSQLConnection(this.pool);
    }

    async close(){
        await this.pool.close();
    }
}

class MSSQLConnection extends Connection{
    constructor(pool){
        super();
        this.pool = pool;
    }

    async exec(sql){
        return new Promise((resolve, reject) => {
            let req = new mssql.Request(this.transaction || this.pool);
            req.query(sql, (err, result) => {
                if(err){
                    reject(err);
                    return;
                }
                resolve(result.recordset || result);
            });
        });
        let rs = await this.pool.request().query(sql);
        return rs.recordset || rs;
    }

    async preparedStatement(sql, types){
        let ps = new mssql.PreparedStatement(this.pool);

    }

    async setAutoCommit(flag){
        this._autoCommit = flag;
        return Promise.resolve(true);
    }

    async startTransaction(){
        if(this.transaction){
            throw new Error('there is already a uncommited transaction');
        }
        this._autoCommit = false;
        this.transaction = new mssql.Transaction(this.pool);
        // await util.promisify(this.transaction.begin).bind(this.transaction)();
        return new Promise((resolve, reject) => {
            this.transaction.begin(err => {
                if(err){
                    reject(err);
                    return;
                }
                resolve(true);
            });
        });
    }

    async commitTransaction(){
        if(!this.transaction){
            throw new Error('no transaction exists');
        }
        return new Promise((resolve, reject) => {
            this.transaction.commit(err => {
                if(err){
                    reject(err);
                    return;
                }
                resolve(true);
            })
        });
    }

    async rollback(){
        if(!this.transaction){
            throw new Error('no transaction exists');
        }
        return new Promise((resolve, reject) => {
            this.transaction.rollback(err => {
                if(err){
                    reject(err);
                    return;
                }
                resolve(true);
            })
        });
    }

    async close(){
        return Promise.resolve(true);
    }

}

exports.MSSQLConnectionPool = MSSQLConnectionPool;
exports.MSSQLConnection = MSSQLConnection;