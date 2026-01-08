FROM mcr.microsoft.com/devcontainers/base:ubuntu-22.04

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl \
    unzip \
    zip \
    clang \
    libssl-dev \
    pkg-config \
    && rm -rf /var/lib/apt/lists/*

# Install Temurin JDK 17
RUN curl -fsSL https://packages.adoptium.net/artifactory/api/gpg/key/public \
    | tee /etc/apt/trusted.gpg.d/adoptium.asc \
    && echo "deb https://packages.adoptium.net/artifactory/deb jammy main" \
    | tee /etc/apt/sources.list.d/adoptium.list \
    && apt-get update \
    && apt-get install -y temurin-17-jdk

ENV JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

# Gradle performance & caching
ENV GRADLE_USER_HOME=/home/vscode/.gradle
ENV GRADLE_OPTS="-Dorg.gradle.daemon=true -Dorg.gradle.jvmargs=-Xmx2g"

# Kotlin Native dependencies expect clang
ENV CC=clang
ENV CXX=clang++

USER vscode
WORKDIR /workspace
