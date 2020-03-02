pipeline {

    agent {
        label 'master'
    }

    tools {
        maven 'apache-maven-3.6.0'
        jdk 'jdk8'
    }

    stages {

        stage('spring') {
            steps {
                dir('parkhaus-spring/parkhaus-manager/') {
                    sh "mvn package"
                    sh "docker build -t registry-cloud.k8s.intern.viadee.de/dip/manager-spring ."
                    sh "docker push registry-cloud.k8s.intern.viadee.de/dip/manager-spring"
                    sh "kubectl apply -f deployment.yml --namespace playground-dev"
                }
            }

        }

        stage('quarkus') {
            steps {
                dir('parkhaus-quarkus/parkhaus-manager/') {
                    sh "mvn package"
                    sh "docker build -t registry-cloud.k8s.intern.viadee.de/dip/manager-quarkus ."
                    sh "docker push registry-cloud.k8s.intern.viadee.de/dip/manager-quarkus"
                    sh "kubectl apply -f deployment.yml --namespace playground-dev"
                }
            }

        }

        stage('micronaut') {
            steps {
                dir('parkhaus-micronaut/parkhaus-manager/') {
                    sh "mvn package"
                    sh "docker build -t registry-cloud.k8s.intern.viadee.de/dip/manager-micronaut ."
                    sh "docker push registry-cloud.k8s.intern.viadee.de/dip/manager-micronaut"
                    sh "kubectl apply -f deployment.yml --namespace playground-dev"
                }
            }

        }
    }

        post {
            always {
                deleteDir()
            }
        }

    }
