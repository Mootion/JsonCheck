import lombok.Data;

@Data
public class Person {

	private String name;
	
	private Integer age;
	
	private Double height;

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", height=" + height + "]";
	}
	
}
