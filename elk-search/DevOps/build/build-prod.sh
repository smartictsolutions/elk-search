#!/bin/bash

#Jenkinsfile stage -> Docker_Build: ELK_LIQUIBASE,Docker_Build: ELK_APP
cd ../../backend/elk-app/ || exit 1
./gradlew dockerSaveElkLiquibaseImage || { printf "\ndockerSaveElkLiquibaseImage başarısız! gradlew hata kodu: %s\n" "$?"; exit 1; }
./gradlew -PclientEnv=prod dockerSaveElkImage || { printf "\ndockerSaveElkImage başarısız! gradlew hata kodu: %s\n" "$?"; exit 1; }

cd ../../DevOps/ || exit 1

#Jenkinsfile stage -> Delete_Old_Compressed_File: COMPRESSION
#Daha önceden sıkıştırılmış paketler siliniyor.
rm -f ./setup/elk_app_server.tar.7z || { printf "\nelk_app_server.tar.7z sıkıştırılmış dosyanın silinmesi başarısız oldu!"; exit 1; }

#Jenkinsfile stage -> Compress_Elk_App_Server_Installation_Files: COMPRESSION
#Uygulama sunucusuna yükleme için kullanılacak dosyalar sıkıştırılıyor.
tar cvf ./setup/elk_app_server.tar.7z \
         ./elk-docker/elk-app-image.tar \
         ./elk-docker/docker-compose-prod.yml \
         ./elk-docker/elk_exposed_config \
         ./install  || { printf "\elk_app_server.tar.7z sıkıştırma başarısız!"; exit 1; }

echo "$(tput setaf 2)elk_app_server.tar.7z oluşturuldu.$(tput setaf 7)"

echo "$(tput setaf 2)build-prod.sh tamamlandı.$(tput setaf 7)"

exit 0
