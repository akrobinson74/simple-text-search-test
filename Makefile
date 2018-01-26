loadtest:
	time xargs -n 4 curl -s  < ./src/main/resources/queries.txt > /dev/null
test:
	mvn clean test
run:
	mvn spring-boot:run
