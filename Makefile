build-frontend:
	infrasave/infrasave-react-client/node/npm ci --prefix infrasave/infrasave-react-client && infrasave/infrasave-react-client/node/npm start --prefix infrasave/infrasave-react-client

build-db:
	docker-compose -f infrasave/docker-compose.yml up -d db

build-backend:
	cd infrasave/infrasave-rest-app && ./mvnw spring-boot:run