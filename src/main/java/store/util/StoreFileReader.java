package store.util;

import static store.common.ErrorMessage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class StoreFileReader {

	private static final int HEADER_INDEX = 1;
	private static final String DELIMITER = ",";

	private StoreFileReader() {
	}

	public static List<String[]> readWithoutHeader(String filePath) {
		try (Stream<String> readLines = Files.lines(Paths.get(filePath))) {
			List<String> readLinesWithoutHeader = readLines
				.skip(HEADER_INDEX)
				.toList();
			return processInfos(readLinesWithoutHeader);

		} catch (IOException e) {
			throw new IllegalStateException(CAN_NOT_READ_FILE.getMessage());
		}
	}

	private static List<String[]> processInfos(List<String> readLines) {
		return readLines.stream()
			.map(readLine -> readLine.split(DELIMITER))
			.toList();
	}

}
