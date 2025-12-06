#!/usr/bin/env pwsh

<#
.SYNOPSIS
    Generate secure secrets for MyFamily deployment

.DESCRIPTION
    Generates JWT_SECRET and DATABASE_PASSWORD for secure deployment

.EXAMPLE
    ./generate-secrets.ps1
#>

Write-Host ""
Write-Host "===================================="
Write-Host "MyFamily Deployment Helper"
Write-Host "===================================="
Write-Host ""

# Generate JWT Secret (64 random characters)
Write-Host "Generating secure JWT_SECRET (64 characters)..." -ForegroundColor Green
$jwt_chars = @()
for ($i = 0; $i -lt 64; $i++) {
  $jwt_chars += [char][int](Get-Random -Min 33 -Max 126)
}
$JWT_SECRET = -join $jwt_chars

Write-Host ""
Write-Host "JWT_SECRET (64 chars):" -ForegroundColor Cyan
Write-Host $JWT_SECRET
Write-Host ""
Write-Host "Add to .env:" -ForegroundColor Yellow
Write-Host "JWT_SECRET=$JWT_SECRET" -ForegroundColor White
Write-Host ""

# Generate secure database password (16 alphanumeric characters)
Write-Host "Generating secure DATABASE_PASSWORD..." -ForegroundColor Green
$chars = @()
for ($i = 0; $i -lt 16; $i++) {
  $charType = Get-Random -InputObject @("upper", "lower", "number")
  switch ($charType) {
    "upper" { $chars += [char](Get-Random -InputObject (65..90)) }
    "lower" { $chars += [char](Get-Random -InputObject (97..122)) }
    "number" { $chars += [char](Get-Random -InputObject (48..57)) }
  }
}
$DB_PASSWORD = -join $chars

Write-Host ""
Write-Host "DATABASE_PASSWORD (16 chars):" -ForegroundColor Cyan
Write-Host $DB_PASSWORD
Write-Host ""
Write-Host "Add to .env:" -ForegroundColor Yellow
Write-Host "DATABASE_PASSWORD=$DB_PASSWORD" -ForegroundColor White
Write-Host ""

# Generate sample .env entry
Write-Host "===================================="
Write-Host "Complete .env Template"
Write-Host "===================================="
Write-Host ""
Write-Host ".env file content:" -ForegroundColor Cyan
Write-Host @"
# Database
DB_NAME=myfamily_db
DB_USER=admin
DB_PASSWORD=$DB_PASSWORD

# JWT Secret
JWT_SECRET=$JWT_SECRET

# Application
ALLOWED_ORIGINS=http://localhost:3000
API_URL=http://localhost:8080/api
"@ -ForegroundColor White

Write-Host ""
Write-Host "===================================="
Write-Host "Next Steps:"
Write-Host "===================================="
Write-Host "1. Create or edit .env file in project root"
Write-Host "2. Copy the values from above"
Write-Host "3. Save the file"
Write-Host "4. IMPORTANT: Add .env to .gitignore"
Write-Host "5. Run: docker-compose up --build"
Write-Host ""
Write-Host "For production deployment:"
Write-Host "1. Generate new secrets"
Write-Host "2. Set in platform (Railway/Render)"
Write-Host "3. Never commit .env to Git"
Write-Host ""
