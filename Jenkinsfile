pipeline {
    agent any
        triggers {
        pollSCM('* * * * *')
    }

    tools {
        maven "MAVEN_HOME"
    }
  stages {
      		   stage('Build') {
            steps {
            
                git branch: 'second', url: 'https://github.com/vishnuvishal-11/Queue_jenkins'
                 bat 'mvn clean install -Dmaven.test.skip=true '
            }
        }
        
        stage('Test') {
            steps {
                 bat 'mvn test'
            }
            }
          stage('deploy') {
            steps {  
                echo 'docker image is building'
                 bat 'docker logout'
                bat 'docker build -f Dockerfile -t spring11 .'
                 echo 'docker image is build sucessfully'
             }
        }
}

        post {
  success {
       bat 'docker logout'
       bat 'docker tag spring10  vishnu11docker/firstdockerrepo:spring11'
        bat 'docker login -u vishnu11docker -p dckr_pat_-L7y2OMAWIx_ejVjTyo6XF0X5sk'
        bat 'docker push vishnu11docker/firstdockerrepo:spring11'
         bat 'docker logout'
          echo 'docker image is pushed to remote repo sucessfully'
    
  }
}
}
