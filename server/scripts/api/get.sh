#!/bin/sh

id="$1"

curl "http://localhost:8080/api/mock/$id" -v