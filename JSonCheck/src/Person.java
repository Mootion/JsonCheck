import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Person {

	private String name;
	
	private Integer age;
	
	private Double height;
	
	private String USER_ID;
	
	@SerializedName("Orgid")
	private String Orgid;
	
	private Person nextPerson;
	
	private String birthDay;
	
	private Boolean alive;
	
	private List<Integer> idList;

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	
}
