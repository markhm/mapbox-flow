pipeline {
  agent any

  options {
    // only keep 25 builds to prevent disk usage from growing out of control
    buildDiscarder( logRotator(
                    artifactDaysToKeepStr: '',
                    artifactNumToKeepStr: '',
                    daysToKeepStr: '',
                    numToKeepStr: '25'
            ))
  }

  stages {

    // Checkout here seems double, but cannot be avoided. Technically, the first checkout is to only get the JenkinsFile,
    // second checkout is for the full code. However, Jenkins cannot checkout only one file.
    // https://stackoverflow.com/questions/39452030/jenkins-pipeline-is-it-possible-to-avoid-multiple-checkout
    stage('Checkout') {
      steps {
        git(url: 'https://github.com/markhm/mapbox-flow/', branch: 'master', credentialsId: 'e317be5f-9a23-4365-8b05-650709074449')
      }
    }

    stage('Clean') {
      steps {
        bat 'clean.bat'
      }
    }

    stage('Build and test') {
      steps {
        bat 'install.bat'
      }
    }

  }
}

