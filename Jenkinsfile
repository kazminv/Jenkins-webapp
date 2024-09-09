#!/usr/bin/env groovy

library identifier: 'my-shared-library@main', retriever: modernSCM(
    [$class: 'GitSCMSource',
    remote: 'https://gitlab.com/devops6503315/my-shared-library.git',
    credentialsId: 'f772b3ad-9828-4300-85fa-e65d2a4c0996'
    ]
)

pipeline {
    agent any

    tools {
        maven 'my-maven-tool'
    }
    environment {
  // IMAGE_VERSION = "1.0.0"
        IMAGE_NAME =  'kazminv/my-dockerhub-repository'
        SERVER_IP_ADDRESS = '54.92.206.240'
        USER_NAME = 'ubuntu'
    }

    stages {
        stage("Increment Version") {
        when {
              expression {
              env.GIT_BRANCH == 'pet-cicd'
              }
        }
            steps {
                script {
                    echo 'Incrementing app version'
                    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit'
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_VERSION = "${version}-${BUILD_NUMBER}"
                }
            }
        }

        stage("Build App") {
            when {
                  expression {
                  env.GIT_BRANCH == 'pet-cicd'
                  }
                        }
            steps {
                script {
                    sh 'mvn clean package'
                    writeMessage('Testname')
                }
            }
        }

        stage("Build and Deploy Docker image") {
            when {
                  expression {
                  env.GIT_BRANCH == 'pet-cicd'
                  }
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${env.IMAGE_NAME}:${env.IMAGE_VERSION} ."
                        sh "echo \$PASS | docker login -u \$USER --password-stdin"
                        sh "docker push ${env.IMAGE_NAME}:${env.IMAGE_VERSION}"
                    }
                }
            }
        }

        stage("Deploy to the EC2-server") {
            when {
                expression {
                env.GIT_BRANCH == 'pet-cicd2'
                }
            }
            steps {
                script {
                   def shellCmd = "bash ./server-commands.sh ${env.IMAGE_NAME} ${env.IMAGE_VERSION}"
                   sshagent(['EC2-server-key']) {
                    sh "scp server-commands.sh ${env.USER_NAME}@${env.SERVER_IP_ADDRESS}:/home/${env.USER_NAME}"
                    sh "scp docker-compose.yaml ${env.USER_NAME}@${env.SERVER_IP_ADDRESS}:/home/${env.USER_NAME}"
                    sh "ssh -o StrictHostKeyChecking=no ${env.USER_NAME}@${env.SERVER_IP_ADDRESS} ${shellCmd}"
                   }
                }
            }
        }

        stage("Commit Version Update") {
            when {
                expression {
                    env.GIT_BRANCH == 'pet-cicd'
                }
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'f772b3ad-9828-4300-85fa-e65d2a4c0996', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        def gitBranch = env.GIT_BRANCH
                        echo "Git branch name is: $gitBranch"
                        // Set Git configurations for this specific repository
                        sh "git config user.email 'jenkins@example.com'"
                        sh "git config user.name 'Jenkins'"
                        // Set the remote URL with credentials
                        sh "git remote set-url origin https://${USER}:${PASS}@gitlab.com/devops6503315/maven-build-project.git"
                        // Stage changes
                        sh 'git add .'
                        // Commit changes with a message
                        sh 'git commit -m "ci: version bump"'
                        // Push changes to the Git repository branch
                        sh "git push origin HEAD:${gitBranch}"
                        // Display user email and name
                        echo "User email: ${env.GIT_COMMITTER_EMAIL}"
                        echo "User name: ${env.GIT_COMMITTER_NAME}"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed.'
        }
        success {
            echo 'Pipeline execution succeeded!'
        }
        failure {
            echo 'Pipeline execution failed!'
        }
        unstable {
            echo 'Pipeline execution is unstable.'
        }
    }
}
