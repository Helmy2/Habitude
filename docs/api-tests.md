# Habitude API — Bash / cURL Test Suite

Base URL: `http://localhost:8080/api/v1`

## 1. Create a New Habit (Valid)

```Bash
curl -i -X POST http://localhost:8080/api/v1/habits \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Read 20 pages",
    "description": "Read technical books or architecture papers",
    "frequencyType": "DAILY",
    "targetCount": 1
  }'
```

**Expected Status:** `201 Created`  
**Headers:** `Location: http://localhost:8080/api/v1/habits/1`

## 2. Create Habit with Validation Errors (Bad Request)

```Bash
curl -i -X POST http://localhost:8080/api/v1/habits \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "description": "Testing empty title and invalid targetCount",
    "frequencyType": "WEEKLY",
    "targetCount": 10
  }'
```

**Expected Status:** `400 Bad Request`  
**Response:** RFC 7807 `ProblemDetail` with field-level `"errors"` map.

## 3. Create a Second Habit (Weekly Target)

```Bash
curl -i -X POST http://localhost:8080/api/v1/habits \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Gym Workout",
    "description": "Upper / Lower split",
    "frequencyType": "WEEKLY",
    "targetCount": 4
  }'
```

**Expected Status:** `201 Created`

## 4. Get All Active Habits (Excludes Archived)

```Bash
curl -i -X GET http://localhost:8080/api/v1/habits \
  -H "Accept: application/json"
```

**Expected Status:** `200 OK`

## 5. Get Habit by ID (Success)

```Bash
curl -i -X GET http://localhost:8080/api/v1/habits/1 \
  -H "Accept: application/json"
```

**Expected Status:** `200 OK`

## 6. Get Habit by ID (Not Found)

```Bash
curl -i -X GET http://localhost:8080/api/v1/habits/999 \
  -H "Accept: application/json"
```

**Expected Status:** `404 Not Found`

## 7. Update an Existing Habit

```Bash
curl -i -X PUT http://localhost:8080/api/v1/habits/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Read 30 pages",
    "description": "Read technical books, system architecture papers, or newsletters",
    "frequencyType": "DAILY",
    "targetCount": 1,
    "archived": false
  }'
```

**Expected Status:** `200 OK`

## 8. Archive a Habit (Dedicated PATCH Endpoint)

```Bash
curl -i -X PATCH http://localhost:8080/api/v1/habits/2/archive \
  -H "Accept: application/json"
```

**Expected Status:** `200 OK` (`"archived": true`)

## 9. Unarchive a Habit (Dedicated PATCH Endpoint)

```Bash
curl -i -X PATCH http://localhost:8080/api/v1/habits/2/unarchive \
  -H "Accept: application/json"
```

**Expected Status:** `200 OK` (`"archived": false`)

## 10. List Active Habits (Excludes Archived)

```Bash
curl -i -X GET "http://localhost:8080/api/v1/habits?includeArchived=false" \
  -H "Accept: application/json"
```

**Expected Status:** `200 OK`

## 11. List All Habits (Includes Archived)

```Bash
curl -i -X GET "http://localhost:8080/api/v1/habits?includeArchived=true" \
  -H "Accept: application/json"
```

**Expected Status:** `200 OK`

## 12. Delete a Habit (Hard Delete)

```Bash
curl -i -X DELETE http://localhost:8080/api/v1/habits/2
```

**Expected Status:** `204 No Content`

## 13. Verify Deletion

```Bash
curl -i -X GET http://localhost:8080/api/v1/habits/2 \
  -H "Accept: application/json"
```

**Expected Status:** `404 Not Found`

## One-Shot Script (Run All in Sequence)

Save and run this in your terminal if you want to execute the full smoke test in one go:

```Bash
#!/usr/bin/env bash

echo "--- 1. Create Habit 1 ---"
curl -s -X POST http://localhost:8080/api/v1/habits \
  -H "Content-Type: application/json" \
  -d '{"title":"Read 20 pages","description":"Architecture books","frequencyType":"DAILY","targetCount":1}'
echo -e "\n"

echo "--- 2. Create Habit 2 ---"
curl -s -X POST http://localhost:8080/api/v1/habits \
  -H "Content-Type: application/json" \
  -d '{"title":"Gym Workout","description":"Upper/Lower split","frequencyType":"WEEKLY","targetCount":4}'
echo -e "\n"

echo "--- 3. Fetch Active Habits ---"
curl -s http://localhost:8080/api/v1/habits
echo -e "\n"

echo "--- 4. Update Habit 1 ---"
curl -s -X PUT http://localhost:8080/api/v1/habits/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Read 30 pages","description":"Architecture books","frequencyType":"DAILY","targetCount":1,"archived":false}'
echo -e "\n"

echo "--- 5. Archive Habit 2 via PATCH ---"
curl -s -X PATCH http://localhost:8080/api/v1/habits/2/archive
echo -e "\n"

echo "--- 6. Delete Habit 2 ---"
curl -s -i -X DELETE http://localhost:8080/api/v1/habits/2 | head -n 1
echo -e "\n"

echo "--- 7. Verify Remaining Habits ---"
curl -s http://localhost:8080/api/v1/habits
echo -e "\n"
```