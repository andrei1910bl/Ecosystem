#!/bin/bash

# Убедитесь, что Java установлена
if ! command -v java &> /dev/null
then
    echo "Java не установлена. Пожалуйста, установите JDK."
    exit
fi

# Запуск приложения
java EcosystemApp