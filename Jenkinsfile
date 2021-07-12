node{
   stage('SCM Checkout'){
     git 'https://github.com/damodaranj/my-app.git'
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
   sh 'docker build -t karkeess/myweb-0.0.2 .'
   }
   stage('Docker Image Push'){
   withCredentials([string(credentialsId: 'docker', variable: 'docker')]) {
   sh "docker login -u karkeess -p ${docker}"
    }
   sh 'docker push karkeess/myweb-0.0.2'
   }
   stage('Nexus Image Push'){
   sh "docker login -u admin -p admin123 13.233.255.221:8083"
   sh "docker tag saidamo/myweb:0.0.2 13.233.255.221:8083/damo:1.0.0"
   sh 'docker push 65.0.181.193:8083/damo:1.0.0'
   }
   stage('Remove Previous Container'){
	try{
		sh 'docker rm -f tomcattest'
	}catch(error){
		//  do nothing if there is an exception
	}
   stage('Docker deployment'){
   sh 'docker run -d -p 8090:8080 --name tomcattest karkeess/myweb-0.0.2' 
   }
}
}
