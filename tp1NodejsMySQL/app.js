const { getConnection, find, insert, update } = require("./operaciones");

const mysql = require("mysql2/promise");
const fetch = require("node-fetch");



(async () => {
    const conex = await getConnection() 
    await conex.beginTransaction()
    try{
    let arrayJson
    let response
    for (let i = 1; i <= 300; i++) {
        response = await fetch(`https://restcountries.com/v2/callingcode/${i}`);
        if (response.status === 200) {
            arrayJson = await response.json();
            arrayJson = arrayJson.map(({name, region, capital = 'No tiene', population, numericCode, latlng : [latitud, longitud] = [0,0]}) => {
                        return {name, region, capital, population, numericCode, latitud, longitud} 
                        });
            for (let j = 0; j < arrayJson.length; j++) {
                const [findRecord] = await find(conex, arrayJson[j]["numericCode"]);
                if ( findRecord.length === 0) {
                    await insert(conex, arrayJson[j])
                } else {
                    await update(conex, arrayJson[j])
                }
            }   
        }
    }
    await conex.commit()
    } catch (err) {
    console.log(err);
    conex.rollback();
    } finally {
        conex.end()
    }
})();


