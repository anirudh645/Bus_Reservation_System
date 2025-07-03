pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'anirudh645', url: 'https://github.com/anirudh645/Bus_Reservation_System'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcat', path: '', url: 'http://localhost:8080')], contextPath: '/EasyBus', war: '/target/Bus_Ticketing_System-0.0.1-SNAPSHOT.war'
            }
        }
    }
}
