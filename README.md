# microservices-rabbitmq
## Descrição

## Tecnologias Utilizadas

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FFC080?style=for-the-badge&logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)](https://kubernetes.io/)
[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white)](https://swagger.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

## Índice
- [Instalação](#instalação)
- [Uso](#uso)
- [Contribuição](#contribuição)
- [Licença](#licença)
- [Autores](#autores)
- [FAQ](#faq)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Configuração de Ambiente](#configuração-de-ambiente)

## Instalação
Para configurar o ambiente de desenvolvimento, siga os passos abaixo:

1. Clone o repositório:
   ```bash
   git clone https://github.com/thiago-abarros/microservices-rabbitmq.git
   ```

2. Configure as variáveis de Ambiente __**Email Microservice**__

- ``SERVER_PORT_EMAIL:`` O número da porta do microserviço de email.
- ``SPRING_RABBITMQ_ADDRESS:`` 
- ``MAIL_USERNAME:`` O usuário para autenticação no servidor de email.
- ``MAIL_PASSWORD:`` A senha para autenticação no servidor de email.
- ``DB_URL:`` A URL do DB, junto com a senha para a autenticação.

Exemplo de arquivo .env para o Email Microservice:

```bash
SERVER_PORT_EMAIL=
SPRING_RABBITMQ_ADDRESS=
MAIL_USERNAME=
MAIL_PASSWORD=
DB_URL=
```

User Microservice

- ``SERVER_PORT_USER:`` O número da porta do microserviço de usuário.
- ``SPRING_RABBITMQ_ADDRESS:``
- ``DB_URL:`` A URL do DB, junto com a senha para a autenticação.
- ``API_SECURITY_TOKEN_SECRET:`` 

Exemplo de arquivo .env para o User Microservice:

```bash
SERVER_PORT_USER=8081
SPRING_RABBITMQ_ADDRESS=
DB_URL=
API_SECURITY_TOKEN_SECRET=segredo
```
Lembre-se de que essas variáveis de ambiente precisam ser definidas em arquivos .env separados para cada microserviço, e que os valores precisam ser ajustados de acordo com a sua configuração específica.