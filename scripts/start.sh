#!/bin/bash
set -e

ENV_FILE="/home/ec2-user/deploy/scripts/.env"
if [ -f "$ENV_FILE" ]; then
    source "$ENV_FILE"
fi

AWS_REGION="ap-northeast-2"

echo "Logging into ECR..."
aws ecr get-login-password --region $AWS_REGION \
    | docker login --username AWS --password-stdin $ECR_REGISTRY

IMAGE="$ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG"
echo "Pulling image: $IMAGE"
docker pull $IMAGE

echo "Starting container..."
docker run -d \
    --name perfscale \
    -p 8080:8080 \
    --restart unless-stopped \
    $IMAGE

echo "Deployment complete."
