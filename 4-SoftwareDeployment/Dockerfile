FROM postgres
LABEL description="Second container"
LABEL maintainer="Anastasia Mayorova"
LABEL version="1.0"

ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD root
ENV POSTGRES_DB lab12

COPY ./dbscript.sql /docker-entrypoint-initdb.d/
