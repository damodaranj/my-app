FROM tomcat:8
# Take the war and copy to webapps of tomcat
# git
COPY target/newapp.war /usr/local/tomcat/webapps/
