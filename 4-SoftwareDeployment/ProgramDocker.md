# Робота з Docker-образом та Docker-контейнером

## Крок 1: Підготовка Dockerfile

Dockerfile, який використовує базовий образ PostgreSQL та встановлює деякі метадані та змінні оточення, виглядає так:

```dockerfile
FROM postgres
LABEL description="Second container"
LABEL maintainer="Anastasia Mayorova"
LABEL version="1.0"

ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD root
ENV POSTGRES_DB lab12

COPY ./dbscript.sql /docker-entrypoint-initdb.d/
```
#### `FROM`
- Використовується для вказівки базового образу. Наприклад, `FROM postgres` означає, що створюється свій образ, використовуючи як основу образ PostgreSQL.

#### `LABEL`
- Використовується для додавання метаданих до образу, таких як опис, автор образу, версія та інше. Наприклад: `LABEL version="1.0"`.

#### `ENV`
- Встановлює змінні оточення всередині образу. Ці змінні можуть бути використані у інших командах Dockerfile або коли контейнер буде запущений. Наприклад, `ENV POSTGRES_USER postgres` задає змінну оточення `POSTGRES_USER` зі значенням `postgres`.

#### `COPY`
- Копіює файли або папки з локальної системи в образ. Наприклад, `COPY ./dbscript.sql /docker-entrypoint-initdb.d/` копіює файл `dbscript.sql` з локальної системи в папку `/docker-entrypoint-initdb.d/` у контейнері.


## Крок 2: Створення нового Docker-образу з іншою назвою, який бере команди з файлу Dockerfile у поточному каталозі запуску команди

Для Створення образу використовується наступна команду в терміналі:

```bash
docker image build -t maiorova-anastasia-2 ./
```

Де `maiorova-anastasia-2` - назва новго Docker-образу

## Крок 3: Переглянути список створених образів

Для перегляду списку створених Docker-образів виконуємо команду:

```bash
docker image ls
```

## Крок 4: Переглянути інформацію про Docker-образ

Для перегляду інформації про Docker-образ maiorova-anastasia-2 виконується команда:

```bash
docker image inspect maiorova-anastasia-2
```

Ця команда покаже список запущених контейнерів.

## Крок 5: Взаємодія з контейнером

Для взаємодії з вашим PostgreSQL сервером в контейнері використовуйте команду `docker exec`:

```bash
docker exec -it my-postgres-container psql -U postgres
```

---
