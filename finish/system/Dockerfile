FROM icr.io/appcafe/open-liberty:kernel-slim-java11-openj9-ubi

ARG VERSION=1.0
ARG REVISION=SNAPSHOT
LABEL \
  org.opencontainers.image.authors="My Name" \
  org.opencontainers.image.vendor="Open Liberty" \
  org.opencontainers.image.url="local" \
  org.opencontainers.image.source="https://github.com/OpenLiberty/guide-microprofile-config-profile" \
  org.opencontainers.image.version="$VERSION" \
  org.opencontainers.image.revision="$REVISION" \
  vendor="Open Liberty" \
  name="system" \
  version="$VERSION-$REVISION" \
  summary="The system microservice from the Externalizing environment-specific microservice configuration for DevOps guide" \
  description="This image contains the system microservice running with the Open Liberty runtime."

# tag::config[]
COPY --chown=1001:0 src/main/liberty/config /config/
# end::config[]

RUN features.sh

# tag::war[]
COPY --chown=1001:0 target/guide-microprofile-config-profile-system.war /config/apps
# end::war[]

RUN configure.sh
