#!/bin/bash

mvn -pl system -ntp clean package liberty:create liberty:install-feature liberty:deploy
mvn -pl query -ntp clean package liberty:create liberty:install-feature liberty:deploy

mvn -pl system -ntp -P test liberty:start
mvn -pl query -ntp -Dliberty.var.mp.config.profile="test" liberty:start

mvn -pl system -ntp -P test failsafe:integration-test
mvn -pl query -ntp failsafe:integration-test

mvn -pl query -ntp liberty:stop
mvn -pl system -ntp liberty:stop
