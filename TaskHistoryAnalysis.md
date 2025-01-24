# Task History Analysis

This document contains a simple analysis of task solving, performed using SQL queries along with their results. While the application is running, these queries can be rerun from http://localhost:8080/h2-console/login.do (login credentials are provided in the application.properties file).

## Observations

### Data observations

This data was gathered with approximately 1000 games with TrainingModeService and includes right now 10971 rows of task results.

- The success rate and task probability are strongly correlated (Query 1).
- The success rate does not have a strong correlation with reward and people's reputation (Query 2, Query 4).
- The success rate appears to be somewhat connected to dragon level and reputation state, but the correlation is not as strong as with probability (Query 3, Query 5).
- A higher dragon level is associated with a higher average reward (Query 6).
- For the highest probability rate ("Sure thing"), all failing task messages follow the same template and offer a higher reward (Query 7, Query 8).
- As the dragon level increases, the distribution of task probabilities shifts towards those with lower success rates. However, the higher score seems to be the main factor influencing this trend, as a higher dragon level is only attained through a higher score, which was not included in the analysis (Query 9).

### Training and manual observations

- Some tasks were encrypted, and ad_ids failed when attempting to solve these tasks. These were filtered out.
- Investigations also use one turn and move tasks, meaning some may expire and new ones can appear. Since investigations don’t incur any cost, it is possible to perform as many investigations as needed until suitable tasks appear. However, as the task distribution shifts toward those with higher failure rates, and since training data for dragon level 9 (and higher) didn’t include any "Sure thing" probabilities (the probability with the highest success rate), this may affect the results.
- As the level increases, it seems more efficient to purchase lower-priced items, as the cost per dragon level is 100, while higher-cost items are priced at 150.
## Items and their impact

<details>
  <summary>Items</summary>
    Impacts were tested manually using swagger

| ID       | NAME                           | COST | IMPACT    |
|----------|--------------------------------|------|-----------|
| hpot     | Healing potion                 | 50   | + 1 live  |
| cs       | Claw Sharpening                | 100  | + 1 level |
| gas      | Gasoline                       | 100  | + 1 level |
| wax      | Copper Plating                 | 100  | + 1 level |
| tricks   | Book of Tricks                 | 100  | + 1 level |
| wingpot  | Potion of Stronger Wings       | 100  | + 1 level |
| ch       | Claw Honing                    | 300  | + 2 level |
| rf       | Rocket Fuel                    | 300  | + 2 level |
| iron     | Iron Plating                   | 300  | + 2 level |
| mtrix    | Book of Megatricks             | 300  | + 2 level |
| wingpotmax| Potion of Awesome Wings       | 300  | + 2 level |

</details>

## Queries and results

### Query 1: Success Rate by Probability

<details>
  <summary>Query</summary>

```sql
SELECT 
  t.PROBABILITY, 
  AVG(
    CASE WHEN t.SUCCESS THEN 1.0 ELSE 0.0 END
  ) AS SUCCESS_RATE, 
  COUNT(*) AS TASK_COUNT
FROM 
  TASK_HISTORY t 
GROUP BY 
  t.PROBABILITY 
ORDER BY 
  SUCCESS_RATE DESC;
```

</details>

<details>
  <summary>Results</summary>

| PROBABILITY           | SUCCESS_RATE   | TASK_COUNT |
|-----------------------|----------------|------------|
| Sure thing            | 0.99584631360  | 963        |
| Piece of cake         | 0.95145299145  | 2925       |
| Walk in the park      | 0.86138079827  | 1854       |
| Quite likely          | 0.75582215949  | 1417       |
| Hmmm....              | 0.64679313460  | 1107       |
| Gamble                | 0.56118143460  | 948        |
| Risky                 | 0.41002949853  | 678        |
| Rather detrimental    | 0.37524950100  | 501        |
| Playing with fire     | 0.27812500000  | 320        |
| Suicide mission       | 0.15891472868  | 258        |


</details>

### Query 2: Success Rate by Reward Range

<details>
  <summary>Query</summary>

```sql
SELECT 
  CASE WHEN t.REWARD BETWEEN 1 
  AND 25 THEN '1-25' WHEN t.REWARD BETWEEN 26 
  AND 50 THEN '26-50' WHEN t.REWARD BETWEEN 51 
  AND 75 THEN '51-75' WHEN t.REWARD BETWEEN 76 
  AND 100 THEN '76-100' WHEN t.REWARD BETWEEN 101 
  AND 150 THEN '101-150' WHEN t.REWARD BETWEEN 151 
  AND 250 THEN '151-250' ELSE '250+' END AS REWARD_RANGE, 
  AVG(
    CASE WHEN t.SUCCESS THEN 1.0 ELSE 0.0 END
  ) AS SUCCESS_RATE, 
  COUNT(*) AS TASK_COUNT 
FROM 
  TASK_HISTORY t 
GROUP BY 
  CASE WHEN t.REWARD BETWEEN 1 
  AND 25 THEN '1-25' WHEN t.REWARD BETWEEN 26 
  AND 50 THEN '26-50' WHEN t.REWARD BETWEEN 51 
  AND 75 THEN '51-75' WHEN t.REWARD BETWEEN 76 
  AND 100 THEN '76-100' WHEN t.REWARD BETWEEN 101 
  AND 150 THEN '101-150' WHEN t.REWARD BETWEEN 151 
  AND 250 THEN '151-250' ELSE '250+' END 
ORDER BY 
  SUCCESS_RATE DESC;
```

</details>

<details>
  <summary>Results</summary>

| REWARD_RANGE | SUCCESS_RATE   | TASK_COUNT |
|--------------|----------------|------------|
| 1-25         | 0.81112176414  | 3129       |
| 51-75        | 0.79460580913  | 1928       |
| 101-150      | 0.75014100395  | 1773       |
| 76-100       | 0.72665534805  | 589        |
| 26-50        | 0.68513553768  | 3357       |
| 151-250      | 0.64948453608  | 194        |
| 250+         | 0.00000000000  | 1          |

</details>

### Query 3: Success Rate by Dragon Level

<details>
  <summary>Query</summary>

```sql
SELECT 
  t.dragon_level, 
  AVG(
    CASE WHEN t.SUCCESS THEN 1.0 ELSE 0.0 END
  ) AS SUCCESS_RATE, 
  COUNT(*) AS TASK_COUNT 
FROM 
  TASK_HISTORY t 
GROUP BY 
  t.dragon_level 
ORDER BY 
  SUCCESS_RATE DESC;
```

</details>

<details>
  <summary>Results</summary>

| DRAGON_LEVEL | SUCCESS_RATE   | TASK_COUNT |
|--------------|----------------|------------|
| 17           | 1.00000000000  | 2          |
| 18           | 1.00000000000  | 1          |
| 4            | 0.81345565749  | 327        |
| 3            | 0.80714285714  | 420        |
| 2            | 0.77958236659  | 862        |
| 0            | 0.76028261418  | 7926       |
| 1            | 0.76000000000  | 375        |
| 5            | 0.75109170306  | 229        |
| 6            | 0.67634854772  | 241        |
| 7            | 0.66666666667  | 174        |
| 9            | 0.61702127660  | 94         |
| 12           | 0.54545454545  | 22         |
| 10           | 0.53968253968  | 63         |
| 8            | 0.52054794521  | 146        |
| 14           | 0.50000000000  | 6          |
| 15           | 0.50000000000  | 2          |
| 11           | 0.38461538462  | 52         |
| 20           | 0.33333333333  | 3          |
| 13           | 0.28571428571  | 21         |
| 16           | 0.20000000000  | 5          |


</details>

### Query 4: Success Rate by Reputation People Range

<details>
  <summary>Query</summary>

```sql
SELECT 
  CASE WHEN t.reputation_people BETWEEN 0 
  AND 1 THEN '0-1' WHEN t.reputation_people BETWEEN 1 
  AND 2 THEN '1-2' WHEN t.reputation_people BETWEEN 2 
  AND 3 THEN '2-3' WHEN t.reputation_people BETWEEN 3 
  AND 5 THEN '3-5' WHEN t.reputation_people BETWEEN 5 
  AND 7 THEN '5-7' ELSE '7+' END AS REPUTATION_PEOPLE_RANGE, 
  AVG(
    CASE WHEN t.SUCCESS THEN 1.0 ELSE 0.0 END
  ) AS SUCCESS_RATE, 
  COUNT(*) 
FROM 
  TASK_HISTORY t 
GROUP BY 
  CASE WHEN t.reputation_people BETWEEN 0 
  AND 1 THEN '0-1' WHEN t.reputation_people BETWEEN 1 
  AND 2 THEN '1-2' WHEN t.reputation_people BETWEEN 2 
  AND 3 THEN '2-3' WHEN t.reputation_people BETWEEN 3 
  AND 5 THEN '3-5' WHEN t.reputation_people BETWEEN 5 
  AND 7 THEN '5-7' ELSE '7+' END 
ORDER BY 
  SUCCESS_RATE DESC;
```

</details>

<details>
  <summary>Results</summary>

| REPUTATION_PEOPLE_RANGE | SUCCESS_RATE   | COUNT(*) |
|-------------------------|----------------|----------|
| 0-1                     | 0.77911244578  | 2997     |
| 1-2                     | 0.76817640048  | 1678     |
| 3-5                     | 0.73931091739  | 2409     |
| 2-3                     | 0.73873873874  | 1443     |
| 5-7                     | 0.73415765070  | 1941     |
| 7+                      | 0.71172962227  | 503      |

</details>

### Query 5: Success Rate by Reputation State

<details>
  <summary>Query</summary>

```sql
SELECT 
  t.reputation_state, 
  AVG(
    CASE WHEN t.SUCCESS THEN 1.0 ELSE 0.0 END
  ) AS SUCCESS_RATE, 
  COUNT(*) AS TASK_COUNT 
FROM 
  TASK_HISTORY t 
GROUP BY 
  t.reputation_state 
ORDER BY 
  SUCCESS_RATE DESC;
```

</details>

<details>
  <summary>Results</summary>

| REPUTATION_STATE | SUCCESS_RATE       | TASK_COUNT |
|-------------------|--------------------|------------|
| 0.0              | 0.76800000000     | 5375       |
| -2.0             | 0.73958333333     | 3072       |
| -4.0             | 0.73955773956     | 1628       |
| -6.0             | 0.73312401884     | 637        |
| -8.0             | 0.71739130435     | 230        |
| -10.0            | 0.68421052632     | 19         |
| -12.0            | 0.50000000000     | 10         |

</details>

### Query 6: Dragon Level and Average Reward
<details>
<summary>SQL Query</summary>

```sql
SELECT 
  t.dragon_level, 
  AVG(t.reward) AS AVERAGE_REWARD, 
  COUNT(*) as TASK_COUNT 
FROM 
  TASK_HISTORY t 
GROUP BY 
  t.dragon_level 
ORDER BY 
  dragon_level DESC;
```
</details>

<details>
<summary>Results</summary>

| DRAGON_LEVEL | AVERAGE_REWARD         | TASK_COUNT |
|--------------|------------------------|------------|
| 20           | 182.33333333333334    | 3          |
| 18           | 159.0                 | 1          |
| 17           | 150.0                 | 2          |
| 16           | 156.6                 | 5          |
| 15           | 135.0                 | 2          |
| 14           | 143.83333333333334    | 6          |
| 13           | 137.8095238095238     | 21         |
| 12           | 128.77272727272728    | 22         |
| 11           | 131.03846153846155    | 52         |
| 10           | 129.5873015873016     | 63         |
| 9            | 131.7872340425532     | 94         |
| 8            | 129.9041095890411     | 146        |
| 7            | 130.79885057471265    | 174        |
| 6            | 126.88381742738589    | 241        |
| 5            | 121.75982532751091    | 229        |
| 4            | 112.46788990825688    | 327        |
| 3            | 96.4047619047619      | 420        |
| 2            | 79.03016241299304     | 862        |
| 1            | 65.09066666666666     | 375        |
| 0            | 35.47400958869543     | 7926       |

</details>

### Query 7: Sure thing probability failures

<details>
<summary>SQL Query</summary>

```sql
select 
  message, 
  probability, 
  reward, 
  success 
from 
  task_history 
where 
  probability = 'Sure thing' 
  and success = FALSE 
order by 
  success 
LIMIT 
  10;
```
</details>

<details>
<summary>Results</summary>

| MESSAGE                                                              | PROBABILITY | REWARD | SUCCESS |
|----------------------------------------------------------------------|-------------|--------|---------|
| Steal super awesome diamond beer mug from Akua Gardner              | Sure thing  | 196    | FALSE   |
| Steal super awesome diamond horse from Talin Kjartansdóttir         | Sure thing  | 224    | FALSE   |
| Steal super awesome diamond pot from Pridoni Southers               | Sure thing  | 163    | FALSE   |
| Steal super awesome diamond turnips from Lilita Deering             | Sure thing  | 174    | FALSE   |

</details>

### Query 8: All steal super awesome diamond tasks

<details>
<summary>SQL Query</summary>

```sql
SELECT 
  message, 
  probability, 
  reward, 
  success, 
  reputation_state 
FROM 
  task_history 
WHERE 
  message LIKE 'Steal super awesome diamond%';
```
</details>

<details>
<summary>Results</summary>

| MESSAGE                                             | PROBABILITY   | REWARD | SUCCESS | REPUTATION_STATE |
|-----------------------------------------------------|---------------|--------|---------|------------------|
| Steal super awesome diamond horse from Talin Kjartansdóttir | Sure thing    | 224    | FALSE   | -12.0            |
| Steal super awesome diamond clothes from Adina Ericson  | Piece of cake | 185    | FALSE   | -12.0            |
| Steal super awesome diamond turnips from Lilita Deering | Sure thing    | 174    | FALSE   | -10.0            |
| Steal super awesome diamond beer mug from Akua Gardner | Sure thing    | 196    | FALSE   | -10.0            |
| Steal super awesome diamond pot from Pridoni Southers | Sure thing    | 163    | FALSE   | -10.0            |
| Steal super awesome diamond house from Parastu Giel   | Piece of cake | 209    | FALSE   | -10.0            |
| Steal super awesome diamond dog from Gertrudes Armistead | Piece of cake | 270    | FALSE   | -10.0            |

</details>

### Query 9: Dragon level tasks probability distribution

<details>
<summary>SQL Query</summary>

```sql
SELECT * FROM (
SELECT 
  p.probability, 
  d.dragon_level, 
  COUNT(th.id) AS count, 
  (
    COUNT(th.id) * 100.0 / NULLIF(
      SUM(
        COUNT(th.id)
      ) OVER (PARTITION BY d.dragon_level), 
      0
    )
  ) AS percentage 
FROM 
  (
    SELECT 
      DISTINCT probability 
    FROM 
      TASK_HISTORY
  ) p CROSS 
  JOIN (
    SELECT 
      DISTINCT dragon_level 
    FROM 
      TASK_HISTORY
  ) d 
  LEFT JOIN task_history th ON th.probability = p.probability 
  AND th.dragon_level = d.dragon_level 
WHERE 
  th.dragon_level <= 10
GROUP BY 
  p.probability, 
  d.dragon_level )
WHERE percentage > 10
ORDER BY 
  dragon_level, 
  percentage desc;
```
</details>

<details>
<summary>Results</summary>

| PROBABILITY           | DRAGON_LEVEL | COUNT | PERCENTAGE  |
|-----------------------|--------------|-------|-------------|
| Piece of cake         | 0            | 2180  | 27.50       |
| Walk in the park      | 0            | 1297  | 16.36       |
| Quite likely          | 0            | 1005  | 12.68       |
| Piece of cake         | 1            | 97    | 25.87       |
| Walk in the park      | 1            | 80    | 21.33       |
| Quite likely          | 1            | 45    | 12.00       |
| Piece of cake         | 2            | 261   | 30.28       |
| Walk in the park      | 2            | 166   | 19.26       |
| Quite likely          | 2            | 118   | 13.69       |
| Hmmm....              | 2            | 90    | 10.44       |
| Piece of cake         | 3            | 126   | 30.00       |
| Walk in the park      | 3            | 92    | 21.90       |
| Quite likely          | 3            | 55    | 13.10       |
| Hmmm....              | 3            | 47    | 11.19       |
| Piece of cake         | 4            | 94    | 28.75       |
| Walk in the park      | 4            | 65    | 19.88       |
| Quite likely          | 4            | 51    | 15.60       |
| Hmmm....              | 4            | 36    | 11.01       |
| Piece of cake         | 5            | 49    | 21.40       |
| Walk in the park      | 5            | 41    | 17.90       |
| Quite likely          | 5            | 33    | 14.41       |
| Hmmm....              | 5            | 28    | 12.23       |
| Gamble                | 5            | 27    | 11.79       |
| Piece of cake         | 6            | 56    | 23.24       |
| Walk in the park      | 6            | 38    | 15.77       |
| Quite likely          | 6            | 35    | 14.52       |
| Hmmm....              | 6            | 34    | 14.11       |
| Gamble                | 6            | 28    | 11.62       |
| Walk in the park      | 7            | 33    | 18.97       |
| Gamble                | 7            | 26    | 14.94       |
| Piece of cake         | 7            | 25    | 14.37       |
| Quite likely          | 7            | 23    | 13.22       |
| Gamble                | 8            | 22    | 15.07       |
| Quite likely          | 8            | 21    | 14.38       |
| Risky                 | 8            | 21    | 14.38       |
| Piece of cake         | 8            | 18    | 12.33       |
| Hmmm....              | 9            | 16    | 17.02       |
| Rather detrimental    | 9            | 14    | 14.89       |
| Walk in the park      | 9            | 14    | 14.89       |
| Gamble                | 9            | 12    | 12.77       |
| Risky                 | 10           | 11    | 17.46       |
| Hmmm....              | 10           | 9     | 14.29       |
| Piece of cake         | 10           | 8     | 12.70       |
| Quite likely          | 10           | 8     | 12.70       |
| Suicide mission       | 10           | 7     | 11.11       |
| Walk in the park      | 10           | 7     | 11.11       |

</details>


---
