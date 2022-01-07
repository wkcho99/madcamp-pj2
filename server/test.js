const express = require('express');
const mysql   = require('mysql');
require('dotenv').config();

const app = express();
const connection = mysql.createConnection({
  host     : 'localhost',
  user     : process.env.DBUSER,
  password : process.env.DBPASS,
  database : process.env.DBNAME
});

app.get("/", (req, res) => {
  res.send("OK");
})
 
 app.post('/post', (req, res) => {

  console.log("post");

   var inputData;
   var loginResult = {};
   var userInfo = {};
   var pokemonInfo = {};

   var pokemon_id;
  
   req.on('data', (data) => { inputData = JSON.parse(data);}); 
   req.on('end', () => {
     
     connection.query(`SELECT coin, pokemon_id from users WHERE kakao_id='${inputData.user_id}'`,
     (_, userRow, __) => {
       
        
        if(userRow.length > 0){
          userInfo["coin"] = userRow[0].coin;
          pokemon_id = userRow[0].pokemon_id;
        
          connection.query(`SELECT * from pokemon WHERE id='${pokemon_id}'`, 
          (_, pkmRow, __) => {

            pokemonInfo["level"] = pkmRow[0].level;
            pokemonInfo["number"] = pkmRow[0].number;
            pokemonInfo["exp"] = pkmRow[0].exp;

            var skills = JSON.parse(pkmRow[0].skills)
            var skillInfoes = []

            connection.query(`SELECT * from skills WHERE id IN(${skills.map(x => `'${x.id}'`).join(', ')})`,
            (_, skillRow, __) => {

              skillRow.forEach((elem, idx) => {
                skillInfo = {}
                skillInfo["name"] = elem.name;
                skillInfo["cool"] = elem.cool;
                skillInfo["power"] = elem.power;
                skillInfo["level"] = skills[idx].level;
                skillInfoes.push(skillInfo);
              });

              pokemonInfo["skills"] = skillInfoes;
              userInfo["pokemon"] = pokemonInfo;

              res.writeHead(200, {'Content-Type': 'application/json; charset=utf-8'});
              res.write(JSON.stringify(userInfo));
              console.log(JSON.stringify(userInfo));
              res.end();
            });
          });
        }

     });
     

   });



 });
 
app.listen(80, () => {
  console.log('Example app listening on port 80!');
});
 