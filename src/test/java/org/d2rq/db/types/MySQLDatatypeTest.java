package org.d2rq.db.types;

import org.d2rq.D2RQTestUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO: Put MySQL connection details into a properties file
 * 
 * @author Richard Cyganiak (richard@cyganiak.de)
 */
public class MySQLDatatypeTest extends DatatypeTestBase {
	
	@Before
	public void setUp() throws Exception {
		initDB("jdbc:mysql:///d2rq_test", "com.mysql.jdbc.Driver", "root", null, 
				D2RQTestUtil.getResourceURL("mysql/mysql_datatypes.sql"));
	}

	@Test
	public void testSerial() {
		createMapping("SERIAL");
		assertMappedType("xsd:unsignedLong");
		assertValues(new String[]{"1", "2", "18446744073709551615"});
	}

	@Test
	public void testBit_4() {
		createMapping("BIT_4");
		assertMappedType("xsd:string");
		assertValues(new String[] {"0", "1", "1000", "1111"});
	}

	@Test
	public void testBit() {
		createMapping("BIT");
		assertMappedType("xsd:string");
		assertValues(new String[] {"0", "1"});
	}

	@Test
	public void testTinyInt() {
		createMapping("TINYINT");
		assertMappedType("xsd:byte");
		assertValues(new String[]{"0", "1", "-128", "127"});
	}
	
	@Test
	public void testTinyInt1() {
		createMapping("TINYINT_1");
		assertMappedType("xsd:boolean");
		assertValues(new String[]{"false", "true", "true"});
	}
	
	@Test
	public void testTinyIntUnsigned() {
		createMapping("TINYINT_UNSIGNED");
		assertMappedType("xsd:unsignedByte");
		assertValues(new String[]{"0", "1", "255"});
	}
	
	@Test
	public void testSmallInt() {
		createMapping("SMALLINT");
		assertMappedType("xsd:short");
		assertValues(new String[]{"0", "1", "-32768", "32767"});
	}
	
	@Test
	public void testSmallIntUnsigned() {
		createMapping("SMALLINT_UNSIGNED");
		assertMappedType("xsd:unsignedShort");
		assertValues(new String[]{"0", "1", "65535"});
	}
	
	@Test
	public void testMediumInt() {
		createMapping("MEDIUMINT");
		assertMappedType("xsd:int");
		assertValues(new String[]{"0", "1", "-8388608", "8388607"});
	}
	
	@Test
	public void testMediumIntUnsigned() {
		createMapping("MEDIUMINT_UNSIGNED");
		assertMappedType("xsd:unsignedInt");
		assertValues(new String[]{"0", "1", "16777215"});
	}
	
	@Test
	public void testInteger() {
		createMapping("INTEGER");
		assertMappedType("xsd:int");
		assertValues(new String[]{"0", "1", "-2147483648", "2147483647"});
	}
	
	@Test
	public void testIntegerUnsigned() {
		createMapping("INTEGER_UNSIGNED");
		assertMappedType("xsd:unsignedInt");
		assertValues(new String[]{"0", "1", "4294967295"});
	}
	
	@Test
	public void testInt() {
		createMapping("INT");
		assertMappedType("xsd:int");
		assertValues(new String[]{"0", "1", "-2147483648", "2147483647"});
	}
	
	@Test
	public void testIntUnsigned() {
		createMapping("INT_UNSIGNED");
		assertMappedType("xsd:unsignedInt");
		assertValues(new String[]{"0", "1", "4294967295"});
	}
	
	@Test
	public void testBigInt() {
		createMapping("BIGINT");
		assertMappedType("xsd:long");
		assertValues(new String[]{"0", "1", "-9223372036854775808", "9223372036854775807"});
	}

	@Test
	public void testBigIntUnsigned() {
		createMapping("BIGINT_UNSIGNED");
		assertMappedType("xsd:unsignedLong");
		assertValues(new String[]{"0", "1", "18446744073709551615"});
	}

	private final static String[] DECIMAL_VALUE = {"0", "1", "100000000", "-100000000"};
	@Test
	public void testDecimal() {
		createMapping("DECIMAL");
		assertMappedType("xsd:decimal");
		assertValues(DECIMAL_VALUE);
	}
	
	@Test
	public void testDecimal_4_2() {
		createMapping("DECIMAL_4_2");
		assertMappedType("xsd:decimal");
		assertValues(new String[]{"0", "1", "4.95", "99.99", "-99.99"});
	}

	@Test
	public void testDec() {
		createMapping("DEC");
		assertMappedType("xsd:decimal");
		assertValues(DECIMAL_VALUE);
	}
	
	@Test
	public void testDec_4_2() {
		createMapping("DEC_4_2");
		assertMappedType("xsd:decimal");
		assertValues(new String[]{"0", "1", "4.95", "99.99", "-99.99"});
	}

	private final static String[] FLOAT_VALUES = 
			{"0.0E0", "1.0E0", "-1.0E0", 
			"-3.0E38", "-1.0E-38", 
			"1.0E-38", "3.0E38"}; 
	@Test
	public void testFloat() {
		createMapping("FLOAT");
		assertMappedType("xsd:double");
		// TODO: Fuzzy match to search for floating-point values
		assertValues(FLOAT_VALUES, false);
	}
	
	private final static String[] DOUBLE_VALUES = 
			{"0.0E0", "1.0E0", "-1.0E0", 
			"-1.0E308", "-2.0E-308", 
			"2.0E-308", "1.0E308"}; 
	@Test
	public void testDouble() {
		createMapping("DOUBLE");
		assertMappedType("xsd:double");
		// TODO: Fuzzy match to search for floating-point values
		assertValues(DOUBLE_VALUES, false);
	}
	
	@Test
	public void testReal() {
		createMapping("REAL");
		assertMappedType("xsd:double");
		// TODO: Fuzzy match to search for floating-point values
		assertValues(DOUBLE_VALUES, false);
	}
	
	@Test
	public void testDoublePrecision() {
		createMapping("DOUBLE_PRECISION");
		assertMappedType("xsd:double");
		// TODO: Fuzzy match to search for floating-point values
		assertValues(DOUBLE_VALUES, false);
	}
	
	@Test
	public void testChar_3() {
		createMapping("CHAR_3");
		assertMappedType("xsd:string");
		assertValues(new String[]{"", "AOU", "\u00C4\u00D6\u00DC"});
	}
	
	private final static String[] CHAR_VALUES = {"", "A", "\u00C4"};
	@Test
	public void testChar() {
		createMapping("CHAR");
		assertMappedType("xsd:string");
		assertValues(CHAR_VALUES);
	}
	
	@Test
	public void testCharacter() {
		createMapping("CHARACTER");
		assertMappedType("xsd:string");
		assertValues(CHAR_VALUES);
	}
	
	@Test
	public void testNationalCharacter() {
		createMapping("NATIONAL_CHARACTER");
		assertMappedType("xsd:string");
		assertValues(CHAR_VALUES);
	}
	
	@Test
	public void testNChar() {
		createMapping("NCHAR");
		assertMappedType("xsd:string");
		assertValues(CHAR_VALUES);
	}
	
	private final static String[] VARCHAR_VALUES = 
			{"", "   ", "AOU", "\u00C4\u00D6\u00DC"};
	@Test
	public void testVarchar() {
		createMapping("VARCHAR");
		assertMappedType("xsd:string");
		assertValues(VARCHAR_VALUES);
	}
	
	@Test
	public void testNVarchar() {
		createMapping("NVARCHAR");
		assertMappedType("xsd:string");
		assertValues(VARCHAR_VALUES);
	}
	
	@Test
	public void testNationalVarchar() {
		createMapping("NATIONAL_VARCHAR");
		assertMappedType("xsd:string");
		assertValues(VARCHAR_VALUES);
	}
	
	@Test
	public void testTinyText() {
		createMapping("TINYTEXT");
		assertMappedType("xsd:string");
		assertValues(VARCHAR_VALUES);
	}
	
	@Test
	public void testMediumText() {
		createMapping("MEDIUMTEXT");
		assertMappedType("xsd:string");
		assertValues(VARCHAR_VALUES);
	}
	
	@Test
	public void testText() {
		createMapping("TEXT");
		assertMappedType("xsd:string");
		assertValues(VARCHAR_VALUES);
	}
	
	@Test
	public void testLongText() {
		createMapping("LONGTEXT");
		assertMappedType("xsd:string");
		assertValues(VARCHAR_VALUES);
	}
	
	@Test
	public void testBinary_4() {
		createMapping("BINARY_4");
		assertMappedType("xsd:hexBinary");
		assertValues(new String[] {"00000000", "FFFFFFFF", "F001F001"});
	}
	
	@Test
	public void testBinary() {
		createMapping("BINARY");
		assertMappedType("xsd:hexBinary");
		assertValues(new String[] {"00", "01", "FF"});
	}
	
	private final static String[] VARBINARY_VALUES = 
			{"", "00", "01", "F001F001F001F001"};
	@Test
	public void testVarBinary() {
		createMapping("VARBINARY");
		assertMappedType("xsd:hexBinary");
		assertValues(VARBINARY_VALUES);
	}
	
	@Test
	public void testTinyBLOB() {
		createMapping("TINYBLOB");
		assertMappedType("xsd:hexBinary");
		assertValues(VARBINARY_VALUES);
	}
	
	@Test
	public void testMediumBLOB() {
		createMapping("MEDIUMBLOB");
		assertMappedType("xsd:hexBinary");
		assertValues(VARBINARY_VALUES);
	}
	
	@Test
	public void testBLOB() {
		createMapping("BLOB");
		assertMappedType("xsd:hexBinary");
		assertValues(VARBINARY_VALUES);
	}
	
	@Test
	public void testLongBLOB() {
		createMapping("LONGBLOB");
		assertMappedType("xsd:hexBinary");
		assertValues(VARBINARY_VALUES);
	}
	
	@Test
	public void testDate() {
		createMapping("DATE");
		assertMappedType("xsd:date");
		assertValues(new String[] {"1000-01-01", "2012-03-07", 
				"9999-12-31", "1978-11-30", "1978-11-30"});
	}

	@Test
	public void testDateTime() {
		createMapping("DATETIME");
		assertMappedType("xsd:dateTime");
		assertValues(new String[]{
			"1000-01-01T00:00:00", 
			"2012-03-07T20:39:21", 
			"9999-12-31T23:59:59",
			"1978-11-30T00:00:00",
			"1978-11-30T00:00:00"});
	}

	@Test
	public void testTimestamp() {
		createMapping("TIMESTAMP");
		assertMappedType("xsd:dateTime");
		assertValues(new String[]{
			"1970-01-01T00:00:01", 
			"2012-03-07T20:39:21", 
			"2038-01-19T03:14:07"});
	}

	@Test
	public void testTime() {
		createMapping("TIME");
		assertMappedType("xsd:time");
		assertValues(new String[]{"00:00:00", "20:39:21", "23:59:59"});
	}

	@Test
	public void testYear() {
		createMapping("YEAR");
		assertMappedType("xsd:date");
		assertValues(new String[] {"1901-01-01", "2012-01-01", "2155-01-01"}); 
	}

	@Test
	public void testYear4() {
		createMapping("YEAR_4");
		assertMappedType("xsd:date");
		assertValues(new String[] {"1901-01-01", "2012-01-01", "2155-01-01"}); 
	}

	@Test
	public void testYear2() {
		createMapping("YEAR_2");
		assertMappedType("xsd:date");
		assertValues(new String[] {"1970-01-01", "2012-01-01", "2069-01-01"}); 
	}
	
	@Test
	public void testEnum() {
		createMapping("ENUM");
		assertMappedType("xsd:string");
		assertValues(new String[] {"foo", "bar"}); 
	}
	
	@Test
	public void testSet() {
		createMapping("SET");
		assertMappedType("xsd:string");
		assertValues(new String[] {"", "foo", "bar", "foo,bar", "foo,bar"}); 
	}
}