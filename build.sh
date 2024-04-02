#!/usr/bin/env bash

# set -x # set this for debugging to print all called commands
set -e # fail non non-zero exit status
set -u # fail on unset variable
set -o pipefail # fail if a pipe fails

mvn clean package

cd target
jpackage \
    --name "LTC Challenges" \
    --input . --icon ../res/icon/app-icon.icns \
    --main-jar ltc-challenges.jar \
    --about-url 'https://github.com/waschmittel/ltc-challenges' \
    --copyright '(C) 2024 Daniel Flassak' \
    --description 'Challenges for teens.' \
    --vendor 'Flubba' \
    --app-version '3.1'
