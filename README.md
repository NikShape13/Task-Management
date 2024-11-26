# Task Management

Это приложение для управления задачами, использующее Spring Boot, Spring Security и PostgreSQL, с развертыванием через Docker Compose.

## Содержание

- [Описание](#Описание)
- [Требования](#Требования)
- [Установка](#Установка)
  - [Клонирование репозитория](#Клонирование-репозитория)
  - [Запуск Maven](#Запуск-Maven)
  - [Запуск Docker Compose](#Запуск-Docker-Compose)
- [Запуск проекта](#Запуск-проекта)
- [Дополнительная информация](#Дополнительная-информация)

## Описание

Это проект для управления задачами, с функционалом для регистрации пользователей, создания задач и управления ими.

## Требования

- [Docker](https://www.docker.com/get-started) — для контейнеризации приложения.
- [Maven](https://maven.apache.org/) - для сборки проекта.
- [Git](https://git-scm.com/) — для клонирования репозитория.

## Установка

### Клонирование репозитория

1. Клонируйте репозиторий на вашу локальную машину:

   ```bash
   git clone https://github.com/NikShape13/Task-Management.git
   
2. Перейдите в директорию проекта:
    ```bash
   cd Task-Management
3. Запуск Maven:
   
       mvn clean install
    
4. Запуск Docker Compose
    ```bash
   docker-compose up --build
    
## Запуск проекта 
После того как Docker Compose завершит сборку и запуск, приложение будет доступно по порту 8080.

Перейдите по следующему адресу в браузере для теста API:
    
    http://localhost:8080/swagger-ui/index.html

Для теста API можете создать новых пользователей - получив их jwt вы сможете авторизованно обращаться к разным API.

Можете использовать этих юзеров, которые будут внедренны в систему во время миграции базы данных:

    'user2@mail.ru' | 'users_password' - обычный юзер

    'admin@mail.ru' | 'users_password' - с правами админа

Так же будет создано несколько тестовых задач и комментариев.

## Дополнительная информация
Все начальные данные для базы данных могут быть добавлены в папку ./docker/sql через SQL-скрипты.

Для остановки работы контейнеров используйте команду:

    docker-compose down
    
Для пересборки контейнеров используйте:

bash

    docker-compose up --build
    

