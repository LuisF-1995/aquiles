#!/bin/bash

# Verificar si se proporcionan suficientes argumentos
if [ $# -ne 2 ]; then
    echo "Uso: $0 nombre_basedatos usuario"
    exit 1
fi

# Asignar argumentos a variables
DB_NAME="$1"
DB_USER="$2"
ROOT_PASS=LuisDevMed@2022*

# Comando para crear la base de datos
mysql -u root -p"$ROOT_PASS" -e "CREATE DATABASE IF NOT EXISTS $DB_NAME DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode>
# Comando para verificar si la base de datos se creó exitosamente
DB_EXIST=$(mysql -u root -p"$ROOT_PASS" -se "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='$DB>
if [ "$DB_EXIST" = "$DB_NAME" ]; then
    # Comando para crear un usuario y concederle permisos sobre la base de datos
    mysql -u root -p"$ROOT_PASS" -e "CREATE USER '$DB_USER'@'localhost' IDENTIFIED BY '$ROOT_PASS';"
    mysql -u root -p"$ROOT_PASS" -e "GRANT ALL PRIVILEGES ON $DB_NAME.* TO '$DB_USER'@'localhost';"
    mysql -u root -p"$ROOT_PASS" -e "FLUSH PRIVILEGES;"
    echo "true"
else
    echo "false"
fi