
# microservices-rabbitmq

## Descrição

Este projeto consiste em dois microserviços que se comunicam via RabbitMQ, um broker de mensagens robusto. O primeiro microserviço é responsável pelo gerenciamento de usuários, oferecendo funcionalidades como login, cadastro e recuperação de senha. O segundo microserviço é dedicado ao envio de emails, sendo acionado sempre que um evento relevante ocorre no sistema, como a criação de um novo usuário ou uma solicitação de redefinição de senha.

Ambos os microserviços utilizam o RabbitMQ para se comunicarem de maneira eficiente e assíncrona, permitindo que o sistema seja escalável e resiliente. O microserviço de usuários publica mensagens no RabbitMQ, que são consumidas pelo microserviço de emails para disparar as notificações correspondentes.

## Tecnologias Utilizadas

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FFC080?style=for-the-badge&logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)[![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)](https://kubernetes.io/)[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white)](https://swagger.io/)[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

## Índice
- [Instalação](#instalação)
- [Guia de Execução](#guia-de-execução)
- [Contribuição](#contribuição)
- [Licença](#licença)
- [Autores](#autores)

## Instalação
Para configurar o ambiente de desenvolvimento, siga os passos abaixo:

1. Clone o repositório:
   ```bash
   git clone https://github.com/thiago-abarros/microservices-rabbitmq.git
   ```

2. Configure as variáveis de ambiente para os dois microserviços.

### Email Microservice

Este microserviço é responsável por enviar emails baseados em eventos publicados no RabbitMQ.

Variáveis de ambiente:
- `SERVER_PORT_EMAIL`: Porta em que o serviço de email será executado.
- `SPRING_RABBITMQ_ADDRESS`: Endereço do broker RabbitMQ.
- `MAIL_USERNAME`: Usuário para autenticação no servidor de email.
- `MAIL_PASSWORD`: Senha para autenticação no servidor de email.
- `DB_URL`: URL de conexão ao banco de dados.

### User Microservice

Este microserviço gerencia o cadastro, login e recuperação de senhas dos usuários.

Variáveis de ambiente:
- `SERVER_PORT_USER`: Porta em que o serviço de usuários será executado.
- `SPRING_RABBITMQ_ADDRESS`: Endereço do broker RabbitMQ.
- `DB_URL`: URL de conexão ao banco de dados.
- `API_SECURITY_TOKEN_SECRET`: Segredo para geração de tokens de segurança.

Lembre-se de que as variáveis de ambiente precisam ser configuradas em arquivos `.env` **separados para cada microserviço**, ajustando os valores conforme a configuração específica do seu ambiente.

## Guia de Execução

### Usando Docker Compose

1. Certifique-se de que os arquivos `.env` foram configurados para ambos os microserviços.
2. No diretório raiz do projeto, já existe o arquivo `docker-compose.yaml`. Para iniciar os serviços, basta executar o seguinte comando:

```bash
docker-compose up --build
```

### Usando Skaffold

Se você deseja rodar seus microserviços em Kubernetes, você pode utilizar o Skaffold para automatizar o processo de construção e implantação.

1. Certifique-se de que o **kubectl**, **minikube** (ou outro cluster Kubernetes) e **Skaffold** estão instalados no seu sistema.
2. No diretório raiz do projeto, já existe o arquivo `skaffold.yaml`. Para iniciar os serviços no Kubernetes, basta executar o comando:

```bash
skaffold dev
```

O Skaffold irá observar as alterações no código e aplicá-las automaticamente no cluster Kubernetes. 

### Utilizando o Microserviço

Para interagir com o microserviço de Usuário, você pode utilizar ferramentas como Postman ou Insomnia para realizar as requisições HTTP. Alternativamente, você pode explorar a documentação interativa e testar as APIs diretamente através do **Swagger**, acessível na seguinte URL:

[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

O Swagger oferece uma interface intuitiva onde você pode visualizar **todos os endpoints disponíveis**, seus métodos e parâmetros, além de executar requisições diretamente pela interface web.
