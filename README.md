"# test_stub" 


docker build -t test_stub .
docker run -p 8080:8080 -p 8778:8778 test_stub
