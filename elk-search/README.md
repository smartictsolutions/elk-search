### Before UI Development

- DevOps\elk-docker dizininde terminal ekranı açılır.
- "docker-compose -f docker-compose-dev.yml up -d" komutu çalıştırılarak gerekli yazılım ve servisler docker üzerinde ayaklandırılır.
- backend\elk-app dizininde terminal ekranı açılır.
- "./gradlew dbLoad" komutu ile veritabanı tabloları oluşturulur.
- ui/ dizininde terminal ekranı açılır.
- "yarn install" komutu ile ui kütüphaneleri yüklenir.
- 