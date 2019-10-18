import com.google.gson.Gson;
import com.mootion.json.JsonUtil;
import com.mootion.json.exception.JsonCheckException;

public class JsonTest {
	
	private static String ruleJson = "{\"handleName\":\"测试操作\",\"ruleList\":["
			+ "{\"key\":\"nextPerson.name\",\"label\":\"姓名\",\"necessary\":true},"
			+ "{\"key\":\"name\",\"label\":\"姓名\",\"maxLength\":10},"
//			+ "{\"key\":\"height\",\"label\":\"体重\",\"necessary\":true},"
			+ "{\"key\":\"alive\",\"label\":\"生还\",\"necessary\":true,\"type\":\"Boolean\"},"
			+ "{\"key\":\"birthDay\",\"label\":\"生日\",\"type\":\"Date\",\"format\":\"yyyy/MM/dd\"},"
			+ "{\"key\":\"age\",\"type\":\"Integer\",\"minValue\":0,\"valueList\":[11,12,13,18]}"
			+ "]}";
//	private static String ruleJson = null;
	private static String dataJson = "{\"name\":\"People\",\"age\":\"18\",\"USER_ID\":\"userid\",\"Orgid\":\"organization\","
			+ "\"nextPerson\":{\"name\":\"People2\"},\"birthDay\":\"2019/07/18\",\"alive\":\"true\"" + 
			"}";
	public static void main(String[] args) {
		try {
			Person p = JsonUtil.jsonFormart(dataJson, ruleJson, Person.class, null);
			System.out.println(p);
			System.out.println(new Gson().toJson(p));
		} catch (JsonCheckException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
