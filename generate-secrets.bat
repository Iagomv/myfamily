@echo off
REM Script to generate secure random values for deployment

echo.
echo ====================================
echo MyFamily Deployment Helper
echo ====================================
echo.

REM Generate JWT Secret (64 characters)
echo Generating secure JWT_SECRET...
for /F "tokens=1-16 delims=" %%A in ('powershell -Command "for($i=0; $i -lt 64; $i++) { [char][int](Get-Random -Min 33 -Max 126) }" 2^>nul') do set JWT_SECRET=!JWT_SECRET!%%A

if not defined JWT_SECRET (
    echo Error generating JWT_SECRET. Using fallback...
    for /f %%i in ('powershell -Command "[guid]::NewGuid().ToString('N')"') do set JWT_PART1=%%i
    for /f %%i in ('powershell -Command "[guid]::NewGuid().ToString('N')"') do set JWT_PART2=%%i
    set JWT_SECRET=!JWT_PART1!!JWT_PART2!
)

echo.
echo Generated JWT_SECRET:
echo %JWT_SECRET%
echo.
echo Copy the above value to your .env file:
echo JWT_SECRET=%JWT_SECRET%
echo.

REM Generate random database password
echo Generating secure DATABASE_PASSWORD...
for /f %%i in ('powershell -Command "[guid]::NewGuid().ToString('N').Substring(0,16)"') do set DB_PASSWORD=%%i

echo.
echo Generated DATABASE_PASSWORD:
echo %DB_PASSWORD%
echo.
echo Copy to .env file:
echo DATABASE_PASSWORD=%DB_PASSWORD%
echo.

echo ====================================
echo Configuration Complete
echo ====================================
echo.
echo Next steps:
echo 1. Copy the values above
echo 2. Edit your .env file
echo 3. Paste the values
echo 4. Save and commit to .gitignore
echo.
pause
