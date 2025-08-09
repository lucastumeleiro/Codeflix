docker network create elastic
docker network create adm_videos_services

New-Item -ItemType Directory -Force -Path ".docker"
New-Item -ItemType Directory -Force -Path ".docker/es01"
New-Item -ItemType Directory -Force -Path ".docker/keycloak"
New-Item -ItemType Directory -Force -Path ".docker/filebeat"

docker-compose -f services/docker-compose.yml up -d

Write-Host "Inicializando os containers..."
Start-Sleep -Seconds 20 