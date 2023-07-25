package cn.hgy.readfundus;

import cn.hgy.readfundus.common.ResourcePath;
import cn.hgy.readfundus.dto.OutcomeDTO;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class ReadFundusApplicationTests {
	@Resource
	private ResourcePath resourcePath;

	@Test
	void testCsvReader() {
		String fileName = "500人理化数据.csv";
		String path = Paths.get(resourcePath.getInfo(), fileName).toString();
		File infoFile = new File(path);
		if (infoFile.exists()) {
			CsvReader reader = CsvUtil.getReader();
			reader.setContainsHeader(true);
			CsvData data = reader.read(infoFile, StandardCharsets.UTF_8);
			String s = data.getRow(0).get(2);
			System.out.println(s);
			System.out.println(LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		}
	}

	@Test
	public void testCsvWriter(){
		String outcomeFileName = "outcome_test.csv";
		String path = Paths.get(resourcePath.getOutcome(), outcomeFileName).toString();
		File file = new File(path);
		if (file.getParentFile().exists() || file.getParentFile().mkdirs()){
			CsvWriter writer = CsvUtil.getWriter(file, CharsetUtil.CHARSET_UTF_8);
			List<OutcomeDTO> l = new ArrayList<>();
			l.add(new OutcomeDTO("1", "2", new BigDecimal("12"), "4", new BigDecimal("12.2")));
			l.add(new OutcomeDTO("1", "2", new BigDecimal("12"), "4", new BigDecimal("12")));
			writer.writeBeans(l);
			writer.close();
		}
	}
}
