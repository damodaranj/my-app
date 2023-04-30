pipeline {
    agent any

    stages {
        stage('SCM_Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'git_repo_pass', url: 'https://github.com/Venkateshd279/my-project.git']])
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
              script {
             withSonarQubeEnv(credentialsId: 'SonarQube_token') {
                 
              sh 'mvn clean verify sonar:sonar'
             }
            }
            
            }
        }
        
        stage("Quality Gate") {
            steps {
                sleep(60)
              timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate abortPipeline: true, credentialsId: 'SonarQube_token'
              }
            }
            post {
        
        failure {
            echo 'sending email notification from jenkins'
            
                   step([$class: 'Mailer',
                   notifyEveryUnstableBuild: true,
                   recipients: emailextrecipients([[$class: 'CulpritsRecipientProvider'],
                                      [$class: 'RequesterRecipientProvider']])])

            
               }
            }
          }
          
        stage('Maven Compile') {
            steps {
                sh 'whoami'
                sh script: 'mvn clean install'
                sh 'mv target/myweb*.war target/newapp.war'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    
                    sh 'cd /var/lib/jenkins/workspace/Onlineshopping'
                    def dockerImage = docker.build("venkateshdhanap/myweb:0.0.2", "--file Dockerfile .")
                }
            }
        }
        
        
        stage('Push Image to ECR') {
            steps {
                script {
                    docker.withRegistry(
                        'https://477099163803.dkr.ecr.ap-south-1.amazonaws.com',
                        'ecr:ap-south-1:my.aws.credentials') {
                            def myImage = docker.build('jenkins_project')
                            myImage.push('latest')
                        }
                    }
                }
        }
        
        stage('Approval - Deploy EKS'){
            steps {
                
                input 'Approve for EKS Deploy'
            }
        }
        
        stage('EKS Deploy') {
            steps {
                
                echo 'Deploying on EKS'
                withKubeCredentials(kubectlCredentials: [[caCertificate: '', clusterName: '', contextName: '', credentialsId: 'k8s', namespace: '', serverUrl: '']]) {
               
                 sh 'kubectl apply -f /var/lib/jenkins/mainservice.yaml'
             }
            }
        }
    }
}
