# Mocky Server

## Configuration

You must define these environement variables to run Mocky server

```
MOCKY_SERVER_PORT
MOCKY_DATABASE_URL
MOCKY_DATABASE_USER
MOCKY_DATABASE_PASSWORD
MOCKY_ADMIN_PASSWORD
MOCKY_CORS_DOMAIN
MOCKY_ENDPOINT
```

## Run

```
sbt run
```

## Release a new version

```
sbt release
```

## Build release package

```
sbt dist
```
