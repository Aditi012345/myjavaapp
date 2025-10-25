pipeline {
    agent any

    environment {
        // --- Deployment Variables ---
        APP_NAME = 'mywebapp2'
        WAR_PATH = "target/${APP_NAME}.war" 
        DOCKER_IMAGE = 'mytomcat2:9.0'
        CONTAINER_NAME = 'tomcat2' 
        TOMCAT_INTERNAL_PORT = '8080'
        HOST_EXPOSED_PORT = '9091'
        DOCKER_CONTEXT = 'tomcat-docker'
        TOMCAT_WEBAPPS = '/usr/local/tomcat/webapps'
        
        // --- Git Variables ---
        // FIX: Simple root URL
        GIT_URL = 'https://github.com/Aditi012345/myjavaapp' 
        GIT_BRANCH = 'main'
    }

    stages {
        stage('1. Checkout Code') {
            steps {
                echo "Cloning source code from ${env.GIT_URL}"
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
                
                // FINAL FIX ATTEMPT: Use the Windows WORKSPACE variable 
                // to provide the full absolute path, forcing Docker client to resolve it.
                bat 'docker build -t %DOCKER_IMAGE% %WORKSPACE%\\%DOCKER_CONTEXT%'
            }
        }

        stage('4. Docker Deploy & Run') {
            steps {
                echo "Removing old container ${env.CONTAINER_NAME} if it exists..."
                bat "docker rm -f ${env.CONTAINER_NAME} || echo 'No old container to remove.'"

                echo "Running new Tomcat container on port ${env.HOST_EXPOSED_PORT}:${env.TOMCAT_INTERNAL_PORT}"
                bat "docker run -d --name ${env.CONTAINER_NAME} -p ${env.HOST_EXPOSED_PORT}:${env.TOMCAT_INTERNAL_PORT} ${env.DOCKER_IMAGE}"
            }
        }
        
        stage('5. Deploy WAR to Tomcat') {
            steps {
                echo "Copying ${env.WAR_PATH} into running container ${env.CONTAINER_NAME}"
                bat "docker cp ${env.WAR_PATH} ${env.CONTAINER_NAME}:${env.TOMCAT_WEBAPPS}/"

                echo 'Restarting Tomcat container to deploy the WAR file...'
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