# config.yaml 비밀번호를 입렵하는 페이이지 업어지기 위함

# Docker 빌드
docker build . -t vscode

# Container 올림 [sh 파일 실행] 
# @params
# --- projects 폴더 전체 프로젝트 저장하는 폴더
# --- user1 유저별로 프로젝트 저장하는 풀더
# --- 해당하는 Container 접근하기 위해서 Port //사용자별로 PORT 제공
./make-container.sh projects user1 3001

# Install for node
sudo apt-get install curl
curl -sL https://deb.nodesource.com/setup_13.x | sudo -E bash -
sudo apt-get install nodejs


# Install for java

sudo apt install default-jdk
- Install Spring Boot CLI

sudo apt install unzip zip
curl -s https://get.sdkman.io | bash
source "/config/.sdkman/bin/sdkman-init.sh"
sdk install springboot
sdk install maven


spring init --build=maven --dependencies=web --name=hello hello-world
./mvnw clean
./mvnw spring-boot:run


# Install Django
sudo apt-get install python3 python3-pip
pip3 install Django
django-admin