package com.dfremont.simplebatch.core;

import java.util.List;

// TODO compatibility with springbatch
// TODO jobparametersvalidator
public class BatchProcess {
	String name;
	List<Step<?, ?>> steps;
	final BatchExecutionReport report = new BatchExecutionReport();

	public BatchProcess(List<Step<?, ?>> steps) {
		this.steps = steps;
	}

	public BatchProcess(List<Step<?, ?>> steps, String name) {
		this(steps);
		this.name = name;
	}

	public final void execute() throws Exception {
		report.status = BatchExecutionReport.STARTED;
		for (Step<?, ?> currentStep : steps) {
			currentStep.execute();
			report.execution += currentStep.getExecution();
		}
		report.status = BatchExecutionReport.TERMINATED;
	}

	public BatchExecutionReport getReport() {
		return report;
	}
}
