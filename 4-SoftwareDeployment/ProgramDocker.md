# Робота з Docker-образом та Docker-контейнером для запуску програми

## Крок 1: Завантажити Docker-образ для JDK з назвою openjdk

```bash
docker pull openjdk
```

## Крок 2: Запустити Docker-образ openjdk для компіляції Java-програми

Для Запуску образу для компіляції використовується наступна команду в терміналі:

```bash
docker run --name java-program -w //app -v /${PWD}://app --rm openjdk javac -cp "./postgresql-42.6.0.jar:./" ./Main.java
```

Де `./postgresql-42.6.0.jar:./` - назва драйверу, а `Main.java` - основний файл програми

## Крок 3: Запустити Docker-образ openjdk для виконання Java-класу

Для Запуску образу для виконання програми використовується наступна команду в терміналі:

```bash
docker run --name java-program -w //app -v /${PWD}://app --rm openjdk java -cp "./postgresql-42.6.0.jar:." Main
```

## Результат виконання:
![ProgramDockerExec](https://github.com/oleksandrblazhko/ai-212-majorova/assets/90724127/f766ec12-25e5-492c-9280-27e9199d796b)

---
