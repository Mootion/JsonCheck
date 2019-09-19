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

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", height=" + height + ", USER_ID=" + USER_ID + ", Orgid="
				+ Orgid + ", nextPerson=" + nextPerson + "]";
	}
	
}
