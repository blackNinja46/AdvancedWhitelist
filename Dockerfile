FROM eclipse-temurin:21-jdk

ARG PAPER_VERSION=1.21.8

ARG PAPER_BUILD=60

LABEL maintainer="Clemens"

WORKDIR /server

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /server/plugins \
    && curl -fsSL -o /server/paper.jar "https://api.papermc.io/v2/projects/paper/versions/${PAPER_VERSION}/builds/${PAPER_BUILD}/downloads/paper-${PAPER_VERSION}-${PAPER_BUILD}.jar" \
    && echo "paper.jar size: $(stat -c%s /server/paper.jar || echo 'unknown')" \
    && (jar tf /server/paper.jar >/dev/null 2>&1 || (echo 'ERROR: paper.jar is not a valid jar' >&2; exit 1))

COPY ./build/libs/AdvancedWhitelist-all.jar /server/plugins/AdvancedWhitelist-all.jar

RUN useradd -m -d /home/minecraft minecraft && chown -R minecraft:minecraft /server
USER minecraft

EXPOSE 25565

CMD ["bash", "-c", "echo 'eula=true' > eula.txt && java -Xmx2G -jar paper.jar nogui"]