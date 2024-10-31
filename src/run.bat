@echo off

:: Убедитесь, что Java установлена
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java не установлена. Пожалуйста, установите JDK.
    exit /b
)

:: Запуск приложения
java EcosystemApp