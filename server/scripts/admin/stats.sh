#!/bin/sh

curl -H "X-Auth-Token: $SETTINGS_ADMIN_PASSWORD" "http://localhost:8080/admin/api/stats" -v