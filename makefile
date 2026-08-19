docker.compose:
	docker compose up --build -d --remove-orphans

docker.start:
	docker compose start

docker.stop:
	docker compose stop

build:
	./mvnw clean install -DskipTests
