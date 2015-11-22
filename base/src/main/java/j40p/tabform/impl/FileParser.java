package j40p.tabform.impl;

import j40p.base.parser.BFHandler;
import j40p.tabform.iface.DocParser;
import j40p.tabform.iface.TabFormHdl;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class FileParser implements DocParser<File> {

	private final static byte[] obom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };

	public void parse(File f, TabFormHdl outerhdl) {

		if (!f.exists())
			throw new RuntimeException("file does not exist.");

		FileChannel fch = null;
		try {
			fch = FileChannel.open(f.toPath(), StandardOpenOption.READ);
		} catch (IOException e) {
			throw new RuntimeException(e);

		}

		// byte[] bomc = new byte[3];
		ByteBuffer inbuffer = ByteBuffer.allocate(3);
		// ByteBuffer bf3 = ByteBuffer.wrap(bomc);

		while (inbuffer.hasRemaining()) {
			try {
				switch (fch.read(inbuffer)) {
					case -1:
						break;
					case 0:
					default:
						continue;
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		byte[] bomc = Arrays.copyOf(inbuffer.array(), 3);
		if (!Arrays.equals(bomc, this.obom)) {
			inbuffer.limit(inbuffer.position());
			inbuffer.position(0);
		} else {
			inbuffer = null;
		}

		EB eventbuilder = new EB();
		TFProcessor tabformscanner = new TFProcessor();
		BFHandler bufferhandler = new BFHandler();
		eventbuilder.setTabFormHdl(outerhdl);
		tabformscanner.setEBuilder(eventbuilder);
		bufferhandler.setScanner(tabformscanner);

		long x = System.currentTimeMillis();
		int lastread = 0;
		while ((inbuffer = bufferhandler.intaking(inbuffer, lastread)) != null) {

			try {
				 lastread = fch.read(inbuffer);
					 
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			// System.out.println("lastred!!!!!!!!!!!!"+lastread);

		}

		// hdlChannel(fch, stm, false, bfc);
		try {
			fch.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println("time consum: "+(System.currentTimeMillis() - x));
	}

}
