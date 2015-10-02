FROM java:latest

MAINTAINER Deepu Roy

ADD /jars /jars
EXPOSE 8080

# On some linux distributions the /dev/random blocks. The -Djava.security.egd=file:/dev/./urandom
# fixes this.
# See
#  http://scott.quine.io/2015/05/10/when-dev-random-fights-back.html
#  http://blog.pivotal.io/pivotal-cloud-foundry/features/challenges-with-randomness-in-multi-tenant-linux-container-platforms
#  http://security.stackexchange.com/questions/14386/what-do-i-need-to-configure-to-make-sure-my-software-uses-dev-urandom/14387#14387


ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /jars/users-0.0.1-SNAPSHOT.jar 