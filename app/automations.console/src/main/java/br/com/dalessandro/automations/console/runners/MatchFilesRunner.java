package br.com.dalessandro.automations.console.runners;

import java.io.IOException;

import br.com.dalessandro.automations.tools.MatchFiles;
import br.com.dalessandro.automations.tools.impl.MatchFilesImpl;

public class MatchFilesRunner {

	private static final MatchFiles matchFiles = new MatchFilesImpl();

	private MatchFilesRunner() {
	}

	public static void seachContents(final String source, final Integer columnIndex, final String comparingFiles)
			throws IOException {

		matchFiles.seachContentsInFiles(source, columnIndex, comparingFiles);

	}

}
