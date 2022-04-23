const mysql = require("mysql2/promise")

const getConnection = async () => {
    const connection = await mysql.createConnection({
        host: "localhost",
        user: "root",
        password: "mysqlutn",
        database: "paises",
    });
    return connection;
}


const find = async (connection, id) => {    
    let insertQuery = `SELECT * FROM paises.Pais WHERE codigoPais = ${ id };`;
    const pais = await connection.execute(insertQuery);
    return pais;
}

const insert = async (connection, pais) => {    
    let insertQuery = "INSERT INTO paises.Pais (codigoPais, nombrePais, capitalPais, region, poblacion, latitud, longitud) VALUES (?, ?, ?, ?, ?, ?, ?);";
    let query = mysql.format(insertQuery, [pais.numericCode, pais.name, pais.capital, pais.region, pais.population, pais.latitud, pais.longitud]);

    const local = await connection.execute(query);
    return local;
}

const update = async (connection, pais) => {    
    let insertQuery = `UPDATE paises.Pais SET nombrePais  = ?, 
                                              capitalPais = ?, 
                                              region      = ?,
                                              poblacion   = ?, 
                                              latitud     = ?, 
                                              longitud    = ? 
                                              WHERE codigoPais = ?;`;
    let query = mysql.format(insertQuery, [pais.name, pais.capital, pais.region, pais.population, pais.latitud, pais.longitud, pais.numericCode]);

    const update = await connection.execute(query);
    return update;
}

module.exports = { getConnection, find, insert, update }