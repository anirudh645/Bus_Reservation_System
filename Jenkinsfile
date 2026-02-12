pipeline {
    agent any

    tools {
        maven 'Maven_3_9_6'
    }

    environment {
        DOCKER_IMAGE = "bus-reservation:${BUILD_NUMBER}"
        GIT_REPO = "https://github.com/anirudh645/Bus_Reservation_System"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'anirudh645', url: "${GIT_REPO}"
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_IMAGE} .'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    sh '''
                        docker-compose down || true
                        docker-compose up -d
                        sleep 30
                    '''
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    sh '''
                        for i in {1..10}; do
                            if curl -f http://localhost:8081 > /dev/null 2>&1; then
                                echo "Application is healthy"
                                exit 0
                            fi
                            echo "Waiting for application to start... Attempt $i"
                            sleep 5
                        done
                        exit 1
                    '''
                }
            }
        }
    }

    post {
        always {
            sh 'docker system prune -f'
        }
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed! Rolling back...'
            sh 'docker-compose down'
        }
        
    }
}
