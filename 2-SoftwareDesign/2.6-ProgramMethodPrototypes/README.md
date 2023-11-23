### Прототипи методів програмних класів
| FR | Опис функціональної вимоги | Назва класу | Назва методу класу |
| --- | --- | --- | --- |
| FR1 | Користувач вивчає інформацію про вологість | Theme | findAllThemes() |
| FR1.2 | Програмний продукт відображає список тем, пов'язаних з вологостю | Theme | findAllThemes() |
| FR1.3 | Користувач вибирає тему, яка його цікавить | TopicsInfo | findTopicsByTheme(theme: Set\<Theme\>) |
| FR1.4 | Програмний продукт надає інформацію про тему, включаючи текст, зображення та відео | TopicsInfo | findAllTopicById(id: int) |
| FR2 | Користувач створює план гарного мікроклімату | MicroclimatePlan | createPlan(planPattern:PlanPattern, user: User, initiallyMicroclimate: Microclimate) |
| FR2.2 | Програмний продукт відображає список можливостей для створення плану мікроклімату | PlanPattern | findAllPatterns() |
| FR2.3 | Користувач вибирає шаблон плану мікроклімату, який його цікавить | PlanPattern | createPlanPattern (optimalMicroclimate: Microclimate, device: String, planParameters: PlanParameters) |
| FR2.4 | Програмний продукт відображає список параметрів вибраного шаблону плану мікроклімату | PlanPattern | findPatternById (id: int) |
| FR2.5 | Користувач налаштовує параметри плану мікроклімату | PlanPattern | updatePlanPattern(planPattern: PlanPattern) |
| FR2.6 | Програмний продукт надає готовий план мікроклімату | MicroclimatePlan | findPlanByUser(user: User) |
