package org.example.repositories;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileRepository {
	private List<File> files;

	public FileRepository() {
		files = new ArrayList<>();
	}

	public Stream<File> all() {
		return files.stream();
	}

	public FileRepository addAll(File[] files) {
		this.files.addAll(List.of(files));
		return this;
	}
}
