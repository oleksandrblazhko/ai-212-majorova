## Функція updatePlanPattern(planPattern: PlanPattern) - FR2.5 Користувач налаштовує параметри плану мікроклімату

### Вхідні параметри:
Вхідним параметром є об'єкт класу PlanPattern planPattern. Він містить такі поля:
1. **Поточна температура** (знаходиться в об'єкті класу Microclimate):
- умова 1. Поточна температура має бути заповненою.
- умова 2. Значення поточної температури повинено бути менше 20 символів.
2. **Температурний режим** (знаходиться в об'єкті класу PlanParameters):
- умова 3. Температурний режим має бути заповненим.
- умова 4. Значення температурного режиму повинено бути менше 100 символів.
3. **Тип вентиляції приміщення** (знаходиться в об'єкті класу Microclimate) : 
- умова 5. Тип вентиляції приміщення повиннен бути заповненим.
- умова 6. Тип вентиляції приміщення має бути розміром менше 150 символів.
4. **Рівень освітленості** (знаходиться в об'єкті класу Microclimate): 
- умова 7. Рівень освітленості повинен мати значення більше 0.
5. **Вологість** (знаходиться в об'єкті класу Microclimate -> в об'єкті класу Humidity) : 
- умова 8. Вологість має бути заповнена.
- умова 9. Абсолютна вологість повинна мати значення більше 0.
- умова 10. Відносна вологість повинна мати значення більше 0.
6. **Час виключення світла** (знаходиться в об'єкті класу PlanParameters): 
- умова 11. Час виключення світла має бути заповненим.

### Вихідні параметри:
- значення = *HTTP 200 OK* - Plan pattern has been updated successfully;
- значення = *HTTP 404 ENTITY_NOT_FOUND* - Plan pattern not found;
- значення = *HTTP 400 INVALID_DATA* - Max size of temperature is 20 characters;
- значення = *HTTP 400 INVALID_DATA* - Max size of temperature schedule is 100 characters;
- значення = *HTTP 400 INVALID_DATA* - Max size of ventilation is 100 characters;
- значення = *HTTP 400 INVALID_DATA* - Humidity levels must be greater than or equal to 1;
- значення = *HTTP 400 INVALID_DATA* - Time when lights go off must be not null on updating parameters;
