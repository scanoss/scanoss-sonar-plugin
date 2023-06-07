FROM sonarsource/sonar-scanner-cli

RUN addgroup -S scanoss \
    && adduser -S scanoss -G scanoss

USER scanoss

RUN pip install scanoss

