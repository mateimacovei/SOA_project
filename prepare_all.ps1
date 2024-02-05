Write-Output $env:JAVA_HOME

Set-Location ./application
mvn clean package

Set-Location ./../notification
mvn clean package

Set-Location ./../users
mvn clean package

Set-Location ./..