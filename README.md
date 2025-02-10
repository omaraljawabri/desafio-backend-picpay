# Desafio Backend PicPay

API construída para resolução do desafio backend do picpay proposto no github: `https://github.com/PicPay/picpay-desafio-backend`

## 📋 Requisitos Mínimos
    Docker instalado e funcionando ou
    JDK 21, Maven 3.9.x ou superior e PostgreSQL instalado

## ⚙️ Aplicação - Como rodar
    Faça o clone desse repositório ou baixe a versão zip e descompacte
    Abra o terminal na aplicação
    Caso possua docker instalado, digite o comando: docker-compose up
    Caso possua JDK 21, Maven 3.9.x e PostgreSQL, vá até o caminho src/main/java/com.omar.desafio_backend/DesafioBackendApplication.java e rode a aplicação
    A aplicação está no ar! http://localhost:8080

## 📄  Documentação

A Api foi documentada utilizando Swagger e springdoc-openapi. Caso queira ter acesso a documentação, acesse o link: `http://localhost:8080/swagger-ui/index.html` com a aplicação funcionando.

## 🧪 Testes unitários

A aplicação possui testes unitários das classes do tipo Service e Controller, caso queira rodá-los em sua máquina é necessário possuir Maven instalado na versão 3.9.x ou superior!

### ⚙️ Como rodar
    Abra o terminal no repositório
    Digite o comando: mvn test

## 💻 Tecnologias utilizadas

- Java 21
- Spring Boot e Spring JPA
- Docker
- Swagger
- Jib para imagem da aplicação
- SonarQube
- JaCoCo
- JUnit
- Mockito
- Maven
- PostgreSQL
