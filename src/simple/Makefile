compile:
	javac Example.java
	javac BadObj.java

run:
	java Example $(cookie)

bad:
	java BadObj

decode:
	base64 --decode cookie.txt > cookie.ser

encode:
	base64 cookie.ser > modified_cookie.txt