#!/bin/bash

# herhangi bir komut başarısız olursa scriptin devam etmemesini sağlıyor
# ancak istisnai olarak || ile bağlanan komutlarda bu durum geçerli değil. hata mesajı yazdıracağımız yerlerde exit 1 çağırmalıyız
set -e


if ! command -v docker &> /dev/null || ! command -v docker-compose &> /dev/null || ! command -v curl &> /dev/null || ! command -v ./yq.exe &> /dev/null
then
  # ./yq.exe için bkz. https://mikefarah.gitbook.io/./yq.exe/
  echo "docker, docker-compose, curl veya ./yq.exe kurulu değil!"
  echo "./yq.exe             -> sudo apt update && sudo apt install snapd && sudo snap install ./yq.exe"
  echo "curl           -> sudo apt update && sudo apt install curl"
  echo "docker         -> https://docs.docker.com/engine/install/ubuntu/#install-using-the-repository"
  echo "docker-compose -> https://docs.docker.com/compose/install/#install-compose-on-linux-systems"
  #exit 1
fi

#her yeni image build edildiğinde veya load edildiğinde docker dangling image oluşturur. Bu imageler gereksiz yer kaplar. Aşağıdaki komut ile temizlenir.
docker rmi $(docker images -qf "dangling=true") || { printf "\nDangling image'lar temizlenemedi! docker hata kodu: %s\n" "$?"; true; }
docker volume rm $(docker volume ls -qf dangling=true) || { printf "\nDangling volume'ler temizlenemedi! docker hata kodu: %s\n" "$?"; true; }

# elastic searchün sıfırlanmaması için elastic search'e ait app directory'sini dışarıdan volume olarak mount ediyoruz. bkz. docker-compose.yml:elk-app
mkdir -p -m 0700 ../elk-docker/elk_exposed_config || { printf "\nelk_exposed_config directory oluşturulamadı! docker hata kodu: %s\n" "$?"; exit 1; }

DOCKER_COMPOSE_FILE="../elk-docker/docker-compose-prod.yml"

docker-compose --ansi never -f $DOCKER_COMPOSE_FILE up -d --remove-orphans || { printf "\n'docker-compose up' başarısız! docker hata kodu: %s\n" "$?"; exit 1; }

echo "$(tput setaf 2)install_elk_app-for-windows.sh tamamlandı.$(tput setaf 7)"
exit 0
