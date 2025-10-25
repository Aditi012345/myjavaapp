# tomcat-docker/Dockerfile
# Uses a standard Tomcat 9 base image
FROM tomcat:9.0

# Tomcat runs on port 8080 by default, EXPOSE is informational
EXPOSE 8080

# The base Tomcat image is usually sufficient. 
# We rely on 'docker cp' from Jenkins to move the WAR file, 
# so no ADD or COPY commands are needed here for the application code.

# Command to run Tomcat (inherited from base image, included for clarity)
CMD ["catalina.sh", "run"]