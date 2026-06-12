跳过检查编译

mvn clean install -Dcheckstyle.skip=true -Dpmd.skip=true -DskipTests

mvn clean compile -Dcheckstyle.skip=true -Dpmd.skip=true -DskipTests

docker build -f docker/Dockerfile -t 192.168.1.181:12316/middleware/openjob-server:latest .
