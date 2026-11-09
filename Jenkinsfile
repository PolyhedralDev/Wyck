pipeline {
    agent any

    tools {
        jdk "Temurin Java 21"
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                scmSkip(deleteBuild: true)
            }
        }

        stage('Setup Gradle') {
            steps {
                sh 'chmod +x gradlew'
            }
        }

        stage('Build') {
            steps {
                withGradle {
                    sh './gradlew :bundle:build :paper:build -Psnapshot -x test'
                }
            }

            post {
                success {
                    archiveArtifacts artifacts: 'paper/build/libs/*.jar', fingerprint: true, onlyIfSuccessful: true
                }
            }
        }

        stage('Deploy to snapshots repository') {
            when {
                not { changeRequest() }
            }

            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'solo-studios-maven',
                        passwordVariable: 'ORG_GRADLE_PROJECT_SoloStudiosSnapshotsPassword',
                        usernameVariable: 'ORG_GRADLE_PROJECT_SoloStudiosSnapshotsUsername'
                    )
                ]) {
                    withGradle {
                        sh './gradlew :bundle:publishAllPublicationsToSoloStudiosSnapshotsRepository -Psnapshot'
                    }
                }
            }
        }
    }

    post {
        always {
            withCredentials([string(credentialsId: 'polydev-discord-webhook-url', variable: 'DISCORD_WEBHOOK_URL')]) {
                discordSend(
                    title: env.JOB_NAME + ' ' + env.BUILD_DISPLAY_NAME,
                    showChangeset: true,
                    enableArtifactsList: true,
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    customAvatarUrl: 'https://github.com/PolyhedralDev.png',
                    customUsername: 'Solo Studios Jenkins',
                    webhookURL: DISCORD_WEBHOOK_URL,
                )
            }

            cleanWs()
        }
    }
}
