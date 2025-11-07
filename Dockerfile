FROM ubuntu:latest
LABEL authors="Clemens"

ENTRYPOINT ["top", "-b"]