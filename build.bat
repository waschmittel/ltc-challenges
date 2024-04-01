@echo off
:: use "call" because mvn itself is a cmd/bat file which would cause this script to stop after mvn
call mvn clean package
cd target
md package
move ltc-challenges.jar package
jpackage \
    --name "LTC Challenges" \
    --input . --icon ../res/icon/app-icon.icns \
    --main-jar ltc-challenges.jar \
    --about-url "https://github.com/waschmittel/ltc-challenges" \
    --copyright "(C) 2024 Daniel Flassak" \
    --description "Challenges for teens." \
    --vendor "Flubba" \
    --app-version "3.1"
