package classes
import gdsl.jenkins

pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    stages {
        stage('Checkout') {
            git branch: 'master', credentialsId: 'fe0b6504-4096-4ba9-a37c-d904d68b4785', url: 'http://localhost:7990/scm/pla/plasma.git'
        }
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git 'http://localhost:7990/scm/pla/plasma.git'

                // Run Maven on a Unix agent.
                sh "mvn -Dmaven.test.failure.ignore=true clean package"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}
