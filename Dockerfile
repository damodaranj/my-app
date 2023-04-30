apiVersion: v1
kind: Pod
metadata:
  name: nginx
  labels:
    app:  myapp
spec:
  containers:
  - name: nginx
    image: 477099163803.dkr.ecr.ap-south-1.amazonaws.com/jenkins_project
    ports:
    - containerPort: 8080



kind: Service
apiVersion: v1
metadata:
  name:  mynodeport
spec:
  selector:
    app:  myapp
  type:  NodePort
  ports:
  - name:  httpd
    port:  80
    targetPort:  8080
    protocol: TCP
