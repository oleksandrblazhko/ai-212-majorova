### Словник атрибутів об'єктів

| Об'єкт               | Атрибут                 | Короткий опис                               | Тип      | Обмеження              |
|----------------------|-------------------------|---------------------------------------------|----------|------------------------|
| Мікроклімат          | Температура             | Значення температури                       | строка   | довжина символів < 20                      |
| Мікроклімат          | Вологість               | Опис вологості                             | Вологість| -                      |
| Мікроклімат          | Вентиляція              | Тип вентиляції                             | строка   | довжина символів < 150                      |
| Мікроклімат          | Рівень освітленості     | Значення освітленості                      | numeric  | значення > 0                      |
| ПланМікроклімату     | Поточне становище       | Опис поточного мікроклімату                | Мікроклімат | -                  |
| ПланМікроклімату     | Список параметрів       | Перелік параметрів плану мікроклімату      | словник  | -                      |
| ШаблонПлануСтворення | Поточне становище       | Опис поточного мікроклімату                | Мікроклімат | -                  |
| ШаблонПлануСтворення | Проект оптимального мікроклімату | Структура оптимального мікроклімату  | json     | -                      |
| Користувач           | Ім'я                    | Повне ім'я користувача                     | строка   | довжина символів < 359                      |
| Користувач           | Прізвище                | Прізвище користувача                       | строка   | довжина символів < 350                      |
| ПараметриПлануСтворення | Температурний режим   | Опис температурного режиму                 | строка   | довжина символів < 100                      |
| ПараметриПлануСтворення | Величина вологості   | Опис вологості                             | Вологість| -                      |
| ПараметриПлануСтворення | Час включення освітлення | Час, коли необхідно включити освітлення | дата     | -                      |
| Вологість            | Відносна вологість      | Значення відносної вологості               | numeric  | значення > 0                     |
| Вологість            | Абсолютна вологість     | Значення абсолютної вологості              | numeric  | значення > 0                      |