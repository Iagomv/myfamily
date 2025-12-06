#!/usr/bin/env pwsh

<#
.SYNOPSIS
    List all deployment configuration files created

.DESCRIPTION
    Shows file structure and purpose of all deployment-related files
#>

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════════╗"
Write-Host "║         MyFamily Deployment Configuration - File Index         ║"
Write-Host "╚════════════════════════════════════════════════════════════════╝"
Write-Host ""

Write-Host "📋 DOCUMENTATION FILES" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "Location                        | Purpose" -ForegroundColor White
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "QUICK_START.md                  | ⭐ START HERE - Quick reference"
Write-Host "DEPLOYMENT_SETUP_COMPLETE.md    | Overview of everything created"
Write-Host "DEPLOYMENT.md                   | Comprehensive deployment guide"
Write-Host "DEPLOYMENT_CHECKLIST.md         | Step-by-step checklists"
Write-Host "RAILWAY_DEPLOYMENT.md           | Railway.app specific guide"
Write-Host "RENDER_DEPLOYMENT.md            | Render.com specific guide"
Write-Host "DOCKER_COMPOSE_GUIDE.md         | Docker commands reference"
Write-Host ""

Write-Host "🐳 DOCKER CONFIGURATION" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "Location                        | Purpose" -ForegroundColor White
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "docker-compose.yml              | Development setup"
Write-Host "docker-compose.prod.yml         | Production setup"
Write-Host "backend/myfamily/Dockerfile.prod| Spring Boot image"
Write-Host "frontend/myFamily/Dockerfile.prod| Angular/Nginx image"
Write-Host "frontend/myFamily/nginx.conf    | Nginx server config"
Write-Host "frontend/myFamily/default.conf  | Frontend routing rules"
Write-Host ""

Write-Host "⚙️  CONFIGURATION & ENVIRONMENT" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "Location                        | Purpose" -ForegroundColor White
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host ".env.example                    | Template for environment vars"
Write-Host ".gitignore                      | Prevents committing secrets"
Write-Host "backend/myfamily/src/main/resources/application-prod.properties | Prod config"
Write-Host ""

Write-Host "🔄 CI/CD WORKFLOWS" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "Location                        | Purpose" -ForegroundColor White
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host ".github/workflows/build-and-test.yml | Automated testing"
Write-Host ".github/workflows/deploy.yml    | Automated deployment"
Write-Host ""

Write-Host "🛠️  UTILITY SCRIPTS" -ForegroundColor Cyan
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "Location                        | Purpose" -ForegroundColor White
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "generate-secrets.ps1            | PowerShell secret generator"
Write-Host "generate-secrets.bat            | Batch secret generator"
Write-Host "FILES_INDEX.ps1                 | This file"
Write-Host ""

Write-Host "═══════════════════════════════════════════════════════════════════"
Write-Host ""

Write-Host "📊 QUICK DEPLOYMENT OPTIONS" -ForegroundColor Yellow
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host ""
Write-Host "Option 1: LOCAL TESTING (Recommended First)" -ForegroundColor Green
Write-Host "  Command: docker-compose up --build"
Write-Host "  Time: 5 minutes"
Write-Host "  Guide: DOCKER_COMPOSE_GUIDE.md"
Write-Host ""

Write-Host "Option 2: RAILWAY.APP (Recommended for Cloud)" -ForegroundColor Green
Write-Host "  Cost: $5/month free credit"
Write-Host "  Time: 10-15 minutes"
Write-Host "  Guide: RAILWAY_DEPLOYMENT.md"
Write-Host ""

Write-Host "Option 3: RENDER.COM (Free Alternative)" -ForegroundColor Green
Write-Host "  Cost: Free (auto-sleep after 15 min)"
Write-Host "  Time: 15-20 minutes"
Write-Host "  Guide: RENDER_DEPLOYMENT.md"
Write-Host ""

Write-Host "Option 4: SELF-HOSTED VPS" -ForegroundColor Green
Write-Host "  Cost: $5-20/month for server"
Write-Host "  Time: 30-45 minutes"
Write-Host "  Guide: DEPLOYMENT.md (Section 3)"
Write-Host ""

Write-Host "═══════════════════════════════════════════════════════════════════"
Write-Host ""

Write-Host "🚀 QUICK START COMMANDS" -ForegroundColor Magenta
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host ""
Write-Host "1. Generate secure passwords:"
Write-Host "   ./generate-secrets.ps1" -ForegroundColor White
Write-Host ""
Write-Host "2. Create .env file:"
Write-Host "   cp .env.example .env" -ForegroundColor White
Write-Host "   # Edit .env with generated values"
Write-Host ""
Write-Host "3. Test locally:"
Write-Host "   docker-compose up --build" -ForegroundColor White
Write-Host ""
Write-Host "4. Access application:"
Write-Host "   Frontend: http://localhost:3000" -ForegroundColor White
Write-Host "   API: http://localhost:8080/api"
Write-Host "   Swagger: http://localhost:8080/api/swagger-ui.html"
Write-Host ""

Write-Host "═══════════════════════════════════════════════════════════════════"
Write-Host ""

Write-Host "📚 DOCUMENTATION READING ORDER" -ForegroundColor Blue
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host ""
Write-Host "1. QUICK_START.md" -ForegroundColor White
Write-Host "   → Overview of all options"
Write-Host ""
Write-Host "2. DEPLOYMENT_SETUP_COMPLETE.md" -ForegroundColor White
Write-Host "   → What has been created"
Write-Host ""
Write-Host "3. Platform-Specific Guide:" -ForegroundColor White
Write-Host "   → RAILWAY_DEPLOYMENT.md OR"
Write-Host "   → RENDER_DEPLOYMENT.md OR"
Write-Host "   → DEPLOYMENT.md (Section 3)"
Write-Host ""
Write-Host "4. DEPLOYMENT_CHECKLIST.md" -ForegroundColor White
Write-Host "   → During deployment for verification"
Write-Host ""
Write-Host "5. DOCKER_COMPOSE_GUIDE.md" -ForegroundColor White
Write-Host "   → Troubleshooting reference"
Write-Host ""

Write-Host "═══════════════════════════════════════════════════════════════════"
Write-Host ""

Write-Host "✅ WHAT'S INCLUDED" -ForegroundColor Green
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "✓ Production-ready Docker configurations"
Write-Host "✓ Multi-stage Docker builds for optimization"
Write-Host "✓ Environment variable management"
Write-Host "✓ Database migration support"
Write-Host "✓ CORS and security settings"
Write-Host "✓ Health checks and monitoring"
Write-Host "✓ CI/CD GitHub Actions workflows"
Write-Host "✓ Nginx reverse proxy configuration"
Write-Host "✓ Spring Boot production profile"
Write-Host "✓ Comprehensive documentation"
Write-Host "✓ Deployment checklists"
Write-Host "✓ Troubleshooting guides"
Write-Host ""

Write-Host "═══════════════════════════════════════════════════════════════════"
Write-Host ""

Write-Host "⚠️  IMPORTANT REMINDERS" -ForegroundColor Red
Write-Host "─────────────────────────────────────────────────────────────────"
Write-Host "• NEVER commit .env file to Git"
Write-Host "• Generate new JWT_SECRET for production"
Write-Host "• Change all default passwords"
Write-Host "• Review CORS settings before deployment"
Write-Host "• Test locally before deploying to cloud"
Write-Host "• Configure backups for your database"
Write-Host "• Use HTTPS in production"
Write-Host "• Monitor resource usage on free tier"
Write-Host ""

Write-Host "═══════════════════════════════════════════════════════════════════"
Write-Host ""
Write-Host "Need help? Read QUICK_START.md first!" -ForegroundColor Yellow
Write-Host ""
