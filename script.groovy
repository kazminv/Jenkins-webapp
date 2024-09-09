def build() {
    echo 'building the application...'
}

def testApp() {
    echo 'testing the application...'
}

def deployApp() {
    echo 'deploying the application...'
}
def buildSendDocker() {
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        echo 'Command from the script.groovy: '
        sh 'docker build -t kazminv/my-dockerhub-repository:jma-2.0 .'
        // Login to Docker Hub and push the image
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push kazminv/my-dockerhub-repository:jma-2.0'
    }
}

return this
