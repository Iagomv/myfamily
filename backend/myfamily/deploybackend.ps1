mvn clean package -DskipTests
docker build -t iagomartinezvarela/myfamily-backend:latest .
docker push iagomartinezvarela/myfamily-backend:latest