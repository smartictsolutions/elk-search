#!/bin/bash

cd ../../backend/elk-app/ || exit 1
./gradlew dockerSaveElkLiquibaseImage || { printf "\ndockerSaveElkLiquibaseImage başarısız! gradlew hata kodu: %s\n" "$?"; exit 1; }
./gradlew dockerSaveElkImage || { printf "\ndockerSaveElkImage başarısız! gradlew hata kodu: %s\n" "$?"; exit 1; }

cd ../../DevOps/elk-docker/ || exit 1

echo "$(tput setaf 2)build-local.sh tamamlandı.$(tput setaf 7)"

exit 0