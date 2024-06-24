const express = require('express');
const app = express();
const PORTA = process.env.PORT || 4200;

app.use(express.static(__dirname + '/dist/dolar-ui'));

app.get('/*', function (req, res) {
    res.sendFile(__dirname + '/dist/dolar-ui/index.html'); // Qualquer requisição deve enviar o index.html, pois é o angular que fará as rotas
});

app.listen(PORTA);
console.log(`Servidor ouvindo na porta ${PORTA}...`);
