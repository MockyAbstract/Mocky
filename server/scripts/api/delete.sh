#!/bin/sh

id="$1"
secret="$2"

curl -X DELETE -d "{ \"secret\": \"$secret\" }" "http://localhost:8080/api/mock/$id" -v