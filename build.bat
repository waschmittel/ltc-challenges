@echo off
:: use "call" because mvn itself is a cmd/bat file which would cause this script to stop after mvn
call mvn clean package
cd target
md package
move ltc-challenges.jar package
cd package
jpackage ^
    --name "LTC Challenges" ^
    --input . --icon ../../res/icon/app-icon.png ^
    --main-jar ltc-challenges.jar ^
    --main-class de.flubba.ltcchallenges.LtcChallenges ^
    --about-url "https://github.com/waschmittel/ltc-challenges" ^
    --copyright "(C) 2024 Daniel Flassak" ^
    --description "Challenges for teens." ^
    --win-menu ^
    --vendor "Flubba" ^
    --win-per-user-install ^
    --type "exe" ^
    --app-version "3.1"
