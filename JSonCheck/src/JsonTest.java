import com.google.gson.Gson;
import com.mootion.json.JsonUtil;
import com.mootion.json.exception.JsonCheckException;

public class JsonTest {
	
//	private static String ruleJson = "{\"handleName\":\"测试操作\",\"ruleList\":["
//			+ "{\"key\":\"nextPerson.name\",\"label\":\"姓名\",\"nessecary\":true}"
//			+ "{\"key\":\"name\",\"label\":\"姓名\",\"maxLength\":2},"
//			+ "{\"key\":\"height\",\"label\":\"体重\",\"nessecary\":true},"
//			+ "{\"key\":\"age\",\"type\":\"Integer\",\"minValue\":0,\"valueList\":[11,12,13]}"
//			+ "]}";
	private static String ruleJson = null;
	private static String dataJson = "{\"name\":\"People1\",\"age\":\"18\",\"USER_ID\":\"userid\",\"Orgid\":\"organization\","
			+ "\"nextPerson\":{\"name\":\"People2\"}" + 
			"}";
	public static void main(String[] args) {
		try {
			Person p = JsonUtil.jsonFormart(dataJson, ruleJson, Person.class);
			System.out.println(p);
			System.out.println(new Gson().toJson(p));
		} catch (JsonCheckException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
