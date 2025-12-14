#!/bin/sh
set -e

# This script creates an `admin` role and ensures both the `admin` and
# application DB (from POSTGRES_DB) exist and are owned by `admin`.
# It tolerates "already exists" errors so it can be safely re-run.

echo "Running DB init script..."

# Use the `postgres` maintenance DB for these commands so they run
# even when the `admin` database doesn't exist yet.

# Create role `admin` (ignore error if it already exists)
psql --username "$POSTGRES_USER" --dbname postgres -c "CREATE ROLE admin WITH LOGIN PASSWORD '${POSTGRES_PASSWORD}';" || true

# Create database `admin` if missing (ignore error if exists)
psql --username "$POSTGRES_USER" --dbname postgres -c "CREATE DATABASE admin OWNER admin;" || true

# Create application database (POSTGRES_DB) if missing and set owner to admin
psql --username "$POSTGRES_USER" --dbname postgres -c "CREATE DATABASE ${POSTGRES_DB:-myfamily_db} OWNER admin;" || true

# Grant privileges
psql --username "$POSTGRES_USER" --dbname postgres -c "GRANT ALL PRIVILEGES ON DATABASE admin TO admin;" || true
psql --username "$POSTGRES_USER" --dbname postgres -c "GRANT ALL PRIVILEGES ON DATABASE ${POSTGRES_DB:-myfamily_db} TO admin;" || true

echo "DB init script finished."
