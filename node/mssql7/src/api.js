/**
 * just an inteface for Connection (sadlly we don't have TypeScript in TC)
 */
class Connection{
    constructor(){}

    async exec(sql){
        throw new Error('not implemented');
    }

    /**
     * be called when return connection back to pool
     */
    async clear(){
        return Promise.resolve(true);
    }

    async close(){
        throw new Error('not implemented');
    }

    async preparedStatement(sql, types){
        throw new Error('not implemented');
    }

    async setAutoCommit(flag){
        throw new Error('not implemented');
    }

    async startTransaction(){
        throw new Error('not implemented');
    }

    async commitTransaction(){
        throw new Error('not implemented');
    }

    async rollback(){
        throw new Error('not implemented');
    }
}

class PreparedStatement{
    constructor(){}

    async exec(params){
        throw new Error('not implemented');
    }

    // async execBatch(params){
    //     throw new Error('not implemented');
    // }

    async close(){
        throw new Error('not implemented');
    }
}

module.exports = {
    Connection: Connection,
    PreparedStatement: PreparedStatement
};