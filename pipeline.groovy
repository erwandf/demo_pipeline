pipeline {
    agent any

    stages {
        stage('checkout') {
            steps {
                script { env.stage = 'Checkout' }
                checkout([$class: 'GitSCM', branches: [[name: 'main']],
                    doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
                    userRemoteConfigs: [[url: 'git@github.com:erwandf/demo_phpunit_jenkins.git']]]
                )
            }
        }
        stage('composer') {
            steps {
                sh './install_composer.sh'
                sh 'php composer.phar install'
            }
        }
        stage('tests') {
            steps {
                sh './vendor/bin/phpunit'
            }
        }
    }
    
    post {
        always {
            step([$class: 'JUnitResultArchiver', testResults: 'junit.xml', healthScaleFactor: 1.0])
            echo 'Fin'
        }
    }
}

