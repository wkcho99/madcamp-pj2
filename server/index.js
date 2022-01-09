require('dotenv').config();

var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);

const mysql   = require('mysql');
const connection = mysql.createConnection({
  host     : 'localhost',
  user     : process.env.DBUSER,
  password : process.env.DBPASS,
  database : process.env.DBNAME
});
const port = 8090;

io.on('connection', (socket) => {
	console.log("user connect");
	
	socket.on("userInfo", obj => {
		console.log(obj.user_id)
		login(obj.user_id, socket);
	})
	
	socket.on('disconnect', () => {
		console.log('user disconnect')
	});
})

server.listen(port, function(){
	console.log(`server open at port ${port}`);
})


app.get("/", (req, res) => {
  res.send("OK");

})


// 유저가 kakao id를 보낼 때
function login(user_id, socket){
	
  	var loginResult = {};
  	var userInfo = {};
  	var pokemonInfo = {};

  	var pokemon_id;
	  
     
     connection.query(`SELECT * from users WHERE kakao_id='${user_id}'`,
     (_, userRow, __) => {
       
        if(userRow.length > 0){
          userInfo["coin"] = userRow[0].coin;
		  userInfo["name"] = userRow[0].name;
          pokemon_id = userRow[0].pokemon_id;
        
          connection.query(`SELECT * from pokemon WHERE id='${pokemon_id}'`, 
          (_, pkmRow, __) => {

			pokemonInfo["id"] = pkmRow[0].id;
            pokemonInfo["level"] = pkmRow[0].level;
            pokemonInfo["number"] = pkmRow[0].number;
            pokemonInfo["exp"] = pkmRow[0].exp;

            var skills = JSON.parse(pkmRow[0].skills)
            var skillInfoes = []

            connection.query(`SELECT * from skills WHERE id IN(${skills.map(x => `'${x.id}'`).join(', ')})`,
            (_, skillRow, __) => {

              skillRow.forEach((elem, idx) => {
                skillInfo = {}
				skillInfo["id"] = elem.id;
                skillInfo["name"] = elem.name;
                skillInfo["cool"] = elem.cool;
                skillInfo["power"] = elem.power;
                skillInfo["level"] = skills[idx].level;
                skillInfoes.push(skillInfo);
              });

              pokemonInfo["skills"] = skillInfoes;
              userInfo["pokemon"] = pokemonInfo;
				
			  socket.emit("userInfo", userInfo);
            });
          });
        }

	});
}