package br.com.dalessandro.automations.tools.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;

import br.com.dalessandro.automations.tools.MatchFiles;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MatchFilesImpl implements MatchFiles {

	@Override
	public void seachContentsInFiles(final String source, final Integer columnIndex, final String comparingFiles)
			throws IOException {

		final Path inputPath = Path.of(source, StringUtils.EMPTY);
		log.info("Source path: ".concat(inputPath.toString()));
		if (Files.exists(inputPath, LinkOption.NOFOLLOW_LINKS)) {
			final File outputPath = getOutputPath();

			log.info("Reading file ".concat(source));
			final String sourceData = FileUtils.readFileToString(new File(source), Charset.defaultCharset());

			final List<File> files = filterComparingFiles(comparingFiles);
			log.info(String.valueOf(files.size()).concat(" found"));

			final List<String> valuesFoundList = new ArrayList<>();
			final List<String> valuesNotFoundList = new ArrayList<>();

			for (final File file : files) {
				log.info("Reading file ".concat(file.toString()));
				final List<String> fileLines = FileUtils.readLines(file, Charset.defaultCharset());
				for (final String line : fileLines) {
					final String valueToCheck = line.split(",")[columnIndex];
					if (sourceData.contains(valueToCheck) && !valuesFoundList.contains(valueToCheck)) {
						valuesFoundList.add(valueToCheck);
					} else if (!sourceData.contains(valueToCheck)) {
						valuesNotFoundList.add(valueToCheck);
					}
				}
			}
			log.info("Search finished. Saving Files.");

			Collections.sort(valuesFoundList);
			final File foundValues = new File(outputPath, "foundValues.csv");
			FileUtils.writeLines(foundValues, valuesFoundList);
			log.info("File ".concat(foundValues.toString()).concat(" ready"));

			Collections.sort(valuesNotFoundList);
			final File notFoundValues = new File(outputPath, "notFoundValues.csv");
			FileUtils.writeLines(notFoundValues, valuesNotFoundList);
			log.info("File ".concat(notFoundValues.toString()).concat(" ready"));
		}
	}

	private File getOutputPath() {

		final File outputPath = new File("./output");
		log.info("Checking If ".concat(outputPath.toString()).concat(" Path Exists"));
		if (!outputPath.exists()) {
			log.info("Creating Output Path");
			outputPath.mkdir();
			log.info("Output Path Created");
		}

		return outputPath;
	}

	private List<File> filterComparingFiles(String comparingFiles) {

		log.info("Filtering matching file(s) ".concat(comparingFiles));

		if (!comparingFiles.contains("/")) {
			comparingFiles = "./".concat(comparingFiles);
		}

		final FileFilter fileFilter = WildcardFileFilter.builder()
				.setWildcards(comparingFiles.substring(comparingFiles.lastIndexOf("/") + 1)).get();
		final File comparingFilesPath = new File(comparingFiles.substring(0, comparingFiles.lastIndexOf("/")));
		final File[] filesList = comparingFilesPath.listFiles(fileFilter);

		if (filesList.length == 0) {
			log.info("No matching ".concat(comparingFiles).concat(" files found"));
			System.exit(1);
		}

		return Arrays.asList(filesList);
	}
}
