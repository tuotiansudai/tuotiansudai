package com.esoft.archer.generate.log;

import java.util.ArrayList;
import java.util.List;

public class GenerateLog {
	private List<String> errorLogs;

	public GenerateLog() {
		this.errorLogs = new ArrayList<String>();
	}

	public void addErrorLog(String error) {
		this.errorLogs.add(error);
	}

	public List<String> getErrorLogs() {
		return this.errorLogs;
	}

	public boolean isEmpty() {
		if (this.errorLogs.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
