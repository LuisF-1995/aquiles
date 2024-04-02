@echo off

REM Verificar si se proporcionan suficientes argumentos
if "%~2"=="" (
    echo Uso: %0 nombre_basedatos usuario
    exit /b 1
)

REM Asignar argumentos a variables
set "DB_NAME=%~1"
set "DB_USER=%~2"
set "ROOT_PASS=DevMed@2022*"

REM Comando para crear la base de datos
mysql -u root -p"%ROOT_PASS%" -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;"

REM Comando para verificar si la base de datos se creó exitosamente
for /f "usebackq" %%A in (`mysql -u root -p"%ROOT_PASS%" -se "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='%DB_NAME%';"`) do set "DB_EXIST=%%A"

if "%DB_EXIST%"=="%DB_NAME%" (
    REM Comando para crear un usuario y concederle permisos sobre la base de datos
    mysql -u root -p"%ROOT_PASS%" -e "CREATE USER '%DB_USER%'@'localhost' IDENTIFIED BY '%ROOT_PASS%';"
    mysql -u root -p"%ROOT_PASS%" -e "GRANT ALL PRIVILEGES ON %DB_NAME%.* TO '%DB_USER%'@'localhost';"
    mysql -u root -p"%ROOT_PASS%" -e "FLUSH PRIVILEGES;"
    echo true
) else (
    echo false
)
