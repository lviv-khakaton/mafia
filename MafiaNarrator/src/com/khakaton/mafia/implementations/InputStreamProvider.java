package com.khakaton.mafia.implementations;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamProvider {

	private InputStream inputStream;
	
	public InputStreamProvider(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public int choosedPerson() throws IOException {
		//TODO need to be done in another thread.
		int choosed = inputStream.read();
		return choosed;
	}
	
}
