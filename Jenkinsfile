pipeline {
    agent any

    environment {
        // --- Deployment Variables ---
        // Match the artifactId from pom.xml and the desired WAR name
        APP_NAME = 'mywebapp2'
        // WAR file path after Maven build
        WAR_PATH = "target/${APP_NAME}.war" 
        // Docker image name for Tomcat
        DOCKER_IMAGE = 'mytomcat2:9.0'
        // Docker container name
        CONTAINER_NAME = 'tomcat2' 
        // Tomcat internal port (standard)
        TOMCAT_INTERNAL_PORT = '8080'
        // Host exposed port (must match your requirement, 9091)
        HOST_EXPOSED_PORT = '9091'
        // Path to the Dockerfile subfolder
        DOCKER_CONTEXT = 'tomcat-docker'
        // Target deployment path inside Tomcat
        TOMCAT_WEBAPPS = '/usr/local/tomcat/webapps'
        
        // --- Git Variables ---
        // FIX: This must be the SIMPLE root URL of the repository.
        GIT_URL = 'https://github.com/Aditi012345/myjavaapp' 
        GIT_BRANCH = 'main'
    }

    stages {
        stage('1. Checkout Code') {
            steps {
                echo "Cloning source code from ${env.GIT_URL}"
                // The git step clones the root of the repository specified here:
                git branch: env.GIT_BRANCH, url: env.GIT_URL
            }
        }

        stage('2. Maven Build (Package WAR)') {
            steps {
                echo 'Building WAR file with Maven...'
                bat 'mvn clean package' 
            }
        }

        stage('3. Docker Build') {
            steps {
                echo "Building Tomcat Docker image: ${env.DOCKER_IMAGE}"
                
                // IMPORTANT: Use the 'dir' step to move into the tomcat-docker subfolder 
                // so the 'docker build' command can find the Dockerfile locally.
                dir("${env.DOCKER_CONTEXT}") {
                    // We run the build command using the current directory (.) as the context.
                    bat "docker build -t ${env.DOCKER_IMAGE} ."
                }
            }
        }

        stage('4. Docker Deploy & Run') {
            steps {
                echo "Removing old container ${env.CONTAINER_NAME} if it exists..."
                // Stop and remove the old container, suppressing failure if it doesn't exist
                bat "docker rm -f ${env.CONTAINER_NAME} || echo 'No old container to remove.'"

                echo "Running new Tomcat container on port ${env.HOST_EXPOSED_PORT}:${env.TOMCAT_INTERNAL_PORT}"
                // Run the new container, mapping the host port 9091 to the container's 8080
                bat "docker run -d --name ${env.CONTAINER_NAME} -p ${env.HOST_EXPOSED_PORT}:${env.TOMCAT_INTERNAL_PORT} ${env.DOCKER_IMAGE}"
            }
        }
        
        stage('5. Deploy WAR to Tomcat') {
            steps {
                echo "Copying ${env.WAR_PATH} into running container ${env.CONTAINER_NAME}"
                // Copy the WAR file from the Jenkins workspace into the Tomcat webapps directory
                bat "docker cp ${env.WAR_PATH} ${env.CONTAINER_NAME}:${env.TOMCAT_WEBAPPS}/"

                echo 'Restarting Tomcat container to deploy the WAR file...'
                // Restart Tomcat to force immediate deployment of the new WAR file
                bat "docker restart ${env.CONTAINER_NAME}"
            }
        }
    }

    post {
        success {
            echo "SUCCESS: Deployment complete. Access app at http://localhost:${env.HOST_EXPOSED_PORT}/${env.APP_NAME}/"
        }
        failure {
            echo "FAILURE: Pipeline failed during build or deployment stages."
        }
    }
}