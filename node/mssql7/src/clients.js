process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

const util = require('util')
const { MSSQLConnectionPool } = require('./connectionPool');
const config = {
    user: 'sa',
    password: 'SAPB1Admin',
    server: '10.58.8.23',
    // server: '10.58.80.79\\DB2',
    database: 'TenantConfigurator',
    trustServerCertificate: true,
    arrayRowMode: true,
    pool: {
        max: 2,
        min: 1,
        idleTimeoutMillis: 30000
    }
};

// const readFilePromise = util.promisify(() => {});

let foo = {
    name: 'Jerry',
    echo: function(cb){
        cb(null, this.name);
    }
};

let pFoo = util.promisify(foo.echo).bind(foo);
pFoo().then(console.log).catch(e => console.log(`error: ${e}`));

async function closeQuietly(){
    for(i = 0; i < arguments.length; i++){
        try {
            let closeable = arguments[i];
            if(closeable && typeof closeable.close === 'function'){
                await closeable.close();
            }
        } catch (error) {
            // ignore error
        }
    }
}

/**
 * 
 * @param {MSSQLConnectionPool} pool 
 * @returns 
 */
async function query(pool){
    let conn = null;
    try {
        conn = await pool.getConnection();
        await conn.startTransaction();
        let rs = await conn.exec(`select * from [Tokens]`);
        console.log(rs.length);
        return true;
    } catch (error) {
        throw error;
    } finally {
        await closeQuietly(conn);
    }
}

(async () => {
    let pool = null;
    try {
        pool = new MSSQLConnectionPool(config);
        let datas = await Promise.all([
            query(pool), query(pool), query(pool), query(pool), query(pool)
        ]);
        datas.forEach(c => console.log(c));
    } catch (error) {
        console.log(error);
    } finally {
        await closeQuietly(pool);
    }
})();