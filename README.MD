<h1 align="center">
  Desafio Cotação do Dólar
</h1>

<h4 align="center">Status: ✔ Concluído</h4>

---

# **Sumário**

* [Sobre o projeto](#sobre-o-projeto)
* [Clonando o projeto](#clonando-o-projeto)
* [Back-End](#back-end)
  * [Executando o back-end](#executando-o-back-end)
    * [Pré-requisitos back-end](#pré-requisitos-back-end)
    * [Instruções adicionais back-end](#instruções-adicionais-back-end)
    * [Instruções de execução do back-end](#instruções-de-execução-do-back-end)
  * [End-points](#end-points)
    * [Cotação Atual](#cotação-atual)
    * [Listagem no Período](#listagem-no-período)
  * [Tecnologias back-end](#tecnologias-back-end)
* [Front-End](#front-end)
  * [Executando o front-end](#executando-o-front-end)
    * [Pré-requisitos front-end](#pré-requisitos-front-end)
    * [Instruções adicionais front-end](#instruções-adicionais-front-end)
    * [Instruções de execução do front-end](#instruções-de-execução-do-front-end)
  * [Tecnologias front-end](#tecnologias-front-end)

---

# **Sobre o projeto**

Aplicação Full-stack desenvolvida para cotação atual do dólar e listagem das cotações do dólar em um determinado período.

# **Clonando o projeto**

Antes de iniciar é necessário que você já tenha a ferramenta [Git](https://git-scm.com/) instalada.

```bash
# Clone este repositório
$ git clone https://github.com/ViniciusHP/desafio-cotacao-dolar.git

# Acesse a pasta deste projeto por meio do terminal
$ cd desafio-cotacao-dolar
```

# **Back-End**

API desenvolvida em Spring Boot para cotação do dólar. Foi utilizada a API externa [AwesomeAPI](https://docs.awesomeapi.com.br/api-de-moedas) para obtenção dos dados de cotações.

## **Executando o back-end**

### Pré-requisitos back-end

- Java 22
- Maven ( utilizei o Maven Wrapper que vem com o Spring )
- PostgreSQL ( versão utilizada: 16.3 )

No banco de dados PostgreSQL, crie um DATABASE chamado `dolar-api`. No script a seguir, foi criado com o usuário padrão `postgres`:

```sql
create database "dolar-api"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
```

### Instruções adicionais back-end

A tabela para guardar a cotação do dólar será criada pela própria aplicação, através do Flyway. Caso ocorra algum contratempo, o script de criação da tabela está na pasta `dolar-api/src/main/resources/db/migration`. 

O usuário do banco de dados utilizado foi o `postgres` com a senha `root`. Caso seja necessário alterá-los, modifique o arquivo `dolar-api/src/main/resources/application-dev.yml`, alterando o valor dos campos `username` e `password`.

O Tomcat irá iniciar por padrão na porta `8080`. Caso seja necessário alterá-la, altere a propriedade `port` do arquivo `dolar-api/src/main/resources/application.yml`.

A configuração do CORS irá permitir a origem `http://localhost:4200` por padrão. É possível alterá-la através da propriedade `origem-permitida` do arquivo `dolar-api/src/main/resources/application.yml` ou, ao rodar o projeto, modificando a origem permitida através da linha de comando.
Exemplo:

```bash
$ ./mvnw spring-boot:run -Dspring-boot.run.arguments=--origem-permitida=http://localhost:8000
```

### Instruções de execução do back-end

```bash
# Acesse a pasta do back-end por meio do terminal
$ cd dolar-api

# Execute o projeto (utilizando o maven wrapper)
$ ./mvnw spring-boot:run
# ou
# Execute o projeto (com o maven instalado)
$ mvn spring-boot:run
```

## **End-points**

O projeto possui 2 end-points, um para cotação atual e outro para fazer listagem de cotações no período especificado.

### Cotação Atual ###
Executar requisição HTTP GET em: `http://localhost:8080/dolar/atual`

Exemplo de retorno:

Código de Status: 200 OK
```json
{
    "valor": "5.68",
    "data": "21/10/2021 - 14:45:55",
    "diferenca": null
}
```

Se a API não conseguir buscar o valor atual da cotação e não existir nenhum registro no banco de dados, será retornado:

Código de Status: 404 Not Found
```json
{
    "mensagem": "A cotação atual do dólar não foi encontrada."
}
```

### Listagem no Período ###

Executar requisição HTTP GET em: `http://localhost:8080/dolar?data-inicial={data-inicial}&data-final={data-final}&page={page}&size={size}`.

Este end-point suporta paginação.

Parâmetros de requisição:

- `data-inicial`: Data de inicio do período no formato AAAA-MM-DD;
- `data-final`: Data de fim do período no formato AAAA-MM-DD;
- `size`: Número de dados que virão nesta paginação (se não for informado será 20);
- `page`: Índice da página (se não for informado será 0). 

Exemplo de retorno para requisição HTTP GET em: `http://localhost:8080/dolar?data-inicial=2021-10-01&data-final=2021-10-20&page=0&size=10`:

Código de Status: 200 OK
```json
{
  "content": [
    {
      "valor": "5.52",
      "data": "20/10/2021 - 15:38:35",
      "diferenca": "-0.16"
    },
    {
      "valor": "5.56",
      "data": "20/10/2021 - 14:32:18",
      "diferenca": "-0.12"
    },
    {
      "valor": "5.55",
      "data": "20/10/2021 - 13:33:10",
      "diferenca": "-0.13"
    },
    {
      "valor": "5.55",
      "data": "20/10/2021 - 11:39:34",
      "diferenca": "-0.13"
    },
    {
      "valor": "5.58",
      "data": "20/10/2021 - 09:31:30",
      "diferenca": "-0.10"
    },
    {
      "valor": "5.58",
      "data": "20/10/2021 - 09:01:03",
      "diferenca": "-0.10"
    },
    {
      "valor": "5.58",
      "data": "19/10/2021 - 17:55:48",
      "diferenca": "-0.10"
    },
    {
      "valor": "5.59",
      "data": "19/10/2021 - 16:19:22",
      "diferenca": "-0.09"
    },
    {
      "valor": "5.61",
      "data": "19/10/2021 - 16:14:22",
      "diferenca": "-0.07"
    },
    {
      "valor": "5.61",
      "data": "19/10/2021 - 16:12:02",
      "diferenca": "-0.07"
    }
  ],
  "pageable": {
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "unpaged": false,
    "paged": true
  },
  "last": false,
  "totalPages": 14,
  "totalElements": 131,
  "number": 0,
  "size": 10,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 10,
  "empty": false
}
```

Se alguma data não for informada a resposta será:

Código de Status: 400 Bad Request
```json
{
    "mensagem": "O parâmetro de requisição 'data-inicial' é obrigatório."
}
```

Código de Status: 400 Bad Request
```json
{
    "mensagem": "O parâmetro de requisição 'data-final' é obrigatório."
}
```

## **Tecnologias back-end**

Este projeto foi construído com as seguintes ferramentas/tecnologias:

- **[Spring Boot](https://spring.io/projects/spring-boot)**
- **[Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign)**
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**
- **[Flyway](https://flywaydb.org/)**
- **[Lombok](https://projectlombok.org/)**
- **[MapStruct](https://mapstruct.org/)**


# **Front-end**

Front-end desenvolvido em Angular para consumo da API desenvolvida em Spring Boot para cotação do dólar.

## **Executando o front-end**

### Pré-requisitos front-end

- NodeJS ( versão utilizada: 18.20.3 )
- Npm ( versão utilizada: 10.7.0 )
- Angular CLI ( versão utilizada: 17.3.8 )

### Instruções adicionais front-end

Por padrão, a aplicação vai buscar os dados em nosso back-end no endereço `http://localhost:8080`. Para alterá-lo, modifique a propriedade `apiUrl` do arquivo `dolar-ui/src/environments/environment.ts`.

### Instruções de execução do front-end

```bash
# Acesse a pasta do front-end por meio do terminal
$ cd dolar-ui

# Instale as dependências deste projeto
$ npm install

# Execute o projeto em modo de desenvolvimento
$ npm start
# ou
$ ng serve

# O servidor de desenvolvimento será iniciado na porta 4200
# Para acessar o projeto, navegue para http://localhost:4200

# Para alterar a porta do servidor de desenvolvimento utilize a opção --port seguida do número da porta
$ ng serve --port 8000
```

## **Tecnologias front-end**

Este projeto foi construído com as seguintes ferramentas/tecnologias:

- **[Angular](https://angular.io/)**
- **[Luxon](https://moment.github.io/luxon/#/)**
- **[PrimeFlex](https://primefaces.org/primeng/showcase/#/primeflex)**
- **[PrimeNG](https://www.primefaces.org/primeng/)**
- **[ngx-translate](http://www.ngx-translate.com/)**
