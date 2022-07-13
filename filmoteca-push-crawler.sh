#!/bin/bash
echo '----'
echo $(pwd)
source ~/filmoteca/filmoteca.environment
echo $FILMOTECA_PATH
echo $FILMOTECA_DATABASE_SERVER
echo $URL_FILMOTECA_API
# java -jar ~/filmoteca/filmoteca-push-crawler.jar ab
java -jar ~/filmoteca/filmoteca-push-crawler.jar cv
