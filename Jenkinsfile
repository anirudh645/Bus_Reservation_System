<<<<<<< HEAD
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
                deploy adapters: [tomcat8(credentialsId: 'tomcat', path: '', url: 'http://localhost:8080')], contextPath: '/EasyBus', war: 'target/Bus_Ticketin_System-0.0.1-SNAPSHOT.war'
            }
        }
    }
=======
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
                deploy adapters: [tomcat8(credentialsId: 'tomcat', path: '', url: 'http://localhost:8080')], contextPath: '/EasyBus', war: 'target/Bus_Ticketin_System-0.0.1-SNAPSHOT.war'
            }
        }
    }
>>>>>>> f060c9a3e93416fae95530a84014f99340188871
}