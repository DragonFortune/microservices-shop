pluginManagement{
    repositories{
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "microservices-shop"
include("order-service")
include("payment-service")
