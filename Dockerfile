FROM sonarsource/sonar-scanner-cli

RUN addgroup -S scanoss \
    && adduser -S scanoss -G scanoss

RUN pip install scanoss

USER scanoss


