FROM openjdk:19-jdk-alpine
VOLUME /tmp
# Add the application's jar to the container
COPY /*.jar elkapp.jar
#COPY config/ /config/

# openjdk:19-jdk-alpine'da bash yok sh var. docker-compose.yml'da CMD'de bash'a ihtiyacımız var.
RUN apk add --no-cache bash
# Rapor gösteriminde gerekli fontlar alpine sistemine yüklenir.
RUN apk --update add fontconfig ttf-dejavu

# İlk başta bu container'ı ENTRYPOINT ile çalıştırıyorduk ancak veritabanının hazır olmasını bekleyebilmek için artık docker-compose.yml'da CMD ile çalıştırıyoruz.
#ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/elkapp.jar"]
