#!/bin/sh

id="$1"

curl "http://localhost:8080/v3/$id"