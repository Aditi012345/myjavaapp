pipeline {
    agent any

    environment {
        // Define the name of your compiled application artifact
        ARTIFACT_NAME = 'myapp-1.0-SNAPSHOT.jar' // Standard Maven default
        // Define the GitHub repository URL
        GIT_URL = 'https://github.com/Aditi012345/myjavaapp' 
        GIT_BRANCH = 'main'
    }

    stages {
        stage('1. Checkout Code') {
            steps {
                echo "Cloning source code from ${env.GIT_URL}"
                // Use default SCM configuration which typically uses Git installed on the agent
                git branch: env.GIT_BRANCH, url: env.GIT_URL
            }
        }

        stage('2. Build and Package (Maven)') {
            steps {
                echo 'Cleaning and building Java application with Maven...'
                // Assumes 'mvn' command is available on the Windows agent's PATH.
                bat 'mvn clean package' 
            }
        }

        stage('3. Run Tests') {
            // Maven runs tests during the 'package' goal, but you can add specific reporting here.
            steps {
                echo 'Tests executed as part of the package phase. Skipping separate test execution stage.'
            }
        }

        stage('4. Execute Application') {
            steps {
                echo "Running the compiled application: target/${env.ARTIFACT_NAME}"
                
                // FIX: Replaced 'timeout /t 5' with a more stable 'ping' loop
                // The ping loop provides a reliable 5-second delay on Windows without input redirection issues.
                bat """
                    echo Starting Java application...
                    start /b java -jar target/${env.ARTIFACT_NAME}
                    ping 127.0.0.1 -n 6 > nul
                    echo Application started (check console output for the output).
                """
            }
        }
    }

    post {
        success {
            echo "SUCCESS: Java application pipeline completed successfully!"
        }
        failure {
            echo "FAILURE: Java build or test failed. Check the Maven output."
        }
        always {
            // Cleanup step
            bat 'echo Pipeline execution finished.'
        }
    }
}
