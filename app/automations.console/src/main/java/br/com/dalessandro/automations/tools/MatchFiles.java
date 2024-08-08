package br.com.dalessandro.automations.tools;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public interface MatchFiles {

	void seachContentsInFiles(String source, Integer columnIndex, String comparingFiles) throws IOException;
}