#!/bin/sh

id="$1"
secret="$2"

curl -X PUT \
  -H 'Content-Type: application/json' \
  -d "{
       \"content\": \"{\\\"hello\\\":\\\"world\\\"}\",
       \"content_type\": \"application/json\",
       \"charset\": \"UTF-8\",
       \"status\": 200,
       \"secret\": \"$secret\",
       \"headers\": {
         \"X-FOO\": \"bar\",
         \"X-BAR\": \"foo\"
       }
     }" \
  "http://localhost:8080/api/mock/$id" -v