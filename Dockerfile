# Kotlin JDK 17 ile başlayalım
FROM openjdk:23-jdk-slim AS build

# Çalışma dizini
WORKDIR /app

# Gradle önbelleğini optimize etmek için bağımlılıkları kopyala ve yükle
COPY build.gradle.kts settings.gradle.kts gradle.properties gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon

# Projenin kalanını kopyala ve build yap
COPY . .
RUN ./gradlew installDist --no-daemon

# Minimal JRE ile çalıştırma aşaması
FROM openjdk:23-jre-slim
WORKDIR /app
COPY --from=build /app/build/install/ .
EXPOSE 8080
CMD ["crmappurtim/bin/crmappurtim"]