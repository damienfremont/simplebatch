package com.dfremont.simplebatch.core;

import java.util.ArrayList;
import java.util.List;

public class Step<READITEM, WRITEITEM> implements Executable {
	static final String MSG_CONSTPRIVATE = "Use public constructors instead";
	static final String MSG_CONST = "Reader and Processor are Mandatory";
	String name;
	ItemReader<READITEM> reader;
	ItemProcessor<READITEM, WRITEITEM> processor;
	ItemWriter<WRITEITEM> writer;

	// TODO properties?
	// TODO int confCommitInterval;
	// TODO int confSkipLimit;
	// TODO int skipError;

	public Step(ItemReader<READITEM> newReader,
			ItemProcessor<READITEM, WRITEITEM> newProcessor,
			ItemWriter<WRITEITEM> newWriter) {
		if (!(newReader != null && newWriter != null))
			throw new IllegalArgumentException(MSG_CONST);
		this.reader = newReader;
		this.writer = newWriter;
		this.processor = newProcessor;
	}

	public void execute() throws Exception {
		execute(0);
		// TODO execute(1); on chunck
	}

	@SuppressWarnings("unchecked")
	void execute(int commitInterval) throws Exception {
		READITEM item = reader.read();
		// TODO batch by commit value
		List<WRITEITEM> chunck = new ArrayList<WRITEITEM>(); // TODO chunck
																// extends List?
		int index = 0;
		while (item != null
				&& (index < (commitInterval - 1) | commitInterval == 0)) {
			if (processor != null) {
				chunck.add(processor.process(item));
			} else {
				chunck.add((WRITEITEM) item);
			}
			index++;
			item = reader.read();
		}
		writer.write(chunck);
	}

	public String getExecution() {
		return String
				.format("reader=[%s], processor [%s], writer=[%s]", //
						((reader instanceof Executable) ? ((Executable) reader)
								.getExecution() : "unknowed"), //
						((processor != null && processor instanceof Executable) ? ((Executable) processor)
								.getExecution() : "unknowed"), //
						((writer instanceof Executable) ? ((Executable) writer)
								.getExecution() : "unknowed"));
	}
}
