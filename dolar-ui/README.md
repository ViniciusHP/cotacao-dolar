<h1 align="center">
  Dólar UI
</h1>

<h4 align="center">Status: ✔ Concluído</h4>

---

<p align="center">
 <a href="#user-content-sobre-o-projeto">Sobre o projeto</a> |
 <a href="#user-content-executando-o-projeto">Executando o projeto</a> |
 <a href="#user-content-tecnologias">Tecnologias</a>
</p>

---

## **Sobre o projeto**

Front-end desenvolvido em Angular para consumo da API desenvolvida em Spring Boot para cotação do dólar.

## **Executando o projeto**

### Pré-requisitos

- NodeJS ( versão utilizada: 14.17.0 )
- Npm ( versão utilizada: 7.16.0 )
- Angular CLI ( versão utilizada: 12.0.5 )

### Instruções adicionais

Por padrão, a aplicação vai buscar os dados em nosso back-end no endereço `http://localhost:8080`. Para alterá-lo, modifique a propriedade `apiUrl` do arquivo `src/environments/environment.ts`.

### Instruções de execução do projeto

```bash
# Na pasta raíz do projeto, instale as dependências
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

## **Tecnologias**

Este projeto foi construído com as seguintes ferramentas/tecnologias:

- **[Angular](https://angular.io/)**
- **[Luxon](https://moment.github.io/luxon/#/)**
- **[PrimeFlex](https://primefaces.org/primeng/showcase/#/primeflex)**
- **[PrimeNG](https://www.primefaces.org/primeng/)**
- **[ngx-translate](http://www.ngx-translate.com/)**
