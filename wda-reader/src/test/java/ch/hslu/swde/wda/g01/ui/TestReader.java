package ch.hslu.swde.wda.g01.ui;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ch.hslu.swde.wda.g01.domain.Ort;
import ch.hslu.swde.wda.g01.reader.rws.proxy.ReaderRWSProxy;

public class TestReader {

	//@Test
	@Disabled
	void TestHoleOrteSize() {
		List<Ort> orte = new ReaderRWSProxy().holeOrte();

		ReaderRWSProxy reader = new ReaderRWSProxy();

		assertEquals(orte.size(), reader.holeOrte().size());

	}

	//@Test
	@Disabled
	void TestHoleOrteNotNull() {
		ReaderRWSProxy reader = new ReaderRWSProxy();
		assertNotNull(reader.holeOrte());
	}

	//@Test
	@Disabled
	void TestHoleWetterdatenNotNull() {
		ReaderRWSProxy reader = new ReaderRWSProxy();

		assertNotNull(reader.holeWetterdaten(2023));
	}

}
