FROM mcr.microsoft.com/devcontainers/base:ubuntu-22.04

# Install system dependencies for KMP project (JVM + iOS native compilation)
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    unzip \
    zip \
    clang \
    libssl-dev \
    pkg-config \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Install Temurin JDK 17
RUN curl -fsSL https://packages.adoptium.net/artifactory/api/gpg/key/public \
    | gpg --dearmor -o /etc/apt/trusted.gpg.d/adoptium.gpg \
    && echo "deb https://packages.adoptium.net/artifactory/deb jammy main" \
    | tee /etc/apt/sources.list.d/adoptium.list \
    && apt-get update \
    && apt-get install -y temurin-17-jdk \
    && rm -rf /var/lib/apt/lists/*

# Environment variables
ENV JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

# Gradle optimization for KMP builds
ENV GRADLE_USER_HOME=/home/vscode/.gradle
ENV GRADLE_OPTS="-Dorg.gradle.daemon=true -Dorg.gradle.jvmargs=-Xmx3g -Dorg.gradle.parallel=true -Dkotlin.daemon.jvmargs=-Xmx1536m"

# Kotlin Native dependencies
ENV CC=clang
ENV CXX=clang++

# Pre-create Gradle cache directories with proper permissions
RUN mkdir -p /home/vscode/.gradle && chown -R vscode:vscode /home/vscode/.gradle

USER vscode
WORKDIR /workspaces/wdist
