node{
   stage('SCM Checkout'){
     git 'https://github.com/crsathish3103/my-app.git'
    }
   stage('Compile-Package'){

      def mvnHome =  tool name: 'maven3', type: 'maven'   
      sh "${mvnHome}/bin/mvn clean package"
	  sh 'mv target/myweb*.war target/newapp.war'
    }
    stage('SonarQube Analysis') {
	        def mvnHome =  tool name: 'maven3', type: 'maven'
	        withSonarQubeEnv('sonar') { 
	          sh "${mvnHome}/bin/mvn sonar:sonar"
	        }
	    }
    stage('Build Docker Imager'){
    sh 'docker build -t crsathish3103/myweb:0.0.2 .'
   }
   stage('Docker Image Push'){
   withCredentials([string(credentialsId: 'dockerPass', variable: 'dockerPassword')]) {
   sh "docker login -u crsathish3103 -p ${dockerPassword}"
    }
   sh 'docker push crsathish3103/myweb:0.0.2'
   }
   
   stage('Nexus Image Push'){
   withCredentials([string(credentialsId: 'nexuspass', variable: 'nexuspassword')]) {
   sh "docker login -u admin -p ${nexuspassword} 13.232.215.245:8085"
   sh "docker tag crsathish3103/myweb:0.0.2 13.232.215.245:8085/sathi:1.0.0"
   }
   sh 'docker push 13.232.215.245:8085/sathi:1.0.0'
   }
   
   stage('Remove Previous Container'){
	try{
		sh 'docker rm -f tomcattest'
	}catch(error){
		//  do nothing if there is an exception
	}
	
   stage('Docker deployment'){
   sh 'docker run -d -p 8092:8080 --name tomcattest crsathish3103/myweb:0.0.2' 
   }
   }
}
