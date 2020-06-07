#!/bin/sh

id="$1"

curl -X DELETE -H "X-Auth-Token: $SETTINGS_ADMIN_PASSWORD" "http://localhost:8080/admin/api/$id" -v