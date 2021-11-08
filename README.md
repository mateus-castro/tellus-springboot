Para fazer build/compile do package via cli...

para limpar o build:
 mvn clean

para criar uma imagem:
 mvn spring-boot:build-image

para build local:
 mvn build

para compilar:
 mvn compile

Para executar o programa via CLI...
 LOCAL:
  java -jar --Dspring.profiles.active=local ./target/tellus-springboot-0.0.1-SNAPSHOT.jar
 HML:
  java -jar --Dspring.profiles.active=hml ./target/tellus-springboot-0.0.1-SNAPSHOT.jar
 PRD:
  java -jar --Dspring.profiles.active=prd ./target/tellus-springboot-0.0.1-SNAPSHOT.jar
