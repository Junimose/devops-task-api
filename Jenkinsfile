pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK21'
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deliver') {
            steps {
                sh './jenkins/scripts/deliver.sh'
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }
    }
}
