package com.khakaton.mafia.implementations;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamProvider {

	private InputStream inputStream;
	
	public InputStreamProvider(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public int chosenPerson() throws IOException {
		//TODO need to be done in another thread.
		int chosen = inputStream.read();
		return chosen;
	}
	
}
