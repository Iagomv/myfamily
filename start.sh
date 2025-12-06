#!/bin/bash
set -e

echo "🚀 Starting MyFamily Application"

# Determine which service to start based on environment
if [ -n "$RAILWAY_SERVICE_NAME" ]; then
  echo "📦 Service: $RAILWAY_SERVICE_NAME"
  
  case $RAILWAY_SERVICE_NAME in
    "backend")
      echo "🔨 Starting Spring Boot Backend..."
      cd backend/myfamily
      mvn clean package -DskipTests
      java -jar target/demo-0.0.1-SNAPSHOT.jar
      ;;
    "frontend")
      echo "🎨 Starting Angular Frontend..."
      cd frontend/myFamily
      npx ng serve --host 0.0.0.0 --poll 2000
      ;;
    *)
      echo "❌ Unknown service: $RAILWAY_SERVICE_NAME"
      exit 1
      ;;
  esac
else
  echo "⚠️  No RAILWAY_SERVICE_NAME set. This script should be used with Railway services."
  echo "Please deploy backend and frontend as separate services in Railway."
  exit 1
fi
